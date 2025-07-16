package es.gobcan.istac.indicators.rest.exports;

import static es.gobcan.istac.indicators.rest.util.ExportUtils.getLabel;
import static es.gobcan.istac.indicators.rest.util.ExportUtils.localisedStringsToInternationalString;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.rest.dto.IndicatorSelection;
import es.gobcan.istac.indicators.rest.dto.IndicatorSelectionDimension;
import es.gobcan.istac.indicators.rest.enume.LabelVisualisationModeEnum;
import es.gobcan.istac.indicators.rest.types.AttributeAttachmentLevelEnumType;
import es.gobcan.istac.indicators.rest.types.MetadataAttributeType;

public class ExcelExporter {

    // https://andriymz.github.io/misc/apache-poi-slow-excel-generation/
    private static final int         ROW_ACCESS_WINDOW_SIZE         = 100;

    private static final XSSFColor   COLOR_WHITE                    = getXSSFColor("FFFFFF");
    private static final XSSFColor   COLOR_BLACK                    = getXSSFColor("000000");

    private final ResourceAccess     resourceAccess;
    private final IndicatorSelection indicatorSelection;
    private SXSSFSheet               sheet;

    private int                      rowsOfData;
    private int                      columnsOfData;
    private int                      leftHeaderSizeOfData;

    private int                      currentRowCount                = 0;
    private SXSSFWorkbook            workbook;
    private int                      maxRightColumnIndexWithContent = 0;

    private static Logger            log                            = LoggerFactory.getLogger(ExcelExporter.class);

    private XSSFCellStyle            dataCellStyle;
    private XSSFCellStyle            headerCellStyle;




    public ExcelExporter(ResourceAccess resourceAccess) throws MetamacException {
        this.resourceAccess = resourceAccess;
        this.indicatorSelection = resourceAccess.getDataSelection();
    }

    public void write(OutputStream os) throws MetamacException {
        initialize();
        header();
        contentHeader();
        content();

        // Adjust column width
        for (int i = 0; i <= maxRightColumnIndexWithContent; i++) {
            sheet.setColumnWidth(i, 16 * 256); // 15 characters of width
        }

        try {
            workbook.write(os);
        } catch (IOException e) {
            log.error("Error writing excel to OutputStream", e);
            throw new MetamacException(e, ServiceExceptionType.UNKNOWN, "Error writing excel to OutputStream");
        }
        workbook.dispose();
    }

    private void initialize() {
        workbook = new SXSSFWorkbook(ROW_ACCESS_WINDOW_SIZE);
        sheet = workbook.createSheet();
        rowsOfData = indicatorSelection.getRows();
        columnsOfData = indicatorSelection.getColumns();
        leftHeaderSizeOfData = indicatorSelection.getLeftDimensions().size();
        headerCellStyle = createStyleSolid(COLOR_BLACK, COLOR_WHITE, true);
        dataCellStyle = createStyleSolid(COLOR_BLACK, null, null);
        addBorderToCellStyle(dataCellStyle);
    }

    private void header() {
        int headerRow = 0;

        // Title
        String title = resourceAccess.getName().get(resourceAccess.getLang());
        if (!StringUtils.isEmpty(title)) {
            SXSSFRow rowTitle = sheet.createRow(headerRow++);
            addStringCell(rowTitle, 0, title, headerCellStyle);
        }

        headerRow++;
        headerRow++;

        currentRowCount = headerRow;
    }

