package es.gobcan.istac.indicators.web.client.widgets;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCanvasItem;

import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.grid.EditorValueMapFunction;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditorEnterEvent;
import com.smartgwt.client.widgets.grid.events.EditorEnterHandler;
import com.smartgwt.client.widgets.grid.events.EditorExitEvent;
import com.smartgwt.client.widgets.grid.events.EditorExitHandler;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

import es.gobcan.istac.indicators.core.dto.DataSourceVariableDto;
import es.gobcan.istac.indicators.web.client.model.DataSourceVariableRecord;
import es.gobcan.istac.indicators.web.client.model.ds.DataSourceDS;
import es.gobcan.istac.indicators.web.client.utils.CommonUtils;

public class VariableListItem extends CustomCanvasItem {

    private Logger                                     logger            = Logger.getLogger(VariableListItem.class.getName());

    protected ListGrid                                 variableList;

    protected boolean                                  required;

    // private List<DataSourceVariableDto> dataSourceVariableDtos = new ArrayList<DataSourceVariableDto>();

    private Map<String, LinkedHashMap<String, String>> variableValueMaps = new java.util.HashMap<String, LinkedHashMap<String, String>>();

    public VariableListItem(String name, String title) {
        super(name, title);

        setCellStyle("dragAndDropCellStyle");

        variableList = new ListGrid();
        variableList.setShowHeader(false);
        variableList.setLeaveScrollbarGap(false);
        variableList.setAlternateRecordStyles(false);
        variableList.setWidth(300);
        variableList.setHeight(150);
        variableList.setAlwaysShowEditors(true);
        variableList.setAutoSaveEdits(true);
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

        final SelectItem selectItem = new SelectItem();
        selectItem.addChangedHandler(new com.smartgwt.client.widgets.form.fields.events.ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                selectItem.setValue(event.getValue());

            }
        });

        categoryField.setEditorType(selectItem);

        categoryField.addEditorEnterHandler(new EditorEnterHandler() {

            @Override
            public void onEditorEnter(EditorEnterEvent event) {
            }
        });
        categoryField.addEditorExitHandler(new EditorExitHandler() {

            @Override
            public void onEditorExit(EditorExitEvent event) {

            }
        });

        // Dynamic valueMap. Its values depends on the record.
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
                // This should not happen
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
        // Not required (at least for now)
        return true;
    }

    public void setVariablesAndCategories(List<String> variables, Map<String, List<String>> categoryCodes, Map<String, List<String>> categoryLabels) {
        for (String var : variables) {
            if (categoryCodes.containsKey(var) && categoryLabels.containsKey(var)) {
                addVariableAndCategories(var, categoryCodes.get(var), categoryLabels.get(var));

            } else {
                logger.log(Level.SEVERE, "Something wrong with variables and its category labels and codes");
            }
        }
    }

    public void addVariableAndCategories(String variable, List<String> categoryCodes, List<String> categoryLabels) {
        variableValueMaps.put(variable, CommonUtils.getVariableCategoriesValueMap(categoryCodes, categoryLabels));

        DataSourceVariableRecord record = new DataSourceVariableRecord(variable, new String(), null);
        variableList.addData(record);
    }

    public void setValue(List<DataSourceVariableDto> dataSourceVariableDtos) {
        // this.dataSourceVariableDtos = dataSourceVariableDtos;
        // Value map must be already set
        // In this method we should set the value selected of this valueMap

        // ??? for (DataSourceVariableDto dataSourceVariableDto : dataSourceVariableDtos) {
        // ??? DataSourceVariableRecord record = RecordUtils.getDataSourceVariableRecord(dataSourceVariableDto);
        // ??? variableList.addData(record);
        // ??? }
    }

    public List<DataSourceVariableDto> getValue() {
        List<DataSourceVariableDto> dataSourceVariableDtos = new ArrayList<DataSourceVariableDto>();
        ListGridRecord[] records = variableList.getRecords();
        if (records != null) {
            for (int i = 0; i < records.length; i++) {
                if (records[i] instanceof DataSourceVariableRecord) {
                    String name = ((DataSourceVariableRecord) records[i]).getVariableName();
                    String category = ((DataSourceVariableRecord) records[i]).getVariableCategory();
                    if (!StringUtils.isBlank(name) && !StringUtils.isBlank(category)) {
                        DataSourceVariableDto dataSourceVariableDto = new DataSourceVariableDto();
                        dataSourceVariableDto.setVariable(name);
                        dataSourceVariableDto.setCategory(category);
                        dataSourceVariableDtos.add(dataSourceVariableDto);
                    }
                }
            }
        }
        return dataSourceVariableDtos;
    }

}
