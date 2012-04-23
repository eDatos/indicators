package es.gobcan.istac.indicators.core.util;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.sso.client.MetamacPrincipal;
import org.siemac.metamac.sso.client.MetamacPrincipalAccess;

import es.gobcan.istac.indicators.core.constants.IndicatorsConstants;
import es.gobcan.istac.indicators.core.enume.domain.RoleEnum;

public class SharedSecurityUtils {

    /**
     * Checks if user has requested role
     */
    public static boolean isUserInRol(MetamacPrincipal metamacPrincipal, RoleEnum role) throws MetamacException {

        if (RoleEnum.ANY_ROLE_ALLOWED.equals(role)) {
            return isAnyIndicatorsRole(metamacPrincipal);
        } else {
            return isRoleInAccesses(metamacPrincipal, role);
        }
    }

    /**
     * Checks if user has access to an operation. To have access, any access must exists to specified role and operation, or has any access with
     * role and operation with 'null' value
     */
    public static boolean haveAccessToOperationInRol(MetamacPrincipal metamacPrincipal, RoleEnum role, String operation) throws MetamacException {
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
     * Checks if user has access with role
     */
    public static Boolean isRoleInAccesses(MetamacPrincipal metamacPrincipal, RoleEnum role) {
        for (MetamacPrincipalAccess metamacPrincipalAccess : metamacPrincipal.getAccesses()) {
            if (IndicatorsConstants.SECURITY_APPLICATION_ID.equals(metamacPrincipalAccess.getApplication()) && metamacPrincipalAccess.getRole().equals(role.name())) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public static Boolean isAdministrator(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, RoleEnum.ADMINISTRADOR);
    }

    public static Boolean isAnyIndicatorsRole(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, RoleEnum.ADMINISTRADOR) || isRoleInAccesses(metamacPrincipal, RoleEnum.TECNICO_SISTEMA_INDICADORES)
                || isRoleInAccesses(metamacPrincipal, RoleEnum.TECNICO_PRODUCCION) || isRoleInAccesses(metamacPrincipal, RoleEnum.TECNICO_APOYO_PRODUCCION)
                || isRoleInAccesses(metamacPrincipal, RoleEnum.TECNICO_APOYO_DIFUSION) || isRoleInAccesses(metamacPrincipal, RoleEnum.TECNICO_DIFUSION);
    }

    public static Boolean isTsi(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, RoleEnum.TECNICO_SISTEMA_INDICADORES);
    }

    public static Boolean isTap(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, RoleEnum.TECNICO_APOYO_PRODUCCION);
    }

    public static Boolean isTp(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, RoleEnum.TECNICO_PRODUCCION);
    }

    public static Boolean isTad(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, RoleEnum.TECNICO_APOYO_DIFUSION);
    }

    public static Boolean isTd(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, RoleEnum.TECNICO_DIFUSION);
    }

}
