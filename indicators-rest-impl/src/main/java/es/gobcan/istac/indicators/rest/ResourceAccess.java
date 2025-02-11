package es.gobcan.istac.indicators.rest;

import static es.gobcan.istac.indicators.rest.util.ExportUtils.dataToDataArray;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.CodeRepresentation;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Data;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DimensionRepresentation;

import es.gobcan.istac.indicators.rest.enume.LabelVisualisationModeEnum;
import es.gobcan.istac.indicators.rest.types.AttributeType;
import es.gobcan.istac.indicators.rest.types.DataDimensionType;
import es.gobcan.istac.indicators.rest.types.DataRepresentationType;
import es.gobcan.istac.indicators.rest.types.DataType;

public class ResourceAccess {

    private static final int                              MAX_PX_MATRIX_LENGTH = 8;

    private List<String>                                  selectedLanguages;

    // Metadata

    private Map<String, String>                           dimensionsValuesCurrentLocaleLabels;

    private Map<String, Map<String, InternationalString>> attributesValuesCurrentLocaleLabels;
    private Map<String, LabelVisualisationModeEnum>       attributesLabelVisualisationMode;
    private List<String>                                  attributeIds         = new ArrayList<String>();

    // Data
    private String[]                                      observations;
    private Map<String, String[]>                         attributesValuesByAttributeId;
    private List<String>                                  dimensionsOrderedForData;

    private final Map<String, Integer>                    multipliers          = new HashMap<String, Integer>();
    private final Map<String, Map<String, Long>>          representationIndex  = new HashMap<String, Map<String, Long>>(); // Map<Dimension, Map<Code, Index>

    public ResourceAccess(DataType data) throws MetamacException {

        initialize(data, data.getDimension(), data.getAttribute());
    }

    private void initialize(DataType data, Map<String, DataDimensionType> dimensions, List<Map<String, AttributeType>> attributes) throws MetamacException {
        this.setSelectedLanguages(selectedLanguages);

        initializeObservations(data);
        initializeDimensions(data);
        initializeDimensionsForData(data);
        initializeAttributes(data, attributes);
        // initializeIndex(data);
    }

    public List<String> getAttributeAttachmentLevelIds() {
        return attributeIds;
    }

    public InternationalString getAttributeValueLabelCurrentLocale(String attributeId, String attributeValue) {
        return attributesValuesCurrentLocaleLabels.get(attributeId).get(attributeValue);
    }

    public String[] getObservations() {
        return observations;
    }

    public String[] getAttributeValues(String attributeId) {
        return attributesValuesByAttributeId.get(attributeId);
    }

    public List<String> getDimensionsOrderedForData() {
        return dimensionsOrderedForData;
    }

    /**
     * Init dimensions and dimensions values
     */
    private void initializeDimensions(DataType data) throws MetamacException {

        Map<String, DataDimensionType> dimensionsMetadata = data.getDimension();
        Map<String, String> valuesCurrentLocaleLabels = new HashMap<>();

        if (dimensionsMetadata != null) {
            for (String dimensionKey : dimensionsMetadata.keySet()) {
                DataDimensionType dimension = dimensionsMetadata.get(dimensionKey);
                valuesCurrentLocaleLabels.put(dimensionKey, dimensionKey);
            }
        }

        this.dimensionsValuesCurrentLocaleLabels = valuesCurrentLocaleLabels;
    }
    /**
     * Init observations values
     */
    private void initializeObservations(DataType data) {
        observations = dataToDataArray(data.getObservation().toString());
    }

    /**
     * Init dimensions and dimensions values. Builds a map with dimensions values to get order provided in DATA, because observations are retrieved in API with this order
     */
    private void initializeDimensionsForData(DataType data) throws MetamacException {

        Map<String, DataDimensionType> dimensionRepresentations = data.getDimension();
        dimensionsOrderedForData = new ArrayList<>();

        for (String dimensionId : dimensionsValuesCurrentLocaleLabels.keySet()) {
            DataDimensionType dimensionRepresentation = dimensionRepresentations.get(dimensionId);
            if (dimensionRepresentation != null) {
                DataRepresentationType representation = dimensionRepresentation.getRepresentation();
                if (representation != null && representation.getIndex() != null) {
                    dimensionsOrderedForData.addAll(representation.getIndex().keySet());
                }
            }
        }

    }

    /**
     * Retrieve the observation for a specific key <param>permutation</param>
     *
     * @param permutation at permutation key
     * @return
     */
    public String observationAtPermutation(Map<String, String> permutation) {
        int offset = calculateOffsetAtPermutation(permutation);

        String observation = getObservations()[offset];
        if (!observation.trim().isEmpty()) {
            return observation;
        } else {
            return null;
        }
    }

