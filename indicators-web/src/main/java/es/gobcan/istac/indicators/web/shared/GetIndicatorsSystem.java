package es.gobcan.istac.indicators.web.shared;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;

@GenDispatch(isSecure=false)
public class GetIndicatorsSystem {

	@In(1)
	Long indSysId;
	
	@Out(1)
	IndicatorsSystemDto indicatorsSystem;
}
