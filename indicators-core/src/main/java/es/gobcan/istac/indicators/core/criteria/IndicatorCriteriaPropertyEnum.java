package es.gobcan.istac.indicators.core.criteria;

import java.io.Serializable;

public enum IndicatorCriteriaPropertyEnum implements Serializable {

    CODE, SUBJECT_CODE, TITLE, PRODUCTION_PROC_STATUS, DIFFUSION_PROC_STATUS;

    private IndicatorCriteriaPropertyEnum() {
    }

    public String value() {
        return name();
    }

    public static IndicatorCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }
}