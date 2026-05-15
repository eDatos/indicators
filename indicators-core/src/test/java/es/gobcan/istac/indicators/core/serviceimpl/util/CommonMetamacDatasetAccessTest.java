package es.gobcan.istac.indicators.core.serviceimpl.util;

import static es.gobcan.istac.indicators.core.serviceimpl.util.AttributeInstanceDtoAsserts.assertSingleDimInstance;
import static es.gobcan.istac.indicators.core.serviceimpl.util.AttributeInstanceDtoAsserts.assertTwoDimInstance;
import static es.gobcan.istac.indicators.core.serviceimpl.util.DatasetAccessBuilder.apiInternString;
import static es.gobcan.istac.indicators.core.serviceimpl.util.DatasetAccessBuilder.buildAccess;
import static es.gobcan.istac.indicators.core.serviceimpl.util.DatasetAccessBuilder.buildQueryNoDataAttrs;
import static es.gobcan.istac.indicators.core.serviceimpl.util.DatasetAccessBuilder.dataAttr;
import static es.gobcan.istac.indicators.core.serviceimpl.util.DatasetAccessBuilder.dataAttrs;
import static es.gobcan.istac.indicators.core.serviceimpl.util.DatasetAccessBuilder.datasetAttr;
import static es.gobcan.istac.indicators.core.serviceimpl.util.DatasetAccessBuilder.dim;
import static es.gobcan.istac.indicators.core.serviceimpl.util.DatasetAccessBuilder.dimMap;
import static es.gobcan.istac.indicators.core.serviceimpl.util.DatasetAccessBuilder.dimensionAttr;
import static es.gobcan.istac.indicators.core.serviceimpl.util.DatasetAccessBuilder.dims;
import static es.gobcan.istac.indicators.core.serviceimpl.util.DatasetAccessBuilder.internDataAttr;
import static es.gobcan.istac.indicators.core.serviceimpl.util.DatasetAccessBuilder.internDataAttrs;
import static es.gobcan.istac.indicators.core.serviceimpl.util.DatasetAccessBuilder.metaAttrs;
import static es.gobcan.istac.indicators.core.serviceimpl.util.DatasetAccessBuilder.noDataAttrs;
import static es.gobcan.istac.indicators.core.serviceimpl.util.DatasetAccessBuilder.noInternDataAttrs;
import static es.gobcan.istac.indicators.core.serviceimpl.util.DatasetAccessBuilder.primaryMeasureAttr;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceDto;

/**
 * Unit tests for DATASET-level, DIMENSION-level and GROUP-level attribute instance extraction
 * in {@link CommonMetamacDatasetAccess#extractDatasetAndDimensionAttributeInstances}.
 *
 * <p>Uses {@link QueryMetamacDatasetAccess} as the concrete subclass.
 * No Spring context or DBUnit — JAXB objects are built programmatically via
 * {@link DatasetAccessBuilder}.</p>
 */
public class CommonMetamacDatasetAccessTest {

    private static final String DIM_GEO     = "DIM_GEO";
    private static final String DIM_TIME    = "DIM_TIME";
    private static final String DIM_MEASURE = "DIM_MEASURE";
    private static final String DIM_OTHER   = "DIM_OTHER";

    // =========================================================================
    // DATASET-level attribute tests
    // =========================================================================

    @Test
    public void testDatasetAttrPlainValue() throws MetamacException {
        QueryMetamacDatasetAccess access = buildAccess(
                dims(dim(DIM_TIME, "2022", "2023")),
                metaAttrs(datasetAttr("ATTR_DS")),
                dataAttrs(dataAttr("ATTR_DS", "hello world")),
                noInternDataAttrs());

        List<AttributeInstanceDto> result = access.extractDatasetAndDimensionAttributeInstances(Collections.<String, String>emptyMap());

        assertEquals(1, result.size());
        AttributeInstanceDto inst = result.get(0);
        assertEquals("ATTR_DS", inst.getAttributeId());
        assertEquals("hello world", inst.getValue().getLocalisedLabel("es"));
        assertTrue(inst.getCodesByDimension().isEmpty());
    }

