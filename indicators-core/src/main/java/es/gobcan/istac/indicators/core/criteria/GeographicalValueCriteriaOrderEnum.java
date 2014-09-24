package es.gobcan.istac.indicators.core.criteria;

public enum GeographicalValueCriteriaOrderEnum {

    UUID, CODE, TITLE, GEOGRAPHICAL_GRANULARITY_UUID, GEOGRAPHICAL_GRANULARITY_TITLE, ORDER;

    private GeographicalValueCriteriaOrderEnum() {
    }

    public String value() {
        return name();
    }

    public static GeographicalValueCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }
}