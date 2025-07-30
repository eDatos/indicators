package es.gobcan.istac.indicators.rest.mapper;

import static es.gobcan.istac.indicators.rest.enume.LabelVisualisationModeEnum.CODE_AND_LABEL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DataStructureDefinition;

import es.gobcan.istac.indicators.rest.dto.IndicatorSelection;
import es.gobcan.istac.indicators.rest.dto.IndicatorSelectionAttribute;
import es.gobcan.istac.indicators.rest.dto.IndicatorSelectionDimension;
import es.gobcan.istac.indicators.rest.enume.LabelVisualisationModeEnum;
import es.gobcan.istac.indicators.rest.types.AttributeType;
import es.gobcan.istac.indicators.rest.types.DataDimensionType;
import es.gobcan.istac.indicators.rest.types.DataRepresentationType;

public class IndicatorSelectionMapper {

    /**
     * @param indicatorDimensions
     * @param indicatorAttributes
     * @param relatedDsd
     * @param exportationBody
     *            We generate the dimensions and attributes object with the one that compose the dataset, where it´s data has been previously
     *            filtered previously when it was retrieved. We enrich the object with the exportationBody, to take into account the user input
     */
    public static IndicatorSelection indicatorToIndicatorSelection(Map<String, DataDimensionType> indicatorDimensions, List<Map<String, AttributeType>> indicatorAttributes,
            DataStructureDefinition relatedDsd, String exportationBody) {
        Map<String, IndicatorSelection> selectionDimensionsMap = null;
        Map<String, IndicatorSelectionAttribute> selectionAttributesMap = null;
        List<IndicatorSelectionDimension> dimensions = dimensionsToIndicatorSelectionDimensions(indicatorDimensions, selectionDimensionsMap, relatedDsd);
        List<IndicatorSelectionAttribute> attributes = attributesToIndicatorSelectionAttributes(indicatorAttributes, selectionAttributesMap);
        return new IndicatorSelection(dimensions, attributes);
    }

    private static List<IndicatorSelectionAttribute> attributesToIndicatorSelectionAttributes(List<Map<String, AttributeType>> attributes,
            Map<String, IndicatorSelectionAttribute> selectionAttributesMap) {

        if (attributes == null) {
            return null;
        }
        List<IndicatorSelectionAttribute> indicatorSelectionAttributes = new ArrayList<IndicatorSelectionAttribute>();

        for (Map<String, AttributeType> attributeTypeMap : attributes) {
            if (attributeTypeMap == null) {
                indicatorSelectionAttributes.add(attributeToIndicatorSelectionAttribute(null, null));
            } else {
                for (Map.Entry<String, AttributeType> attributeTypeEntry : attributeTypeMap.entrySet()) {
                    indicatorSelectionAttributes.add(attributeToIndicatorSelectionAttribute(attributeTypeEntry, selectionAttributesMap));
                }
            }
        }
        return indicatorSelectionAttributes;
    }

    private static IndicatorSelectionAttribute attributeToIndicatorSelectionAttribute(Map.Entry<String, AttributeType> attributeMap, Map<String, IndicatorSelectionAttribute> selectionAttributesMap) {

        String id = (attributeMap != null) ? attributeMap.getKey() : null;

        IndicatorSelectionAttribute selectionAttribute = (selectionAttributesMap != null) ? selectionAttributesMap.get(id) : null;

        if (selectionAttribute == null) {
            selectionAttribute = new IndicatorSelectionAttribute(id);
        }

        selectionAttribute.setLabelVisualisationMode(LabelVisualisationModeEnum.LABEL);
        return selectionAttribute;
    }

    private static List<IndicatorSelectionDimension> dimensionsToIndicatorSelectionDimensions(Map<String, DataDimensionType> indicatorDimensions,
            Map<String, IndicatorSelection> selectionDimensionsMap, DataStructureDefinition dataStructureDefinition) {
        List<IndicatorSelectionDimension> indicatorSelectionDimensions = new ArrayList<IndicatorSelectionDimension>();
        Integer position = 0;
        for (Map.Entry<String, DataDimensionType> dimension : indicatorDimensions.entrySet()) {
            indicatorSelectionDimensions.add(dimensionToIndicatorSelectionDimension(dimension, dataStructureDefinition, position));
            position++;
        }
        return indicatorSelectionDimensions;
    }

    private static IndicatorSelectionDimension dimensionToIndicatorSelectionDimension(Map.Entry<String, DataDimensionType> dimension, DataStructureDefinition dataStructureDefinition,
            Integer position) {
        IndicatorSelectionDimension indicatorSelectionDimension = new IndicatorSelectionDimension(dimension.getKey());

        // Default values
        LabelVisualisationModeEnum labelVisualizationMode = CODE_AND_LABEL;
        indicatorSelectionDimension.setLabelVisualisationMode(labelVisualizationMode);
        indicatorSelectionDimension.setPosition(position);
        indicatorSelectionDimension.setSelectedDimensionValues(codeRepresentationsToSelectedDimensionValues(dimension.getValue().getRepresentation()));
        return indicatorSelectionDimension;
    }
    private static List<String> codeRepresentationsToSelectedDimensionValues(DataRepresentationType dataRepresentation) {
        List<String> selectedDimensionValues = new ArrayList<String>();
        for (Map.Entry<String, Integer> index : dataRepresentation.getIndex().entrySet()) {
            selectedDimensionValues.add(index.getKey());
        }
        return selectedDimensionValues;
    }

}