package es.gobcan.istac.indicators.web.client.indicator.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;

import es.gobcan.istac.indicators.web.client.model.ds.DataSourceDS;

public class JsonStatSearchWindow extends CustomWindow {

    private static final String FIELD_SAVE = "save-ind";

    private CustomDynamicForm   form;

    public JsonStatSearchWindow(String title) {
        super(title);
        setHeight(150);
        setWidth(780);

        RequiredTextItem jsonStatItem = new RequiredTextItem(DataSourceDS.QUERY_UUID, getConstants().dataSourceQuerySelection());
        jsonStatItem.setValidators(CommonWebUtils.getUrlValidator());
        jsonStatItem.setWidth(690);

        ButtonItem saveItem = new ButtonItem(FIELD_SAVE, MetamacWebCommon.getConstants().actionSave());
        saveItem.setColSpan(2);
        saveItem.setAlign(Alignment.CENTER);

        form = new CustomDynamicForm();
        form.setFields(jsonStatItem, saveItem);

        addItem(form);
        show();
    }

    public HasClickHandlers getSave() {
        return form.getItem(FIELD_SAVE);
    }

    public boolean validateForm() {
        return form.validate();
    }

    public String getJsonStatUrl() {
        return form.getValueAsString(DataSourceDS.QUERY_UUID);
    }

}
