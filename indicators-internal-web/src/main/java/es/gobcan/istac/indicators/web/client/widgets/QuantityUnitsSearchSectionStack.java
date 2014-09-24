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

import es.gobcan.istac.indicators.core.criteria.QuantityUnitCriteriaOrderEnum;
import es.gobcan.istac.indicators.web.client.admin.view.handlers.AdminQuantityUnitsUiHandlers;
import es.gobcan.istac.indicators.web.client.model.ds.QuantityUnitDS;
import es.gobcan.istac.indicators.web.client.utils.ClientCriteriaUtils;
import es.gobcan.istac.indicators.web.client.utils.CommonUtils;
import es.gobcan.istac.indicators.web.shared.criteria.QuantityUnitCriteria;

public class QuantityUnitsSearchSectionStack extends BaseAdvancedSearchSectionStack {

    private AdminQuantityUnitsUiHandlers uiHandlers;

    public QuantityUnitsSearchSectionStack() {
        searchForm.getItem(SEARCH_ITEM_NAME).setWidth(180);
    }

    @Override
    protected void createAdvancedSearchForm() {
        advancedSearchForm = new GroupDynamicForm(StringUtils.EMPTY);
        advancedSearchForm.setPadding(5);
        advancedSearchForm.setMargin(5);
        advancedSearchForm.setVisible(false);

        SelectItem orderBy = new SelectItem(QuantityUnitDS.ORDER_BY, getConstants().orderBy());
        orderBy.setValueMap(CommonUtils.getQuantityUnitOrderValueMap());
        orderBy.addChangedHandler(FormItemUtils.getMarkForRedrawChangedHandler(advancedSearchForm));

        SelectItem orderType = new SelectItem(QuantityUnitDS.ORDER_TYPE, getConstants().orderType());
        orderType.setValueMap(CommonUtils.getOrderTypeValueMap());
        orderType.setStartRow(true);
        orderType.setDefaultValue(OrderTypeEnum.ASC.name());
        orderType.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !StringUtils.isBlank(form.getValueAsString(QuantityUnitDS.ORDER_BY));
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

        FormItem[] advancedSearchFormItems = new FormItem[]{orderBy, orderType, searchItem};
        setFormItemsInAdvancedSearchForm(advancedSearchFormItems);
    }

    @Override
    protected void retrieveResources() {
        getUiHandlers().retrieveQuantityUnits(getQuantityUnitCriteria());
    }

    public QuantityUnitCriteria getQuantityUnitCriteria() {
        QuantityUnitCriteria criteria = new QuantityUnitCriteria();
        criteria.setCriteria(searchForm.getValueAsString(SEARCH_ITEM_NAME));

        QuantityUnitCriteriaOrderEnum quantityUnitCriteriaOrderEnum = CommonUtils.getQuantityUnitCriteriaOrderEnum(advancedSearchForm.getValueAsString(QuantityUnitDS.ORDER_BY));
        if (quantityUnitCriteriaOrderEnum != null) {
            OrderTypeEnum orderTypeEnum = CommonUtils.getOrderTypeEnum(advancedSearchForm.getValueAsString(QuantityUnitDS.ORDER_TYPE));
            criteria.setOrders(ClientCriteriaUtils.buildCriteriaOrder(orderTypeEnum, quantityUnitCriteriaOrderEnum));
        }

        return criteria;
    }

    public void setUiHandlers(AdminQuantityUnitsUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public AdminQuantityUnitsUiHandlers getUiHandlers() {
        return uiHandlers;
    }
}
