package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Attribute;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.AttributeDimension;
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
import es.gobcan.istac.indicators.core.enume.domain.IndicatorDataDimensionTypeEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

public abstract class CommonMetamacDatasetAccess {

    public static String                DATA_SEPARATOR     = " | ";
    protected String[]                  observations;
    protected List<String[]>            observationsAttributes;
    protected List<String>              dimensionsOrderedForData;
    protected Map<String, List<String>> dimensionValuesOrderedForDataByDimensionId;
    protected List<String>              attributesMetadataMap;

    public static final String          OBS_CONF_ATTRIBUTE         = IndicatorDataAttributeTypeEnum.OBS_CONF.name();
    private Set<String>                              multilingualAttributeIds              = new HashSet<String>();
    private Map<String, String>                      enumLabelByAttributeId                = new HashMap<String, String>();
    private Map<String, Map<String, String>>         dimensionEnumLabelMapByAttributeId    = new HashMap<String, Map<String, String>>();

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

    public Set<String> getMultilingualAttributeIds() {
        return multilingualAttributeIds;
    }

    public Map<String, String> getEnumLabelByAttributeId() {
        return enumLabelByAttributeId;
    }

    public Map<String, Map<String, String>> getDimensionEnumLabelMapByAttributeId() {
        return dimensionEnumLabelMapByAttributeId;
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
     * Extracts DATASET-level, DIMENSION-level, and GROUP-level attribute instances from the API response.
     *
     * <p>For each attribute in the metadata whose attachment level is DATASET or DIMENSION,
     * an {@link AttributeInstanceDto} is built and added to the result list.
     * PRIMARY_MEASURE attributes are skipped (they are handled separately as observation attributes).
     * Multi-dimension (GROUP) attributes are supported when all their dimensions are mapped to
     * indicator dimensions; otherwise the attribute is skipped.</p>
     *
     * <p>The {@code sourceDimensionToIndicatorDimension} parameter maps each source API dimension ID to its
     * corresponding indicator dimension type name (e.g. "GEOGRAPHICAL", "TIME", "MEASURE").
     * For DIMENSION attributes, if the attached dimension is not present in this map the attribute
     * is skipped, because it cannot be mapped to an indicator dimension.</p>
     *
     * @param sourceDimensionToIndicatorDimension mapping from source dimension ID to indicator dimension type name
     * @return list of attribute instances; empty if there are no DATASET/DIMENSION/GROUP attributes
     */
    public List<AttributeInstanceDto> extractDatasetAndDimensionAttributeInstances(Map<String, String> sourceDimensionToIndicatorDimension) throws MetamacException {
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

            String attributeId = metadataAttribute.getId();

            // Try plain string attribute first, then international
            DataAttribute matchedDataAttribute = findDataAttribute(dataAttributes, attributeId);
            DataInternationalAttribute matchedInternationalAttribute = findDataInternationalAttribute(internationalAttributes, attributeId);

            if (matchedInternationalAttribute != null) {
                multilingualAttributeIds.add(attributeId);
            }

            if (isDimension && metadataAttribute.getDimensions().getDimensions().size() > 1) {
                if (matchedDataAttribute != null && metadataAttribute.getAttributeValues() instanceof EnumeratedAttributeValues) {
                    Map<String, String> labelMap = buildEnumCodeToLabelMap(
                            (EnumeratedAttributeValues) metadataAttribute.getAttributeValues(),
                            matchedDataAttribute.getValue());
                    if (!labelMap.isEmpty()) {
                        dimensionEnumLabelMapByAttributeId.put(attributeId, labelMap);
                    }
                }
                List<AttributeInstanceDto> groupInstances = buildGroupLevelInstances(
                        attributeId, metadataAttribute, sourceDimensionToIndicatorDimension, matchedDataAttribute, matchedInternationalAttribute);
                result.addAll(groupInstances);
                continue;
            }

            if (isDataset) {
                if (matchedDataAttribute != null
                        && metadataAttribute.getAttributeValues() instanceof EnumeratedAttributeValues) {
                    String label = resolveEnumeratedLabel(
                            (EnumeratedAttributeValues) metadataAttribute.getAttributeValues(),
                            matchedDataAttribute.getValue());
                    if (label != null) {
                        enumLabelByAttributeId.put(attributeId, label);
                    }
                }
                List<AttributeInstanceDto> instances = buildDatasetLevelInstances(attributeId, matchedDataAttribute, matchedInternationalAttribute);
                result.addAll(instances);

            } else if (isDimension) {
                String sourceDimensionId = metadataAttribute.getDimensions().getDimensions().get(0).getDimensionId();
                String indicatorDimensionId = sourceDimensionToIndicatorDimension.get(sourceDimensionId);
                if (indicatorDimensionId == null) {
                    // Dimension not mapped to any indicator dimension type — skip
                    continue;
                }
                List<String> dimensionCodes = getDimensionValuesOrderedForData(sourceDimensionId);
                if (dimensionCodes == null || dimensionCodes.isEmpty()) {
                    continue;
                }
                // Normalize TIME codes from statistical-resources format (e.g. "2023-M01") to
                // indicators-data format (e.g. "2023-01"), matching how observation codes are stored.
                if (IndicatorDataDimensionTypeEnum.TIME.name().equals(indicatorDimensionId)) {
                    dimensionCodes = MetamacTimeUtils.normalizeToMetamacTimeValues(dimensionCodes);
                }
                if (matchedDataAttribute != null && metadataAttribute.getAttributeValues() instanceof EnumeratedAttributeValues) {
                    Map<String, String> labelMap = buildEnumCodeToLabelMap(
                            (EnumeratedAttributeValues) metadataAttribute.getAttributeValues(),
                            matchedDataAttribute.getValue());
                    if (!labelMap.isEmpty()) {
                        dimensionEnumLabelMapByAttributeId.put(attributeId, labelMap);
                    }
                }
                List<AttributeInstanceDto> instances = buildDimensionLevelInstances(attributeId, indicatorDimensionId, dimensionCodes, matchedDataAttribute, matchedInternationalAttribute);
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
        for (DataInternationalAttribute internationalAttribute : internationalAttributes) {
            if (attributeId.equals(internationalAttribute.getId())) {
                return internationalAttribute;
            }
        }
        return null;
    }

    private List<AttributeInstanceDto> buildDatasetLevelInstances(String attributeId, DataAttribute dataAttribute, DataInternationalAttribute internationalAttribute) {
        List<AttributeInstanceDto> result = new ArrayList<AttributeInstanceDto>();
        Map<String, List<String>> emptyCodes = new HashMap<String, List<String>>();

        if (dataAttribute != null && !StringUtils.isBlank(dataAttribute.getValue())) {
            InternationalStringDto value = new InternationalStringDto();
            value.addText(new LocalisedStringDto(IndicatorsConstants.DATASET_REPOSITORY_LOCALE, dataAttribute.getValue()));
            AttributeInstanceDto instance = new AttributeInstanceDto();
            instance.setAttributeId(attributeId);
            instance.setValue(value);
            instance.setCodesByDimension(emptyCodes);
            result.add(instance);

        } else if (internationalAttribute != null && !internationalAttribute.getValues().isEmpty()) {
            InternationalString apiInternationalString = internationalAttribute.getValues().get(0);
            if (apiInternationalString != null) {
                InternationalStringDto value = InternationalStringUtils.buildDatasetRepositoryInternationalStringDtoFromCommonInternationalStringDto(apiInternationalString);
                AttributeInstanceDto instance = new AttributeInstanceDto();
                instance.setAttributeId(attributeId);
                instance.setValue(value);
                instance.setCodesByDimension(emptyCodes);
                result.add(instance);
            }
        }

        return result;
    }

    private List<AttributeInstanceDto> buildDimensionLevelInstances(String attributeId, String indicatorDimensionId, List<String> dimensionCodes, DataAttribute dataAttribute, DataInternationalAttribute internationalAttribute) {
        List<AttributeInstanceDto> result = new ArrayList<AttributeInstanceDto>();

        if (dataAttribute != null && !StringUtils.isBlank(dataAttribute.getValue())) {
            String[] values = StringUtils.splitByWholeSeparatorPreserveAllTokens(dataAttribute.getValue(), DATA_SEPARATOR);
            for (int i = 0; i < values.length && i < dimensionCodes.size(); i++) {
                if (!StringUtils.isBlank(values[i])) {
                    InternationalStringDto value = new InternationalStringDto();
                    value.addText(new LocalisedStringDto(IndicatorsConstants.DATASET_REPOSITORY_LOCALE, values[i]));
                    AttributeInstanceDto instance = new AttributeInstanceDto();
                    instance.setAttributeId(attributeId);
                    instance.setValue(value);
                    Map<String, List<String>> codesByDimension = new HashMap<String, List<String>>();
                    codesByDimension.put(indicatorDimensionId, Arrays.asList(dimensionCodes.get(i)));
                    instance.setCodesByDimension(codesByDimension);
                    result.add(instance);
                }
            }

        } else if (internationalAttribute != null) {
            List<InternationalString> internationalValues = internationalAttribute.getValues();
            for (int i = 0; i < internationalValues.size() && i < dimensionCodes.size(); i++) {
                InternationalString apiInternationalString = internationalValues.get(i);
                if (apiInternationalString != null) {
                    InternationalStringDto value = InternationalStringUtils.buildDatasetRepositoryInternationalStringDtoFromCommonInternationalStringDto(apiInternationalString);
                    AttributeInstanceDto instance = new AttributeInstanceDto();
                    instance.setAttributeId(attributeId);
                    instance.setValue(value);
                    Map<String, List<String>> codesByDimension = new HashMap<String, List<String>>();
                    codesByDimension.put(indicatorDimensionId, Arrays.asList(dimensionCodes.get(i)));
                    instance.setCodesByDimension(codesByDimension);
                    result.add(instance);
                }
            }
        }

        return result;
    }


    /**
     * Builds attribute instances for a GROUP-level attribute (multiple dimensions).
     *
     * <p>All dimensions declared in the attribute must be mapped in {@code sourceDimensionToIndicatorDimension};
     * if any dimension is unmapped the method returns an empty list and the attribute is skipped.</p>
     *
     * <p>Values are indexed as a flat array following the cartesian product of the group dimensions
     * ordered by {@link #getDimensionsOrderedForData()} (first dimension varies slowest).</p>
     *
     * @param attributeId           attribute identifier
     * @param metadataAttribute     metadata descriptor of the attribute
     * @param sourceDimensionToIndicatorDimension mapping from source dimension ID to indicator dimension type name
     * @param dataAttribute              plain-string data attribute (may be null)
     * @param internationalAttribute            international-string data attribute (may be null)
     * @return list of attribute instances; empty if any group dimension is unmapped or there are no values
     */
    private List<AttributeInstanceDto> buildGroupLevelInstances(String attributeId, Attribute metadataAttribute,
            Map<String, String> sourceDimensionToIndicatorDimension, DataAttribute dataAttribute, DataInternationalAttribute internationalAttribute) throws MetamacException {

        // Collect all source dimension IDs declared in the group attribute
        List<String> groupSourceDimensionIds = new ArrayList<String>();
        for (AttributeDimension attributeDimension : metadataAttribute.getDimensions().getDimensions()) {
            groupSourceDimensionIds.add(attributeDimension.getDimensionId());
        }

        // All dimensions must be mapped to an indicator dimension; skip otherwise
        Map<String, String> groupDimensionMapping = new LinkedHashMap<String, String>();
        for (String sourceDimensionId : groupSourceDimensionIds) {
            String indicatorDimensionId = sourceDimensionToIndicatorDimension.get(sourceDimensionId);
            if (indicatorDimensionId == null) {
                return new ArrayList<AttributeInstanceDto>();
            }
            groupDimensionMapping.put(sourceDimensionId, indicatorDimensionId);
        }

        // Order the group dimensions by global data dimension order (consistent with observation indexing)
        List<String> orderedGroupDimensionIds = new ArrayList<String>();
        for (String globalDimensionId : getDimensionsOrderedForData()) {
            if (groupDimensionMapping.containsKey(globalDimensionId)) {
                orderedGroupDimensionIds.add(globalDimensionId);
            }
        }

        // Get the ordered code lists for each group dimension
        List<List<String>> dimensionCodesList = new ArrayList<List<String>>();
        for (String sourceDimensionId : orderedGroupDimensionIds) {
            List<String> codes = getDimensionValuesOrderedForData(sourceDimensionId);
            if (codes == null || codes.isEmpty()) {
                return new ArrayList<AttributeInstanceDto>();
            }
            // Normalize TIME codes from statistical-resources format (e.g. "2023-M01") to
            // indicators-data format (e.g. "2023-01"), matching how observation codes are stored.
            if (IndicatorDataDimensionTypeEnum.TIME.name().equals(groupDimensionMapping.get(sourceDimensionId))) {
                codes = MetamacTimeUtils.normalizeToMetamacTimeValues(codes);
            }
            dimensionCodesList.add(codes);
        }

        // Compute total size and strides for cartesian-product flat indexing
        int totalSize = 1;
        for (List<String> codes : dimensionCodesList) {
            totalSize *= codes.size();
        }
        int[] strides = new int[dimensionCodesList.size()];
        strides[dimensionCodesList.size() - 1] = 1;
        for (int k = dimensionCodesList.size() - 2; k >= 0; k--) {
            strides[k] = strides[k + 1] * dimensionCodesList.get(k + 1).size();
        }

        List<AttributeInstanceDto> result = new ArrayList<AttributeInstanceDto>();

        if (dataAttribute != null && !StringUtils.isBlank(dataAttribute.getValue())) {
            String[] values = StringUtils.splitByWholeSeparatorPreserveAllTokens(dataAttribute.getValue(), DATA_SEPARATOR);
            for (int i = 0; i < values.length && i < totalSize; i++) {
                if (!StringUtils.isBlank(values[i])) {
                    InternationalStringDto value = new InternationalStringDto();
                    value.addText(new LocalisedStringDto(IndicatorsConstants.DATASET_REPOSITORY_LOCALE, values[i]));
                    AttributeInstanceDto instance = new AttributeInstanceDto();
                    instance.setAttributeId(attributeId);
                    instance.setValue(value);
                    instance.setCodesByDimension(buildGroupCodesByDimension(i, orderedGroupDimensionIds, groupDimensionMapping, dimensionCodesList, strides));
                    result.add(instance);
                }
            }
        } else if (internationalAttribute != null) {
            List<InternationalString> internationalValues = internationalAttribute.getValues();
            for (int i = 0; i < internationalValues.size() && i < totalSize; i++) {
                InternationalString apiInternationalString = internationalValues.get(i);
                if (apiInternationalString != null) {
                    InternationalStringDto value = InternationalStringUtils.buildDatasetRepositoryInternationalStringDtoFromCommonInternationalStringDto(apiInternationalString);
                    AttributeInstanceDto instance = new AttributeInstanceDto();
                    instance.setAttributeId(attributeId);
                    instance.setValue(value);
                    instance.setCodesByDimension(buildGroupCodesByDimension(i, orderedGroupDimensionIds, groupDimensionMapping, dimensionCodesList, strides));
                    result.add(instance);
                }
            }
        }

        return result;
    }

    /**
     * Builds the {@code codesByDimension} map for a single GROUP attribute value at flat index {@code flatIndex}.
     *
     * <p>Uses the stride array to decompose the flat index into per-dimension indices following the
     * cartesian product ordering where the last dimension varies fastest.</p>
     *
     * @param flatIndex          position in the flat values array
     * @param orderedGroupDimensionIds source dimension IDs ordered by global data dimension order
     * @param groupDimensionMapping    mapping from source dimension ID to indicator dimension type name
     * @param dimensionCodesList       ordered code lists, one per group dimension (same order as orderedGroupDimensionIds)
     * @param strides            pre-computed strides for cartesian-product index decomposition
     * @return map from indicator dimension type name to singleton list containing the resolved code
     */
    private Map<String, List<String>> buildGroupCodesByDimension(int flatIndex, List<String> orderedGroupDimensionIds,
            Map<String, String> groupDimensionMapping, List<List<String>> dimensionCodesList, int[] strides) {
        Map<String, List<String>> codesByDimension = new HashMap<String, List<String>>();
        for (int k = 0; k < orderedGroupDimensionIds.size(); k++) {
            String sourceDimensionId = orderedGroupDimensionIds.get(k);
            String indicatorDimensionId = groupDimensionMapping.get(sourceDimensionId);
            int dimensionIndex = (flatIndex / strides[k]) % dimensionCodesList.get(k).size();
            String code = dimensionCodesList.get(k).get(dimensionIndex);
            codesByDimension.put(indicatorDimensionId, Arrays.asList(code));
        }
        return codesByDimension;
    }

    private Map<String, String> buildEnumCodeToLabelMap(EnumeratedAttributeValues enumValues, String rawValues) {
        Map<String, String> labelMap = new LinkedHashMap<String, String>();
        if (rawValues == null) {
            return labelMap;
        }
        String[] codes = StringUtils.splitByWholeSeparatorPreserveAllTokens(rawValues, DATA_SEPARATOR);
        for (String code : codes) {
            if (!StringUtils.isBlank(code) && !labelMap.containsKey(code)) {
                String label = resolveEnumeratedLabel(enumValues, code);
                if (label != null) {
                    labelMap.put(code, label);
                }
            }
        }
        return labelMap;
    }

    private String resolveEnumeratedLabel(EnumeratedAttributeValues enumValues, String rawCode) {
        for (EnumeratedAttributeValue enumValue : enumValues.getValues()) {
            if (Objects.equals(enumValue.getId(), rawCode)) {
                return getLocalizedValue(enumValue);
            }
        }
        return null;
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
