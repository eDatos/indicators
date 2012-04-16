package es.gobcan.istac.indicators.core.criteria;

public enum IndicatorsSystemCriteriaPropertyEnum {

    CODE;

    public String value() {
        return name();
    }

    public static IndicatorsSystemCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }
}