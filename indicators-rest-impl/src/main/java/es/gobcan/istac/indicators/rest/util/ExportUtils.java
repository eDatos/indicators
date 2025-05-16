package es.gobcan.istac.indicators.rest.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.statistical_resources.rest.common.StatisticalResourcesRestConstants;

import es.gobcan.istac.indicators.rest.domain.IndicatorSelection;
import es.gobcan.istac.indicators.rest.enume.LabelVisualisationModeEnum;
import es.gobcan.istac.indicators.rest.types.MetadataAttributeType;
import es.gobcan.istac.indicators.rest.types.MetadataDimensionType;
import es.gobcan.istac.indicators.rest.types.MetadataRepresentationType;

public class ExportUtils {

    public static final String DEFAULT = "__default__";

    private ExportUtils() {
        // without impl
    }

    /**
     * Returns label in locale 'lang'. Null if it does not exist
     */
    public static String getLabel(InternationalString internationalString, String lang) {
        if (internationalString == null) {
            return null;
        }
        for (LocalisedString localisedString : internationalString.getTexts()) {
            if (localisedString.getLang().equals(lang)) {
                return localisedString.getValue();
            }
        }
        return null;
    }

    /**
     * Builds map indexed by dimensionValueId and value as title of the dimension value
     */
    public static Map<String, String> buildMapDimensionsValuesLabels(Map.Entry<String, MetadataDimensionType> dimension, String lang) throws MetamacException {
        Map<String, String> dimensionValuesLabels = null;
        String dimensionId = dimension.getKey();
        // TODO: revisar

        dimensionValuesLabels = new HashMap<String, String>(dimension.getValue().getRepresentation().size());
        for (MetadataRepresentationType dimensionValue : dimension.getValue().getRepresentation()) {
            String dimensionValueId = dimensionValue.getCode();
            String dimensionValueLabel = getLabel(localisedStringsToInternationalString(dimensionValue.getTitle()), lang);
            dimensionValuesLabels.put(dimensionValueId, dimensionValueLabel);
        }
        return dimensionValuesLabels;
    }

    /**
     * Builds a map indexed by dimensionId with a value as name of the dimension
     */
    public static Map<String, String> buildMapAttributesLabels(List<MetadataAttributeType> attributes, String lang) throws MetamacException {
        Map<String, String> attributesValuesLabels = new HashMap<String, String>(attributes.size());
        for (MetadataAttributeType attribute : attributes) {
            String attributeId = attribute.getCode();
            String attributeLabel = getLabel(localisedStringsToInternationalString(attribute.getTitle()), lang);
            attributesValuesLabels.put(attributeId, attributeLabel);
        }
        return attributesValuesLabels;
    }

    /**
     * Builds a map indexed by attributeId with a map indexed by attributeValueId and value as title of the attribute value
     */
    public static Map<String, Map<String, String>> buildMapAttributesValuesLabels(List<MetadataAttributeType> attributes, String lang) throws MetamacException {
        Map<String, Map<String, String>> attributesValuesLabels = new HashMap<String, Map<String, String>>(attributes.size());

        for (MetadataAttributeType attribute : attributes) {
            String attributeId = attribute.getCode();
            Map<String, String> attributeValuesLabels = attribute.getTitle();

            // EnumeratedAttributeValues attributeValues = (EnumeratedAttributeValues) attribute.getTitle();
            // attributeValuesLabels = new HashMap<String, String>(attributeValues.getValues().size());
            // for (EnumeratedAttributeValue attributeValue : attributeValues.getValues()) {
            // String attributeValueId = attributeValue.getId();
            // String attributeValueLabel = getLabel(attributeValue.getName(), lang);
            // attributeValuesLabels.put(attributeValueId, attributeValueLabel);
            // }

            attributesValuesLabels.put(attributeId, attributeValuesLabels);
        }
        return attributesValuesLabels;

    }

    public static String[] dataToDataArray(String data) {
        return StringUtils.splitByWholeSeparatorPreserveAllTokens(data, StatisticalResourcesRestConstants.DATA_SEPARATOR);
    }

