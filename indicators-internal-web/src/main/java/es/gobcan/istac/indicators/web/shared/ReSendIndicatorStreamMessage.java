package es.gobcan.istac.indicators.web.shared;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

@GenDispatch(isSecure = false)
public class ReSendIndicatorStreamMessage {

    @In(1)
    String              indicatorCode;

    @Out(1)
    IndicatorDto        indicatorDto;

    @Out(2)
    MetamacWebException notificationException;
}
