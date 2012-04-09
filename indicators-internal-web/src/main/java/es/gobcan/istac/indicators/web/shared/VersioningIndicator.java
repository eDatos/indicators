package es.gobcan.istac.indicators.web.shared;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.enume.domain.VersionTypeEnum;

@GenDispatch(isSecure = false)
public class VersioningIndicator {

    @In(1)
    String          uuid;

    @In(2)
    VersionTypeEnum versionType;

    @Out(1)
    IndicatorDto    indicatorDto;

}