    /**
     * Builds a map indexed by attributeId with the effective label visualisation mode.
     * If attributes has not attributes values in metadata, returns 'only code'
     * If configuration does not exist for component, returns default configuration
     */
    public static Map<String, LabelVisualisationModeEnum> buildMapAttributesLabelVisualisationMode(IndicatorSelection indicatorSelection, List<MetadataAttributeType> attributes) {
        Map<String, LabelVisualisationModeEnum> labelVisualisationsMode = new HashMap<String, LabelVisualisationModeEnum>(attributes.size());
        for (MetadataAttributeType attribute : attributes) {
            String attributeId = attribute.getCode();
            LabelVisualisationModeEnum labelVisualisationMode = null;
            labelVisualisationMode = indicatorSelection != null ? indicatorSelection.getAttributeLabelVisualisationModel(attributeId) : null;
            if (labelVisualisationMode == null) {
                // default value
                labelVisualisationMode = LabelVisualisationModeEnum.LABEL;
            }
            labelVisualisationsMode.put(attributeId, labelVisualisationMode);
        }
        return labelVisualisationsMode;
    }

    public static String escapeNulls(String value) {
        return StringUtils.isEmpty(value) ? StringUtils.EMPTY : value;
    }
    /**
     * Calculate the value as name of the dimension
     */
    public static String buildMapDimensionLabel(Map.Entry<String, MetadataDimensionType> dimension, String lang) throws MetamacException {
        String dimensionLabel = dimension.getKey();
        return dimensionLabel;
    }

    /*
     * Calculate the effective label visualisation mode for a dimension.
     * If configuration does not exist for component, returns default configuration
     * @param dimension
     */
    public static LabelVisualisationModeEnum buildMapDimensionToMapDimensionsLabelVisualisationMode(Map.Entry<String, MetadataDimensionType> dimension) {
        String dimensionId = dimension.getKey();
        LabelVisualisationModeEnum labelVisualisationMode = null;
        if (labelVisualisationMode == null) {
            // default value
            labelVisualisationMode = LabelVisualisationModeEnum.CODE_AND_LABEL;
        }
        return labelVisualisationMode;
    }

    /**
     * Builds a map indexed by attributeId with a map indexed by attributeValueId and localised value as title of the attribute value
     */
    // FIXME: NO USAGE
    // public static Map<String, Map<String, InternationalString>> buildMapAttributesValuesLocalisedLabels(List<AttributeType> attributes) throws MetamacException {
    // Map<String, Map<String, InternationalString>> attributesValuesLocalisedLabels = new HashMap<String, Map<String, InternationalString>>(attributes.size());
    // for (AttributeType attribute : attributes) {
    // String attributeId = attribute.getCode();
    // Map<String, InternationalString> attributeValuesLabels = null;
    // // FIXME ......
    // // if (attribute.getAttributeValues() == null) {
    // // attributeValuesLabels = new HashMap<String, InternationalString>();
    // // } else if (attribute.getAttributeValues() instanceof EnumeratedAttributeValues) {
    // // EnumeratedAttributeValues attributeValues = (EnumeratedAttributeValues) attribute.getAttributeValues();
    // // attributeValuesLabels = new HashMap<String, InternationalString>(attributeValues.getValues().size());
    // // for (EnumeratedAttributeValue attributeValue : attributeValues.getValues()) {
    // // String attributeValueId = attributeValue.getId();
    // // attributeValuesLabels.put(attributeValueId, attributeValue.getName());
    // // }
    // // } else if (attribute.getAttributeValues() instanceof NonEnumeratedAttributeValues) {
    // // NonEnumeratedAttributeValues attributeValues = (NonEnumeratedAttributeValues) attribute.getAttributeValues();
    // // attributeValuesLabels = new HashMap<String, InternationalString>(attributeValues.getValues().size());
    // // for (NonEnumeratedAttributeValue attributeValue : attributeValues.getValues()) {
    // // String attributeValueId = attributeValue.getId();
    // // attributeValuesLabels.put(attributeValueId, attributeValue.getName());
    // // }
    // // } else {
    // // throw new MetamacException(ServiceExceptionType.UNKNOWN, "Attribute values unexpected: " + attribute.getAttributeValues().getClass().getCanonicalName());
    // // }
    // attributesValuesLocalisedLabels.put(attributeId, attributeValuesLabels);
    // }
    // return attributesValuesLocalisedLabels;
    // }

    public static InternationalString localisedStringsToInternationalString(Map<String, String> localisedStrings) {
        InternationalString internationalString = new InternationalString();
        for (Map.Entry<String, String> entry : localisedStrings.entrySet()) {
            if (!DEFAULT.equals(entry.getKey())) {
                LocalisedString localisedString = new LocalisedString();
                localisedString.setLang(entry.getKey());
                localisedString.setValue(entry.getValue());
                internationalString.getTexts().add(localisedString);
            }
        }
        return internationalString;
    }

}