    @Test
    public void testDatasetAttrInternationalValue() throws MetamacException {
        QueryMetamacDatasetAccess access = buildAccess(
                dims(dim(DIM_TIME, "2022")),
                metaAttrs(datasetAttr("ATTR_DS")),
                noDataAttrs(),
                internDataAttrs(internDataAttr("ATTR_DS", apiInternString("es", "valor", "en", "value"))));

        List<AttributeInstanceDto> result = access.extractDatasetAndDimensionAttributeInstances(Collections.<String, String>emptyMap());

        assertEquals(1, result.size());
        AttributeInstanceDto inst = result.get(0);
        assertEquals("ATTR_DS", inst.getAttributeId());
        assertEquals("valor", inst.getValue().getLocalisedLabel("es"));
        assertEquals("value", inst.getValue().getLocalisedLabel("en"));
        assertTrue(inst.getCodesByDimension().isEmpty());
    }

    @Test
    public void testDatasetAttrBlankValueProducesNoInstance() throws MetamacException {
        QueryMetamacDatasetAccess access = buildAccess(
                dims(dim(DIM_TIME, "2022")),
                metaAttrs(datasetAttr("ATTR_DS")),
                dataAttrs(dataAttr("ATTR_DS", "   ")),
                noInternDataAttrs());

        List<AttributeInstanceDto> result = access.extractDatasetAndDimensionAttributeInstances(Collections.<String, String>emptyMap());

        assertTrue(result.isEmpty());
    }

    @Test
    public void testNoDataAttributesProducesEmptyResult() throws MetamacException {
        // Data has no DataAttributes set at all (null) — early return expected
        QueryMetamacDatasetAccess access = new QueryMetamacDatasetAccess(
                buildQueryNoDataAttrs(dims(dim(DIM_TIME, "2022")), metaAttrs(datasetAttr("ATTR_DS"))),
                Collections.<String, String>emptyMap(), null);

        List<AttributeInstanceDto> result = access.extractDatasetAndDimensionAttributeInstances(Collections.<String, String>emptyMap());

        assertTrue(result.isEmpty());
    }

    @Test
    public void testPrimaryMeasureAttrSkipped() throws MetamacException {
        QueryMetamacDatasetAccess access = buildAccess(
                dims(dim(DIM_TIME, "2022")),
                metaAttrs(primaryMeasureAttr("OBS_CONF")),
                dataAttrs(dataAttr("OBS_CONF", "C")),
                noInternDataAttrs());

        List<AttributeInstanceDto> result = access.extractDatasetAndDimensionAttributeInstances(Collections.<String, String>emptyMap());

        assertTrue(result.isEmpty());
    }

    // =========================================================================
    // DIMENSION-level attribute tests
    // =========================================================================

    @Test
    public void testDimensionAttrMappedToGeographical() throws MetamacException {
        QueryMetamacDatasetAccess access = buildAccess(
                dims(dim(DIM_GEO, "ES70", "ES71")),
                metaAttrs(dimensionAttr("ATTR_GEO", DIM_GEO)),
                dataAttrs(dataAttr("ATTR_GEO", "north | south")),
                noInternDataAttrs());

        List<AttributeInstanceDto> result = access.extractDatasetAndDimensionAttributeInstances(dimMap(DIM_GEO, "GEOGRAPHICAL"));

        assertEquals(2, result.size());
        assertSingleDimInstance(result.get(0), "ATTR_GEO", "north", "GEOGRAPHICAL", "ES70");
        assertSingleDimInstance(result.get(1), "ATTR_GEO", "south", "GEOGRAPHICAL", "ES71");
    }

