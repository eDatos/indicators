package es.gobcan.istac.indicators.rest.mapper;

import static es.gobcan.istac.indicators.rest.domain.IndicatorSelection.FIXED_DIMENSIONS_START_POSITION;
import static es.gobcan.istac.indicators.rest.domain.IndicatorSelection.LEFT_DIMENSIONS_START_POSITION;
import static es.gobcan.istac.indicators.rest.domain.IndicatorSelection.TOP_DIMENSIONS_START_POSITION;
import static es.gobcan.istac.indicators.rest.enume.LabelVisualisationModeEnum.CODE_AND_LABEL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response.Status;

import org.siemac.metamac.rest.exception.RestCommonServiceExceptionType;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DataStructureDefinition;

import es.gobcan.istac.indicators.rest.domain.IndicatorSelection;
import es.gobcan.istac.indicators.rest.domain.IndicatorSelectionAttribute;
import es.gobcan.istac.indicators.rest.domain.IndicatorSelectionDimension;
import es.gobcan.istac.indicators.rest.enume.LabelVisualisationModeEnum;
import es.gobcan.istac.indicators.rest.types.AttributeType;
import es.gobcan.istac.indicators.rest.types.DataDimensionType;
import es.gobcan.istac.indicators.rest.types.DataRepresentationType;

public class IndicatorSelectionMapper {

    private static final int MAX_SIZE_URL = 2000;

    public static IndicatorSelection toIndicatorSelection(IndicatorSelection source) throws Exception {
        List<IndicatorSelectionDimension> dimensions = toIndicatorSelectionDimensions(source);
        List<IndicatorSelectionAttribute> attributes = toIndicatorSelectionAttributes(source);
        return new IndicatorSelection(dimensions, attributes);
    }

    private static List<IndicatorSelectionDimension> toIndicatorSelectionDimensions(IndicatorSelection source) throws Exception {
        if (source == null || source.getDimensions() == null) {
            return null;
        }
        List<IndicatorSelectionDimension> dimensions = new ArrayList<IndicatorSelectionDimension>();
        // List<IndicatorSelectionDimension> dimensions = new ArrayList<IndicatorSelectionDimension>(source.getDimensions().getDimensions().size());
        // for (Iterator<IndicatorSelection> iterator = source.getDimensions().getDimensions().iterator(); iterator.hasNext();) {
        // IndicatorSelection dimensionSource = iterator.next();
        // IndicatorSelectionDimension target = toIndicatorSelectionDimension(dimensionSource);
        // dimensions.add(target);
        // }
        return dimensions;
    }

    private static IndicatorSelectionDimension toIndicatorSelectionDimension(IndicatorSelection source) throws Exception {
        // IndicatorSelectionDimension target = new IndicatorSelectionDimension(source.getDimensionId());
        // target.setSelectedDimensionValues(source.getDimensionValues().getDimensionValues());
        // target.setPosition(source.getPosition());
        // target.setLabelVisualisationMode(toLabelVisualisationMode(source.getLabelVisualisationMode()));
        // return target;
        return null;
    }

    private static List<IndicatorSelectionAttribute> toIndicatorSelectionAttributes(IndicatorSelection source) throws Exception {
        // if (source == null || source.getAttributes() == null) {
        // return null;
        // }
        // List<IndicatorSelectionAttribute> attributes = new ArrayList<IndicatorSelectionAttribute>(source.getAttributes().getAttributes().size());
        // for (Iterator<IndicatorSelectionAttribute> iterator = source.getAttributes().getAttributes().iterator(); iterator.hasNext();) {
        // IndicatorSelectionAttribute attributeSource = iterator.next();
        // IndicatorSelectionAttribute target = toIndicatorSelectionAttribute(attributeSource);
        // attributes.add(target);
        // }
        return null;
    }

    // private static IndicatorSelectionAttribute toIndicatorSelectionAttribute(IndicatorSelectionAttribute source) throws Exception {
    // IndicatorSelectionAttribute target = new IndicatorSelectionAttribute(source.getAttributeId());
    // target.setLabelVisualisationMode(toLabelVisualisationMode(source.getLabelVisualisationMode()));
    // return target;
    // }

