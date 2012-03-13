package es.gobcan.istac.indicators.web.client.indicator.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import java.util.LinkedHashMap;
import java.util.List;

import org.siemac.metamac.core.common.dto.serviceapi.InternationalStringDto;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.QuantityDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.SubjectDto;
import es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS;
import es.gobcan.istac.indicators.web.client.utils.CommonUtils;


public class NewIndicatorWindow extends CustomWindow {

    private static final String FIELD_SAVE = "save-ind";
    
    private CustomDynamicForm form;
    
    private List<SubjectDto> subjectDtos;
    
    
    public NewIndicatorWindow(String title) {
        super(title);
        setHeight(150);
        setWidth(330);
        
        RequiredTextItem codeItem = new RequiredTextItem(IndicatorDS.CODE, getConstants().indicDetailIdentifier());
        RequiredTextItem titleItem = new RequiredTextItem(IndicatorDS.TITLE, getConstants().indicDetailTitle());
        RequiredSelectItem subjectItem = new RequiredSelectItem(IndicatorDS.SUBJECT, getConstants().indicDetailSubject());
        ButtonItem saveItem = new ButtonItem(FIELD_SAVE, MetamacWebCommon.getConstants().actionSave());
        saveItem.setAlign(Alignment.RIGHT);
        
        form = new CustomDynamicForm();
        form.setFields(codeItem, titleItem, subjectItem, saveItem);
        
        addItem(form);
        show();
    }
    
    public HasClickHandlers getSave() {
        return form.getItem(FIELD_SAVE);
    }
    
    public IndicatorDto getNewIndicatorDto() {
        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode(form.getValueAsString(IndicatorDS.CODE));
        indicatorDto.setTitle(InternationalStringUtils.updateInternationalString(new InternationalStringDto(), form.getValueAsString(IndicatorDS.TITLE)));
        indicatorDto.setSubjectCode(form.getValueAsString(IndicatorDS.SUBJECT));
        indicatorDto.setSubjectTitle(CommonUtils.getSubjectTitleFromCode(subjectDtos, form.getValueAsString(IndicatorDS.SUBJECT)));
        // TODO Do no set quantity (must be not required)
        QuantityDto quantity = new QuantityDto();
        quantity.setType(QuantityTypeEnum.QUANTITY);
        quantity.setUnitUuid("1");
        indicatorDto.setQuantity(quantity);
        return indicatorDto;
    }
    
    public boolean validateForm() {
        return form.validate();
    }
    
    public void setSubjetcs(List<SubjectDto> subjectDtos) {
        this.subjectDtos = subjectDtos;
        LinkedHashMap<String, String> valueMap = CommonUtils.getSubjectsValueMap(subjectDtos);
        ((SelectItem)form.getItem(IndicatorDS.SUBJECT)).setValueMap(valueMap);
    }
    
}
