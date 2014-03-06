package es.gobcan.istac.indicators.web.shared;

import java.util.List;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;

@GenDispatch(isSecure = false)
public class GetGeographicalGranularitiesPaginatedList {

    @In(1)
    int                              maxResults;
    @In(2)
    int                              firstResult;

    @Out(1)
    List<GeographicalGranularityDto> dtos;

    @Out(2)
    Integer                          totalResults;
}
