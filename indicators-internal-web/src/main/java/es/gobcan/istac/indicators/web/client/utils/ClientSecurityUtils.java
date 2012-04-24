package es.gobcan.istac.indicators.web.client.utils;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.sso.client.MetamacPrincipal;

import es.gobcan.istac.indicators.core.enume.domain.RoleEnum;
import es.gobcan.istac.indicators.core.util.SharedSecurityUtils;
import es.gobcan.istac.indicators.web.client.IndicatorsWeb;

public class ClientSecurityUtils {

    // INDICATORS SYSTEM

    public static boolean canSendIndicatorsSystemToProductionValidation(String operationCode) {
        if (isRoleAllowed(RoleEnum.TECNICO_SISTEMA_INDICADORES) && isIndicatorsSystemAllowed(operationCode, RoleEnum.TECNICO_SISTEMA_INDICADORES)) {
            return true;
        }
        return false;
    }

    public static boolean canSendIndicatorsSystemToDiffusionValidation(String operationCode) {
        if (isRoleAllowed(RoleEnum.TECNICO_SISTEMA_INDICADORES) && isIndicatorsSystemAllowed(operationCode, RoleEnum.TECNICO_SISTEMA_INDICADORES)) {
            return true;
        }
        return false;
    }

    public static boolean canRejectIndicatorsSystemProductionValidation(String operationCode) {
        if (isRoleAllowed(RoleEnum.TECNICO_PRODUCCION) && isIndicatorsSystemAllowed(operationCode, RoleEnum.TECNICO_PRODUCCION)) {
            return true;
        }
        return false;
    }

    public static boolean canRejectIndicatorsSystemDiffusionValidation(String operationCode) {
        if (isRoleAllowed(RoleEnum.TECNICO_DIFUSION) && isIndicatorsSystemAllowed(operationCode, RoleEnum.TECNICO_DIFUSION)) {
            return true;
        }
        return false;
    }

    public static boolean canPublishIndicatorsSystem(String operationCode) {
        if (isRoleAllowed(RoleEnum.TECNICO_DIFUSION, RoleEnum.TECNICO_APOYO_DIFUSION) && isIndicatorsSystemAllowed(operationCode, RoleEnum.TECNICO_DIFUSION, RoleEnum.TECNICO_APOYO_DIFUSION)) {
            return true;
        }
        return false;
    }

    public static boolean canArchiveIndicatorsSystem(String operationCode) {
        if (isRoleAllowed(RoleEnum.TECNICO_DIFUSION) && isIndicatorsSystemAllowed(operationCode, RoleEnum.TECNICO_DIFUSION)) {
            return true;
        }
        return false;
    }

    public static boolean canVersioningIndicatorsSystem(String operationCode) {
        if (isRoleAllowed(RoleEnum.TECNICO_SISTEMA_INDICADORES) && isIndicatorsSystemAllowed(operationCode, RoleEnum.TECNICO_SISTEMA_INDICADORES)) {
            return true;
        }
        return false;
    }

    // STRUCTURE

    public static boolean canEditStructure(String operationCode) {
        // Edit an structure includes: create and delete dimensions and instances
        if (isRoleAllowed(RoleEnum.TECNICO_SISTEMA_INDICADORES) && isIndicatorsSystemAllowed(operationCode, RoleEnum.TECNICO_SISTEMA_INDICADORES)) {
            return true;
        }
        return false;
    }
    
    public static boolean canEditDimension(String operationCode) {
        if (isRoleAllowed(RoleEnum.TECNICO_SISTEMA_INDICADORES) && isIndicatorsSystemAllowed(operationCode, RoleEnum.TECNICO_SISTEMA_INDICADORES)) {
            return true;
        }
        return false;
    }
    
    public static boolean canDropNode(String operationCode) {
        // Make changes in structure (dimension and instance nodes)
        if (isRoleAllowed(RoleEnum.TECNICO_SISTEMA_INDICADORES) && isIndicatorsSystemAllowed(operationCode, RoleEnum.TECNICO_SISTEMA_INDICADORES)) {
            return true;
        }
        return false;
    }

    // INDICATORS

    public static boolean canCreateIndicator() {
        if (isRoleAllowed(RoleEnum.TECNICO_PRODUCCION, RoleEnum.TECNICO_APOYO_PRODUCCION)) {
            return true;
        }
        return false;
    }

