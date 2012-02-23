package es.gobcan.istac.indicators.web.shared;

import java.util.List;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorInstanceDto;

@GenDispatch(isSecure=false)
public class GetIndicatorsSystemStructure {

	@In(1)
	String uuid;
	
	@Out(1)
	List<DimensionDto> dimensions;
	
	@Out(2)
	List<IndicatorInstanceDto> indicatorInstances;
}
