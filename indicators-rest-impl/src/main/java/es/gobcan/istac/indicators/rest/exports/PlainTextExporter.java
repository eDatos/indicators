package es.gobcan.istac.indicators.rest.exports;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.rest.dto.IndicatorSelection;
import es.gobcan.istac.indicators.rest.enume.ExportFormatEnum;
import es.gobcan.istac.indicators.rest.enume.LabelVisualisationModeEnum;
import es.gobcan.istac.indicators.rest.types.AttributeAttachmentLevelEnumType;
import es.gobcan.istac.indicators.rest.types.MetadataAttributeType;

public class PlainTextExporter {

    private final ResourceAccess     resourceAccess;
    private final IndicatorSelection indicatorSelection;

    private static final String      ESCAPE_DOUBLE_QUOTES                = "\"";
    private static final String      HEADER_OBSERVATION                  = "OBS_VALUE";
    private static final String      HEADER_SUFIX_CODE_WHEN_EXPORT_TITLE = "_CODE";
    private static final boolean     ESCAPE_IF_NECESSARY                 = true;

    private ExportFormatEnum         exportFormatEnum                    = null;

    public PlainTextExporter(ExportFormatEnum exportFormatEnum, ResourceAccess resourceAccess) throws MetamacException {
        this.resourceAccess = resourceAccess;
        indicatorSelection = resourceAccess.getDataSelection();
        this.exportFormatEnum = exportFormatEnum;
        if (this.exportFormatEnum == null) {
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
            throw new MetamacException(e, ServiceExceptionType.UNKNOWN, "Error exporting to " + exportFormatEnum.getName());
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
                header.append(escapeString(dimensionId, ESCAPE_IF_NECESSARY) + exportFormatEnum.getSeparator());
            }
            if (labelVisualisation.isCode() && labelVisualisation.isLabel()) {
                header.append(escapeString(dimensionId + HEADER_SUFIX_CODE_WHEN_EXPORT_TITLE, ESCAPE_IF_NECESSARY) + exportFormatEnum.getSeparator());
            }
        }
        header.append(HEADER_OBSERVATION);
        for (MetadataAttributeType attribute : resourceAccess.getAttributesMetadata()) {
            if (!AttributeAttachmentLevelEnumType.OBSERVATION.equals(attribute.getAttachmentLevel())) {
                continue; // only observation attachment level
            }
            String attributeId = attribute.getCode();
            LabelVisualisationModeEnum labelVisualisation = resourceAccess.getAttributeLabelVisualisationMode(attributeId);
            if (labelVisualisation.isLabel() || labelVisualisation.isCode()) {
                header.append(exportFormatEnum.getSeparator() + escapeString(attributeId, ESCAPE_IF_NECESSARY));
            }
            if (labelVisualisation.isCode() && labelVisualisation.isLabel()) {
                header.append(exportFormatEnum.getSeparator() + escapeString(attributeId + HEADER_SUFIX_CODE_WHEN_EXPORT_TITLE, ESCAPE_IF_NECESSARY));
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
                        line.append(escapeString(dimensionValueLabel, ESCAPE_IF_NECESSARY) + exportFormatEnum.getSeparator());
                    }
                    if (labelVisualisation.isCode()) {
                        line.append(escapeString(dimensionValueId, ESCAPE_IF_NECESSARY) + exportFormatEnum.getSeparator());
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
                    if (!AttributeAttachmentLevelEnumType.OBSERVATION.equals(attribute.getAttachmentLevel())) {
                        continue; // only observation attachment level
                    }

                    String attributeId = attribute.getCode();
                    String attributeValue = resourceAccess.measureAttributeValueAtPermutation(attributeId, permutationAtCell);
                    if (attributeValue == null) {
                        attributeValue = StringUtils.EMPTY;
                        line.append(exportFormatEnum.getSeparator() + escapeString(attributeValue, ESCAPE_IF_NECESSARY));
                    } else {
                        LabelVisualisationModeEnum labelVisualisation = resourceAccess.getAttributeLabelVisualisationMode(attributeId);
                        if (labelVisualisation.isLabel()) {
                            String attributeValueLabel = resourceAccess.getAttributeValueLabelCurrentLocale(attributeId, attributeValue);
                            line.append(attributeValueLabel != null
                                    ? exportFormatEnum.getSeparator() + escapeString(attributeValueLabel, ESCAPE_IF_NECESSARY)
                                    : exportFormatEnum.getSeparator() + escapeString(attributeValue, ESCAPE_IF_NECESSARY));
                        }
                        if (labelVisualisation.isCode()) {
                            line.append(exportFormatEnum.getSeparator() + escapeString(attributeValue, ESCAPE_IF_NECESSARY));
                        }
                    }
                }
                printWriter.println(line);
            }
        }
    }





    private String escapeString(String source, boolean escapeOnlyIfNecessary) {
        if (StringUtils.isEmpty(source)) {
            return source;
        }

        if (escapeOnlyIfNecessary) {
            if (!source.contains(exportFormatEnum.getSeparator())) {
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