package es.gobcan.istac.indicators.core.criteria;

import java.io.Serializable;

public enum GeographicalGranularityCriteriaPropertyEnum implements Serializable {

    CODE, TITLE;

    private GeographicalGranularityCriteriaPropertyEnum() {
    }

    public String value() {
        return name();
    }

    public static GeographicalGranularityCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }
}