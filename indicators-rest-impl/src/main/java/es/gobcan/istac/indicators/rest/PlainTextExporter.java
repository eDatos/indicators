package org.siemac.metamac.portal.core.exporters;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.AttributeAttachmentLevelType;

import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceimpl.util.DataOrderingStackElement;
import es.gobcan.istac.indicators.rest.ResourceAccess;
import es.gobcan.istac.indicators.rest.domain.IndicatorSelection;
import es.gobcan.istac.indicators.rest.enume.LabelVisualisationModeEnum;
import es.gobcan.istac.indicators.rest.enume.PlainTextTypeEnum;
import es.gobcan.istac.indicators.rest.types.MetadataAttributeType;

public class PlainTextExporter {

    private final ResourceAccess     resourceAccess;
    private final IndicatorSelection indicatorSelection;

    private static final String      ESCAPE_DOUBLE_QUOTES                = "\"";
    private static final String      HEADER_OBSERVATION                  = "OBS_VALUE";
    private static final String      HEADER_ATTRIBUTE_ID                 = "ATTRIBUTE";
    private static final String      HEADER_ATTRIBUTE_VALUE              = "ATTRIBUTE_VALUE";
    private static final String      HEADER_ATTRIBUTE_VALUE_CODE         = "ATTRIBUTE_VALUE_CODE";
    private static final String      HEADER_SUFIX_CODE_WHEN_EXPORT_TITLE = "_CODE";
    private static final boolean     ESCAPE_IF_NECESSARY                 = true;

    private PlainTextTypeEnum        plainTextTypeEnum                   = null;

    public PlainTextExporter(PlainTextTypeEnum plainTextTypeEnum, ResourceAccess resourceAccess) throws MetamacException {
        this.resourceAccess = resourceAccess;
        indicatorSelection = resourceAccess.getDataSelection();
        this.plainTextTypeEnum = plainTextTypeEnum;
        if (this.plainTextTypeEnum == null) {
            throw new MetamacException(ServiceExceptionType.UNKNOWN, "Plain Text format is required ");
        }
    }

    // public PlainTextExporter(PlainTextTypeEnum plainTextTypeEnum, DatasetBase dataset, IndicatorSelection indicatorSelection, String lang, String langAlternative) throws MetamacException {
    // resourceAccess = new ResourceAccess(dataset, indicatorSelection, lang, langAlternative);
    // this.indicatorSelection = indicatorSelection;
    // this.plainTextTypeEnum = plainTextTypeEnum;
    // if (this.plainTextTypeEnum == null) {
    // throw new MetamacException(ServiceExceptionType.UNKNOWN, "Plain Text format is required ");
    // }
    // }
    //
    // public PlainTextExporter(PlainTextTypeEnum plainTextTypeEnum, QueryBase query, IndicatorSelection indicatorSelection, String lang, String langAlternative) throws MetamacException {
    // resourceAccess = new ResourceAccess(query, null, indicatorSelection, lang, langAlternative);
    // this.indicatorSelection = indicatorSelection;
    // this.plainTextTypeEnum = plainTextTypeEnum;
    // if (this.plainTextTypeEnum == null) {
    // throw new MetamacException(ServiceExceptionType.UNKNOWN, "Plain Text format is required ");
    // }
    // }

