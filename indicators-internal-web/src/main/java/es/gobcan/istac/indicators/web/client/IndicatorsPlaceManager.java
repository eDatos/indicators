package es.gobcan.istac.indicators.web.client;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.proxy.PlaceManagerImpl;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.TokenFormatter;

import es.gobcan.istac.indicators.web.client.gin.DefaultPlace;

public class IndicatorsPlaceManager extends PlaceManagerImpl {

    private final PlaceRequest defaultPlaceRequest;

    @Inject
    public IndicatorsPlaceManager(EventBus eventBus, TokenFormatter tokenFormatter, @DefaultPlace String defaultNameToken) {
        super(eventBus, tokenFormatter);
        this.defaultPlaceRequest = new PlaceRequest(defaultNameToken);
    }

    public void revealDefaultPlace() {
        revealPlace(defaultPlaceRequest);
    }
}
