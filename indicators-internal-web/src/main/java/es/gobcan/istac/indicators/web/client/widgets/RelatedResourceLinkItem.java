package es.gobcan.istac.indicators.web.client.widgets;

import java.util.List;

import org.siemac.metamac.web.common.client.widgets.form.fields.RelatedResourceBaseLinkItem;
import org.siemac.metamac.web.common.client.widgets.handlers.CustomLinkItemNavigationClickHandler;

import com.gwtplatform.mvp.client.proxy.PlaceRequest;

import es.gobcan.istac.indicators.core.dto.RelatedResourceDto;
import es.gobcan.istac.indicators.web.client.utils.PlaceRequestUtils;

public class RelatedResourceLinkItem extends RelatedResourceBaseLinkItem<RelatedResourceDto> {

    public RelatedResourceLinkItem(String name, String title, CustomLinkItemNavigationClickHandler clickHandler) {
        super(name, title, clickHandler);
    }

    @Override
    protected List<PlaceRequest> buildLocation(RelatedResourceDto relatedResourceDto) {
        return PlaceRequestUtils.buildAbsoluteResourcePlaceRequest(relatedResourceDto);
    }

    @Override
    public void clearValue() {
        super.clearRelatedResource();
    }
}
