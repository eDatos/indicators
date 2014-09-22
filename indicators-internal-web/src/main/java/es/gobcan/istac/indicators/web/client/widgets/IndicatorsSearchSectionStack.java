package es.gobcan.istac.indicators.web.client.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.widgets.BaseAdvancedSearchSectionStack;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;

import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorListUiHandler;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS;
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
        SelectItem productionVersionProcStatus = new SelectItem(IndicatorDS.PROC_STATUS, getConstants().indicatorProductionEnvironmentProcStatus());
        productionVersionProcStatus.setValueMap(CommonUtils.getProcStatusValueMap());
        SelectItem diffusionVersionProcStatus = new SelectItem(IndicatorDS.PROC_STATUS_DIFF, getConstants().indicatorDiffusionEnvironmentProcStatus());
        diffusionVersionProcStatus.setValueMap(CommonUtils.getProcStatusValueMap());

        CustomButtonItem searchItem = new CustomButtonItem(ADVANCED_SEARCH_ITEM_NAME, MetamacWebCommon.getConstants().search());
        searchItem.setColSpan(4);
        searchItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                retrieveResources();
            }
        });

        FormItem[] advancedSearchFormItems = new FormItem[]{title, productionVersionProcStatus, diffusionVersionProcStatus, searchItem};
        setFormItemsInAdvancedSearchForm(advancedSearchFormItems);
    }

    @Override
    protected void retrieveResources() {
        getUiHandlers().retrieveIndicators(getIndicatorCriteria());
    }

    public IndicatorCriteria getIndicatorCriteria() {
        IndicatorCriteria criteria = new IndicatorCriteria();
        criteria.setCriteria(searchForm.getValueAsString(SEARCH_ITEM_NAME));
        criteria.setTitle(advancedSearchForm.getValueAsString(IndicatorDS.TITLE));
        criteria.setProductionVersionProcStatus(CommonUtils.getIndicatorProcStatusEnum(advancedSearchForm.getValueAsString(IndicatorDS.PROC_STATUS)));
        criteria.setDiffusionVersionProcStatus(CommonUtils.getIndicatorProcStatusEnum(advancedSearchForm.getValueAsString(IndicatorDS.PROC_STATUS_DIFF)));
        return criteria;
    }

    public void setUiHandlers(IndicatorListUiHandler uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public IndicatorListUiHandler getUiHandlers() {
        return uiHandlers;
    }
}
