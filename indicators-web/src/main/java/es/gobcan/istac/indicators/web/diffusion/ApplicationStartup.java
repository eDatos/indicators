package es.gobcan.istac.indicators.web.diffusion;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.listener.ApplicationStartupListener;

import es.gobcan.istac.indicators.core.constants.IndicatorsConfigurationConstants;

public class ApplicationStartup extends ApplicationStartupListener {

    @Override
    public String projectName() {
        return "indicators-visualizations";
    }

    @Override
    public void checkApplicationProperties() throws MetamacException {
        // Other
        checkRequiredProperty(IndicatorsConfigurationConstants.WEB_APPLICATION_PORTAL_EXTERNAL_WEB);
        checkRequiredProperty(IndicatorsConfigurationConstants.WEB_APPLICATION_PORTAL_EXTERNAL_WEB_VISUALIZER);
        checkRequiredProperty(IndicatorsConfigurationConstants.ENDPOINT_STATISTICAL_OPERATIONS_EXTERNAL_API);
        checkRequiredProperty(IndicatorsConfigurationConstants.ENDPOINT_INDICATORS_EXTERNAL_API);
        checkRequiredProperty(IndicatorsConfigurationConstants.METAMAC_ORGANISATION);

        // Widgets
        checkRequiredProperty(IndicatorsConfigurationConstants.WIDGETS_TYPE_LIST_URL);
        checkRequiredProperty(IndicatorsConfigurationConstants.WIDGETS_QUERY_TOOLS_URL);
        checkRequiredProperty(IndicatorsConfigurationConstants.WIDGETS_SPARKLINE_MAX);

        checkRequiredProperty(IndicatorsConfigurationConstants.ENDPOINT_PORTAL_EXTERNAL_BASE);
        checkRequiredProperty(IndicatorsConfigurationConstants.ENDPOINT_PORTAL_EXTERNAL_APIS_PERMALINKS);
    }

}
