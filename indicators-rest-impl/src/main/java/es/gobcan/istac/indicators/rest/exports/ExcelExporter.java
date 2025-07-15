package es.gobcan.istac.indicators.rest.exports;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFCreationHelper;
import org.apache.poi.xssf.streaming.SXSSFDrawing;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.siemac.edatos.core.common.lang.LocaleUtil;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.AttributeAttachmentLevelType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.rest.dto.CellCommentDetails;
import es.gobcan.istac.indicators.rest.dto.IndicatorSelection;
import es.gobcan.istac.indicators.rest.dto.IndicatorSelectionDimension;
import es.gobcan.istac.indicators.rest.enume.ExportFormatEnum;
import es.gobcan.istac.indicators.rest.enume.LabelVisualisationModeEnum;
import es.gobcan.istac.indicators.rest.types.MetadataAttributeType;

public class ExcelExporter {

    // https://andriymz.github.io/misc/apache-poi-slow-excel-generation/
    private static final int         ROW_ACCESS_WINDOW_SIZE         = 100;
    private static final int         HEURISTIC_ROWS                 = 75;

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
    private SXSSFCreationHelper      creationHelper;
    private int                      maxRightColumnIndexWithContent = 0;

    private static Logger            log                            = LoggerFactory.getLogger(ExcelExporter.class);

    private XSSFCellStyle            dataCellStyle;
    private XSSFCellStyle            headerCellStyle;
    private XSSFCellStyle            listTitleCellStyle;

    private SXSSFDrawing             drawing;

    private boolean                  highPerformanceMode            = false;
    private Integer                  cellThreshold                  = 250_000;
    private String                   unitMeasure                    = "UNIDAD_MEDIDA";
    private String                   unitMultiplier                 = "MULTIPLICADOR_UNIDAD";

    private ExportFormatEnum         exportFormatEnum               = null;

    public ExcelExporter(ExportFormatEnum exportFormatEnum, ResourceAccess resourceAccess) throws MetamacException {
        this.resourceAccess = resourceAccess;
        this.indicatorSelection = resourceAccess.getDataSelection();
        this.exportFormatEnum = exportFormatEnum;
    }

    public void write(OutputStream os) throws MetamacException {
        initialize();
        header();
        contentHeader();
        content();
        footer();

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
        drawing = sheet.createDrawingPatriarch();
        rowsOfData = indicatorSelection.getRows();
        columnsOfData = indicatorSelection.getColumns();
        leftHeaderSizeOfData = indicatorSelection.getLeftDimensions().size();
        creationHelper = (SXSSFCreationHelper) workbook.getCreationHelper();
        headerCellStyle = createStyleSolid(COLOR_BLACK, COLOR_WHITE, true);
        dataCellStyle = createStyleSolid(COLOR_BLACK, null, null);
        listTitleCellStyle = createStyleSolid(COLOR_BLACK, null, true);
        addBorderToCellStyle(dataCellStyle);
    }

    private void header() {
        int headerRow = 0;
        Locale locale = LocaleUtil.getLocaleFromLocaleString(resourceAccess.getLang());

        // Title
        String title = resourceAccess.getName().get(resourceAccess.getLang());
        if (!StringUtils.isEmpty(title)) {
            SXSSFRow rowTitle = sheet.createRow(headerRow++);
            addStringCell(rowTitle, 0, title, headerCellStyle);
        }

        headerRow++;
        headerRow++;
        headerRow++;

        currentRowCount = headerRow;
    }

