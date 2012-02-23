package es.gobcan.istac.indicators.web.shared;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

@GenDispatch(isSecure=false)
public class DeleteIndicatorInstance {
	
	@In(1)
	String indicatorInstanceUuid;
}
