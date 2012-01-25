package es.gobcan.istac.indicadores.web.client.system.presenter;

import com.gwtplatform.mvp.client.UiHandlers;

public interface SystemListUiHandler extends UiHandlers {

	void createIndicatorSystem(String name);
	void reloadIndicatorSystemList();
	void goToIndicatorSystem(String indSystem);
}
