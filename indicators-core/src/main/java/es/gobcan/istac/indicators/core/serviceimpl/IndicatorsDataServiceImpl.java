package es.gobcan.istac.indicators.core.serviceimpl;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.fornax.cartridges.sculptor.framework.errorhandling.ApplicationException;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.dataset.repository.dto.AttributeBasicDto;
import com.arte.statistic.dataset.repository.dto.CodeDimensionDto;
import com.arte.statistic.dataset.repository.dto.ConditionDimensionDto;
import com.arte.statistic.dataset.repository.dto.ConditionObservationDto;
import com.arte.statistic.dataset.repository.dto.DatasetRepositoryDto;
import com.arte.statistic.dataset.repository.dto.InternationalStringDto;
import com.arte.statistic.dataset.repository.dto.LocalisedStringDto;
import com.arte.statistic.dataset.repository.dto.ObservationDto;
import com.arte.statistic.dataset.repository.dto.ObservationExtendedDto;
import com.arte.statistic.dataset.repository.service.DatasetRepositoriesServiceFacade;
import com.arte.statistic.dataset.repository.util.DtoUtils;

import es.gobcan.istac.indicators.core.domain.Data;
import es.gobcan.istac.indicators.core.domain.DataContent;
import es.gobcan.istac.indicators.core.domain.DataDefinition;
import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.DataSourceVariable;
import es.gobcan.istac.indicators.core.domain.DataStructure;
import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.Indicator;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.MeasureValue;
import es.gobcan.istac.indicators.core.domain.Quantity;
import es.gobcan.istac.indicators.core.domain.TimeGranularity;
import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.dto.DataSourceDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorDataAttributeTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorDataDimensionTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.MeasureDimensionTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.RateDerivationMethodTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.RateDerivationRoundingEnum;
import es.gobcan.istac.indicators.core.enume.domain.TimeGranularityEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionParameters;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceimpl.util.DataOperation;
import es.gobcan.istac.indicators.core.serviceimpl.util.DataSourceCompatibilityChecker;
import es.gobcan.istac.indicators.core.serviceimpl.util.InvocationValidator;
import es.gobcan.istac.indicators.core.serviceimpl.util.TimeVariableUtils;

/**
 * Implementation of IndicatorsDataService.
 */
@Service("indicatorsDataService")
public class IndicatorsDataServiceImpl extends IndicatorsDataServiceImplBase {

    private final Logger                     LOG                = LoggerFactory.getLogger(IndicatorsDataServiceImpl.class);

    public static final String               GEO_DIM            = IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name();
    public static final String               TIME_DIM           = IndicatorDataDimensionTypeEnum.TIME.name();
    public static final String               MEASURE_DIM        = IndicatorDataDimensionTypeEnum.MEASURE.name();
    public static final String               CODE_ATTR          = IndicatorDataAttributeTypeEnum.CODE.name();
    public static final String               CODE_ATTR_LOC      = "es";
    public static final String               OBS_CONF_ATTR      = IndicatorDataAttributeTypeEnum.OBS_CONF.name();
    public static final String               OBS_CONF_LOC       = "es";

    public static final String               DOT_NOT_APPLICABLE = ".";
    public static final String               DOT_UNAVAILABLE    = "..";
    public static final Double               ZERO_RANGE         = 1E-6;
    public static final int                  MAX_MEASURE_LENGTH = 50;

    private static final Map<String, String> SPECIAL_STRING_MAPPING;

    static {
        SPECIAL_STRING_MAPPING = new HashMap<String, String>();
        SPECIAL_STRING_MAPPING.put("-", "");
        SPECIAL_STRING_MAPPING.put(".", "No procede");
        SPECIAL_STRING_MAPPING.put("..", "Dato no disponible");
        SPECIAL_STRING_MAPPING.put("...", "Dato oculto por impreciso o baja calidad");
        SPECIAL_STRING_MAPPING.put("....", "Dato oculto por secreto estadístico");
        SPECIAL_STRING_MAPPING.put(".....", "Dato incluido en otra categoría");
        SPECIAL_STRING_MAPPING.put("......", "Dato no disponible por vacaciones o festivos");
    }

    @Autowired
    private DatasetRepositoriesServiceFacade datasetRepositoriesServiceFacade;

    private ObjectMapper                     mapper             = new ObjectMapper();

    public IndicatorsDataServiceImpl() {
    }

    @Override
    public List<DataDefinition> retrieveDataDefinitions(ServiceContext ctx) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveDataDefinitions(null);

