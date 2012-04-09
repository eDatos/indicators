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
	private static IndicatorsWebCoreMessages coreMessages;
	private static IndicatorsWebConstants constants;
	private IndicatorsWebGinjector ginjector = GWT.create(IndicatorsWebGinjector.class);
	

	@Override
	public void onModuleLoad() {
		DelayedBindRegistry.bind(ginjector);
		
		ginjector.getPlaceManager().revealCurrentPlace();
	}
	
	public static IndicatorsWebCoreMessages getCoreMessages() {
	    if (coreMessages == null) {
	        coreMessages = GWT.create(IndicatorsWebCoreMessages.class);
	    }
	    return coreMessages;
	}
	
	public static IndicatorsWebMessages getMessages() {
		if (messages == null) {
			messages = GWT.create(IndicatorsWebMessages.class);
		}
		return messages;
	}
	
	public static IndicatorsWebConstants getConstants() {
	    if (constants == null) {
	        constants = GWT.create(IndicatorsWebConstants.class);
	    }
	    return constants;
	}
	
	
}