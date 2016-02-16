package es.gobcan.istac.indicators.web.server.listener;

import org.siemac.metamac.web.common.server.listener.InternalApplicationStartupListener;

import es.gobcan.istac.indicators.core.constants.IndicatorsConfigurationConstants;

public class ApplicationStartup extends InternalApplicationStartupListener {

    @Override
    public void checkDatasourceProperties() {
        checkRequiredProperty(IndicatorsConfigurationConstants.DB_INDICATORS_URL);
        checkRequiredProperty(IndicatorsConfigurationConstants.DB_INDICATORS_DRIVER_NAME);
        checkRequiredProperty(IndicatorsConfigurationConstants.DB_INDICATORS_USERNAME);
        checkRequiredProperty(IndicatorsConfigurationConstants.DB_INDICATORS_PASSWORD);

        checkRequiredProperty(IndicatorsConfigurationConstants.DB_REPO_URL);
        checkRequiredProperty(IndicatorsConfigurationConstants.DB_REPO_DRIVER_NAME);
        checkRequiredProperty(IndicatorsConfigurationConstants.DB_REPO_USERNAME);
        checkRequiredProperty(IndicatorsConfigurationConstants.DB_REPO_PASSWORD);

        checkRequiredProperty(IndicatorsConfigurationConstants.DB_SUBJECTS_URL);
        checkRequiredProperty(IndicatorsConfigurationConstants.DB_SUBJECTS_DRIVER_NAME);
        checkRequiredProperty(IndicatorsConfigurationConstants.DB_SUBJECTS_USERNAME);
        checkRequiredProperty(IndicatorsConfigurationConstants.DB_SUBJECTS_PASSWORD);
        checkRequiredProperty(IndicatorsConfigurationConstants.DB_SUBJECTS_TABLE);
        checkRequiredProperty(IndicatorsConfigurationConstants.DB_SUBJECTS_COLUMN_CODE);
        checkRequiredProperty(IndicatorsConfigurationConstants.DB_SUBJECTS_COLUMN_TITLE);
    }

    @Override
    public void checkWebApplicationsProperties() {
        // Statistical operations
        checkRequiredProperty(IndicatorsConfigurationConstants.WEB_APPLICATION_STATISTICAL_OPERATIONS_INTERNAL_WEB);
    }

    @Override
    public void checkApiProperties() {
        // Statistical Operations
        checkRequiredProperty(IndicatorsConfigurationConstants.ENDPOINT_STATISTICAL_OPERATIONS_INTERNAL_API);
        
        // Indicators
        checkRequiredProperty(IndicatorsConfigurationConstants.ENDPOINT_INDICATORS_INTERNAL_API);
    }

    @Override
    public void checkOtherModuleProperties() {
        // JAXI
        checkRequiredProperty(IndicatorsConfigurationConstants.JAXI_LOCAL_URL);
        checkRequiredProperty(IndicatorsConfigurationConstants.JAXI_LOCAL_URL_INDICATOR);
        checkRequiredProperty(IndicatorsConfigurationConstants.JAXI_LOCAL_URL_INSTANCE);

        // DSPL
        checkRequiredProperty(IndicatorsConfigurationConstants.DSPL_PROVIDER_NAME);
        checkRequiredProperty(IndicatorsConfigurationConstants.DSPL_PROVIDER_DESCRIPTION);
        checkRequiredProperty(IndicatorsConfigurationConstants.DSPL_PROVIDER_URL);
        checkRequiredProperty(IndicatorsConfigurationConstants.DSPL_INDICATORS_SYSTEM_URL);

        // OTHERS
        checkRequiredProperty(IndicatorsConfigurationConstants.ENDPOINT_STATISTICAL_OPERATIONS_INTERNAL_API);
        checkRequiredProperty(IndicatorsConfigurationConstants.QUARTZ_EXPRESSION_UPDATE_INDICATORS);
        checkRequiredProperty(IndicatorsConfigurationConstants.HELP_URL);
    }

    @Override
    public String projectName() {
        return "indicators-internal";
    }
}
