package es.gobcan.istac.indicators.core.criteria;

import java.io.Serializable;

public enum GeographicalValueCriteriaPropertyEnum implements Serializable {

    UUID, CODE, TITLE, GEOGRAPHICAL_GRANULARITY_UUID, GEOGRAPHICAL_GRANULARITY_CODE;

    private GeographicalValueCriteriaPropertyEnum() {
    }

    public String value() {
        return name();
    }
    public static GeographicalValueCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }
}