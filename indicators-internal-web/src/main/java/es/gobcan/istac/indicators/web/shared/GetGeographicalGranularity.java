package es.gobcan.istac.indicators.web.shared;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;

@GenDispatch(isSecure = false)
public class GetGeographicalGranularity {

    @In(1)
    String                     geographicalGranularityUuid;

    @Out(2)
    GeographicalGranularityDto geographicalGranularityDto;

}
