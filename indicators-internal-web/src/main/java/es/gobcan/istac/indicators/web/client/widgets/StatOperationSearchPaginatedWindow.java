package es.gobcan.istac.indicators.web.client.widgets;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.web.common.client.widgets.actions.search.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.windows.search.SearchRelatedResourceBasePaginatedWindow;

import es.gobcan.istac.indicators.web.client.widgets.filter.StatOperationsFilterForm;
import es.gobcan.istac.indicators.web.shared.criteria.QueryWebCriteria;

public class StatOperationSearchPaginatedWindow extends SearchRelatedResourceBasePaginatedWindow<ExternalItemDto, QueryWebCriteria> {
    
    public StatOperationSearchPaginatedWindow(String title, int maxResults, SearchPaginatedAction<QueryWebCriteria> filter, SearchPaginatedAction<QueryWebCriteria> action) {
        super(title, maxResults, new StatOperationsFilterForm(maxResults, filter), action);
    }
    
    public void setStatisticalOperations(List<ExternalItemDto> statisticalOperations) {
        setResources(statisticalOperations);
    }

    public void setFilterResources(List<ExternalItemDto> resources) {
        getFilter().setFilterResources(resources);
    }

    public void refreshFilterSourcePaginationInfo(int firstResult, int elementsInPage, int totalResults) {
        getFilter().refreshFilterSourcePaginationInfo(firstResult, elementsInPage, totalResults);
    }

    public StatOperationsFilterForm getFilter() {
        return (StatOperationsFilterForm) getFilterForm();
    }
}
