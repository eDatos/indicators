package es.gobcan.istac.indicators.web.client.enums;

public enum RateDerivationTypeEnum {

    ANNUAL_PERCENTAGE_RATE_TYPE, INTERPERIOD_PERCENTAGE_RATE_TYPE, ANNUAL_PUNTUAL_RATE_TYPE, INTERPERIOD_PUNTUAL_RATE_TYPE;

    private RateDerivationTypeEnum() {
    }

    public String getName() {
        return name();
    }

}