    private void contentHeader() {
        int headerRow = currentRowCount;

        for (IndicatorSelectionDimension dimension : indicatorSelection.getTopDimensions()) {
            SXSSFRow row = sheet.createRow(headerRow);
            List<String> selectedDimensionValues = dimension.getSelectedDimensionValues();
            int headerColumn = leftHeaderSizeOfData + getObservationAttributesCount();
            int multiplier = indicatorSelection.getMultiplierForDimension(dimension);
            int repeat = columnsOfData / (multiplier * selectedDimensionValues.size());

            for (int i = 0; i < repeat; i++) {
                for (String selectedDimensionValue : selectedDimensionValues) {
                    String dimensionValueLabel = toDimensionValueLabel(dimension.getId(), selectedDimensionValue);

                    SXSSFCell cell = initializeCell(row, headerColumn);
                    cell.setCellValue(dimensionValueLabel);
                    cell.setCellType(CellType.STRING);
                    cell.setCellStyle(headerCellStyle);

                    headerColumn += multiplier;
                }
            }
            headerRow++;
        }

        SXSSFRow attributeHeaderRow = sheet.createRow(headerRow);
        int currentColumn = 0;

        for (IndicatorSelectionDimension dimension : indicatorSelection.getLeftDimensions()) {
            SXSSFCell cell = initializeCell(attributeHeaderRow, currentColumn++);
            String label = resourceAccess.getDimensionLabelCurrentLocale(dimension.getId());
            cell.setCellValue(label);
            cell.setCellType(CellType.STRING);
            cell.setCellStyle(headerCellStyle);
        }

        for (MetadataAttributeType attribute : resourceAccess.getAttributesMetadata()) {
            if (!AttributeAttachmentLevelEnumType.OBSERVATION.equals(attribute.getAttachmentLevel())) {
                continue;
            }

            String label = getLabel(localisedStringsToInternationalString(attribute.getTitle()), resourceAccess.getLang());

            SXSSFCell cell = initializeCell(attributeHeaderRow, currentColumn++);
            cell.setCellValue(label);
            cell.setCellType(CellType.STRING);
            cell.setCellStyle(headerCellStyle);
        }

        currentRowCount = headerRow + 1;
    }

    private int getObservationAttributesCount() {
        int count = 0;
        for (MetadataAttributeType attribute : resourceAccess.getAttributesMetadata()) {
            if (AttributeAttachmentLevelEnumType.OBSERVATION.equals(attribute.getAttachmentLevel())) {
                count++;
            }
        }
        return count;
    }

    private String toDimensionValueLabel(String dimensionId, String dimensionValueId) {
        LabelVisualisationModeEnum labelVisualisation = resourceAccess.getDimensionLabelVisualisationMode(dimensionId);
        String dimensionValueLabel = null;
        if (labelVisualisation.isLabel() && labelVisualisation.isCode()) {
            dimensionValueLabel = "(" + dimensionValueId + ") " + resourceAccess.getDimensionValueLabelCurrentLocale(dimensionId, dimensionValueId);
        } else if (labelVisualisation.isLabel()) {
            dimensionValueLabel = resourceAccess.getDimensionValueLabelCurrentLocale(dimensionId, dimensionValueId);
        } else if (labelVisualisation.isCode()) {
            dimensionValueLabel = dimensionValueId;
        }
        return dimensionValueLabel;
    }

    private void content() {
        int observationsStartRow = currentRowCount;
        for (int i = 0; i < rowsOfData; i++) {
            SXSSFRow row = sheet.createRow(observationsStartRow + i);
            leftHeaderAtRow(i, row);
            observationsAtRow(i, row);
        }
        currentRowCount += rowsOfData;
    }



    private void addStringCell(SXSSFRow row, int column, String cellValue, CellStyle cellStyle) {
        SXSSFCell cell = initializeCell(row, column);
        cell.setCellValue(cellValue);
        cell.setCellType(CellType.STRING);
        cell.setCellStyle(cellStyle);
    }

