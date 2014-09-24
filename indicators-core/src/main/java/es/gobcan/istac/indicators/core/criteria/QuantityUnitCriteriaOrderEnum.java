package es.gobcan.istac.indicators.core.criteria;

import java.io.Serializable;

public enum QuantityUnitCriteriaOrderEnum implements Serializable {

    UUID, TITLE;

    private QuantityUnitCriteriaOrderEnum() {
    }

    public String value() {
        return name();
    }

    public static QuantityUnitCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }
}