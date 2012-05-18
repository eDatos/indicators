package es.gobcan.istac.indicators.core.criteria;

public enum GeographicalValueCriteriaOrderEnum {

    GRANULARITY,
    ORDER_IN_GRANULARITY;

    public String value() {
        return name();
    }

    public static GeographicalValueCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }
}