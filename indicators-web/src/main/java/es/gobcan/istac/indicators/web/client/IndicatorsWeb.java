package es.gobcan.istac.indicators.web.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ClientBundle.Source;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.gwtplatform.mvp.client.DelayedBindRegistry;

import es.gobcan.istac.indicators.web.client.gin.IndicatorsWebGinjector;
import es.gobcan.istac.indicators.web.client.resources.IndicatorsResources;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class IndicatorsWeb implements EntryPoint {

	private static IndicatorsWebMessages messages;
	private static IndicatorsWebConstants constants;
	private IndicatorsWebGinjector ginjector = GWT.create(IndicatorsWebGinjector.class);
	
   interface GlobalResources extends ClientBundle {
        @NotStrict
        @Source("resources/IndicatorsWebStyles.css")
        CssResource css();
    }

	@Override
	public void onModuleLoad() {
		DelayedBindRegistry.bind(ginjector);
		
		ginjector.getPlaceManager().revealCurrentPlace();
		
		GWT.<IndicatorsResources>create(IndicatorsResources.class);
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
