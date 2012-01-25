package es.gobcan.istac.indicators.web.client.system.presenter;

import java.util.List;

import com.gwtplatform.mvp.client.UiHandlers;

import es.gobcan.istac.indicators.web.shared.db.IndicatorSystemContent;

public interface SystemUiHandler extends UiHandlers {
	
	void goToGeneral();
	
	void retrieveSystemStructure();

}
