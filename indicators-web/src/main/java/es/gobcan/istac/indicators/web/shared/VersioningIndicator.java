package es.gobcan.istac.indicators.web.shared;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.enume.domain.VersiontTypeEnum;


@GenDispatch(isSecure=false)
public class VersioningIndicator {

    @In(1)
    String uuid;
    
    @In(2)
    VersiontTypeEnum versiontType;
    
    @Out(1)
    IndicatorDto indicatorDto;
    
}
