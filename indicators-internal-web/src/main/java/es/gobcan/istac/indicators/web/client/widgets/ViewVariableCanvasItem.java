package es.gobcan.istac.indicators.web.client.widgets;

import java.util.List;

import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCanvasItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.layout.VLayout;

import es.gobcan.istac.indicators.core.dto.DataSourceVariableDto;

public class ViewVariableCanvasItem extends CustomCanvasItem {

    private CustomDynamicForm form;

    protected boolean         required;

    public ViewVariableCanvasItem(String name, String title) {
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

    public void setValue(List<DataSourceVariableDto> dataSourceVariableDtos) {
        if (dataSourceVariableDtos != null) {
            FormItem[] formItems = new FormItem[dataSourceVariableDtos.size()];
            for (int i = 0; i < dataSourceVariableDtos.size(); i++) {
                ViewTextItem item = new ViewTextItem("variable" + i, dataSourceVariableDtos.get(i).getVariable());
                item.setValue(dataSourceVariableDtos.get(i).getCategory());
                formItems[i] = item;
            }
            form.setFields(formItems);
            form.markForRedraw();
        }
    }

}
