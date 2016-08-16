package es.gobcan.istac.indicators.web.shared;

import java.util.List;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.core.dto.UnitMultiplierDto;

@GenDispatch(isSecure = false)
public class GetUnitMultipliers {

    @Out(1)
    List<UnitMultiplierDto> unitMultiplierDtos;

}
