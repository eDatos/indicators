package es.gobcan.istac.indicators.web.client.widgets;

import java.util.List;

import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCanvasItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.layout.VLayout;

import es.gobcan.istac.indicators.core.dto.DataSourceVariableDto;
import es.gobcan.istac.indicators.core.dto.DataStructureDto;

public class ViewVariableCanvasItem extends CustomCanvasItem {

    private CustomDynamicForm form;

    protected boolean         required;

    public ViewVariableCanvasItem(String name, String title) {
        super(name, title);

        setTitleStyle("staticFormItemTitle");

        setCellStyle("variableCanvasCell");
        setCellHeight(30);
        setTextBoxStyle("variableCanvasCell");
        setWidth(650);

        form = new CustomDynamicForm();
        form.setColWidths("20%", "80%");

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
        // Set an empty hidden item to remove fields from the form.
        ViewTextItem item = new ViewTextItem();
        item.setVisible(false);
        form.setFields(item);
    }

    @Override
    public Boolean validate() {
        // Not required (at least for now)
        return true;
    }

    public void setVariablesAndCategories(List<DataSourceVariableDto> dataSourceVariableDtos, DataStructureDto dataStructureDto) {
        form.clearValues();
        if (dataSourceVariableDtos != null) {
            FormItem[] formItems = new FormItem[dataSourceVariableDtos.size()];
            for (int i = 0; i < dataSourceVariableDtos.size(); i++) {
                ViewTextItem item = new ViewTextItem("variable" + i, dataSourceVariableDtos.get(i).getVariable());
                List<String> codes = dataStructureDto.getValueCodes().get(dataSourceVariableDtos.get(i).getVariable());
                List<String> labels = dataStructureDto.getValueLabels().get(dataSourceVariableDtos.get(i).getVariable());
                if (codes != null && labels != null) {
                    for (int j = 0; j < codes.size(); j++) {
                        if (codes.get(j).equals(dataSourceVariableDtos.get(i).getCategory())) {
                            item.setValue(dataSourceVariableDtos.get(i).getCategory() + " - " + labels.get(j));
                            break;
                        }
                    }
                }
                formItems[i] = item;
            }
            form.setFields(formItems);
            form.markForRedraw();
        }
    }
}
