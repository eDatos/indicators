package es.gobcan.istac.indicators.web.shared;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;

@GenDispatch(isSecure = false)
public class SaveGeoGranularity {

    @In(1)
    GeographicalGranularityDto dto;

    @Out(1)
    GeographicalGranularityDto outputDto;
}
