package es.gobcan.istac.indicators.web.client.admin.view.handlers;

import com.gwtplatform.mvp.client.UiHandlers;

import es.gobcan.istac.indicators.web.shared.criteria.GeoValueCriteria;

public interface AdminGeoValuesUiHandlers extends UiHandlers {

    void retrieveGeoValues(GeoValueCriteria criteria);
}