    @Test
    public void testDimensionAttrMappedToTime() throws MetamacException {
        QueryMetamacDatasetAccess access = buildAccess(
                dims(dim(DIM_TIME, "2022", "2023")),
                metaAttrs(dimensionAttr("ATTR_TIME", DIM_TIME)),
                dataAttrs(dataAttr("ATTR_TIME", "val_2022 | val_2023")),
                noInternDataAttrs());

        List<AttributeInstanceDto> result = access.extractDatasetAndDimensionAttributeInstances(dimMap(DIM_TIME, "TIME"));

        assertEquals(2, result.size());
        assertSingleDimInstance(result.get(0), "ATTR_TIME", "val_2022", "TIME", "2022");
        assertSingleDimInstance(result.get(1), "ATTR_TIME", "val_2023", "TIME", "2023");
    }

    @Test
    public void testDimensionAttrMappedToMeasure() throws MetamacException {
        QueryMetamacDatasetAccess access = buildAccess(
                dims(dim(DIM_MEASURE, "ABSOLUTE")),
                metaAttrs(dimensionAttr("ATTR_MEAS", DIM_MEASURE)),
                dataAttrs(dataAttr("ATTR_MEAS", "meas_val")),
                noInternDataAttrs());

        List<AttributeInstanceDto> result = access.extractDatasetAndDimensionAttributeInstances(dimMap(DIM_MEASURE, "MEASURE"));

        assertEquals(1, result.size());
        assertSingleDimInstance(result.get(0), "ATTR_MEAS", "meas_val", "MEASURE", "ABSOLUTE");
    }

    @Test
    public void testDimensionAttrNotMappedProducesEmptyResult() throws MetamacException {
        // DIM_OTHER is not present in sourceDimToIndicatorDim — attribute must be skipped
        QueryMetamacDatasetAccess access = buildAccess(
                dims(dim(DIM_OTHER, "X")),
                metaAttrs(dimensionAttr("ATTR_OTHER", DIM_OTHER)),
                dataAttrs(dataAttr("ATTR_OTHER", "val")),
                noInternDataAttrs());

        List<AttributeInstanceDto> result = access.extractDatasetAndDimensionAttributeInstances(Collections.<String, String>emptyMap());

        assertTrue(result.isEmpty());
    }

    @Test
    public void testDimensionAttrBlankValuesSkipped() throws MetamacException {
        // Position 1 (code "2023") has a blank value — no instance expected for it
        QueryMetamacDatasetAccess access = buildAccess(
                dims(dim(DIM_TIME, "2022", "2023", "2024")),
                metaAttrs(dimensionAttr("ATTR_TIME", DIM_TIME)),
                dataAttrs(dataAttr("ATTR_TIME", "val_2022 |  | val_2024")),
                noInternDataAttrs());

        List<AttributeInstanceDto> result = access.extractDatasetAndDimensionAttributeInstances(dimMap(DIM_TIME, "TIME"));

        assertEquals(2, result.size());
        assertSingleDimInstance(result.get(0), "ATTR_TIME", "val_2022", "TIME", "2022");
        assertSingleDimInstance(result.get(1), "ATTR_TIME", "val_2024", "TIME", "2024");
    }

    @Test
    public void testDimensionAttrInternationalValue() throws MetamacException {
        QueryMetamacDatasetAccess access = buildAccess(
                dims(dim(DIM_GEO, "ES70", "ES71")),
                metaAttrs(dimensionAttr("ATTR_GEO", DIM_GEO)),
                noDataAttrs(),
                internDataAttrs(internDataAttr("ATTR_GEO",
                        apiInternString("es", "Norte", "en", "North"),
                        apiInternString("es", "Sur", "en", "South"))));

        List<AttributeInstanceDto> result = access.extractDatasetAndDimensionAttributeInstances(dimMap(DIM_GEO, "GEOGRAPHICAL"));

        assertEquals(2, result.size());

        AttributeInstanceDto inst0 = result.get(0);
        assertEquals("ATTR_GEO", inst0.getAttributeId());
        assertEquals("Norte", inst0.getValue().getLocalisedLabel("es"));
        assertEquals("North", inst0.getValue().getLocalisedLabel("en"));
        assertEquals(Arrays.asList("ES70"), inst0.getCodesByDimension().get("GEOGRAPHICAL"));

        AttributeInstanceDto inst1 = result.get(1);
        assertEquals("Sur", inst1.getValue().getLocalisedLabel("es"));
        assertEquals(Arrays.asList("ES71"), inst1.getCodesByDimension().get("GEOGRAPHICAL"));
    }