    public void writeObservationsAndAttributesWithObservationAttachmentLevel(OutputStream os) throws MetamacException {
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(new OutputStreamWriter(os, Charset.forName("UTF-8")));
            writeHeaderForPlainTextObservations(printWriter);
            writeBodyForPlainTextObservations(printWriter);
        } catch (Exception e) {
            throw new MetamacException(e, ServiceExceptionType.UNKNOWN, "Error exporting to " + plainTextTypeEnum.getName());
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
            throw new MetamacException(e, ServiceExceptionType.UNKNOWN, "Error exporting to " + plainTextTypeEnum.getName());
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
                header.append(escapeString(dimensionId, ESCAPE_IF_NECESSARY) + plainTextTypeEnum.getSeparator());
            }
            if (labelVisualisation.isCode() && labelVisualisation.isLabel()) {
                header.append(escapeString(dimensionId + HEADER_SUFIX_CODE_WHEN_EXPORT_TITLE, ESCAPE_IF_NECESSARY) + plainTextTypeEnum.getSeparator());
            }
        }
        header.append(HEADER_OBSERVATION);
        for (MetadataAttributeType attribute : resourceAccess.getAttributesMetadata()) {
            // FIXME ¿Necesario el attachmentLevel?
            // if (!AttributeAttachmentLevelType.PRIMARY_MEASURE.equals(attribute.getAttachmentLevel())) {
            // continue; // only observation attachment level
            // }
            String attributeId = attribute.getCode();
            LabelVisualisationModeEnum labelVisualisation = resourceAccess.getAttributeLabelVisualisationMode(attributeId);
            if (labelVisualisation.isLabel() || labelVisualisation.isCode()) {
                header.append(plainTextTypeEnum.getSeparator() + escapeString(attributeId, ESCAPE_IF_NECESSARY));
            }
            if (labelVisualisation.isCode() && labelVisualisation.isLabel()) {
                header.append(plainTextTypeEnum.getSeparator() + escapeString(attributeId + HEADER_SUFIX_CODE_WHEN_EXPORT_TITLE, ESCAPE_IF_NECESSARY));
            }
        }
        printWriter.println(header);
    }

    private void writeBodyForPlainTextObservations(PrintWriter printWriter) {
        for (int i = 0; i < indicatorSelection.getRows(); i++) {
            for (int j = 0; j < indicatorSelection.getColumns(); j++) {
                Map<String, String> permutationAtCell = indicatorSelection.permutationAtCell(i, j);

                // The observation is complete
                StringBuilder line = new StringBuilder();

                // Dimension values
                for (String dimensionId : resourceAccess.getDimensionsOrderedForData()) {
                    String dimensionValueId = permutationAtCell.get(dimensionId);
                    LabelVisualisationModeEnum labelVisualisation = resourceAccess.getDimensionLabelVisualisationMode(dimensionId);
                    if (labelVisualisation.isLabel()) {
                        String dimensionValueLabel = resourceAccess.getDimensionValueLabelCurrentLocale(dimensionId, dimensionValueId);
                        line.append(escapeString(dimensionValueLabel, ESCAPE_IF_NECESSARY) + plainTextTypeEnum.getSeparator());
                    }
                    if (labelVisualisation.isCode()) {
                        line.append(escapeString(dimensionValueId, ESCAPE_IF_NECESSARY) + plainTextTypeEnum.getSeparator());
                    }
                }

                // Observation
                String observation = resourceAccess.observationAtPermutation(permutationAtCell);
                if (observation == null) {
                    observation = StringUtils.EMPTY;
                }
                line.append(escapeString(observation, ESCAPE_IF_NECESSARY));

                // Attributes
                for (MetadataAttributeType attribute : resourceAccess.getAttributesMetadata()) {
                    // if (!AttributeAttachmentLevelType.PRIMARY_MEASURE.equals(attribute.getAttachmentLevel())) {
                    // continue; // only observation attachment level
                    // }

                    String attributeId = attribute.getCode();
                    String attributeValue = resourceAccess.measureAttributeValueAtPermutation(attributeId, permutationAtCell);
                    if (attributeValue == null) {
                        attributeValue = StringUtils.EMPTY;
                        line.append(plainTextTypeEnum.getSeparator() + escapeString(attributeValue, ESCAPE_IF_NECESSARY));
                    } else {
                        LabelVisualisationModeEnum labelVisualisation = resourceAccess.getAttributeLabelVisualisationMode(attributeId);
                        if (labelVisualisation.isLabel()) {
                            String attributeValueLabel = resourceAccess.getAttributeValueLabelCurrentLocale(attributeId, attributeValue);
                            line.append(attributeValueLabel != null
                                    ? plainTextTypeEnum.getSeparator() + escapeString(attributeValueLabel, ESCAPE_IF_NECESSARY)
                                    : plainTextTypeEnum.getSeparator() + escapeString(attributeValue, ESCAPE_IF_NECESSARY));
                        }
                        if (labelVisualisation.isCode()) {
                            line.append(plainTextTypeEnum.getSeparator() + escapeString(attributeValue, ESCAPE_IF_NECESSARY));
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
                header.append(escapeString(dimensionId, ESCAPE_IF_NECESSARY) + plainTextTypeEnum.getSeparator());
            }
            if (labelVisualisation.isCode() && labelVisualisation.isLabel()) {
                header.append(escapeString(dimensionId + HEADER_SUFIX_CODE_WHEN_EXPORT_TITLE, ESCAPE_IF_NECESSARY) + plainTextTypeEnum.getSeparator());
            }
        }
        header.append(HEADER_ATTRIBUTE_ID);
        header.append(plainTextTypeEnum.getSeparator() + escapeString(HEADER_ATTRIBUTE_VALUE, ESCAPE_IF_NECESSARY));
        if (numberOfColumnsToAttributeValue == 2) {
            header.append(plainTextTypeEnum.getSeparator() + escapeString(HEADER_ATTRIBUTE_VALUE_CODE, ESCAPE_IF_NECESSARY)); // put this column only when it is necessary
        }
        printWriter.println(header);
    }

    private void writeBodyForPlainTextAttributesDataset(PrintWriter printWriter, List<MetadataAttributeType> attributes, int numberOfColumnsToAttributeValue) {
        for (MetadataAttributeType attribute : attributes) {
            // FIXME if??
            // if (!AttributeAttachmentLevelType.DATASET.equals(attribute.getAttachmentLevel())) {
            // continue;
            // }
            String attributeId = attribute.getCode();
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
                    line.append(plainTextTypeEnum.getSeparator());
                }
                if (labelVisualisation.isCode()) {
                    line.append(plainTextTypeEnum.getSeparator());
                }
            }
            // Attribute Id
            line.append(escapeString(attributeId, ESCAPE_IF_NECESSARY) + plainTextTypeEnum.getSeparator());
            // Attribute value
            String attributeValue = attributeValues[0];
            writeBodyAttributeValueForPlainTextAttributes(line, attributeId, attributeValue, numberOfColumnsToAttributeValue);
            printWriter.println(line);
        }
    }

    private void writeBodyForPlainTextAttributesDimensions(PrintWriter printWriter, List<MetadataAttributeType> attributes, int numberOfColumnsToAttributeValue) {
        for (MetadataAttributeType attribute : attributes) {
            // FIXME if??
            if (!AttributeAttachmentLevelType.DIMENSION.equals(attribute.getAttachmentLevel())) {
                continue;
            }
            String attributeId = attribute.getCode();
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
        stack.push(new DataOrderingStackElement(null, -1, null, null));
        Map<String, String> dimensionValuesForAttributeValue = new HashMap<String, String>(dimensionsAttributeOrderedForData.size());

        int lastDimensionPosition = dimensionsAttributeOrderedForData.size() - 1;
        int attributeValueIndex = 0;
        while (stack.size() > 0) {
            DataOrderingStackElement elem = stack.pop();
            int dimensionPosition = elem.getDimensionPosition();
            // TODO: revisar
            // String dimensionCodeId = elem.getDimensionCodeId();
            String dimensionCodeId = "";

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
                            line.append(plainTextTypeEnum.getSeparator());
                        }
                        if (labelVisualisation.isCode()) {
                            if (dimensionValuesForAttributeValue.containsKey(dimensionId)) {
                                line.append(escapeString(dimensionValueId, ESCAPE_IF_NECESSARY));
                            }
                            line.append(plainTextTypeEnum.getSeparator());
                        }
                    }
                    // Attribute Id
                    line.append(escapeString(attributeId, ESCAPE_IF_NECESSARY) + plainTextTypeEnum.getSeparator());
                    // Attribute value
                    writeBodyAttributeValueForPlainTextAttributes(line, attributeId, attributeValue, numberOfColumnsToAttributeValue);
                    printWriter.println(line);
                }
            } else {
                String dimensionId = dimensionsAttributeOrderedForData.get(dimensionPosition + 1);
                List<String> dimensionValues = resourceAccess.getDimensionValuesOrderedForData(dimensionId);
                for (int i = dimensionValues.size() - 1; i >= 0; i--) {
                    DataOrderingStackElement temp = new DataOrderingStackElement(dimensionId, dimensionPosition + 1, dimensionValues.get(i), null);
                    stack.push(temp);
                }
            }
        }
    }

    private boolean allDimensionValuesAreSelected(List<String> dimensionsOrderedForData, Map<String, String> dimensionValuesForAttributeValue) {
        for (String dimensionId : dimensionsOrderedForData) {
            if (dimensionValuesForAttributeValue.containsKey(dimensionId)) {
                String dimensionValueId = dimensionValuesForAttributeValue.get(dimensionId);
                if (!indicatorSelection.getDimension(dimensionId).getSelectedDimensionValues().contains(dimensionValueId)) {
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
                line.append(plainTextTypeEnum.getSeparator());
            }
        } else if (numberOfColumnsToAttributeValue == 2) {
            line.append(plainTextTypeEnum.getSeparator());
        }
        if (labelVisualisation.isCode()) {
            line.append(attributeValueCode);
        }
    }

    private int guessNumberOfColumnsToAttributeValue() {
        for (MetadataAttributeType attribute : resourceAccess.getAttributesMetadata()) {

            if (!AttributeAttachmentLevelType.DATASET.equals(attribute.getAttachmentLevel()) && AttributeAttachmentLevelType.DIMENSION.equals(attribute.getAttachmentLevel())) {
                continue;
            }
            String attributeId = attribute.getCode();
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
            if (!source.contains(plainTextTypeEnum.getSeparator())) {
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