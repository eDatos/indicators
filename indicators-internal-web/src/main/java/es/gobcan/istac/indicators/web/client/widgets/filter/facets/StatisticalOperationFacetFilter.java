package es.gobcan.istac.indicators.web.client.widgets.filter.facets;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.web.common.client.utils.ExternalItemUtils;
import org.siemac.metamac.web.common.client.widgets.filters.base.FilterAction;
import org.siemac.metamac.web.common.client.widgets.filters.facets.FacetFilter;

import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;

import es.gobcan.istac.indicators.web.client.IndicatorsWeb;
import es.gobcan.istac.indicators.web.shared.criteria.base.HasStatisticalOperationCriteria;

public class StatisticalOperationFacetFilter implements FacetFilter {

    private static final String FIELD_STAT_OPER = "stat-oper-select";

    private SelectItem          statisticalOperationSelectItem;

    public StatisticalOperationFacetFilter() {
        statisticalOperationSelectItem = new SelectItem(FIELD_STAT_OPER, IndicatorsWeb.getConstants().statisticalOperation());
        statisticalOperationSelectItem.setRequired(false);
        statisticalOperationSelectItem.setWidth("*");
        statisticalOperationSelectItem.setTitleOrientation(TitleOrientation.LEFT);
    }

    public void setStatisticalOperations(List<ExternalItemDto> statisticalOperations) {
        statisticalOperationSelectItem.setValueMap(ExternalItemUtils.getExternalItemsHashMap(statisticalOperations));
    }

    public void setSelectedStatisticalOperation(ExternalItemDto statisticalOperation) {
        if (statisticalOperation != null) {
            statisticalOperationSelectItem.setValue(statisticalOperation.getUrn());
        }
    }

    public void populateCriteria(HasStatisticalOperationCriteria criteria) {
        criteria.setStatisticalOperationUrn(statisticalOperationSelectItem.getValueAsString());
    }

    @Override
    public FormItem getFormItem() {
        return statisticalOperationSelectItem;
    }

    @Override
    public void setFilterAction(final FilterAction action) {
        statisticalOperationSelectItem.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                action.applyFilter();
            }
        });
    }

    @Override
    public void setColSpan(int cols) {
        statisticalOperationSelectItem.setColSpan(cols);
    }
}
