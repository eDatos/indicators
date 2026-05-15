package es.gobcan.istac.indicators.rest.mapper;

import static es.gobcan.istac.indicators.rest.test.utils.IndicatorsRestAsserts.assertAttributeDefault;
import static es.gobcan.istac.indicators.rest.test.utils.IndicatorsRestAsserts.assertI18nValue;
import static es.gobcan.istac.indicators.rest.test.utils.IndicatorsRestAsserts.assertI18nValueNull;
import static es.gobcan.istac.indicators.rest.test.utils.IndicatorsRestMocks.mockDatasetAttr;
import static es.gobcan.istac.indicators.rest.test.utils.IndicatorsRestMocks.mockDatasetI18nAttr;
import static es.gobcan.istac.indicators.rest.test.utils.IndicatorsRestMocks.mockDimAttr;
import static es.gobcan.istac.indicators.rest.test.utils.IndicatorsRestMocks.mockDimI18nAttr;
import static es.gobcan.istac.indicators.rest.test.utils.IndicatorsRestMocks.mockGroupAttr;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceDto;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorDataDimensionTypeEnum;
import es.gobcan.istac.indicators.rest.types.InternationalDataAttributeType;

/**
 * Unit tests for the private method {@code buildAttributes}
 * in {@link Do2TypeMapperImpl}.
 *
 * <p>No Spring context needed — the method under test only manipulates DTOs and constants.
 * Private access is obtained via reflection.</p>
 */
public class Do2TypeMapperImplTest {

    private static final String GEO          = IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name();
    private static final String MEASURE      = IndicatorDataDimensionTypeEnum.MEASURE.name();
    private static final String LANG_DEFAULT = "es";

    // =========================================================================
    // DATASET-level — non-i18n
    // =========================================================================

    @Test
    public void testDatasetNonI18n_singleValue() throws Exception {
        List<AttributeInstanceDto> attrs = Arrays.asList(
                mockDatasetAttr("TIPO_PROCESO", "MUNICIPALES"));

        List<InternationalDataAttributeType> result = invoke(attrs, noI18n(), request(geo("ES70", "ES71"), time("2022"), measure("ABSOLUTE")));

        assertEquals(1, result.size());
        assertEquals("TIPO_PROCESO", result.get(0).getId());
        assertEquals(1, result.get(0).getValue().size());
        assertAttributeDefault("MUNICIPALES", result.get(0), 0);
    }

    @Test
    public void testDatasetNonI18n_multipleAttrs() throws Exception {
        List<AttributeInstanceDto> attrs = Arrays.asList(
                mockDatasetAttr("FECHA_ELECCION", "2019-05-26"),
                mockDatasetAttr("TIPO_PROCESO", "MUNICIPALES"));

        List<InternationalDataAttributeType> result = invoke(attrs, noI18n(), request(geo("ES70"), time("2022"), measure("ABSOLUTE")));

        assertEquals(2, result.size());
        assertEquals("FECHA_ELECCION", result.get(0).getId());
        assertAttributeDefault("2019-05-26", result.get(0), 0);
        assertEquals("TIPO_PROCESO", result.get(1).getId());
        assertAttributeDefault("MUNICIPALES", result.get(1), 0);
    }

    // =========================================================================
    // DATASET-level — i18n
    // =========================================================================

    @Test
    public void testDatasetI18n_allLocales() throws Exception {
        List<AttributeInstanceDto> attrs = Arrays.asList(
                mockDatasetI18nAttr("NOTAS_DS", "nota es", "nota ca", "note en"));

        List<InternationalDataAttributeType> result = invoke(attrs, i18nIds("NOTAS_DS"), request(geo("ES70"), time("2022"), measure("ABSOLUTE")));

        assertEquals(1, result.size());
        assertEquals("NOTAS_DS", result.get(0).getId());
        assertEquals(1, result.get(0).getValue().size());
        assertI18nValue(result.get(0).getValue(), 0, "es", "nota es", "ca", "nota ca", "en", "note en", "__default__", "nota es");
    }

    // =========================================================================
    // DIMENSION-level — non-i18n (GEOGRAPHICAL)
    // =========================================================================

    @Test
    public void testDimensionGeoNonI18n_allPositionsFilled() throws Exception {
        List<AttributeInstanceDto> attrs = Arrays.asList(
                mockDimAttr("CAMPO_GEO", GEO, "ES70", "val_ES70"),
                mockDimAttr("CAMPO_GEO", GEO, "ES71", "val_ES71"));

        List<InternationalDataAttributeType> result = invoke(attrs, noI18n(), request(geo("ES70", "ES71"), time("2022"), measure("ABSOLUTE")));

        assertEquals(1, result.size());
        assertEquals("CAMPO_GEO", result.get(0).getId());
        assertEquals(2, result.get(0).getValue().size());
        assertAttributeDefault("val_ES70", result.get(0), 0);
        assertAttributeDefault("val_ES71", result.get(0), 1);
    }

