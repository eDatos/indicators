package es.gobcan.istac.indicators.web.shared;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;

@GenDispatch(isSecure=false)
public class UpdateDimension {

	@In(1)
	DimensionDto dimension;
}
