package es.gobcan.istac.indicators.web.shared;

import java.util.List;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.web.shared.criteria.GeoValueCriteria;

@GenDispatch(isSecure = false)
public class GetGeographicalValuesPaginatedList {

    @In(1)
    GeoValueCriteria           criteria;

    @Out(1)
    List<GeographicalValueDto> dtos;

    @Out(2)
    Integer                    totalResults;
}