    private void contentHeader() {
        int headerRow = currentRowCount;
        for (IndicatorSelectionDimension dimension : indicatorSelection.getTopDimensions()) {
            SXSSFRow row = sheet.createRow(headerRow);
            List<String> selectedDimensionValues = dimension.getSelectedDimensionValues();
            int headerColumn = leftHeaderSizeOfData;
            int multiplier = indicatorSelection.getMultiplierForDimension(dimension);
            int repeat = columnsOfData / (multiplier * selectedDimensionValues.size());
            for (int i = 0; i < repeat; i++) {
                for (String selectedDimensionValue : selectedDimensionValues) {
                    String dimensionValueLabel = toDimensionValueLabel(dimension.getId(), selectedDimensionValue);

                    SXSSFCell cell = initializeCell(row, headerColumn);

                    CellCommentDetails cellDetails = resourceAccess.attributesAtPermutation(indicatorSelection.permutationAtDimension(dimension.getId(), selectedDimensionValue), unitMeasure,
                            unitMultiplier);
                    addCellComment(cellDetails, cell);

                    cell.setCellValue(dimensionValueLabel);
                    cell.setCellType(CellType.STRING);
                    cell.setCellStyle(headerCellStyle);
                    headerColumn += multiplier;
                }
            }
            headerRow++;
        }
        currentRowCount = headerRow;
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

    private void footer() {
        Locale locale = LocaleUtil.getLocaleFromLocaleString(resourceAccess.getLang());
        currentRowCount += 2;

        // Dataset Notes Attributes
        {
            Integer newCurrentRowCount = addBodyOfDatasetAttributes(currentRowCount);
            // Some dataset note attribute was added
            if (newCurrentRowCount > currentRowCount) {
                addFootNotesTitle(currentRowCount);
                currentRowCount = newCurrentRowCount;
            }
        }

        currentRowCount++;
    }

    private int addBodyOfDatasetAttributes(int footerRow) {
        final int COLUMN_OF_BODY_NOTES_START = 1;

        for (MetadataAttributeType attribute : resourceAccess.getAttributesMetadata()) {
            if (!AttributeAttachmentLevelType.DATASET.equals(attribute.getAttachmentLevel())) {
                continue;
            }
            String attributeId = attribute.getCode();
            String[] attributeValues = resourceAccess.getAttributeValues(attributeId);

            if (attributeValues == null) {
                continue;
            }
            // FIXME
            String attributeName = "getLabel(resourceAccess.getAttributesMetadata().get(Integer.parseInt(attributeId)).getTitle(), resourceAccess.getLang())";
            String attributeValue = resourceAccess.obtainAttributeValue(attributeId, 0);
            if (StringUtils.isNotBlank(attributeValue)) {
                attributeValue = attributeValue.trim();

                // Data table cell
                SXSSFRow row = sheet.createRow(++footerRow);
                addStringCell(row, COLUMN_OF_BODY_NOTES_START, attributeName, headerCellStyle);
                XSSFCellStyle styleSolid = dataCellStyle;
                addBorderToCellStyle(styleSolid);
                addStringCell(row, COLUMN_OF_BODY_NOTES_START + 1, attributeValue, styleSolid);
            }
        }
        return footerRow;
    }

    private void addFootNotesTitle(int footerRow) {
        SXSSFRow row = sheet.createRow(footerRow);
        // FIXME
        String messageForCode = "LocaleUtil.getMessageForCode(MessageKeyType.MESSAGE_NOTES_TABLE, LocaleUtil.getLocaleFromLocaleString(resourceAccess.getLang()))";
        addStringCell(row, 0, messageForCode, listTitleCellStyle);
    }

    private void addStringCell(SXSSFRow row, int column, String cellValue, CellStyle cellStyle) {
        SXSSFCell cell = initializeCell(row, column);
        cell.setCellValue(cellValue);
        cell.setCellType(CellType.STRING);
        cell.setCellStyle(cellStyle);
    }

    private void leftHeaderAtRow(int observationRowIndex, SXSSFRow row) {
        List<IndicatorSelectionDimension> leftDimensions = indicatorSelection.getLeftDimensions();
        for (int leftDimensionIndex = 0; leftDimensionIndex < leftDimensions.size(); leftDimensionIndex++) {
            IndicatorSelectionDimension dimension = leftDimensions.get(leftDimensionIndex);
            String dimensionId = dimension.getId();
            int multiplier = indicatorSelection.getMultiplierForDimension(dimension);
            if (observationRowIndex % multiplier == 0) {
                String dimensionValueId = dimension.getSelectedDimensionValues().get((observationRowIndex / multiplier) % dimension.getSelectedDimensionValues().size());
                String dimensionValueLabel = toDimensionValueLabel(dimensionId, dimensionValueId);

                SXSSFCell cell = initializeCell(row, leftDimensionIndex);

                CellCommentDetails cellDetails = resourceAccess.attributesAtPermutation(indicatorSelection.permutationAtDimension(dimension.getId(), dimensionValueId), unitMeasure, unitMultiplier);
                addCellComment(cellDetails, cell);

                cell.setCellValue(dimensionValueLabel);
                cell.setCellType(CellType.STRING);
                cell.setCellStyle(headerCellStyle);
            } else {
                // Empty Cells
                SXSSFCell cell = initializeCell(row, leftDimensionIndex);
                cell.setCellType(CellType.STRING);
                cell.setCellStyle(headerCellStyle);
            }
        }
    }

    private void observationsAtRow(int observationRowIndex, SXSSFRow row) {
        for (int j = 0; j < columnsOfData; j++) {
            Map<String, String> permutationAtCell = indicatorSelection.permutationAtCell(observationRowIndex, j);
            String observation = resourceAccess.observationAtPermutation(permutationAtCell);
            SXSSFCell cell = initializeCell(row, leftHeaderSizeOfData + j);

            addCellComment(resourceAccess.attributesAtPermutation(permutationAtCell, unitMeasure, unitMultiplier), cell);

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

    private void addCellComment(CellCommentDetails cellCommentDetails, SXSSFCell cell) {
        String commentString = cellCommentDetails.getValue();
        if (commentString == null) {
            return;
        }

        if (highPerformanceMode) {
            SXSSFCell commentCell = initializeCell((SXSSFRow) cell.getRow(), cell.getColumnIndex() + 1);
            commentCell.setCellValue(commentString);
            commentCell.setCellType(CellType.STRING);
        } else {
            addCellCommentAsComment(cellCommentDetails, cell, commentString);
        }
    }

    private void addCellCommentAsComment(CellCommentDetails cellCommentDetails, SXSSFCell cell, String commentString) {
        // When the comment box is visible, have it show in a box
        ClientAnchor anchor = creationHelper.createClientAnchor();
        anchor.setCol1(cell.getColumnIndex());

        anchor.setCol2(cell.getColumnIndex() + cellCommentDetails.calculateNumberOfColumnsToAccomodateComment());
        anchor.setRow1(cell.getRowIndex());
        anchor.setRow2(cell.getRowIndex() + cellCommentDetails.calculateNumberOfRowsToAccomodateComment());

        Comment comment = drawing.createCellComment(anchor);
        RichTextString str = creationHelper.createRichTextString(commentString);
        comment.setString(str);

        cell.setCellComment(comment);
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