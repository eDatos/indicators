package es.gobcan.istac.indicators.web.client.utils;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.sso.client.MetamacPrincipal;

import es.gobcan.istac.indicators.core.enume.domain.RoleEnum;
import es.gobcan.istac.indicators.core.util.shared.SharedSecurityUtils;
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
        return isRoleAllowed(RoleEnum.ADMINISTRADOR);
    }

    public static boolean canRejectIndicatorsSystemProductionValidation(String operationCode) {
        return isRoleAllowed(RoleEnum.ADMINISTRADOR);
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

    public static boolean canDeleteIndicatorsSystem() {
        // It is necessary to check if user can delete an specific indicators system, but users can delete many systems simultaneously, so we skip this validation (an exception will be thrown in the
        // service facade
        if (isRoleAllowed(RoleEnum.TECNICO_SISTEMA_INDICADORES)) {
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

    public static boolean canEditIndicatorInstance(String operationCode) {
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

    public static boolean canPopulateIndicatorData() {
        return SharedSecurityUtils.canPopulateIndicatorData(IndicatorsWeb.getCurrentUser());
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
        MetamacPrincipal userPrincipal = IndicatorsWeb.getCurrentUser();
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
        MetamacPrincipal userPrincipal = IndicatorsWeb.getCurrentUser();
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

    public static boolean canCreateQuantityUnit() {
        return isRoleAllowed(RoleEnum.ADMINISTRADOR);
    }

    public static boolean canDeleteQuantityUnit() {
        return isRoleAllowed(RoleEnum.ADMINISTRADOR);
    }

    public static boolean canEditQuantityUnit() {
        return isRoleAllowed(RoleEnum.ADMINISTRADOR);
    }

    public static boolean canAdministrate() {
        return isRoleAllowed(RoleEnum.ADMINISTRADOR);
    }

    public static boolean canCreateGeographicalGranularity() {
        return isRoleAllowed(RoleEnum.ADMINISTRADOR);
    }

    public static boolean canDeleteGeoGranularity() {
        return isRoleAllowed(RoleEnum.ADMINISTRADOR);
    }

    public static boolean canEditGeographicalGranularity() {
        return isRoleAllowed(RoleEnum.ADMINISTRADOR);
    }

    public static boolean canCreateUnitMultiplier() {
        return isRoleAllowed(RoleEnum.ADMINISTRADOR);
    }

    public static boolean canEditUnitMultiplier() {
        return isRoleAllowed(RoleEnum.ADMINISTRADOR);
    }

    public static boolean canDeleteUnitMultiplier() {
        return isRoleAllowed(RoleEnum.ADMINISTRADOR);
    }

    public static boolean canCreateGeographicalValue() {
        return isRoleAllowed(RoleEnum.ADMINISTRADOR);
    }

    public static boolean canEditGeographicalValue() {
        return isRoleAllowed(RoleEnum.ADMINISTRADOR);
    }

    public static boolean canDeleteGeoValue() {
        return isRoleAllowed(RoleEnum.ADMINISTRADOR);
    }

    public static boolean canEnableNotifyPopulationErrors() {
        return isRoleAllowed(RoleEnum.ADMINISTRADOR);
    }

    public static boolean canDisableNotifyPopulationErrors() {
        return isRoleAllowed(RoleEnum.ADMINISTRADOR);
    }

}
