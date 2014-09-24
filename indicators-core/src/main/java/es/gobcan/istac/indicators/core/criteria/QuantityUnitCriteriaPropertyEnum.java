package es.gobcan.istac.indicators.core.criteria;

import java.io.Serializable;

public enum QuantityUnitCriteriaPropertyEnum implements Serializable {

    UUID, SYMBOL_POSITION, TITLE;

    private QuantityUnitCriteriaPropertyEnum() {
    }

    public String value() {
        return name();
    }

    public static QuantityUnitCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }
}