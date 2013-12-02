package es.gobcan.istac.indicators.web.shared;

import java.util.List;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.core.dto.UnitMultiplierDto;

@GenDispatch(isSecure = false)
public class GetUnitMultipliersPaginatedList {

    @In(1)
    int                     maxResults;
    @In(2)
    int                     firstResult;

    @Out(1)
    List<UnitMultiplierDto> dtos;

    @Out(2)
    Integer                 totalResults;
}
