package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Attribute;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.AttributeAttachmentLevelType;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Attributes;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.CodeRepresentation;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Data;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DataAttribute;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DataInternationalAttribute;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DimensionRepresentation;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DimensionRepresentations;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.EnumeratedAttributeValue;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.EnumeratedAttributeValues;

import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceDto;
import es.gobcan.istac.edatos.dataset.repository.dto.InternationalStringDto;
import es.gobcan.istac.edatos.dataset.repository.dto.LocalisedStringDto;
import es.gobcan.istac.indicators.core.constants.IndicatorsConstants;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorDataAttributeTypeEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

public abstract class CommonMetamacDatasetAccess {

    public static String                DATA_SEPARATOR     = " | ";
    protected String[]                  observations;
    protected List<String[]>            observationsAttributes;
    protected List<String>              dimensionsOrderedForData;
    protected Map<String, List<String>> dimensionValuesOrderedForDataByDimensionId;
    protected List<String>              attributesMetadataMap;

    public static final String          OBS_CONF_ATTRIBUTE = IndicatorDataAttributeTypeEnum.OBS_CONF.name();

    protected abstract DimensionRepresentations getDimensions();

    protected abstract Attributes getMetadataAttributes();

    protected abstract Data getData();

    public CommonMetamacDatasetAccess() throws MetamacException {

    }

    public void initialize(Map<String, String> variableElementsByCode, List<String> geographicalDimensionsId) throws MetamacException {
        initializeObservations();
        initializeObservationsAttributes();
        initializeDimensionsForData(variableElementsByCode, geographicalDimensionsId);
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
        if (observationsAttributes != null && !observationsAttributes.isEmpty()) {
            for (String[] observationsAttribute : observationsAttributes) {
                if (observationsAttribute != null && observationsAttribute.length > 0) {
                    attributeValues.add(observationsAttribute[index]);
                }
            }
        }

        return attributeValues;
    }

    public List<String> getAttributesMetadataMap() {
        return attributesMetadataMap;
    }

    /**
     * Init observations values
     */
    protected void initializeObservations() {
        this.observations = dataToDataArray(getData().getObservations());
    }

    public static String[] dataToDataArray(String data) {
        return StringUtils.splitByWholeSeparatorPreserveAllTokens(data, DATA_SEPARATOR);
    }

