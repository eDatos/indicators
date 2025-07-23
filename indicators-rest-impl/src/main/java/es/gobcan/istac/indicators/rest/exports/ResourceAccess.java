package es.gobcan.istac.indicators.rest.exports;

import static es.gobcan.istac.indicators.rest.constants.IndicatorsRestApiConstants.DEFAULT;
import static es.gobcan.istac.indicators.rest.util.ExportUtils.buildMapAttributesLabelVisualisationMode;
import static es.gobcan.istac.indicators.rest.util.ExportUtils.buildMapAttributesLabels;
import static es.gobcan.istac.indicators.rest.util.ExportUtils.buildMapAttributesValuesLabels;
import static es.gobcan.istac.indicators.rest.util.ExportUtils.buildMapDimensionLabel;
import static es.gobcan.istac.indicators.rest.util.ExportUtils.buildMapDimensionToMapDimensionsLabelVisualisationMode;
import static es.gobcan.istac.indicators.rest.util.ExportUtils.buildMapDimensionsValuesLabels;
import static es.gobcan.istac.indicators.rest.util.ExportUtils.dataToDataArray;
import static es.gobcan.istac.indicators.rest.util.ExportUtils.getLabel;
import static es.gobcan.istac.indicators.rest.util.ExportUtils.localisedStringsToInternationalString;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.CodeRepresentation;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.CodeRepresentations;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Data;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DataAttribute;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DataAttributes;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DimensionRepresentation;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DimensionRepresentations;

import es.gobcan.istac.indicators.rest.dto.IndicatorSelection;
import es.gobcan.istac.indicators.rest.enume.LabelVisualisationModeEnum;
import es.gobcan.istac.indicators.rest.types.AttributeType;
import es.gobcan.istac.indicators.rest.types.DataDimensionType;
import es.gobcan.istac.indicators.rest.types.DataType;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceType;
import es.gobcan.istac.indicators.rest.types.IndicatorType;
import es.gobcan.istac.indicators.rest.types.MetadataAttributeType;
import es.gobcan.istac.indicators.rest.types.MetadataDimensionType;

public class ResourceAccess {

    private static final String                           OBSERVATIONS_SEPARATOR          = " | ";
    public static final int                               LEFT_DIMENSIONS_START_POSITION  = 0;
    public static final int                               TOP_DIMENSIONS_START_POSITION   = 20;
    public static final int                               FIXED_DIMENSIONS_START_POSITION = 40;
    private IndicatorSelection                            indicatorSelection;
    private String                                        lang;
    private Data                                          data;

    // Metadata
    private Map<String, MetadataDimensionType>            dimensionsMetadata;
    private Map<String, MetadataDimensionType>            dimensionsMetadataMap;

    private Map<String, String>                           dimensionLabelsCurrentLocale;
    private Map<String, String>                           dimensionLabelsDefaultLocale;
    private Map<String, Map<String, String>>              dimensionsValuesCurrentLocaleLabels;
    private Map<String, Map<String, InternationalString>> dimensionsValuesLabels;
    private Map<String, LabelVisualisationModeEnum>       dimensionsLabelVisualisationMode;

    private List<MetadataAttributeType>                   attributesMetadata;
    private Map<String, MetadataAttributeType>            attributesMetadataMap;
    private AttributeType                                 measureAttribute;
    private Map<String, String>                           attributesLabels;
    private Map<String, Map<String, String>>              attributesValuesCurrentLocaleLabels;
    private Map<String, LabelVisualisationModeEnum>       attributesLabelVisualisationMode;

    // Data
    private String[]                                      observations;
    private Map<String, String[]>                         attributesValuesByAttributeId;
    private List<String>                                  dimensionsOrderedForData;
    private Map<String, List<String>>                     dimensionValuesOrderedForDataByDimensionId;
    private final Map<String, Integer>                    multipliers                     = new HashMap<String, Integer>();
    private final Map<String, Map<String, Integer>>       multipliersByAttribute          = new HashMap<String, Map<String, Integer>>();
    private final Map<String, Map<String, Long>>          representationIndex             = new HashMap<String, Map<String, Long>>();   // Map<Dimension, Map<Code, Index>

    private int                                           primaryMeasureAttributesCount   = 0;


