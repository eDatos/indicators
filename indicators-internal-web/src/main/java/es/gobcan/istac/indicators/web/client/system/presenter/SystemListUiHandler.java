package es.gobcan.istac.indicators.web.client.system.presenter;

import java.util.List;

import com.gwtplatform.mvp.client.UiHandlers;

public interface SystemListUiHandler extends UiHandlers {

    void goToIndicatorsSystem(String indSystem);
    void deleteIndicatorsSystems(List<String> uuids);
    void retrieveSystems(int firstResult, int maxResults);
}
