package es.gobcan.istac.indicators.web.shared;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

import es.gobcan.istac.indicators.web.shared.db.IndicatorSystem;

@GenDispatch(isSecure=false)
public class SaveIndicatorSystem {

	@In(1)
	IndicatorSystem indSystem;
}
