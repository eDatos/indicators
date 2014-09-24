package es.gobcan.istac.indicators.web.client.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import org.siemac.metamac.core.common.criteria.shared.MetamacCriteriaOrder.OrderTypeEnum;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.widgets.BaseAdvancedSearchSectionStack;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

import es.gobcan.istac.indicators.web.client.IndicatorsValues;
import es.gobcan.istac.indicators.web.client.admin.view.handlers.AdminGeoValuesUiHandlers;
import es.gobcan.istac.indicators.web.client.enums.MultipleGeographicalValueOrderTypeEnum;
import es.gobcan.istac.indicators.web.client.model.ds.GeoValueDS;
import es.gobcan.istac.indicators.web.client.utils.ClientCriteriaUtils;
import es.gobcan.istac.indicators.web.client.utils.CommonUtils;
import es.gobcan.istac.indicators.web.shared.criteria.GeoValueCriteria;

public class GeographicalValuesSearchSectionStack extends BaseAdvancedSearchSectionStack {

    private AdminGeoValuesUiHandlers uiHandlers;

    public GeographicalValuesSearchSectionStack() {
        searchForm.getItem(SEARCH_ITEM_NAME).setWidth(180);
    }

    @Override
    protected void createAdvancedSearchForm() {
        advancedSearchForm = new GroupDynamicForm(StringUtils.EMPTY);
        advancedSearchForm.setPadding(5);
        advancedSearchForm.setMargin(5);
        advancedSearchForm.setVisible(false);

        SelectItem granularity = new SelectItem(GeoValueDS.GRANULARITY, getConstants().geoValueGranularity());
        granularity.setValueMap(CommonUtils.getGeographicalGranularituesValueMap(IndicatorsValues.getGeographicalGranularities()));

        SelectItem orderBy = new SelectItem(GeoValueDS.ORDER_BY, getConstants().orderBy());
        orderBy.setValueMap(CommonUtils.getMultipleGeographicalValueOrderValueMap());
        orderBy.setStartRow(true);
        orderBy.addChangedHandler(FormItemUtils.getMarkForRedrawChangedHandler(advancedSearchForm));

        SelectItem orderType = new SelectItem(GeoValueDS.ORDER_TYPE, getConstants().orderType());
        orderType.setValueMap(CommonUtils.getOrderTypeValueMap());
        orderType.setStartRow(true);
        orderType.setDefaultValue(OrderTypeEnum.ASC.name());
        orderType.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !StringUtils.isBlank(form.getValueAsString(GeoValueDS.ORDER_BY));
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

        FormItem[] advancedSearchFormItems = new FormItem[]{granularity, orderBy, orderType, searchItem};
        setFormItemsInAdvancedSearchForm(advancedSearchFormItems);
    }

    @Override
    protected void retrieveResources() {
        getUiHandlers().retrieveGeoValues(getGeoValueCriteria());
    }

    public GeoValueCriteria getGeoValueCriteria() {
        GeoValueCriteria criteria = new GeoValueCriteria();
        criteria.setCriteria(searchForm.getValueAsString(SEARCH_ITEM_NAME));
        criteria.setGranularityCode(advancedSearchForm.getValueAsString(GeoValueDS.GRANULARITY));

        MultipleGeographicalValueOrderTypeEnum geoValueCriteriaOrderEnum = CommonUtils.getMultipleGeographicalValueOrderTypeEnum(advancedSearchForm.getValueAsString(GeoValueDS.ORDER_BY));
        if (geoValueCriteriaOrderEnum != null) {
            OrderTypeEnum orderTypeEnum = CommonUtils.getOrderTypeEnum(advancedSearchForm.getValueAsString(GeoValueDS.ORDER_TYPE));
            criteria.setOrders(ClientCriteriaUtils.buildGeographicalValueCriteriaOrder(orderTypeEnum, geoValueCriteriaOrderEnum));
        }

        return criteria;
    }

    public void setUiHandlers(AdminGeoValuesUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public AdminGeoValuesUiHandlers getUiHandlers() {
        return uiHandlers;
    }
}
