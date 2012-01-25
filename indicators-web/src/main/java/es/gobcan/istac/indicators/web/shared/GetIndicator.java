package es.gobcan.istac.indicators.web.shared;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.web.shared.db.Indicator;

@GenDispatch(isSecure=false)
public class GetIndicator {

	@In(1)
	Long indicatorId;
	
	@Out(1)
	Indicator indicator;
}
