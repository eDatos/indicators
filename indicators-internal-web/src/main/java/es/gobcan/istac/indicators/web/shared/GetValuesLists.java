package es.gobcan.istac.indicators.web.shared;

import java.util.List;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.UnitMultiplierDto;

@GenDispatch(isSecure = false)
public class GetValuesLists {

    @Out(1)
    List<GeographicalGranularityDto> geoGranularities;

    @Out(2)
    List<UnitMultiplierDto>          unitMultiplers;

}