    private void leftHeaderAtRow(int observationRowIndex, SXSSFRow row) {
        List<IndicatorSelectionDimension> leftDimensions = indicatorSelection.getLeftDimensions();

        int currentColumn = 0;

        // LEFT DIMENSIONS
        for (int leftDimensionIndex = 0; leftDimensionIndex < leftDimensions.size(); leftDimensionIndex++) {
            IndicatorSelectionDimension dimension = leftDimensions.get(leftDimensionIndex);
            String dimensionId = dimension.getId();
            int multiplier = indicatorSelection.getMultiplierForDimension(dimension);
            if (observationRowIndex % multiplier == 0) {
                String dimensionValueId = dimension.getSelectedDimensionValues().get((observationRowIndex / multiplier) % dimension.getSelectedDimensionValues().size());
                String dimensionValueLabel = toDimensionValueLabel(dimensionId, dimensionValueId);

                SXSSFCell cell = initializeCell(row, currentColumn);
                cell.setCellValue(dimensionValueLabel);
                cell.setCellType(CellType.STRING);
                cell.setCellStyle(headerCellStyle);
            } else {
                SXSSFCell cell = initializeCell(row, currentColumn);
                cell.setCellType(CellType.STRING);
                cell.setCellStyle(headerCellStyle);
            }
            currentColumn++;
        }

        // ATTRIBUTES AT OBSERVATION LEVEL
        Map<String, String> permutationAtCell = indicatorSelection.permutationAtCell(observationRowIndex, 0); // columna 0 solo para clave de permutación

        for (MetadataAttributeType attribute : resourceAccess.getAttributesMetadata()) {
            if (!AttributeAttachmentLevelEnumType.OBSERVATION.equals(attribute.getAttachmentLevel())) {
                continue;
            }

            String attributeId = attribute.getCode();
            String attributeValue = resourceAccess.measureAttributeValueAtPermutation(attributeId, permutationAtCell);
            String cellValue = "";

            if (attributeValue != null) {
                LabelVisualisationModeEnum labelVisualisation = resourceAccess.getAttributeLabelVisualisationMode(attributeId);
                if (labelVisualisation.isLabel()) {
                    String label = resourceAccess.getAttributeValueLabelCurrentLocale(attributeId, attributeValue);
                    cellValue = label != null ? label : attributeValue;
                }
                if (labelVisualisation.isCode()) {
                    if (!cellValue.isEmpty()) {
                        cellValue += " (" + attributeValue + ")";
                    } else {
                        cellValue = attributeValue;
                    }
                }
            }

            SXSSFCell cell = initializeCell(row, currentColumn);
            cell.setCellValue(cellValue);
            cell.setCellType(CellType.STRING);
            cell.setCellStyle(dataCellStyle);
            currentColumn++;
        }
    }

    private void observationsAtRow(int observationRowIndex, SXSSFRow row) {
        for (int j = 0; j < columnsOfData; j++) {
            Map<String, String> permutationAtCell = indicatorSelection.permutationAtCell(observationRowIndex, j);
            String observation = resourceAccess.observationAtPermutation(permutationAtCell);
            SXSSFCell cell = initializeCell(row, leftHeaderSizeOfData + j);

            if (observation != null) {
                if (NumberUtils.isNumber(observation)) {
                    cell.setCellValue(NumberUtils.createDouble(observation));
                    cell.setCellType(CellType.NUMERIC);
                } else {
                    cell.setCellValue(observation);
                    cell.setCellType(CellType.STRING);
                }
            }
            cell.setCellStyle(dataCellStyle);
        }
    }



    private static XSSFColor getXSSFColor(String RGB) {

        int red = Integer.parseInt(RGB.substring(0, 2), 16);
        int green = Integer.parseInt(RGB.substring(2, 4), 16);
        int blue = Integer.parseInt(RGB.substring(4, 6), 16);

        return new XSSFColor(new byte[]{(byte) red, (byte) green, (byte) blue}, new DefaultIndexedColorMap());
    }

    /**
     * Create a cellStyle with solid foreground color and font color
     *
     * @param foregroundColor required
     * @param fontColor optional
     * @return
     */
    private XSSFCellStyle createStyleSolid(XSSFColor fontColor, XSSFColor foregroundColor, Boolean bold) {
        if (foregroundColor == null && fontColor == null) {
            return null;
        }

        // We work with XLSX -> SXSSFWorkbook
        XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();

        if (foregroundColor != null) {
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setFillForegroundColor(foregroundColor);
        }

        if (fontColor != null) {
            XSSFFont font = (XSSFFont) workbook.createFont();
            // We work with XLSX -> SXSSFWorkbook
            font.setColor(fontColor);
            if (bold != null) {
                font.setBold(bold);
            }
            style.setFont(font);
        }

        return style;
    }

    private void addBorderToCellStyle(XSSFCellStyle cellStyle) {
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
    }

    private SXSSFCell initializeCell(SXSSFRow row, int headerColumn) {
        if (headerColumn > maxRightColumnIndexWithContent) {
            maxRightColumnIndexWithContent = headerColumn;
        }
        return row.createCell(headerColumn);
    }
}