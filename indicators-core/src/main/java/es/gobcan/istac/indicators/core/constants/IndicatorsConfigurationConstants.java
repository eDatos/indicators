package es.gobcan.istac.indicators.core.constants;

import org.siemac.metamac.core.common.constants.shared.ConfigurationConstants;

public class IndicatorsConfigurationConstants extends ConfigurationConstants {

    public static final String DATA_URL                                      = "environment.indicators.data";

    public static final String DB_INDICATORS_URL                             = "indicators.core.db.url";
    public static final String DB_INDICATORS_USERNAME                        = "indicators.core.db.username";
    public static final String DB_INDICATORS_PASSWORD                        = "indicators.core.db.password";
    public static final String DB_INDICATORS_DRIVER_NAME                     = "indicators.core.db.driver_name";

    public static final String DB_REPO_URL                                   = "indicators.dsrepo.db.url";
    public static final String DB_REPO_USERNAME                              = "indicators.dsrepo.db.username";
    public static final String DB_REPO_PASSWORD                              = "indicators.dsrepo.db.password";
    public static final String DB_REPO_DRIVER_NAME                           = "indicators.dsrepo.db.driver_name";

    // DB
    public static final String DB_DATA_VIEWS_ROLE                            = "indicators.bbbd.data_views_role";

    // JAXI
    public static final String JAXI_LOCAL_URL                                = "indicators.jaxi.local.url";

    // WIDGETS
    public static final String WIDGETS_TYPE_LIST_URL                         = "indicators.widgets.typelist.url";
    public static final String WIDGETS_QUERY_TOOLS_URL                       = "indicators.querytools.url";
    public static final String WIDGETS_SPARKLINE_MAX                         = "indicators.widgets.sparkline.max";
    public static final String WIDGETS_OPENDATA_URL                          = "indicators.opendata.url";

    public static final String QUARTZ_EXPRESSION_UPDATE_INDICATORS           = "indicators.update.quartz.expression";

    // DSPL
    public static final String DSPL_PROVIDER_NAME                            = "indicators.dspl.provider.name";
    public static final String DSPL_PROVIDER_DESCRIPTION                     = "indicators.dspl.provider.description";
    public static final String DSPL_PROVIDER_URL                             = "indicators.dspl.provider.url";
    public static final String DSPL_INDICATORS_SYSTEM_URL                    = "indicators.dspl.indicators.system.url";

    // Help URL
    public static final String HELP_URL                                      = "indicators.help.url";

    public static final String CRON_EXPRESSION_FOR_CATEGORY_CACHE_REFRESH    = "indicators.category_cache_refresh.cron_expression";

    public static final String CRON_EXPRESSION_GEOGRAPHICAL_VALUES_MIGRATION = "indicators.geographical_values_migration.cron_expression";

    public static final String ANALYTICS_SCRIPT_URL                          = "metamac.analytics.script.url";

    public static final String DEFAULT_GEOGRAPHICAL_CODELIST_URN             = "indicators.geographical_values.code_list.urn";

    // if it is only necessary to reload jaxi messages for e-catalogo it is better to disabled this consumer to avoid bad performance.
    public static final String DISABLED_JAXI_PUBLICATIONS_CONSUMER           = "metamac.indicators.kafka.jaxi_publication_consumer_disabled";

    // API KEYS
    public static final String INDICATORS_WEB_EXTERNAL_REST_API_KEY          = "indicators.web.external.api_key";
}
