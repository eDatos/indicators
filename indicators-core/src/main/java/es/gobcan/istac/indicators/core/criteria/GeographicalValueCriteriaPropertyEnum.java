package es.gobcan.istac.indicators.core.criteria;

public enum GeographicalValueCriteriaPropertyEnum {

    UUID, CODE, TITLE, GEOGRAPHICAL_GRANULARITY_UUID, GEOGRAPHICAL_GRANULARITY_CODE;

    public String value() {
        return name();
    }
    public static GeographicalValueCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }
}