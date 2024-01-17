package es.gobcan.istac.indicators.web.client.widgets.windows.search;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.web.common.client.widgets.form.fields.SearchRelatedResourceBaseLinkItem;
import org.siemac.metamac.web.common.client.widgets.handlers.CustomLinkItemNavigationClickHandler;

import com.gwtplatform.mvp.client.proxy.PlaceRequest;

import es.gobcan.istac.indicators.core.dto.RelatedResourceDto;

public class SearchRelatedResourceLinkItem extends SearchRelatedResourceBaseLinkItem<RelatedResourceDto> {

    public SearchRelatedResourceLinkItem(String name, String title, CustomLinkItemNavigationClickHandler clickHandler) {
        super(name, title, clickHandler);
    }

    @Override
    protected List<PlaceRequest> buildLocation(RelatedResourceDto relatedResourceDto) {
        return new ArrayList<PlaceRequest>();
    }

}
