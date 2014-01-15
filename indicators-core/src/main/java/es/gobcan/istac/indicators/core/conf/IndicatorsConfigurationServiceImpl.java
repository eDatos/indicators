package es.gobcan.istac.indicators.core.conf;

import org.siemac.metamac.core.common.conf.ConfigurationServiceImpl;
import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.core.constants.IndicatorsConfigurationConstants;

public class IndicatorsConfigurationServiceImpl extends ConfigurationServiceImpl implements IndicatorsConfigurationService {

    @Override
    public String retrieveJaxiRemoteUrl() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.JAXI_REMOTE_URL, Boolean.TRUE);
    }

    @Override
    public String retrieveWidgetsTypeListUrl() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.WIDGETS_TYPE_LIST_URL, Boolean.TRUE);
    }

    @Override
    public String retrieveWidgetsQueryToolsUrl() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.WIDGETS_QUERY_TOOLS_URL, Boolean.TRUE);
    }

    @Override
    public String retrieveWidgetsSparklineMax() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.WIDGETS_SPARKLINE_MAX, Boolean.TRUE);
    }

}
