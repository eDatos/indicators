package es.gobcan.istac.indicators.web.client.enums;

public enum DataSourceQuantityType {

    INTERPERIOD_RATE, ANNUAL_RATE;

    private DataSourceQuantityType() {

    }

    public String getName() {
        return name();
    }

}
