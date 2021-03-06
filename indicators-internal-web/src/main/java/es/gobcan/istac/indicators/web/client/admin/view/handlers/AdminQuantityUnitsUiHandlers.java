package es.gobcan.istac.indicators.web.client.admin.view.handlers;

import java.util.List;

import com.gwtplatform.mvp.client.UiHandlers;

import es.gobcan.istac.indicators.core.dto.QuantityUnitDto;
import es.gobcan.istac.indicators.web.shared.criteria.QuantityUnitCriteria;

public interface AdminQuantityUnitsUiHandlers extends UiHandlers {

    void deleteQuantityUnits(List<String> quantityUnitsUuids, int firstResult);

    void saveQuantityUnit(int currentPage, QuantityUnitDto quantityUnitDto);

    void retrieveQuantityUnits(QuantityUnitCriteria criteria);
}