    // =========================================================================
    // GROUP-level attribute tests
    // =========================================================================

    @Test
    public void testGroupAttrGeoTime() throws MetamacException {
        // GROUP [DIM_GEO, DIM_TIME] — global dim order: GEO first, TIME second
        // Cartesian product (GEO varies slowest): (ES70,2022) (ES70,2023) (ES71,2022) (ES71,2023)
        QueryMetamacDatasetAccess access = buildAccess(
                dims(dim(DIM_GEO, "ES70", "ES71"), dim(DIM_TIME, "2022", "2023")),
                metaAttrs(dimensionAttr("ATTR_GROUP", DIM_GEO, DIM_TIME)),
                dataAttrs(dataAttr("ATTR_GROUP", "v_70_22 | v_70_23 | v_71_22 | v_71_23")),
                noInternDataAttrs());

        List<AttributeInstanceDto> result = access.extractDatasetAndDimensionAttributeInstances(dimMap(DIM_GEO, "GEOGRAPHICAL", DIM_TIME, "TIME"));

        assertEquals(4, result.size());
        assertTwoDimInstance(result.get(0), "ATTR_GROUP", "v_70_22", "GEOGRAPHICAL", "ES70", "TIME", "2022");
        assertTwoDimInstance(result.get(1), "ATTR_GROUP", "v_70_23", "GEOGRAPHICAL", "ES70", "TIME", "2023");
        assertTwoDimInstance(result.get(2), "ATTR_GROUP", "v_71_22", "GEOGRAPHICAL", "ES71", "TIME", "2022");
        assertTwoDimInstance(result.get(3), "ATTR_GROUP", "v_71_23", "GEOGRAPHICAL", "ES71", "TIME", "2023");
    }

    @Test
    public void testGroupAttrGeoMeasure() throws MetamacException {
        QueryMetamacDatasetAccess access = buildAccess(
                dims(dim(DIM_GEO, "ES70", "ES71"), dim(DIM_MEASURE, "ABSOLUTE")),
                metaAttrs(dimensionAttr("ATTR_GROUP", DIM_GEO, DIM_MEASURE)),
                dataAttrs(dataAttr("ATTR_GROUP", "v_70_abs | v_71_abs")),
                noInternDataAttrs());

        List<AttributeInstanceDto> result = access.extractDatasetAndDimensionAttributeInstances(dimMap(DIM_GEO, "GEOGRAPHICAL", DIM_MEASURE, "MEASURE"));

        assertEquals(2, result.size());
        assertTwoDimInstance(result.get(0), "ATTR_GROUP", "v_70_abs", "GEOGRAPHICAL", "ES70", "MEASURE", "ABSOLUTE");
        assertTwoDimInstance(result.get(1), "ATTR_GROUP", "v_71_abs", "GEOGRAPHICAL", "ES71", "MEASURE", "ABSOLUTE");
    }

    @Test
    public void testGroupAttrTimeMeasure() throws MetamacException {
        // GROUP [DIM_TIME, DIM_MEASURE] — TIME varies slowest
        // Cartesian product: (2022,ABS) (2022,RATE) (2023,ABS) (2023,RATE)
        QueryMetamacDatasetAccess access = buildAccess(
                dims(dim(DIM_TIME, "2022", "2023"), dim(DIM_MEASURE, "ABSOLUTE", "RATE")),
                metaAttrs(dimensionAttr("ATTR_GROUP", DIM_TIME, DIM_MEASURE)),
                dataAttrs(dataAttr("ATTR_GROUP", "v_22_abs | v_22_rate | v_23_abs | v_23_rate")),
                noInternDataAttrs());

        List<AttributeInstanceDto> result = access.extractDatasetAndDimensionAttributeInstances(dimMap(DIM_TIME, "TIME", DIM_MEASURE, "MEASURE"));

        assertEquals(4, result.size());
        assertTwoDimInstance(result.get(0), "ATTR_GROUP", "v_22_abs", "TIME", "2022", "MEASURE", "ABSOLUTE");
        assertTwoDimInstance(result.get(1), "ATTR_GROUP", "v_22_rate", "TIME", "2022", "MEASURE", "RATE");
        assertTwoDimInstance(result.get(2), "ATTR_GROUP", "v_23_abs", "TIME", "2023", "MEASURE", "ABSOLUTE");
        assertTwoDimInstance(result.get(3), "ATTR_GROUP", "v_23_rate", "TIME", "2023", "MEASURE", "RATE");
    }

