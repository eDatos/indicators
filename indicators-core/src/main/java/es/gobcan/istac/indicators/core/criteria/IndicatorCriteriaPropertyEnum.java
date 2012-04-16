package es.gobcan.istac.indicators.core.criteria;

public enum IndicatorCriteriaPropertyEnum  {

    CODE,
    SUBJECT_CODE;
    
    public String value() {
        return name();
    }
    
    public static IndicatorCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }
}