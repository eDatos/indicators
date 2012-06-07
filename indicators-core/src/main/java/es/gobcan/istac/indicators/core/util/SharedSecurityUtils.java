package es.gobcan.istac.indicators.core.util;

import org.siemac.metamac.sso.client.MetamacPrincipal;
import org.siemac.metamac.sso.client.MetamacPrincipalAccess;

import es.gobcan.istac.indicators.core.constants.IndicatorsConstants;
import es.gobcan.istac.indicators.core.enume.domain.RoleEnum;

public class SharedSecurityUtils {

    /**
     * Checks if user has requested role
     */
    public static boolean isUserInRol(MetamacPrincipal metamacPrincipal, RoleEnum role) {
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
    public static boolean haveAccessToOperationInRol(MetamacPrincipal metamacPrincipal, RoleEnum role, String operation) {
        for (MetamacPrincipalAccess metamacPrincipalAccess : metamacPrincipal.getAccesses()) {
            if (IndicatorsConstants.SECURITY_APPLICATION_ID.equals(metamacPrincipalAccess.getApplication()) && metamacPrincipalAccess.getRole().equals(role.name())) {
                if (metamacPrincipalAccess.getOperation() == null || metamacPrincipalAccess.getOperation().equals(operation)) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }

    public static Boolean isAdministrator(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, RoleEnum.ADMINISTRADOR);
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

    public static Boolean isAnyIndicatorsRole(MetamacPrincipal metamacPrincipal) {
        return isAdministrator(metamacPrincipal) || isTecnicoSistemaIndicadores(metamacPrincipal) || isTecnicoProduccion(metamacPrincipal) || isTecnicoApoyoProduccion(metamacPrincipal)
                || isTecnicoDifusion(metamacPrincipal) || isTecnicoApoyoDifusion(metamacPrincipal);
    }

    public static Boolean isTecnicoSistemaIndicadores(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, RoleEnum.TECNICO_SISTEMA_INDICADORES);
    }

    public static Boolean isTecnicoApoyoProduccion(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, RoleEnum.TECNICO_APOYO_PRODUCCION);
    }

    public static Boolean isTecnicoProduccion(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, RoleEnum.TECNICO_PRODUCCION);
    }

    public static Boolean isTecnicoApoyoDifusion(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, RoleEnum.TECNICO_APOYO_DIFUSION);
    }

    public static Boolean isTecnicoDifusion(MetamacPrincipal metamacPrincipal) {
        return isRoleInAccesses(metamacPrincipal, RoleEnum.TECNICO_DIFUSION);
    }
}