    private static LabelVisualisationModeEnum toLabelVisualisationMode(LabelVisualisationModeEnum source) {
        if (source == null) {
            return null;
        }
        switch (source) {
            case LABEL:
                return LabelVisualisationModeEnum.LABEL;
            case CODE:
                return LabelVisualisationModeEnum.CODE;
            case CODE_AND_LABEL:
                return CODE_AND_LABEL;
            default:
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestCommonServiceExceptionType.UNKNOWN);
                throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
        }
    }

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
        // FIXME
        // if (exportationBody != null && exportationBody.getIndicatorSelection() != null) {
        // if (exportationBody.getIndicatorSelection().getDimensions() != null) {
        // selectionDimensionsMap = indexDimensionsById(exportationBody.getIndicatorSelection().getDimensions().getDimensions());
        // }
        // if (exportationBody.getIndicatorSelection().getAttributes() != null) {
        // selectionAttributesMap = indexAttributesById(exportationBody.getIndicatorSelection().getAttributes().getAttributes());
        // }
        // }
        List<IndicatorSelectionDimension> dimensions = dimensionsToIndicatorSelectionDimensions(indicatorDimensions, selectionDimensionsMap, relatedDsd);
        // List<IndicatorSelectionAttribute> attributes = attributesToIndicatorSelectionAttributes(indicatorAttributes, selectionAttributesMap);
        return new IndicatorSelection(dimensions, null, false);
    }

    // @SuppressWarnings("unchecked")
    // private static Map<String, IndicatorSelection> indexDimensionsById(List<IndicatorSelection> dimensions) {
    // if (dimensions == null) {
    // return MapUtils.EMPTY_MAP;
    // }
    // Map<String, IndicatorSelection> a = new HashMap<String, IndicatorSelection>();
    // for (IndicatorSelection dimension : dimensions) {
    // a.put(dimension.getDimensionId(), dimension);
    // }
    // return a;
    // }
    //
    // @SuppressWarnings("unchecked")
    // private static Map<String, IndicatorSelectionAttribute> indexAttributesById(List<IndicatorSelectionAttribute> attributes) {
    // if (attributes == null) {
    // return MapUtils.EMPTY_MAP;
    // }
    // Map<String, IndicatorSelectionAttribute> a = new HashMap<String, IndicatorSelectionAttribute>();
    // for (IndicatorSelectionAttribute attribute : attributes) {
    // a.put(attribute.getAttributeId(), attribute);
    // }
    // return a;
    // }

    private static List<IndicatorSelectionAttribute> attributesToIndicatorSelectionAttributes(List<Map<String, AttributeType>> attributes,
            Map<String, IndicatorSelectionAttribute> selectionAttributesMap) {
        if (attributes == null) {
            return null;
        }
        List<IndicatorSelectionAttribute> indicatorSelectionAttributes = new ArrayList<IndicatorSelectionAttribute>();
    // for (Attribute attribute : attributes.getAttributes()) {
    // finalIndicatorSelectionAttribute selectionAttribute = selectionAttributesMap != null ? selectionAttributesMap.get(attribute.getId()) : null;
    // indicatorSelectionAttributes.add(attributeToIndicatorSelectionAttribute(attribute, selectionAttribute));
    // }
    // return indicatorSelectionAttributes;
    // }

    // private static IndicatorSelectionAttribute attributeToIndicatorSelectionAttribute(Attribute attribute, IndicatorSelectionAttribute selectionAttribute) {
    // IndicatorSelectionAttribute indicatorSelectionAttribute = new IndicatorSelectionAttribute(attribute.getId());
    //
    // // Default values
    // LabelVisualisationModeEnum labelVisualizationMode = CODE_AND_LABEL;
    //
    // // If we have data sent via api, use that instead
    // if (selectionAttribute != null) {
    // if (selectionAttribute.getLabelVisualisationMode() != null) {
    // labelVisualizationMode = toLabelVisualisationMode(selectionAttribute.getLabelVisualisationMode());
    // }
    // }
    //
    // indicatorSelectionAttribute.setLabelVisualisationMode(labelVisualizationMode);
    return null;
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
        // Integer position = dataStructureDefinitionToPosition(dimension.getValue().getRepresen, dataStructureDefinition);
        //
        indicatorSelectionDimension.setLabelVisualisationMode(labelVisualizationMode);
        indicatorSelectionDimension.setPosition(position);
        indicatorSelectionDimension.setSelectedDimensionValues(codeRepresentationsToSelectedDimensionValues(dimension.getValue().getRepresentation()));
        return indicatorSelectionDimension;
    }

    private static Integer dataStructureDefinitionToPosition(String id, DataStructureDefinition dataStructureDefinition) {
        if (dataStructureDefinition.getHeading().getDimensionIds().contains(id)) {
            return TOP_DIMENSIONS_START_POSITION + dataStructureDefinition.getHeading().getDimensionIds().indexOf(id);
        } else if (dataStructureDefinition.getStub().getDimensionIds().contains(id)) {
            return LEFT_DIMENSIONS_START_POSITION + dataStructureDefinition.getStub().getDimensionIds().indexOf(id);
        } else {
            return FIXED_DIMENSIONS_START_POSITION;
        }
    }

    private static List<String> codeRepresentationsToSelectedDimensionValues(DataRepresentationType dataRepresentation) {
        List<String> selectedDimensionValues = new ArrayList<String>();
        for (Map.Entry<String, Integer> index : dataRepresentation.getIndex().entrySet()) {
            selectedDimensionValues.add(index.getKey());
        }
        return selectedDimensionValues;
    }

}