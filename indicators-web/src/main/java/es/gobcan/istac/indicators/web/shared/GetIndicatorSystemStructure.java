package es.gobcan.istac.indicadores.web.shared;

import java.util.List;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicadores.web.shared.db.IndicatorSystemContent;

@GenDispatch(isSecure=false)
public class GetIndicatorSystemStructure {

	@In(1)
	Long indSysId;
	
	@Out(1)
	List<IndicatorSystemContent> listContent;
}
