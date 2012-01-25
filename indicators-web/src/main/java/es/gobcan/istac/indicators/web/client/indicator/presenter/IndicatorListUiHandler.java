package es.gobcan.istac.indicators.web.client.indicator.presenter;

import com.gwtplatform.mvp.client.UiHandlers;

public interface IndicatorListUiHandler extends UiHandlers {
	void createIndicator(String name);
	void reloadIndicatorList();
}
