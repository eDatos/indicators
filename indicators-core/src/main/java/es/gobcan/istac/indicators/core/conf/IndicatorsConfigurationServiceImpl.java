package es.gobcan.istac.indicators.core.conf;

import org.siemac.metamac.core.common.conf.ConfigurationServiceImpl;
import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.core.constants.IndicatorsConfigurationConstants;

public class IndicatorsConfigurationServiceImpl extends ConfigurationServiceImpl implements IndicatorsConfigurationService {

    final String INDICATOR_DATASET_GROUP          = "INDICATOR_DATASET_GROUP";
    final String INDICATOR_JAXI_DATASET_GROUP     = "INDICATOR_JAXI_DATASET_GROUP";
    final String INDICATOR_QUERY_GROUP            = "INDICATOR_QUERY_GROUP";
    final String INDICATOR_VARIABLE_ELEMENT_GROUP = "INDICATOR_VARIABLE_ELEMENT_GROUP";
    final String INDICATOR_CODELIST_GROUP         = "INDICATOR_CODELIST_GROUP";

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
    public String retrieveJaxiLocalUrl() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.JAXI_LOCAL_URL);
    }

    @Override
    public String retrieveQuartzExpressionUpdateIndicators() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.QUARTZ_EXPRESSION_UPDATE_INDICATORS);
    }

    @Override
    public String retrieveHelpUrl() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.HELP_URL);
    }

    @Override
    public String retrieveDefaultCategoryScheme() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.DEFAULT_CATEGORY_SCHEME);
    }

    @Override
    public String retrieveDefaultTerritoryVariable() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.DEFAULT_TERRITORY_VARIABLE);
    }

    @Override
    public String retrieveDefaultTerritoryCodelistForGpeJsonStat() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.DEFAULT_TERRITORY_CODELIST_GPE_JSONSTAT);
    }

    @Override
    public String retrieveCodelistAnnotationTypePositionUnit() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.CODELIST_ANNOTATION_TYPE_POSITION_UNIT_MEASURE);
    }

    @Override
    public String retrieveKafkaQueryGroup() throws MetamacException {
        return INDICATOR_QUERY_GROUP; // Hard coded for evit manual edition
    }

    @Override
    public String retrieveKafkaDatasetGroup() throws MetamacException {
        return INDICATOR_DATASET_GROUP; // Hard coded for evit manual edition
    }

    @Override
    public String retrieveKafkaJaxiDatasetGroup() throws MetamacException {
        return INDICATOR_JAXI_DATASET_GROUP; // Hard coded for evit manual edition
    }

    @Override
    public String retrieveKafkaVariableElementGroup() throws MetamacException {
        return INDICATOR_VARIABLE_ELEMENT_GROUP;
    }

    @Override
    public String retrieveKafkaCodelistGroup() throws MetamacException {
        return INDICATOR_CODELIST_GROUP;
    }

    @Override
    public String retrieveCronExpressionCategoryCacheRefresh() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.CRON_EXPRESSION_FOR_CATEGORY_CACHE_REFRESH);
    }

    @Override
    public String retrieveDefaultGeographicalCodeListUrn() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.DEFAULT_GEOGRAPHICAL_CODELIST_URN);
    }

    @Override
    public boolean retrieveJaxiPublicationConsumerIsDisabled() {
        try {
            return retrievePropertyBoolean(IndicatorsConfigurationConstants.DISABLED_JAXI_PUBLICATIONS_CONSUMER);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String retrieveIndicatorsWebRestApiKey() throws MetamacException {
        return retrieveProperty(IndicatorsConfigurationConstants.INDICATORS_WEB_EXTERNAL_REST_API_KEY);
    }

}
