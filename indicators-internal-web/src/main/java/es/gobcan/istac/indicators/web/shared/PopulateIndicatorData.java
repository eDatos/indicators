package es.gobcan.istac.indicators.web.shared;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.core.dto.IndicatorDto;

@GenDispatch(isSecure = false)
public class PopulateIndicatorData {

    @In(1)
    String indicatorUuid;

    @In(2)
    String version;
    
    @Out(1)
    IndicatorDto indicatorDto;

}