package es.gobcan.istac.indicators.core.criteria;

public enum GeographicalValueCriteriaPropertyEnum  {

    GEOGRAPHICAL_GRANULARITY_UUID;
    
    public String value() {
        return name();
    }
    public static GeographicalValueCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }
}