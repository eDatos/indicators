package es.gobcan.istac.indicators.rest;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Attribute;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.AttributeAttachmentLevelType;

import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceimpl.util.DataOrderingStackElement;
import es.gobcan.istac.indicators.rest.enume.LabelVisualisationModeEnum;

public class PlainTextExporter {

    private final ResourceAccess             resourceAccess;
    private static final String              ESCAPE_DOUBLE_QUOTES                = "\"";
    private static final String              HEADER_OBSERVATION                  = "OBS_VALUE";
    private static final String              HEADER_ATTRIBUTE_ID                 = "ATTRIBUTE";
    private static final String              HEADER_ATTRIBUTE_VALUE              = "ATTRIBUTE_VALUE";
    private static final String              HEADER_ATTRIBUTE_VALUE_CODE         = "ATTRIBUTE_VALUE_CODE";
    private static final String              HEADER_SUFIX_CODE_WHEN_EXPORT_TITLE = "_CODE";
    private static final boolean             ESCAPE_IF_NECESSARY                 = true;

    private String                           format                              = null;
    private String                           separator;
    private static final Map<String, String> separatorsByFormat                  = initMapSeparators();

    private static Map<String, String> initMapSeparators() {
        Map<String, String> map = new HashMap<>();
        map.put("csv", ",");
        map.put("tsv", "\t");
        return Collections.unmodifiableMap(map);
    }

    public PlainTextExporter(String format, ResourceAccess resourceAccess) throws MetamacException {
        resourceAccess = resourceAccess;
        this.format = format;
        this.separator = separatorsByFormat.get(format);
        if (this.format == null) {
            throw new MetamacException(ServiceExceptionType.UNKNOWN, "Plain Text format is required ");
        }
    }

