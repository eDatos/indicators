package es.gobcan.istac.indicators.web.diffusion;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.listener.ApplicationStartupListener;

import es.gobcan.istac.indicators.core.constants.IndicatorsConfigurationConstants;

public class ApplicationStartup extends ApplicationStartupListener {

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
        checkRequiredProperty(IndicatorsConfigurationConstants.JAXI_REMOTE_URL);
        checkRequiredProperty(IndicatorsConfigurationConstants.ENDPOINT_STATISTICAL_OPERATIONS_EXTERNAL_API);

        // Widgets
        checkRequiredProperty(IndicatorsConfigurationConstants.WIDGETS_TYPE_LIST_URL);
        checkRequiredProperty(IndicatorsConfigurationConstants.WIDGETS_QUERY_TOOLS_URL);
        checkRequiredProperty(IndicatorsConfigurationConstants.WIDGETS_SPARKLINE_MAX);

        checkRequiredProperty(IndicatorsConfigurationConstants.ENDPOINT_PORTAL_EXTERNAL_BASE);
        checkRequiredProperty(IndicatorsConfigurationConstants.ENDPOINT_PORTAL_EXTERNAL_APIS_PERMALINKS);
    }

}
