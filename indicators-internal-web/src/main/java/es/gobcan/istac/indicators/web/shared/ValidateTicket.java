package es.gobcan.istac.indicators.web.shared;

import org.siemac.metamac.sso.client.MetamacPrincipal;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class ValidateTicket {

    @In(1)
    String           ticket;

    @In(2)
    String           serviceUrl;

    @Out(2)
    MetamacPrincipal metamacPrincipal;

}
