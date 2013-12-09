package es.gobcan.istac.indicators.web.diffusion;

import es.gobcan.istac.indicators.core.constants.IndicatorsConfigurationConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

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
        LOG.info("[indicators-web] Checking application configuration");
        LOG.info("**********************************************************");

        // Datasource
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.DB_INDICATORS_URL);
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.DB_INDICATORS_DRIVER_NAME);
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.DB_INDICATORS_USERNAME);
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.DB_INDICATORS_PASSWORD);

        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.DB_REPO_URL);
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.DB_REPO_DRIVER_NAME);
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.DB_REPO_USERNAME);
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.DB_REPO_PASSWORD);

        // Other
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.JAXI_LOCAL_URL);
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.ENDPOINT_STATISTICAL_OPERATIONS_EXTERNAL_API);

        // Widgets
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.WIDGETS_TYPE_LIST_URL);
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.WIDGETS_QUERY_TOOLS_URL);
        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.WIDGETS_SPARKLINE_MAX);

        configurationService.checkRequiredProperty(IndicatorsConfigurationConstants.ENDPOINT_METAMAC_PORTAL);

        LOG.info("**********************************************************");
        LOG.info("[indicators-web] Application configuration checked");
        LOG.info("**********************************************************");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

}
