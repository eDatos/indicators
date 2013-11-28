package es.gobcan.istac.indicators.core.criteria;

public enum GeographicalGranularityCriteriaPropertyEnum {

    CODE, TITLE;

    public String value() {
        return name();
    }

    public static GeographicalGranularityCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }
}