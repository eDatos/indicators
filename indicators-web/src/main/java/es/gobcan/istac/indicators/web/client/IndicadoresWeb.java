package es.gobcan.istac.indicadores.web.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.gwtplatform.mvp.client.DelayedBindRegistry;

import es.gobcan.istac.indicadores.web.client.gin.IndicadoresWebGinjector;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class IndicadoresWeb implements EntryPoint {

	private static IndicadoresWebMessages messages;
	private IndicadoresWebGinjector ginjector = GWT.create(IndicadoresWebGinjector.class);

	@Override
	public void onModuleLoad() {
		DelayedBindRegistry.bind(ginjector);
		
		ginjector.getPlaceManager().revealCurrentPlace();
	}
	
	public static IndicadoresWebMessages getMessages() {
		if (messages == null) {
			messages = GWT.create(IndicadoresWebMessages.class);
		}
		return messages;
	}
	
}
