package es.gobcan.istac.indicators.web.shared;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.core.dto.DimensionDto;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemDtoWeb;

@GenDispatch(isSecure = false)
public class CreateDimension {

    @In(1)
    IndicatorsSystemDtoWeb indicatorsSystem;

    @In(2)
    DimensionDto           dimension;

    @Out(1)
    DimensionDto           createdDimension;
}
