package es.gobcan.istac.indicators.web.shared;

import java.util.List;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;

@GenDispatch(isSecure = false)
public class GetGeographicalValuesByGranularityInIndicator {

    @In(1)
    String                     indicatorUuid;

    @In(2)
    String                     indicatorVersion;

    @In(3)
    String                     geographicalGranularityUuid;

    @Out(1)
    List<GeographicalValueDto> geographicalValueDtos;
}
