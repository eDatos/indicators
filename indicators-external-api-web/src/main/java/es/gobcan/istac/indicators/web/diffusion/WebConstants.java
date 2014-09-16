package es.gobcan.istac.indicators.web.diffusion;

public interface WebConstants {

    public String VIEW_NAME_INDEX                                                   = "/index";

    // Other
    public String VIEW_NAME_ERROR_500                                               = "errors/500";
    public String VIEW_NAME_ERROR_404                                               = "errors/404";

    // Locales
    public String LOCALE_ES                                                         = "es";
    public String LOCALE_EN                                                         = "en";

    // Configuration
    public String DATA_URL_PROPERTY                                                 = "environment.indicators.data";
    public String METAMAC_STATISTICAL_OPERATIONS_EXTERNAL_ENDPOINT_ADDRESS_PROPERTY = "indicators.ws.metamac.statistical.operations.external.endpoint";
}