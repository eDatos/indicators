package es.gobcan.istac.indicators.web.client.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCanvasItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.layout.VLayout;

import es.gobcan.istac.indicators.core.dto.DataSourceVariableDto;
import es.gobcan.istac.indicators.core.dto.DataStructureDto;
import es.gobcan.istac.indicators.web.client.utils.CommonUtils;

public class VariableCanvasItem extends CustomCanvasItem {

    // private final static Integer SELECT_ITEM_WIDTH = FormItemUtils.FORM_ITEM_WIDTH - 64;
    private final static String SELECT_ITEM_WIDTH  = FormItemUtils.FORM_ITEM_WIDTH;

    private Logger              logger             = Logger.getLogger(VariableCanvasItem.class.getName());

    private CustomDynamicForm   form;

    protected boolean           required;

    private Map<String, String> variablesItemNames = new HashMap<String, String>();

    public VariableCanvasItem(String name, String title) {
        super(name, title);

        setTitleStyle("staticFormItemTitle");
        setCellStyle("variableCanvasCell");
        setCellHeight(30);
        setTextBoxStyle("variableCanvasCell");

        form = new CustomDynamicForm();
        form.setWidth(270);

        VLayout vLayout = new VLayout();
        vLayout.addMember(form);

        setCanvas(vLayout);
    }

    public void setRequired(boolean required) {
        this.required = required;
        super.setRequired(required);
        setTitleStyle("requiredFormLabel");
    }

    @Override
    public void clearValue() {
        form.clearValues();
        // Set an empty hidden item to remove fields from the form.
        ViewTextItem item = new ViewTextItem();
        item.setVisible(false);
        form.setFields(item);
    }

    @Override
    public Boolean validate() {
        return form.validate(false);
    }

    public void setVariablesAndCategories(DataStructureDto dataStructureDto, String... excludeVars) {
        variablesItemNames.clear();
        List<String> variables = dataStructureDto.getVariables();
        Map<String, List<String>> categoryCodes = dataStructureDto.getValueCodes();
        Map<String, List<String>> categoryLabels = dataStructureDto.getValueLabels();
        if (variables != null) {
            List<FormItem> formItems = new ArrayList<FormItem>();
            for (int i = 0; i < variables.size(); i++) {
                String var = variables.get(i);
                if (includeVariable(dataStructureDto, var)) {
                    if (categoryCodes.containsKey(var) && categoryLabels.containsKey(var)) {
                        SelectItem selectItem = new SelectItem("variable-" + i, var);
                        selectItem.setRequired(true);
                        selectItem.setWidth(SELECT_ITEM_WIDTH);
                        selectItem.setValueMap(CommonUtils.getVariableCategoriesValueMap(categoryCodes.get(var), categoryLabels.get(var)));
                        formItems.add(selectItem);
                        variablesItemNames.put(var, selectItem.getName());
                    } else {
                        logger.log(Level.SEVERE, "Something wrong with variables and its category labels and codes");
                    }
                }
            }
            if (!formItems.isEmpty()) {
                form.setFields(formItems.toArray(new FormItem[formItems.size()]));
                form.markForRedraw();
            }
        }
    }

    public void restartVisibility(String... hiddenVars) {
        for (FormItem item : form.getFields()) {
            if (!item.isVisible()) {
                item.show();
            }
        }
        for (String var : hiddenVars) {
            String name = variablesItemNames.get(var);
            if (name != null) {
                form.getItem(name).hide();
            }
        }
    }

    @Override
    public List<DataSourceVariableDto> getValue() {
        List<DataSourceVariableDto> dataSourceVariableDtos = new ArrayList<DataSourceVariableDto>();
        FormItem[] formItems = form.getFields();
        for (int i = 0; i < formItems.length; i++) {
            if (formItems[i] != null && formItems[i].isVisible()) {
                String name = formItems[i].getTitle();
                String category = formItems[i].getValue() != null ? (String) formItems[i].getValue() : null;
                if (!StringUtils.isBlank(name) && !StringUtils.isBlank(category)) {
                    DataSourceVariableDto dataSourceVariableDto = new DataSourceVariableDto();
                    dataSourceVariableDto.setVariable(name);
                    dataSourceVariableDto.setCategory(category);
                    dataSourceVariableDtos.add(dataSourceVariableDto);
                }
            }
        }
        return dataSourceVariableDtos;
    }

    /**
     * The variable can be included if is not the temporal, geographical or measure (contVariable) one
     * 
     * @param var
     * @param dataStructureDto
     * @return
     */
    private boolean includeVariable(DataStructureDto dataStructureDto, String var) {
        String temporalVariable = dataStructureDto.getTemporalVariable();
        // String geographicalVariable = dataStructureDto.getSpatialVariables();
        // FIXME:
        String measureVariable = dataStructureDto.getContVariable();
        if (var.equals(temporalVariable) || /* var.equals(geographicalVariable) || */var.equals(measureVariable)) {
            return false;
        }
        return true;
    }
}
