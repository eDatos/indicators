package es.gobcan.istac.indicators.rest.types;

import java.io.Serializable;

/**
 * Enum for QuantityUnitSymbolPositionEnum
 * 
 * @see es.gobcan.istac.indicators.core.enume.domain.QuantityUnitSymbolPositionEnum
 */
public enum QuantityUnitSymbolPositionEnum implements Serializable {
    START, END;

    private QuantityUnitSymbolPositionEnum() {
    }

    public String getName() {
        return name();
    }
}
