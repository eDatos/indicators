package es.gobcan.istac.indicators.web.client.enums;

public enum IndicatorCalculationTypeEnum {

    NUMERATOR, DENOMINATOR;

    private IndicatorCalculationTypeEnum() {
    }

    public String getName() {
        return name();
    }
}
