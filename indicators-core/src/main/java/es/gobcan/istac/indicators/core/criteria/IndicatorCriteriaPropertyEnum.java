package es.gobcan.istac.indicators.core.criteria;

public enum IndicatorCriteriaPropertyEnum {

    CODE, SUBJECT_CODE, TITLE, PRODUCTION_PROC_STATUS, DIFFUSION_PROC_STATUS;

    public String value() {
        return name();
    }

    public static IndicatorCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }
}