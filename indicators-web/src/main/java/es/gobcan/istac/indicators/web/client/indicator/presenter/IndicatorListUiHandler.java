package es.gobcan.istac.indicators.web.client.indicator.presenter;

import com.gwtplatform.mvp.client.UiHandlers;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;

public interface IndicatorListUiHandler extends UiHandlers {
	void createIndicator(IndicatorDto indicator);
	void reloadIndicatorList();
}
