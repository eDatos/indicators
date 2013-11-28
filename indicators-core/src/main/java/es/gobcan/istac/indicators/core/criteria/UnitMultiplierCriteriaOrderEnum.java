package es.gobcan.istac.indicators.core.criteria;

public enum UnitMultiplierCriteriaOrderEnum {

    UNIT_MULTIPLIER, TITLE;

    public String value() {
        return name();
    }

    public static UnitMultiplierCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }
}