package es.gobcan.istac.indicators.web.client.widgets;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.siemac.metamac.web.common.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCanvasItem;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.widgets.TransferImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;
import com.smartgwt.client.widgets.grid.events.RecordDropEvent;
import com.smartgwt.client.widgets.grid.events.RecordDropHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;

import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.GeographicalValueBaseDto;
import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.web.client.model.GeographicalValueRecord;
import es.gobcan.istac.indicators.web.client.utils.CommonUtils;

public class GeographicalValuesDragAndDropItem extends CustomCanvasItem {

    private final static String GEOGRAPHICAL_GRANULARITY_SELECTION = "geo-gran";

    private CustomDynamicForm   form;

    protected ListGrid          sourceList;
    protected ListGrid          targetList;

    protected boolean           required;

    public GeographicalValuesDragAndDropItem(String name, String title, String formItemWidth) {
        super(name, title);
        setWidth(formItemWidth);

        String dragDropType = new Date().toString();

        setCellStyle("dragAndDropCellStyle");

        // SelectItem to select the geographical granularity
        form = new CustomDynamicForm();
        form.setColWidths("100%");
        SelectItem selectItem = new SelectItem(GEOGRAPHICAL_GRANULARITY_SELECTION);
        selectItem.setShowTitle(false);
        selectItem.setWidth(formItemWidth);
        form.setFields(selectItem);

        sourceList = new ListGrid();
        sourceList.setShowHeader(false);
        sourceList.setLeaveScrollbarGap(false);
        sourceList.setAlternateRecordStyles(false);
        sourceList.setWidth("*");
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
                addNonDuplicatedRecordToTarget(event.getRecord());
            }
        });
        sourceList.addRecordDropHandler(new RecordDropHandler() {

            @Override
            public void onRecordDrop(RecordDropEvent event) {
                event.cancel();
            }
        });
        sourceList.setCanDrop(false);
        ListGridField sourceField = new ListGridField(GeographicalValueRecord.TITLE);
        sourceList.setFields(sourceField);

        targetList = new ListGrid();
        targetList.setLeaveScrollbarGap(false);
        targetList.setShowHeader(false);
        targetList.setAlternateRecordStyles(false);
        targetList.setWidth("*");
        targetList.setHeight(100);
        targetList.setAutoFitMaxRecords(10);
        targetList.setAutoFitData(Autofit.VERTICAL);
        targetList.setCanDragRecordsOut(true);
        targetList.setCanAcceptDroppedRecords(true);
        targetList.setCanReorderRecords(true);
        targetList.setSaveLocally(true);
        targetList.setDragType(dragDropType);
        targetList.setDropTypes(dragDropType);
        targetList.addRecordDropHandler(new RecordDropHandler() {

            @Override
            public void onRecordDrop(RecordDropEvent event) {
                if (event.getDropRecords() != null) {
                    ListGridRecord[] records = event.getDropRecords();
                    for (Record record : records) {
                        addNonDuplicatedRecordToTarget(record);
                    }
                    event.cancel();
                }
            }
        });
        targetList.setCanRemoveRecords(true);
        targetList.setRemoveIcon(GlobalResources.RESOURCE.deleteListGrid().getURL());
        ListGridField targetField = new ListGridField(GeographicalValueRecord.TITLE);
        targetList.setFields(targetField);

        VStack buttonStack = new VStack(10);
        buttonStack.setWidth(32);
        buttonStack.setHeight(74);
        buttonStack.setLayoutAlign(Alignment.CENTER);

        TransferImgButton rightImg = new TransferImgButton(TransferImgButton.RIGHT);
        rightImg.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                Record[] records = sourceList.getSelectedRecords();
                for (Record record : records) {
                    addNonDuplicatedRecordToTarget(record);
                }
            }
        });

        TransferImgButton rightAll = new TransferImgButton(TransferImgButton.RIGHT_ALL);
        rightAll.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                Record[] records = sourceList.getRecords();
                for (Record record : records) {
                    addNonDuplicatedRecordToTarget(record);
                }
            }
        });

        buttonStack.addMember(rightImg);
        buttonStack.addMember(rightAll);

        HLayout hLayout = new HLayout(1);
        hLayout.addMember(sourceList);
        hLayout.addMember(buttonStack);
        hLayout.addMember(targetList);

        VLayout vLayout = new VLayout();
        vLayout.setWidth(formItemWidth);
        vLayout.addMember(form);
        vLayout.addMember(hLayout);

        setCanvas(vLayout);
    }

    public void setGeographicalGranularities(List<GeographicalGranularityDto> geographicalGranularityDtos) {
        // Clear previous values
        clearValue();
        // Set granularities
        LinkedHashMap<String, String> valueMap = CommonUtils.getGeographicalGranularituesValueMap(geographicalGranularityDtos);
        form.getItem(GEOGRAPHICAL_GRANULARITY_SELECTION).setValueMap(valueMap);
    }

    public void setGeographicalValues(List<GeographicalValueDto> geographicalValueDtos) {
        clearSourceGeographicalValues();
        for (GeographicalValueDto geographicalValueDto : geographicalValueDtos) {
            GeographicalValueRecord record = new GeographicalValueRecord(geographicalValueDto);
            sourceList.addData(record);
        }
    }

    public List<GeographicalValueBaseDto> getSelectedValues() {
        List<GeographicalValueBaseDto> selectedGeographicalValues = new ArrayList<GeographicalValueBaseDto>();
        ListGridRecord records[] = targetList.getRecords();
        for (ListGridRecord record : records) {
            GeographicalValueRecord geographicalValueRecord = (GeographicalValueRecord) record;
            selectedGeographicalValues.add(geographicalValueRecord.getGeographicalValueDto());
        }
        return selectedGeographicalValues;
    }

    public SelectItem getGranularitySelectItem() {
        return (SelectItem) form.getItem(GEOGRAPHICAL_GRANULARITY_SELECTION);
    }

    public void clearGeographicalValues() {
        clearSourceGeographicalValues();
        targetList.selectAllRecords();
        targetList.removeSelectedData();
    }

    public void clearSourceGeographicalValues() {
        sourceList.selectAllRecords();
        sourceList.removeSelectedData();
    }

    @Override
    public void clearValue() {
        ((SelectItem) form.getItem(GEOGRAPHICAL_GRANULARITY_SELECTION)).setValue(new String());
        ((SelectItem) form.getItem(GEOGRAPHICAL_GRANULARITY_SELECTION)).setValueMap(new LinkedHashMap<String, String>());
        clearGeographicalValues();
    }

    private void addNonDuplicatedRecordToTarget(Record record) {
        String code = record.getAttribute(GeographicalValueRecord.CODE);
        if (targetList.getRecordList().find(GeographicalValueRecord.CODE, code) == null) {
            targetList.addData(record);
        }
    }
}
