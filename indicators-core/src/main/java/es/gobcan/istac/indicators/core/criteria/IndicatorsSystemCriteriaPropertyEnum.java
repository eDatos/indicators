package es.gobcan.istac.indicators.core.criteria;

import java.io.Serializable;

public enum IndicatorsSystemCriteriaPropertyEnum implements Serializable {

    CODE;

    private IndicatorsSystemCriteriaPropertyEnum() {
    }

    public String value() {
        return name();
    }

    public static IndicatorsSystemCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }
}