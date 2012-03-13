package es.gobcan.istac.indicators.web.client.indicator.presenter;

import com.gwtplatform.mvp.client.UiHandlers;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;


public interface IndicatorUiHandler extends UiHandlers {

    void saveIndicator(IndicatorDto indicator);
    void retrieveSubjects();
    
}
