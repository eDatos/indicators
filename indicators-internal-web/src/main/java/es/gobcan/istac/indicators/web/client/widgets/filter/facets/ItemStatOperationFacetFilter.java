package es.gobcan.istac.indicators.web.client.widgets.filter.facets;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.constants.CommonWebConstants;
import org.siemac.metamac.web.common.client.widgets.actions.search.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.filters.base.FilterAction;
import org.siemac.metamac.web.common.client.widgets.filters.facets.FacetFilter;
import org.siemac.metamac.web.common.client.widgets.form.fields.external.SearchExternalItemLinkItem;
import org.siemac.metamac.web.common.client.widgets.windows.search.SearchRelatedResourceBasePaginatedWindow;

import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

import es.gobcan.istac.indicators.web.client.IndicatorsWeb;
import es.gobcan.istac.indicators.web.client.model.ds.DataSourceDS;
import es.gobcan.istac.indicators.web.client.widgets.filter.SimpleStatHiddenLastVersionFilterForm;
import es.gobcan.istac.indicators.web.shared.criteria.QueryWebCriteria;

public class ItemStatOperationFacetFilter implements FacetFilter {
    
    protected SearchExternalItemLinkItem     statOperationFilter;
    protected SearchSrmStatPaginatedWindow   statOperationWindow;
    protected FilterAction                   filterAction;

    public ItemStatOperationFacetFilter(final int maxResults, final SearchPaginatedAction<QueryWebCriteria> action) {
        super();
        statOperationFilter = createStatFilterItem(action);
        statOperationFilter.setShowTitle(true);
    }

    @Override
    public void setFilterAction(FilterAction action) {
        filterAction = action;
    }

    @Override
    public FormItem getFormItem() {
        return statOperationFilter;
    }

    @Override
    public void setColSpan(int cols) {
        statOperationFilter.setColSpan(cols);
    }

    public void populateCriteria(QueryWebCriteria criteria) {
        ExternalItemDto selectedResource = statOperationFilter.getExternalItemDto();
        criteria.setStatisticalOperationUrn(selectedResource != null ? selectedResource.getUrn() : null);
    }

    public void setFilterResources(List<ExternalItemDto> resources) {
        if (statOperationWindow != null) {
            statOperationWindow.setResources(resources);
        }
    }

    public void refreshFilterSourcePaginationInfo(int firstResult, int elementsInPage, int totalResults) {
        if (statOperationWindow != null) {
            statOperationWindow.refreshSourcePaginationInfo(firstResult, elementsInPage, totalResults);
        }
    }

    public void setSelectedItemScheme(ExternalItemDto selected) {
        statOperationFilter.setExternalItem(selected);
        filterAction.applyFilter();
    }

    private SearchExternalItemLinkItem createStatFilterItem(final SearchPaginatedAction<QueryWebCriteria> action) {
        SearchExternalItemLinkItem item = new SearchExternalItemLinkItem(DataSourceDS.QUERY_METAMAC, IndicatorsWeb.getConstants().statisticalOperation()) {

            @Override
            public void onSearch() {
                statOperationWindow = new SearchSrmStatPaginatedWindow(MetamacWebCommon.getConstants().resourceSelection(), CommonWebConstants.FORM_LIST_MAX_RESULTS, action);
                statOperationWindow.retrieveItems();
                statOperationWindow.setSaveAction(new ClickHandler() {
                    
                    @Override
                    public void onClick(ClickEvent event) {
                        setSelectedItemScheme(statOperationWindow.getSelectedResource());
                        statOperationWindow.markForDestroy();
                    }
                });
            }

            @Override
            protected void onClear() {
                super.onClear();
                setSelectedItemScheme(null);
            }
        };
        return item;
    }

    private class SearchSrmStatPaginatedWindow extends SearchRelatedResourceBasePaginatedWindow<ExternalItemDto, QueryWebCriteria> {

        public SearchSrmStatPaginatedWindow(String title, int maxResults, SearchPaginatedAction<QueryWebCriteria> action) {
            super(title, maxResults, new SimpleStatHiddenLastVersionFilterForm(), action);
        }
    }
}
