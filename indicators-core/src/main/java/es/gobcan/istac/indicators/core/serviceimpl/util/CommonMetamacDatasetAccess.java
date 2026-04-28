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
     * <p>The {@code sourceDimToIndicatorDim} parameter maps each source API dimension ID to its
     * corresponding indicator dimension type name (e.g. "GEOGRAPHICAL", "TIME", "MEASURE").
     * For DIMENSION attributes, if the attached dimension is not present in this map the attribute
     * is skipped, because it cannot be mapped to an indicator dimension.</p>
     *
     * @param sourceDimToIndicatorDim mapping from source dimension ID to indicator dimension type name
     * @return list of attribute instances; empty if there are no DATASET/DIMENSION/GROUP attributes
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

            String attributeId = metadataAttribute.getId();

            // Try plain string attribute first, then international
            DataAttribute matchedDataAttr = findDataAttribute(dataAttributes, attributeId);
            DataInternationalAttribute matchedInternAttr = findDataInternationalAttribute(internationalAttributes, attributeId);

            if (matchedInternAttr != null) {
                multilingualAttributeIds.add(attributeId);
            }

            if (isDimension && metadataAttribute.getDimensions().getDimensions().size() > 1) {
                if (matchedDataAttr != null && metadataAttribute.getAttributeValues() instanceof EnumeratedAttributeValues) {
                    Map<String, String> labelMap = buildEnumCodeToLabelMap(
                            (EnumeratedAttributeValues) metadataAttribute.getAttributeValues(),
                            matchedDataAttr.getValue());
                    if (!labelMap.isEmpty()) {
                        dimensionEnumLabelMapByAttributeId.put(attributeId, labelMap);
                    }
                }
                List<AttributeInstanceDto> groupInstances = buildGroupLevelInstances(
                        attributeId, metadataAttribute, sourceDimToIndicatorDim, matchedDataAttr, matchedInternAttr);
                result.addAll(groupInstances);
                continue;
            }

            if (isDataset) {
                if (matchedDataAttr != null
                        && metadataAttribute.getAttributeValues() instanceof EnumeratedAttributeValues) {
                    String label = resolveEnumeratedLabel(
                            (EnumeratedAttributeValues) metadataAttribute.getAttributeValues(),
                            matchedDataAttr.getValue());
                    if (label != null) {
                        enumLabelByAttributeId.put(attributeId, label);
                    }
                }
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
                if (matchedDataAttr != null && metadataAttribute.getAttributeValues() instanceof EnumeratedAttributeValues) {
                    Map<String, String> labelMap = buildEnumCodeToLabelMap(
                            (EnumeratedAttributeValues) metadataAttribute.getAttributeValues(),
                            matchedDataAttr.getValue());
                    if (!labelMap.isEmpty()) {
                        dimensionEnumLabelMapByAttributeId.put(attributeId, labelMap);
                    }
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
                    InternationalStringDto value = InternationalStringUtils.buildDatasetRepositoryInternationalStringDtoFromCommonInternationalStringDto(apiInternationalString);
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


    /**
     * Builds attribute instances for a GROUP-level attribute (multiple dimensions).
     *
     * <p>All dimensions declared in the attribute must be mapped in {@code sourceDimToIndicatorDim};
     * if any dimension is unmapped the method returns an empty list and the attribute is skipped.</p>
     *
     * <p>Values are indexed as a flat array following the cartesian product of the group dimensions
     * ordered by {@link #getDimensionsOrderedForData()} (first dimension varies slowest).</p>
     *
     * @param attributeId           attribute identifier
     * @param metadataAttribute     metadata descriptor of the attribute
     * @param sourceDimToIndicatorDim mapping from source dimension ID to indicator dimension type name
     * @param dataAttr              plain-string data attribute (may be null)
     * @param internAttr            international-string data attribute (may be null)
     * @return list of attribute instances; empty if any group dimension is unmapped or there are no values
     */
    private List<AttributeInstanceDto> buildGroupLevelInstances(String attributeId, Attribute metadataAttribute,
            Map<String, String> sourceDimToIndicatorDim, DataAttribute dataAttr, DataInternationalAttribute internAttr) {

        // Collect all source dimension IDs declared in the group attribute
        List<String> groupSourceDimIds = new ArrayList<String>();
        for (AttributeDimension dim : metadataAttribute.getDimensions().getDimensions()) {
            groupSourceDimIds.add(dim.getDimensionId());
        }

        // All dimensions must be mapped to an indicator dimension; skip otherwise
        Map<String, String> groupDimMapping = new LinkedHashMap<String, String>();
        for (String sourceDimId : groupSourceDimIds) {
            String indicatorDimId = sourceDimToIndicatorDim.get(sourceDimId);
            if (indicatorDimId == null) {
                return new ArrayList<AttributeInstanceDto>();
            }
            groupDimMapping.put(sourceDimId, indicatorDimId);
        }

        // Order the group dimensions by global data dimension order (consistent with observation indexing)
        List<String> orderedGroupDimIds = new ArrayList<String>();
        for (String globalDimId : getDimensionsOrderedForData()) {
            if (groupDimMapping.containsKey(globalDimId)) {
                orderedGroupDimIds.add(globalDimId);
            }
        }

        // Get the ordered code lists for each group dimension
        List<List<String>> dimCodesList = new ArrayList<List<String>>();
        for (String sourceDimId : orderedGroupDimIds) {
            List<String> codes = getDimensionValuesOrderedForData(sourceDimId);
            if (codes == null || codes.isEmpty()) {
                return new ArrayList<AttributeInstanceDto>();
            }
            dimCodesList.add(codes);
        }

        // Compute total size and strides for cartesian-product flat indexing
        int totalSize = 1;
        for (List<String> codes : dimCodesList) {
            totalSize *= codes.size();
        }
        int[] strides = new int[dimCodesList.size()];
        strides[dimCodesList.size() - 1] = 1;
        for (int k = dimCodesList.size() - 2; k >= 0; k--) {
            strides[k] = strides[k + 1] * dimCodesList.get(k + 1).size();
        }

        List<AttributeInstanceDto> result = new ArrayList<AttributeInstanceDto>();

        if (dataAttr != null && !StringUtils.isBlank(dataAttr.getValue())) {
            String[] values = StringUtils.splitByWholeSeparatorPreserveAllTokens(dataAttr.getValue(), DATA_SEPARATOR);
            for (int i = 0; i < values.length && i < totalSize; i++) {
                if (!StringUtils.isBlank(values[i])) {
                    InternationalStringDto value = new InternationalStringDto();
                    value.addText(new LocalisedStringDto(IndicatorsConstants.DATASET_REPOSITORY_LOCALE, values[i]));
                    AttributeInstanceDto instance = new AttributeInstanceDto();
                    instance.setAttributeId(attributeId);
                    instance.setValue(value);
                    instance.setCodesByDimension(buildGroupCodesByDimension(i, orderedGroupDimIds, groupDimMapping, dimCodesList, strides));
                    result.add(instance);
                }
            }
        } else if (internAttr != null) {
            List<InternationalString> internValues = internAttr.getValues();
            for (int i = 0; i < internValues.size() && i < totalSize; i++) {
                InternationalString apiInternationalString = internValues.get(i);
                if (apiInternationalString != null) {
                    InternationalStringDto value = InternationalStringUtils.buildDatasetRepositoryInternationalStringDtoFromCommonInternationalStringDto(apiInternationalString);
                    AttributeInstanceDto instance = new AttributeInstanceDto();
                    instance.setAttributeId(attributeId);
                    instance.setValue(value);
                    instance.setCodesByDimension(buildGroupCodesByDimension(i, orderedGroupDimIds, groupDimMapping, dimCodesList, strides));
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
     * @param orderedGroupDimIds source dimension IDs ordered by global data dimension order
     * @param groupDimMapping    mapping from source dimension ID to indicator dimension type name
     * @param dimCodesList       ordered code lists, one per group dimension (same order as orderedGroupDimIds)
     * @param strides            pre-computed strides for cartesian-product index decomposition
     * @return map from indicator dimension type name to singleton list containing the resolved code
     */
    private Map<String, List<String>> buildGroupCodesByDimension(int flatIndex, List<String> orderedGroupDimIds,
            Map<String, String> groupDimMapping, List<List<String>> dimCodesList, int[] strides) {
        Map<String, List<String>> codesByDimension = new HashMap<String, List<String>>();
        for (int k = 0; k < orderedGroupDimIds.size(); k++) {
            String sourceDimId = orderedGroupDimIds.get(k);
            String indicatorDimId = groupDimMapping.get(sourceDimId);
            int dimIndex = (flatIndex / strides[k]) % dimCodesList.get(k).size();
            String code = dimCodesList.get(k).get(dimIndex);
            codesByDimension.put(indicatorDimId, Arrays.asList(code));
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
