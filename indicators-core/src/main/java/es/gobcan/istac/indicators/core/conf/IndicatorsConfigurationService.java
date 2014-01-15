package es.gobcan.istac.indicators.core.conf;

import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.exception.MetamacException;

public interface IndicatorsConfigurationService extends ConfigurationService {

    public String retrieveJaxiRemoteUrl() throws MetamacException;

    public String retrieveWidgetsTypeListUrl() throws MetamacException;

    public String retrieveWidgetsQueryToolsUrl() throws MetamacException;

    public String retrieveWidgetsSparklineMax() throws MetamacException;
}
