package es.gobcan.istac.indicators.web.shared;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.core.dto.IndicatorInstanceDto;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemDtoWeb;

@GenDispatch(isSecure = false)
public class CreateIndicatorInstance {

    @In(1)
    IndicatorsSystemDtoWeb indicatorsSystem;

    @In(2)
    IndicatorInstanceDto   indicatorInstance;

    @Out(1)
    IndicatorInstanceDto   createdIndicatorInstance;
}
