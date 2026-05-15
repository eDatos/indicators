package es.gobcan.istac.indicators.core.serviceimpl.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceDto;
import es.gobcan.istac.indicators.core.constants.IndicatorsConstants;

/**
 * Static assertion helpers for {@link AttributeInstanceDto} instances produced by
 * {@link CommonMetamacDatasetAccess#extractDatasetAndDimensionAttributeInstances}.
 */
public class AttributeInstanceDtoAsserts {

    private AttributeInstanceDtoAsserts() {
    }

    /**
     * Asserts a DATASET-level or single-dimension attribute instance.
     *
     * <p>Checks the attribute ID, the plain-locale value, and that exactly one dimension key
     * maps to the expected code.</p>
     *
     * @param inst             the instance to assert
     * @param expectedAttrId   expected value of {@link AttributeInstanceDto#getAttributeId()}
     * @param expectedValue    expected label under {@link IndicatorsConstants#DATASET_REPOSITORY_LOCALE}
     * @param expectedDimKey   the indicator dimension type name (e.g. "GEOGRAPHICAL", "TIME")
     * @param expectedDimCode  the expected code in the dimension's list
     */
    public static void assertSingleDimInstance(AttributeInstanceDto inst, String expectedAttrId, String expectedValue,
            String expectedDimKey, String expectedDimCode) {
        assertEquals(expectedAttrId, inst.getAttributeId());
        assertEquals(expectedValue, inst.getValue().getLocalisedLabel(IndicatorsConstants.DATASET_REPOSITORY_LOCALE));
        List<String> codes = inst.getCodesByDimension().get(expectedDimKey);
        assertNotNull("codesByDimension should contain key: " + expectedDimKey, codes);
        assertEquals(1, codes.size());
        assertEquals(expectedDimCode, codes.get(0));
    }

    /**
     * Asserts a GROUP-level attribute instance that spans exactly two dimensions.
     *
     * <p>Checks the attribute ID, the plain-locale value, and that both dimension keys map
     * to the expected codes.</p>
     *
     * @param inst            the instance to assert
     * @param expectedAttrId  expected value of {@link AttributeInstanceDto#getAttributeId()}
     * @param expectedValue   expected label under {@link IndicatorsConstants#DATASET_REPOSITORY_LOCALE}
     * @param dim1Key         first dimension type name
     * @param dim1Code        expected code for the first dimension
     * @param dim2Key         second dimension type name
     * @param dim2Code        expected code for the second dimension
     */
    public static void assertTwoDimInstance(AttributeInstanceDto inst, String expectedAttrId, String expectedValue,
            String dim1Key, String dim1Code, String dim2Key, String dim2Code) {
        assertEquals(expectedAttrId, inst.getAttributeId());
        assertEquals(expectedValue, inst.getValue().getLocalisedLabel(IndicatorsConstants.DATASET_REPOSITORY_LOCALE));

        List<String> dim1Codes = inst.getCodesByDimension().get(dim1Key);
        assertNotNull("codesByDimension should contain key: " + dim1Key, dim1Codes);
        assertEquals(dim1Code, dim1Codes.get(0));

        List<String> dim2Codes = inst.getCodesByDimension().get(dim2Key);
        assertNotNull("codesByDimension should contain key: " + dim2Key, dim2Codes);
        assertEquals(dim2Code, dim2Codes.get(0));
    }
}
