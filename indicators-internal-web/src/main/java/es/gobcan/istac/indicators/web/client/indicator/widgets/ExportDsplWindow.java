package es.gobcan.istac.indicators.web.client.indicator.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;

import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

public abstract class ExportDsplWindow extends CustomWindow {

    private static final String FIELD_EXPORT_SIMPLE = "export-simple";
    private static final String FIELD_EXPORT_MERGE  = "export-merge";

    public ExportDsplWindow(String title) {
        super(title);
        setHeight(330);
        setWidth(330);

        Label label1 = new Label();
        label1.setMargin(15);
        label1.setContents(getConstants().exportDsplSimple());

        Label label2 = new Label();
        label2.setMargin(15);
        label2.setContents(getConstants().exportDsplMerging());

        CustomButtonItem exportSimpleItem = new CustomButtonItem(FIELD_EXPORT_SIMPLE, getConstants().actionExport());

        CustomButtonItem exportMerge = new CustomButtonItem(FIELD_EXPORT_MERGE, getConstants().actionExport());

        DynamicForm formSimple = new DynamicForm();
        formSimple.setFields(exportSimpleItem);

        DynamicForm formMerging = new DynamicForm();
        formMerging.setFields(exportMerge);

        addItem(label1);
        addItem(formSimple);
        addItem(label2);
        addItem(formMerging);
        show();

        exportSimpleItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                exportSimple();
            }
        });

        exportMerge.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                exportMerging();
            }
        });
    }

    public abstract void exportSimple();
    public abstract void exportMerging();
}
