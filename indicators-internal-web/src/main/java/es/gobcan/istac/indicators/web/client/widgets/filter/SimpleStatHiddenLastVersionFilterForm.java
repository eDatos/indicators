package es.gobcan.istac.indicators.web.client.widgets.filter;

import java.util.Arrays;
import java.util.List;

import org.siemac.metamac.web.common.client.widgets.filters.base.SimpleFilterBaseForm;
import org.siemac.metamac.web.common.client.widgets.filters.facets.FacetFilter;

import es.gobcan.istac.indicators.web.shared.criteria.QueryWebCriteria;

public class SimpleStatHiddenLastVersionFilterForm extends SimpleFilterBaseForm<QueryWebCriteria> {

    public SimpleStatHiddenLastVersionFilterForm() {
        super();
        criteriaFacet.setColSpan(2);
    }

    // IMPORTANT: This method must be inherited if you change the WebCriteria in T
    @Override
    public QueryWebCriteria getSearchCriteria() {
        QueryWebCriteria searchCriteria = super.getSearchCriteria();
        return searchCriteria;
    }

    @Override
    protected QueryWebCriteria buildEmptySearchCriteria() {
        return new QueryWebCriteria();
    }

    @Override
    public List<FacetFilter> getFacets() {
        return Arrays.asList((FacetFilter) criteriaFacet);
    }
}