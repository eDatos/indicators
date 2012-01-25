package es.gobcan.istac.indicators.web.shared;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

import es.gobcan.istac.indicators.web.shared.db.Indicator;

@GenDispatch(isSecure=false)
public class SaveIndicator {
	
	@In(1)
	Indicator indicator;
}
