package es.gobcan.istac.indicators.web.shared;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;

@GenDispatch(isSecure=false)
public class CreateIndicator {

	@In(1)
	IndicatorDto indicator;
}
