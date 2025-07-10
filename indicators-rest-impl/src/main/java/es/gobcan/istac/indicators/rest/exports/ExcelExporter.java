package es.gobcan.istac.indicators.rest.exports;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
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
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.AttributeAttachmentLevelType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.rest.dto.IndicatorSelection;
import es.gobcan.istac.indicators.rest.enume.ExportFormatEnum;
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

    // public ExcelExporter(DatasetBase dataset, IndicatorSelection indicatorSelection, String lang, String langAlternative, Integer cellThreshold, String unitMeasure, String unitMultiplier)
    // throws MetamacException {
    // resourceAccess = new ResourceAccess(dataset, indicatorSelection, lang, langAlternative);
    // this.indicatorSelection = indicatorSelection;
    // this.cellThreshold = cellThreshold;
    // this.unitMeasure = unitMeasure;
    // this.unitMultiplier = unitMultiplier;
    // enableHighPerformanceModeIfNeeded(dataset.getUrn());
    // }
    //
    // public ExcelExporter(QueryBase query, DatasetBase relatedDataset, IndicatorSelection indicatorSelection, String lang, String langAlternative, Integer cellThreshold, String unitMeasure,
    // String unitMultiplier) throws MetamacException {
    // resourceAccess = new ResourceAccess(query, relatedDataset, indicatorSelection, lang, langAlternative);
    //
    // this.indicatorSelection = indicatorSelection;
    // this.cellThreshold = cellThreshold;
    // this.unitMeasure = unitMeasure;
    // this.unitMultiplier = unitMultiplier;
    // enableHighPerformanceModeIfNeeded(relatedDataset.getUrn());
    // }

    public ExcelExporter(ExportFormatEnum exportFormatEnum, ResourceAccess resourceAccess) throws MetamacException {
        this.resourceAccess = resourceAccess;
        indicatorSelection = resourceAccess.getDataSelection();
        this.exportFormatEnum = exportFormatEnum;
    }

    /*
     * Apache POI is not very performant when drawing comment cells.
     * - It's slow, because it needs to copy xml internally. This problem can produce timeouts
     * - And it consumes a high amount of ram, because it keeps the whole uncompressed XML with the comments
     * in memory, that later is efficiently zipped into the xlsx. This problem can produce "out of memory" errors
     * The workaround consists on removing the inneficient comments if we are above a certain threshold of them. In that case
     * we'll reorganize the table and present the attributes in a column besides the observations
     * This approach is highly heuristic and subject to change on the selected value
     * getPrimaryMeasureAttributesCount is on itself, an upper bound, because if we have several primary measure attributes,
     * they could be drawn on the same cell
     * This way of calculate the number of comment cells is subject to change when the attributes at other levels are implemented
     */
    // FIXME: NO USAGE
    // private void enableHighPerformanceModeIfNeeded(String urn) throws MetamacException {
    // highPerformanceMode = resourceAccess.getPrimaryMeasureAttributesCount() > cellThreshold;
    // if (highPerformanceMode) {
    // log.info("Excel export enabled high performance mode; Aproximate number of non empty cells: "
    // + String.valueOf(resourceAccess.getPrimaryMeasureAttributesCount() + ", thresholdSize : " + cellThreshold));
    // indicatorSelection.moveTopDimensionsToLeft();
    // }
    //
    // Long dimensionRows = Long.valueOf(indicatorSelection.getRows());
    // Long dimensionCols = Long.valueOf(indicatorSelection.getColumns());
    // if ((dimensionRows + HEURISTIC_ROWS) > SpreadsheetVersion.EXCEL2007.getMaxRows() || dimensionCols > SpreadsheetVersion.EXCEL2007.getMaxColumns()) {
    // throw new MetamacException(ServiceExceptionType.DATASET_OBSERVATIONS_EXCEED_MAX_FOR_XLSX, urn);
    // }
    // }

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
        String title = "getLabel(resourceAccess.getName())";
        // if (!StringUtils.isEmpty(title)) {
        // SXSSFRow rowTitle = sheet.createRow(headerRow++);
        // addStringCell(rowTitle, 0, title, headerCellStyle);
        // }
        //
        // // Subject area
        // ResourcesStatisticalResourceBase subjectAreas = resourceAccess.getMetadata().getSubjectAreas();
        // StringBuilder valueName = new StringBuilder();
        // if (subjectAreas != null) {
        // Iterator<ResourceStatisticalResourceBase> iterator = subjectAreas.getResources().iterator();
        // while (iterator.hasNext()) {
        // ResourceStatisticalResourceBase subjectArea = iterator.next();
        // valueName.append(getLabel(subjectArea.getName()));
        // if (iterator.hasNext()) {
        // valueName.append("; ");
        // }
        // }
        // }
        //
        // if (valueName.length() > 0) {
        // SXSSFRow rowSubjectArea = sheet.createRow(headerRow++);
        // addStringCell(rowSubjectArea, 0, valueName.toString(), headerCellStyle);
        // }
        //
        // headerRow++; // Blank row
        //
        // // Last update date
        // Date lastUpdateDate = resourceAccess.getMetadata().getLastUpdate();
        // if (lastUpdateDate != null) {
        // SXSSFRow row = sheet.createRow(headerRow++);
        // String messageForCode = LocaleUtil.getMessageForCode(MessageKeyType.MESSAGE_LAST_UPDATE, locale);
        // addStringCell(row, 0, messageForCode, listTitleCellStyle);
        // DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
        // addStringCell(row, 1, dateFormat.format(lastUpdateDate), null);
        // }
        //
        // // Units
        // int titleRowForUnitsIndex = headerRow++;
        // boolean hasAnyUnits = false;
        // if (resourceAccess.existsContVariable()) { // Measure dimension
        // // Indexed to ContVariable Values
        // for (EnumeratedDimensionValue dimensionValue : resourceAccess.getSelectedValuesForMeasureDimension()) {
        // final String unit = buildUnitText(dimensionValue);
        // if (unit != null) {
        // SXSSFRow row = sheet.createRow(headerRow++);
        // addStringCell(row, 1, unit, null);
        // hasAnyUnits = true;
        // }
        // }
        // } else { // Measure attribute
        // Attribute measureAttribute = resourceAccess.getMeasureAttribute();
        // if (measureAttribute != null) {
        // // By Metamac Constraints, the measure attribute has dataset attachment and enumerated representation.
        // // Quantity
        // String[] attributeValuesFromData = resourceAccess.getAttributeValues(measureAttribute.getId()); // Enumerated representation
        // if (attributeValuesFromData == null || attributeValuesFromData.length != 1) {
        // throw new RuntimeException("No instances of measure attribute type in the dataset. This is a Metamac error.");
        // }
        // AttributeValues attributeValuesFromMetadata = measureAttribute.getAttributeValues();
        // if (attributeValuesFromMetadata instanceof EnumeratedAttributeValues) {
        // // En este caso sólo debería de tener un único Valor la representación ENUMERADA
        // List<EnumeratedAttributeValue> metadataAttributeValues = ((EnumeratedAttributeValues) attributeValuesFromMetadata).getValues();
        // for (EnumeratedAttributeValue attributeValue : metadataAttributeValues) {
        // if (attributeValue.getId().equals(attributeValuesFromData[0])) {
        // final String unit = buildUnitText(attributeValue);
        // if (unit != null) {
        // SXSSFRow row = sheet.createRow(headerRow++);
        // addStringCell(row, 1, unit, null);
        // hasAnyUnits = true;
        // }
        // }
        // }
        // }
        // } else {
        // throw new RuntimeException("No attribute of type Measure or Measure Dimension found in the dataset. This is a Metamac error.");
        // }
        // }
        // if (hasAnyUnits) {
        // SXSSFRow titleRowForUnits = sheet.createRow(titleRowForUnitsIndex);
        // String messageForCode = LocaleUtil.getMessageForCode(MessageKeyType.MESSAGE_UNITS, locale);
        // addStringCell(titleRowForUnits, 0, messageForCode, listTitleCellStyle);
        // }

        headerRow++; // Blank row

        // Fixed Dimensions
        StringBuilder fixedDimensionsText = new StringBuilder();
        // for (IndicatorSelectionDimension indicatorSelectionDimension : indicatorSelection.getFixedDimensions()) {
        // String dimensionText = resourceAccess.applyLabelVisualizationModeForDimension(indicatorSelectionDimension.getId());
        // String dimensionValueText = resourceAccess.applyLabelVisualizationModeForDimensionValue(indicatorSelectionDimension.getId(),
        // indicatorSelectionDimension.getSelectedDimensionValues().get(0));
        // fixedDimensionsText.append(dimensionText).append(" = ").append(dimensionValueText).append(", ");
        //
        // }

