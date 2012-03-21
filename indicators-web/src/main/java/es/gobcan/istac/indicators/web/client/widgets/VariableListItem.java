package es.gobcan.istac.indicators.web.client.widgets;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCanvasItem;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.EditorValueMapFunction;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

import es.gobcan.istac.indicators.core.dto.serviceapi.DataSourceVariableDto;
import es.gobcan.istac.indicators.web.client.model.DataSourceVariableRecord;
import es.gobcan.istac.indicators.web.client.model.ds.DataSourceDS;
import es.gobcan.istac.indicators.web.client.utils.CommonUtils;


public class VariableListItem extends CustomCanvasItem {

    protected ListGrid variableList;
    
    protected boolean required;
    
    private List<DataSourceVariableDto> dataSourceVariableDtos;
    
    private Map<String, LinkedHashMap<String, String>> variableValueMaps = new java.util.HashMap<String, LinkedHashMap<String, String>>();
    
    
    public VariableListItem(String name, String title) {
        super(name, title);

        setCellStyle("dragAndDropCellStyle");
        
        variableList = new ListGrid();
        variableList.setShowHeader(false);
        variableList.setLeaveScrollbarGap(false);
        variableList.setAlternateRecordStyles(false);
        variableList.setWidth(400);
        variableList.setHeight(200);
        variableList.setAlwaysShowEditors(true);
        variableList.addRecordClickHandler(new RecordClickHandler() {
            @Override
            public void onRecordClick(RecordClickEvent event) {
                variableList.deselectAllRecords();
                variableList.selectRecord(event.getRecord());
            }
        });
        
        ListGridField variableField = new ListGridField(DataSourceDS.VARIABLE_NAME, "variable");
        variableField.setCanEdit(false);
        
        ListGridField categoryField = new ListGridField(DataSourceDS.VARIABLE_CATEGORY, "category");
        categoryField.setCanEdit(true);
        categoryField.setEditorType(new SelectItem());
        
        EditorValueMapFunction editorValueMapFunction = new EditorValueMapFunction() {
            @SuppressWarnings("rawtypes")
            @Override
            public Map getEditorValueMap(Map values, ListGridField field, ListGrid grid) {
                if (values.containsKey(DataSourceDS.VARIABLE_NAME) && values.get(DataSourceDS.VARIABLE_NAME) != null) {
                    String variableName = values.get(DataSourceDS.VARIABLE_NAME).toString();
                    if (variableValueMaps.containsKey(variableName)) {
                        return variableValueMaps.get(variableName);
                    }
                }
                LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
                return valueMap;
            }
        };
        
        categoryField.setEditorValueMapFunction(editorValueMapFunction);
        
        variableList.setFields(variableField, categoryField);
        
        HLayout hLayout = new HLayout(1);
        hLayout.addMember(variableList);
        
        VLayout vLayout = new VLayout();
        vLayout.addMember(hLayout);
        
        setCanvas(vLayout);
    }

    public void setRequired(boolean required) {
        this.required = required;
        super.setRequired(required);
        setTitleStyle("requiredFormLabel");
    }
    
    public void clearValue() {
        variableList.selectAllRecords();
        variableList.removeSelectedData();
    }
    
    @Override
    public Boolean validate() {
        // TODO How can i validate this?
        return true;
    }
    
    public void addVariableAndCategories(String variable, List<String> categoryCodes, List<String> categoryLabels) {
        variableValueMaps.put(variable, CommonUtils.getVariableCategoriesValueMap(categoryCodes, categoryLabels));
        
        DataSourceVariableRecord record = new DataSourceVariableRecord(variable, new String(), null);
        variableList.addData(record);
    }
    
    public void setValue(List<DataSourceVariableDto> dataSourceVariableDtos) {
        this.dataSourceVariableDtos = dataSourceVariableDtos;
//        for (DataSourceVariableDto dataSourceVariableDto : dataSourceVariableDtos) {
//            DataSourceVariableRecord record = RecordUtils.getDataSourceVariableRecord(dataSourceVariableDto);
//            variableList.addData(record);
//        }
    }
    
    public List<DataSourceVariableDto> getValue() {
        return null;
    }
    
}
