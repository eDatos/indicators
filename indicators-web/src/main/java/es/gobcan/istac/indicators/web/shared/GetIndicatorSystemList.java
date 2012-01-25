package es.gobcan.istac.indicators.web.shared;

import java.util.List;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.web.shared.db.IndicatorSystem;

@GenDispatch(isSecure=false)
public class GetIndicatorSystemList {
	
	@Out(1)
	List<IndicatorSystem> indSysList;
}
