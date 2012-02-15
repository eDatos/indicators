package es.gobcan.istac.indicators.web.client.system.presenter;

import java.util.List;

import com.gwtplatform.mvp.client.UiHandlers;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;

public interface SystemListUiHandler extends UiHandlers {

	void createIndicatorsSystem(IndicatorsSystemDto system);
	void deleteIndicatorsSystems(List<String> codes);
	
	void reloadIndicatorsSystemList();
	void goToIndicatorsSystem(String indSystem);
}
