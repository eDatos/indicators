package es.gobcan.istac.indicators.web.client.system.presenter;

import java.util.List;

import com.gwtplatform.mvp.client.UiHandlers;

public interface SystemListUiHandler extends UiHandlers {

    void reloadIndicatorsSystemList();
    void goToIndicatorsSystem(String indSystem);
    void deleteIndicatorsSystems(List<String> uuids);

    // Pagination
    void onResultSetNextButtonClicked();
    void onResultSetFirstButtonClicked();
    void onResultSetLastButtonClicked();
    void onResultSetPreviousButtonClicked();

}
