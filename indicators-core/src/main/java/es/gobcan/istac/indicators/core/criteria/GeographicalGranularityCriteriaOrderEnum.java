package es.gobcan.istac.indicators.core.criteria;

public enum GeographicalGranularityCriteriaOrderEnum {

    CODE, TITLE;

    public String value() {
        return name();
    }

    public static GeographicalGranularityCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }
}