package es.gobcan.istac.indicators.core.conf;

import org.siemac.metamac.core.common.conf.ConfigurationServiceImpl;
import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.core.constants.IndicatorsConfigurationConstants;

public class IndicatorsConfigurationServiceImpl extends ConfigurationServiceImpl implements IndicatorsConfigurationService {

    @Override
    public String retrieveJaxiRemoteUrl() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.JAXI_REMOTE_URL);
    }

    @Override
    public String retrieveWidgetsTypeListUrl() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.WIDGETS_TYPE_LIST_URL);
    }

    @Override
    public String retrieveWidgetsQueryToolsUrl() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.WIDGETS_QUERY_TOOLS_URL);
    }

    @Override
    public String retrieveWidgetsSparklineMax() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.WIDGETS_SPARKLINE_MAX);
    }

    @Override
    public String retrieveWidgetsOpendataUrl() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.WIDGETS_OPENDATA_URL);
    }

    @Override
    public String retrieveIndicatorsDocsPath() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.INDICATORS_DOCS_PATH);
    }

    @Override
    public String retrieveIndicatorsUserGuideFilename() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.USER_GUIDE_FILENAME);
    }

    @Override
    public String retrieveDsplIndicatorsSystemUrl() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.DSPL_INDICATORS_SYSTEM_URL);
    }

    @Override
    public String retrieveDsplProviderName() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.DSPL_PROVIDER_NAME);
    }

    @Override
    public String retrieveDsplProviderDescription() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.DSPL_PROVIDER_DESCRIPTION);
    }

    @Override
    public String retrieveDsplProviderUrl() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.DSPL_PROVIDER_URL);
    }

    @Override
    public String retrieveDbDataViewsRole() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.DB_DATA_VIEWS_ROLE);
    }

    @Override
    public String retrieveDbSubjectsColumnCode() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.DB_SUBJECTS_COLUMN_CODE);
    }

    @Override
    public String retrieveDbSubjectsColumnTitle() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.DB_SUBJECTS_COLUMN_TITLE);
    }

    @Override
    public String retrieveDbSubjectsTable() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.DB_SUBJECTS_TABLE);
    }

    @Override
    public String retrieveJaxiLocalUrl() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.JAXI_LOCAL_URL);
    }

    @Override
    public String retrieveJaxiLocalUrlIndicator() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.JAXI_LOCAL_URL_INDICATOR);
    }

    @Override
    public String retrieveJaxiLocalUrlInstance() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.JAXI_LOCAL_URL_INSTANCE);
    }

    @Override
    public String retrieveQuartzExpressionUpdateIndicators() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.QUARTZ_EXPRESSION_UPDATE_INDICATORS);
    }

    @Override
    public String retrieveIndicatorsExternalApiUrlBase() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.ENDPOINT_INDICATORS_EXTERNAL_API);
    }

    @Override
    public String retrieveIndicatorsExternalWebUrlBase() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.ENDPOINT_INDICATORS_EXTERNAL_WEB);
    }
}
