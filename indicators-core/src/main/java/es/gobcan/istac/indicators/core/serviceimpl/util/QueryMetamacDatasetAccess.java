package es.gobcan.istac.indicators.core.serviceimpl.util;

import es.gobcan.istac.indicators.core.constants.IndicatorsConstants;
import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Query;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.*;

import java.util.*;

public class QueryMetamacDatasetAccess {
    public static String DATA_SEPARATOR = " | ";

    private final String ATTACHMENT_LEVEL = "PRIMARY_MEASURE";
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
        Attributes metadataAttributes  = query.getMetadata().getAttributes();
        List<DataAttribute> dataAttributesDef = new ArrayList<>();

        // Recoger solo los atributos con ATTACHMENT_LEVEL
        for (Attribute metadataAttribute : metadataAttributes.getAttributes()) {
            if (metadataAttribute.getAttachmentLevel().name().equals(ATTACHMENT_LEVEL)) {
                for (DataAttribute dataAttribute : dataAttributes) {
                    if (metadataAttribute.getId().equals(dataAttribute.getId())) {
                        dataAttributesDef.add(dataAttribute);
                        break;
                    }
                }
            }
        }

        Map<String, String[]> attributesMap = new HashMap<>();

        // Iterar sobre los atributos para encontrar el valor del atributo deseado
        for (DataAttribute dataAttribute : dataAttributes) {
            String attributeId = dataAttribute.getId();
            for (DataAttribute dataAttributeDef : dataAttributesDef) {
                if (dataAttributeDef.getId().equals(attributeId)) {
                    attributesMap.put(attributeId, getObservationsAttributesDataValue(query, dataAttribute.getValue(), attributeId));
                }

            }
        }

        // Obtener el String[] consolidado
        this.observationsAttributes = consolidateAttributesMap(attributesMap);
    }

    /**
     * Consolidates the values from a Map of String arrays into a single String array.
     *
     * The resulting array contains concatenated entries in the format "key1:value1;key2:value3", where each entry
     * corresponds to a position across the arrays associated with each key in the original map.
     *
     * Empty values are omitted from the entries, and the assumption is that all arrays associated with each key
     * have the same length.
     *
     * @param attributesMap The Map containing String arrays associated with keys.
     * @return A String array containing consolidated entries based on the values from attributesMap.
     */
    private static String[] consolidateAttributesMap(Map<String, String[]> attributesMap) {
        List<String> consolidatedList = new ArrayList<>();

        // Obtener el largo de los arrays asociados a la primera clave (suponiendo que todos son del mismo largo)
        int arrayLength = attributesMap.values().iterator().next().length;

        // Iterar sobre cada posición de los arrays
        for (int i = 0; i < arrayLength; i++) {
            StringBuilder entry = new StringBuilder();

            // Construir cada entrada
            for (Map.Entry<String, String[]> entrySet : attributesMap.entrySet()) {
                String key = entrySet.getKey();
                String[] values = entrySet.getValue();
                String value = (i < values.length) ? values[i] : "";

                if (!value.isEmpty()) {
                    entry.append(key).append(":").append(value);

                    if (i < arrayLength - 1) {
                        entry.append(";");
                    }
                }
            }

            consolidatedList.add(entry.toString());
        }

        return consolidatedList.toArray(new String[0]);
    }

    /**
     * Gets observation attributes' data values based on attribute IDs and a string of attribute values.
     *
     * @param query            The Query object containing the metadata.
     * @param attributesString A string of attribute values.
     * @param attributeId      The ID of the attribute to process.
     * @return An array of observation attributes' data values.
     */
    private String[] getObservationsAttributesDataValue(Query query, String attributesString, String attributeId) {
        String[] dataArrayAttributes = StringUtils.splitByWholeSeparatorPreserveAllTokens(attributesString, DATA_SEPARATOR);
        processAttribute(query, dataArrayAttributes, attributeId);
        return dataArrayAttributes;
    }

    /**
     * Processes a specific attribute, updating dataArrayAttributes based on attribute values.
     *
     * @param query               The Query object containing the metadata.
     * @param dataArrayAttributes An array of attribute values to be updated.
     * @param attributeId         The ID of the attribute to process.
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
     * @param attribute           The Attribute object containing enumerated values.
     * @param dataArrayAttributes An array of attribute values to be updated.
     */
    private void updateDataArrayAttributes(Attribute attribute, String[] dataArrayAttributes) {
        EnumeratedAttributeValues attributeValues = (EnumeratedAttributeValues) attribute.getAttributeValues();

        for (int i = 0; i < attributeValues.getValues().size(); i++) {
            for (int j = 0; j < dataArrayAttributes.length; j++) {
                if (Objects.equals(attributeValues.getValues().get(i).getId(), dataArrayAttributes[j])) {
                    String localizedValue = getLocalizedValue(attributeValues.getValues().get(i));
                    // Asigna el valor en el locale correspondiente a dataArrayAttributes[j]
                    dataArrayAttributes[j] = localizedValue;
                }
            }
        }
    }

    /**
     * Retrieves the localized value from an EnumeratedAttributeValue.
     *
     * @param attributeValue The EnumeratedAttributeValue object containing localized values.
     * @return String The localized value corresponding to the DATASET_REPOSITORY_LOCALE.
     * Returns null if the specified locale is not found.
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
