package es.gobcan.istac.indicators.web.client.enums;

public enum TimeSelectionTypeEnum {

    GRANULARITY, VALUE;

    private TimeSelectionTypeEnum() {

    }

    public String getName() {
        return name();
    }

}
