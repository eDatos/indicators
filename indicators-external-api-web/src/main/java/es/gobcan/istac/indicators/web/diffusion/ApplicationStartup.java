package es.gobcan.istac.indicators.web.diffusion;

import javax.servlet.ServletContextEvent;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.listener.ApplicationStartupListener;
import org.siemac.metamac.core.common.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.gobcan.istac.indicators.core.constants.IndicatorsConfigurationConstants;

public class ApplicationStartup extends ApplicationStartupListener {

    private static final Logger log = LoggerFactory.getLogger(ApplicationStartup.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        super.contextInitialized(sce);
        try {
            WebUtils.setAppsBaseUrl(configurationService.retrieveAppsExternalWebApplicationUrlBase());
        } catch (MetamacException e) {
            log.error("Error retrieving application configuration", e);
        }

    }

    @Override
    public String projectName() {
        return "indicators";
    }

    @Override
    public void checkApplicationProperties() throws MetamacException {
        // Datasource
        checkRequiredProperty(IndicatorsConfigurationConstants.DB_INDICATORS_URL);
        checkRequiredProperty(IndicatorsConfigurationConstants.DB_INDICATORS_DRIVER_NAME);
        checkRequiredProperty(IndicatorsConfigurationConstants.DB_INDICATORS_USERNAME);
        checkRequiredProperty(IndicatorsConfigurationConstants.DB_INDICATORS_PASSWORD);

        checkRequiredProperty(IndicatorsConfigurationConstants.DB_REPO_URL);
        checkRequiredProperty(IndicatorsConfigurationConstants.DB_REPO_DRIVER_NAME);
        checkRequiredProperty(IndicatorsConfigurationConstants.DB_REPO_USERNAME);
        checkRequiredProperty(IndicatorsConfigurationConstants.DB_REPO_PASSWORD);

        // Other
        checkRequiredProperty(IndicatorsConfigurationConstants.ENDPOINT_STATISTICAL_OPERATIONS_EXTERNAL_API);
        checkRequiredProperty(IndicatorsConfigurationConstants.METAMAC_ORGANISATION);

    }

}
