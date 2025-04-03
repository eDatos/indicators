package es.gobcan.istac.indicators.rest;

import static es.gobcan.istac.indicators.rest.util.ExportUtils.buildMapDimensionLabel;
import static es.gobcan.istac.indicators.rest.util.ExportUtils.buildMapDimensionToMapDimensionsLabelVisualisationMode;
import static es.gobcan.istac.indicators.rest.util.ExportUtils.buildMapDimensionsValuesLabels;
import static es.gobcan.istac.indicators.rest.util.ExportUtils.buildMapDimensionsValuesLocalisedLabels;
import static es.gobcan.istac.indicators.rest.util.ExportUtils.dataToDataArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Attribute;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.AttributeAttachmentLevelType;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.AttributeDimension;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Attributes;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.CodeRepresentation;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ComponentType;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Data;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DataAttribute;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DataStructureDefinition;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DatasetBase;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DatasetMetadataBase;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Dimension;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DimensionRepresentation;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DimensionType;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Dimensions;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.EnumeratedAttributeValue;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.EnumeratedDimensionValue;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.EnumeratedDimensionValues;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.MeasureQuantity;

import es.gobcan.istac.indicators.rest.enume.LabelVisualisationModeEnum;
import es.gobcan.istac.indicators.rest.types.DataType;
import es.gobcan.istac.indicators.rest.util.ExportUtils;

public class ResourceAccess {

    private String                                        lang;
    private String                                        langDefault;

    private Data                                          data;
    private Dimensions                                    dimensions;
    private Attributes                                    attributes;

    private InternationalString                           name;
    private DatasetMetadataBase                           metadata;
    private DataStructureDefinition                       relatedDsd;
    private String                                        urn;
    private String                                        id;
    private String                                        uniqueId;
    private InternationalString                           description;

    // Metadata
    private List<Dimension>                               dimensionsMetadata;
    private Map<String, Dimension>                        dimensionsMetadataMap;
    private Dimension                                     measureDimension;
    private Map<String, String>                           dimensionLabelsCurrentLocale;
    private Map<String, String>                           dimensionLabelsDefaultLocale;
    private Map<String, Map<String, InternationalString>> dimensionsValuesCurrentLocaleLabels;
    private Map<String, Map<String, InternationalString>> dimensionsValuesLabels;
    private Map<String, LabelVisualisationModeEnum>       dimensionsLabelVisualisationMode;

    private List<Attribute>                               attributesMetadata;
    private Map<String, Attribute>                        attributesMetadataMap;
    private Attribute                                     measureAttribute;
    private Map<String, String>                           attributesLabels;
    private Map<String, Map<String, String>>              attributesValuesCurrentLocaleLabels;
    private Map<String, Map<String, InternationalString>> attributesValuesLabels;
    private Map<String, LabelVisualisationModeEnum>       attributesLabelVisualisationMode;

    // Data
    private String[]                                      observations;
    private Map<String, String[]>                         attributesValuesByAttributeId;
    private List<String>                                  dimensionsOrderedForData;
    private Map<String, List<String>>                     dimensionValuesOrderedForDataByDimensionId;

    private DatasetBase                                   dataset;

    private final Map<String, Integer>                    multipliers                   = new HashMap<String, Integer>();
    private final Map<String, Map<String, Integer>>       multipliersByAttribute        = new HashMap<String, Map<String, Integer>>();
    private final Map<String, Map<String, Long>>          representationIndex           = new HashMap<String, Map<String, Long>>();   // Map<Dimension, Map<Code, Index>

    private int                                           primaryMeasureAttributesCount = 0;

    // private SrmRestExternalFacade srmRestExternalFacade;

    public ResourceAccess(DataType indicator) throws MetamacException {
        data = dataset.getData();
        dimensions = dataset.getMetadata().getDimensions();
        attributes = dataset.getMetadata().getAttributes();

        uniqueId = dataset.getId();
        // if (datasetSelection != null && datasetSelection.isUserSelection()) {
        // uniqueId = PxExporter.generateMatrixFromString(dataset.getId());
        // }

        name = dataset.getName();
        id = dataset.getId();
        urn = dataset.getUrn();
        description = dataset.getDescription();
        relatedDsd = dataset.getMetadata().getRelatedDsd();

        this.dataset = dataset;
        metadata = dataset.getMetadata();

        initialize(data, dimensions, attributes);
    }

