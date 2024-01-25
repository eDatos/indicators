package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.*;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.*;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Query;
import org.springframework.beans.factory.annotation.Autowired;

public class QueryMetamacDatasetAccess {

    protected IndicatorsConfigurationService configurationService;

    public static String DATA_SEPARATOR = " | ";

    //Lo ideal es que se cargara directamente desde configurationService.retrieveLanguageDefaultLocale().getLanguage()
    public static String DATASET_REPOSITORY_LOCALE = "es";

    private String OBSERVATION_ATTRIBUTE_ID = "ESTADO_OBSERVACION";

    // Data
    private String[] observations;

    private String[] observationsAttributes;
    private List<String> dimensionsOrderedForData;
    private Map<String, List<String>> dimensionValuesOrderedForDataByDimensionId;

    public QueryMetamacDatasetAccess(Query query) throws MetamacException {

        initializeObservations(query);
        initializeObservationsAttributes(query);
        initializeDimensionsForData(query);

        this.configurationService = configurationService;
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
        String attributesString = null;
        // Iteramos sobre los atributos para encontrar el valor del atributo deseado
        for (DataAttribute dataAttribute : dataAttributes) {
            if (OBSERVATION_ATTRIBUTE_ID.equals(dataAttribute.getId())) {
                attributesString = dataAttribute.getValue();
                break;  // Rompemos el bucle una vez que encontramos el atributo con el ID deseado y definido en OBSERVATION_ATTRIBUTE_ID
            }
        }
        this.observationsAttributes = getObservationsAttributesDataValue(query, attributesString);
    }

    /**
     * Get observations attributes data values based on attribute IDs and a string of attribute values.
     *
     * @param query            The Query object containing the metadata¡
     * @param attributesString A string of attribute values.
     * @return An array of observations attributes data values.
     */
    private String[] getObservationsAttributesDataValue(Query query, String attributesString) {
        String[] dataArrayAttributes = StringUtils.splitByWholeSeparatorPreserveAllTokens(attributesString, DATA_SEPARATOR);
        processAttribute(query, dataArrayAttributes);
        return dataArrayAttributes;
    }

    /**
     * Process a specific attribute, updating dataArrayAttributes based on attribute values.
     *
     * @param query               The Query object containing the metadata.
     * @param dataArrayAttributes An array of attribute values to be updated.
     */
    private void processAttribute(Query query, String[] dataArrayAttributes) {
        for (Attribute attribute : query.getMetadata().getAttributes().getAttributes()) {
            if (Objects.equals(attribute.getId(), OBSERVATION_ATTRIBUTE_ID)) {
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
//                    dataArrayAttributes[j] = attributeValues.getValues().get(i).getName().getTexts().get(0).getValue();
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
            if (localisedString.getLang() != null && localisedString.getLang().equals(DATASET_REPOSITORY_LOCALE)) {
                // Si la propiedad 'lang' es igual a DATASET_REPOSITORY_LOCALE, devuelve el valor
                return localisedString.getValue();
            }
        }

        // Si no se encuentra DATASET_REPOSITORY_LOCALE, devuelve null
        return null;
    }

}
