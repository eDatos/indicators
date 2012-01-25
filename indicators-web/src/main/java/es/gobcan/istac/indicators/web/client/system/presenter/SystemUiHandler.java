package es.gobcan.istac.indicadores.web.client.system.presenter;

import java.util.List;

import com.gwtplatform.mvp.client.UiHandlers;

import es.gobcan.istac.indicadores.web.shared.db.IndicatorSystemContent;

public interface SystemUiHandler extends UiHandlers {
	
	void goToGeneral();
	
	void retrieveSystemStructure();

}