    private void initialize(Data data, Dimensions dimensions, Attributes attributes) throws MetamacException {
        // this.lang = lang;
        // this.langDefault = langDefault;

        initializeDimensions(dimensions);
        initializeAttributes(data, attributes);
        initializeObservations(data);
        initializeDimensionsForData(data);
        initializeMultipliers();
        initializeMultipliersAttributes();
        initializeIndex();
    }

    public Data getData() {
        return data;
    }

    public Dimensions getDimensions() {
        return dimensions;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public InternationalString getName() {
        return name;
    }

    public DatasetBase getDataset() {
        return dataset;
    }
    public DatasetMetadataBase getMetadata() {
        return metadata;
    }

    public DataStructureDefinition getRelatedDsd() {
        return relatedDsd;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public String getId() {
        return id;
    }

    public String getUrn() {
        return urn;
    }

    public InternationalString getDescription() {
        return description;
    }

    public String getLang() {
        return lang;
    }

    public String getLangDefault() {
        return langDefault;
    }

    public List<Dimension> getDimensionsMetadata() {
        return dimensionsMetadata;
    }

    public Map<String, Dimension> getDimensionsMetadataMap() {
        return dimensionsMetadataMap;
    }

    public List<Attribute> getAttributesMetadata() {
        return attributesMetadata;
    }

    public Map<String, Attribute> getAttributesMetadataMap() {
        return attributesMetadataMap;
    }

    public Attribute getMeasureAttribute() {
        return measureAttribute;
    }

    public Dimension getMeasureDimension() {
        return measureDimension;
    }

    public String getDimensionLabelCurrentLocale(String dimensionId) {
        return dimensionLabelsCurrentLocale.get(dimensionId);
    }

    public String getDimensionLabelDefaultLocale(String dimensionId) {
        return dimensionLabelsDefaultLocale.get(dimensionId);
    }

    public String getDimensionValueLabelCurrentLocale(String dimensionId, String dimensionValueId) {
        return dimensionsValuesCurrentLocaleLabels.get(dimensionId).get(dimensionValueId).toString();
    }

    public InternationalString getDimensionValueLabel(String dimensionId, String dimensionValueId) {
        return dimensionsValuesLabels.get(dimensionId).get(dimensionValueId);
    }

    public String getAttributeLabel(String attributeId) {
        return attributesLabels.get(attributeId);
    }

    public String getAttributeValueLabelCurrentLocale(String attributeId, String attributeValue) {
        return attributesValuesCurrentLocaleLabels.get(attributeId).get(attributeValue);
    }

    public InternationalString getAttributeValue(String attributeId, String attributeValue) {
        return attributesValuesLabels.get(attributeId).get(attributeValue);
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

    public List<String> getDimensionsAttributeOrderedForData(Attribute attribute) {
        List<String> allDimensionsOrderedForData = getDimensionsOrderedForData();
        if (AttributeAttachmentLevelType.DIMENSION.equals(attribute.getAttachmentLevel())) {
            List<String> dimensionsAttribute = new ArrayList<String>();
            for (AttributeDimension attributeDimension : attribute.getDimensions().getDimensions()) {
                dimensionsAttribute.add(attributeDimension.getDimensionId());
            }
            List<String> dimensionsAttributeOrdered = new ArrayList<String>(dimensionsAttribute.size());
            for (String dimensionDatasetId : getDimensionsOrderedForData()) {
                if (dimensionsAttribute.contains(dimensionDatasetId)) {
                    dimensionsAttributeOrdered.add(dimensionDatasetId);
                }
            }
            return dimensionsAttributeOrdered;
        } else if (AttributeAttachmentLevelType.PRIMARY_MEASURE.equals(attribute.getAttachmentLevel())) {
            return allDimensionsOrderedForData;
        } else {
            throw new IllegalArgumentException("Attribute attachement level unsupported in this operation: " + attribute.getAttachmentLevel());
        }
    }

    /**
     * Init dimensions and dimensions values
     */
    private void initializeDimensions(Dimensions dimensions) throws MetamacException {
        dimensionsMetadata = dimensions.getDimensions();

        Map<String, Dimension> dimensionsMetadataMap = new HashMap<String, Dimension>(dimensionsMetadata.size());
        Map<String, LabelVisualisationModeEnum> labelVisualisationsMode = new HashMap<String, LabelVisualisationModeEnum>(dimensionsMetadata.size());
        // TODO: Corregir
        // Map<String, Map<String, InternationalString>> dimensionsValuesCurrentLocaleLabels = new HashMap<String, Map<String, String>>(dimensionsMetadata.size());
        Map<String, Map<String, InternationalString>> dimensionsValuesLabels = new HashMap<String, Map<String, InternationalString>>(dimensionsMetadata.size());
        Map<String, String> dimensionsLabelsCurrentLocale = new HashMap<String, String>(dimensionsMetadata.size());
        Map<String, String> dimensionsLabelsDefaultLocale = new HashMap<String, String>(dimensionsMetadata.size());

        for (Dimension dimension : dimensionsMetadata) {
            String dimensionId = dimension.getId();

            dimensionsMetadataMap.put(dimensionId, dimension);
            labelVisualisationsMode.put(dimensionId, buildMapDimensionToMapDimensionsLabelVisualisationMode(dimension));
            dimensionsValuesCurrentLocaleLabels.put(dimension.getId(), buildMapDimensionsValuesLabels(dimension));
            dimensionsValuesLabels.put(dimension.getId(), buildMapDimensionsValuesLocalisedLabels(dimension));
            dimensionsLabelsCurrentLocale.put(dimensionId, buildMapDimensionLabel(dimension, lang, langDefault));
            dimensionsLabelsDefaultLocale.put(dimensionId, buildMapDimensionLabel(dimension, langDefault, langDefault));

            if (DimensionType.MEASURE_DIMENSION.equals(dimension.getType())) {
                measureDimension = dimension;
            }
        }

        this.dimensionsMetadataMap = dimensionsMetadataMap;
        dimensionsLabelVisualisationMode = labelVisualisationsMode;
        this.dimensionsValuesCurrentLocaleLabels = dimensionsValuesCurrentLocaleLabels;
        this.dimensionsValuesLabels = dimensionsValuesLabels;
        dimensionLabelsCurrentLocale = dimensionsLabelsCurrentLocale;
        dimensionLabelsDefaultLocale = dimensionsLabelsDefaultLocale;
    }

    /**
     * Init definitions and values of attributes
     *
     * @param attributes
     */
    private void initializeAttributes(Data data, Attributes attributes) throws MetamacException {
        if (attributes == null) {
            attributesMetadata = new ArrayList<Attribute>();
        } else {
            attributesMetadata = attributes.getAttributes();
        }

        Map<String, Attribute> attributesMetadataMap = new HashMap<String, Attribute>(attributesMetadata.size());

        // Attribute Instances
        attributesValuesByAttributeId = new HashMap<String, String[]>(attributesMetadata.size());
        for (Attribute attribute : attributesMetadata) {
            if (data.getAttributes() != null) {
                for (DataAttribute dataAttribute : data.getAttributes().getAttributes()) {
                    if (dataAttribute.getId().equals(attribute.getId())) {
                        attributesValuesByAttributeId.put(attribute.getId(), dataToDataArray(dataAttribute.getValue()));
                    }
                }
            }

            if (AttributeAttachmentLevelType.PRIMARY_MEASURE.equals(attribute.getAttachmentLevel())) {
                primaryMeasureAttributesCount += calculateNonEmptyCount(attributesValuesByAttributeId.get(attribute.getId()));
            }

            // Measure Attribute
            if (ComponentType.MEASURE.equals(attribute.getType())) {
                measureAttribute = attribute;
            }

            // Attributes Metadata Map
            attributesMetadataMap.put(attribute.getId(), attribute);
        }
        // TODO: corregir
        // this.attributesMetadataMap = attributesMetadataMap;
        // attributesLabelVisualisationMode = buildMapAttributesLabelVisualisationMode(attributesMetadata);
        // attributesValuesCurrentLocaleLabels = buildMapAttributesValuesLabels(attributesMetadata, lang, langDefault);
        // attributesValuesLabels = buildMapAttributesValuesLocalisedLabels(attributesMetadata);
        // attributesLabels = buildMapAttributesLabels(attributesMetadata, lang, langDefault);
    }

    private int calculateNonEmptyCount(String[] strings) {
        if (strings == null) {
            return 0;
        }
        int totalNonEmpty = 0;
        for (int i = 0; i < strings.length; i++) {
            if (StringUtils.isNotBlank(strings[i])) {
                totalNonEmpty++;
            }
        }
        return totalNonEmpty;
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

    public String applyLabelVisualizationModeForAttributeValue(String attributeId, String attributeValue) {
        // Visualisation mode
        LabelVisualisationModeEnum labelVisualisation = getAttributeLabelVisualisationMode(attributeId);
        switch (labelVisualisation) {
            case CODE:
                // no extra action
                break;
            case LABEL: {
                String attributeValueLabel = getAttributeValueLabelCurrentLocale(attributeId, attributeValue);
                if (attributeValueLabel != null) {
                    attributeValue = attributeValueLabel;
                }
            }
                break;
            case CODE_AND_LABEL: {
                String attributeValueLabel = getAttributeValueLabelCurrentLocale(attributeId, attributeValue);
                if (attributeValueLabel != null) {
                    attributeValue = attributeValueLabel + " (" + attributeValue + ")";
                }
            }
                break;
            default:
                break;
        }
        return attributeValue;
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

    public String obtainAttributeValue(String attributeId, int offset) {
        String[] attributeValues = getAttributeValues(attributeId);
        String attributeValue = null;
        if (attributeValues != null) {
            attributeValue = attributeValues[offset];
            attributeValue = applyLabelVisualizationModeForAttributeValue(attributeId, attributeValue);
        }
        return attributeValue;
    }

    public String applyLabelVisualizationModeForAttribute(String attributeId) {
        // Visualisation mode
        LabelVisualisationModeEnum labelVisualisation = getAttributeLabelVisualisationMode(attributeId);
        String resultText = null;
        switch (labelVisualisation) {
            case CODE:
                resultText = attributeId;
                break;
            case LABEL: {
                String attributeLabel = getAttributeLabel(attributeId);
                if (attributeLabel != null) {
                    resultText = attributeLabel;
                }
            }
                break;
            case CODE_AND_LABEL: {
                String attributeLabel = getAttributeLabel(attributeId);
                if (attributeLabel != null) {
                    resultText = attributeLabel + " (" + attributeId + ")";
                }
            }
                break;
            default:
                break;
        }
        return resultText;
    }

    public String applyLabelVisualizationModeForDimension(String dimensionId) {
        LabelVisualisationModeEnum labelVisualisation = getDimensionLabelVisualisationMode(dimensionId);
        String resultText = null;
        switch (labelVisualisation) {
            case CODE:
                resultText = dimensionId;
                break;
            case LABEL: {
                String dimensionValueLabel = getDimensionLabelCurrentLocale(dimensionId);
                if (dimensionValueLabel != null) {
                    resultText = dimensionValueLabel;
                }
            }
                break;
            case CODE_AND_LABEL: {
                String dimensionValueLabel = getDimensionLabelCurrentLocale(dimensionId);
                if (dimensionValueLabel != null) {
                    resultText = dimensionValueLabel + " (" + dimensionId + ")";
                }
            }
                break;
            default:
                break;
        }
        return resultText;
    }

    public String applyLabelVisualizationModeForDimensionValue(String dimensionId, String dimensionValueId) {
        LabelVisualisationModeEnum labelVisualisation = getDimensionLabelVisualisationMode(dimensionId);
        switch (labelVisualisation) {
            case CODE:
                // no extra action
                break;
            case LABEL: {
                String dimensionValueLabel = getDimensionValueLabelCurrentLocale(dimensionId, dimensionValueId);
                if (dimensionValueLabel != null) {
                    dimensionValueId = dimensionValueLabel;
                }
            }
                break;
            case CODE_AND_LABEL: {
                String dimensionValueLabel = getDimensionValueLabelCurrentLocale(dimensionId, dimensionValueId);
                if (dimensionValueLabel != null) {
                    dimensionValueId = dimensionValueLabel + " (" + dimensionValueId + ")";
                }
            }
                break;
            default:
                break;
        }
        return dimensionValueId;
    }

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

    // ... and we'll have the multipliers stored for attributes at DIMENSION or combinated DIMENSION attachment level
    private void initializeMultipliersAttributes() {
        for (Attribute attribute : attributesMetadata) {
            if (!AttributeAttachmentLevelType.DIMENSION.equals(attribute.getAttachmentLevel())) {
                continue;
            }
            multipliersByAttribute.put(attribute.getId(), new HashMap<String, Integer>());
            List<AttributeDimension> dimensions = attribute.getDimensions().getDimensions();
            ListIterator<AttributeDimension> dimensionsListIterator = dimensions.listIterator(dimensions.size());
            int incrementCounter = 1;
            // Iterate the list in reverse order: right to left or down to up in the display table to calculate cell spacing
            while (dimensionsListIterator.hasPrevious()) {
                AttributeDimension dimension = dimensionsListIterator.previous();
                multipliersByAttribute.get(attribute.getId()).put(dimension.getDimensionId(), incrementCounter);
                incrementCounter *= dimensionValuesOrderedForDataByDimensionId.get(dimension.getDimensionId()).size();
            }
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

    public InternationalString extractUnitCode(EnumeratedDimensionValue dimensionValue, String unitMeasureKey, String unitMultiplierKey) {
        Integer offset = calculateOffsetDimensionValueId(dimensionValue.getId(), unitMeasureKey);
        InternationalString unitMeasureName = getAttributeByDimensionValue(unitMeasureKey, offset);

        offset = calculateOffsetDimensionValueId(dimensionValue.getId(), unitMultiplierKey);
        InternationalString unitMultiplierName = getAttributeByDimensionValue(unitMultiplierKey, offset);
        return extractUnitCode(dimensionValue.getMeasureQuantity(), unitMeasureName, unitMultiplierName);
    }

    public InternationalString extractUnitCode(EnumeratedAttributeValue attributeValue, String unitMeasureKey, String unitMultiplierKey) {
        InternationalString unitMeasureName = getAttributeByDimensionValue(unitMeasureKey, 0);
        InternationalString unitMultiplierName = getAttributeByDimensionValue(unitMultiplierKey, 0);
        return extractUnitCode(attributeValue.getMeasureQuantity(), unitMeasureName, unitMultiplierName);
    }

    private InternationalString extractUnitCode(MeasureQuantity measureQuantity, InternationalString unitMeasureName, InternationalString unitMultiplierName) {
        if (unitMeasureName == null && measureQuantity != null && measureQuantity.getUnitCode() != null) {
            unitMeasureName = measureQuantity.getUnitCode().getName();
        }
        if (unitMultiplierName == null && measureQuantity != null && measureQuantity.getUnitMultiplier() != null) {
            unitMultiplierName = measureQuantity.getUnitMultiplier().getName();
        }
        return prepareQuantityInternationalString(unitMeasureName, unitMultiplierName);
    }

    private InternationalString getAttributeByDimensionValue(String attributeKey, Integer offset) {
        if (!attributesValuesByAttributeId.containsKey(attributeKey)) {
            return null;
        }
        String attributeValue = attributesValuesByAttributeId.get(attributeKey)[offset];
        return attributesValuesLabels.get(attributeKey).get(attributeValue);
    }

    private Integer calculateOffsetDimensionValueId(String dimensionValueId, String attributeKey) {
        Integer offset = null;
        if (multipliersByAttribute.containsKey(attributeKey)) {
            Map<String, String> permutation = new HashMap<>();
            permutation.put(measureDimension.getId(), dimensionValueId);
            offset = calculateOffsetAtPermutation(permutation, multipliersByAttribute.get(attributeKey));
        }
        return offset;
    }

    public boolean existsContVariable() {
        return getMeasureDimension() != null;
    }

    public List<EnumeratedDimensionValue> getSelectedValuesForMeasureDimension() {
        List<EnumeratedDimensionValue> result = new ArrayList<>();

        Dimension measureDimension = getMeasureDimension();
        if (!(measureDimension.getDimensionValues() instanceof EnumeratedDimensionValues)) {
            return result;
        }

        // List<String> selectedDimensionValues = datasetSelection.getDimension(measureDimension.getId()).getSelectedDimensionValues();
        List<String> selectedDimensionValues = null;
        for (EnumeratedDimensionValue dimensionValue : ((EnumeratedDimensionValues) measureDimension.getDimensionValues()).getValues()) {
            if (selectedDimensionValues.contains(dimensionValue.getId())) {
                result.add(dimensionValue);
            }
        }
        return result;
    }

    private InternationalString prepareQuantityInternationalString(InternationalString unitMeasure, InternationalString unitMultiplier) {
        InternationalString result = null;

        if (unitMeasure != null) {
            result = new InternationalString();
            for (LocalisedString unitMeasureLocalisedString : unitMeasure.getTexts()) {
                String lang = unitMeasureLocalisedString.getLang();
                String unitMeasureLabel = unitMeasureLocalisedString.getValue();
                String unitMultiplierLabel = ExportUtils.getLabel(unitMultiplier, lang);

                String value = unitMeasureLabel;
                value += (unitMultiplierLabel != null) ? " (" + unitMultiplierLabel + ")" : "";

                LocalisedString localisedString = new LocalisedString();
                localisedString.setLang(lang);
                localisedString.setValue(value);
                result.getTexts().add(localisedString);
            }
        } else if (unitMultiplier != null) {
            result = new InternationalString();
            for (LocalisedString localisedString : unitMultiplier.getTexts()) {
                localisedString.setValue("(" + localisedString.getValue() + ")");
                result.getTexts().add(localisedString);
            }
        }
        return result;
    }

    public int getPrimaryMeasureAttributesCount() {
        return primaryMeasureAttributesCount;
    }
}