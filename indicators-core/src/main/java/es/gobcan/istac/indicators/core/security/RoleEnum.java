package es.gobcan.istac.indicators.core.security;

import java.io.Serializable;

/**
 * Enum for roles
 */
public enum RoleEnum implements Serializable {

    ADMINISTRADOR, TECNICO_SISTEMA_INDICADORES, TECNICO_AYUDA_PRODUCCION, TECNICO_PRODUCCION, TECNICO_AYUDA_DIFUSION, TECNICO_DIFUSION, ANY_ROLE_ALLOWED;

    private RoleEnum() {
    }

    public String getName() {
        return name();
    }
}
