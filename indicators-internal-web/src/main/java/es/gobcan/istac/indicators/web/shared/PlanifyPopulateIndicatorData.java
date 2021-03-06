package es.gobcan.istac.indicators.web.shared;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.core.dto.IndicatorDto;

@GenDispatch(isSecure = false)
public class PlanifyPopulateIndicatorData {

    @In(1)
    String       indicatorUuid;

    @Out(1)
    IndicatorDto indicatorDto;

}
