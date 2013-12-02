package es.gobcan.istac.indicators.web.shared;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;

@GenDispatch(isSecure = false)
public class SaveGeoValue {

    @In(1)
    GeographicalValueDto dto;

    @Out(1)
    GeographicalValueDto outputDto;
}
