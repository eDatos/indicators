package es.gobcan.istac.indicators.web.server;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.sso.client.MetamacPrincipal;
import org.siemac.metamac.sso.client.MetamacPrincipalAccess;
import org.siemac.metamac.sso.client.SsoClientConstants;

import es.gobcan.istac.indicators.core.constants.IndicatorsConstants;
import es.gobcan.istac.indicators.core.security.RoleEnum;

public class ServiceContextHelper {

    private static ServiceContext serviceContext;

    public static ServiceContext getServiceContext() {
        if (serviceContext == null) {
            
            serviceContext = new ServiceContext("user", "12345", IndicatorsConstants.SECURITY_APPLICATION_ID);
            
            MetamacPrincipal metamacPrincipal = new MetamacPrincipal();
            metamacPrincipal.setUserId(serviceContext.getUserId());
            metamacPrincipal.getAccesses().add(new MetamacPrincipalAccess(RoleEnum.ADMINISTRADOR.getName(), IndicatorsConstants.SECURITY_APPLICATION_ID, null));
            serviceContext.setProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE, metamacPrincipal);
            
        }
        return serviceContext;
    }

}
