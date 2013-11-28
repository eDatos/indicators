package es.gobcan.istac.indicators.core.criteria;

public enum QuantityUnitCriteriaPropertyEnum {

    SYMBOL_POSITION, TITLE;

    public String value() {
        return name();
    }

    public static QuantityUnitCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }
}