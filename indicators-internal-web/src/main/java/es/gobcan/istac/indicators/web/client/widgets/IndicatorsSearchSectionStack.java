package es.gobcan.istac.indicators.web.client.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import java.util.LinkedHashMap;
import java.util.List;

import org.siemac.metamac.core.common.criteria.shared.MetamacCriteriaOrder.OrderTypeEnum;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.widgets.BaseAdvancedSearchSectionStack;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCheckboxItem;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

import es.gobcan.istac.indicators.core.criteria.IndicatorCriteriaOrderEnum;
import es.gobcan.istac.indicators.core.dto.SubjectDto;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorListUiHandler;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS;
import es.gobcan.istac.indicators.web.client.utils.ClientCriteriaUtils;
import es.gobcan.istac.indicators.web.client.utils.CommonUtils;
import es.gobcan.istac.indicators.web.shared.criteria.IndicatorCriteria;

public class IndicatorsSearchSectionStack extends BaseAdvancedSearchSectionStack {

    private IndicatorListUiHandler uiHandlers;

    public IndicatorsSearchSectionStack() {
    }

    @Override
    protected void createAdvancedSearchForm() {
        advancedSearchForm = new GroupDynamicForm(StringUtils.EMPTY);
        advancedSearchForm.setPadding(5);
        advancedSearchForm.setMargin(5);
        advancedSearchForm.setVisible(false);

        TextItem title = new TextItem(IndicatorDS.TITLE, getConstants().indicDetailTitle());
        SelectItem subject = new SelectItem(IndicatorDS.SUBJECT, getConstants().indicDetailSubject());
        subject.setWidth(300);
        SelectItem productionVersionProcStatus = new SelectItem(IndicatorDS.PROC_STATUS, getConstants().indicatorProductionEnvironmentProcStatus());
        productionVersionProcStatus.setValueMap(CommonUtils.getProcStatusValueMap());
        SelectItem diffusionVersionProcStatus = new SelectItem(IndicatorDS.PROC_STATUS_DIFF, getConstants().indicatorDiffusionEnvironmentProcStatus());
        diffusionVersionProcStatus.setValueMap(CommonUtils.getProcStatusValueMap());

        SelectItem orderBy = new SelectItem(IndicatorDS.ORDER_BY, getConstants().orderBy());
        orderBy.setValueMap(CommonUtils.getIndicatorOrderValueMap());
        orderBy.setStartRow(true);
        orderBy.setWidth(200);
        orderBy.addChangedHandler(FormItemUtils.getMarkForRedrawChangedHandler(advancedSearchForm));

        SelectItem orderType = new SelectItem(IndicatorDS.ORDER_TYPE, getConstants().orderType());
        orderType.setValueMap(CommonUtils.getOrderTypeValueMap());
        orderType.setDefaultValue(OrderTypeEnum.ASC.name());
        orderType.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !StringUtils.isBlank(form.getValueAsString(IndicatorDS.ORDER_BY));
            }
        });

        CustomButtonItem searchItem = new CustomButtonItem(ADVANCED_SEARCH_ITEM_NAME, MetamacWebCommon.getConstants().search());
        searchItem.setColSpan(4);
        searchItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                retrieveResources();
            }
        });
        
        SelectItem notifyPopulationErrors = new SelectItem(IndicatorDS.NOTIFY_POPULATION_ERRORS, getConstants().indicDetailNotifyPopulationErrors());
        notifyPopulationErrors.setValueMap(CommonUtils.getIndicatorNotifyPopulationErrorsValueMap());
        notifyPopulationErrors.setWidth(200);

        FormItem[] advancedSearchFormItems = new FormItem[]{title, subject, productionVersionProcStatus, diffusionVersionProcStatus, notifyPopulationErrors, orderBy, orderType, searchItem};
        setFormItemsInAdvancedSearchForm(advancedSearchFormItems);
    }

    @Override
    protected void retrieveResources() {
        getUiHandlers().retrieveIndicators(getIndicatorCriteria());
    }

    @Override
    protected void showAdvancedSearchSection() {
        super.showAdvancedSearchSection();
        getUiHandlers().retrieveSubjectsListForSearchIndicator();
    }

    public IndicatorCriteria getIndicatorCriteria() {
        IndicatorCriteria criteria = new IndicatorCriteria();
        criteria.setCriteria(searchForm.getValueAsString(SEARCH_ITEM_NAME));
        criteria.setTitle(advancedSearchForm.getValueAsString(IndicatorDS.TITLE));
        criteria.setProductionVersionProcStatus(CommonUtils.getIndicatorProcStatusEnum(advancedSearchForm.getValueAsString(IndicatorDS.PROC_STATUS)));
        criteria.setDiffusionVersionProcStatus(CommonUtils.getIndicatorProcStatusEnum(advancedSearchForm.getValueAsString(IndicatorDS.PROC_STATUS_DIFF)));
        criteria.setSubjectCode(advancedSearchForm.getValueAsString(IndicatorDS.SUBJECT));
        
        String notifyPopulationErrors = advancedSearchForm.getValueAsString(IndicatorDS.NOTIFY_POPULATION_ERRORS);
        if (!StringUtils.isBlank(notifyPopulationErrors)) {
            criteria.setNotifyPopulationErrors(new Boolean(notifyPopulationErrors));
        }
        
        IndicatorCriteriaOrderEnum indicatorCriteriaOrderEnum = CommonUtils.getIndicatorCriteriaOrderEnum(advancedSearchForm.getValueAsString(IndicatorDS.ORDER_BY));
        if (indicatorCriteriaOrderEnum != null) {
            OrderTypeEnum orderTypeEnum = CommonUtils.getOrderTypeEnum(advancedSearchForm.getValueAsString(IndicatorDS.ORDER_TYPE));
            criteria.setOrders(ClientCriteriaUtils.buildCriteriaOrder(orderTypeEnum, indicatorCriteriaOrderEnum));
        }

        return criteria;
    }

    public void setSubjects(List<SubjectDto> subjectDtos) {
        LinkedHashMap<String, String> valueMap = CommonUtils.getSubjectsValueMap(subjectDtos);
        ((SelectItem) advancedSearchForm.getItem(IndicatorDS.SUBJECT)).setValueMap(valueMap);
    }

    public void setUiHandlers(IndicatorListUiHandler uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public IndicatorListUiHandler getUiHandlers() {
        return uiHandlers;
    }
}
