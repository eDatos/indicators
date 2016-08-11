package es.gobcan.istac.indicators.web.shared;

import java.util.List;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.core.dto.QuantityUnitDto;
import es.gobcan.istac.indicators.web.shared.criteria.QuantityUnitCriteria;

@GenDispatch(isSecure = false)
public class GetQuantityUnitsPaginatedList {

    @In(1)
    QuantityUnitCriteria  criteria;

    @Out(1)
    List<QuantityUnitDto> dtos;

    @Out(2)
    Integer               totalResults;
}
