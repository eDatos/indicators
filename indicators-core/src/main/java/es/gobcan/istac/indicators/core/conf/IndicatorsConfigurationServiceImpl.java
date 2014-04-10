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

    @Override
    public String retrieveIndicatorsDocsPath() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.INDICATORS_DOCS_PATH, Boolean.TRUE);
    }

    @Override
    public String retrieveIndicatorsUserGuideFilename() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.USER_GUIDE_FILENAME, Boolean.TRUE);
    }

    @Override
    public String retrieveDsplIndicatorsSystemUrl() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.DSPL_INDICATORS_SYSTEM_URL, Boolean.TRUE);
    }

    @Override
    public String retrieveDsplProviderName() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.DSPL_PROVIDER_NAME, Boolean.TRUE);
    }

    @Override
    public String retrieveDsplProviderDescription() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.DSPL_PROVIDER_DESCRIPTION, Boolean.TRUE);
    }

    @Override
    public String retrieveDsplProviderUrl() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.DSPL_PROVIDER_URL, Boolean.TRUE);
    }

    @Override
    public String retrieveDbDataViewsRole() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.DB_DATA_VIEWS_ROLE, Boolean.TRUE);
    }

    @Override
    public String retrieveDbSubjectsColumnCode() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.DB_SUBJECTS_COLUMN_CODE, Boolean.TRUE);
    }

    @Override
    public String retrieveDbSubjectsColumnTitle() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.DB_SUBJECTS_COLUMN_TITLE, Boolean.TRUE);
    }

    @Override
    public String retrieveDbSubjectsTable() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.DB_SUBJECTS_TABLE, Boolean.TRUE);
    }

    @Override
    public String retrieveJaxiLocalUrl() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.JAXI_LOCAL_URL, Boolean.TRUE);
    }

    @Override
    public String retrieveJaxiLocalUrlIndicator() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.JAXI_LOCAL_URL_INDICATOR, Boolean.TRUE);
    }

    @Override
    public String retrieveJaxiLocalUrlInstance() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.JAXI_LOCAL_URL_INSTANCE, Boolean.TRUE);
    }

    @Override
    public String retrieveQuartzExpressionUpdateIndicators() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.QUARTZ_EXPRESSION_UPDATE_INDICATORS, Boolean.TRUE);
    }
}
