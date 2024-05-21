package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Dataset;
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

public class DatasetMetamacDatasetAccess {

    public static String                    DATA_SEPARATOR = " | ";
    private       String[]                  observations;
    private       List<String[]>            observationsAttributes;
    private       List<String>              dimensionsOrderedForData;
    private       Map<String, List<String>> dimensionValuesOrderedForDataByDimensionId;
    private       List<String>              attributesMetadataMap;

    public DatasetMetamacDatasetAccess(Dataset dataset, Map<String, String> variableElementsByCode, List<String> geographicalDimensionsId) throws MetamacException {

        initializeObservations(dataset);
        initializeObservationsAttributes(dataset);
        initializeDimensionsForData(dataset, variableElementsByCode, geographicalDimensionsId);
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
    private void initializeObservations(Dataset dataset) {
        this.observations = dataToDataArray(dataset.getData().getObservations());
    }

    /**
     * Init dimensions and dimensions values. Builds a map with dimensions values to get order provided in DATA, because observations are retrieved in API with this order
     */
    private void initializeDimensionsForData(Dataset dataset, Map<String, String> variableElementsByCode, List<String> geographicalDimensionsId) throws MetamacException {
        if (geographicalDimensionsId == null) {
            geographicalDimensionsId = new ArrayList<String>();
        }
        List<DimensionRepresentation> dimensionRepresentations = dataset.getData().getDimensions().getDimensions();
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

    private void initializeObservationsAttributes(Dataset dataset) {
        List<DataAttribute> dataAttributes = dataset.getData().getAttributes().getAttributes();
        Attributes metadataAttributes = dataset.getMetadata().getAttributes();

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

        this.observationsAttributes = new ArrayList<>(dataAttributesDef.size());

        for (DataAttribute dataAttributeDef : dataAttributesDef) {
            this.attributesMetadataMap.add(dataAttributeDef.getId());
            this.observationsAttributes.add(
                    StringUtils.splitByWholeSeparatorPreserveAllTokens(getObservationsAttributesDataValue(dataset, dataAttributeDef.getValue(), dataAttributeDef.getId()), DATA_SEPARATOR));
        }
    }

    /**
     * Gets observation attributes' data values based on attribute IDs and a string of attribute values.
     *
     * @param dataset The Dataset object containing the metadata.
     * @param attributesString A string of attribute values.
     * @param attributeId The ID of the attribute to process.
     * @return An array of observation attributes' data values.
     */
    private String getObservationsAttributesDataValue(Dataset dataset, String attributesString, String attributeId) {
        String[] dataArrayAttributes = StringUtils.splitByWholeSeparatorPreserveAllTokens(attributesString, DATA_SEPARATOR);
        processAttribute(dataset, dataArrayAttributes, attributeId);
        return String.join(DATA_SEPARATOR, dataArrayAttributes);
    }

    /**
     * Processes a specific attribute, updating dataArrayAttributes based on attribute values.
     *
     * @param dataset The Dataset object containing the metadata.
     * @param dataArrayAttributes An array of attribute values to be updated.
     * @param attributeId The ID of the attribute to process.
     */
    private void processAttribute(Dataset dataset, String[] dataArrayAttributes, String attributeId) {
        for (Attribute attribute : dataset.getMetadata().getAttributes().getAttributes()) {
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
        // Verificar si attributeValues es nulo antes de continuar
        if (attributeValues == null) {
            return; // No hacer nada si attributeValues es nulo
        }
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
