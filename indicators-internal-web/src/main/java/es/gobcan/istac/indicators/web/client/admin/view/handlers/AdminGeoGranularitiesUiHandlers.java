package es.gobcan.istac.indicators.web.client.admin.view.handlers;

import java.util.List;

import com.gwtplatform.mvp.client.UiHandlers;

import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;

public interface AdminGeoGranularitiesUiHandlers extends UiHandlers {

    void deleteGeoGranularities(List<String> uuids, int firstResult);

    void saveGeoGranularity(int currentPage, GeographicalGranularityDto dto);

    void retrieveGeoGranularities(int firstResult);
}
