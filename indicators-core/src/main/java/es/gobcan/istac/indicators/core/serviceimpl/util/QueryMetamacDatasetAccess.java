package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Query;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Attribute;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.AttributeAttachmentLevelType;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Attributes;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.CodeRepresentation;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DataAttribute;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DimensionRepresentation;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.EnumeratedAttributeValue;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.EnumeratedAttributeValues;

import es.gobcan.istac.indicators.core.constants.IndicatorsConstants;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

public class QueryMetamacDatasetAccess {

    public static String                    DATA_SEPARATOR = " | ";
    private       String[]                  observations;
    private List<String[]> observationsAttributes;
    private       List<String>              dimensionsOrderedForData;
    private       Map<String, List<String>> dimensionValuesOrderedForDataByDimensionId;
    private       List<String>              attributesMetadataMap;

    public QueryMetamacDatasetAccess(Query query, Map<String, String> variableElementsByCode, List<String> geographicalDimensionsId) throws MetamacException {

        initializeObservations(query);
        initializeObservationsAttributes(query);
        initializeDimensionsForData(query, variableElementsByCode, geographicalDimensionsId);
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

    public List<String> getObservationsAttributes(int index) {
        List<String> attributeValues = new ArrayList<>();

        for (String[] observationsAttribute : observationsAttributes) {
            attributeValues.add(observationsAttribute[index]);
        }

        return attributeValues;
    }

    public List<String> getAttributesMetadataMap() {
        return attributesMetadataMap;
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
    private void initializeDimensionsForData(Query query, Map<String, String> variableElementsByCode, List<String> geographicalDimensionsId) throws MetamacException {
        if (geographicalDimensionsId == null) {
            geographicalDimensionsId = new ArrayList<String>();
        }
        List<DimensionRepresentation> dimensionRepresentations = query.getData().getDimensions().getDimensions();
        this.dimensionsOrderedForData = new ArrayList<String>(dimensionRepresentations.size());
        this.dimensionValuesOrderedForDataByDimensionId = new HashMap<String, List<String>>(dimensionRepresentations.size());
        for (DimensionRepresentation dimensionRepresentation : dimensionRepresentations) {
            String dimensionId = dimensionRepresentation.getDimensionId();
            this.dimensionsOrderedForData.add(dimensionId);

            List<CodeRepresentation> codesRepresentations = dimensionRepresentation.getRepresentations().getRepresentations();
            this.dimensionValuesOrderedForDataByDimensionId.put(dimensionId, new ArrayList<String>(codesRepresentations.size()));
            for (CodeRepresentation codeRepresentation : codesRepresentations) {
                if (geographicalDimensionsId.contains(dimensionId)) {
                    String variableElement = variableElementsByCode.get(codeRepresentation.getCode());

                    if (variableElement != null) {
                        this.dimensionValuesOrderedForDataByDimensionId.get(dimensionId).add(variableElement);
                    } else {
                        throw new MetamacException(ServiceExceptionType.GEOGRAPHICAL_VARIABLE_ELEMENT_NOT_FOUND_WITH_CODE, codeRepresentation.getCode());
                    }
                } else {
                    this.dimensionValuesOrderedForDataByDimensionId.get(dimensionId).add(codeRepresentation.getCode());
                }
            }
        }
    }

    public static String[] dataToDataArray(String data) {
        return StringUtils.splitByWholeSeparatorPreserveAllTokens(data, DATA_SEPARATOR);
    }

    private void initializeObservationsAttributes(Query query) {
        List<DataAttribute> dataAttributes = query.getData().getAttributes().getAttributes();
        Attributes metadataAttributes = query.getMetadata().getAttributes();

        this.attributesMetadataMap = new ArrayList<>();
        List<DataAttribute> dataAttributesDef = new ArrayList<>();

        for (Attribute metadataAttribute : metadataAttributes.getAttributes()) {
            if (AttributeAttachmentLevelType.PRIMARY_MEASURE.equals(metadataAttribute.getAttachmentLevel())) {
                for (DataAttribute dataAttribute : dataAttributes) {
                    if (metadataAttribute.getId().equals(dataAttribute.getId())) {
                        dataAttributesDef.add(dataAttribute);
                        break;
                    }
                }
            }
        }

        this.observationsAttributes = new ArrayList<>();

        for (DataAttribute dataAttributeDef : dataAttributesDef) {
            this.attributesMetadataMap.add(dataAttributeDef.getId());
            dataAttributeDef.setValue(getObservationsAttributesDataValue(query, dataAttributeDef.getValue(), dataAttributeDef.getId()));

            String[] splitValues = StringUtils.splitByWholeSeparatorPreserveAllTokens(dataAttributeDef.getValue(), DATA_SEPARATOR);
            this.observationsAttributes.add(splitValues);
        }
    }

    /**
     * Gets observation attributes' data values based on attribute IDs and a string of attribute values.
     *
     * @param query The Query object containing the metadata.
     * @param attributesString A string of attribute values.
     * @param attributeId The ID of the attribute to process.
     * @return An array of observation attributes' data values.
     */
    private String getObservationsAttributesDataValue(Query query, String attributesString, String attributeId) {
        String[] dataArrayAttributes = StringUtils.splitByWholeSeparatorPreserveAllTokens(attributesString, DATA_SEPARATOR);
        processAttribute(query, dataArrayAttributes, attributeId);
        return String.join(DATA_SEPARATOR, dataArrayAttributes);
    }

    /**
     * Processes a specific attribute, updating dataArrayAttributes based on attribute values.
     *
     * @param query The Query object containing the metadata.
     * @param dataArrayAttributes An array of attribute values to be updated.
     * @param attributeId The ID of the attribute to process.
     */
    private void processAttribute(Query query, String[] dataArrayAttributes, String attributeId) {
        for (Attribute attribute : query.getMetadata().getAttributes().getAttributes()) {
            if (Objects.equals(attribute.getId(), attributeId)) {
                updateDataArrayAttributes(attribute, dataArrayAttributes);
            }
        }
    }

    /**
     * Update dataArrayAttributes based on enumerated attribute values.
     *
     * @param attribute The Attribute object containing enumerated values.
     * @param dataArrayAttributes An array of attribute values to be updated.
     */
    private void updateDataArrayAttributes(Attribute attribute, String[] dataArrayAttributes) {
        EnumeratedAttributeValues attributeValues = (EnumeratedAttributeValues) attribute.getAttributeValues();
        for (int i = 0; i < attributeValues.getValues().size(); i++) {
            for (int j = 0; j < dataArrayAttributes.length; j++) {
                if (Objects.equals(attributeValues.getValues().get(i).getId(), dataArrayAttributes[j])) {
                    String localizedValue = getLocalizedValue(attributeValues.getValues().get(i));
                    dataArrayAttributes[j] = localizedValue;
                }
            }
        }
    }

    /**
     * Retrieves the localized value from an EnumeratedAttributeValue.
     *
     * @param attributeValue The EnumeratedAttributeValue object containing localized values.
     * @return String The localized value corresponding to the DATASET_REPOSITORY_LOCALE. Returns null if the specified locale is not found.
     */
    private String getLocalizedValue(EnumeratedAttributeValue attributeValue) {
        List<LocalisedString> texts = attributeValue.getName().getTexts();
        // Recorre todos los LocalisedString en el ArrayList
        for (LocalisedString localisedString : texts) {
            if (localisedString.getLang() != null && localisedString.getLang().equals(IndicatorsConstants.DATASET_REPOSITORY_LOCALE)) {
                // Si la propiedad 'lang' es igual a DATASET_REPOSITORY_LOCALE, devuelve el valor
                return localisedString.getValue();
            }
        }
        // Si no se encuentra DATASET_REPOSITORY_LOCALE, devuelve null
        return null;
    }

}
