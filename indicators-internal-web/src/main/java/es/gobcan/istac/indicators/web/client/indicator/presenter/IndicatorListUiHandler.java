package es.gobcan.istac.indicators.web.client.indicator.presenter;

import java.util.List;

import org.siemac.metamac.web.common.client.view.handlers.SrmExternalResourcesUiHandlers;

import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.web.shared.criteria.IndicatorCriteria;

public interface IndicatorListUiHandler extends SrmExternalResourcesUiHandlers {

    void createIndicator(IndicatorDto indicator);
    void deleteIndicators(List<String> uuids);

    void goToIndicator(String code);

    void retrieveIndicators(IndicatorCriteria criteria);

    void exportIndicators(IndicatorCriteria criteria);

    void enableNotifyPopulationErrors(List<String> uuids);
    void disableNotifyPopulationErrors(List<String> uuids);

    void updateCategoryCache();
}