        // Find db
        List<DataDefinition> dataDefinitions = getDataGpeRepository().findCurrentDataDefinitions();
        return dataDefinitions;
    }

    @Override
    public DataDefinition retrieveDataDefinition(ServiceContext ctx, String uuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveDataDefinition(uuid, null);

        // Find db
        DataDefinition dataDefinition = getDataGpeRepository().findCurrentDataDefinition(uuid);
        if (dataDefinition == null) {
            throw new MetamacException(ServiceExceptionType.DATA_DEFINITION_RETRIEVE_ERROR, uuid);
        }
        return dataDefinition;
    }

    @Override
    public DataStructure retrieveDataStructure(ServiceContext ctx, String uuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveDataStructure(uuid, null);
        try {
            // Call jaxi for query structure
            String json = getIndicatorsDataProviderService().retrieveDataStructureJson(ctx, uuid);
            return jsonToDataStructure(json);
        } catch (Exception e) {
            throw new MetamacException(e, ServiceExceptionType.DATA_STRUCTURE_RETRIEVE_ERROR, uuid);
        }
    }

    @Override
    public void populateIndicatorData(ServiceContext ctx, String indicatorUuid, String indicatorVersionNumber) throws MetamacException {
        // Validation
        InvocationValidator.checkPopulateIndicatorData(indicatorUuid, indicatorVersionNumber, null);

        DatasetRepositoryDto datasetRepoDto = null;
        try {
            IndicatorVersion indicatorVersion = getIndicatorVersionRepository().retrieveIndicatorVersion(indicatorUuid, indicatorVersionNumber);
            List<DataSource> dataSources = indicatorVersion.getDataSources();

            if (dataSources.size() == 0) {
                throw new MetamacException(ServiceExceptionType.DATA_POPULATE_NO_DATASOURCES_ERROR, indicatorUuid, indicatorVersionNumber);
            }

            // Data will be stored in a map (cache), because the same json can be requested many times
            Map<String, Data> dataCache = retrieveDatasFromProvider(ctx, dataSources);

            checkDataSourcesDataCompatibility(dataSources, dataCache);

            // Transform list to process first load operations
            List<DataOperation> dataOps = transformDataSourcesForProcessing(dataSources);

            datasetRepoDto = createDatasetRepositoryDefinition(indicatorUuid, indicatorVersionNumber);

            // Process observations for each dataOperation
            for (DataOperation dataOperation : dataOps) {
                Data data = dataCache.get(dataOperation.getDataGpeUuid());
                List<ObservationExtendedDto> observations = createObservationsFromDataOperationData(dataOperation, data, datasetRepoDto.getDatasetId());
                datasetRepositoriesServiceFacade.insertObservationsExtended(datasetRepoDto.getDatasetId(), observations);
            }
            // Replace the whole dataset
            indicatorVersion = setDatasetRepositoryDeleteOldOne(indicatorVersion, datasetRepoDto);

            // No more inconsistent data, no more needs update
            markIndicatorVersionAsDataUpdated(indicatorVersion);
        } catch (Exception e) {
            deleteDatasetRepositoryIfExists(datasetRepoDto);
            if (e instanceof MetamacException) {
                throw (MetamacException) e;
            } else {
                throw new MetamacException(e, ServiceExceptionType.DATA_POPULATE_ERROR, indicatorUuid, indicatorVersionNumber);
            }
        }
    }

    @Override
    public void updateIndicatorsData(ServiceContext ctx) throws MetamacException {
        LOG.info("Starting Indicators data update process");

        // Validation
        InvocationValidator.checkUpdateIndicatorsData(null);

        Date lastQueryDate = getIndicatorsConfigurationService().retrieveLastSuccessfulGpeQueryDate(ctx);

        markIndicatorsVersionWhichNeedsUpdate(ctx, lastQueryDate);

        List<IndicatorVersion> pendingIndicators = getIndicatorVersionRepository().findIndicatorsVersionNeedsUpdate();
        for (IndicatorVersion indicatorVersion : pendingIndicators) {
            Indicator indicator = indicatorVersion.getIndicator();
            String diffusionVersion = indicator.getIsPublished() ? indicator.getDiffusionVersion().getVersionNumber() : null;

            String indicatorUuid = indicator.getUuid();
            if (indicatorVersion.getVersionNumber().equals(diffusionVersion)) {
                try {
                    populateIndicatorData(ctx, indicatorUuid, diffusionVersion);
                } catch (MetamacException e) {
                    LOG.warn("Error populating indicator indicatorUuid:" + indicatorUuid, e);
                }
            }
        }
        LOG.info("Finished Indicators data update process");
    }

    /* Geographical granularities and values */

    @Override
    public List<GeographicalGranularity> retrieveGeographicalGranularitiesInIndicator(ServiceContext ctx, String indicatorUuid, String indicatorVersionNumber) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveGeographicalGranularitiesInIndicator(indicatorUuid, indicatorVersionNumber, null);

        IndicatorVersion indicatorVersion = getIndicatorVersion(indicatorUuid, indicatorVersionNumber);
        return calculateGeographicalGranularitiesInIndicatorVersion(indicatorVersion);
    }

    @Override
    public List<GeographicalGranularity> retrieveGeographicalGranularitiesInIndicatorPublished(ServiceContext ctx, String indicatorUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveGeographicalGranularitiesInIndicatorPublished(indicatorUuid, null);

        IndicatorVersion indicatorVersion = getIndicatorPublishedVersion(indicatorUuid);
        return calculateGeographicalGranularitiesInIndicatorVersion(indicatorVersion);
    }

    private List<GeographicalGranularity> calculateGeographicalGranularitiesInIndicatorVersion(IndicatorVersion indicatorVersion) throws MetamacException {
        String datasetId = indicatorVersion.getDataRepositoryId();
        if (datasetId != null) {
            try {
                Set<GeographicalGranularity> geoGranularitiesInIndicator = new HashSet<GeographicalGranularity>();
                List<String> geoCodesInIndicator = findCodesForDimensionInIndicator(indicatorVersion, IndicatorDataDimensionTypeEnum.GEOGRAPHICAL);

                for (String geoCode : geoCodesInIndicator) {
                    GeographicalValue geographicalValue = getGeographicalValueRepository().findGeographicalValueByCode(geoCode);
                    if (geographicalValue != null) {
                        geoGranularitiesInIndicator.add(geographicalValue.getGranularity());
                    }
                }
                return new ArrayList<GeographicalGranularity>(geoGranularitiesInIndicator);
            } catch (ApplicationException e) {
                throw new MetamacException(e, ServiceExceptionType.INDICATOR_FIND_DIMENSION_CODES_ERROR, indicatorVersion.getIndicator().getUuid(), ServiceExceptionParameters.INDICATOR_DATA_DIMENSION_TYPE_GEOGRAPHICAL);
            }
        } else {
            throw new MetamacException(ServiceExceptionType.INDICATOR_NOT_POPULATED, indicatorVersion.getIndicator().getUuid(), indicatorVersion.getVersionNumber());
        }
    }

    @Override
    public List<GeographicalGranularity> retrieveGeographicalGranularitiesInIndicatorInstance(ServiceContext ctx, String indicatorInstanceUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveGeographicalGranularitiesInIndicatorInstance(indicatorInstanceUuid, null);

        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);
        if (indInstance.getGeographicalValue() != null) {
            return Arrays.asList(indInstance.getGeographicalValue().getGranularity());
        } else if (indInstance.getGeographicalGranularity() != null) {
            return Arrays.asList(indInstance.getGeographicalGranularity());
        } else {
            return retrieveGeographicalGranularitiesInIndicatorPublished(ctx, indInstance.getIndicator().getUuid());
        }
    }

    @Override
    public List<GeographicalValue> retrieveGeographicalValuesByGranularityInIndicator(ServiceContext ctx, String indicatorUuid, String indicatorVersionNumber, String granularityUuid)
            throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveGeographicalValuesByGranularityInIndicator(indicatorUuid, indicatorVersionNumber, granularityUuid, null);

        IndicatorVersion indicatorVersion = getIndicatorVersion(indicatorUuid, indicatorVersionNumber);
        return calculateGeographicalValuesByGranularityInIndicatorVersion(granularityUuid, indicatorVersion);
    }

    @Override
    public List<GeographicalValue> retrieveGeographicalValuesByGranularityInIndicatorPublished(ServiceContext ctx, String indicatorUuid, String granularityUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveGeographicalValuesByGranularityInIndicatorPublished(indicatorUuid, granularityUuid, null);

        IndicatorVersion indicatorVersion = getIndicatorPublishedVersion(indicatorUuid);
        return calculateGeographicalValuesByGranularityInIndicatorVersion(granularityUuid, indicatorVersion);
    }

    private List<GeographicalValue> calculateGeographicalValuesByGranularityInIndicatorVersion(String granularityUuid, IndicatorVersion indicatorVersion) throws MetamacException {
        String datasetId = indicatorVersion.getDataRepositoryId();
        if (datasetId != null) {
            try {
                GeographicalGranularity granularity = getGeographicalGranularityRepository().retrieveGeographicalGranularity(granularityUuid);
                List<GeographicalValue> geographicalValuesInIndicator = new ArrayList<GeographicalValue>();
                List<String> geographicalCodesInIndicator = findCodesForDimensionInIndicator(indicatorVersion, IndicatorDataDimensionTypeEnum.GEOGRAPHICAL);
                List<GeographicalValue> geographicalValuesInGranularity = getGeographicalValueRepository().findGeographicalValuesByGranularity(granularity.getCode());
                for (GeographicalValue geoValue : geographicalValuesInGranularity) {
                    if (geographicalCodesInIndicator.contains(geoValue.getCode())) {
                        geographicalValuesInIndicator.add(geoValue);
                    }
                }
                return geographicalValuesInIndicator;
            } catch (ApplicationException e) {
                throw new MetamacException(e, ServiceExceptionType.INDICATOR_FIND_DIMENSION_CODES_ERROR, indicatorVersion.getIndicator().getUuid(), ServiceExceptionParameters.INDICATOR_DATA_DIMENSION_TYPE_GEOGRAPHICAL);
            }
        } else {
            throw new MetamacException(ServiceExceptionType.INDICATOR_NOT_POPULATED, indicatorVersion.getIndicator().getUuid(), indicatorVersion.getVersionNumber());
        }
    }

    @Override
    public List<GeographicalValue> retrieveGeographicalValuesInIndicator(ServiceContext ctx, String indicatorUuid, String indicatorVersionNumber) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveGeographicalValuesInIndicator(indicatorUuid, indicatorVersionNumber, null);

        IndicatorVersion indicatorVersion = getIndicatorVersion(indicatorUuid, indicatorVersionNumber);
        return calculateGeographicalValuesInIndicatorVersion(indicatorVersion);
    }

    @Override
    public List<GeographicalValue> retrieveGeographicalValuesInIndicatorPublished(ServiceContext ctx, String indicatorUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveGeographicalValuesInIndicatorPublished(indicatorUuid, null);

        IndicatorVersion indicatorVersion = getIndicatorPublishedVersion(indicatorUuid);
        return calculateGeographicalValuesInIndicatorVersion(indicatorVersion);
    }

    private List<GeographicalValue> calculateGeographicalValuesInIndicatorVersion(IndicatorVersion indicatorVersion) throws MetamacException {
        String datasetId = indicatorVersion.getDataRepositoryId();
        if (datasetId != null) {
            try {
                List<GeographicalValue> geographicalValuesInIndicator = new ArrayList<GeographicalValue>();
                List<String> geographicalCodesInIndicator = findCodesForDimensionInIndicator(indicatorVersion, IndicatorDataDimensionTypeEnum.GEOGRAPHICAL);
                for (String geoCode : geographicalCodesInIndicator) {
                    GeographicalValue geoValue = getGeographicalValueRepository().findGeographicalValueByCode(geoCode);
                    if (geoValue != null) {
                        geographicalValuesInIndicator.add(geoValue);
                    }
                }
                return geographicalValuesInIndicator;
            } catch (ApplicationException e) {
                throw new MetamacException(e, ServiceExceptionType.INDICATOR_FIND_DIMENSION_CODES_ERROR, indicatorVersion.getIndicator().getUuid(), ServiceExceptionParameters.INDICATOR_DATA_DIMENSION_TYPE_GEOGRAPHICAL);
            }
        } else {
            throw new MetamacException(ServiceExceptionType.INDICATOR_NOT_POPULATED, indicatorVersion.getIndicator().getUuid(), indicatorVersion.getVersionNumber());
        }
    }

    @Override
    public List<GeographicalValue> retrieveGeographicalValuesInIndicatorInstance(ServiceContext ctx, String indicatorInstanceUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveGeographicalValuesInIndicatorInstance(indicatorInstanceUuid, null);

        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);
        if (indInstance.getGeographicalValue() != null) { // Fixed value
            return Arrays.asList(indInstance.getGeographicalValue());
        } else if (indInstance.getGeographicalGranularity() != null) { // fixed granularity
            return retrieveGeographicalValuesByGranularityInIndicatorPublished(ctx, indInstance.getIndicator().getUuid(), indInstance.getGeographicalGranularity().getUuid());
        } else { // nothing is fixed
            return retrieveGeographicalValuesInIndicatorPublished(ctx, indInstance.getIndicator().getUuid());
        }
    }

    /* Time granularities and values */

    @Override
    public List<TimeGranularity> retrieveTimeGranularitiesInIndicator(ServiceContext ctx, String indicatorUuid, String indicatorVersionNumber) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveTimeGranularitiesInIndicator(indicatorUuid, indicatorVersionNumber, null);

        IndicatorVersion indicatorVersion = getIndicatorVersion(indicatorUuid, indicatorVersionNumber);
        return calculateTimeGranularitiesInIndicatorVersion(ctx, indicatorVersion);
    }

    @Override
    public List<TimeGranularity> retrieveTimeGranularitiesInIndicatorPublished(ServiceContext ctx, String indicatorUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveTimeGranularitiesInIndicatorPublished(indicatorUuid, null);

        IndicatorVersion indicatorVersion = getIndicatorPublishedVersion(indicatorUuid);
        return calculateTimeGranularitiesInIndicatorVersion(ctx, indicatorVersion);
    }

    private List<TimeGranularity> calculateTimeGranularitiesInIndicatorVersion(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {
        String datasetId = indicatorVersion.getDataRepositoryId();
        if (datasetId != null) {
            try {
                Set<TimeGranularityEnum> timeGranularitiesEnumInIndicator = new HashSet<TimeGranularityEnum>();
                List<String> timeCodesInIndicator = findCodesForDimensionInIndicator(indicatorVersion, IndicatorDataDimensionTypeEnum.TIME);
                for (String timeCode : timeCodesInIndicator) {
                    TimeGranularityEnum guessedGranularity = TimeVariableUtils.guessTimeGranularity(timeCode);
                    timeGranularitiesEnumInIndicator.add(guessedGranularity);
                }
                List<TimeGranularity> timeGranularitiesInIndicator = new ArrayList<TimeGranularity>();
                for (TimeGranularityEnum timeGranularityEnum : timeGranularitiesEnumInIndicator) {
                    TimeGranularity timeGranularity = getIndicatorsSystemsService().retrieveTimeGranularity(ctx, timeGranularityEnum);
                    timeGranularitiesInIndicator.add(timeGranularity);
                }
                return new ArrayList<TimeGranularity>(timeGranularitiesInIndicator);
            } catch (ApplicationException e) {
                throw new MetamacException(e, ServiceExceptionType.INDICATOR_FIND_DIMENSION_CODES_ERROR, indicatorVersion.getIndicator().getUuid(), ServiceExceptionParameters.INDICATOR_DATA_DIMENSION_TYPE_TIME);
            }
        } else {
            throw new MetamacException(ServiceExceptionType.INDICATOR_NOT_POPULATED, indicatorVersion.getIndicator().getUuid(), indicatorVersion.getVersionNumber());
        }
    }

    @Override
    public List<TimeGranularity> retrieveTimeGranularitiesInIndicatorInstance(ServiceContext ctx, String indicatorInstanceUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveTimeGranularitiesInIndicatorInstance(indicatorInstanceUuid, null);

        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);

        if (indInstance.getTimeValue() != null) {
            TimeGranularityEnum guessedGranularity = TimeVariableUtils.guessTimeGranularity(indInstance.getTimeValue());
            TimeGranularity timeGranularity = getIndicatorsSystemsService().retrieveTimeGranularity(ctx, guessedGranularity);
            return Arrays.asList(timeGranularity);
        } else if (indInstance.getTimeGranularity() != null) {
            TimeGranularity timeGranularity = getIndicatorsSystemsService().retrieveTimeGranularity(ctx, indInstance.getTimeGranularity());
            return Arrays.asList(timeGranularity);
        } else {
            return retrieveTimeGranularitiesInIndicatorPublished(ctx, indInstance.getIndicator().getUuid());
        }
    }

    @Override
    public List<TimeValue> retrieveTimeValuesByGranularityInIndicator(ServiceContext ctx, String indicatorUuid, String indicatorVersionNumber, TimeGranularityEnum granularity) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveTimeValuesByGranularityInIndicator(indicatorUuid, indicatorVersionNumber, granularity, null);

        IndicatorVersion indicatorVersion = getIndicatorVersion(indicatorUuid, indicatorVersionNumber);
        return calculateTimeValuesByGranularityInIndicatorVersion(ctx, granularity, indicatorVersion);
    }

    @Override
    public List<TimeValue> retrieveTimeValuesByGranularityInIndicatorPublished(ServiceContext ctx, String indicatorUuid, TimeGranularityEnum granularity) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveTimeValuesByGranularityInIndicatorPublished(indicatorUuid, granularity, null);

        IndicatorVersion indicatorVersion = getIndicatorPublishedVersion(indicatorUuid);
        return calculateTimeValuesByGranularityInIndicatorVersion(ctx, granularity, indicatorVersion);
    }

    private List<TimeValue> calculateTimeValuesByGranularityInIndicatorVersion(ServiceContext ctx, TimeGranularityEnum granularity, IndicatorVersion indicatorVersion) throws MetamacException {
        String datasetId = indicatorVersion.getDataRepositoryId();
        if (datasetId != null) {
            try {
                List<TimeValue> timeValuesInIndicator = new ArrayList<TimeValue>();
                List<String> timeCodesInIndicator = findCodesForDimensionInIndicator(indicatorVersion, IndicatorDataDimensionTypeEnum.TIME);
                for (String timeCode : timeCodesInIndicator) {
                    TimeGranularityEnum guessedGranularity = TimeVariableUtils.guessTimeGranularity(timeCode);
                    if (granularity.equals(guessedGranularity)) {
                        TimeValue timeValue = getIndicatorsSystemsService().retrieveTimeValue(ctx, timeCode);
                        timeValuesInIndicator.add(timeValue);
                    }
                }
                return timeValuesInIndicator;
            } catch (ApplicationException e) {
                throw new MetamacException(e, ServiceExceptionType.INDICATOR_FIND_DIMENSION_CODES_ERROR, indicatorVersion.getIndicator().getUuid(), ServiceExceptionParameters.INDICATOR_DATA_DIMENSION_TYPE_TIME);
            }
        } else {
            throw new MetamacException(ServiceExceptionType.INDICATOR_NOT_POPULATED, indicatorVersion.getIndicator().getUuid(), indicatorVersion.getVersionNumber());
        }
    }

    @Override
    public List<TimeValue> retrieveTimeValuesInIndicator(ServiceContext ctx, String indicatorUuid, String indicatorVersionNumber) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveTimeValuesInIndicator(indicatorUuid, indicatorVersionNumber, null);

        IndicatorVersion indicatorVersion = getIndicatorVersion(indicatorUuid, indicatorVersionNumber);
        return calculateTimeValuesInIndicatorVersion(ctx, indicatorVersion);
    }

    @Override
    public List<TimeValue> retrieveTimeValuesInIndicatorPublished(ServiceContext ctx, String indicatorUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveTimeValuesInIndicatorPublished(indicatorUuid, null);

        IndicatorVersion indicatorVersion = getIndicatorPublishedVersion(indicatorUuid);
        return calculateTimeValuesInIndicatorVersion(ctx, indicatorVersion);
    }

    private List<TimeValue> calculateTimeValuesInIndicatorVersion(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {
        String datasetId = indicatorVersion.getDataRepositoryId();
        if (datasetId != null) {
            try {
                List<TimeValue> timeValuesInIndicator = new ArrayList<TimeValue>();
                List<String> timeCodesInIndicator = findCodesForDimensionInIndicator(indicatorVersion, IndicatorDataDimensionTypeEnum.TIME);
                for (String timeCode : timeCodesInIndicator) {
                    if (TimeVariableUtils.isTimeValue(timeCode)) {
                        TimeValue timeValue = getIndicatorsSystemsService().retrieveTimeValue(ctx, timeCode);
                        timeValuesInIndicator.add(timeValue);
                    }
                }

                return timeValuesInIndicator;
            } catch (ApplicationException e) {
                throw new MetamacException(e, ServiceExceptionType.INDICATOR_FIND_DIMENSION_CODES_ERROR, indicatorVersion.getIndicator().getUuid(), ServiceExceptionParameters.INDICATOR_DATA_DIMENSION_TYPE_TIME);
            }
        } else {
            throw new MetamacException(ServiceExceptionType.INDICATOR_NOT_POPULATED, indicatorVersion.getIndicator().getUuid(), indicatorVersion.getVersionNumber());
        }
    }

    @Override
    public List<TimeValue> retrieveTimeValuesInIndicatorInstance(ServiceContext ctx, String indicatorInstanceUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveTimeValuesInIndicatorInstance(indicatorInstanceUuid, null);

        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);
        if (indInstance.getTimeValue() != null) {
            TimeValue timeValue = getIndicatorsSystemsService().retrieveTimeValue(ctx, indInstance.getTimeValue());
            return Arrays.asList(timeValue);
        } else if (indInstance.getTimeGranularity() != null) {
            return retrieveTimeValuesByGranularityInIndicatorPublished(ctx, indInstance.getIndicator().getUuid(), indInstance.getTimeGranularity());
        } else {
            return retrieveTimeValuesInIndicatorPublished(ctx, indInstance.getIndicator().getUuid());
        }
    }

    @Override
    public List<MeasureValue> retrieveMeasureValuesInIndicator(ServiceContext ctx, String indicatorUuid, String indicatorVersionNumber) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveMeasureValuesInIndicator(indicatorUuid, indicatorVersionNumber, null);

        IndicatorVersion indicatorVersion = getIndicatorVersion(indicatorUuid, indicatorVersionNumber);
        return calculateMeasureValuesInIndicatorVersion(ctx, indicatorVersion);
    }

    @Override
    public List<MeasureValue> retrieveMeasureValuesInIndicatorPublished(ServiceContext ctx, String indicatorUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveMeasureValuesInIndicatorPublished(indicatorUuid, null);

        IndicatorVersion indicatorVersion = getIndicatorPublishedVersion(indicatorUuid);
        return calculateMeasureValuesInIndicatorVersion(ctx, indicatorVersion);
    }

    @Override
    public List<MeasureValue> retrieveMeasureValuesInIndicatorInstance(ServiceContext ctx, String indicatorInstanceUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveMeasureValuesInIndicatorInstance(indicatorInstanceUuid, null);
        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);
        IndicatorVersion indicatorVersion = getIndicatorPublishedVersion(indInstance.getIndicator().getUuid());
        return calculateMeasureValuesInIndicatorVersion(ctx, indicatorVersion);
    }

    private List<MeasureValue> calculateMeasureValuesInIndicatorVersion(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {
        String datasetId = indicatorVersion.getDataRepositoryId();
        if (datasetId != null) {
            try {
                List<MeasureValue> measureValuesInIndicator = new ArrayList<MeasureValue>();
                List<String> measureCodesInIndicator = findCodesForDimensionInIndicator(indicatorVersion, IndicatorDataDimensionTypeEnum.MEASURE);
                for (String measureCode : measureCodesInIndicator) {
                    MeasureDimensionTypeEnum measureDimValue = MeasureDimensionTypeEnum.valueOf(measureCode);
                    MeasureValue measureValue = getIndicatorsSystemsService().retrieveMeasureValue(ctx, measureDimValue);
                    measureValuesInIndicator.add(measureValue);
                }
                return measureValuesInIndicator;
            } catch (ApplicationException e) {
                throw new MetamacException(e, ServiceExceptionType.INDICATOR_FIND_DIMENSION_CODES_ERROR, indicatorVersion.getIndicator().getUuid(), ServiceExceptionParameters.INDICATOR_DATA_DIMENSION_TYPE_TIME);
            }
        } else {
            throw new MetamacException(ServiceExceptionType.INDICATOR_NOT_POPULATED, indicatorVersion.getIndicator().getUuid(), indicatorVersion.getVersionNumber());
        }
    }

    /* Data finders */

    @Override
    public Map<String, ObservationDto> findObservationsByDimensionsInIndicatorPublished(ServiceContext ctx, String indicatorUuid, List<ConditionDimensionDto> conditions) throws MetamacException {
        IndicatorVersion indicatorVersion = getIndicatorPublishedVersion(indicatorUuid);
        try {
            return findObservationsByDimensions(ctx, indicatorVersion, conditions);
        } catch (ApplicationException e) {
            throw new MetamacException(e, ServiceExceptionType.DATA_FIND_OBSERVATIONS_ERROR, indicatorUuid);
        }
    }

    @Override
    public Map<String, ObservationExtendedDto> findObservationsExtendedByDimensionsInIndicatorPublished(ServiceContext ctx, String indicatorUuid, List<ConditionDimensionDto> conditions)
            throws MetamacException {
        IndicatorVersion indicatorVersion = getIndicatorPublishedVersion(indicatorUuid);
        try {
            return findObservationsExtendedByDimensions(ctx, indicatorVersion, conditions);
        } catch (ApplicationException e) {
            throw new MetamacException(e, ServiceExceptionType.DATA_FIND_OBSERVATIONS_EXTENDED_ERROR, indicatorUuid);
        }
    }

    @Override
    public Map<String, ObservationDto> findObservationsByDimensionsInIndicatorInstance(ServiceContext ctx, String indicatorInstanceUuid, List<ConditionDimensionDto> conditions)
            throws MetamacException {
        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);
        IndicatorVersion indicatorVersion = getIndicatorPublishedVersion(indInstance.getIndicator().getUuid());

        List<GeographicalValue> geoValues = retrieveGeographicalValuesInIndicatorInstance(ctx, indicatorInstanceUuid);
        List<TimeValue> timeValues = retrieveTimeValuesInIndicatorInstance(ctx, indicatorInstanceUuid);

        List<ConditionDimensionDto> newConditions = filterConditionsByValues(conditions, geoValues, timeValues);
        try {
            return findObservationsByDimensions(ctx, indicatorVersion, newConditions);
        } catch (ApplicationException e) {
            throw new MetamacException(e, ServiceExceptionType.DATA_INSTANCES_FIND_OBSERVATIONS_ERROR, indicatorInstanceUuid);
        }
    }

    @Override
    public Map<String, ObservationExtendedDto> findObservationsExtendedByDimensionsInIndicatorInstance(ServiceContext ctx, String indicatorInstanceUuid, List<ConditionDimensionDto> conditions)
            throws MetamacException {
        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);
        IndicatorVersion indicatorVersion = getIndicatorPublishedVersion(indInstance.getIndicator().getUuid());

        List<GeographicalValue> geoValues = retrieveGeographicalValuesInIndicatorInstance(ctx, indicatorInstanceUuid);
        List<TimeValue> timeValues = retrieveTimeValuesInIndicatorInstance(ctx, indicatorInstanceUuid);

        List<ConditionDimensionDto> newConditions = filterConditionsByValues(conditions, geoValues, timeValues);
        try {
            return findObservationsExtendedByDimensions(ctx, indicatorVersion, newConditions);
        } catch (ApplicationException e) {
            throw new MetamacException(e, ServiceExceptionType.DATA_INSTANCES_FIND_OBSERVATIONS_EXTENDED_ERROR, indicatorInstanceUuid);
        }
    }

    /*
     * Given some conditions this method ensures that:
     * - In geographical conditions only the given values are specified
     * - In time conditions only the given values are specified
     */
    private List<ConditionDimensionDto> filterConditionsByValues(List<ConditionDimensionDto> conditions, List<GeographicalValue> geoValues, List<TimeValue> timeValues) {
        List<ConditionDimensionDto> rawConditions = new ArrayList<ConditionDimensionDto>();
        if (conditions != null) {
            rawConditions = conditions;
        }

        List<ConditionDimensionDto> newConditions = new ArrayList<ConditionDimensionDto>();
        for (ConditionDimensionDto condition : rawConditions) {
            ConditionDimensionDto newCondition = new ConditionDimensionDto();
            newCondition.setDimensionId(condition.getDimensionId());
            if (IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name().equals(condition.getDimensionId())) {
                List<String> newCodeDims = new ArrayList<String>();
                for (GeographicalValue geoVal : geoValues) {
                    if (condition.getCodesDimension().contains(geoVal.getCode())) {
                        newCodeDims.add(geoVal.getCode());
                    }
                }
                newCondition.setCodesDimension(newCodeDims);
            } else if (IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name().equals(condition.getDimensionId())) {
                List<String> newCodeDims = new ArrayList<String>();
                for (TimeValue timeVal : timeValues) {
                    if (condition.getCodesDimension().contains(timeVal.getTimeValue())) {
                        newCodeDims.add(timeVal.getTimeValue());
                    }
                }
                newCondition.setCodesDimension(newCodeDims);
            } else { // Other dimensions get their conditions unmodified
                newCondition.setCodesDimension(condition.getCodesDimension());
            }
            newConditions.add(newCondition);
        }
        return newConditions;
    }

    /* Private methods */

    private List<String> findCodesForDimensionInIndicator(IndicatorVersion indicatorVersion, IndicatorDataDimensionTypeEnum dimension) throws ApplicationException {
        List<String> codes = new ArrayList<String>();
        List<ConditionObservationDto> conditions = datasetRepositoriesServiceFacade.findCodeDimensions(indicatorVersion.getDataRepositoryId());
        for (ConditionObservationDto condition : conditions) {
            if (condition.getCodesDimension().size() > 0) {
                if (dimension.name().equals(condition.getCodesDimension().get(0).getDimensionId())) {
                    for (CodeDimensionDto codeDim : condition.getCodesDimension()) {
                        codes.add(codeDim.getCodeDimensionId());
                    }
                }
            }
        }
        return codes;
    }

    private Map<String, ObservationDto> findObservationsByDimensions(ServiceContext ctx, IndicatorVersion indicatorVersion, List<ConditionDimensionDto> conditions) throws MetamacException,
            ApplicationException {
        String datasetId = indicatorVersion.getDataRepositoryId();
        if (datasetId != null) {
            return datasetRepositoriesServiceFacade.findObservationsByDimensions(datasetId, conditions);
        } else {
            throw new MetamacException(ServiceExceptionType.INDICATOR_NOT_POPULATED, indicatorVersion.getIndicator().getUuid(), indicatorVersion.getVersionNumber());
        }
    }

    private Map<String, ObservationExtendedDto> findObservationsExtendedByDimensions(ServiceContext ctx, IndicatorVersion indicatorVersion, List<ConditionDimensionDto> conditions)
            throws MetamacException, ApplicationException {
        String datasetId = indicatorVersion.getDataRepositoryId();
        if (datasetId != null) {
            return datasetRepositoriesServiceFacade.findObservationsExtendedByDimensions(datasetId, conditions);
        } else {
            throw new MetamacException(ServiceExceptionType.INDICATOR_NOT_POPULATED, indicatorVersion.getIndicator().getUuid(), indicatorVersion.getVersionNumber());
        }
    }

    private IndicatorVersion getIndicatorPublishedVersion(String indicatorUuid) throws MetamacException {
        Indicator indicator = getIndicatorRepository().retrieveIndicator(indicatorUuid);
        if (indicator.getIsPublished()) {
            return getIndicatorVersion(indicatorUuid, indicator.getDiffusionVersion().getVersionNumber());
        } else {
            throw new MetamacException(ServiceExceptionType.INDICATOR_IN_DIFFUSION_NOT_FOUND, indicatorUuid);
        }
    }

    private IndicatorVersion getIndicatorVersion(String indicatorUuid, String indicatorVersionNumber) throws MetamacException {
        IndicatorVersion indicatorVersion = getIndicatorVersionRepository().retrieveIndicatorVersion(indicatorUuid, indicatorVersionNumber);
        if (indicatorVersion != null) {
            return indicatorVersion;
        } else {
            throw new MetamacException(ServiceExceptionType.INDICATOR_VERSION_NOT_FOUND, indicatorUuid, indicatorVersionNumber);
        }
    }

    private IndicatorInstance getIndicatorInstance(String indicatorInstanceUuid) throws MetamacException {
        IndicatorInstance indInstance = getIndicatorInstanceRepository().findIndicatorInstance(indicatorInstanceUuid);
        if (indInstance != null) {
            return indInstance;
        } else {
            throw new MetamacException(ServiceExceptionType.INDICATOR_INSTANCE_NOT_FOUND, indicatorInstanceUuid);
        }
    }

    private void checkDataSourcesDataCompatibility(List<DataSource> dataSources, Map<String, Data> dataCache) throws MetamacException {
        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        for (DataSource dataSource : dataSources) {
            Data data = dataCache.get(dataSource.getDataGpeUuid());
            exceptionItems.addAll(DataSourceCompatibilityChecker.check(dataSource, data));
        }
        if (exceptionItems.size() > 0) {
            throw new MetamacException(exceptionItems);
        }
    }

    /* Mark only diffusionVersion */
    private void markIndicatorsVersionWhichNeedsUpdate(ServiceContext ctx, Date lastQuery) throws MetamacException {
        Date newQueryDate = Calendar.getInstance().getTime();
        List<String> dataDefinitionsUuids = null;
        try {
            dataDefinitionsUuids = getDataGpeRepository().findDataDefinitionsWithDataUpdatedAfter(lastQuery);
        } catch (Exception e) {
            throw new MetamacException(e, ServiceExceptionType.DATA_UPDATE_INDICATORS_GPE_CHECK_ERROR);
        }

        List<IndicatorVersion> pendingIndicators = getIndicatorVersionRepository().findIndicatorsVersionLinkedToAnyDataGpeUuids(dataDefinitionsUuids);
        markIndicatorsNeedsUpdateTransactional(ctx, pendingIndicators);
        getIndicatorsConfigurationService().setLastSuccessfulGpeQueryDate(ctx, newQueryDate);
    }

    // No more inconsistent data, no more needs update
    private void markIndicatorVersionAsDataUpdated(IndicatorVersion indicatorVersion) {
        indicatorVersion.setNeedsUpdate(Boolean.FALSE);
        indicatorVersion.setInconsistentData(Boolean.FALSE);
        getIndicatorVersionRepository().save(indicatorVersion);
    }

    @Transactional(value = "txManager")
    private void markIndicatorsNeedsUpdateTransactional(ServiceContext ctx, List<IndicatorVersion> indicatorsVersion) throws MetamacException {
        for (IndicatorVersion indicatorVersion : indicatorsVersion) {
            indicatorVersion.setNeedsUpdate(Boolean.TRUE);
            getIndicatorVersionRepository().save(indicatorVersion);
        }
    }

    /*
     * Given an indicator, the old dataset repository is replaced and deleted and a new one is assigned
     */
    private IndicatorVersion setDatasetRepositoryDeleteOldOne(IndicatorVersion indicatorVersion, DatasetRepositoryDto datasetRepoDto) throws MetamacException {
        String oldDatasetId = indicatorVersion.getDataRepositoryId();
        indicatorVersion.setDataRepositoryId(datasetRepoDto.getDatasetId());
        indicatorVersion.setDataRepositoryTableName(datasetRepoDto.getTableName());
        IndicatorVersion updatedIndicatorVerison = getIndicatorVersionRepository().save(indicatorVersion);
        if (oldDatasetId != null) {
            try {
                datasetRepositoriesServiceFacade.deleteDatasetRepository(oldDatasetId);
            } catch (ApplicationException e) {
                LOG.error("Old dataset repository could not be deleted", e);
            }
        }
        return updatedIndicatorVerison;
    }

    /*
     * Given a DataOperation and its Data, A list of observations is created
     */
    private List<ObservationExtendedDto> createObservationsFromDataOperationData(DataOperation dataOperation, Data data, String datasetId) throws MetamacException {
        // Get geographic and time values
        List<String> geoValues = getGeographicValue(dataOperation, data);
        List<String> timeValues = getTimeValue(dataOperation, data);

        List<ObservationExtendedDto> observations = new ArrayList<ObservationExtendedDto>();
        for (String geoVal : geoValues) {
            for (String timeVal : timeValues) {
                // Map for querying the data from the json
                Map<String, String> varCodesForQuery = new HashMap<String, String>();

                // Only include geographical and time values if a variable has been selected
                if (dataOperation.hasGeographicalVariable()) {
                    varCodesForQuery.put(dataOperation.getGeographicalVariable(), geoVal);
                }
                if (dataOperation.hasTimeVariable()) {
                    varCodesForQuery.put(dataOperation.getTimeVariable(), timeVal);
                }
                for (DataSourceVariable var : dataOperation.getOtherVariables()) {
                    varCodesForQuery.put(var.getVariable(), var.getCategory());
                }

                ObservationExtendedDto observation = null;
                if (RateDerivationMethodTypeEnum.LOAD.equals(dataOperation.getMethodType())) {
                    observation = getObservationValue(dataOperation, data, varCodesForQuery, geoVal, timeVal);
                } else if (RateDerivationMethodTypeEnum.CALCULATE.equals(dataOperation.getMethodType())) {
                    observation = getCalculatedValue(dataOperation, datasetId, geoVal, timeVal);
                } else {
                    throw new MetamacException(ServiceExceptionType.DATA_POPULATE_UNKNOWN_METHOD_TYPE, dataOperation.getMethodType());
                }
                
                //check observation length
                if (observation.getPrimaryMeasure() != null && observation.getPrimaryMeasure().length() > MAX_MEASURE_LENGTH) {
                    throw new MetamacException(ServiceExceptionType.DATA_POPULATE_WRONG_OBSERVATION_VALUE_LENGTH,dataOperation.getDataSourceUuid(), observation.getPrimaryMeasure(),MAX_MEASURE_LENGTH);
                }
                observations.add(observation);
            }
        }
        return observations;
    }

    private void deleteDatasetRepositoryIfExists(DatasetRepositoryDto datasetRepositoryDto) {
        try {
            if (datasetRepositoryDto != null && datasetRepositoryDto.getDatasetId() != null) {
                datasetRepositoriesServiceFacade.deleteDatasetRepository(datasetRepositoryDto.getDatasetId());
            }
        } catch (ApplicationException e) {
            LOG.error("Temporal Dataset repository could not be deleted DatasetId:" + datasetRepositoryDto.getDatasetId(), e);
        }
    }

    /*
     * Retrieve data from IndicatorDataProvider
     * The data is not always asked to the service, sometimes the requested data is in the data cache.
     */
    private Map<String, Data> retrieveDatasFromProvider(ServiceContext ctx, List<DataSource> dataSources) throws MetamacException {
        Map<String, Data> dataCache = new HashMap<String, Data>();
        for (DataSource dataSource : dataSources) {
            try {
                Data data = dataCache.get(dataSource.getDataGpeUuid());
                if (data == null) {
                    String json = getIndicatorsDataProviderService().retrieveDataJson(ctx, dataSource.getDataGpeUuid());
                    if (json == null) {
                        throw new MetamacException(ServiceExceptionType.DATA_POPULATE_RETRIEVE_DATA_EMPTY, dataSource.getDataGpeUuid(), dataSource.getUuid());
                    }
                    data = jsonToData(json);
                    dataCache.put(dataSource.getDataGpeUuid(), data);
                }
            } catch (MetamacException e) {
                throw e;
            } catch (Exception e) {
                throw new MetamacException(e, ServiceExceptionType.DATA_POPULATE_RETRIEVE_DATA_ERROR, dataSource.getDataGpeUuid(), dataSource.getUuid());
            }
        }
        return dataCache;
    }

    /*
     * Returns the geographic value whether a geographical variable has been selected or a value
     * has been fixed
     */
    private List<String> getGeographicValue(DataOperation dataOperation, Data data) throws MetamacException {
        List<String> geoValues = null;
        // Check whether geographical variable is fixed or not
        if (dataOperation.hasGeographicalVariable()) {
            geoValues = new ArrayList<String>();
            List<String> geoCodesProvided = data.getValueCodes().get(dataOperation.getGeographicalVariable());
            List<String> unknownCodes = new ArrayList<String>();
            for (String geoCode : geoCodesProvided) {
                GeographicalValue geoValue = getGeographicalValueRepository().findGeographicalValueByCode(geoCode);
                if (geoValue != null) {
                    geoValues.add(geoCode);
                } else {
                    unknownCodes.add(geoCode);
                }
            }
            if (unknownCodes.size() > 0) {
                String codes = StringUtils.join(unknownCodes, ",");
                throw new MetamacException(ServiceExceptionType.DATA_POPULATE_UNKNOWN_GEOGRAPHIC_VALUE, dataOperation.getDataSourceUuid(), dataOperation.getDataSource().getDataGpeUuid(), codes);
            }
        } else {
            geoValues = new ArrayList<String>();
            GeographicalValue geoVal = dataOperation.getGeographicalValue();
            geoValues.add(geoVal.getCode());
        }
        return geoValues;
    }

    /*
     * Returns the time value whether a geographical variable has been selected or a value
     * has been fixed
     */
    private List<String> getTimeValue(DataOperation dataOperation, Data data) throws MetamacException {
        List<String> timeValues = null;
        // Check whether time variable is fixed or not
        if (dataOperation.hasTimeVariable()) {
            timeValues = data.getValueCodes().get(dataOperation.getTimeVariable());

            timeValues = new ArrayList<String>();
            List<String> timeCodesProvided = data.getValueCodes().get(dataOperation.getTimeVariable());
            List<String> unknownCodes = new ArrayList<String>();
            for (String timeCode : timeCodesProvided) {
                if (TimeVariableUtils.isTimeValue(timeCode)) {
                    timeValues.add(timeCode);
                } else {
                    unknownCodes.add(timeCode);
                }
            }
            if (unknownCodes.size() > 0) {
                String codes = StringUtils.join(unknownCodes, ",");
                throw new MetamacException(ServiceExceptionType.DATA_POPULATE_UNKNOWN_TIME_VALUE, dataOperation.getDataSourceUuid(), dataOperation.getDataSource().getDataGpeUuid(), codes);
            }

        } else {
            timeValues = new ArrayList<String>();
            timeValues.add(dataOperation.getTimeValue());
        }
        return timeValues;
    }

    /*
     * Creates Indicator's basic structure in dataset repository
     */
    private DatasetRepositoryDto createDatasetRepositoryDefinition(String indicatorUuid, String indicatorVersion) throws MetamacException {
        DatasetRepositoryDto datasetRepoDto = new DatasetRepositoryDto();
        datasetRepoDto.setDatasetId("dataset:" + UUID.randomUUID().toString());
        datasetRepoDto.setMaxAttributesObservation(2);
        datasetRepoDto.getDimensions().add(GEO_DIM);
        datasetRepoDto.getDimensions().add(TIME_DIM);
        datasetRepoDto.getDimensions().add(MEASURE_DIM);

        List<String> languages = new ArrayList<String>();
        languages.add("es");
        datasetRepoDto.setLanguages(languages);

        try {
            datasetRepoDto = datasetRepositoriesServiceFacade.createDatasetRepository(datasetRepoDto);
        } catch (ApplicationException e) {
            throw new MetamacException(ServiceExceptionType.DATA_POPULATE_DATASETREPO_CREATE_ERROR, indicatorUuid, indicatorVersion);
        }
        return datasetRepoDto;
    }

    private ObservationExtendedDto getObservationValue(DataOperation dataOperation, Data data, Map<String, String> varCodes, String geoValue, String timeValue) throws MetamacException {
        DataContent content = getValue(dataOperation, data, varCodes);
        String value = content.getValue();

        ObservationExtendedDto observation = new ObservationExtendedDto();
        observation.addCodesDimension(new CodeDimensionDto(GEO_DIM, geoValue));
        observation.addCodesDimension(new CodeDimensionDto(TIME_DIM, timeValue));
        observation.addCodesDimension(new CodeDimensionDto(MEASURE_DIM, dataOperation.getMeasureDimension().name()));
        observation.addAttribute(createAttribute(CODE_ATTR, CODE_ATTR_LOC, dataOperation.getDataSourceUuid()));

        // Check for dotted notation
        if (isSpecialString(value)) {
            String text = getSpecialStringMeaning(value);
            //Some Special Strings may not need to create an attribute 
            if (!StringUtils.isEmpty(text)) {
                observation.addAttribute(createAttribute(OBS_CONF_ATTR, OBS_CONF_LOC, getSpecialStringMeaning(value)));
            }
            observation.setPrimaryMeasure(null);
        } else {
            observation.setPrimaryMeasure(value);
        }
        
        return observation;
    }
    /*
     * Get value has to take a look to method type and methd
     * For LOAD method type method can be OBS_VALUE or A category name for ContVariable
     */
    private DataContent getValue(DataOperation dataOperation, Data data, Map<String, String> varCodes) throws MetamacException {
        Map<String, String> codes = new HashMap<String, String>(varCodes);
        if (data.hasContVariable()) {
            if (DataSourceDto.isObsValue(dataOperation.getMethod())) { // INCOMPATIBLE DataSource, got obs_value but the query has contvariable
                throw new MetamacException(ServiceExceptionType.DATA_POPULATE_INVALID_CONTVARIABLE_LOAD_METHOD, dataOperation.getMethod());
            } else {
                List<String> contVarCodes = data.getValueCodes().get(data.getContVariable());
                if (contVarCodes.contains(dataOperation.getMethod())) {
                    codes.put(data.getContVariable(), dataOperation.getMethod());
                    return data.getDataContent(codes);
                } else {
                    throw new MetamacException(ServiceExceptionType.DATA_POPULATE_INVALID_CONTVARIABLE_LOAD_METHOD, dataOperation.getMethod());
                }
            }
        } else {
            if (DataSourceDto.isObsValue(dataOperation.getMethod())) {
                return data.getDataContent(codes);
            } else {
                throw new MetamacException(ServiceExceptionType.DATA_POPULATE_INVALID_NOCONTVARIABLE_LOAD_METHOD, dataOperation.getMethod());
            }
        }
    }

    /*
     * Get value has to take a look to method type and methd
     * For LOAD method type method can be OBS_VALUE or A category name for ContVariable
     */
    private ObservationExtendedDto getCalculatedValue(DataOperation dataOperation, String datasetId, String geoValue, String timeValue) throws MetamacException {
        // Create base for observation
        ObservationExtendedDto observation = new ObservationExtendedDto();
        observation.addCodesDimension(new CodeDimensionDto(GEO_DIM, geoValue));
        observation.addCodesDimension(new CodeDimensionDto(TIME_DIM, timeValue));
        observation.addCodesDimension(new CodeDimensionDto(MEASURE_DIM, dataOperation.getMeasureDimension().name()));
        observation.addAttribute(createAttribute(CODE_ATTR, CODE_ATTR_LOC, dataOperation.getDataSourceUuid()));

        String previousTimeValue = null;
        if (dataOperation.isAnnualMethod()) {
            previousTimeValue = TimeVariableUtils.calculatePreviousYearTimeValue(timeValue);
        } else if (dataOperation.isInterPeriodMethod()) {
            previousTimeValue = TimeVariableUtils.calculatePreviousTimeValue(timeValue);
        } else {
            throw new MetamacException(ServiceExceptionType.DATA_POPULATE_UNKNOWN_METHOD_TYPE, dataOperation.getMethodType());
        }

        if (previousTimeValue == null) {
            observation.addAttribute(createAttribute(OBS_CONF_ATTR, OBS_CONF_LOC, getSpecialStringMeaning(DOT_NOT_APPLICABLE)));
            observation.setPrimaryMeasure(null);
            return observation;
        }

        ConditionDimensionDto geoConditionDto = createCondition(GEO_DIM, geoValue);
        ConditionDimensionDto timeConditionDto = createCondition(TIME_DIM, previousTimeValue, timeValue);
        ConditionDimensionDto measureConditionDto = createCondition(MEASURE_DIM, MeasureDimensionTypeEnum.ABSOLUTE.name());
        List<ConditionDimensionDto> conditions = Arrays.asList(geoConditionDto, timeConditionDto, measureConditionDto);
        Map<String, ObservationExtendedDto> observationsMap = null;
        try {
            observationsMap = datasetRepositoriesServiceFacade.findObservationsExtendedByDimensions(datasetId, conditions);
        } catch (ApplicationException e) {
            throw new MetamacException(e, ServiceExceptionType.DATA_POPULATE_DATASETREPO_FIND_ERROR);
        }

        ObservationExtendedDto currentObs = getObservationExtendedFromMap(geoValue, timeValue, MeasureDimensionTypeEnum.ABSOLUTE.name(), observationsMap);
        ObservationExtendedDto previousObs = getObservationExtendedFromMap(geoValue, previousTimeValue, MeasureDimensionTypeEnum.ABSOLUTE.name(), observationsMap);

        /* Some observations were not found */
        if (currentObs == null || previousObs == null) {
            observation.setPrimaryMeasure(null);
            observation.addAttribute(createAttribute(OBS_CONF_ATTR, OBS_CONF_LOC, getSpecialStringMeaning(DOT_UNAVAILABLE)));
            return observation;
        }

        /* Some observations have dot notation values */
        if (currentObs.getPrimaryMeasure() == null || previousObs.getPrimaryMeasure() == null) {
            observation.setPrimaryMeasure(null);
            observation.addAttribute(mergeObsConfAttribute(currentObs, previousObs));
            return observation;
        } else { // Calculate
            Double currentValue = null;
            Double previousValue = null;
            try {
                String currentValStr = currentObs.getPrimaryMeasure();
                String previousValStr = previousObs.getPrimaryMeasure();
                currentValue = Double.parseDouble(currentValStr);
                previousValue = Double.parseDouble(previousValStr);
            } catch (NumberFormatException e) {
                throw new MetamacException(ServiceExceptionType.DATA_POPULATE_OBSERVATION_CALCULATE_ERROR, currentObs.getPrimaryMeasure(), previousObs.getPrimaryMeasure());
            }
            Double calculatedValue = null;
            if (dataOperation.isPercentageMethod()) {
                if (Math.abs(previousValue) < ZERO_RANGE) {
                    observation.setPrimaryMeasure(null);
                    observation.addAttribute(createAttribute(OBS_CONF_ATTR, OBS_CONF_LOC, getSpecialStringMeaning(DOT_UNAVAILABLE)));
                    return observation;
                }
                Quantity quantity = dataOperation.getQuantity();
                calculatedValue = ((currentValue - previousValue) / previousValue) * quantity.getUnitMultiplier();
            } else if (dataOperation.isPuntualMethod()) {
                calculatedValue = currentValue - previousValue;
            } else {
                throw new MetamacException(ServiceExceptionType.DATA_POPULATE_UNKNOWN_METHOD_TYPE, dataOperation.getMethodType());
            }
            String formattedValue = formatCalculatedValue(calculatedValue, dataOperation);
            observation.setPrimaryMeasure(formattedValue);
        }
        return observation;
    }

    private ObservationExtendedDto getObservationExtendedFromMap(String geoValue, String timeValue, String measureValue, Map<String, ObservationExtendedDto> observationsMap) {
        String generatedKey = generateObservationUniqueKey(geoValue, timeValue, measureValue);
        return observationsMap.get(generatedKey);
    }

    private String formatCalculatedValue(Double value, DataOperation dataOperation) {
        DecimalFormat formatter = (DecimalFormat) (DecimalFormat.getInstance(Locale.ENGLISH));
        formatter.setGroupingUsed(false); // DO NOT use thousands separator
        if (dataOperation.shouldBeRounded()) {
            if (RateDerivationRoundingEnum.UPWARD.equals(dataOperation.getRateRounding())) {
                formatter.setRoundingMode(RoundingMode.HALF_UP);
            } else if (RateDerivationRoundingEnum.DOWN.equals(dataOperation.getRateRounding())) {
                formatter.setRoundingMode(RoundingMode.DOWN); // Truncate
            }
            formatter.setMaximumFractionDigits(dataOperation.getQuantity().getDecimalPlaces());
            formatter.setMinimumFractionDigits(dataOperation.getQuantity().getDecimalPlaces());
            return formatter.format(value);
        } else {
            return formatter.format(value);
        }
    }

    /*
     * Transform list of datasources to a list of simpler operations, sorted by precedence
     * Load operations must be executed prior calculated operation.
     */
    private List<DataOperation> transformDataSourcesForProcessing(List<DataSource> dataSources) {
        LinkedList<DataOperation> dataOperations = new LinkedList<DataOperation>();
        // Load operations are posisionated first
        for (DataSource dataSource : dataSources) {
            if (dataSource.getAbsoluteMethod() != null) {
                DataOperation dataOp = new DataOperation(dataSource, MeasureDimensionTypeEnum.ABSOLUTE);
                dataOperations.addFirst(dataOp);
            }
            if (dataSource.getAnnualPercentageRate() != null) {
                DataOperation dataOp = new DataOperation(dataSource, MeasureDimensionTypeEnum.ANNUAL_PERCENTAGE_RATE);
                if (RateDerivationMethodTypeEnum.LOAD.equals(dataOp.getMethodType())) {
                    dataOperations.addFirst(dataOp);
                } else {
                    dataOperations.addLast(dataOp);
                }
            }
            if (dataSource.getAnnualPuntualRate() != null) {
                DataOperation dataOp = new DataOperation(dataSource, MeasureDimensionTypeEnum.ANNUAL_PUNTUAL_RATE);
                if (RateDerivationMethodTypeEnum.LOAD.equals(dataOp.getMethodType())) {
                    dataOperations.addFirst(dataOp);
                } else {
                    dataOperations.addLast(dataOp);
                }
            }
            if (dataSource.getInterperiodPercentageRate() != null) {
                DataOperation dataOp = new DataOperation(dataSource, MeasureDimensionTypeEnum.INTERPERIOD_PERCENTAGE_RATE);
                if (RateDerivationMethodTypeEnum.LOAD.equals(dataOp.getMethodType())) {
                    dataOperations.addFirst(dataOp);
                } else {
                    dataOperations.addLast(dataOp);
                }
            }
            if (dataSource.getInterperiodPuntualRate() != null) {
                DataOperation dataOp = new DataOperation(dataSource, MeasureDimensionTypeEnum.INTERPERIOD_PUNTUAL_RATE);
                if (RateDerivationMethodTypeEnum.LOAD.equals(dataOp.getMethodType())) {
                    dataOperations.addFirst(dataOp);
                } else {
                    dataOperations.addLast(dataOp);
                }
            }
        }
        return dataOperations;
    }

    /*
     * Gets dot convention descriptions
     */
    public static String getSpecialStringMeaning(String dottedStr) {
        return SPECIAL_STRING_MAPPING.get(dottedStr);
    }

    private boolean isSpecialString(String str) {
        return SPECIAL_STRING_MAPPING.containsKey(str);
    }

    /*
     * Helper for attributes
     */
    private AttributeBasicDto createAttribute(String id, String locale, String value) {
        AttributeBasicDto attributeBasicDto = new AttributeBasicDto();
        attributeBasicDto.setAttributeId(id);
        InternationalStringDto intStr = new InternationalStringDto();
        LocalisedStringDto locStr = new LocalisedStringDto();
        locStr.setLocale(locale);
        locStr.setLabel(value);
        intStr.addText(locStr);
        attributeBasicDto.setValue(intStr);
        return attributeBasicDto;
    }

    /*
     * Helper for merge Two OBS_CONF attributes into one
     */
    private AttributeBasicDto mergeObsConfAttribute(ObservationExtendedDto obs1, ObservationExtendedDto obs2) {
        AttributeBasicDto attribute1 = null;
        AttributeBasicDto attribute2 = null;

        if (obs1.getPrimaryMeasure() == null) {
            attribute1 = getAttribute(OBS_CONF_ATTR, obs1);
        }
        if (obs2.getPrimaryMeasure() == null) {
            attribute2 = getAttribute(OBS_CONF_ATTR, obs2);
        }

        if (attribute1 != null && attribute2 != null) {
            return createAttribute(OBS_CONF_ATTR, OBS_CONF_LOC, attribute1.getValue().getLocalisedLabel(OBS_CONF_LOC) + ", " + attribute2.getValue().getLocalisedLabel(OBS_CONF_LOC));
        }
        if (attribute1 != null) {
            return createAttribute(OBS_CONF_ATTR, OBS_CONF_LOC, attribute1.getValue().getLocalisedLabel(OBS_CONF_LOC));
        }
        if (attribute2 != null) {
            return createAttribute(OBS_CONF_ATTR, OBS_CONF_LOC, attribute2.getValue().getLocalisedLabel(OBS_CONF_LOC));
        }
        return null;
    }

    /*
     * Helper for ConditionDimension
     */
    private ConditionDimensionDto createCondition(String dimensionId, String... codeDimensions) {
        ConditionDimensionDto condition = new ConditionDimensionDto();
        condition.setDimensionId(dimensionId);
        condition.setCodesDimension(Arrays.asList(codeDimensions));
        return condition;
    }

    /*
     * Helper for generate id for observation maps
     */
    private String generateObservationUniqueKey(String geoValue, String timeValue, String measureValue) {
        CodeDimensionDto geoDimCode = new CodeDimensionDto(GEO_DIM, geoValue);
        CodeDimensionDto timeDimCode = new CodeDimensionDto(TIME_DIM, timeValue);
        CodeDimensionDto measureDimCode = new CodeDimensionDto(MEASURE_DIM, measureValue);
        return DtoUtils.generateUniqueKey(Arrays.asList(geoDimCode, timeDimCode, measureDimCode));
    }

    /*
     * Helper to get certain attribute from an observation
     */
    private AttributeBasicDto getAttribute(String attributeId, ObservationExtendedDto observation) {
        for (AttributeBasicDto attr : observation.getAttributes()) {
            if (OBS_CONF_ATTR.equals(attr.getAttributeId())) {
                return attr;
            }
        }
        return null;
    }

    /*
     * Private methods that get info from jaxi
     */
    private DataStructure jsonToDataStructure(String json) throws Exception {
        DataStructure target = new DataStructure();
        target = mapper.readValue(json, DataStructure.class);
        return target;
    }

    /*
     * Private methods that get data from jaxi
     */
    private Data jsonToData(String json) throws Exception {
        Data target = new Data();
        target = mapper.readValue(json, Data.class);
        return target;
    }
}
