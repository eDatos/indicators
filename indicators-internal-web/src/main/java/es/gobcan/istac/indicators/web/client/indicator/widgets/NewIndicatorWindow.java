package es.gobcan.istac.indicators.web.client.indicator.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import java.util.LinkedHashMap;
import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;

import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.QuantityDto;
import es.gobcan.istac.indicators.core.dto.SubjectDto;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorListUiHandler;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS;
import es.gobcan.istac.indicators.web.client.utils.CommonUtils;
import es.gobcan.istac.indicators.web.client.widgets.CategoryElementSelectItem;

public class NewIndicatorWindow extends CustomWindow {

    private static final String       VIEW_IDENTIFIER_PREFIX = "DV_";
    private static final String       FIELD_SAVE             = "save-ind";
    private static final int          FORM_ITEM_WIDTH        = 300;

    private CustomDynamicForm         form;

    private List<SubjectDto>          subjectDtos;

    private CategoryElementSelectItem categoryElementSelectItem;

    public NewIndicatorWindow(String title) {
        super(title);
        setHeight(250);
        setWidth(450);

        form = new CustomDynamicForm();

        RequiredTextItem codeItem = new RequiredTextItem(IndicatorDS.CODE, getConstants().indicDetailIdentifier());
        codeItem.setValidators(CommonWebUtils.getSemanticIdentifierCustomValidator());
        codeItem.setWidth(FORM_ITEM_WIDTH);
        RequiredTextItem viewItem = new RequiredTextItem(IndicatorDS.VIEW_CODE, getConstants().indicDetailViewIdentifier());
        viewItem.setLength(27);
        viewItem.setValidators(CommonWebUtils.getSemanticIdentifierCustomValidator());
        viewItem.setWidth(FORM_ITEM_WIDTH);
        RequiredTextItem titleItem = new RequiredTextItem(IndicatorDS.TITLE, getConstants().indicDetailTitle());
        titleItem.setWidth(FORM_ITEM_WIDTH);
        RequiredSelectItem subjectItem = new RequiredSelectItem(IndicatorDS.SUBJECT, getConstants().indicDetailSubject());
        subjectItem.setWidth(FORM_ITEM_WIDTH);

        categoryElementSelectItem = new CategoryElementSelectItem(form, true, FORM_ITEM_WIDTH);

        ButtonItem saveItem = new ButtonItem(FIELD_SAVE, MetamacWebCommon.getConstants().actionSave());
        saveItem.setColSpan(2);
        saveItem.setAlign(Alignment.CENTER);

        form.setFields(codeItem, viewItem, titleItem, subjectItem, categoryElementSelectItem.getItem(), saveItem);

        addItem(form);
        show();
    }

    public HasClickHandlers getSave() {
        return form.getItem(FIELD_SAVE);
    }

    public IndicatorDto getNewIndicatorDto() {
        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode(form.getValueAsString(IndicatorDS.CODE));
        indicatorDto.setViewCode(VIEW_IDENTIFIER_PREFIX + form.getValueAsString(IndicatorDS.VIEW_CODE));
        indicatorDto.setTitle(InternationalStringUtils.updateInternationalString(new InternationalStringDto(), form.getValueAsString(IndicatorDS.TITLE)));
        indicatorDto.setSubjectCode(form.getValueAsString(IndicatorDS.SUBJECT));
        indicatorDto.setCategoryElement(categoryElementSelectItem.getValue());

        indicatorDto.setSubjectTitle(CommonUtils.getSubjectTitleFromCode(subjectDtos, form.getValueAsString(IndicatorDS.SUBJECT)));
        indicatorDto.setQuantity(new QuantityDto()); // Set always an empty Quantity (required by service)
        return indicatorDto;
    }

    public boolean validateForm() {
        return form.validate();
    }

    public void setSubjetcs(List<SubjectDto> subjectDtos) {
        this.subjectDtos = subjectDtos;
        LinkedHashMap<String, String> valueMap = CommonUtils.getSubjectsValueMap(subjectDtos);
        ((SelectItem) form.getItem(IndicatorDS.SUBJECT)).setValueMap(valueMap);
    }

    public void setUiHandlers(IndicatorListUiHandler uiHandlers) {
        categoryElementSelectItem.setUiHandlers(uiHandlers);
    }

    public void setCategoryElementExternalItem(List<ExternalItemDto> categoryElementExternalItem, int firstResult, int totalResults) {
        categoryElementSelectItem.setCategoryElementExternalItem(categoryElementExternalItem, firstResult, totalResults);
    }
}
