package es.gobcan.istac.indicators.web.shared;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetIndicatorInstancePreviewUrl {

    @In(1)
    String indicatorInstanceCode;

    @In(2)
    String indicatorsSystemCode;

    @Out(1)
    String url;

}
