package es.gobcan.istac.indicators.web.client.widgets.filter.base;

import java.util.Arrays;
import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.web.common.client.widgets.filters.base.SimpleVersionableFilterBaseForm;
import org.siemac.metamac.web.common.client.widgets.filters.facets.FacetFilter;
import org.siemac.metamac.web.common.client.widgets.filters.facets.OnlyLastVersionFacetFilter;

import es.gobcan.istac.indicators.web.client.widgets.filter.facets.StatisticalOperationFacetFilter;
import es.gobcan.istac.indicators.web.shared.criteria.VersionableStatisticalResourceWebCriteria;

public abstract class VersionableStatisticalResourceFilterBaseForm<T extends VersionableStatisticalResourceWebCriteria> extends SimpleVersionableFilterBaseForm<T> {

    private StatisticalOperationFacetFilter statisticalOperationFacet;

    public VersionableStatisticalResourceFilterBaseForm() {
        super();
        statisticalOperationFacet = new StatisticalOperationFacetFilter();
         onlyLastVersionFacet = new OnlyLastVersionFacetFilter();
         onlyLastVersionFacet.setColSpan(2);
        criteriaFacet.setColSpan(2);
    }

    public void setStatisticalOperations(List<ExternalItemDto> statisticalOperations) {
        statisticalOperationFacet.setStatisticalOperations(statisticalOperations);
    }

    public void setSelectedStatisticalOperation(ExternalItemDto statisticalOperation) {
        if (statisticalOperation != null) {
            statisticalOperationFacet.setSelectedStatisticalOperation(statisticalOperation);
        }
    }

    // IMPORTANT: This method must be inherited if you change the WebCriteria in T
    @Override
    public T getSearchCriteria() {
        T searchCriteria = super.getSearchCriteria();

        statisticalOperationFacet.populateCriteria(searchCriteria);

        return searchCriteria;
    }

    @Override
    public List<FacetFilter> getFacets() {
        return Arrays.asList(statisticalOperationFacet, criteriaFacet);
    }
}
