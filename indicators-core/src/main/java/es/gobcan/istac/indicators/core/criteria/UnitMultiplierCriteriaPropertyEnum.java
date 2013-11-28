package es.gobcan.istac.indicators.core.criteria;

public enum UnitMultiplierCriteriaPropertyEnum {

    UNIT_MULTIPLIER, TITLE;

    public String value() {
        return name();
    }

    public static UnitMultiplierCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }
}