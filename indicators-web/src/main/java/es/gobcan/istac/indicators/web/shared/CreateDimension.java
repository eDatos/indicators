package es.gobcan.istac.indicators.web.shared;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;

@GenDispatch(isSecure=false)
public class CreateDimension {
	
	@In(1)
	IndicatorsSystemDto indicatorsSystem;
	
	@In(2)
	DimensionDto dimension;
	
	@Out(1)
	DimensionDto createdDimension;
}