// if (fixedDimensionsText.length() > 0) {
// fixedDimensionsText.delete(fixedDimensionsText.length() - 3, fixedDimensionsText.length()); // delete last comma and space
// String by = LocaleUtil.getMessageForCode(MessageKeyType.MESSAGE_LABEL_PARA, locale);
// SXSSFRow rowTitle = sheet.createRow(headerRow++);
// addStringCell(rowTitle, 0, by + " " + fixedDimensionsText.toString(), null);
// headerRow += 1;
// }

        headerRow++;

        currentRowCount = headerRow;
    }

    // Samples:
    // - Unidad de medida (Índice de palomas): Índice de palomas
    // - Unidad de medida (Variacion): Porcentaje (Unidades)
    private String buildUnitText(Resource value) {
        StringBuilder unitBuilder = new StringBuilder();
        Locale locale = LocaleUtil.getLocaleFromLocaleString(resourceAccess.getLang());
        InternationalString unitCode = null;
        // Ugly, but...
        // if (value instanceof EnumeratedDimensionValue) {
        // unitCode = resourceAccess.extractUnitCode((EnumeratedDimensionValue) value, unitMeasure, unitMultiplier);
        // } else if (value instanceof EnumeratedAttributeValue) {
        // unitCode = resourceAccess.extractUnitCode((EnumeratedAttributeValue) value, unitMeasure, unitMultiplier);
        // }
        if (unitCode == null) {
            return null;
        }
        // @formatter:off
//        unitBuilder
//            .append(LocaleUtil.getMessageForCode(MessageKeyType.MESSAGE_UNITS_UNIT, locale))
//            .append(" (").append(getLabel(value.getName())).append("): ")
//            .append(getLabel(unitCode));
        // @formatter:on
        return unitBuilder.toString();
    }
    // FIXME NO USAGE
    // private String buildContactText(Contact contact) {
    // StringBuilder contactBuilder = new StringBuilder();
    // Locale locale = LocaleUtil.getLocaleFromLocaleString(resourceAccess.getLang());
    // final String SEPARATOR_FIELD = ". ";
    // final String SEPARATOR_LABEL = ": ";
    // final String SEPARATOR_ITEM_LIST = ", ";
    //
