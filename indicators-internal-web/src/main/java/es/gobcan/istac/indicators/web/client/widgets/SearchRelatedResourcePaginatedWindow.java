package es.gobcan.istac.indicators.web.client.widgets;

import org.siemac.metamac.web.common.client.widgets.SearchRelatedResourceBasePaginatedWindow;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;

import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.grid.ListGrid;

import es.gobcan.istac.indicators.core.dto.RelatedResourceDto;

/**
 * Window with a {@link SearchRelatedResourcePaginatedItem}. The source {@link ListGrid} is paginated.
 */
@SuppressWarnings("deprecation")
public class SearchRelatedResourcePaginatedWindow extends SearchRelatedResourceBasePaginatedWindow<RelatedResourceDto> {

    public SearchRelatedResourcePaginatedWindow(String title, int maxResults, PaginatedAction action) {
        super(title, maxResults, action);
    }

    public SearchRelatedResourcePaginatedWindow(String title, int maxResults, FormItem initialSelectionItem, PaginatedAction action) {
        super(title, maxResults, initialSelectionItem, action);
    }
}
