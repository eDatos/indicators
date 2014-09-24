package es.gobcan.istac.indicators.core.criteria;

import java.io.Serializable;

public enum IndicatorCriteriaOrderEnum implements Serializable {

    CODE, TITLE, PRODUCTION_VERSION, PRODUCTION_PROC_STATUS, DIFFUSION_VERSION, DIFFUSION_PROC_STATUS;

    private IndicatorCriteriaOrderEnum() {
    }

    public String value() {
        return name();
    }

    public static IndicatorCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }
}