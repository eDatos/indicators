package es.gobcan.istac.indicators.core.criteria;

public enum GeographicalValueCriteriaOrderEnum {

    CODE, TITLE, GEOGRAPHICAL_GRANULARITY_UUID, ORDER;

    public String value() {
        return name();
    }

    public static GeographicalValueCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }
}