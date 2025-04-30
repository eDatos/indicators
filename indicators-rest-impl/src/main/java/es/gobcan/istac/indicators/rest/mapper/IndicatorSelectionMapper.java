package es.gobcan.istac.indicators.rest.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Attribute;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Attributes;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.CodeRepresentation;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.CodeRepresentations;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DataStructureDefinition;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DimensionRepresentation;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DimensionRepresentations;

import es.gobcan.istac.indicators.rest.domain.IndicatorSelection;
import es.gobcan.istac.indicators.rest.domain.IndicatorSelectionAttribute;
import es.gobcan.istac.indicators.rest.domain.IndicatorSelectionDimension;
import es.gobcan.istac.indicators.rest.enume.LabelVisualisationModeEnum;

public class IndicatorSelectionMapper {

    private static final int MAX_SIZE_URL = 2000;

    public static IndicatorSelection toIndicatorSelection(IndicatorSelection source) throws Exception {
        List<IndicatorSelectionDimension> dimensions = toIndicatorSelectionDimensions(source);
        List<IndicatorSelectionAttribute> attributes = toIndicatorSelectionAttributes(source);
        return new IndicatorSelection(dimensions, attributes);
    }

    public static String toStatisticalResourcesApiRepresentationParameter(Exportation exportationBody) {
        if (exportationBody == null) {
            return null;
        }
        org.siemac.metamac.rest.export.v1_0.domain.IndicatorSelection datasetSelection = exportationBody.getIndicatorSelection();
        if (datasetSelection == null || datasetSelection.getDimensions() == null || datasetSelection.getDimensions().getDimensions() == null) {
            return null;
        }
        List<IndicatorSelection> dimensions = datasetSelection.getDimensions().getDimensions();

        StringBuilder sb = new StringBuilder();
        for (IndicatorSelection dimension : dimensions) {
            sb.append(dimension.getDimensionId());
            sb.append("[");

            if (dimension.getDimensionFilters() != null) {
                DimensionFilters dimensionFilters = dimension.getDimensionFilters();
                if (dimensionFilters.getAfter() != null) {
                    sb.append("~after=").append(dimensionFilters.getAfter()).append("|");
                }
                if (dimensionFilters.getLast() != null) {
                    sb.append("~last=").append(dimensionFilters.getLast()).append("|");
                }
                if (dimensionFilters.getRange() != null) {
                    sb.append("~range=").append(dimensionFilters.getRange().getStart()).append(";").append(dimensionFilters.getRange().getEnd()).append("|");
                }
            }
            if (dimension.getDimensionValues() != null && dimension.getDimensionValues().getDimensionValues() != null && dimension.getDimensionValues().getDimensionValues().size() > 0) {
                sb.append(StringUtils.join(dimension.getDimensionValues().getDimensionValues(), "|"));
            }
            if ('|' == sb.charAt(sb.length() - 1)) {
                sb.deleteCharAt(sb.length() - 1); // delete last |
            }

            sb.append("]");
            sb.append(":");
        }
        if (':' == sb.charAt(sb.length() - 1)) {
            sb.deleteCharAt(sb.length() - 1); // delete last :
        }

        if (sb.length() > MAX_SIZE_URL) {
            return null;
        }

        return sb.toString();
    }

    private static List<IndicatorSelectionDimension> toIndicatorSelectionDimensions(IndicatorSelection source) throws Exception {
        if (source == null || source.getDimensions() == null) {
            return null;
        }
        List<IndicatorSelectionDimension> dimensions = new ArrayList<IndicatorSelectionDimension>(source.getDimensions().getDimensions().size());
        for (Iterator<IndicatorSelection> iterator = source.getDimensions().getDimensions().iterator(); iterator.hasNext();) {
            IndicatorSelection dimensionSource = iterator.next();
            IndicatorSelectionDimension target = toIndicatorSelectionDimension(dimensionSource);
            dimensions.add(target);
        }
        return dimensions;
    }

    private static IndicatorSelectionDimension toIndicatorSelectionDimension(IndicatorSelection source) throws Exception {
        IndicatorSelectionDimension target = new IndicatorSelectionDimension(source.getDimensionId());
        target.setSelectedDimensionValues(source.getDimensionValues().getDimensionValues());
        target.setPosition(source.getPosition());
        target.setLabelVisualisationMode(toLabelVisualisationMode(source.getLabelVisualisationMode()));
        return target;
    }

