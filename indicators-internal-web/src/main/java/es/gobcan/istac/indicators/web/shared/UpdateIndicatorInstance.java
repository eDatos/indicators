package es.gobcan.istac.indicators.web.shared;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorInstanceDto;

@GenDispatch(isSecure = false)
public class UpdateIndicatorInstance {

    @In(1)
    IndicatorInstanceDto indicatorInstanceToUpdate;

    @Out(1)
    IndicatorInstanceDto indicatorInstance;

}
