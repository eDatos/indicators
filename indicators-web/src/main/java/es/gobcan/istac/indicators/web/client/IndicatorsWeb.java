package es.gobcan.istac.indicators.web.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.gwtplatform.mvp.client.DelayedBindRegistry;

import es.gobcan.istac.indicators.web.client.gin.IndicatorsWebGinjector;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class IndicatorsWeb implements EntryPoint {

	private static IndicatorsWebMessages messages;
	private IndicatorsWebGinjector ginjector = GWT.create(IndicatorsWebGinjector.class);

	@Override
	public void onModuleLoad() {
		DelayedBindRegistry.bind(ginjector);
		
		ginjector.getPlaceManager().revealCurrentPlace();
	}
	
	public static IndicatorsWebMessages getMessages() {
		if (messages == null) {
			messages = GWT.create(IndicatorsWebMessages.class);
		}
		return messages;
	}
	
}
