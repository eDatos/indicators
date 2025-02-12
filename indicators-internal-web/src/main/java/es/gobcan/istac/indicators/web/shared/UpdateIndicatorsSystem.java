package es.gobcan.istac.indicators.web.shared;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemDtoWeb;

@GenDispatch(isSecure = false)
public class UpdateIndicatorsSystem {

    @In(1)
    IndicatorsSystemDtoWeb indicatorsSystemUpdated;

    @Out(1)
    IndicatorsSystemDtoWeb indicatorsSystemDtoWeb;

}