//        // @formatter:off
//        if (contact.getName() != null) {
//            contactBuilder
//                .append(getLabel(contact.getName()))
//                .append(SEPARATOR_FIELD);
//        }
//
//        if (contact.getResponsibility() != null) {
//            contactBuilder
//                .append(getLabel(contact.getResponsibility()))
//                .append(SEPARATOR_FIELD);
//        }
//
//        if (contact.getOrganisationUnit() != null) {
//            contactBuilder
//                .append(getLabel(contact.getOrganisationUnit()))
//                .append(SEPARATOR_FIELD);
//        }
//
//        if (contact.getEmails().size() > 0) {
//            contactBuilder
//                .append(LocaleUtil.getMessageForCode(MessageKeyType.MESSAGE_CONTACTS_EMAILS, locale))
//                .append(SEPARATOR_LABEL).append(StringUtils.join(contact.getEmails(), SEPARATOR_ITEM_LIST))
//                .append(SEPARATOR_FIELD);
//        }
//
//        if (contact.getTelephones().size() > 0) {
//            contactBuilder
//                .append(LocaleUtil.getMessageForCode(MessageKeyType.MESSAGE_CONTACTS_TELEPHONES, locale))
//                .append(SEPARATOR_LABEL).append(StringUtils.join(contact.getTelephones(), SEPARATOR_ITEM_LIST))
//                .append(SEPARATOR_FIELD);
//        }
//
//        if (contact.getFaxes().size() > 0) {
//            contactBuilder
//                .append(LocaleUtil.getMessageForCode(MessageKeyType.MESSAGE_CONTACTS_FAXES, locale))
//                .append(SEPARATOR_LABEL).append(StringUtils.join(contact.getFaxes(), SEPARATOR_ITEM_LIST))
//                .append(SEPARATOR_FIELD);
//        }
//
//        if (contact.getUrls().size() > 0) {
//            contactBuilder
//                .append(StringUtils.join(contact.getUrls(), SEPARATOR_ITEM_LIST))
//                .append(SEPARATOR_FIELD);
//        }
//
//        // @formatter:on
    //
    // return contactBuilder.toString();
    // }

    // private void contentHeader() {
    // int headerRow = currentRowCount;
    // for (IndicatorSelectionDimension dimension : indicatorSelection.getTopDimensions()) {
    // SXSSFRow row = sheet.createRow(headerRow);
    // List<String> selectedDimensionValues = dimension.getSelectedDimensionValues();
    // int headerColumn = leftHeaderSizeOfData;
    // int multiplier = indicatorSelection.getMultiplierForDimension(dimension);
    // int repeat = columnsOfData / (multiplier * selectedDimensionValues.size());
    // for (int i = 0; i < repeat; i++) {
    // for (String selectedDimensionValue : selectedDimensionValues) {
    // String dimensionValueLabel = toDimensionValueLabel(dimension.getId(), selectedDimensionValue);
    //
    // SXSSFCell cell = initializeCell(row, headerColumn);
    //
    // CellCommentDetails cellDetails = resourceAccess.attributesAtPermutation(indicatorSelection.permutationAtDimension(dimension.getId(), selectedDimensionValue), unitMeasure,
    // unitMultiplier);
    // addCellComment(cellDetails, cell);
    //
    // cell.setCellValue(dimensionValueLabel);
    // cell.setCellType(CellType.STRING);
    // cell.setCellStyle(headerCellStyle);
    // headerColumn += multiplier;
    // }
    // }
    // headerRow++;
    // }
    // currentRowCount = headerRow;
    // }

    private void content() {
        int observationsStartRow = currentRowCount;
        // for (int i = 0; i < rowsOfData; i++) {
        // SXSSFRow row = sheet.createRow(observationsStartRow + i);
        // leftHeaderAtRow(i, row);
        // observationsAtRow(i, row);
        // }
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

        // FIXME all comments
        // // Rights Holder
        // Organisation rightsHolder = resourceAccess.getMetadata().getRightsHolder();
        // if (rightsHolder != null) {
        // String rightsHolderLabel = getLabel(rightsHolder.getName());
        //
        // SXSSFRow row = sheet.createRow(currentRowCount++);
        // String messageForCode = LocaleUtil.getMessageForCode(MessageKeyType.MESSAGE_RIGHTS_HOLDER, locale);
        // addStringCell(row, 0, messageForCode, listTitleCellStyle);
        // addStringCell(row, 1, rightsHolderLabel, null);
        // }
        //
        // // Copyright date
        // Integer copyrightDate = resourceAccess.getMetadata().getCopyrightDate();
        // if (copyrightDate != null) {
        // SXSSFRow row = sheet.createRow(currentRowCount++);
        // String messageForCode = LocaleUtil.getMessageForCode(MessageKeyType.MESSAGE_COPYRIGHT, locale);
        // addStringCell(row, 0, messageForCode, listTitleCellStyle);
        // addStringCell(row, 1, copyrightDate.toString(), null);
        // }
        //
        // // Contacts
        // if (rightsHolder != null && rightsHolder.getContacts() != null && rightsHolder.getContacts().getTotal().longValue() > 0) {
        // SXSSFRow rowHeader = sheet.createRow(currentRowCount++);
        // String messageForCode = LocaleUtil.getMessageForCode(MessageKeyType.MESSAGE_CONTACTS, locale);
        // addStringCell(rowHeader, 0, messageForCode, listTitleCellStyle);
        // for (Contact contact : rightsHolder.getContacts().getContacts()) {
        // SXSSFRow row = sheet.createRow(currentRowCount++);
        // addStringCell(row, 1, buildContactText(contact), null);
        // }
        // }
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
            // String attributeName = getLabel(resourceAccess.getAttributesMetadata().get(attributeId).getName());
            // String attributeValue = resourceAccess.obtainAttributeValue(attributeId, 0);
            String attributeName = "getLabel(resourceAccess.getAttributesMetadata().get(attributeId).getName())";
            String attributeValue = "atributeValue";
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

    // private void leftHeaderAtRow(int observationRowIndex, SXSSFRow row) {
    // List<IndicatorSelectionDimension> leftDimensions = indicatorSelection.getLeftDimensions();
    // for (int leftDimensionIndex = 0; leftDimensionIndex < leftDimensions.size(); leftDimensionIndex++) {
    // IndicatorSelectionDimension dimension = leftDimensions.get(leftDimensionIndex);
    // String dimensionId = dimension.getId();
    // int multiplier = indicatorSelection.getMultiplierForDimension(dimension);
    // if (observationRowIndex % multiplier == 0) {
    // String dimensionValueId = dimension.getSelectedDimensionValues().get((observationRowIndex / multiplier) % dimension.getSelectedDimensionValues().size());
    // String dimensionValueLabel = toDimensionValueLabel(dimensionId, dimensionValueId);
    //
    // SXSSFCell cell = initializeCell(row, leftDimensionIndex);
    //
    // CellCommentDetails cellDetails = resourceAccess.attributesAtPermutation(indicatorSelection.permutationAtDimension(dimension.getId(), dimensionValueId), unitMeasure, unitMultiplier);
    // addCellComment(cellDetails, cell);
    //
    // cell.setCellValue(dimensionValueLabel);
    // cell.setCellType(CellType.STRING);
    // cell.setCellStyle(headerCellStyle);
    // } else {
    // // Empty Cells
    // SXSSFCell cell = initializeCell(row, leftDimensionIndex);
    // cell.setCellType(CellType.STRING);
    // cell.setCellStyle(headerCellStyle);
    // }
    // }
    // }

// private void observationsAtRow(int observationRowIndex, SXSSFRow row) {
// for (int j = 0; j < columnsOfData; j++) {
// Map<String, String> permutationAtCell = indicatorSelection.permutationAtCell(observationRowIndex, j);
// String observation = resourceAccess.observationAtPermutation(permutationAtCell);
// SXSSFCell cell = initializeCell(row, leftHeaderSizeOfData + j);
//
// addCellComment(resourceAccess.attributesAtPermutation(permutationAtCell, unitMeasure, unitMultiplier), cell);
//
// if (observation != null) {
// if (NumberUtils.isNumber(observation)) {
// cell.setCellValue(NumberUtils.createDouble(observation));
// cell.setCellType(CellType.NUMERIC);
// } else {
// cell.setCellValue(observation);
// cell.setCellType(CellType.STRING);
// }
// }
// cell.setCellStyle(dataCellStyle);
// }
// }

// private void addCellComment(CellCommentDetails cellCommentDetails, SXSSFCell cell) {
// String commentString = cellCommentDetails.getValue();
// if (commentString == null) {
// return;
// }
//
// if (highPerformanceMode) {
// SXSSFCell commentCell = initializeCell((SXSSFRow) cell.getRow(), cell.getColumnIndex() + 1);
// commentCell.setCellValue(commentString);
// commentCell.setCellType(CellType.STRING);
// } else {
// addCellCommentAsComment(cellCommentDetails, cell, commentString);
// }
// }
//
// private void addCellCommentAsComment(CellCommentDetails cellCommentDetails, SXSSFCell cell, String commentString) {
// // When the comment box is visible, have it show in a box
// ClientAnchor anchor = creationHelper.createClientAnchor();
// anchor.setCol1(cell.getColumnIndex());
//
// anchor.setCol2(cell.getColumnIndex() + cellCommentDetails.calculateNumberOfColumnsToAccomodateComment());
// anchor.setRow1(cell.getRowIndex());
// anchor.setRow2(cell.getRowIndex() + cellCommentDetails.calculateNumberOfRowsToAccomodateComment());
//
// Comment comment = drawing.createCellComment(anchor);
// RichTextString str = creationHelper.createRichTextString(commentString);
// comment.setString(str);
//
// cell.setCellComment(comment);
// }

// private String toDimensionValueLabel(String dimensionId, String dimensionValueId) {
// LabelVisualisationModeEnum labelVisualisation = resourceAccess.getDimensionLabelVisualisationMode(dimensionId);
// String dimensionValueLabel = null;
// if (labelVisualisation.isLabel() && labelVisualisation.isCode()) {
// dimensionValueLabel = "(" + dimensionValueId + ") " + resourceAccess.getDimensionValueLabelCurrentLocale(dimensionId, dimensionValueId);
// } else if (labelVisualisation.isLabel()) {
// dimensionValueLabel = resourceAccess.getDimensionValueLabelCurrentLocale(dimensionId, dimensionValueId);
// } else if (labelVisualisation.isCode()) {
// dimensionValueLabel = dimensionValueId;
// }
// return dimensionValueLabel;
// }

    public void write(OutputStream os) throws MetamacException {
        initialize();
        // header();
        // contentHeader();
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

    private static XSSFColor getXSSFColor(String RGB) {

        int red = Integer.parseInt(RGB.substring(0, 2), 16);
        int green = Integer.parseInt(RGB.substring(2, 4), 16);
        int blue = Integer.parseInt(RGB.substring(4, 6), 16);

        return new XSSFColor(new byte[]{(byte) red, (byte) green, (byte) blue}, new DefaultIndexedColorMap());
    }

    // public String getLabel(InternationalString internationalString) {
    // return PortalUtils.getLabel(internationalString, resourceAccess.getLang(), resourceAccess.getLangDefault());
    // }

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