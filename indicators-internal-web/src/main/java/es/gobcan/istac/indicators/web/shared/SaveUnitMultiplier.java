package es.gobcan.istac.indicators.web.shared;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.core.dto.UnitMultiplierDto;

@GenDispatch(isSecure = false)
public class SaveUnitMultiplier {

    @In(1)
    UnitMultiplierDto dto;

    @Out(1)
    UnitMultiplierDto outputDto;
}
