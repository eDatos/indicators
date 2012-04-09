package es.gobcan.istac.indicators.web.shared;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;

@GenDispatch(isSecure = false)
public class SendIndicatorsSystemToProductionValidation {

    @In(1)
    IndicatorsSystemDto systemToSend;

    @Out(1)
    IndicatorsSystemDto indicatorsSystemDto;

}
