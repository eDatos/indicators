package es.gobcan.istac.indicators.web.client.widgets;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.web.common.client.widgets.actions.search.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.filters.base.FilterForm;
import org.siemac.metamac.web.common.client.widgets.windows.search.SearchRelatedResourceBasePaginatedWindow;

import es.gobcan.istac.indicators.web.client.indicator.widgets.QueryFilterForm;
import es.gobcan.istac.indicators.web.shared.criteria.QueryWebCriteria;

public class SearchSingleQueryPaginatedWindow extends SearchRelatedResourceBasePaginatedWindow<ExternalItemDto, QueryWebCriteria> {

    private QueryFilterForm filterForm;

    public SearchSingleQueryPaginatedWindow(String title, int maxResults, SearchPaginatedAction<QueryWebCriteria> action) {
        super(title, maxResults, new QueryFilterForm(), action);
        filterForm = (QueryFilterForm) getFilterForm();
    }

    private SearchSingleQueryPaginatedWindow(String title, int maxResults, FilterForm<QueryWebCriteria> filter, SearchPaginatedAction<QueryWebCriteria> action) {
        super(title, maxResults, filter, action);
    }

    public void setStatisticalOperations(List<ExternalItemDto> statisticalOperations) {
        filterForm.setStatisticalOperations(statisticalOperations);
    }

    public void setSelectedStatisticalOperation(ExternalItemDto statisticalOperation) {
        filterForm.setSelectedStatisticalOperation(statisticalOperation);
    }

    public void setFixedQueryCode(String queryCode) {
        filterForm.setFixedQueryCode(queryCode);
    }

    public QueryWebCriteria getQueryWebCriteria() {
        return filterForm.getSearchCriteria();
    }

    public void setOnlyLastVersion(boolean onlyLastVersion) {
        filterForm.setOnlyLastVersion(onlyLastVersion);
    }
}
