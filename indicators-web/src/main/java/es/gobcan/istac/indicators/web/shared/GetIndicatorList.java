package es.gobcan.istac.indicadores.web.shared;

import java.util.List;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicadores.web.shared.db.Indicator;

@GenDispatch(isSecure=false)
public class GetIndicatorList {

	@Out(1)
	List<Indicator> indicatorList;
}
