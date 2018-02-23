package es.gobcan.istac.indicators.rest.types;

import java.io.Serializable;

/**
 * Enum for QuantityTypeEnum
 * 
 * @see es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum
 */
public enum QuantityTypeEnum implements Serializable {
    QUANTITY, AMOUNT, MAGNITUDE, FRACTION, RATIO, RATE, INDEX, CHANGE_RATE;

    private QuantityTypeEnum() {
    }

    public String getName() {
        return name();
    }
}
