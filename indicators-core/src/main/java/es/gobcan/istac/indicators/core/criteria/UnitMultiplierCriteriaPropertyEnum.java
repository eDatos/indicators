package es.gobcan.istac.indicators.core.criteria;

import java.io.Serializable;

public enum UnitMultiplierCriteriaPropertyEnum implements Serializable {

    UNIT_MULTIPLIER, TITLE;

    private UnitMultiplierCriteriaPropertyEnum() {
    }

    public String value() {
        return name();
    }

    public static UnitMultiplierCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }
}