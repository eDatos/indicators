package es.gobcan.istac.indicators.web.client.enums;

public enum EnvironmentTypeEnum {

    PRODUCTION, DIFFUSION;

    private EnvironmentTypeEnum() {
    }

    public String getName() {
        return name();
    }
}
