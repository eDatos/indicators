package es.gobcan.istac.indicators.web.shared;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;

@GenDispatch(isSecure = false)
public class GetGeographicalValue {

    @In(1)
    String               geographicalValueUuid;

    @Out(1)
    GeographicalValueDto geographicalValueDto;

}
