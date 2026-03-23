package es.gobcan.istac.indicators.rest.mapper;

import static es.gobcan.istac.indicators.rest.test.utils.IndicatorsRestAsserts.assertDataAttribute;
import static es.gobcan.istac.indicators.rest.test.utils.IndicatorsRestAsserts.assertI18nValue;
import static es.gobcan.istac.indicators.rest.test.utils.IndicatorsRestAsserts.assertI18nValueNull;
import static es.gobcan.istac.indicators.rest.test.utils.IndicatorsRestAsserts.assertInternationalAttribute;
import static es.gobcan.istac.indicators.rest.test.utils.IndicatorsRestMocks.mockDatasetAttr;
import static es.gobcan.istac.indicators.rest.test.utils.IndicatorsRestMocks.mockDatasetI18nAttr;
import static es.gobcan.istac.indicators.rest.test.utils.IndicatorsRestMocks.mockDimAttr;
import static es.gobcan.istac.indicators.rest.test.utils.IndicatorsRestMocks.mockDimI18nAttr;
import static es.gobcan.istac.indicators.rest.test.utils.IndicatorsRestMocks.mockGroupAttr;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceDto;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorDataDimensionTypeEnum;
import es.gobcan.istac.indicators.rest.types.DataAttributeType;
import es.gobcan.istac.indicators.rest.types.InternationalDataAttributeType;

/**
 * Unit tests for the private method {@code buildDatasetAndInternationalAttributeLists}
 * in {@link Do2TypeMapperImpl}.
 *
 * <p>No Spring context needed — the method under test only manipulates DTOs and constants.
 * Private access is obtained via reflection.</p>
 */
public class Do2TypeMapperImplTest {

    private static final String GEO     = IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name();
    private static final String MEASURE = IndicatorDataDimensionTypeEnum.MEASURE.name();

    // =========================================================================
    // DATASET-level — non-i18n
    // =========================================================================

    @Test
    public void testDatasetNonI18n_singleValue() throws Exception {
        List<AttributeInstanceDto> attrs = Arrays.asList(
                mockDatasetAttr("TIPO_PROCESO", "MUNICIPALES"));

        Result r = invoke(attrs, noI18n(), request(geo("ES70", "ES71"), time("2022"), measure("ABSOLUTE")));

        assertEquals(1, r.nonI18n.size());
        assertDataAttribute("TIPO_PROCESO", "MUNICIPALES", r.nonI18n.get(0));
        assertTrue(r.i18n.isEmpty());
    }

    @Test
    public void testDatasetNonI18n_multipleAttrs() throws Exception {
        List<AttributeInstanceDto> attrs = Arrays.asList(
                mockDatasetAttr("FECHA_ELECCION", "2019-05-26"),
                mockDatasetAttr("TIPO_PROCESO", "MUNICIPALES"));

        Result r = invoke(attrs, noI18n(), request(geo("ES70"), time("2022"), measure("ABSOLUTE")));

        assertEquals(2, r.nonI18n.size());
        assertDataAttribute("FECHA_ELECCION", "2019-05-26", r.nonI18n.get(0));
        assertDataAttribute("TIPO_PROCESO",   "MUNICIPALES", r.nonI18n.get(1));
    }

    // =========================================================================
    // DATASET-level — i18n
    // =========================================================================

    @Test
    public void testDatasetI18n_allLocales() throws Exception {
        List<AttributeInstanceDto> attrs = Arrays.asList(
                mockDatasetI18nAttr("NOTAS_DS", "nota es", "nota ca", "note en"));

        Result r = invoke(attrs, i18nIds("NOTAS_DS"), request(geo("ES70"), time("2022"), measure("ABSOLUTE")));

        assertTrue(r.nonI18n.isEmpty());
        assertInternationalAttribute("NOTAS_DS", 1, r.i18n.get(0));
        assertI18nValue(r.i18n.get(0).getValues(), 0, "es", "nota es", "ca", "nota ca", "en", "note en");
    }

    // =========================================================================
    // DIMENSION-level — non-i18n (GEOGRAPHICAL)
    // =========================================================================