    /**
     * Init dimensions and dimensions values. Builds a map with dimensions values to get order provided in DATA, because observations are retrieved in API with this order
     */
    protected void initializeDimensionsForData(Map<String, String> variableElementsByCode, List<String> geographicalDimensionsId) throws MetamacException {
        if (geographicalDimensionsId == null) {
            geographicalDimensionsId = new ArrayList<String>();
        }
        List<DimensionRepresentation> dimensionRepresentations = getDimensions().getDimensions();
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

    protected void initializeObservationsAttributes() {
        if (getData() == null || getData().getAttributes() == null) {
            return;
        }
        List<DataAttribute> dataAttributes = getData().getAttributes().getAttributes();
        Attributes metadataAttributes = getMetadataAttributes();

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

        boolean foundObs = false;
        String valueObsAux = "";

        for (DataAttribute dataAttributeDef : dataAttributesDef) {
            if (OBS_CONF_ATTRIBUTE.equals(dataAttributeDef.getId())) {
                foundObs = true;
            } else {
                valueObsAux = dataAttributeDef.getValue();
            }
            this.attributesMetadataMap.add(dataAttributeDef.getId());
            this.observationsAttributes
                    .add(StringUtils.splitByWholeSeparatorPreserveAllTokens(getObservationsAttributesDataValue(dataAttributeDef.getValue(), dataAttributeDef.getId()), DATA_SEPARATOR));
        }

        if (!foundObs) {
            this.attributesMetadataMap.add(OBS_CONF_ATTRIBUTE);
            String[] dataObs = new String[StringUtils.splitByWholeSeparatorPreserveAllTokens(valueObsAux, DATA_SEPARATOR).length];
            this.observationsAttributes.add(dataObs);
        }
    }

    /**
     * Gets observation attributes' data values based on attribute IDs and a string of attribute values.
     *
     * @param attributesString A string of attribute values.
     * @param attributeId The ID of the attribute to process.
     * @return An array of observation attributes' data values.
     */
    private String getObservationsAttributesDataValue(String attributesString, String attributeId) {
        String[] dataArrayAttributes = StringUtils.splitByWholeSeparatorPreserveAllTokens(attributesString, DATA_SEPARATOR);
        processAttribute(dataArrayAttributes, attributeId);
        return String.join(DATA_SEPARATOR, dataArrayAttributes);
    }

    /**
     * Processes a specific attribute, updating dataArrayAttributes based on attribute values.
     *
     * @param dataArrayAttributes An array of attribute values to be updated.
     * @param attributeId The ID of the attribute to process.
     */
    private void processAttribute(String[] dataArrayAttributes, String attributeId) {
        for (Attribute attribute : getMetadataAttributes().getAttributes()) {
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
        if (attributeValues == null) {
            return;
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
     * Extracts DATASET-level and DIMENSION-level attribute instances from the API response.
     *
     * <p>For each attribute in the metadata whose attachment level is DATASET or DIMENSION
     * (single-dimension), an {@link AttributeInstanceDto} is built and added to the result list.
     * PRIMARY_MEASURE attributes are skipped (they are handled separately as observation attributes).
     * Multi-dimension (GROUP) attributes are out of scope and are also skipped.</p>
     *
     * <p>The {@code sourceDimToIndicatorDim} parameter maps each source API dimension ID to its
     * corresponding indicator dimension type name (e.g. "GEOGRAPHICAL", "TIME", "MEASURE").
     * For DIMENSION attributes, if the attached dimension is not present in this map the attribute
     * is skipped, because it cannot be mapped to an indicator dimension.</p>
     *
     * @param sourceDimToIndicatorDim mapping from source dimension ID to indicator dimension type name
     * @return list of attribute instances; empty if there are no DATASET/DIMENSION attributes
     */
    public List<AttributeInstanceDto> extractDatasetAndDimensionAttributeInstances(Map<String, String> sourceDimToIndicatorDim) {
        List<AttributeInstanceDto> result = new ArrayList<AttributeInstanceDto>();

        if (getData() == null || getData().getAttributes() == null) {
            return result;
        }

        List<DataAttribute> dataAttributes = getData().getAttributes().getAttributes();
        List<DataInternationalAttribute> internationalAttributes = getData().getAttributes().getInternationalAttributes();

        for (Attribute metadataAttribute : getMetadataAttributes().getAttributes()) {
            AttributeAttachmentLevelType attachmentLevel = metadataAttribute.getAttachmentLevel();

            if (AttributeAttachmentLevelType.PRIMARY_MEASURE.equals(attachmentLevel)) {
                continue;
            }

            boolean isDataset = AttributeAttachmentLevelType.DATASET.equals(attachmentLevel)
                    || metadataAttribute.getDimensions() == null
                    || metadataAttribute.getDimensions().getDimensions().isEmpty();

            boolean isDimension = AttributeAttachmentLevelType.DIMENSION.equals(attachmentLevel)
                    && metadataAttribute.getDimensions() != null
                    && !metadataAttribute.getDimensions().getDimensions().isEmpty();

            // Skip GROUP / multi-dimension attributes (out of scope)
            if (isDimension && metadataAttribute.getDimensions().getDimensions().size() > 1) {
                continue;
            }

            String attributeId = metadataAttribute.getId();

            // Try plain string attribute first, then international
            DataAttribute matchedDataAttr = findDataAttribute(dataAttributes, attributeId);
            DataInternationalAttribute matchedInternAttr = findDataInternationalAttribute(internationalAttributes, attributeId);

            if (isDataset) {
                List<AttributeInstanceDto> instances = buildDatasetLevelInstances(attributeId, matchedDataAttr, matchedInternAttr);
                result.addAll(instances);

            } else if (isDimension) {
                String sourceDimId = metadataAttribute.getDimensions().getDimensions().get(0).getDimensionId();
                String indicatorDimId = sourceDimToIndicatorDim.get(sourceDimId);
                if (indicatorDimId == null) {
                    // Dimension not mapped to any indicator dimension type — skip
                    continue;
                }
                List<String> dimCodes = getDimensionValuesOrderedForData(sourceDimId);
                if (dimCodes == null || dimCodes.isEmpty()) {
                    continue;
                }
                List<AttributeInstanceDto> instances = buildDimensionLevelInstances(attributeId, indicatorDimId, dimCodes, matchedDataAttr, matchedInternAttr);
                result.addAll(instances);
            }
        }

        return result;
    }

    private DataAttribute findDataAttribute(List<DataAttribute> dataAttributes, String attributeId) {
        for (DataAttribute dataAttribute : dataAttributes) {
            if (attributeId.equals(dataAttribute.getId())) {
                return dataAttribute;
            }
        }
        return null;
    }

    private DataInternationalAttribute findDataInternationalAttribute(List<DataInternationalAttribute> internationalAttributes, String attributeId) {
        for (DataInternationalAttribute internAttr : internationalAttributes) {
            if (attributeId.equals(internAttr.getId())) {
                return internAttr;
            }
        }
        return null;
    }

    private List<AttributeInstanceDto> buildDatasetLevelInstances(String attributeId, DataAttribute dataAttr, DataInternationalAttribute internAttr) {
        List<AttributeInstanceDto> result = new ArrayList<AttributeInstanceDto>();
        Map<String, List<String>> emptyCodes = new HashMap<String, List<String>>();

        if (dataAttr != null && !StringUtils.isBlank(dataAttr.getValue())) {
            InternationalStringDto value = new InternationalStringDto();
            value.addText(new LocalisedStringDto(IndicatorsConstants.DATASET_REPOSITORY_LOCALE, dataAttr.getValue()));
            AttributeInstanceDto instance = new AttributeInstanceDto();
            instance.setAttributeId(attributeId);
            instance.setValue(value);
            instance.setCodesByDimension(emptyCodes);
            result.add(instance);

        } else if (internAttr != null && !internAttr.getValues().isEmpty()) {
            InternationalString apiInternationalString = internAttr.getValues().get(0);
            if (apiInternationalString != null) {
                InternationalStringDto value = toInternationalStringDto(apiInternationalString);
                AttributeInstanceDto instance = new AttributeInstanceDto();
                instance.setAttributeId(attributeId);
                instance.setValue(value);
                instance.setCodesByDimension(emptyCodes);
                result.add(instance);
            }
        }

        return result;
    }

    private List<AttributeInstanceDto> buildDimensionLevelInstances(String attributeId, String indicatorDimId, List<String> dimCodes, DataAttribute dataAttr, DataInternationalAttribute internAttr) {
        List<AttributeInstanceDto> result = new ArrayList<AttributeInstanceDto>();

        if (dataAttr != null && !StringUtils.isBlank(dataAttr.getValue())) {
            String[] values = StringUtils.splitByWholeSeparatorPreserveAllTokens(dataAttr.getValue(), DATA_SEPARATOR);
            for (int i = 0; i < values.length && i < dimCodes.size(); i++) {
                if (!StringUtils.isBlank(values[i])) {
                    InternationalStringDto value = new InternationalStringDto();
                    value.addText(new LocalisedStringDto(IndicatorsConstants.DATASET_REPOSITORY_LOCALE, values[i]));
                    AttributeInstanceDto instance = new AttributeInstanceDto();
                    instance.setAttributeId(attributeId);
                    instance.setValue(value);
                    Map<String, List<String>> codesByDimension = new HashMap<String, List<String>>();
                    codesByDimension.put(indicatorDimId, Arrays.asList(dimCodes.get(i)));
                    instance.setCodesByDimension(codesByDimension);
                    result.add(instance);
                }
            }

        } else if (internAttr != null) {
            List<InternationalString> internValues = internAttr.getValues();
            for (int i = 0; i < internValues.size() && i < dimCodes.size(); i++) {
                InternationalString apiInternationalString = internValues.get(i);
                if (apiInternationalString != null) {
                    InternationalStringDto value = toInternationalStringDto(apiInternationalString);
                    AttributeInstanceDto instance = new AttributeInstanceDto();
                    instance.setAttributeId(attributeId);
                    instance.setValue(value);
                    Map<String, List<String>> codesByDimension = new HashMap<String, List<String>>();
                    codesByDimension.put(indicatorDimId, Arrays.asList(dimCodes.get(i)));
                    instance.setCodesByDimension(codesByDimension);
                    result.add(instance);
                }
            }
        }

        return result;
    }

    private InternationalStringDto toInternationalStringDto(InternationalString apiInternationalString) {
        InternationalStringDto dto = new InternationalStringDto();
        for (LocalisedString localisedString : apiInternationalString.getTexts()) {
            if (localisedString.getLang() != null && localisedString.getValue() != null) {
                dto.addText(new LocalisedStringDto(localisedString.getLang(), localisedString.getValue()));
            }
        }
        return dto;
    }

    /**
     * Retrieves the localized value from an EnumeratedAttributeValue.
     *
     * @param attributeValue The EnumeratedAttributeValue object containing localized values.
     * @return String The localized value corresponding to the DATASET_REPOSITORY_LOCALE. Returns null if the specified locale is not found.
     */
    private String getLocalizedValue(EnumeratedAttributeValue attributeValue) {
        List<LocalisedString> texts = attributeValue.getName().getTexts();
        for (LocalisedString localisedString : texts) {
            if (localisedString.getLang() != null && localisedString.getLang().equals(IndicatorsConstants.DATASET_REPOSITORY_LOCALE)) {
                return localisedString.getValue();
            }
        }
        return null;
    }
}
