package es.gobcan.istac.indicators.web.shared;

import org.siemac.metamac.sso.client.MetamacPrincipal;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetUserPrincipal {

    @Out(1)
    MetamacPrincipal userPrincipal;

}
