package es.gobcan.istac.indicators.web.client.enums;

public enum QuantityIndexBaseTypeEnum {

    BASE_VALUE, BASE_TIME, BASE_LOCATION;

    private QuantityIndexBaseTypeEnum() {
    }

    public String getName() {
        return name();
    }

}
