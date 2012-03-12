package es.gobcan.istac.indicators.web.shared;

import java.util.List;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.core.dto.serviceapi.GeographicalValueDto;

@GenDispatch(isSecure=false)
public class GetGeographicalValues {

    @In(1)
    String geographicalGranularityUuid;
    
    @Out(1)
    List<GeographicalValueDto> geographicalValueDtos;
    
}
