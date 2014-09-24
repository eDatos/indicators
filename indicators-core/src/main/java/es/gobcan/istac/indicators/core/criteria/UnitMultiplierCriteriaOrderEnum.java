package es.gobcan.istac.indicators.core.criteria;

import java.io.Serializable;

public enum UnitMultiplierCriteriaOrderEnum implements Serializable {

    UNIT_MULTIPLIER, TITLE;

    private UnitMultiplierCriteriaOrderEnum() {
    }

    public String value() {
        return name();
    }

    public static UnitMultiplierCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }
}