package es.gobcan.istac.indicators.core.security;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.sso.client.MetamacPrincipal;
import org.siemac.metamac.sso.client.MetamacPrincipalAccess;
import org.siemac.metamac.sso.client.SsoClientConstants;

import es.gobcan.istac.indicators.core.constants.IndicatorsConstants;
import es.gobcan.istac.indicators.core.enume.domain.RoleEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

public class SecurityUtils {

    // TODO poner en librería común algunos métodos?
    // TODO refactor  --> poner *Apoyo*

    /**
     * Checks user can execute any operation, if has any role of requested roles
     */
    public static void checkServiceOperationAllowed(ServiceContext ctx, RoleEnum... roles) throws MetamacException {

        MetamacPrincipal metamacPrincipal = getMetamacPrincipal(ctx);

        // Administration has total control
        if (isAdministrator(metamacPrincipal)) {
            return;
        }
        // Checks user has any role of requested
        if (roles != null) {
            for (int i = 0; i < roles.length; i++) {
                RoleEnum role = roles[i];
                if (isUserInRol(metamacPrincipal, role)) {
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
        if (isAdministrator(metamacPrincipal)) {
            return;
        }
        // Checks indicators system is in any role 
        if (roles != null) {
            for (int i = 0; i < roles.length; i++) {
                RoleEnum role = roles[i];
                if (haveAccessToOperationInRol(metamacPrincipal, role, operationCode)) {
                    return;
                }
            }
        }
        throw new MetamacException(ServiceExceptionType.SECURITY_ACCESS_INDICATORS_SYSTEM_NOT_ALLOWED, operationCode, metamacPrincipal.getUserId());
    }

    /**
     * Checks user has any rol
     */
    private static boolean isUserInRol(MetamacPrincipal metamacPrincipal, RoleEnum role) throws MetamacException {

        switch (role) {
            case ADMINISTRADOR:
                return isAdministrator(metamacPrincipal);
            case ANY_ROLE_ALLOWED:
                return isAnyIndicatorsRole(metamacPrincipal);
            case TECNICO_SISTEMA_INDICADORES:
                return isTsi(metamacPrincipal);
            case TECNICO_PRODUCCION:
                return isTp(metamacPrincipal);
            case TECNICO_AYUDA_PRODUCCION:
                return isTap(metamacPrincipal);
            case TECNICO_DIFUSION:
                return isTd(metamacPrincipal);
            case TECNICO_AYUDA_DIFUSION:
                return isTad(metamacPrincipal);
            default:
                throw new MetamacException(ServiceExceptionType.UNKNOWN, "Operation not supported in security checker: " + role);
        }
    }
    
    /**
     * Checks if user has access to an operation. To have access, any access must exists to specified rol and operation, or has any access with 
     * role and operation with 'null' value
     */
    private static boolean haveAccessToOperationInRol(MetamacPrincipal metamacPrincipal, RoleEnum role, String operation) throws MetamacException {
        for (MetamacPrincipalAccess metamacPrincipalAccess : metamacPrincipal.getAccesses()) {
            if (IndicatorsConstants.SECURITY_APPLICATION_ID.equals(metamacPrincipalAccess.getApplication()) && metamacPrincipalAccess.getRole().equals(role.name())) {
                if (metamacPrincipalAccess.getOperation() == null || metamacPrincipalAccess.getOperation().equals(operation)) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Retrieves MetamacPrincipal in ServiceContext
     */
    private static MetamacPrincipal getMetamacPrincipal(ServiceContext ctx) throws MetamacException {
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

    /**
     * Checks if user has access with role
     */
    private static Boolean isRoleInAccesses(MetamacPrincipal metamacPrincipal, RoleEnum role) {
        for (MetamacPrincipalAccess metamacPrincipalAccess : metamacPrincipal.getAccesses()) {
            if (IndicatorsConstants.SECURITY_APPLICATION_ID.equals(metamacPrincipalAccess.getApplication()) && metamacPrincipalAccess.getRole().equals(role.name())) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    private static Boolean isAnyIndicatorsRole(MetamacPrincipal metamacPrincipal) {
        return isAdministrator(metamacPrincipal) || isTsi(metamacPrincipal) || isTap(metamacPrincipal) || isTp(metamacPrincipal) || isTad(metamacPrincipal) || isTd(metamacPrincipal);
    }

    private static Boolean isAdministrator(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, RoleEnum.ADMINISTRADOR);
    }

    private static Boolean isTsi(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, RoleEnum.TECNICO_SISTEMA_INDICADORES);
    }

    private static Boolean isTap(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, RoleEnum.TECNICO_AYUDA_PRODUCCION);
    }

    private static Boolean isTp(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, RoleEnum.TECNICO_PRODUCCION);
    }

    private static Boolean isTad(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, RoleEnum.TECNICO_AYUDA_DIFUSION);
    }

    private static Boolean isTd(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, RoleEnum.TECNICO_DIFUSION);
    }
}