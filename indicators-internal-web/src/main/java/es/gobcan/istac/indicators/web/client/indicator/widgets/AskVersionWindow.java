package es.gobcan.istac.indicators.web.client.indicator.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredSelectItem;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;

import es.gobcan.istac.indicators.core.enume.domain.VersionTypeEnum;
import es.gobcan.istac.indicators.web.client.utils.CommonUtils;

public class AskVersionWindow extends CustomWindow {

    private static final String FIELD_VERSION = "version-ind";
    private static final String FIELD_SAVE    = "save-ind";

    private CustomDynamicForm   form;

    public AskVersionWindow(String title) {
        super(title);
        setHeight(100);
        setWidth(330);

        RequiredSelectItem versionItem = new RequiredSelectItem(FIELD_VERSION, getConstants().indicDetailVersion());
        versionItem.setValueMap(CommonUtils.getVersionTypeValueMap());

        ButtonItem saveItem = new ButtonItem(FIELD_SAVE, MetamacWebCommon.getConstants().actionSave());
        saveItem.setAlign(Alignment.RIGHT);

        form = new CustomDynamicForm();
        form.setFields(versionItem, saveItem);

        addItem(form);
        show();
    }

    public VersionTypeEnum getSelectedVersion() {
        String value = form.getValueAsString(FIELD_VERSION);
        return (value != null && !value.isEmpty()) ? VersionTypeEnum.valueOf(value) : null;
    }

    public boolean validateForm() {
        return form.validate();
    }

    public HasClickHandlers getSave() {
        return form.getItem(FIELD_SAVE);
    }

}
