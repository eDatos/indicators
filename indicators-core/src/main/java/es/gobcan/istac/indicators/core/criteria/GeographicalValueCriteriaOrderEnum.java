package es.gobcan.istac.indicators.core.criteria;

public enum GeographicalValueCriteriaOrderEnum {

    GEOGRAPHICAL_GRANULARITY_UUID,
    ORDER_IN_GEOGRAPHICAL_GRANULARITY;

    public String value() {
        return name();
    }

    public static GeographicalValueCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }
}