    @Test
    public void testDimensionGeoNonI18n_sparseValues() throws Exception {
        // Only ES70 has a value — ES71 position is null
        List<AttributeInstanceDto> attrs = Arrays.asList(
                mockDimAttr("CAMPO_GEO", GEO, "ES70", "val_ES70"));

        List<InternationalDataAttributeType> result = invoke(attrs, noI18n(), request(geo("ES70", "ES71"), time("2022"), measure("ABSOLUTE")));

        assertEquals(1, result.size());
        assertEquals("CAMPO_GEO", result.get(0).getId());
        assertEquals(2, result.get(0).getValue().size());
        assertAttributeDefault("val_ES70", result.get(0), 0);
        assertI18nValueNull(result.get(0).getValue(), 1);
    }

    // =========================================================================
    // DIMENSION-level — i18n (MEASURE)
    // =========================================================================

    @Test
    public void testDimensionMeasureI18n_sparseValues() throws Exception {
        // ABSOLUTE has a value; ANNUAL_PUNTUAL_RATE is absent → null at position 1
        List<AttributeInstanceDto> attrs = Arrays.asList(
                mockDimI18nAttr("FIELD_MEDIDA", MEASURE, "ABSOLUTE", "nota abs", "abs note"));

        List<InternationalDataAttributeType> result = invoke(attrs, i18nIds("FIELD_MEDIDA"),
                request(geo("ES70"), time("2022"), measure("ABSOLUTE", "ANNUAL_PUNTUAL_RATE")));

        assertEquals(1, result.size());
        assertEquals("FIELD_MEDIDA", result.get(0).getId());
        assertEquals(2, result.get(0).getValue().size());
        assertI18nValue(result.get(0).getValue(), 0, "es", "nota abs", "en", "abs note", "__default__", "nota abs");
        assertI18nValueNull(result.get(0).getValue(), 1);
    }

    // =========================================================================
    // GROUP-level (GEO x MEASURE cartesian product)
    // =========================================================================

    @Test
    public void testGroupGeoMeasureNonI18n_cartesianProduct() throws Exception {
        // geo=[ES70, ES71], measure=[ABSOLUTE, ANNUAL_PUNTUAL_RATE]
        // flat positions: (ES70,ABS)=0, (ES70,ANN)=1, (ES71,ABS)=2, (ES71,ANN)=3
        List<AttributeInstanceDto> attrs = Arrays.asList(
                mockGroupAttr("CAMPO_GROUP", GEO, "ES70", MEASURE, "ABSOLUTE",            "v0"),
                mockGroupAttr("CAMPO_GROUP", GEO, "ES71", MEASURE, "ANNUAL_PUNTUAL_RATE", "v3"));

        List<InternationalDataAttributeType> result = invoke(attrs, noI18n(),
                request(geo("ES70", "ES71"), time("2022"), measure("ABSOLUTE", "ANNUAL_PUNTUAL_RATE")));

        assertEquals(1, result.size());
        assertEquals("CAMPO_GROUP", result.get(0).getId());
        assertEquals(4, result.get(0).getValue().size());
        assertAttributeDefault("v0", result.get(0), 0);
        assertI18nValueNull(result.get(0).getValue(), 1);
        assertI18nValueNull(result.get(0).getValue(), 2);
        assertAttributeDefault("v3", result.get(0), 3);
    }

    // =========================================================================
    // Reflection invocation helper
    // =========================================================================

    @SuppressWarnings("unchecked")
    private List<InternationalDataAttributeType> invoke(List<AttributeInstanceDto> attrs, Set<String> i18nIds,
            DataTypeRequest req) throws Exception {
        Do2TypeMapperImpl mapper = new Do2TypeMapperImpl();
        Method method = Do2TypeMapperImpl.class.getDeclaredMethod(
                "buildAttributes",
                List.class, Set.class, DataTypeRequest.class, String.class);
        method.setAccessible(true);
        return (List<InternationalDataAttributeType>) method.invoke(mapper, attrs, i18nIds, req, LANG_DEFAULT);
    }

    // =========================================================================
    // Request builders
    // =========================================================================

    private static DataTypeRequest request(List<String> geo, List<String> time, List<String> measure) {
        return new DataTypeRequest((IndicatorVersion) null, geo, time, measure, null);
    }

    private static List<String> geo(String... codes)     { return Arrays.asList(codes); }
    private static List<String> time(String... codes)    { return Arrays.asList(codes); }
    private static List<String> measure(String... codes) { return Arrays.asList(codes); }

    private static Set<String> noI18n()               { return new HashSet<String>(); }
    private static Set<String> i18nIds(String... ids) { return new HashSet<String>(Arrays.asList(ids)); }
}
