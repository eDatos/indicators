package es.gobcan.istac.indicators.core.conf;

import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.exception.MetamacException;

public interface IndicatorsConfigurationService extends ConfigurationService {

    String retrieveJaxiRemoteUrl() throws MetamacException;
    
    String retrieveJaxiRemoteUrlIndicator() throws MetamacException;

    String retrieveWidgetsTypeListUrl() throws MetamacException;

    String retrieveWidgetsQueryToolsUrl() throws MetamacException;

    String retrieveWidgetsSparklineMax() throws MetamacException;

    String retrieveWidgetsOpendataUrl() throws MetamacException;

    String retrieveIndicatorsDocsPath() throws MetamacException;

    String retrieveDsplIndicatorsSystemUrl() throws MetamacException;

    String retrieveDsplProviderName() throws MetamacException;

    String retrieveDsplProviderDescription() throws MetamacException;

    String retrieveDsplProviderUrl() throws MetamacException;

    String retrieveDbDataViewsRole() throws MetamacException;

    String retrieveDbSubjectsColumnCode() throws MetamacException;

    String retrieveDbSubjectsColumnTitle() throws MetamacException;

    String retrieveDbSubjectsTable() throws MetamacException;

    String retrieveJaxiLocalUrl() throws MetamacException;

    String retrieveJaxiLocalUrlIndicator() throws MetamacException;

    String retrieveJaxiLocalUrlInstance() throws MetamacException;

    String retrieveQuartzExpressionUpdateIndicators() throws MetamacException;

    String retrieveHelpUrl() throws MetamacException;
}
