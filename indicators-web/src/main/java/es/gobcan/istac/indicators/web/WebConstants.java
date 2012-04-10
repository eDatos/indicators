package es.gobcan.istac.indicators.web;

public interface WebConstants {

    public String VIEW_NAME_INDEX                   = "/index";

    // Indicators systems
    public String VIEW_NAME_INDICATORS_SYSTEMS_LIST = "indicators-systems/indicators-systems";

    // Indicators
    public String VIEW_NAME_INDICATORS_LIST         = "indicators/indicators";

    // Other
    public String VIEW_NAME_ERROR_500               = "errors/500";
    public String VIEW_NAME_ERROR_404               = "errors/404";

    // Locales
    public String LOCALE_ES                         = "es";
    public String LOCALE_EN                         = "en";
    
    // Configuration
    public String METAMAC_STATISTICAL_OPERATIONS_INTERNAL_ENDPOINT_ADDRESS_PROPERTY = "indicators.ws.metamac.statistical.operations.internal.endpoint";
}