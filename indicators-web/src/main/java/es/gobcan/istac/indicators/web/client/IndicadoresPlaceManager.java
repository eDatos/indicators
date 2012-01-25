package es.gobcan.istac.indicadores.web.client;


import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.proxy.PlaceManagerImpl;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.TokenFormatter;

import es.gobcan.istac.indicadores.web.client.gin.DefaultPlace;

public class IndicadoresPlaceManager extends PlaceManagerImpl {

	private final PlaceRequest defaultPlaceRequest;
	
	@Inject
	public IndicadoresPlaceManager(EventBus eventBus, TokenFormatter tokenFormatter, @DefaultPlace String defaultNameToken) {
		super(eventBus,tokenFormatter);
		this.defaultPlaceRequest = new PlaceRequest(defaultNameToken);
	}

	public void revealDefaultPlace() {
		revealPlace(defaultPlaceRequest);
	}
}
