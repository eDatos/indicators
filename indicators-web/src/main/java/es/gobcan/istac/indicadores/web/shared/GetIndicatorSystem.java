package es.gobcan.istac.indicadores.web.shared;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicadores.web.shared.db.IndicatorSystem;

@GenDispatch(isSecure=false)
public class GetIndicatorSystem {

	@In(1)
	Long indSysId;
	
	@Out(1)
	IndicatorSystem indicatorSystem;
}
