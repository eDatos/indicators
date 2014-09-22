package es.gobcan.istac.indicators.web.client.indicator.presenter;

import java.util.List;

import com.gwtplatform.mvp.client.UiHandlers;

import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.web.shared.criteria.IndicatorCriteria;

public interface IndicatorListUiHandler extends UiHandlers {

    void createIndicator(IndicatorDto indicator);
    void deleteIndicators(List<String> uuids);

    void goToIndicator(String code);

    void retrieveSubjectsList();

    void retrieveIndicators(IndicatorCriteria criteria);
}