    public ResourceAccess(DataType indicatorData, IndicatorType indicator, IndicatorSelection indicatorSelection, String lang) throws MetamacException {
        commonConstructor(indicatorData, indicatorSelection);
        initializeCommon(indicator, indicator.getAttribute(), indicatorData, indicatorSelection, lang);
    }

    public ResourceAccess(DataType indicatorData, IndicatorInstanceType indicatorInstance, IndicatorSelection indicatorSelection, String lang) throws MetamacException {
        commonConstructor(indicatorData, indicatorSelection);
        initializeCommon(indicatorInstance, indicatorInstance.getAttribute(), indicatorData, indicatorSelection, lang);
    }

    private void commonConstructor(DataType indicatorData, IndicatorSelection indicatorSelection) {
        data = new Data();
        data.setObservations(indicatorObservationsToDataObservations(indicatorData.getObservation()));
        data.setAttributes(indicatorAttributesToDataAttributes(indicatorData.getAttribute()));
        data.setDimensions(indicatorDimensionsToDataDimensions(indicatorData.getDimension()));
        this.indicatorSelection = indicatorSelection;
    }

    private void initializeCommon(Object indicator, Map<String, MetadataAttributeType> attributes, DataType indicatorData, IndicatorSelection indicatorSelection, String lang) throws MetamacException {
        this.lang = lang;

        if (indicator instanceof IndicatorType) {
            initializeDimensions((IndicatorType) indicator);
        } else if (indicator instanceof IndicatorInstanceType) {
            initializeDimensions((IndicatorInstanceType) indicator);
        }

        initializeAttributes(data, attributes, indicatorData, indicatorSelection);
        initializeObservations(data);
        initializeDimensionsForData(data);
        initializeMultipliers();
        initializeIndex();
    }
    public IndicatorSelection getDataSelection() {
        return indicatorSelection;
    }

    public Data getData() {
        return data;
    }

    public String getLang() {
        return lang;
    }

    public List<MetadataAttributeType> getAttributesMetadata() {
        return attributesMetadata;
    }

    public String getDimensionValueLabelCurrentLocale(String dimensionId, String dimensionValueId) {
        return dimensionsValuesCurrentLocaleLabels.get(dimensionId).get(dimensionValueId).toString();
    }

    public String getAttributeLabel(String attributeId) {
        return attributesLabels.get(attributeId);
    }

    public String getAttributeValueLabelCurrentLocale(String attributeId, String attributeValue) {
        return attributesValuesCurrentLocaleLabels.get(attributeId).get(attributeValue);
    }

    public LabelVisualisationModeEnum getDimensionLabelVisualisationMode(String dimensionId) {
        return dimensionsLabelVisualisationMode.get(dimensionId);
    }

