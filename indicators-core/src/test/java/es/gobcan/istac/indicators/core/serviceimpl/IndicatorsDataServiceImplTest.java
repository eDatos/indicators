package es.gobcan.istac.indicators.core.serviceimpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceDto;
import es.gobcan.istac.edatos.dataset.repository.dto.InternationalStringDto;
import es.gobcan.istac.edatos.dataset.repository.dto.LocalisedStringDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorDataDimensionTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.MeasureDimensionTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.RateDerivationMethodTypeEnum;
import es.gobcan.istac.indicators.core.serviceimpl.util.DataOperation;

/**
 * Unit tests for the private methods {@code buildSourceMeasureToIndicatorMeasureMap}
 * and {@code translateMeasureCodes} in {@link IndicatorsDataServiceImpl}.
 *
 * <p>No Spring context needed — the methods under test only manipulate DTOs and enums.
 * {@link DataOperation} is mocked with Mockito. Private access is obtained via reflection.</p>
 */
public class IndicatorsDataServiceImplTest {

    private static final String MEASURE_DIM = IndicatorDataDimensionTypeEnum.MEASURE.name();
    private static final String GEO_DIM     = IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name();

    // =========================================================================
    // buildSourceMeasureToIndicatorMeasureMap
    // =========================================================================

    @Test
    public void testBuildMap_singleLoadOperation() throws Exception {
        DataOperation op = loadOp("VOTOS", MeasureDimensionTypeEnum.ABSOLUTE);

        Map<String, List<String>> result = invokeMap(Arrays.asList(op));

        assertEquals(1, result.size());
        assertEquals(Arrays.asList("ABSOLUTE"), result.get("VOTOS"));
    }

    @Test
    public void testBuildMap_multipleLoadsFromSameSourceCode() throws Exception {
        // Two measures both loaded from the same source code
        DataOperation op1 = loadOp("VOTOS", MeasureDimensionTypeEnum.ABSOLUTE);
        DataOperation op2 = loadOp("VOTOS", MeasureDimensionTypeEnum.ANNUAL_PUNTUAL_RATE);

        Map<String, List<String>> result = invokeMap(Arrays.asList(op1, op2));

        assertEquals(1, result.size());
        List<String> measures = result.get("VOTOS");
        assertEquals(2, measures.size());
        assertTrue(measures.contains("ABSOLUTE"));
        assertTrue(measures.contains("ANNUAL_PUNTUAL_RATE"));
    }

    @Test
    public void testBuildMap_calculateOperationIsIgnored() throws Exception {
        DataOperation calculateOp = calculateOp(MeasureDimensionTypeEnum.ANNUAL_PERCENTAGE_RATE);

        Map<String, List<String>> result = invokeMap(Arrays.asList(calculateOp));

        assertTrue(result.isEmpty());
    }

    @Test
    public void testBuildMap_mixedLoadAndCalculate() throws Exception {
        DataOperation loadOp      = loadOp("DIPUTADOS", MeasureDimensionTypeEnum.ABSOLUTE);
        DataOperation calculateOp = calculateOp(MeasureDimensionTypeEnum.ANNUAL_PERCENTAGE_RATE);

        Map<String, List<String>> result = invokeMap(Arrays.asList(loadOp, calculateOp));

        assertEquals(1, result.size());
        assertEquals(Arrays.asList("ABSOLUTE"), result.get("DIPUTADOS"));
    }

    @Test
    public void testBuildMap_emptyOperations() throws Exception {
        Map<String, List<String>> result = invokeMap(Collections.<DataOperation>emptyList());

        assertTrue(result.isEmpty());
    }

    // =========================================================================
    // translateMeasureCodes
    // =========================================================================

    @Test
    public void testTranslate_emptyMapReturnsOriginalList() throws Exception {
        // No LOAD operations → map is empty → original list returned unchanged
        List<AttributeInstanceDto> attrs = Arrays.asList(measureAttr("ATTR", "VOTOS", "valor"));

        List<AttributeInstanceDto> result = invokeTranslate(attrs, Collections.<DataOperation>emptyList());

        assertSame(attrs, result);
    }

    @Test
    public void testTranslate_datasetLevelInstancePassesThrough() throws Exception {
        // DATASET-level instance (codesByDimension empty) is not touched
        AttributeInstanceDto datasetInst = datasetAttr("ATTR_DS", "value");
        DataOperation op = loadOp("VOTOS", MeasureDimensionTypeEnum.ABSOLUTE);

        List<AttributeInstanceDto> result = invokeTranslate(
                Arrays.asList(datasetInst), Arrays.asList(op));

        assertEquals(1, result.size());
        assertSame(datasetInst, result.get(0));
    }

    @Test
    public void testTranslate_geographicalOnlyInstancePassesThrough() throws Exception {
        // Instance with only GEOGRAPHICAL dimension is not touched
        AttributeInstanceDto geoInst = dimAttr("ATTR_GEO", GEO_DIM, "ES70", "val");
        DataOperation op = loadOp("VOTOS", MeasureDimensionTypeEnum.ABSOLUTE);

        List<AttributeInstanceDto> result = invokeTranslate(
                Arrays.asList(geoInst), Arrays.asList(op));

        assertEquals(1, result.size());
        assertSame(geoInst, result.get(0));
    }