    public String measureAttributeValueAtPermutation(String attributeId, Map<String, String> permutation) {
        int offset = calculateOffsetAtPermutation(permutation);
        String[] attributeValues = getAttributeValues(attributeId);
        String attributeValue = null;
        if (attributeValues != null) {
            attributeValue = attributeValues[offset];
        }
        return attributeValue;
    }

    private void initializeMultipliers(Data data) {
        List<DimensionRepresentation> dimensionsRepresentation = data.getDimensions().getDimensions();
        ListIterator<DimensionRepresentation> dimensionsListIterator = dimensionsRepresentation.listIterator(dimensionsRepresentation.size());
        int incrementCounter = 1;

        // Iterate the list in reverse order: right to left or down to up in the display table for calculate cell spacing
        while (dimensionsListIterator.hasPrevious()) {
            DimensionRepresentation dimension = dimensionsListIterator.previous();
            multipliers.put(dimension.getDimensionId(), incrementCounter);
            incrementCounter *= dimension.getRepresentations().getRepresentations().size();
        }
    }

    /**
     * Calculate a map indexed by dimension with map as value. The value map is indexed by code and its value is a index.
     */
    private void initializeIndex(Data data) {
        List<DimensionRepresentation> dimensionsRepresentation = data.getDimensions().getDimensions();
        for (DimensionRepresentation dimension : dimensionsRepresentation) {
            Map<String, Long> representationIndexMap = new HashMap<String, Long>();
            List<CodeRepresentation> representations = dimension.getRepresentations().getRepresentations();
            for (CodeRepresentation representation : representations) {
                representationIndexMap.put(representation.getCode(), representation.getIndex());
            }
            representationIndex.put(dimension.getDimensionId(), representationIndexMap);
        }
    }

    private int calculateOffsetAtPermutation(Map<String, String> permutation) {
        int offset = 0;
        for (Map.Entry<String, String> permutationEntry : permutation.entrySet()) {
            String dimensionId = permutationEntry.getKey();
            String representationId = permutationEntry.getValue();

            long index = representationIndex.get(dimensionId).get(representationId);
            int multiplier = multipliers.get(dimensionId);
            offset += index * multiplier;
        }
        return offset;
    }

    /**
     * Init definitions and values of attributes
     *
     * @param attributes
     */
    private void initializeAttributes(DataType data, List<Map<String, AttributeType>> attributes) throws MetamacException {

        List<AttributeType> attributesMetadata = new ArrayList<>();

        if (attributes != null) {
            for (Map<String, AttributeType> attributeMap : attributes) {
                if (attributeMap == null) {
                    attributesMetadata.add(null);
                } else {
                    for (AttributeType attribute : attributeMap.values()) {
                        attributesMetadata.add(attribute);
                    }
                }
            }
        }

        // Attribute Instances
        attributesValuesByAttributeId = new HashMap<String, String[]>(attributesMetadata.size());
        for (AttributeType attribute : attributesMetadata) {
            // if (AttributeAttachmentLevelType.PRIMARY_MEASURE.equals(attribute.getAttachmentLevel())) {
            // // only observation attachment level
            // attributeIds.add(attribute.getId());
            // }

            if (data.getAttribute() != null) {
                // for (DataAttribute dataAttribute : data.getAttribute().g()) {
                // if (dataAttribute.getId().equals(attribute.getId())) {
                // attributesValuesByAttributeId.put(attribute.getId(), ExportUtils.dataToDataArray(dataAttribute.getValue()));
                // }
                // }
            }
        }

        // attributesLabelVisualisationMode = ExportUtils.buildMapAttributesLabelVisualisationMode(attributesMetadata);
        // attributesValuesCurrentLocaleLabels = new HashMap<String, Map<String, InternationalString>>();
        // for (AttributeType attribute : attributesMetadata) {
        // attributesValuesCurrentLocaleLabels.put(attribute.getId(), ExportUtils.buildMapAttributesValuesLabels(attribute));
        // }
    }

    public List<String> getSelectedLanguages() {
        return selectedLanguages;
    }

    public void setSelectedLanguages(List<String> selectedLanguages) {
        this.selectedLanguages = selectedLanguages;
    }

    public static String generateMatrixFromString(String string) {
        return Base64.getEncoder().encodeToString(string.getBytes()).substring(0, MAX_PX_MATRIX_LENGTH);
    }
}