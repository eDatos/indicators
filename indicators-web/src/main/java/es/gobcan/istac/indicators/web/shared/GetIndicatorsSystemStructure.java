package es.gobcan.istac.indicators.web.shared;

import java.util.List;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.web.shared.db.IndicatorSystemContent;

@GenDispatch(isSecure=false)
public class GetIndicatorsSystemStructure {

	@In(1)
	Long indSysId;
	
	@Out(1)
	List<IndicatorSystemContent> listContent;
}
