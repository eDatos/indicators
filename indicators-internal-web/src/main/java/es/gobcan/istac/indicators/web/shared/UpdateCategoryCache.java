package es.gobcan.istac.indicators.web.shared;

import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.Optional;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class UpdateCategoryCache {

    @Out(1)
    @Optional
    MetamacWebException notificationException;
}
