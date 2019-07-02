package es.gobcan.istac.indicators.web.client.indicator.presenter;

import java.util.List;

import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;

import com.gwtplatform.mvp.client.UiHandlers;

import es.gobcan.istac.indicators.core.dto.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.web.client.enums.EnvironmentTypeEnum;
import es.gobcan.istac.indicators.web.client.enums.IndicatorCalculationTypeEnum;
import es.gobcan.istac.indicators.web.client.enums.RateDerivationTypeEnum;
import es.gobcan.istac.indicators.web.shared.criteria.IndicatorCriteria;
import es.gobcan.istac.indicators.web.shared.criteria.QueryWebCriteria;

public interface IndicatorUiHandler extends UiHandlers {

    void retrieveDiffusionIndicator(String code, String versionNumber);
    void saveIndicator(IndicatorDto indicator);

    void retrieveSubjects();
    void retrieveGeographicalValuesByGranularity(String geographicalGranularityUuid);
    void retrieveGeographicalValue(String geographicalValueUuid);

    void sendToProductionValidation(String uuid);
    void sendToDiffusionValidation(String uuid);
    void rejectValidation(IndicatorDto indicatorDto);
    void publish(String uuid);
    void archive(String uuid);

    void versioningIndicator(String uuid, VersionTypeEnum versionType);

    void populateData(String uuid);

    void previewData(String code, EnvironmentTypeEnum environmentType);

    // DataSource

    boolean hasDatasources();
    void hasDiffusionIndicatorDatasources(String indicatorUuid, String indicatorVersion);

    void retrieveDataDefinitionsOperationsCodes();
    void retrieveDataDefinitionsByOperationCode(String operationCode);
    void retrieveDataDefinition(String uuid);
    void retrieveDataStructure(String uuid);
    void retrieveDataStructureEdition(String uuid);

    void retrieveStatisticalOperationsForQuerySelection();
    void retrieveQueriesForRelatedQuery(int firstResult, int maxResults, QueryWebCriteria criteria);

    void retrieveGeographicalValueDS(String uuid);

    void saveDataSource(String indicatorUuid, DataSourceDto dataSourceDto);
    void deleteDataSource(List<String> uuid);

    void searchIndicatorsQuantityDenominator(IndicatorCriteria criteria);
    void searchIndicatorsQuantityNumerator(IndicatorCriteria criteria);
    void searchIndicatorsQuantityIndicatorBase(IndicatorCriteria criteria);

    void retrieveQuantityDenominatorIndicator(String indicatorUuid);
    void retrieveQuantityNumeratorIndicator(String indicatorUuid);
    void retrieveQuantityIndicatorBase(String indicatorUuid);

    void retrieveUnitMultipliers();

    void searchRateIndicators(IndicatorCriteria criteria, RateDerivationTypeEnum rateDerivationTypeEnum, IndicatorCalculationTypeEnum indicatorCalculationTypeEnum);
    void retrieveRateIndicator(String indicatorUuid, RateDerivationTypeEnum rateDerivationTypeEnum, IndicatorCalculationTypeEnum indicatorCalculationTypeEnum);

    void enableNotifyPopulationErrors(String indicatorUuid);
    void disableNotifyPopulationErrors(String indicatorUuid);
}
