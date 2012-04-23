package es.gobcan.istac.indicators.core.security;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.sso.client.MetamacPrincipal;
import org.siemac.metamac.sso.client.SsoClientConstants;

import es.gobcan.istac.indicators.core.enume.domain.RoleEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.util.SharedSecurityUtils;

public class SecurityUtils {

    /**
     * Checks user can execute any operation, if has any role of requested roles
     */
    public static void checkServiceOperationAllowed(ServiceContext ctx, RoleEnum... roles) throws MetamacException {

        MetamacPrincipal metamacPrincipal = getMetamacPrincipal(ctx);

        // Administration has total control
        if (SharedSecurityUtils.isAdministrator(metamacPrincipal)) {
            return;
        }
        // Checks user has any role of requested
        if (roles != null) {
            for (int i = 0; i < roles.length; i++) {
                RoleEnum role = roles[i];
                if (SharedSecurityUtils.isUserInRol(metamacPrincipal, role)) {
                    return;
                }
            }
        }
        throw new MetamacException(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED, metamacPrincipal.getUserId());
    }

    /**
     * Checks user has access to an operation or indicators system. To have access, user must have this indicators system in any role of requested roles
     */
    public static void checkResourceIndicatorsSystemAllowed(ServiceContext ctx, String operationCode, RoleEnum... roles) throws MetamacException {

        MetamacPrincipal metamacPrincipal = getMetamacPrincipal(ctx);

        // Administration has total control in all indicators systems
        if (SharedSecurityUtils.isAdministrator(metamacPrincipal)) {
            return;
        }
        // Checks indicators system is in any role
        if (roles != null) {
            for (int i = 0; i < roles.length; i++) {
                RoleEnum role = roles[i];
                if (SharedSecurityUtils.haveAccessToOperationInRol(metamacPrincipal, role, operationCode)) {
                    return;
                }
            }
        }
        throw new MetamacException(ServiceExceptionType.SECURITY_ACCESS_INDICATORS_SYSTEM_NOT_ALLOWED, operationCode, metamacPrincipal.getUserId());
    }

    /**
     * Retrieves MetamacPrincipal in ServiceContext
     */
    public static MetamacPrincipal getMetamacPrincipal(ServiceContext ctx) throws MetamacException {
        Object principalProperty = ctx.getProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE);
        if (principalProperty == null) {
            throw new MetamacException(ServiceExceptionType.SECURITY_PRINCIPAL_NOT_FOUND);
        }
        MetamacPrincipal metamacPrincipal = (MetamacPrincipal) principalProperty;
        if (!metamacPrincipal.getUserId().equals(ctx.getUserId())) {
            throw new MetamacException(ServiceExceptionType.SECURITY_PRINCIPAL_NOT_FOUND);
        }
        return metamacPrincipal;
    }

}