    public LabelVisualisationModeEnum getAttributeLabelVisualisationMode(String attributeId) {
        return attributesLabelVisualisationMode.get(attributeId);
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

    public List<String> getDimensionValuesOrderedForData(String dimensionId) {
        return dimensionValuesOrderedForDataByDimensionId.get(dimensionId);
    }

    /**
     * Init dimensions and dimensions values for IndicatorType
     */
    private void initializeDimensions(IndicatorType indicator) throws MetamacException {
        initializeDimensionsCommon(indicator.getDimension());
    }

    /**
     * Init dimensions and dimensions values for IndicatorInstanceType
     */
    private void initializeDimensions(IndicatorInstanceType indicator) throws MetamacException {
        initializeDimensionsCommon(indicator.getDimension());
    }

    /**
     * Shared logic to initialize dimensions
     */
    private void initializeDimensionsCommon(Map<String, MetadataDimensionType> dimensionsMetadata) throws MetamacException {
        this.dimensionsMetadata = dimensionsMetadata;

        Map<String, MetadataDimensionType> dimensionsMetadataMap = new HashMap<>(dimensionsMetadata.size());
        Map<String, LabelVisualisationModeEnum> labelVisualisationsMode = new HashMap<>(dimensionsMetadata.size());
        Map<String, Map<String, String>> dimensionsValuesCurrentLocaleLabels = new HashMap<>(dimensionsMetadata.size());
        Map<String, String> dimensionsLabelsCurrentLocale = new HashMap<>(dimensionsMetadata.size());
        Map<String, String> dimensionsLabelsDefaultLocale = new HashMap<>(dimensionsMetadata.size());

        for (Map.Entry<String, MetadataDimensionType> dimension : dimensionsMetadata.entrySet()) {
            String dimensionId = dimension.getKey();

            dimensionsMetadataMap.put(dimensionId, dimension.getValue());
            labelVisualisationsMode.put(dimensionId, buildMapDimensionToMapDimensionsLabelVisualisationMode(dimension));
            dimensionsValuesCurrentLocaleLabels.put(dimensionId, buildMapDimensionsValuesLabels(dimension, lang));
            dimensionsLabelsCurrentLocale.put(dimensionId, buildMapDimensionLabel(dimension, lang));
            dimensionsLabelsDefaultLocale.put(dimensionId, buildMapDimensionLabel(dimension, lang));
        }

        this.dimensionsMetadataMap = dimensionsMetadataMap;
        this.dimensionsLabelVisualisationMode = labelVisualisationsMode;
        this.dimensionsValuesCurrentLocaleLabels = dimensionsValuesCurrentLocaleLabels;
        this.dimensionLabelsCurrentLocale = dimensionsLabelsCurrentLocale;
        this.dimensionLabelsDefaultLocale = dimensionsLabelsDefaultLocale;
    }

    /**
     * Init definitions and values of attributes
     *
     * @param data
     */
    private void initializeAttributes(Data data, Map<String, MetadataAttributeType> attributes, DataType indicatorData, IndicatorSelection indicatorSelection) throws MetamacException {

        attributesMetadata = indicatorAttributesToListAttributes(attributes);

        Map<String, MetadataAttributeType> attributesMetadataMap = new HashMap<String, MetadataAttributeType>();

        // Attribute Instances
        attributesValuesByAttributeId = new HashMap<String, String[]>(attributesMetadata.size());

        for (MetadataAttributeType attribute : attributesMetadata) {
            List<String> valuesList = new ArrayList<>();
            if (indicatorData.getAttribute() != null) {
                for (Map<String, AttributeType> attributeTypeMap : indicatorData.getAttribute()) {
                    if (attributeTypeMap != null && attributeTypeMap.containsKey(attribute.getCode())) {
                        AttributeType attr = attributeTypeMap.get(attribute.getCode());
                        String value = getLabel(localisedStringsToInternationalString(attr.getValue()), lang);
                        valuesList.add(value);
                    } else {
                        valuesList.add(null);
                    }
                }
            }

            attributesValuesByAttributeId.put(attribute.getCode(), valuesList.isEmpty() ? null : valuesList.toArray(new String[0]));

            attributesMetadataMap.put(attribute.getCode(), attribute);
        }

        this.attributesMetadataMap = attributesMetadataMap;
        attributesLabelVisualisationMode = buildMapAttributesLabelVisualisationMode(indicatorSelection, attributesMetadata);
        attributesValuesCurrentLocaleLabels = buildMapAttributesValuesLabels(attributesMetadata, lang);
        attributesLabels = buildMapAttributesLabels(attributesMetadata, lang);
    }

    /**
     * Init observations values
     */
    private void initializeObservations(Data data) {
        observations = dataToDataArray(data.getObservations());
    }

    /**
     * Init dimensions and dimensions values. Builds a map with dimensions values to get order provided in DATA, because observations are retrieved in API with this order
     */
    private void initializeDimensionsForData(Data data) throws MetamacException {
        List<DimensionRepresentation> dimensionRepresentations = data.getDimensions().getDimensions();
        dimensionsOrderedForData = new ArrayList<String>(dimensionRepresentations.size());
        dimensionValuesOrderedForDataByDimensionId = new HashMap<String, List<String>>(dimensionRepresentations.size());
        for (DimensionRepresentation dimensionRepresentation : dimensionRepresentations) {
            String dimensionId = dimensionRepresentation.getDimensionId();
            dimensionsOrderedForData.add(dimensionId);

            List<CodeRepresentation> codesRepresentations = dimensionRepresentation.getRepresentations().getRepresentations();
            dimensionValuesOrderedForDataByDimensionId.put(dimensionId, new ArrayList<String>(codesRepresentations.size()));
            for (CodeRepresentation codeRepresentation : codesRepresentations) {
                dimensionValuesOrderedForDataByDimensionId.get(dimensionId).add(codeRepresentation.getCode());
            }
        }
    }

    /**
     * Retrieve the observation for a specific key <param>permutation</param>
     *
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

    // TODO: EDATOS-5025
    // public CellCommentDetails attributesAtPermutation(Map<String, String> permutation, String unitMeasure, String unitMultiplier) {
    // CellCommentDetails cellCommentDetails = new CellCommentDetails();
    // for (Attribute attribute : getAttributesMetadata()) {
    // // We handle dataset level attributes elsewhere
    // if (AttributeAttachmentLevelType.DATASET.equals(attribute.getAttachmentLevel())) {
    // continue;
    // }
    //
    // Integer offset = null;
    // String attributeId = attribute.getId();
    // // unit multiplier and unit measure attributes wont appear here because they must appear elsewhere, in units
    // if ((unitMeasure != null && unitMeasure.equals(attributeId)) || (unitMultiplier != null && unitMultiplier.equals(attributeId))) {
    // continue;
    // }
    //
    // boolean CELL_AT_HEADER = permutation.size() == 1;
    // boolean CELL_AT_BODY = permutation.size() > 1;
    // // Observation level attributes (body cell)
    // if (CELL_AT_BODY && AttributeAttachmentLevelType.PRIMARY_MEASURE.equals(attribute.getAttachmentLevel())) {
    // offset = calculateOffsetAtPermutation(permutation);
    // } else if (AttributeAttachmentLevelType.DIMENSION.equals(attribute.getAttachmentLevel())) {
    // if (attribute.getDimensions() == null) {
    // continue;
    // }
    // boolean singleDimensionLevelAttributeForHeaderCellMatchHeaderCellDimension = CELL_AT_HEADER && attribute.getDimensions().getDimensions().size() == 1
    // && permutation.get(attribute.getDimensions().getDimensions().get(0).getDimensionId()) != null;
    // boolean combinationDimensionLevelAttributeForBodyCell = CELL_AT_BODY && attribute.getDimensions().getDimensions().size() > 1;
    // if (!(singleDimensionLevelAttributeForHeaderCellMatchHeaderCellDimension || combinationDimensionLevelAttributeForBodyCell)) {
    // continue;
    // }
    // offset = calculateOffsetAtPermutation(permutation, multipliersByAttribute.get(attribute.getId()));
    // }
    // if (offset != null) {
    // String attributeValue = obtainAttributeValue(attributeId, offset);
    // cellCommentDetails.addCommentLine(attributeValue);
    // }
    // }
    // return cellCommentDetails;
    //
    // }

    // public String obtainAttributeValue(String attributeId, int offset) {
    // String[] attributeValues = getAttributeValues(attributeId);
    // String attributeValue = null;
    // if (attributeValues != null) {
    // attributeValue = attributeValues[offset];
    // attributeValue = applyLabelVisualizationModeForAttributeValue(attributeId, attributeValue);
    // }
    // return attributeValue;
    // }
    // public String applyLabelVisualizationModeForAttributeValue(String attributeId, String attributeValue) {
    // // Visualisation mode
    // LabelVisualisationModeEnum labelVisualisation = getAttributeLabelVisualisationMode(attributeId);
    // switch (labelVisualisation) {
    // case CODE:
    // // no extra action
    // break;
    // case LABEL: {
    // String attributeValueLabel = getAttributeValueLabelCurrentLocale(attributeId, attributeValue);
    // if (attributeValueLabel != null) {
    // attributeValue = attributeValueLabel;
    // }
    // }
    // break;
    // case CODE_AND_LABEL: {
    // String attributeValueLabel = getAttributeValueLabelCurrentLocale(attributeId, attributeValue);
    // if (attributeValueLabel != null) {
    // attributeValue = attributeValueLabel + " (" + attributeValue + ")";
    // }
    // }
    // break;
    // default:
    // break;
    // }
    // return attributeValue;
    // }
    // public String applyLabelVisualizationModeForAttribute(String attributeId) {
    // // Visualisation mode
    // LabelVisualisationModeEnum labelVisualisation = getAttributeLabelVisualisationMode(attributeId);
    // String resultText = null;
    // switch (labelVisualisation) {
    // case CODE:
    // resultText = attributeId;
    // break;
    // case LABEL: {
    // String attributeLabel = getAttributeLabel(attributeId);
    // if (attributeLabel != null) {
    // resultText = attributeLabel;
    // }
    // }
    // break;
    // case CODE_AND_LABEL: {
    // String attributeLabel = getAttributeLabel(attributeId);
    // if (attributeLabel != null) {
    // resultText = attributeLabel + " (" + attributeId + ")";
    // }
    // }
    // break;
    // default:
    // break;
    // }
    // return resultText;
    // }
    // public String applyLabelVisualizationModeForDimension(String dimensionId) {
    // LabelVisualisationModeEnum labelVisualisation = getDimensionLabelVisualisationMode(dimensionId);
    // String resultText = null;
    // switch (labelVisualisation) {
    // case CODE:
    // resultText = dimensionId;
    // break;
    // case LABEL: {
    // String dimensionValueLabel = getDimensionLabelCurrentLocale(dimensionId);
    // if (dimensionValueLabel != null) {
    // resultText = dimensionValueLabel;
    // }
    // }
    // break;
    // case CODE_AND_LABEL: {
    // String dimensionValueLabel = getDimensionLabelCurrentLocale(dimensionId);
    // if (dimensionValueLabel != null) {
    // resultText = dimensionValueLabel + " (" + dimensionId + ")";
    // }
    // }
    // break;
    // default:
    // break;
    // }
    // return resultText;
    // }
    // public String applyLabelVisualizationModeForDimensionValue(String dimensionId, String dimensionValueId) {
    // LabelVisualisationModeEnum labelVisualisation = getDimensionLabelVisualisationMode(dimensionId);
    // switch (labelVisualisation) {
    // case CODE:
    // // no extra action
    // break;
    // case LABEL: {
    // String dimensionValueLabel = getDimensionValueLabelCurrentLocale(dimensionId, dimensionValueId);
    // if (dimensionValueLabel != null) {
    // dimensionValueId = dimensionValueLabel;
    // }
    // }
    // break;
    // case CODE_AND_LABEL: {
    // String dimensionValueLabel = getDimensionValueLabelCurrentLocale(dimensionId, dimensionValueId);
    // if (dimensionValueLabel != null) {
    // dimensionValueId = dimensionValueLabel + " (" + dimensionValueId + ")";
    // }
    // }
    // break;
    // default:
    // break;
    // }
    // return dimensionValueId;
    // }

    // We´ll have the "multipliers" variable, used for calculating the offset for observations or attributes at PRIMARY_MEASURE attachment level
    private void initializeMultipliers() {
        List<DimensionRepresentation> dimensions = getData().getDimensions().getDimensions();
        ListIterator<DimensionRepresentation> dimensionsListIterator = dimensions.listIterator(dimensions.size());
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
    private void initializeIndex() {
        List<DimensionRepresentation> dimensions = getData().getDimensions().getDimensions();
        for (DimensionRepresentation dimension : dimensions) {
            Map<String, Long> representationIndexMap = new HashMap<String, Long>();
            List<CodeRepresentation> representations = dimension.getRepresentations().getRepresentations();
            for (CodeRepresentation representation : representations) {
                representationIndexMap.put(representation.getCode(), representation.getIndex());
            }
            representationIndex.put(dimension.getDimensionId(), representationIndexMap);
        }
    }

    private int calculateOffsetAtPermutation(Map<String, String> permutation) {
        return calculateOffsetAtPermutation(permutation, multipliers);
    }

    private int calculateOffsetAtPermutation(Map<String, String> permutation, Map<String, Integer> multipliers) {
        int offset = 0;
        for (Map.Entry<String, String> permutationEntry : permutation.entrySet()) {
            String dimensionId = permutationEntry.getKey();
            String representationId = permutationEntry.getValue();

            long index = representationIndex.get(dimensionId).get(representationId);
            Integer multiplier = multipliers.get(dimensionId);
            if (multiplier != null) {
                offset += index * multiplier;
            }
        }
        return offset;
    }

    // TODO: EDATOS-5025
    // public InternationalString extractUnitCode(EnumeratedDimensionValue dimensionValue, String unitMeasureKey, String unitMultiplierKey) {
    // Integer offset = calculateOffsetDimensionValueId(dimensionValue.getId(), unitMeasureKey);
    // InternationalString unitMeasureName = getAttributeByDimensionValue(unitMeasureKey, offset);
    //
    // offset = calculateOffsetDimensionValueId(dimensionValue.getId(), unitMultiplierKey);
    // InternationalString unitMultiplierName = getAttributeByDimensionValue(unitMultiplierKey, offset);
    // return extractUnitCode(dimensionValue.getMeasureQuantity(), unitMeasureName, unitMultiplierName);
    // }

    // public InternationalString extractUnitCode(EnumeratedAttributeValue attributeValue, String unitMeasureKey, String unitMultiplierKey) {
    // InternationalString unitMeasureName = getAttributeByDimensionValue(unitMeasureKey, 0);
    // InternationalString unitMultiplierName = getAttributeByDimensionValue(unitMultiplierKey, 0);
    // return extractUnitCode(attributeValue.getMeasureQuantity(), unitMeasureName, unitMultiplierName);
    // }

    // private InternationalString extractUnitCode(MeasureQuantity measureQuantity, InternationalString unitMeasureName, InternationalString unitMultiplierName) {
    // if (unitMeasureName == null && measureQuantity != null && measureQuantity.getUnitCode() != null) {
    // unitMeasureName = measureQuantity.getUnitCode().getName();
    // }
    // if (unitMultiplierName == null && measureQuantity != null && measureQuantity.getUnitMultiplier() != null) {
    // unitMultiplierName = measureQuantity.getUnitMultiplier().getName();
    // }
    // return prepareQuantityInternationalString(unitMeasureName, unitMultiplierName);
    // }

    // private Integer calculateOffsetDimensionValueId(String dimensionValueId, String attributeKey) {
    // Integer offset = null;
    // if (multipliersByAttribute.containsKey(attributeKey)) {
    // Map<String, String> permutation = new HashMap<>();
    // permutation.put(measureDimension.getId(), dimensionValueId);
    // offset = calculateOffsetAtPermutation(permutation, multipliersByAttribute.get(attributeKey));
    // }
    // return offset;
    // }

    // private InternationalString getAttributeByDimensionValue(String attributeKey, Integer offset) {
    // if (!attributesValuesByAttributeId.containsKey(attributeKey)) {
    // return null;
    // }
    // String attributeValue = attributesValuesByAttributeId.get(attributeKey)[offset];
    // return attributesValuesLabels.get(attributeKey).get(attributeValue);
    // }
    // private InternationalString prepareQuantityInternationalString(InternationalString unitMeasure, InternationalString unitMultiplier) {
    // InternationalString result = null;
    //
    // if (unitMeasure != null) {
    // result = new InternationalString();
    // for (LocalisedString unitMeasureLocalisedString : unitMeasure.getTexts()) {
    // String lang = unitMeasureLocalisedString.getLang();
    // String unitMeasureLabel = unitMeasureLocalisedString.getValue();
    // String unitMultiplierLabel = ExportUtils.getLabel(unitMultiplier, lang);
    //
    // String value = unitMeasureLabel;
    // value += (unitMultiplierLabel != null) ? " (" + unitMultiplierLabel + ")" : "";
    //
    // LocalisedString localisedString = new LocalisedString();
    // localisedString.setLang(lang);
    // localisedString.setValue(value);
    // result.getTexts().add(localisedString);
    // }
    // } else if (unitMultiplier != null) {
    // result = new InternationalString();
    // for (LocalisedString localisedString : unitMultiplier.getTexts()) {
    // localisedString.setValue("(" + localisedString.getValue() + ")");
    // result.getTexts().add(localisedString);
    // }
    // }
    // return result;
    // }
    // public int getPrimaryMeasureAttributesCount() {
    // return primaryMeasureAttributesCount;
    // }

    private static String indicatorObservationsToDataObservations(List<String> observation) {
        return String.join(OBSERVATIONS_SEPARATOR, observation);
    }

    private static DataAttributes indicatorAttributesToDataAttributes(List<Map<String, AttributeType>> attributes) {
        if (attributes == null) {
            return null;
        }

        List<DataAttribute> dataAttributesList = indicatorAttributeToDataAttribute(attributes);
        DataAttributes dataAttributes = new DataAttributes();
        for (DataAttribute dataAttribute : dataAttributesList) {
            dataAttributes.getAttributes().add(dataAttribute);
        }
        dataAttributes.setTotal(BigInteger.valueOf(attributes.size()));
        return dataAttributes;
    }

    private static List<DataAttribute> indicatorAttributeToDataAttribute(List<Map<String, AttributeType>> attributes) {
        List<DataAttribute> dataAttributes = new ArrayList<>();
        for (Map<String, AttributeType> attribute : attributes) {
            if (attribute == null || attribute.isEmpty()) {
                continue;
            }
            List<String> ids = new ArrayList<>();
            List<String> values = new ArrayList<>();
            for (Map.Entry<String, AttributeType> entry : attribute.entrySet()) {
                String value = indicatorAttributeValueToDataAttributeValue(entry.getValue());
                if (StringUtils.isNotBlank(value)) {
                    ids.add(entry.getKey());
                    values.add(value);
                }
            }
            if (!values.isEmpty()) {
                DataAttribute dataAttribute = new DataAttribute();
                dataAttribute.setId(String.join(OBSERVATIONS_SEPARATOR, ids));
                dataAttribute.setValue(String.join(OBSERVATIONS_SEPARATOR, values));
                dataAttributes.add(dataAttribute);
            }
        }
        return dataAttributes;
    }

    private static String indicatorAttributeValueToDataAttributeValue(AttributeType attribute) {
        if (attribute == null || attribute.getValue() == null) {
            return StringUtils.EMPTY;
        }
        return localisedStringsToDefaultString(attribute.getValue());
    }

    private static String localisedStringsToDefaultString(Map<String, String> localisedStrings) {
        return localisedStrings.get(DEFAULT);
    }
    private static DimensionRepresentations indicatorDimensionsToDataDimensions(Map<String, DataDimensionType> dataDimensions) {
        if (dataDimensions == null) {
            return null;
        }
        DimensionRepresentations dimensionRepresentations = new DimensionRepresentations();

        for (Map.Entry<String, DataDimensionType> entry : dataDimensions.entrySet()) {
            dimensionRepresentations.getDimensions().add(indicatorDataDimensionToDataDimensionRepresentation(entry.getKey(), entry.getValue()));
        }
        return dimensionRepresentations;
    }

    private static DimensionRepresentation indicatorDataDimensionToDataDimensionRepresentation(String index, DataDimensionType dataDimension) {
        DimensionRepresentation dimensionRepresentation = new DimensionRepresentation();
        dimensionRepresentation.setDimensionId(index);
        dimensionRepresentation.setRepresentations(indicatorDataDimensionTypeToDataCodeRepresentations(dataDimension));
        return dimensionRepresentation;
    }

    private static CodeRepresentations indicatorDataDimensionTypeToDataCodeRepresentations(DataDimensionType dataDimension) {
        CodeRepresentations codeRepresentations = new CodeRepresentations();
        codeRepresentations.setTotal(BigInteger.valueOf(dataDimension.getRepresentation().getSize()));
        for (Map.Entry<String, Integer> entry : dataDimension.getRepresentation().getIndex().entrySet()) {
            CodeRepresentation codeRepresentation = new CodeRepresentation();
            codeRepresentation.setCode(entry.getKey());
            codeRepresentation.setIndex(entry.getValue());
            codeRepresentations.getRepresentations().add(codeRepresentation);
        }
        return codeRepresentations;
    }

    private static List<MetadataAttributeType> indicatorAttributesToListAttributes(Map<String, MetadataAttributeType> attributes) {
        List<MetadataAttributeType> listAttribute = new ArrayList<>();
        for (Map.Entry<String, MetadataAttributeType> metadataAttributeTypeMap : attributes.entrySet()) {
            listAttribute.add(metadataAttributeTypeMap.getValue());
        }
        return listAttribute;
    }

}