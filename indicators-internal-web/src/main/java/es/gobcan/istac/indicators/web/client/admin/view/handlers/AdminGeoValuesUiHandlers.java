package es.gobcan.istac.indicators.web.client.admin.view.handlers;

import java.util.List;

import com.gwtplatform.mvp.client.UiHandlers;

import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;

public interface AdminGeoValuesUiHandlers extends UiHandlers {

    void deleteGeoValues(List<String> uuids, int firstResult);

    void saveGeoValue(int currentPage, GeographicalValueDto dto);

    void retrieveGeoValues(int firstResult);
}
