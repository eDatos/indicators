package es.gobcan.istac.indicators.core.conf;

import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.exception.MetamacException;

public interface IndicatorsConfigurationService extends ConfigurationService {

    String retrieveWidgetsTypeListUrl() throws MetamacException;

    String retrieveWidgetsQueryToolsUrl() throws MetamacException;

    String retrieveWidgetsSparklineMax() throws MetamacException;

    String retrieveWidgetsOpendataUrl() throws MetamacException;

    String retrieveDsplIndicatorsSystemUrl() throws MetamacException;

    String retrieveDsplProviderName() throws MetamacException;

    String retrieveDsplProviderDescription() throws MetamacException;

    String retrieveDsplProviderUrl() throws MetamacException;

    String retrieveDbDataViewsRole() throws MetamacException;

    String retrieveJaxiLocalUrl() throws MetamacException;

    String retrieveQuartzExpressionUpdateIndicators() throws MetamacException;

    String retrieveHelpUrl() throws MetamacException;

    String retrieveKafkaQueryGroup() throws MetamacException;

    String retrieveKafkaDatasetGroup() throws MetamacException;

    String retrieveKafkaJaxiDatasetGroup() throws MetamacException;

    String retrieveKafkaVariableElementGroup() throws MetamacException;

    String retrieveKafkaCodelistGroup() throws MetamacException;

    String retrieveKafkaConceptSchemeGroup() throws MetamacException;

    String retrieveDefaultCategoryScheme() throws MetamacException;

    String retrieveDefaultTerritoryVariable() throws MetamacException;

    String retrieveDefaultTerritoryCodelistForGpeJsonStat() throws MetamacException;

    String retrieveCodelistAnnotationTypePositionUnit() throws MetamacException;

    String retrieveCronExpressionCategoryCacheRefresh() throws MetamacException;

    String retrieveDefaultGeographicalCodeListUrn() throws MetamacException;

    boolean retrieveJaxiPublicationConsumerIsDisabled();

    String retrieveIndicatorsWebRestApiKey() throws MetamacException;

    String retrieveDefaultCodelistMeasureDimensionValues() throws MetamacException;

    String retrieveVisualisationOrderForGeographicGranularity() throws MetamacException;
}