    @Test
    public void testDimensionGeoNonI18n_allPositionsFilled() throws Exception {
        List<AttributeInstanceDto> attrs = Arrays.asList(
                mockDimAttr("CAMPO_GEO", GEO, "ES70", "val_ES70"),
                mockDimAttr("CAMPO_GEO", GEO, "ES71", "val_ES71"));

        Result r = invoke(attrs, noI18n(), request(geo("ES70", "ES71"), time("2022"), measure("ABSOLUTE")));

        assertEquals(1, r.nonI18n.size());
        assertDataAttribute("CAMPO_GEO", "val_ES70 | val_ES71", r.nonI18n.get(0));
        assertTrue(r.i18n.isEmpty());
    }

    @Test
    public void testDimensionGeoNonI18n_sparseValues() throws Exception {
        // Only ES70 has a value — ES71 position produces an empty string
        List<AttributeInstanceDto> attrs = Arrays.asList(
                mockDimAttr("CAMPO_GEO", GEO, "ES70", "val_ES70"));

        Result r = invoke(attrs, noI18n(), request(geo("ES70", "ES71"), time("2022"), measure("ABSOLUTE")));

        assertEquals(1, r.nonI18n.size());
        assertDataAttribute("CAMPO_GEO", "val_ES70 | ", r.nonI18n.get(0));
    }

    // =========================================================================
    // DIMENSION-level — i18n (MEASURE)
    // =========================================================================

    @Test
    public void testDimensionMeasureI18n_sparseValues() throws Exception {
        // ABSOLUTE has a value; ANNUAL_PUNTUAL_RATE is absent → null at position 1
        List<AttributeInstanceDto> attrs = Arrays.asList(
                mockDimI18nAttr("FIELD_MEDIDA", MEASURE, "ABSOLUTE", "nota abs", "abs note"));

        Result r = invoke(attrs, i18nIds("FIELD_MEDIDA"),
                request(geo("ES70"), time("2022"), measure("ABSOLUTE", "ANNUAL_PUNTUAL_RATE")));

        assertTrue(r.nonI18n.isEmpty());
        assertInternationalAttribute("FIELD_MEDIDA", 2, r.i18n.get(0));
        assertI18nValue(r.i18n.get(0).getValues(),    0, "es", "nota abs", "en", "abs note");
        assertI18nValueNull(r.i18n.get(0).getValues(), 1);
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

        Result r = invoke(attrs, noI18n(),
                request(geo("ES70", "ES71"), time("2022"), measure("ABSOLUTE", "ANNUAL_PUNTUAL_RATE")));

        assertEquals(1, r.nonI18n.size());
        // pos0=v0, pos1=empty, pos2=empty, pos3=v3
        assertDataAttribute("CAMPO_GROUP", "v0 |  |  | v3", r.nonI18n.get(0));
        assertTrue(r.i18n.isEmpty());
    }

    // =========================================================================
    // Reflection invocation helper
    // =========================================================================

    private Result invoke(List<AttributeInstanceDto> attrs, Set<String> i18nIds,
            DataTypeRequest req) throws Exception {
        Do2TypeMapperImpl mapper = new Do2TypeMapperImpl();
        Method method = Do2TypeMapperImpl.class.getDeclaredMethod(
                "buildDatasetAndInternationalAttributeLists",
                List.class, Set.class, DataTypeRequest.class, List.class, List.class);
        method.setAccessible(true);

        List<DataAttributeType> nonI18n = new ArrayList<DataAttributeType>();
        List<InternationalDataAttributeType> i18n = new ArrayList<InternationalDataAttributeType>();
        method.invoke(mapper, attrs, i18nIds, req, nonI18n, i18n);
        return new Result(nonI18n, i18n);
    }

    /** Holds the two output lists produced by the method under test. */
    private static class Result {
        final List<DataAttributeType>              nonI18n;
        final List<InternationalDataAttributeType> i18n;

        Result(List<DataAttributeType> nonI18n, List<InternationalDataAttributeType> i18n) {
            this.nonI18n = nonI18n;
            this.i18n    = i18n;
        }
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

    private static Set<String> noI18n()            { return new HashSet<String>(); }
    private static Set<String> i18nIds(String... ids) { return new HashSet<String>(Arrays.asList(ids)); }
}