    @Test
    public void testGroupAttrGeoTimeInternational() throws MetamacException {
        QueryMetamacDatasetAccess access = buildAccess(
                dims(dim(DIM_GEO, "ES70"), dim(DIM_TIME, "2022")),
                metaAttrs(dimensionAttr("ATTR_GROUP", DIM_GEO, DIM_TIME)),
                noDataAttrs(),
                internDataAttrs(internDataAttr("ATTR_GROUP", apiInternString("es", "valor", "en", "value"))));

        List<AttributeInstanceDto> result = access.extractDatasetAndDimensionAttributeInstances(dimMap(DIM_GEO, "GEOGRAPHICAL", DIM_TIME, "TIME"));

        assertEquals(1, result.size());
        AttributeInstanceDto inst = result.get(0);
        assertEquals("ATTR_GROUP", inst.getAttributeId());
        assertEquals("valor", inst.getValue().getLocalisedLabel("es"));
        assertEquals("value", inst.getValue().getLocalisedLabel("en"));
        assertEquals(Arrays.asList("ES70"), inst.getCodesByDimension().get("GEOGRAPHICAL"));
        assertEquals(Arrays.asList("2022"), inst.getCodesByDimension().get("TIME"));
    }

    @Test
    public void testGroupAttrUnmappedDimensionProducesEmptyResult() throws MetamacException {
        // Group spans DIM_GEO and DIM_OTHER; DIM_OTHER is not in sourceDimToIndicatorDim
        QueryMetamacDatasetAccess access = buildAccess(
                dims(dim(DIM_GEO, "ES70", "ES71"), dim(DIM_OTHER, "X")),
                metaAttrs(dimensionAttr("ATTR_GROUP", DIM_GEO, DIM_OTHER)),
                dataAttrs(dataAttr("ATTR_GROUP", "val1 | val2")),
                noInternDataAttrs());

        List<AttributeInstanceDto> result = access.extractDatasetAndDimensionAttributeInstances(dimMap(DIM_GEO, "GEOGRAPHICAL"));

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGroupAttrBlankValueSkipped() throws MetamacException {
        // 2×2 group: position 1 (ES70/2023) has blank value — only 3 instances expected
        QueryMetamacDatasetAccess access = buildAccess(
                dims(dim(DIM_GEO, "ES70", "ES71"), dim(DIM_TIME, "2022", "2023")),
                metaAttrs(dimensionAttr("ATTR_GROUP", DIM_GEO, DIM_TIME)),
                dataAttrs(dataAttr("ATTR_GROUP", "v_70_22 |  | v_71_22 | v_71_23")),
                noInternDataAttrs());

        List<AttributeInstanceDto> result = access.extractDatasetAndDimensionAttributeInstances(dimMap(DIM_GEO, "GEOGRAPHICAL", DIM_TIME, "TIME"));

        assertEquals(3, result.size());
        assertTwoDimInstance(result.get(0), "ATTR_GROUP", "v_70_22", "GEOGRAPHICAL", "ES70", "TIME", "2022");
        assertTwoDimInstance(result.get(1), "ATTR_GROUP", "v_71_22", "GEOGRAPHICAL", "ES71", "TIME", "2022");
        assertTwoDimInstance(result.get(2), "ATTR_GROUP", "v_71_23", "GEOGRAPHICAL", "ES71", "TIME", "2023");
    }
}