    private static List<IndicatorSelectionAttribute> toIndicatorSelectionAttributes(org.siemac.metamac.rest.export.v1_0.domain.IndicatorSelection source) throws Exception {
        if (source == null || source.getAttributes() == null) {
            return null;
        }
        List<IndicatorSelectionAttribute> attributes = new ArrayList<IndicatorSelectionAttribute>(source.getAttributes().getAttributes().size());
        for (Iterator<org.siemac.metamac.rest.export.v1_0.domain.IndicatorSelectionAttribute> iterator = source.getAttributes().getAttributes().iterator(); iterator.hasNext();) {
            org.siemac.metamac.rest.export.v1_0.domain.IndicatorSelectionAttribute attributeSource = iterator.next();
            IndicatorSelectionAttribute target = toIndicatorSelectionAttribute(attributeSource);
            attributes.add(target);
        }
        return attributes;
    }

    private static IndicatorSelectionAttribute toIndicatorSelectionAttribute(org.siemac.metamac.rest.export.v1_0.domain.IndicatorSelectionAttribute source) throws Exception {
        IndicatorSelectionAttribute target = new IndicatorSelectionAttribute(source.getAttributeId());
        target.setLabelVisualisationMode(toLabelVisualisationMode(source.getLabelVisualisationMode()));
        return target;
    }

