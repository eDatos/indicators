package es.gobcan.istac.indicators.web.client.system.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;

import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS;

public class NewIndicatorsSystemWindow extends CustomWindow {

    private static final String FIELD_SAVE      = "save-indsys";
    private static final int    FORM_ITEM_WIDTH = 300;

    private CustomDynamicForm   form;

    public NewIndicatorsSystemWindow(String title) {
        super(title);
        setHeight(150);
        setWidth(450);

        form = new CustomDynamicForm();

        RequiredTextItem codeItem = new RequiredTextItem(IndicatorDS.CODE, getConstants().indicDetailIdentifier());
        codeItem.setValidators(CommonWebUtils.getSemanticIdentifierCustomValidator());
        codeItem.setWidth(FORM_ITEM_WIDTH);
        RequiredTextItem titleItem = new RequiredTextItem(IndicatorDS.TITLE, getConstants().indicDetailTitle());
        titleItem.setWidth(FORM_ITEM_WIDTH);

        ButtonItem saveItem = new ButtonItem(FIELD_SAVE, MetamacWebCommon.getConstants().actionSave());
        saveItem.setColSpan(2);
        saveItem.setAlign(Alignment.CENTER);

        form.setFields(codeItem, titleItem, saveItem);

        addItem(form);
        show();
    }

    public HasClickHandlers getSave() {
        return form.getItem(FIELD_SAVE);
    }

    public IndicatorsSystemDto getNewIndicatorsSystemDto() {
        IndicatorsSystemDto indicatorsSystemDto = new IndicatorsSystemDto();
        indicatorsSystemDto.setCode(form.getValueAsString(IndicatorDS.CODE));
        indicatorsSystemDto.setTitle(InternationalStringUtils.updateInternationalString(new InternationalStringDto(), form.getValueAsString(IndicatorDS.TITLE)));
        indicatorsSystemDto.setIsOperational(false);
        return indicatorsSystemDto;
    }

    public boolean validateForm() {
        return form.validate();
    }
}
