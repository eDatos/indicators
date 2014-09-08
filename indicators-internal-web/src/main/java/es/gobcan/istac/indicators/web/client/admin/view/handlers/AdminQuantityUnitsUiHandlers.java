package es.gobcan.istac.indicators.web.client.admin.view.handlers;

import java.util.List;

import com.gwtplatform.mvp.client.UiHandlers;

import es.gobcan.istac.indicators.core.dto.QuantityUnitDto;

public interface AdminQuantityUnitsUiHandlers extends UiHandlers {

    void deleteQuantityUnits(List<String> quantityUnitsUuids, int firstResult);

    void saveQuantityUnit(int currentPage, QuantityUnitDto quantityUnitDto);

    void retrieveQuantityUnits(final int firstResult, final int maxResults);
}
