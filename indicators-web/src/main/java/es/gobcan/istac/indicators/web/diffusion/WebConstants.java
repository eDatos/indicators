package es.gobcan.istac.indicators.web.diffusion;

public interface WebConstants {

    public String VIEW_NAME_INDEX                                                   = "/index";

    // Indicators systems
    public String VIEW_NAME_INDICATORS_SYSTEMS_LIST                                 = "indicators-systems/indicators-systems";
    public String VIEW_NAME_INDICATORS_SYSTEM_VIEW                                  = "indicators-systems/indicators-system";

    // Indicators
    public String VIEW_NAME_INDICATORS_LIST                                         = "indicators/indicators";

    // Other
    public String VIEW_NAME_ERROR_500                                               = "errors/500";
    public String VIEW_NAME_ERROR_404                                               = "errors/404";

    // Locales
    public String LOCALE_ES                                                         = "es";
    public String LOCALE_EN                                                         = "en";

    // Configuration
    public String DATA_URL_PROPERTY                                                 = "environment.indicators.data";
    public String METAMAC_STATISTICAL_OPERATIONS_EXTERNAL_ENDPOINT_ADDRESS_PROPERTY = "indicators.ws.metamac.statistical.operations.external.endpoint";
    public String JAXI_URL_PROPERTY                                                 = "indicators.jaxi.remote.url";
    public String WIDGETS_TYPE_LIST_URL_PROPERTY                                    = "indicators.widgets.typelist.url";
    public String WIDGETS_QUERY_TOOLS_URL_PROPERTY                                  = "indicators.querytools.url";
    public String WIDGETS_SPARKLINE_MAX                                             = "indicators.widgets.sparkline.max";
}