package es.gobcan.istac.indicators.web.client.admin.view.handlers;

import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;

import es.gobcan.istac.indicators.web.shared.criteria.GeoValueCriteria;

public interface AdminGeoValuesUiHandlers extends BaseUiHandlers {

    void retrieveGeoValues(GeoValueCriteria criteria);
    void goToGeoValue(String code);
}
