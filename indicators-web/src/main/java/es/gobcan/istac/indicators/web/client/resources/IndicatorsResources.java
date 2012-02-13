package es.gobcan.istac.indicators.web.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundleWithLookup;
import com.google.gwt.resources.client.ImageResource;

public interface IndicatorsResources extends ClientBundleWithLookup {
	
	public static final IndicatorsResources RESOURCE =  GWT.create(IndicatorsResources.class);

	
	@Source("images/ok_apply.png")
	ImageResource okApply();
	
}
