package es.gobcan.istac.indicators.web.client.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.widgets.BaseAdvancedSearchSectionStack;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;

import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

import es.gobcan.istac.indicators.web.client.model.ds.IndicatorsSystemsDS;
import es.gobcan.istac.indicators.web.client.system.presenter.SystemListUiHandler;
import es.gobcan.istac.indicators.web.client.utils.CommonUtils;
import es.gobcan.istac.indicators.web.shared.criteria.IndicatorsSystemCriteria;

public class IndicatorsSystemSearchSectionStack extends BaseAdvancedSearchSectionStack {

    private SystemListUiHandler uiHandlers;

    public IndicatorsSystemSearchSectionStack() {
    }

    @Override
    protected void createAdvancedSearchForm() {
        advancedSearchForm = new GroupDynamicForm(StringUtils.EMPTY);
        advancedSearchForm.setPadding(5);
        advancedSearchForm.setMargin(5);
        advancedSearchForm.setVisible(false);

        CustomButtonItem searchItem = new CustomButtonItem(ADVANCED_SEARCH_ITEM_NAME, MetamacWebCommon.getConstants().search());
        searchItem.setColSpan(4);
        searchItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                retrieveResources();
            }
        });

        SelectItem operational = new SelectItem(IndicatorsSystemsDS.OPERATIONAL, getConstants().operational());
        operational.setValueMap(CommonUtils.getYesOrNoValueMap());

        FormItem[] advancedSearchFormItems = new FormItem[]{operational, searchItem};
        setFormItemsInAdvancedSearchForm(advancedSearchFormItems);
    }

    @Override
    protected void retrieveResources() {
        getUiHandlers().retrieveSystems(getIndicatorsSystemCriteria());
    }

    public void setUiHandlers(SystemListUiHandler uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public SystemListUiHandler getUiHandlers() {
        return uiHandlers;
    }

    public IndicatorsSystemCriteria getIndicatorsSystemCriteria() {
        IndicatorsSystemCriteria criteria = new IndicatorsSystemCriteria();

        criteria.setCriteria(searchForm.getValueAsString(SEARCH_ITEM_NAME));
        String isOperational = advancedSearchForm.getValueAsString(IndicatorsSystemsDS.OPERATIONAL);
        if (!StringUtils.isBlank(isOperational)) {
            criteria.setIsOperational(Boolean.valueOf(isOperational));
        }

        return criteria;
    }
}