    private static LabelVisualisationModeEnum toLabelVisualisationMode(LabelVisualisationMode source) {
        if (source == null) {
            return null;
        }
        switch (source) {
            case LABEL:
                return LabelVisualisationModeEnum.LABEL;
            case CODE:
                return LabelVisualisationModeEnum.CODE;
            case CODE_AND_LABEL:
                return LabelVisualisationModeEnum.CODE_AND_LABEL;
            default:
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.UNKNOWN);
                throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @param dimensionRepresentations
     * @param datasetAttributes
     * @param relatedDsd
     * @param exportationBody
     *            We generate the dimensions and attributes object with the one that compose the dataset, where it´s data has been previously
     *            filtered previously when it was retrieved. We enrich the object with the exportationBody, to take into account the user input
     */
    public static IndicatorSelection datasetToIndicatorSelection(DimensionRepresentations dimensionRepresentations, Attributes datasetAttributes, DataStructureDefinition relatedDsd,
            Exportation exportationBody) {
        Map<String, IndicatorSelection> selectionDimensionsMap = null;
        Map<String, org.siemac.metamac.rest.export.v1_0.domain.IndicatorSelectionAttribute> selectionAttributesMap = null;
        if (exportationBody != null && exportationBody.getIndicatorSelection() != null) {
            if (exportationBody.getIndicatorSelection().getDimensions() != null) {
                selectionDimensionsMap = indexDimensionsById(exportationBody.getIndicatorSelection().getDimensions().getDimensions());
            }
            if (exportationBody.getIndicatorSelection().getAttributes() != null) {
                selectionAttributesMap = indexAttributesById(exportationBody.getIndicatorSelection().getAttributes().getAttributes());
            }
        }
        List<IndicatorSelectionDimension> dimensions = dimensionsToIndicatorSelectionDimensions(dimensionRepresentations, selectionDimensionsMap, relatedDsd);
        List<IndicatorSelectionAttribute> attributes = attributesToIndicatorSelectionAttributes(datasetAttributes, selectionAttributesMap);
        return new IndicatorSelection(dimensions, attributes, false);
    }

    @SuppressWarnings("unchecked")
    private static Map<String, IndicatorSelection> indexDimensionsById(List<IndicatorSelection> dimensions) {
        if (dimensions == null) {
            return MapUtils.EMPTY_MAP;
        }
        Map<String, IndicatorSelection> a = new HashMap<String, IndicatorSelection>();
        for (IndicatorSelection dimension : dimensions) {
            a.put(dimension.getDimensionId(), dimension);
        }
        return a;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, org.siemac.metamac.rest.export.v1_0.domain.IndicatorSelectionAttribute> indexAttributesById(
            List<org.siemac.metamac.rest.export.v1_0.domain.IndicatorSelectionAttribute> attributes) {
        if (attributes == null) {
            return MapUtils.EMPTY_MAP;
        }
        Map<String, org.siemac.metamac.rest.export.v1_0.domain.IndicatorSelectionAttribute> a = new HashMap<String, org.siemac.metamac.rest.export.v1_0.domain.IndicatorSelectionAttribute>();
        for (org.siemac.metamac.rest.export.v1_0.domain.IndicatorSelectionAttribute attribute : attributes) {
            a.put(attribute.getAttributeId(), attribute);
        }
        return a;
    }

    private static List<IndicatorSelectionAttribute> attributesToIndicatorSelectionAttributes(Attributes attributes,
            Map<String, org.siemac.metamac.rest.export.v1_0.domain.IndicatorSelectionAttribute> selectionAttributesMap) {
        if (attributes == null) {
            return null;
        }
        List<IndicatorSelectionAttribute> datasetSelectionAttributes = new ArrayList<IndicatorSelectionAttribute>();
        for (Attribute attribute : attributes.getAttributes()) {
            final org.siemac.metamac.rest.export.v1_0.domain.IndicatorSelectionAttribute selectionAttribute = selectionAttributesMap != null ? selectionAttributesMap.get(attribute.getId()) : null;
            datasetSelectionAttributes.add(attributeToIndicatorSelectionAttribute(attribute, selectionAttribute));
        }
        return datasetSelectionAttributes;
    }

    private static IndicatorSelectionAttribute attributeToIndicatorSelectionAttribute(Attribute attribute, org.siemac.metamac.rest.export.v1_0.domain.IndicatorSelectionAttribute selectionAttribute) {
        IndicatorSelectionAttribute datasetSelectionAttribute = new IndicatorSelectionAttribute(attribute.getId());

        // Default values
        LabelVisualisationModeEnum labelVisualizationMode = CODE_AND_LABEL;

        // If we have data sent via api, use that instead
        if (selectionAttribute != null) {
            if (selectionAttribute.getLabelVisualisationMode() != null) {
                labelVisualizationMode = toLabelVisualisationMode(selectionAttribute.getLabelVisualisationMode());
            }
        }

        datasetSelectionAttribute.setLabelVisualisationMode(labelVisualizationMode);
        return datasetSelectionAttribute;
    }

    private static List<IndicatorSelectionDimension> dimensionsToIndicatorSelectionDimensions(DimensionRepresentations dimensionRepresentations, Map<String, IndicatorSelection> selectionDimensionsMap,
            DataStructureDefinition dataStructureDefinition) {
        List<IndicatorSelectionDimension> datasetSelectionDimensions = new ArrayList<IndicatorSelectionDimension>();
        for (DimensionRepresentation dimension : dimensionRepresentations.getDimensions()) {
            final IndicatorSelection selectionDimension = selectionDimensionsMap != null ? selectionDimensionsMap.get(dimension.getDimensionId()) : null;
            datasetSelectionDimensions.add(dimensionToIndicatorSelectionDimension(dimension, dataStructureDefinition, selectionDimension));
        }
        return datasetSelectionDimensions;
    }

    private static IndicatorSelectionDimension dimensionToIndicatorSelectionDimension(DimensionRepresentation dimension, DataStructureDefinition dataStructureDefinition,
            IndicatorSelection selectionDimension) {
        IndicatorSelectionDimension datasetSelectionDimension = new IndicatorSelectionDimension(dimension.getDimensionId());

        // Default values
        LabelVisualisationModeEnum labelVisualizationMode = CODE_AND_LABEL;
        Integer position = dataStructureDefinitionToPosition(dimension.getDimensionId(), dataStructureDefinition);

        // If we have data sent via api, use that instead
        if (selectionDimension != null) {
            if (selectionDimension.getLabelVisualisationMode() != null) {
                labelVisualizationMode = toLabelVisualisationMode(selectionDimension.getLabelVisualisationMode());
            }
            if (selectionDimension.getPosition() != null) {
                position = selectionDimension.getPosition();
            }
        }

        datasetSelectionDimension.setLabelVisualisationMode(labelVisualizationMode);
        datasetSelectionDimension.setPosition(position);
        datasetSelectionDimension.setSelectedDimensionValues(codeRepresentationsToSelectedDimensionValues(dimension.getRepresentations()));
        return datasetSelectionDimension;
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

    private static List<String> codeRepresentationsToSelectedDimensionValues(CodeRepresentations codeRepresentations) {
        List<String> selectedDimensionValues = new ArrayList<String>();
        for (CodeRepresentation dimensionValue : codeRepresentations.getRepresentations()) {
            selectedDimensionValues.add(dimensionValue.getCode());
        }
        return selectedDimensionValues;
    }

}