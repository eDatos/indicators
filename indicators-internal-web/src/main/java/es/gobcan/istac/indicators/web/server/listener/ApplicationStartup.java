package es.gobcan.istac.indicators.web.server.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;

import es.gobcan.istac.indicators.core.constants.IndicatorsConfigurationConstants;

public class ApplicationStartup implements ServletContextListener {

    private static final Log     LOG = LogFactory.getLog(ApplicationStartup.class);

    private ConfigurationService configurationService;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            configurationService = ApplicationContextProvider.getApplicationContext().getBean(ConfigurationService.class);
            checkConfiguration();
        } catch (Exception e) {
            // Abort startup application
            throw new RuntimeException(e);
        }
    }

    private void checkConfiguration() {
        LOG.info("**********************************************************");
        LOG.info("[indicators-internal-web] Checking application configuration");
        LOG.info("**********************************************************");

        // DATASOURCES
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.DB_INDICATORS_URL);
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.DB_INDICATORS_DRIVER_NAME);
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.DB_INDICATORS_USERNAME);
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.DB_INDICATORS_PASSWORD);

        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.DB_REPO_URL);
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.DB_REPO_DRIVER_NAME);
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.DB_REPO_USERNAME);
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.DB_REPO_PASSWORD);
        
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.DB_SUBJECTS_URL);
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.DB_SUBJECTS_DRIVER_NAME);
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.DB_SUBJECTS_USERNAME);
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.DB_SUBJECTS_PASSWORD);
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.DB_SUBJECTS_TABLE);
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.DB_SUBJECTS_COLUMN_CODE);
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.DB_SUBJECTS_COLUMN_TITLE);

        // SECURITY
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.SECURITY_CAS_SERVER_URL_PREFIX);
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.SECURITY_CAS_SERVICE_LOGIN_URL);
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.SECURITY_CAS_SERVICE_LOGOUT_URL);
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.SECURITY_TOLERANCE);
        
        // JAXI
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.JAXI_URL);
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.JAXI_URL_INDICATOR);
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.JAXI_URL_INSTANCE);
        
        
        // DSPL
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.DSPL_PROVIDER_NAME);
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.DSPL_PROVIDER_DESCRIPTION);
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.DSPL_PROVIDER_URL);
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.DSPL_INDICATORS_SYSTEM_URL);
        
        // OTHERS
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.USER_GUIDE_FILENAME);
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.EDITION_LANGUAGES);
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.ENDPOINT_STATISTICAL_OPERATIONS_INTERNAL_API);
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.QUARTZ_EXPRESSION_UPDATE_INDICATORS);

        LOG.info("**********************************************************");
        LOG.info("[indicators-internal-web] Application configuration checked");
        LOG.info("**********************************************************");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

}