    @Test
    public void testTranslate_sourceCodeExpandsToOneIndicatorMeasure() throws Exception {
        // "DIPUTADOS" → ABSOLUTE only
        AttributeInstanceDto inst = measureAttr("ATTR", "DIPUTADOS", "valor");
        DataOperation op = loadOp("DIPUTADOS", MeasureDimensionTypeEnum.ABSOLUTE);

        List<AttributeInstanceDto> result = invokeTranslate(Arrays.asList(inst), Arrays.asList(op));

        assertEquals(1, result.size());
        assertEquals("ATTR", result.get(0).getAttributeId());
        assertEquals(Arrays.asList("ABSOLUTE"), result.get(0).getCodesByDimension().get(MEASURE_DIM));
    }

    @Test
    public void testTranslate_sourceCodeExpandsToMultipleIndicatorMeasures() throws Exception {
        // "VOTOS" loads ABSOLUTE and ANNUAL_PUNTUAL_RATE → one instance becomes two
        AttributeInstanceDto inst = measureAttr("ATTR", "VOTOS", "valor");
        DataOperation op1 = loadOp("VOTOS", MeasureDimensionTypeEnum.ABSOLUTE);
        DataOperation op2 = loadOp("VOTOS", MeasureDimensionTypeEnum.ANNUAL_PUNTUAL_RATE);

        List<AttributeInstanceDto> result = invokeTranslate(
                Arrays.asList(inst), Arrays.asList(op1, op2));

        assertEquals(2, result.size());
        List<String> measureCodes = new ArrayList<String>();
        measureCodes.add(result.get(0).getCodesByDimension().get(MEASURE_DIM).get(0));
        measureCodes.add(result.get(1).getCodesByDimension().get(MEASURE_DIM).get(0));
        assertTrue(measureCodes.contains("ABSOLUTE"));
        assertTrue(measureCodes.contains("ANNUAL_PUNTUAL_RATE"));
        // Both expanded instances carry the original value
        assertEquals("valor", result.get(0).getValue().getLocalisedLabel("es"));
        assertEquals("valor", result.get(1).getValue().getLocalisedLabel("es"));
    }

    @Test
    public void testTranslate_unmappedSourceCodeIsSkipped() throws Exception {
        // Instance has measure code "UNKNOWN" which is not in any LOAD operation
        AttributeInstanceDto inst = measureAttr("ATTR", "UNKNOWN", "valor");
        DataOperation op = loadOp("DIPUTADOS", MeasureDimensionTypeEnum.ABSOLUTE);

        List<AttributeInstanceDto> result = invokeTranslate(Arrays.asList(inst), Arrays.asList(op));

        assertTrue(result.isEmpty());
    }

    // =========================================================================
    // Reflection helpers
    // =========================================================================

    @SuppressWarnings("unchecked")
    private Map<String, List<String>> invokeMap(List<DataOperation> ops) throws Exception {
        IndicatorsDataServiceImpl service = new IndicatorsDataServiceImpl();
        Method method = IndicatorsDataServiceImpl.class.getDeclaredMethod(
                "buildSourceMeasureToIndicatorMeasureMap", List.class);
        method.setAccessible(true);
        return (Map<String, List<String>>) method.invoke(service, ops);
    }

    @SuppressWarnings("unchecked")
    private List<AttributeInstanceDto> invokeTranslate(List<AttributeInstanceDto> attrs,
            List<DataOperation> ops) throws Exception {
        IndicatorsDataServiceImpl service = new IndicatorsDataServiceImpl();
        Method method = IndicatorsDataServiceImpl.class.getDeclaredMethod(
                "translateMeasureCodes", List.class, List.class);
        method.setAccessible(true);
        return (List<AttributeInstanceDto>) method.invoke(service, attrs, ops);
    }

    // =========================================================================
    // DataOperation mock builders
    // =========================================================================

    private static DataOperation loadOp(String sourceCode, MeasureDimensionTypeEnum measure) {
        DataOperation op = mock(DataOperation.class);
        when(op.getMethodType()).thenReturn(RateDerivationMethodTypeEnum.LOAD);
        when(op.getMethod()).thenReturn(sourceCode);
        when(op.getMeasureDimension()).thenReturn(measure);
        return op;
    }

    private static DataOperation calculateOp(MeasureDimensionTypeEnum measure) {
        DataOperation op = mock(DataOperation.class);
        when(op.getMethodType()).thenReturn(RateDerivationMethodTypeEnum.CALCULATE);
        when(op.getMeasureDimension()).thenReturn(measure);
        return op;
    }

    // =========================================================================
    // AttributeInstanceDto builders
    // =========================================================================

    private static AttributeInstanceDto datasetAttr(String id, String esValue) {
        AttributeInstanceDto inst = new AttributeInstanceDto();
        inst.setAttributeId(id);
        inst.setValue(isDto("es", esValue));
        inst.setCodesByDimension(new HashMap<String, List<String>>());
        return inst;
    }

    private static AttributeInstanceDto dimAttr(String id, String dimId, String code, String esValue) {
        AttributeInstanceDto inst = new AttributeInstanceDto();
        inst.setAttributeId(id);
        inst.setValue(isDto("es", esValue));
        Map<String, List<String>> codes = new HashMap<String, List<String>>();
        codes.put(dimId, Arrays.asList(code));
        inst.setCodesByDimension(codes);
        return inst;
    }

    /**
     * Creates an instance with a MEASURE dimension code matching a source dataset code
     * (before translation).
     */
    private static AttributeInstanceDto measureAttr(String id, String sourceMeasureCode, String esValue) {
        return dimAttr(id, MEASURE_DIM, sourceMeasureCode, esValue);
    }

    private static InternationalStringDto isDto(String locale, String label) {
        InternationalStringDto dto = new InternationalStringDto();
        dto.addText(new LocalisedStringDto(locale, label));
        return dto;
    }
}
