package es.gobcan.istac.indicators.core.criteria;

public enum QuantityUnitCriteriaOrderEnum {

    TITLE;

    public String value() {
        return name();
    }

    public static QuantityUnitCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }
}