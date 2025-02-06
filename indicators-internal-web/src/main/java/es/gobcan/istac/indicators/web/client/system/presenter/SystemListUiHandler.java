package es.gobcan.istac.indicators.web.client.system.presenter;

import java.util.List;

import com.gwtplatform.mvp.client.UiHandlers;

import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;

public interface SystemListUiHandler extends UiHandlers {

    void goToIndicatorsSystem(String indSystem);
    void deleteIndicatorsSystems(List<String> uuids);
    void retrieveSystems(int firstResult, int maxResults);
    void createIndicatorsSystems(IndicatorsSystemDto indicatorsSystem);
}
