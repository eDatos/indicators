package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.*;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Query;

public class QueryMetamacDatasetAccess {

    public static String DATA_SEPARATOR = " | ";

    // Data
    private String[] observations;

    private String[] observationsAttributes;
    private List<String> dimensionsOrderedForData;
    private Map<String, List<String>> dimensionValuesOrderedForDataByDimensionId;

    public QueryMetamacDatasetAccess(Query query) throws MetamacException {

        initializeObservations(query);
        initializeObservationsAttributes(query);
        initializeDimensionsForData(query);
    }

    public List<String> getDimensionsOrderedForData() {
        return dimensionsOrderedForData;
    }

    public List<String> getDimensionValuesOrderedForData(String dimensionId) {
        return dimensionValuesOrderedForDataByDimensionId.get(dimensionId);
    }

    public String[] getObservations() {
        return observations;
    }

    public String[] getObservationsAttributes() {
        return observationsAttributes;
    }

    /**
     * Init observations values
     */
    private void initializeObservations(Query query) {
        this.observations = dataToDataArray(query.getData().getObservations());
    }

    /**
     * Init dimensions and dimensions values. Builds a map with dimensions values to get order provided in DATA, because observations are retrieved in API with this order
     */
    private void initializeDimensionsForData(Query query) throws MetamacException {
        List<DimensionRepresentation> dimensionRepresentations = query.getData().getDimensions().getDimensions();
        this.dimensionsOrderedForData = new ArrayList<String>(dimensionRepresentations.size());
        this.dimensionValuesOrderedForDataByDimensionId = new HashMap<String, List<String>>(dimensionRepresentations.size());
        for (DimensionRepresentation dimensionRepresentation : dimensionRepresentations) {
            String dimensionId = dimensionRepresentation.getDimensionId();
            this.dimensionsOrderedForData.add(dimensionId);

            List<CodeRepresentation> codesRepresentations = dimensionRepresentation.getRepresentations().getRepresentations();
            this.dimensionValuesOrderedForDataByDimensionId.put(dimensionId, new ArrayList<String>(codesRepresentations.size()));
            for (CodeRepresentation codeRepresentation : codesRepresentations) {
                this.dimensionValuesOrderedForDataByDimensionId.get(dimensionId).add(codeRepresentation.getCode());
            }
        }
    }

    public static String[] dataToDataArray(String data) {
        return StringUtils.splitByWholeSeparatorPreserveAllTokens(data, DATA_SEPARATOR);
    }

    /**
     * Initialize observations attributes values.
     *
     * @param query The Query object containing the data and metadata.
     */
    private void initializeObservationsAttributes(Query query) {
        List<DataAttribute> dataAttributes = query.getData().getAttributes().getAttributes();
        String attributesString = dataAttributes.get(0).getValue();
        String[] attributesIds = getAttributesIds(dataAttributes);
        this.observationsAttributes = getObservationsAttributesDataValue(query, attributesIds, attributesString);
    }

    /**
     * Get attribute IDs from a list of DataAttributes.
     *
     * @param dataAttributes The list of DataAttributes.
     * @return An array of attribute IDs.
     */
    private static String[] getAttributesIds(List<DataAttribute> dataAttributes) {
        String[] ids = new String[dataAttributes.size()];
        int index = 0;
        for (DataAttribute dataAttribute : dataAttributes) {
            ids[index++] = dataAttribute.getId();
        }
        return ids;
    }

    /**
     * Get observations attributes data values based on attribute IDs and a string of attribute values.
     *
     * @param query             The Query object containing the metadata.
     * @param attributesIds     An array of attribute IDs.
     * @param attributesString  A string of attribute values.
     * @return An array of observations attributes data values.
     */
    private String[] getObservationsAttributesDataValue(Query query, String[] attributesIds, String attributesString) {
        String[] dataArrayAttributes = StringUtils.splitByWholeSeparatorPreserveAllTokens(attributesString, DATA_SEPARATOR);

        for (String attributeId : attributesIds) {
            if (attributeId != null && !attributeId.isEmpty()) {
                processAttribute(query, attributeId, dataArrayAttributes);
            }
        }

        return dataArrayAttributes;
    }

    /**
     * Process a specific attribute, updating dataArrayAttributes based on attribute values.
     *
     * @param query                The Query object containing the metadata.
     * @param attributeId          The ID of the attribute to process.
     * @param dataArrayAttributes  An array of attribute values to be updated.
     */
    private void processAttribute(Query query, String attributeId, String[] dataArrayAttributes) {
        for (Attribute attribute : query.getMetadata().getAttributes().getAttributes()) {
            if (Objects.equals(attribute.getId(), attributeId)) {
                updateDataArrayAttributes(attribute, dataArrayAttributes);
            }
        }
    }

    /**
     * Update dataArrayAttributes based on enumerated attribute values.
     *
     * @param attribute           The Attribute object containing enumerated values.
     * @param dataArrayAttributes An array of attribute values to be updated.
     */
    private void updateDataArrayAttributes(Attribute attribute, String[] dataArrayAttributes) {
        EnumeratedAttributeValues attributeValues = (EnumeratedAttributeValues) attribute.getAttributeValues();

        for (int i = 0; i < attributeValues.getValues().size(); i++) {
            for (int j = 0; j < dataArrayAttributes.length; j++) {
                if (Objects.equals(attributeValues.getValues().get(i).getId(), dataArrayAttributes[j])) {
                    dataArrayAttributes[j] = attributeValues.getValues().get(i).getName().getTexts().get(0).getValue();
                }
            }
        }
    }

}
