package es.gobcan.istac.indicators.core.criteria;

import java.io.Serializable;

public enum GeographicalGranularityCriteriaOrderEnum implements Serializable {

    CODE, TITLE;

    private GeographicalGranularityCriteriaOrderEnum() {
    }

    public String value() {
        return name();
    }

    public static GeographicalGranularityCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }
}