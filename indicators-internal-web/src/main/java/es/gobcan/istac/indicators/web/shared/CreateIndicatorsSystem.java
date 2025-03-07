package es.gobcan.istac.indicators.web.shared;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;

@GenDispatch(isSecure = false)
public class CreateIndicatorsSystem {

    @In(1)
    IndicatorsSystemDto indicatorsSystemDto;

    @Out(1)
    IndicatorsSystemDto newIndicatorsSystemDto;

}
