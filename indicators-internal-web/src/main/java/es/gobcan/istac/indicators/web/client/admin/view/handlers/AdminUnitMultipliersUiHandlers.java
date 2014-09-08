package es.gobcan.istac.indicators.web.client.admin.view.handlers;

import java.util.List;

import com.gwtplatform.mvp.client.UiHandlers;

import es.gobcan.istac.indicators.core.dto.UnitMultiplierDto;

public interface AdminUnitMultipliersUiHandlers extends UiHandlers {

    void deleteUnitMultipliers(List<String> uuids, int firstResult);

    void saveUnitMultiplier(int currentPage, UnitMultiplierDto dto);

    void retrieveUnitMultipliers(int firstResult);
}