    public static boolean canEditIndicator() {
        if (isRoleAllowed(RoleEnum.TECNICO_PRODUCCION, RoleEnum.TECNICO_APOYO_PRODUCCION)) {
            return true;
        }
        return false;
    }

    public static boolean canDeleteIndicator() {
        if (isRoleAllowed(RoleEnum.TECNICO_PRODUCCION, RoleEnum.TECNICO_APOYO_PRODUCCION)) {
            return true;
        }
        return false;
    }

    public static boolean canSendIndicatorToProductionValidation() {
        if (isRoleAllowed(RoleEnum.TECNICO_PRODUCCION, RoleEnum.TECNICO_APOYO_PRODUCCION)) {
            return true;
        }
        return false;
    }

    public static boolean canSendIndicatorToDiffusionValidation() {
        if (isRoleAllowed(RoleEnum.TECNICO_PRODUCCION)) {
            return true;
        }
        return false;
    }

    public static boolean canRejectIndicatorProductionValidation() {
        if (isRoleAllowed(RoleEnum.TECNICO_PRODUCCION)) {
            return true;
        }
        return false;
    }

    public static boolean canRejectIndicatorDiffusionValidation() {
        if (isRoleAllowed(RoleEnum.TECNICO_DIFUSION)) {
            return true;
        }
        return false;
    }

    public static boolean canPublishIndicator() {
        if (isRoleAllowed(RoleEnum.TECNICO_DIFUSION, RoleEnum.TECNICO_APOYO_DIFUSION)) {
            return true;
        }
        return false;
    }

    public static boolean canArchiveIndicator() {
        if (isRoleAllowed(RoleEnum.TECNICO_DIFUSION)) {
            return true;
        }
        return false;
    }

    public static boolean canVersioningIndicator() {
        if (isRoleAllowed(RoleEnum.TECNICO_PRODUCCION, RoleEnum.TECNICO_APOYO_PRODUCCION)) {
            return true;
        }
        return false;
    }

    // DATA SOURCES

    public static boolean canCreateDataSource() {
        if (isRoleAllowed(RoleEnum.TECNICO_PRODUCCION, RoleEnum.TECNICO_APOYO_PRODUCCION)) {
            return true;
        }
        return false;
    }

    public static boolean canDeleteDataSource() {
        if (isRoleAllowed(RoleEnum.TECNICO_PRODUCCION, RoleEnum.TECNICO_APOYO_PRODUCCION)) {
            return true;
        }
        return false;
    }

    public static boolean canEditDataSource() {
        if (isRoleAllowed(RoleEnum.TECNICO_PRODUCCION, RoleEnum.TECNICO_APOYO_PRODUCCION)) {
            return true;
        }
        return false;
    }

    /**
     * Checks if logged user has one of the allowed roles
     * 
     * @param roles
     * @return
     */
    private static boolean isRoleAllowed(RoleEnum... roles) {
        MetamacPrincipal userPrincipal = IndicatorsWeb.getUserPrincipal();
        // Administration has total control
        if (SharedSecurityUtils.isAdministrator(userPrincipal)) {
            return true;
        }
        // Checks user has any role of requested
        if (roles != null) {
            for (int i = 0; i < roles.length; i++) {
                RoleEnum role = roles[i];
                if (SharedSecurityUtils.isUserInRol(userPrincipal, role)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if logged user has access to a indicators system with one of the selected roles
     * 
     * @param operationCode
     * @param roles
     * @return
     * @throws MetamacException
     */
    private static boolean isIndicatorsSystemAllowed(String operationCode, RoleEnum... roles) {
        MetamacPrincipal userPrincipal = IndicatorsWeb.getUserPrincipal();
        // Administration has total control in all indicators systems
        if (SharedSecurityUtils.isAdministrator(userPrincipal)) {
            return true;
        }
        // Checks if indicators system is in some role
        if (roles != null) {
            for (int i = 0; i < roles.length; i++) {
                RoleEnum role = roles[i];
                if (SharedSecurityUtils.haveAccessToOperationInRol(userPrincipal, role, operationCode)) {
                    return true;
                }
            }
        }
        return false;
    }

}
