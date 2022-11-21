package es.gobcan.istac.indicators.web.client.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.web.common.client.widgets.actions.search.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.fields.external.SearchExternalItemLinkItem;
import org.siemac.metamac.web.common.client.widgets.form.utils.FormUtils;

import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

import es.gobcan.istac.indicators.web.shared.criteria.QueryWebCriteria;

public abstract class StatOperationsSearchExternalItemLinkItem extends SearchExternalItemLinkItem {

    private int                                 maxResults;
    private StatOperationSearchPaginatedWindow  window;

    public StatOperationsSearchExternalItemLinkItem(String name, String title, int maxResults) {
        super(name, title);
        this.maxResults = maxResults;
    }

    @Override
    protected void onSearch() {
        SearchPaginatedAction<QueryWebCriteria> filterSearchAction = new SearchPaginatedAction<QueryWebCriteria>() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults, QueryWebCriteria webCriteria) {
                retrieveStatisticalOperationsForQuerySelection(firstResult, maxResults, webCriteria);
            }
        };
        
        window = new StatOperationSearchPaginatedWindow(getConstants().resourceSelection(), maxResults, filterSearchAction, new SearchPaginatedAction<QueryWebCriteria>() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults, QueryWebCriteria criteria) {
                retrieveResultSetQuery(firstResult, maxResults, criteria);
            }
        });
        
        window.retrieveItems();
        window.setSaveAction(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (getForm() != null) {
                    ExternalItemDto selectedResource = window.getSelectedResource();
                    retrieveQueryForRelatedQuery(selectedResource);
                    window.markForDestroy();
                    FormUtils.setValue(getForm(), getName(), selectedResource);
                }
            }
        });
    }
    
    public void setFilterResources(List<ExternalItemDto> externalItemsDtos, int firstResult, int elementsInPage, int totalResults) {
        if (window != null) {
            window.setFilterResources(externalItemsDtos);
            window.refreshFilterSourcePaginationInfo(firstResult, elementsInPage, totalResults);
        }
    }
    
    public void setResources(List<ExternalItemDto> externalItemsDtos, int firstResult, int elementsInPage, int totalResults) {
        if (window != null) {
            window.setResources(externalItemsDtos);
            window.refreshSourcePaginationInfo(firstResult, elementsInPage, totalResults);
        }
    }
    
    protected abstract void retrieveStatisticalOperationsForQuerySelection(int firstResult, int maxResults, QueryWebCriteria criteria);

    protected abstract void retrieveResultSetQuery(int firstResult, int maxResults, QueryWebCriteria criteria);

    protected abstract void retrieveQueryForRelatedQuery(ExternalItemDto selectedResource);
}
