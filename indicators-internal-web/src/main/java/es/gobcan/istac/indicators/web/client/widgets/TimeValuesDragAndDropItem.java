package es.gobcan.istac.indicators.web.client.widgets;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCanvasItem;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.widgets.TransferImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;

import es.gobcan.istac.indicators.core.dto.TimeValueDto;
import es.gobcan.istac.indicators.web.client.model.TimeValueRecord;

public class TimeValuesDragAndDropItem extends CustomCanvasItem {

    protected ListGrid sourceList;
    protected ListGrid targetList;

    protected boolean  required;

    public TimeValuesDragAndDropItem(String name, String title) {
        super(name, title);
        String dragDropType = new Date().toString();

        setCellStyle("dragAndDropCellStyle");

        sourceList = new ListGrid();
        sourceList.setShowHeader(false);
        sourceList.setLeaveScrollbarGap(false);
        sourceList.setAlternateRecordStyles(false);
        sourceList.setWidth(150);
        sourceList.setHeight(100);
        sourceList.setAutoFitMaxRecords(10);
        sourceList.setAutoFitData(Autofit.VERTICAL);
        sourceList.setCanDragRecordsOut(true);
        sourceList.setCanAcceptDroppedRecords(true);
        sourceList.setCanReorderFields(true);
        sourceList.setDragDataAction(DragDataAction.MOVE);
        sourceList.setSaveLocally(true);
        sourceList.setDragType(dragDropType);
        sourceList.setDropTypes(dragDropType);
        sourceList.addCellDoubleClickHandler(new CellDoubleClickHandler() {

            @Override
            public void onCellDoubleClick(CellDoubleClickEvent event) {
                sourceList.removeRecordClick(event.getRowNum());
                targetList.addData(event.getRecord());
            }
        });
        ListGridField sourceField = new ListGridField(TimeValueRecord.TITLE);
        sourceList.setFields(sourceField);

        targetList = new ListGrid();
        targetList.setLeaveScrollbarGap(false);
        targetList.setShowHeader(false);
        targetList.setAlternateRecordStyles(false);
        targetList.setWidth(150);
        targetList.setHeight(100);
        targetList.setAutoFitMaxRecords(10);
        targetList.setAutoFitData(Autofit.VERTICAL);
        targetList.setCanDragRecordsOut(true);
        targetList.setCanAcceptDroppedRecords(true);
        targetList.setCanReorderRecords(true);
        targetList.setSaveLocally(true);
        targetList.setDragType(dragDropType);
        targetList.setDropTypes(dragDropType);
        targetList.addCellDoubleClickHandler(new CellDoubleClickHandler() {

            @Override
            public void onCellDoubleClick(CellDoubleClickEvent event) {
                targetList.removeRecordClick(event.getRowNum());
                sourceList.addData(event.getRecord());
            }
        });
        ListGridField targetField = new ListGridField(TimeValueRecord.TITLE);
        targetList.setFields(targetField);

        VStack buttonStack = new VStack(10);
        buttonStack.setWidth(32);
        buttonStack.setHeight(74);
        buttonStack.setLayoutAlign(Alignment.CENTER);

        TransferImgButton rightImg = new TransferImgButton(TransferImgButton.RIGHT);
        rightImg.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                targetList.transferSelectedData(sourceList);
            }
        });

        TransferImgButton leftImg = new TransferImgButton(TransferImgButton.LEFT);
        leftImg.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                sourceList.transferSelectedData(targetList);
            }
        });

        TransferImgButton rightAll = new TransferImgButton(TransferImgButton.RIGHT_ALL);
        rightAll.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                Record[] records = sourceList.getRecords();
                sourceList.setData(new Record[]{});
                for (Record record : records) {
                    targetList.addData(record);
                }
            }
        });

        TransferImgButton leftAll = new TransferImgButton(TransferImgButton.LEFT_ALL);
        leftAll.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                Record[] records = targetList.getRecords();
                targetList.setData(new Record[]{});
                for (Record record : records) {
                    sourceList.addData(record);
                }
            }
        });

        buttonStack.addMember(rightImg);
        buttonStack.addMember(leftImg);
        buttonStack.addMember(rightAll);
        buttonStack.addMember(leftAll);

        HLayout hLayout = new HLayout(1);
        hLayout.addMember(sourceList);
        hLayout.addMember(buttonStack);
        hLayout.addMember(targetList);

        VLayout vLayout = new VLayout();
        vLayout.addMember(hLayout);

        setCanvas(vLayout);
    }

    public void setSourceValues(List<TimeValueDto> timeValueDtos) {
        clearValue();
        for (TimeValueDto timeValueDto : timeValueDtos) {
            TimeValueRecord record = new TimeValueRecord(timeValueDto);
            sourceList.addData(record);
        }
    }

    public List<String> getSelectedValues() {
        List<String> selectedTimeValues = new ArrayList<String>();
        ListGridRecord records[] = targetList.getRecords();
        for (ListGridRecord record : records) {
            TimeValueRecord timeValueRecord = (TimeValueRecord) record;
            selectedTimeValues.add(timeValueRecord.getTimeValue());
        }
        return selectedTimeValues;
    }

    @Override
    public void clearValue() {
        sourceList.selectAllRecords();
        sourceList.removeSelectedData();
        targetList.selectAllRecords();
        targetList.removeSelectedData();
    }

}
