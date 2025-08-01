package es.gobcan.istac.indicators.rest.exports;

import java.io.OutputStream;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.rest.dto.IndicatorSelection;
import es.gobcan.istac.indicators.rest.enume.LabelVisualisationModeEnum;
import es.gobcan.istac.indicators.rest.types.AttributeAttachmentLevelEnumType;
import es.gobcan.istac.indicators.rest.types.MetadataAttributeType;
import es.gobcan.istac.indicators.rest.util.ExportUtils;

public class ExcelExporter {

    private final ResourceAccess     resourceAccess;
    private final IndicatorSelection indicatorSelection;

    private static final String      HEADER_OBSERVATION                  = "OBS_VALUE";
    private static final String      HEADER_SUFIX_CODE_WHEN_EXPORT_TITLE = "_CODE";
    private static final String      EXCEL_SHEET_NAME                    = "Sheet1";

    public ExcelExporter(ResourceAccess resourceAccess) {
        this.resourceAccess = resourceAccess;
        this.indicatorSelection = resourceAccess.getDataSelection();
    }

    public void write(OutputStream os) throws MetamacException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(EXCEL_SHEET_NAME);

            int rowIndex = 0;
            createHeaderRow(sheet.createRow(rowIndex++));

            for (int i = 0; i < indicatorSelection.getRows(); i++) {
                for (int j = 0; j < indicatorSelection.getColumns(); j++) {
                    Map<String, String> permutation = indicatorSelection.permutationAtCell(i, j);
                    addObservationRow(sheet.createRow(rowIndex++), permutation);
                }
            }

            workbook.write(os);
        } catch (Exception e) {
            throw new MetamacException(e, ServiceExceptionType.UNKNOWN, "Error writing excel workbook to outputStream");
        }
    }

    private void createHeaderRow(Row headerRow) {
        int colIndex = 0;

        for (String dimensionId : resourceAccess.getDimensionsOrderedForData()) {
            LabelVisualisationModeEnum labelVisualisationMode = resourceAccess.getDimensionLabelVisualisationMode(dimensionId);
            if (labelVisualisationMode.isLabel() || labelVisualisationMode.isCode()) {
                headerRow.createCell(colIndex++).setCellValue(dimensionId);
            }
            if (labelVisualisationMode.isLabel() && labelVisualisationMode.isCode()) {
                headerRow.createCell(colIndex++).setCellValue(dimensionId + HEADER_SUFIX_CODE_WHEN_EXPORT_TITLE);
            }
        }

        headerRow.createCell(colIndex++).setCellValue(HEADER_OBSERVATION);

        for (MetadataAttributeType attribute : resourceAccess.getAttributesMetadata()) {
            if (!AttributeAttachmentLevelEnumType.OBSERVATION.equals(attribute.getAttachmentLevel())) {
                continue;
            }
            String attributeId = attribute.getCode();
            LabelVisualisationModeEnum labelVisualisationMode = resourceAccess.getAttributeLabelVisualisationMode(attributeId);
            if (labelVisualisationMode.isLabel() || labelVisualisationMode.isCode()) {
                headerRow.createCell(colIndex++).setCellValue(attributeId);
            }
            if (labelVisualisationMode.isLabel() && labelVisualisationMode.isCode()) {
                headerRow.createCell(colIndex++).setCellValue(attributeId + HEADER_SUFIX_CODE_WHEN_EXPORT_TITLE);
            }
        }
    }

    private void addObservationRow(Row row, Map<String, String> permutation) {
        int colIndex = 0;

        for (String dimensionId : resourceAccess.getDimensionsOrderedForData()) {
            String valueId = permutation.get(dimensionId);
            LabelVisualisationModeEnum labelVisualisationMode = resourceAccess.getDimensionLabelVisualisationMode(dimensionId);

            if (labelVisualisationMode.isLabel()) {
                String label = resourceAccess.getDimensionValueLabelCurrentLocale(dimensionId, valueId);
                row.createCell(colIndex++).setCellValue(ExportUtils.escapeNulls(label));
            }
            if (labelVisualisationMode.isCode()) {
                row.createCell(colIndex++).setCellValue(ExportUtils.escapeNulls(valueId));
            }
        }

        String obsValue = resourceAccess.observationAtPermutation(permutation);
        row.createCell(colIndex++).setCellValue(ExportUtils.escapeNulls(obsValue));

        for (MetadataAttributeType attribute : resourceAccess.getAttributesMetadata()) {
            if (!AttributeAttachmentLevelEnumType.OBSERVATION.equals(attribute.getAttachmentLevel()))
                continue;

            String attributeId = attribute.getCode();
            String attributeValue = resourceAccess.measureAttributeValueAtPermutation(attributeId, permutation);
            LabelVisualisationModeEnum labelVisualisationMode = resourceAccess.getAttributeLabelVisualisationMode(attributeId);

            if (attributeValue == null) {
                if (labelVisualisationMode.isLabel() || labelVisualisationMode.isCode())
                    row.createCell(colIndex++).setCellValue("");
                if (labelVisualisationMode.isLabel() && labelVisualisationMode.isCode())
                    row.createCell(colIndex++).setCellValue("");
                continue;
            }

            if (labelVisualisationMode.isLabel()) {
                String label = resourceAccess.getAttributeValueLabelCurrentLocale(attributeId, attributeValue);
                row.createCell(colIndex++).setCellValue(ExportUtils.escapeNulls(label != null ? label : attributeValue));
            }
            if (labelVisualisationMode.isCode()) {
                row.createCell(colIndex++).setCellValue(ExportUtils.escapeNulls(attributeValue));
            }
        }
    }
}
