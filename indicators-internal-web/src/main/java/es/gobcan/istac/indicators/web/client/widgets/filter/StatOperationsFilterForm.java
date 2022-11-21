package es.gobcan.istac.indicators.web.client.widgets.filter;

import java.util.Arrays;
import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.web.common.client.widgets.actions.search.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.filters.base.SimpleFilterBaseForm;
import org.siemac.metamac.web.common.client.widgets.filters.facets.FacetFilter;

import es.gobcan.istac.indicators.web.client.widgets.filter.facets.ItemStatOperationFacetFilter;
import es.gobcan.istac.indicators.web.shared.criteria.QueryWebCriteria;

public class StatOperationsFilterForm extends SimpleFilterBaseForm<QueryWebCriteria> {
    
    protected ItemStatOperationFacetFilter itemStatOperationFacetFilter;

    public StatOperationsFilterForm(int maxResults, SearchPaginatedAction<QueryWebCriteria> filter) {
        super();
        itemStatOperationFacetFilter = new ItemStatOperationFacetFilter(maxResults, filter);
        criteriaFacet.setColSpan(2);
    }
    
    public void setFilterResources(List<ExternalItemDto> resources) {
        if (itemStatOperationFacetFilter != null) {
            itemStatOperationFacetFilter.setFilterResources(resources);
        }
    }
    
    public void refreshFilterSourcePaginationInfo(int firstResult, int elementsInPage, int totalResults) {
        if (itemStatOperationFacetFilter != null) {
            itemStatOperationFacetFilter.refreshFilterSourcePaginationInfo(firstResult, elementsInPage, totalResults);
        }
    }
    
    public ItemStatOperationFacetFilter getItemSchemeFilterFacet() {
        return itemStatOperationFacetFilter;
    }

    @Override
    public List<FacetFilter> getFacets() {
        return Arrays.asList(itemStatOperationFacetFilter, criteriaFacet);
    }

    @Override
    public QueryWebCriteria getSearchCriteria() {
        QueryWebCriteria criteria = super.getSearchCriteria();
        itemStatOperationFacetFilter.populateCriteria(criteria);
        return criteria;
    }

    @Override
    protected QueryWebCriteria buildEmptySearchCriteria() {
        return new QueryWebCriteria();
    }
}