    public void writeObservationsAndAttributesWithObservationAttachmentLevel(OutputStream os) throws MetamacException {
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(new OutputStreamWriter(os, Charset.forName("UTF-8")));
            writeHeaderForPlainTextObservations(printWriter);
            writeBodyForPlainTextObservations(printWriter);
        } catch (Exception e) {
            throw new MetamacException(e, ServiceExceptionType.UNKNOWN, "Error exporting to " + format);
        } finally {
            if (printWriter != null) {
                printWriter.flush();
            }
        }
    }

    public void writeAttributesWithDatasetAndDimensionAttachmentLevel(OutputStream os) throws MetamacException {
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(new OutputStreamWriter(os, Charset.forName("UTF-8")));

            int numberOfColumnsToAttributeValue = guessNumberOfColumnsToAttributeValue();
            writeHeaderForPlainTextAttributes(printWriter, numberOfColumnsToAttributeValue);
            writeBodyForPlainTextAttributesDataset(printWriter, resourceAccess.getAttributesMetadata(), numberOfColumnsToAttributeValue);
            writeBodyForPlainTextAttributesDimensions(printWriter, resourceAccess.getAttributesMetadata(), numberOfColumnsToAttributeValue);
            // NOTE: Attributes observations are exported another plain text
        } catch (Exception e) {
            throw new MetamacException(e, ServiceExceptionType.UNKNOWN, "Error exporting to " + format);
        } finally {
            if (printWriter != null) {
                printWriter.flush();
            }
        }
    }

    private void writeHeaderForPlainTextObservations(PrintWriter printWriter) {
        StringBuilder header = new StringBuilder();
        for (String dimensionId : resourceAccess.getDimensionsOrderedForData()) {
            LabelVisualisationModeEnum labelVisualisation = resourceAccess.getDimensionLabelVisualisationMode(dimensionId);
            if (labelVisualisation.isLabel() || labelVisualisation.isCode()) {
                header.append(escapeString(dimensionId, ESCAPE_IF_NECESSARY) + separator);
            }
            if (labelVisualisation.isCode() && labelVisualisation.isLabel()) {
                header.append(escapeString(dimensionId + HEADER_SUFIX_CODE_WHEN_EXPORT_TITLE, ESCAPE_IF_NECESSARY) + separator);
            }
        }
        header.append(HEADER_OBSERVATION);
        for (Attribute attribute : resourceAccess.getAttributesMetadata()) {
            if (!AttributeAttachmentLevelType.PRIMARY_MEASURE.equals(attribute.getAttachmentLevel())) {
                continue; // only observation attachment level
            }
            String attributeId = attribute.getId();
            LabelVisualisationModeEnum labelVisualisation = resourceAccess.getAttributeLabelVisualisationMode(attributeId);
            if (labelVisualisation.isLabel() || labelVisualisation.isCode()) {
                header.append(separator + escapeString(attributeId, ESCAPE_IF_NECESSARY));
            }
            if (labelVisualisation.isCode() && labelVisualisation.isLabel()) {
                header.append(separator + escapeString(attributeId + HEADER_SUFIX_CODE_WHEN_EXPORT_TITLE, ESCAPE_IF_NECESSARY));
            }
        }
        printWriter.println(header);
    }

    private void writeBodyForPlainTextObservations(PrintWriter printWriter) {
        for (int i = 0; i < datasetSelection.getRows(); i++) {
            for (int j = 0; j < datasetSelection.getColumns(); j++) {
                Map<String, String> permutationAtCell = datasetSelection.permutationAtCell(i, j);

                // The observation is complete
                StringBuilder line = new StringBuilder();

                // Dimension values
                for (String dimensionId : resourceAccess.getDimensionsOrderedForData()) {
                    String dimensionValueId = permutationAtCell.get(dimensionId);
                    LabelVisualisationModeEnum labelVisualisation = resourceAccess.getDimensionLabelVisualisationMode(dimensionId);
                    if (labelVisualisation.isLabel()) {
                        String dimensionValueLabel = resourceAccess.getDimensionValueLabelCurrentLocale(dimensionId, dimensionValueId);
                        line.append(escapeString(dimensionValueLabel, ESCAPE_IF_NECESSARY) + separator);
                    }
                    if (labelVisualisation.isCode()) {
                        line.append(escapeString(dimensionValueId, ESCAPE_IF_NECESSARY) + separator);
                    }
                }

                // Observation
                String observation = resourceAccess.observationAtPermutation(permutationAtCell);
                if (observation == null) {
                    observation = StringUtils.EMPTY;
                }
                line.append(escapeString(observation, ESCAPE_IF_NECESSARY));

                // Attributes
                for (Attribute attribute : resourceAccess.getAttributesMetadata()) {
                    if (!AttributeAttachmentLevelType.PRIMARY_MEASURE.equals(attribute.getAttachmentLevel())) {
                        continue; // only observation attachment level
                    }

                    String attributeId = attribute.getId();
                    String attributeValue = resourceAccess.measureAttributeValueAtPermutation(attributeId, permutationAtCell);
                    if (attributeValue == null) {
                        attributeValue = StringUtils.EMPTY;
                        line.append(separator + escapeString(attributeValue, ESCAPE_IF_NECESSARY));
                    } else {
                        LabelVisualisationModeEnum labelVisualisation = resourceAccess.getAttributeLabelVisualisationMode(attributeId);
                        if (labelVisualisation.isLabel()) {
                            String attributeValueLabel = resourceAccess.getAttributeValueLabelCurrentLocale(attributeId, attributeValue);
                            line.append(
                                    attributeValueLabel != null ? separator + escapeString(attributeValueLabel, ESCAPE_IF_NECESSARY) : separator + escapeString(attributeValue, ESCAPE_IF_NECESSARY));
                        }
                        if (labelVisualisation.isCode()) {
                            line.append(separator + escapeString(attributeValue, ESCAPE_IF_NECESSARY));
                        }
                    }
                }
                printWriter.println(line);
            }
        }
    }

    private void writeHeaderForPlainTextAttributes(PrintWriter printWriter, int numberOfColumnsToAttributeValue) {
        StringBuilder header = new StringBuilder();
        for (String dimensionId : resourceAccess.getDimensionsOrderedForData()) {
            LabelVisualisationModeEnum labelVisualisation = resourceAccess.getDimensionLabelVisualisationMode(dimensionId);
            if (labelVisualisation.isLabel() || labelVisualisation.isCode()) {
                header.append(escapeString(dimensionId, ESCAPE_IF_NECESSARY) + separator);
            }
            if (labelVisualisation.isCode() && labelVisualisation.isLabel()) {
                header.append(escapeString(dimensionId + HEADER_SUFIX_CODE_WHEN_EXPORT_TITLE, ESCAPE_IF_NECESSARY) + separator);
            }
        }
        header.append(HEADER_ATTRIBUTE_ID);
        header.append(separator + escapeString(HEADER_ATTRIBUTE_VALUE, ESCAPE_IF_NECESSARY));
        if (numberOfColumnsToAttributeValue == 2) {
            header.append(separator + escapeString(HEADER_ATTRIBUTE_VALUE_CODE, ESCAPE_IF_NECESSARY)); // put this column only when it is necessary
        }
        printWriter.println(header);
    }

    private void writeBodyForPlainTextAttributesDataset(PrintWriter printWriter, List<Attribute> attributes, int numberOfColumnsToAttributeValue) {
        for (Attribute attribute : attributes) {
            if (!AttributeAttachmentLevelType.DATASET.equals(attribute.getAttachmentLevel())) {
                continue;
            }
            String attributeId = attribute.getId();
            String[] attributeValues = resourceAccess.getAttributeValues(attributeId);
            if (attributeValues == null) {
                continue;
            }
            StringBuilder line = new StringBuilder();
            // Dimensions
            for (String dimensionId : resourceAccess.getDimensionsOrderedForData()) {
                // Write empty code dimensions
                LabelVisualisationModeEnum labelVisualisation = resourceAccess.getDimensionLabelVisualisationMode(dimensionId);
                if (labelVisualisation.isLabel()) {
                    line.append(separator);
                }
                if (labelVisualisation.isCode()) {
                    line.append(separator);
                }
            }
            // Attribute Id
            line.append(escapeString(attributeId, ESCAPE_IF_NECESSARY) + separator);
            // Attribute value
            String attributeValue = attributeValues[0];
            writeBodyAttributeValueForPlainTextAttributes(line, attributeId, attributeValue, numberOfColumnsToAttributeValue);
            printWriter.println(line);
        }
    }

    private void writeBodyForPlainTextAttributesDimensions(PrintWriter printWriter, List<Attribute> attributes, int numberOfColumnsToAttributeValue) {
        for (Attribute attribute : attributes) {
            if (!AttributeAttachmentLevelType.DIMENSION.equals(attribute.getAttachmentLevel())) {
                continue;
            }
            String attributeId = attribute.getId();
            String[] attributeValues = resourceAccess.getAttributeValues(attributeId);
            if (attributeValues == null) {
                continue;
            }
            List<String> dimensionsAttributeOrderedForData = resourceAccess.getDimensionsAttributeOrderedForData(attribute);
            writeBodyForPlainTextAttributeDimensions(printWriter, attributeId, attributeValues, dimensionsAttributeOrderedForData, numberOfColumnsToAttributeValue);
        }
    }

    private void writeBodyForPlainTextAttributeDimensions(PrintWriter printWriter, String attributeId, String[] attributeValues, List<String> dimensionsAttributeOrderedForData,
            int numberOfColumnsToAttributeValue) {

        Stack<DataOrderingStackElement> stack = new Stack<DataOrderingStackElement>();
        stack.push(new DataOrderingStackElement(null, -1, null));
        Map<String, String> dimensionValuesForAttributeValue = new HashMap<String, String>(dimensionsAttributeOrderedForData.size());

        int lastDimensionPosition = dimensionsAttributeOrderedForData.size() - 1;
        int attributeValueIndex = 0;
        while (stack.size() > 0) {
            DataOrderingStackElement elem = stack.pop();
            int dimensionPosition = elem.getDimensionPosition();
            String dimensionCodeId = elem.getDimensionCodeId();

            if (dimensionPosition != -1) {
                String dimensionId = elem.getDimensionId();
                dimensionValuesForAttributeValue.put(dimensionId, dimensionCodeId);
            }

            if (dimensionPosition == lastDimensionPosition) {
                // We have all dimensions here
                String attributeValue = attributeValues[attributeValueIndex++];
                if (!StringUtils.isEmpty(attributeValue) && allDimensionValuesAreSelected(resourceAccess.getDimensionsOrderedForData(), dimensionValuesForAttributeValue)) {
                    StringBuilder line = new StringBuilder();
                    // Dimensions
                    for (String dimensionId : resourceAccess.getDimensionsOrderedForData()) {
                        String dimensionValueId = dimensionValuesForAttributeValue.get(dimensionId);
                        LabelVisualisationModeEnum labelVisualisation = resourceAccess.getDimensionLabelVisualisationMode(dimensionId);
                        if (labelVisualisation.isLabel()) {
                            if (dimensionValuesForAttributeValue.containsKey(dimensionId)) {
                                String dimensionValueLabel = resourceAccess.getDimensionValueLabelCurrentLocale(dimensionId, dimensionValueId);
                                line.append(escapeString(dimensionValueLabel, ESCAPE_IF_NECESSARY));
                            }
                            line.append(separator);
                        }
                        if (labelVisualisation.isCode()) {
                            if (dimensionValuesForAttributeValue.containsKey(dimensionId)) {
                                line.append(escapeString(dimensionValueId, ESCAPE_IF_NECESSARY));
                            }
                            line.append(separator);
                        }
                    }
                    // Attribute Id
                    line.append(escapeString(attributeId, ESCAPE_IF_NECESSARY) + separator);
                    // Attribute value
                    writeBodyAttributeValueForPlainTextAttributes(line, attributeId, attributeValue, numberOfColumnsToAttributeValue);
                    printWriter.println(line);
                }
            } else {
                String dimensionId = dimensionsAttributeOrderedForData.get(dimensionPosition + 1);
                List<String> dimensionValues = resourceAccess.getDimensionValuesOrderedForData(dimensionId);
                for (int i = dimensionValues.size() - 1; i >= 0; i--) {
                    DataOrderingStackElement temp = new DataOrderingStackElement(dimensionId, dimensionPosition + 1, dimensionValues.get(i));
                    stack.push(temp);
                }
            }
        }
    }

    private boolean allDimensionValuesAreSelected(List<String> dimensionsOrderedForData, Map<String, String> dimensionValuesForAttributeValue) {
        for (String dimensionId : dimensionsOrderedForData) {
            if (dimensionValuesForAttributeValue.containsKey(dimensionId)) {
                String dimensionValueId = dimensionValuesForAttributeValue.get(dimensionId);
                if (!datasetSelection.getDimension(dimensionId).getSelectedDimensionValues().contains(dimensionValueId)) {
                    return false;
                }
            }
        }

        return true;
    }

    private void writeBodyAttributeValueForPlainTextAttributes(StringBuilder line, String attributeId, String attributeValueCode, int numberOfColumnsToAttributeValue) {
        LabelVisualisationModeEnum labelVisualisation = resourceAccess.getAttributeLabelVisualisationMode(attributeId);
        attributeValueCode = escapeString(attributeValueCode, ESCAPE_IF_NECESSARY);
        if (labelVisualisation.isLabel()) {
            String attributeValueLabel = resourceAccess.getAttributeValueLabelCurrentLocale(attributeId, attributeValueCode);
            attributeValueLabel = escapeString(attributeValueLabel, ESCAPE_IF_NECESSARY);
            line.append(attributeValueLabel != null ? attributeValueLabel : attributeValueCode);
            if (numberOfColumnsToAttributeValue == 2) {
                line.append(separator);
            }
        } else if (numberOfColumnsToAttributeValue == 2) {
            line.append(separator);
        }
        if (labelVisualisation.isCode()) {
            line.append(attributeValueCode);
        }
    }

    private int guessNumberOfColumnsToAttributeValue() {
        for (Attribute attribute : resourceAccess.getAttributesMetadata()) {
            if (!AttributeAttachmentLevelType.DATASET.equals(attribute.getAttachmentLevel()) && AttributeAttachmentLevelType.DIMENSION.equals(attribute.getAttachmentLevel())) {
                continue;
            }
            String attributeId = attribute.getId();
            LabelVisualisationModeEnum labelVisualisation = resourceAccess.getAttributeLabelVisualisationMode(attributeId);
            if (labelVisualisation.isCode() && labelVisualisation.isLabel()) {
                return 2;
            }
        }
        return 1;
    }

    private String escapeString(String source, boolean escapeOnlyIfNecessary) {
        if (StringUtils.isEmpty(source)) {
            return source;
        }

        if (escapeOnlyIfNecessary) {
            if (!source.contains(separator)) {
                return source;
            }
            if (source.startsWith(ESCAPE_DOUBLE_QUOTES) && source.endsWith(ESCAPE_DOUBLE_QUOTES)) {
                return source; // Already escaped
            }
        }

        // Escape always
        return ESCAPE_DOUBLE_QUOTES + source + ESCAPE_DOUBLE_QUOTES;
    }
}