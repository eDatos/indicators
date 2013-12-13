package es.gobcan.istac.indicators.core.serviceimpl;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
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
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.dataset.repository.domain.AttributeAttachmentLevelEnum;
import com.arte.statistic.dataset.repository.dto.AttributeDto;
import com.arte.statistic.dataset.repository.dto.AttributeInstanceObservationDto;
import com.arte.statistic.dataset.repository.dto.CodeDimensionDto;
import com.arte.statistic.dataset.repository.dto.ConditionDimensionDto;
import com.arte.statistic.dataset.repository.dto.DatasetRepositoryDto;
import com.arte.statistic.dataset.repository.dto.InternationalStringDto;
import com.arte.statistic.dataset.repository.dto.LocalisedStringDto;
import com.arte.statistic.dataset.repository.dto.ObservationDto;
import com.arte.statistic.dataset.repository.dto.ObservationExtendedDto;
import com.arte.statistic.dataset.repository.service.DatasetRepositoriesServiceFacade;
import com.arte.statistic.dataset.repository.util.DtoUtils;

import es.gobcan.istac.indicators.core.constants.IndicatorsConfigurationConstants;
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
import es.gobcan.istac.indicators.core.domain.IndicatorInstanceLastValue;
import es.gobcan.istac.indicators.core.domain.IndicatorInstanceLastValueCache;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionInformation;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionLastValue;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionLastValueCache;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystem;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersionInformation;
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
import es.gobcan.istac.indicators.core.enume.domain.VersionTypeEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionParameters;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceimpl.util.DataOperation;
import es.gobcan.istac.indicators.core.serviceimpl.util.DataSourceCompatibilityChecker;
import es.gobcan.istac.indicators.core.serviceimpl.util.InvocationValidator;
import es.gobcan.istac.indicators.core.serviceimpl.util.ServiceUtils;
import es.gobcan.istac.indicators.core.serviceimpl.util.TimeVariableUtils;

/**
 * Implementation of IndicatorsDataService.
 */
@Service("indicatorsDataService")
public class IndicatorsDataServiceImpl extends IndicatorsDataServiceImplBase {

    @Autowired
    ConfigurationService                     configurationService;

    private final Logger                     LOG                       = LoggerFactory.getLogger(IndicatorsDataServiceImpl.class);

    public static final String               GEO_DIMENSION             = IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name();
    public static final String               TIME_DIMENSION            = IndicatorDataDimensionTypeEnum.TIME.name();
    public static final String               MEASURE_DIMENSION         = IndicatorDataDimensionTypeEnum.MEASURE.name();
    public static final String               CODE_ATTRIBUTE            = IndicatorDataAttributeTypeEnum.CODE.name();
    public static final String               OBS_CONF_ATTRIBUTE        = IndicatorDataAttributeTypeEnum.OBS_CONF.name();
    public static final String               DATASET_REPOSITORY_LOCALE = "es";

    public static final String               DOT_NOT_APPLICABLE        = ".";
    public static final String               DOT_UNAVAILABLE           = "..";
    public static final Double               ZERO_RANGE                = 1E-6;
    public static final int                  MAX_MEASURE_LENGTH        = 50;

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

    private final ObjectMapper               mapper                    = new ObjectMapper();

    public IndicatorsDataServiceImpl() {
    }

    @Override
    public List<String> retrieveDataDefinitionsOperationsCodes(ServiceContext ctx) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveDataDefinitionsOperationsCodes(null);

        // Find db
        List<String> operationsCodes = getDataGpeRepository().findCurrentDataDefinitionsOperationsCodes();
        return operationsCodes;
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
    public List<DataDefinition> findDataDefinitionsByOperationCode(ServiceContext ctx, String operationCode) throws MetamacException {
        // Validation
        InvocationValidator.checkFindDataDefinitionsByOperationCode(operationCode, null);

        // Find db
        List<DataDefinition> result = getDataGpeRepository().findCurrentDataDefinitionsByOperationCode(operationCode);
        return result;
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
    public void populateIndicatorData(ServiceContext ctx, String indicatorUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkPopulateIndicatorData(indicatorUuid, null);

        Indicator indicator = getIndicatorRepository().retrieveIndicator(indicatorUuid);
        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        if (indicator.getIsPublished()) { // avoid populating archived
            try {
                indicator = populateIndicatorVersionData(ctx, indicatorUuid, indicator.getDiffusionVersion().getVersionNumber());
            } catch (MetamacException e) {
                exceptionItems.addAll(e.getExceptionItems());
            }
        }
        if (indicator.getProductionVersion() != null) {
            try {
                indicator = populateIndicatorVersionData(ctx, indicatorUuid, indicator.getProductionVersion().getVersionNumber());
            } catch (MetamacException e) {
                exceptionItems.addAll(e.getExceptionItems());
            }
        }

        if (exceptionItems.size() > 0) {
            throw new MetamacException(exceptionItems);
        }
    }

    @Override
    public Indicator populateIndicatorVersionData(ServiceContext ctx, String indicatorUuid, String indicatorVersionNumber) throws MetamacException {
        // Validation
        InvocationValidator.checkPopulateIndicatorVersionData(indicatorUuid, indicatorVersionNumber, null);

        IndicatorVersion indicatorVersion = getIndicatorVersionRepository().retrieveIndicatorVersion(indicatorUuid, indicatorVersionNumber);

        if (indicatorVersion != null) {
            Indicator indicator = indicatorVersion.getIndicator();
            if (shouldIndicatorVersionBePopulated(indicatorVersion)) {
                String diffusionVersion = indicator.getIsPublished() ? indicator.getDiffusionVersion().getVersionNumber() : null;

                onlyPopulateIndicatorVersion(ctx, indicatorUuid, indicatorVersionNumber);

                // After diffusion version's data is populated all related systems must update their versions
                if (indicatorVersion.getVersionNumber().equals(diffusionVersion) && indicator.getIsPublished()) {
                    indicator = changeDiffusionVersion(indicator);
                    // update system version
                    List<String> modifiedSystems = findAllIndicatorsSystemsDiffusionVersionWithIndicator(indicatorUuid);
                    changeVersionForModifiedIndicatorsSystems(modifiedSystems);

                    buildLastValuesCache(ctx, indicatorVersion);
                }
            } else {
                LOG.info("Skipping unnecesary data populate for indicator uuid:" + indicatorUuid + " version " + indicatorVersionNumber);
            }
            return indicator;
        } else {
            throw new MetamacException(ServiceExceptionType.INDICATOR_VERSION_NOT_FOUND, indicatorUuid, indicatorVersionNumber);
        }
    }

    private boolean shouldIndicatorVersionBePopulated(IndicatorVersion indicatorVersion) {
        if (indicatorVersion.getDataRepositoryId() == null || indicatorVersion.getNeedsUpdate() || indicatorVersion.getInconsistentData() || indicatorVersion.getLastPopulateDate() == null) {
            return true;
        }
        // All dataGpeUuids from indicator version
        List<String> dataGpeUuids = getDataSourceRepository().findDatasourceDataGpeUuidLinkedToIndicatorVersion(indicatorVersion.getId());
        List<String> dataDefinitionsUpdated = getDataGpeRepository().filterDataDefinitionsWithDataUpdatedAfter(dataGpeUuids, indicatorVersion.getLastPopulateDate().toDate());
        return dataDefinitionsUpdated.size() > 0;
    }

    @Override
    public void updateIndicatorsData(ServiceContext ctx) throws MetamacException {
        LOG.info("Starting Indicators data update process");

        // Validation
        InvocationValidator.checkUpdateIndicatorsData(null);

        Date lastQueryDate = getIndicatorsConfigurationService().retrieveLastSuccessfulGpeQueryDate(ctx);

        markIndicatorsVersionWhichNeedsUpdate(ctx, lastQueryDate);
        Set<String> modifiedSystems = new HashSet<String>();
        List<IndicatorVersion> pendingIndicators = getIndicatorVersionRepository().findIndicatorsVersionNeedsUpdate();
        for (IndicatorVersion indicatorVersion : pendingIndicators) {
            Indicator indicator = indicatorVersion.getIndicator();
            String diffusionVersion = indicator.getIsPublished() ? indicator.getDiffusionVersion().getVersionNumber() : null;

            String indicatorUuid = indicator.getUuid();
            if (indicatorVersion.getVersionNumber().equals(diffusionVersion)) {
                try {
                    onlyPopulateIndicatorVersion(ctx, indicatorUuid, diffusionVersion);
                    changeDiffusionVersion(indicator);
                    modifiedSystems.addAll(findAllIndicatorsSystemsDiffusionVersionWithIndicator(indicatorUuid));
                    buildLastValuesCache(ctx, indicatorVersion);
                } catch (MetamacException e) {
                    LOG.warn("Error populating indicator indicatorUuid:" + indicatorUuid, e);
                }
            }
        }
        changeVersionForModifiedIndicatorsSystems(modifiedSystems);
        LOG.info("Finished Indicators data update process");
    }

    private void changeVersionForModifiedIndicatorsSystems(Collection<String> modifiedSystems) {
        for (String systemUuid : modifiedSystems) {
            try {
                IndicatorsSystem indicatorsSystem = getIndicatorsSystemRepository().retrieveIndicatorsSystem(systemUuid);
                IndicatorsSystemVersionInformation diffusionVersionInfo = indicatorsSystem.getIsPublished() ? indicatorsSystem.getDiffusionVersion() : null;
                if (diffusionVersionInfo != null) {
                    String newDiffusionVersion = ServiceUtils.generateVersionNumber(diffusionVersionInfo.getVersionNumber(), VersionTypeEnum.MINOR);
                    IndicatorsSystemVersionInformation productionVersionInfo = indicatorsSystem.getProductionVersion();

                    // check version collision
                    if (productionVersionInfo != null && newDiffusionVersion.equals(productionVersionInfo.getVersionNumber())) {
                        IndicatorsSystemVersion productionVersion = getIndicatorsSystemVersionRepository().retrieveIndicatorsSystemVersion(systemUuid, productionVersionInfo.getVersionNumber());
                        String newProductionVersion = ServiceUtils.generateVersionNumber(productionVersionInfo.getVersionNumber(), VersionTypeEnum.MINOR);
                        // new production version, new update date
                        productionVersion.setVersionNumber(newProductionVersion);
                        productionVersion.setLastUpdated(new DateTime());
                        productionVersion = getIndicatorsSystemVersionRepository().save(productionVersion);

                        // change system relationship
                        indicatorsSystem.setProductionVersion(new IndicatorsSystemVersionInformation(productionVersion.getId(), productionVersion.getVersionNumber()));
                    }

                    // update diffusion version
                    IndicatorsSystemVersion diffusionVersion = getIndicatorsSystemVersionRepository().retrieveIndicatorsSystemVersion(systemUuid, diffusionVersionInfo.getVersionNumber());
                    diffusionVersion.setVersionNumber(newDiffusionVersion);
                    diffusionVersion.setLastUpdated(new DateTime());
                    diffusionVersion = getIndicatorsSystemVersionRepository().save(diffusionVersion);

                    // change system
                    indicatorsSystem.setDiffusionVersion(new IndicatorsSystemVersionInformation(diffusionVersion.getId(), diffusionVersion.getVersionNumber()));
                    getIndicatorsSystemRepository().save(indicatorsSystem);
                }
            } catch (MetamacException e) {
                LOG.warn("Error changing version for indicator system: " + systemUuid, e);
            }
        }
        LOG.info("Indicators System \"version change\" proccess has finished");
    }

    private List<String> findAllIndicatorsSystemsDiffusionVersionWithIndicator(String indicatorUuid) {
        return getIndicatorInstanceRepository().findIndicatorsSystemsPublishedWithIndicator(indicatorUuid);
    }

    private List<String> findAllIndicatorInstancesPublishedWithIndicator(String indicatorUuid) {
        return getIndicatorInstanceRepository().findIndicatorsInstancesInPublishedIndicatorSystemWithIndicator(indicatorUuid);
    }

    private void onlyPopulateIndicatorVersion(ServiceContext ctx, String indicatorUuid, String indicatorVersionNumber) throws MetamacException {

        DatasetRepositoryDto datasetRepoDto = null;
        try {
            IndicatorVersion indicatorVersion = getIndicatorVersionRepository().retrieveIndicatorVersion(indicatorUuid, indicatorVersionNumber);
            List<DataSource> dataSources = indicatorVersion.getDataSources();

            checkIndicatorVersionPopulateCapabilities(indicatorVersion);

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
                datasetRepositoriesServiceFacade.createOrUpdateObservationsExtended(datasetRepoDto.getDatasetId(), observations);
            }
            // Replace the whole dataset
            indicatorVersion = setDatasetRepositoryDeleteOldOne(indicatorVersion, datasetRepoDto);

            // No more inconsistent data, no more needs update
            markIndicatorVersionAsDataUpdated(indicatorVersion);

            // Update view if it's necessary
            manageDatabaseViewForLastVersion(indicatorVersion);
        } catch (Exception e) {
            deleteDatasetRepositoryIfExists(datasetRepoDto);
            if (e instanceof MetamacException) {
                throw (MetamacException) e;
            } else {
                throw new MetamacException(e, ServiceExceptionType.DATA_POPULATE_ERROR, indicatorUuid, indicatorVersionNumber);
            }
        }
    }

    private void manageDatabaseViewForLastVersion(IndicatorVersion indicatorVersion) {
        if (indicatorVersion.getIsLastVersion()) {
            createOrReplaceLastVersionDatabaseView(indicatorVersion);
            assignIndicatorDataOracleRolePermissionsToView(indicatorVersion.getIndicator().getViewCode());
        }
    }

    private void assignIndicatorDataOracleRolePermissionsToView(String viewCode) {
        try {
            datasetRepositoriesServiceFacade.assignRolePermissionsToSelectDatasetView(getDataViewsRole(), viewCode);
        } catch (Exception e) {
            LOG.error("Error assigning SELECT permission to " + getDataViewsRole() + " over " + viewCode);
        }
    }

    private void createOrReplaceLastVersionDatabaseView(IndicatorVersion indicatorVersion) {
        try {
            datasetRepositoriesServiceFacade.createOrReplaceDatasetRepositoryView(indicatorVersion.getDataRepositoryId(), indicatorVersion.getIndicator().getViewCode());
        } catch (Exception e) {
            LOG.error("Error creating or replacing view " + indicatorVersion.getIndicator().getViewCode() + " for datasetRepositoryTableName " + indicatorVersion.getDataRepositoryTableName()
                    + " related with indicatorVersionUuid " + indicatorVersion.getUuid());
        }
    }

    private Indicator changeDiffusionVersion(Indicator indicator) throws MetamacException {
        IndicatorVersionInformation diffusionVersionInfo = indicator.getIsPublished() ? indicator.getDiffusionVersion() : null;
        if (diffusionVersionInfo != null) {
            String nextDiffusionVersionNumber = ServiceUtils.generateVersionNumber(diffusionVersionInfo.getVersionNumber(), VersionTypeEnum.MINOR);
            // Check if new version number is the same as production version
            IndicatorVersionInformation productionVersionInfo = indicator.getProductionVersion();
            if (productionVersionInfo != null && productionVersionInfo.getVersionNumber().equals(nextDiffusionVersionNumber)) {
                String nextProductionVersionNumber = ServiceUtils.generateVersionNumber(productionVersionInfo.getVersionNumber(), VersionTypeEnum.MINOR);
                IndicatorVersion productionVersion = getIndicatorVersion(indicator.getUuid(), productionVersionInfo.getVersionNumber());
                productionVersion.setVersionNumber(nextProductionVersionNumber);
                productionVersion.setUpdateDate(new DateTime());
                productionVersion = getIndicatorVersionRepository().save(productionVersion);
                indicator.setProductionVersion(new IndicatorVersionInformation(productionVersion.getId(), productionVersion.getVersionNumber()));
            }

            // update diffusion version
            IndicatorVersion diffusionVersion = getIndicatorVersion(indicator.getUuid(), diffusionVersionInfo.getVersionNumber());
            diffusionVersion.setVersionNumber(nextDiffusionVersionNumber);
            diffusionVersion.setUpdateDate(new DateTime());
            diffusionVersion = getIndicatorVersionRepository().save(diffusionVersion);
            indicator.setDiffusionVersion(new IndicatorVersionInformation(diffusionVersion.getId(), diffusionVersion.getVersionNumber()));
            indicator = getIndicatorRepository().save(indicator);
        }
        return indicator;
    }

    @Override
    public void deleteIndicatorVersionData(ServiceContext ctx, String indicatorUuid, String indicatorVersionNumber) throws MetamacException {
        // Validation
        InvocationValidator.checkDeleteIndicatorData(indicatorUuid, indicatorVersionNumber, null);

        try {
            IndicatorVersion indicatorVersion = getIndicatorVersionRepository().retrieveIndicatorVersion(indicatorUuid, indicatorVersionNumber);

            if (indicatorVersion.getDataRepositoryId() != null) {
                datasetRepositoriesServiceFacade.deleteDatasetRepository(indicatorVersion.getDataRepositoryId());
                indicatorVersion.setDataRepositoryId(null);
                indicatorVersion.setDataRepositoryTableName(null);
                getIndicatorVersionRepository().save(indicatorVersion);

                deleteIndicatorVersionLastValuesCache(indicatorVersion);
            } else {
                throw new MetamacException(ServiceExceptionType.INDICATOR_VERSION_NO_DATA, indicatorUuid, indicatorVersionNumber);
            }
        } catch (Exception e) {
            if (e instanceof MetamacException) {
                throw (MetamacException) e;
            } else {
                throw new MetamacException(e, ServiceExceptionType.DATA_DELETE_ERROR, indicatorUuid, indicatorVersionNumber);
            }
        }
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

                List<GeographicalValue> geographicalValues = getGeographicalValueRepository().findGeographicalValuesByCodes(geoCodesInIndicator);

                for (GeographicalValue geographicalValue : geographicalValues) {
                    geoGranularitiesInIndicator.add(geographicalValue.getGranularity());
                }
                return new ArrayList<GeographicalGranularity>(geoGranularitiesInIndicator);
            } catch (ApplicationException e) {
                throw new MetamacException(e, ServiceExceptionType.INDICATOR_FIND_DIMENSION_CODES_ERROR, indicatorVersion.getIndicator().getUuid(),
                        ServiceExceptionParameters.INDICATOR_DATA_DIMENSION_TYPE_GEOGRAPHICAL);
            }
        } else {
            throw new MetamacException(ServiceExceptionType.INDICATOR_NOT_POPULATED, indicatorVersion.getIndicator().getUuid(), indicatorVersion.getVersionNumber());
        }
    }

    @Override
    public List<GeographicalGranularity> retrieveGeographicalGranularitiesInIndicatorInstanceWithPublishedIndicator(ServiceContext ctx, String indicatorInstanceUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveGeographicalGranularitiesInIndicatorInstance(indicatorInstanceUuid, null);

        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);
        IndicatorVersion indicatorVersion = getIndicatorPublishedVersion(indInstance.getIndicator().getUuid());

        return calculateGeographicalGranularitiesInIndicatorInstanceLinkedToIndicatorVersion(ctx, indInstance, indicatorVersion);
    }

    @Override
    public List<GeographicalGranularity> retrieveGeographicalGranularitiesInIndicatorInstanceWithLastVersionIndicator(ServiceContext ctx, String indicatorInstanceUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveGeographicalGranularitiesInIndicatorInstance(indicatorInstanceUuid, null);

        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);
        IndicatorVersion indicatorVersion = getIndicatorLastVersion(indInstance.getIndicator().getUuid());

        return calculateGeographicalGranularitiesInIndicatorInstanceLinkedToIndicatorVersion(ctx, indInstance, indicatorVersion);
    }

    private List<GeographicalGranularity> calculateGeographicalGranularitiesInIndicatorInstanceLinkedToIndicatorVersion(ServiceContext ctx, IndicatorInstance indInstance,
            IndicatorVersion indicatorVersion) throws MetamacException {
        if (indicatorVersion.getDataRepositoryId() != null) {
            if (indInstance.getGeographicalValues() != null && indInstance.getGeographicalValues().size() > 0) {
                Set<GeographicalGranularity> granularities = new HashSet<GeographicalGranularity>();
                for (GeographicalValue geoValue : indInstance.getGeographicalValues()) {
                    granularities.add(geoValue.getGranularity());
                }
                return new ArrayList<GeographicalGranularity>(granularities);
            } else if (indInstance.getGeographicalGranularity() != null) {
                return Arrays.asList(indInstance.getGeographicalGranularity());
            } else {
                return retrieveGeographicalGranularitiesInIndicatorPublished(ctx, indInstance.getIndicator().getUuid());
            }
        } else {
            throw new MetamacException(ServiceExceptionType.INDICATOR_NOT_POPULATED, indicatorVersion.getIndicator().getUuid(), indicatorVersion.getVersionNumber());
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
                ServiceUtils.sortGeographicalValuesList(geographicalValuesInIndicator);
                return geographicalValuesInIndicator;
            } catch (ApplicationException e) {
                throw new MetamacException(e, ServiceExceptionType.INDICATOR_FIND_DIMENSION_CODES_ERROR, indicatorVersion.getIndicator().getUuid(),
                        ServiceExceptionParameters.INDICATOR_DATA_DIMENSION_TYPE_GEOGRAPHICAL);
            }
        } else {
            throw new MetamacException(ServiceExceptionType.INDICATOR_NOT_POPULATED, indicatorVersion.getIndicator().getUuid(), indicatorVersion.getVersionNumber());
        }
    }

    @Override
    public List<GeographicalValue> retrieveGeographicalValuesByGranularityInIndicatorInstance(ServiceContext ctx, String indicatorInstanceUuid, String granularityUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveGeographicalValuesByGranularityInIndicatorInstance(indicatorInstanceUuid, granularityUuid, null);

        IndicatorInstance indicatorInstance = getIndicatorInstance(indicatorInstanceUuid);
        IndicatorVersion indicatorVersion = getIndicatorPublishedVersion(indicatorInstance.getIndicator().getUuid());
        return calculateGeographicalValuesByGranularityInIndicatorInstance(granularityUuid, indicatorInstance, indicatorVersion);
    }

    private List<GeographicalValue> calculateGeographicalValuesByGranularityInIndicatorInstance(String granularityUuid, IndicatorInstance indicatorInstance, IndicatorVersion indicatorVersion)
            throws MetamacException {
        if (indicatorVersion.getDataRepositoryId() != null) {
            GeographicalGranularity granularity = getGeographicalGranularityRepository().retrieveGeographicalGranularity(granularityUuid);
            if (indicatorInstance.getGeographicalValues() != null && indicatorInstance.getGeographicalValues().size() > 0) { // Fixed values
                List<GeographicalValue> geoValues = new ArrayList<GeographicalValue>();
                for (GeographicalValue geoValue : indicatorInstance.getGeographicalValues()) {
                    if (geoValue.getGranularity().equals(granularity)) {
                        geoValues.add(geoValue);
                    }
                }
                return geoValues;
            } else if (indicatorInstance.getGeographicalGranularity() != null) {
                if (indicatorInstance.getGeographicalGranularity().equals(granularity)) { // fixed granularity
                    return calculateGeographicalValuesByGranularityInIndicatorVersion(granularityUuid, indicatorVersion);
                } else {
                    return new ArrayList<GeographicalValue>();
                }
            } else { // nothing is fixed
                return calculateGeographicalValuesByGranularityInIndicatorVersion(granularityUuid, indicatorVersion);
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
                List<String> geographicalCodesInIndicator = findCodesForDimensionInIndicator(indicatorVersion, IndicatorDataDimensionTypeEnum.GEOGRAPHICAL);
                List<GeographicalValue> geographicalValuesInIndicator = getGeographicalValueRepository().findGeographicalValuesByCodes(geographicalCodesInIndicator);
                ServiceUtils.sortGeographicalValuesList(geographicalValuesInIndicator);
                return geographicalValuesInIndicator;
            } catch (ApplicationException e) {
                throw new MetamacException(e, ServiceExceptionType.INDICATOR_FIND_DIMENSION_CODES_ERROR, indicatorVersion.getIndicator().getUuid(),
                        ServiceExceptionParameters.INDICATOR_DATA_DIMENSION_TYPE_GEOGRAPHICAL);
            }
        } else {
            throw new MetamacException(ServiceExceptionType.INDICATOR_NOT_POPULATED, indicatorVersion.getIndicator().getUuid(), indicatorVersion.getVersionNumber());
        }
    }

    @Override
    public List<GeographicalValue> retrieveGeographicalValuesInIndicatorInstanceWithPublishedIndicator(ServiceContext ctx, String indicatorInstanceUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveGeographicalValuesInIndicatorInstance(indicatorInstanceUuid, null);

        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);
        IndicatorVersion indicatorVersion = getIndicatorPublishedVersion(indInstance.getIndicator().getUuid());
        return calculateGeographicalValuesInIndicatorInstance(indInstance, indicatorVersion);
    }

    @Override
    public List<GeographicalValue> retrieveGeographicalValuesInIndicatorInstanceWithLastVersionIndicator(ServiceContext ctx, String indicatorInstanceUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveGeographicalValuesInIndicatorInstance(indicatorInstanceUuid, null);

        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);
        IndicatorVersion indicatorVersion = getIndicatorLastVersion(indInstance.getIndicator().getUuid());
        return calculateGeographicalValuesInIndicatorInstance(indInstance, indicatorVersion);
    }

    private List<GeographicalValue> calculateGeographicalValuesInIndicatorInstance(IndicatorInstance indInstance, IndicatorVersion indicatorVersion) throws MetamacException {
        if (indicatorVersion.getDataRepositoryId() != null) {
            if (indInstance.getGeographicalValues() != null && indInstance.getGeographicalValues().size() > 0) { // Fixed values
                List<GeographicalValue> geoValues = new ArrayList<GeographicalValue>();
                for (GeographicalValue geoValue : indInstance.getGeographicalValues()) {
                    geoValues.add(geoValue);
                }
                return geoValues;
            } else if (indInstance.getGeographicalGranularity() != null) { // fixed granularity
                return calculateGeographicalValuesByGranularityInIndicatorVersion(indInstance.getGeographicalGranularity().getUuid(), indicatorVersion);
            } else { // nothing is fixed
                return calculateGeographicalValuesInIndicatorVersion(indicatorVersion);
            }
        } else {
            throw new MetamacException(ServiceExceptionType.INDICATOR_NOT_POPULATED, indicatorVersion.getIndicator().getUuid(), indicatorVersion.getVersionNumber());
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
                throw new MetamacException(e, ServiceExceptionType.INDICATOR_FIND_DIMENSION_CODES_ERROR, indicatorVersion.getIndicator().getUuid(),
                        ServiceExceptionParameters.INDICATOR_DATA_DIMENSION_TYPE_TIME);
            }
        } else {
            throw new MetamacException(ServiceExceptionType.INDICATOR_NOT_POPULATED, indicatorVersion.getIndicator().getUuid(), indicatorVersion.getVersionNumber());
        }
    }

    @Override
    public List<TimeGranularity> retrieveTimeGranularitiesInIndicatorInstanceWithPublishedIndicator(ServiceContext ctx, String indicatorInstanceUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveTimeGranularitiesInIndicatorInstance(indicatorInstanceUuid, null);

        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);

        List<TimeGranularity> timeGranularitiesFixedInIndicatorInstance = getFixedTimeGranularitiesInIndicatorInstance(ctx, indInstance);

        if (timeGranularitiesFixedInIndicatorInstance == null) {
            return retrieveTimeGranularitiesInIndicatorPublished(ctx, indInstance.getIndicator().getUuid());
        } else {
            return timeGranularitiesFixedInIndicatorInstance;
        }
    }

    @Override
    public List<TimeGranularity> retrieveTimeGranularitiesInIndicatorInstanceWithLastVersionIndicator(ServiceContext ctx, String indicatorInstanceUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveTimeGranularitiesInIndicatorInstance(indicatorInstanceUuid, null);

        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);

        List<TimeGranularity> timeGranularitiesFixedInIndicatorInstance = getFixedTimeGranularitiesInIndicatorInstance(ctx, indInstance);

        if (timeGranularitiesFixedInIndicatorInstance == null) {
            IndicatorVersion indicatorVersion = getIndicatorLastVersion(indInstance.getIndicator().getUuid());
            return retrieveTimeGranularitiesInIndicator(ctx, indInstance.getIndicator().getUuid(), indicatorVersion.getVersionNumber());
        } else {
            return timeGranularitiesFixedInIndicatorInstance;
        }
    }

    private List<TimeGranularity> getFixedTimeGranularitiesInIndicatorInstance(ServiceContext ctx, IndicatorInstance indInstance) throws MetamacException {
        if (!StringUtils.isEmpty(indInstance.getTimeValues())) {
            Map<TimeGranularityEnum, TimeGranularity> timeGranularities = new HashMap<TimeGranularityEnum, TimeGranularity>();
            for (String timeValueStr : indInstance.getTimeValuesAsList()) {
                TimeGranularityEnum guessedGranularity = TimeVariableUtils.guessTimeGranularity(timeValueStr);
                if (!timeGranularities.containsKey(guessedGranularity)) {
                    TimeGranularity timeGranularity = getIndicatorsSystemsService().retrieveTimeGranularity(ctx, guessedGranularity);
                    timeGranularities.put(guessedGranularity, timeGranularity);
                }
            }
            return new ArrayList<TimeGranularity>(timeGranularities.values());
        } else if (indInstance.getTimeGranularity() != null) {
            TimeGranularity timeGranularity = getIndicatorsSystemsService().retrieveTimeGranularity(ctx, indInstance.getTimeGranularity());
            return Arrays.asList(timeGranularity);
        } else {
            return null;
        }
    }
    @Override
    public List<GeographicalGranularity> retrieveGeographicalGranularitiesInIndicatorsInstanceInPublishedIndicatorsSystem(ServiceContext ctx, String systemCode) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveGeographicalGranularitiesInIndicatorsInstanceInPublishedIndicatorsSystem(systemCode, null);

        List<IndicatorInstance> instances = getIndicatorInstanceRepository().findIndicatorsInstancesInPublishedIndicatorSystem(systemCode);

        Set<GeographicalGranularity> granularities = new HashSet<GeographicalGranularity>();
        for (IndicatorInstance instance : instances) {
            granularities.addAll(retrieveGeographicalGranularitiesInIndicatorInstanceWithPublishedIndicator(ctx, instance.getUuid()));
        }
        return new ArrayList<GeographicalGranularity>(granularities);
    }

    @Override
    public List<GeographicalGranularity> retrieveGeographicalGranularitiesInIndicatorsPublishedWithSubjectCode(ServiceContext ctx, String subjectCode) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveGeographicalGranularitiesInIndicatorsPublishedWithSubjectCode(subjectCode, null);

        List<IndicatorVersion> indicatorVersions = getIndicatorVersionRepository().findPublishedIndicatorVersionWithSubjectCode(subjectCode);

        Set<GeographicalGranularity> granularities = new HashSet<GeographicalGranularity>();
        for (IndicatorVersion indicatorVersion : indicatorVersions) {
            granularities.addAll(retrieveGeographicalGranularitiesInIndicatorPublished(ctx, indicatorVersion.getIndicator().getUuid()));
        }

        return new ArrayList<GeographicalGranularity>(granularities);
    }

    @Override
    public List<GeographicalValue> retrieveGeographicalValuesByGranularityInIndicatorsInstancesInPublishedIndicatorsSystem(ServiceContext ctx, String systemCode, String granularityUuid)
            throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveGeographicalValuesByGranularityInIndicatorsInstancesInPublishedIndicatorsSystem(systemCode, granularityUuid, null);

        List<IndicatorInstance> instances = getIndicatorInstanceRepository().findIndicatorsInstancesInPublishedIndicatorSystem(systemCode);

        Set<GeographicalValue> geoValues = new HashSet<GeographicalValue>();
        for (IndicatorInstance instance : instances) {
            geoValues.addAll(retrieveGeographicalValuesByGranularityInIndicatorInstance(ctx, instance.getUuid(), granularityUuid));
        }
        List<GeographicalValue> geoValuesList = new ArrayList<GeographicalValue>(geoValues);
        ServiceUtils.sortGeographicalValuesList(geoValuesList);
        return geoValuesList;
    }

    @Override
    public List<GeographicalValue> retrieveGeographicalValuesInIndicatorsInstancesInPublishedIndicatorsSystem(ServiceContext ctx, String systemCode) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveGeographicalValuesInIndicatorsInstancesInPublishedIndicatorsSystem(systemCode, null);

        List<IndicatorInstance> instances = getIndicatorInstanceRepository().findIndicatorsInstancesInPublishedIndicatorSystem(systemCode);

        Set<GeographicalValue> geoValues = new HashSet<GeographicalValue>();
        for (IndicatorInstance instance : instances) {
            geoValues.addAll(retrieveGeographicalValuesInIndicatorInstanceWithPublishedIndicator(ctx, instance.getUuid()));
        }

        List<GeographicalValue> geoValuesList = new ArrayList<GeographicalValue>(geoValues);
        ServiceUtils.sortGeographicalValuesList(geoValuesList);
        return geoValuesList;
    }

    @Override
    public List<GeographicalValue> retrieveGeographicalValuesByGranularityInIndicatorPublishedWithSubjectCode(ServiceContext ctx, String subjectCode, String granularityUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveGeographicalValuesByGranularityInIndicatorPublishedWithSubjectCode(subjectCode, granularityUuid, null);

        List<IndicatorVersion> indicatorVersions = getIndicatorVersionRepository().findPublishedIndicatorVersionWithSubjectCode(subjectCode);

        Set<GeographicalValue> geoValues = new HashSet<GeographicalValue>();
        for (IndicatorVersion indicatorVersion : indicatorVersions) {
            geoValues.addAll(retrieveGeographicalValuesByGranularityInIndicatorPublished(ctx, indicatorVersion.getIndicator().getUuid(), granularityUuid));
        }
        List<GeographicalValue> geoValuesList = new ArrayList<GeographicalValue>(geoValues);
        ServiceUtils.sortGeographicalValuesList(geoValuesList);
        return geoValuesList;
    }

    @Override
    public List<GeographicalValue> retrieveGeographicalValuesInPublishedIndicatorsWithSubjectCode(ServiceContext ctx, String subjectCode) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveGeographicalValuesInPublishedIndicatorsWithSubjectCode(subjectCode, null);

        List<IndicatorVersion> indicatorVersions = getIndicatorVersionRepository().findPublishedIndicatorVersionWithSubjectCode(subjectCode);

        Set<GeographicalValue> geoValues = new HashSet<GeographicalValue>();
        for (IndicatorVersion indicatorVersion : indicatorVersions) {
            geoValues.addAll(retrieveGeographicalValuesInIndicatorPublished(ctx, indicatorVersion.getIndicator().getUuid()));
        }
        List<GeographicalValue> geoValuesList = new ArrayList<GeographicalValue>(geoValues);
        ServiceUtils.sortGeographicalValuesList(geoValuesList);
        return geoValuesList;
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

    @Override
    public List<TimeValue> retrieveTimeValuesByGranularityInIndicatorInstance(ServiceContext ctx, String indicatorInstanceUuid, TimeGranularityEnum granularity) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveTimeValuesByGranularityInIndicatorInstance(indicatorInstanceUuid, granularity, null);

        IndicatorInstance indicatorInstance = getIndicatorInstance(indicatorInstanceUuid);
        IndicatorVersion indicatorVersion = getIndicatorPublishedVersion(indicatorInstance.getIndicator().getUuid());
        return calculateTimeValuesByGranularityInIndicatorInstance(ctx, granularity, indicatorInstance, indicatorVersion);
    }

    private List<TimeValue> calculateTimeValuesByGranularityInIndicatorInstance(ServiceContext ctx, TimeGranularityEnum granularity, IndicatorInstance indicatorInstance,
            IndicatorVersion indicatorVersion) throws MetamacException {
        String datasetId = indicatorVersion.getDataRepositoryId();
        if (datasetId != null) {

            if (indicatorInstance.getTimeValuesAsList() != null && indicatorInstance.getTimeValuesAsList().size() > 0) {
                List<TimeValue> timeValuesInIndicatorInstance = new ArrayList<TimeValue>();
                for (String timeStr : indicatorInstance.getTimeValuesAsList()) {
                    TimeValue value = TimeVariableUtils.parseTimeValue(timeStr);
                    if (granularity.equals(value.getGranularity())) {
                        timeValuesInIndicatorInstance.add(value);
                    }
                }

                TimeVariableUtils.sortTimeValuesMostRecentFirst(timeValuesInIndicatorInstance);

                return timeValuesInIndicatorInstance;
            } else if (indicatorInstance.getTimeGranularity() != null) {
                if (granularity.equals(indicatorInstance.getTimeGranularity())) {
                    return calculateTimeValuesByGranularityInIndicatorVersion(ctx, granularity, indicatorVersion);
                } else {
                    return new ArrayList<TimeValue>();
                }
            } else {
                return calculateTimeValuesByGranularityInIndicatorVersion(ctx, granularity, indicatorVersion);
            }
        } else {
            throw new MetamacException(ServiceExceptionType.INDICATOR_NOT_POPULATED, indicatorVersion.getIndicator().getUuid(), indicatorVersion.getVersionNumber());
        }
    }

    private List<TimeValue> calculateTimeValuesByGranularityInIndicatorVersion(ServiceContext ctx, TimeGranularityEnum granularity, IndicatorVersion indicatorVersion) throws MetamacException {
        String datasetId = indicatorVersion.getDataRepositoryId();
        if (datasetId != null) {
            try {
                List<TimeValue> timeValuesInIndicator = new ArrayList<TimeValue>();
                List<String> timeCodesInIndicator = findCodesForDimensionInIndicator(indicatorVersion, IndicatorDataDimensionTypeEnum.TIME);
                List<String> timeCodesByGranularity = new ArrayList<String>();
                for (String timeCode : timeCodesInIndicator) {
                    TimeGranularityEnum guessedGranularity = TimeVariableUtils.guessTimeGranularity(timeCode);
                    if (granularity.equals(guessedGranularity)) {
                        timeCodesByGranularity.add(timeCode);
                    }
                }

                if (!timeCodesByGranularity.isEmpty()) {
                    timeValuesInIndicator = getIndicatorsSystemsService().retrieveTimeValues(ctx, timeCodesByGranularity);
                    TimeVariableUtils.sortTimeValuesMostRecentFirst(timeValuesInIndicator);
                }

                return timeValuesInIndicator;
            } catch (ApplicationException e) {
                throw new MetamacException(e, ServiceExceptionType.INDICATOR_FIND_DIMENSION_CODES_ERROR, indicatorVersion.getIndicator().getUuid(),
                        ServiceExceptionParameters.INDICATOR_DATA_DIMENSION_TYPE_TIME);
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
                List<String> timeCodesInIndicator = findCodesForDimensionInIndicator(indicatorVersion, IndicatorDataDimensionTypeEnum.TIME);

                List<TimeValue> timeValuesInIndicator = getIndicatorsSystemsService().retrieveTimeValues(ctx, timeCodesInIndicator);

                TimeVariableUtils.sortTimeValuesMostRecentFirst(timeValuesInIndicator);

                return timeValuesInIndicator;
            } catch (ApplicationException e) {
                throw new MetamacException(e, ServiceExceptionType.INDICATOR_FIND_DIMENSION_CODES_ERROR, indicatorVersion.getIndicator().getUuid(),
                        ServiceExceptionParameters.INDICATOR_DATA_DIMENSION_TYPE_TIME);
            }
        } else {
            throw new MetamacException(ServiceExceptionType.INDICATOR_NOT_POPULATED, indicatorVersion.getIndicator().getUuid(), indicatorVersion.getVersionNumber());
        }
    }

    @Override
    public List<TimeValue> retrieveTimeValuesInIndicatorInstanceWithPublishedIndicator(ServiceContext ctx, String indicatorInstanceUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveTimeValuesInIndicatorInstance(indicatorInstanceUuid, null);

        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);
        IndicatorVersion indicatorVersion = getIndicatorPublishedVersion(indInstance.getIndicator().getUuid());
        return calculateTimeValuesInIndicatorInstance(ctx, indInstance, indicatorVersion);
    }

    @Override
    public List<TimeValue> retrieveTimeValuesInIndicatorInstanceWithLastVersionIndicator(ServiceContext ctx, String indicatorInstanceUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveTimeValuesInIndicatorInstance(indicatorInstanceUuid, null);

        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);
        IndicatorVersion indicatorVersion = getIndicatorLastVersion(indInstance.getIndicator().getUuid());
        return calculateTimeValuesInIndicatorInstance(ctx, indInstance, indicatorVersion);
    }

    private List<TimeValue> calculateTimeValuesInIndicatorInstance(ServiceContext ctx, IndicatorInstance indInstance, IndicatorVersion indicatorVersion) throws MetamacException {
        if (!StringUtils.isEmpty(indInstance.getTimeValues())) {
            List<TimeValue> timeValues = getIndicatorsSystemsService().retrieveTimeValues(ctx, indInstance.getTimeValuesAsList());
            TimeVariableUtils.sortTimeValuesMostRecentFirst(timeValues);
            return timeValues;
        } else if (indInstance.getTimeGranularity() != null) {
            return calculateTimeValuesByGranularityInIndicatorVersion(ctx, indInstance.getTimeGranularity(), indicatorVersion);
        } else {
            return calculateTimeValuesInIndicatorVersion(ctx, indicatorVersion);
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
    public List<MeasureValue> retrieveMeasureValuesInIndicatorInstanceWithPublishedIndicator(ServiceContext ctx, String indicatorInstanceUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveMeasureValuesInIndicatorInstance(indicatorInstanceUuid, null);
        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);
        IndicatorVersion indicatorVersion = getIndicatorPublishedVersion(indInstance.getIndicator().getUuid());
        return calculateMeasureValuesInIndicatorVersion(ctx, indicatorVersion);
    }

    @Override
    public List<MeasureValue> retrieveMeasureValuesInIndicatorInstanceWithLastVersionIndicator(ServiceContext ctx, String indicatorInstanceUuid) throws MetamacException {
        // Validation
        InvocationValidator.checkRetrieveMeasureValuesInIndicatorInstance(indicatorInstanceUuid, null);
        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);
        IndicatorVersion indicatorVersion = getIndicatorLastVersion(indInstance.getIndicator().getUuid());
        return calculateMeasureValuesInIndicatorVersion(ctx, indicatorVersion);
    }

    private List<MeasureValue> calculateMeasureValuesInIndicatorVersion(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {
        String datasetId = indicatorVersion.getDataRepositoryId();
        if (datasetId != null) {
            try {
                List<String> measureCodesInIndicator = findCodesForDimensionInIndicator(indicatorVersion, IndicatorDataDimensionTypeEnum.MEASURE);
                List<MeasureValue> measureValuesInIndicator = getIndicatorsSystemsService().retrieveMeasuresValues(ctx, measureCodesInIndicator);
                return measureValuesInIndicator;
            } catch (ApplicationException e) {
                throw new MetamacException(e, ServiceExceptionType.INDICATOR_FIND_DIMENSION_CODES_ERROR, indicatorVersion.getIndicator().getUuid(),
                        ServiceExceptionParameters.INDICATOR_DATA_DIMENSION_TYPE_TIME);
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
    public Map<String, ObservationDto> findObservationsByDimensionsInIndicatorLastVersion(ServiceContext ctx, String indicatorUuid, List<ConditionDimensionDto> conditions) throws MetamacException {
        IndicatorVersion indicatorVersion = getIndicatorLastVersion(indicatorUuid);
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
    public Map<String, ObservationExtendedDto> findObservationsExtendedByDimensionsInIndicatorLastVersion(ServiceContext ctx, String indicatorUuid, List<ConditionDimensionDto> conditions)
            throws MetamacException {
        IndicatorVersion indicatorVersion = getIndicatorLastVersion(indicatorUuid);
        try {
            return findObservationsExtendedByDimensions(ctx, indicatorVersion, conditions);
        } catch (ApplicationException e) {
            throw new MetamacException(e, ServiceExceptionType.DATA_FIND_OBSERVATIONS_EXTENDED_ERROR, indicatorUuid);
        }
    }

    @Override
    public Map<String, ObservationDto> findObservationsByDimensionsInIndicatorInstanceWithPublishedIndicator(ServiceContext ctx, String indicatorInstanceUuid, List<ConditionDimensionDto> conditions)
            throws MetamacException {
        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);
        IndicatorVersion indicatorVersion = getIndicatorPublishedVersion(indInstance.getIndicator().getUuid());

        List<GeographicalValue> geoValues = retrieveGeographicalValuesInIndicatorInstanceWithPublishedIndicator(ctx, indicatorInstanceUuid);
        List<TimeValue> timeValues = retrieveTimeValuesInIndicatorInstanceWithPublishedIndicator(ctx, indicatorInstanceUuid);

        List<ConditionDimensionDto> newConditions = filterConditionsByValues(conditions, geoValues, timeValues);
        try {
            return findObservationsByDimensions(ctx, indicatorVersion, newConditions);
        } catch (ApplicationException e) {
            throw new MetamacException(e, ServiceExceptionType.DATA_INSTANCES_FIND_OBSERVATIONS_ERROR, indicatorInstanceUuid);
        }
    }

    @Override
    public Map<String, ObservationDto> findObservationsByDimensionsInIndicatorInstanceWithLastVersionIndicator(ServiceContext ctx, String indicatorInstanceUuid, List<ConditionDimensionDto> conditions)
            throws MetamacException {
        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);
        IndicatorVersion indicatorVersion = getIndicatorLastVersion(indInstance.getIndicator().getUuid());

        List<GeographicalValue> geoValues = retrieveGeographicalValuesInIndicatorInstanceWithLastVersionIndicator(ctx, indicatorInstanceUuid);
        List<TimeValue> timeValues = retrieveTimeValuesInIndicatorInstanceWithLastVersionIndicator(ctx, indicatorInstanceUuid);

        List<ConditionDimensionDto> newConditions = filterConditionsByValues(conditions, geoValues, timeValues);
        try {
            return findObservationsByDimensions(ctx, indicatorVersion, newConditions);
        } catch (ApplicationException e) {
            throw new MetamacException(e, ServiceExceptionType.DATA_INSTANCES_FIND_OBSERVATIONS_ERROR, indicatorInstanceUuid);
        }
    }

    @Override
    public Map<String, ObservationExtendedDto> findObservationsExtendedByDimensionsInIndicatorInstanceWithPublishedIndicator(ServiceContext ctx, String indicatorInstanceUuid,
            List<ConditionDimensionDto> conditions) throws MetamacException {
        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);
        IndicatorVersion indicatorVersion = getIndicatorPublishedVersion(indInstance.getIndicator().getUuid());

        List<GeographicalValue> geoValues = retrieveGeographicalValuesInIndicatorInstanceWithPublishedIndicator(ctx, indicatorInstanceUuid);
        List<TimeValue> timeValues = retrieveTimeValuesInIndicatorInstanceWithPublishedIndicator(ctx, indicatorInstanceUuid);

        List<ConditionDimensionDto> newConditions = filterConditionsByValues(conditions, geoValues, timeValues);
        try {
            return findObservationsExtendedByDimensions(ctx, indicatorVersion, newConditions);
        } catch (ApplicationException e) {
            throw new MetamacException(e, ServiceExceptionType.DATA_INSTANCES_FIND_OBSERVATIONS_EXTENDED_ERROR, indicatorInstanceUuid);
        }
    }

    @Override
    public Map<String, ObservationExtendedDto> findObservationsExtendedByDimensionsInIndicatorInstanceWithLastVersionIndicator(ServiceContext ctx, String indicatorInstanceUuid,
            List<ConditionDimensionDto> conditions) throws MetamacException {
        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);
        IndicatorVersion indicatorVersion = getIndicatorLastVersion(indInstance.getIndicator().getUuid());

        List<GeographicalValue> geoValues = retrieveGeographicalValuesInIndicatorInstanceWithLastVersionIndicator(ctx, indicatorInstanceUuid);
        List<TimeValue> timeValues = retrieveTimeValuesInIndicatorInstanceWithLastVersionIndicator(ctx, indicatorInstanceUuid);

        List<ConditionDimensionDto> newConditions = filterConditionsByValues(conditions, geoValues, timeValues);
        try {
            return findObservationsExtendedByDimensions(ctx, indicatorVersion, newConditions);
        } catch (ApplicationException e) {
            throw new MetamacException(e, ServiceExceptionType.DATA_INSTANCES_FIND_OBSERVATIONS_EXTENDED_ERROR, indicatorInstanceUuid);
        }
    }

    @Override
    public List<IndicatorVersion> findIndicatorsVersionsPublishedWithSubjectCodeAndGeoCodeOrderedByLastUpdate(ServiceContext ctx, String subjectCode, String geoCode) throws MetamacException {
        // Validation
        InvocationValidator.checkFindIndicatorsVersionsPublishedWithSubjectCodeAndGeoCodeOrderedByLastUpdate(subjectCode, geoCode, null);

        List<IndicatorVersion> indicatorsVersions = getIndicatorVersionLastValueCacheRepository().findLastNIndicatorsVersionsWithGeoCodeAndSubjectCodeOrderedByLastUpdate(subjectCode, geoCode,
                Integer.MAX_VALUE);

        return indicatorsVersions;
    }

    @Override
    public List<IndicatorVersionLastValue> findLastValueNLastIndicatorsVersionsWithSubjectCodeAndGeoCodeOrderedByLastUpdate(ServiceContext ctx, String subjectCode, String geoCode,
            List<MeasureDimensionTypeEnum> measureValues, int numResults) throws MetamacException {
        // Validation
        InvocationValidator.checkFindLastValueNLastIndicatorsVersionsWithSubjectCodeAndGeoCodeOrderedByLastUpdate(subjectCode, geoCode, measureValues, numResults, null);

        List<IndicatorVersion> indicatorsVersions = getIndicatorVersionLastValueCacheRepository().findLastNIndicatorsVersionsWithGeoCodeAndSubjectCodeOrderedByLastUpdate(subjectCode, geoCode,
                numResults);
        GeographicalValue geoValue = getGeographicalValueRepository().findGeographicalValueByCode(geoCode);

        List<IndicatorVersionLastValue> latestValues = new ArrayList<IndicatorVersionLastValue>();
        for (IndicatorVersion indicatorVersion : indicatorsVersions) {
            IndicatorVersionLastValue lastValue = retrieveLastValueForIndicatorVersion(indicatorVersion, geoValue, measureValues);
            if (lastValue != null) {
                latestValues.add(lastValue);
            }
        }
        return latestValues;
    }

    @Override
    public List<IndicatorVersionLastValue> findLastValueForIndicatorsVersionsWithGeoCodeOrderedByLastUpdate(ServiceContext ctx, List<String> indicatorsCodes, String geoCode,
            List<MeasureDimensionTypeEnum> measures) throws MetamacException {
        // Validation
        InvocationValidator.checkFindLastValueForIndicatorsVersionsWithGeoCodeOrderedByLastUpdate(indicatorsCodes, geoCode, measures, null);

        GeographicalValue geoValue = getGeographicalValueRepository().findGeographicalValueByCode(geoCode);
        List<IndicatorVersion> indicatorsVersions = new ArrayList<IndicatorVersion>();
        for (String indicatorCode : indicatorsCodes) {
            indicatorsVersions.add(getIndicatorVersionRepository().findPublishedIndicatorVersionByCode(indicatorCode));
        }

        List<IndicatorVersionLastValue> latestValues = new ArrayList<IndicatorVersionLastValue>();
        for (IndicatorVersion indicatorVersion : indicatorsVersions) {
            IndicatorVersionLastValue lastValue = retrieveLastValueForIndicatorVersion(indicatorVersion, geoValue, measures);
            if (lastValue != null) {
                latestValues.add(lastValue);
            }
        }

        ServiceUtils.sortLastValuesCache(latestValues);

        return latestValues;
    }

    @Override
    public List<IndicatorInstance> findIndicatorsInstancesInPublishedIndicatorsSystemWithGeoCodeOrderedByLastUpdate(ServiceContext ctx, String systemCode, String geoCode) throws MetamacException {
        // Validation
        InvocationValidator.checkFindIndicatorsInstancesInPublishedIndicatorsSystemWithGeoCodeOrderedByLastUpdate(systemCode, geoCode, null);

        List<IndicatorInstance> indicatorsInstances = getIndicatorInstanceLastValueCacheRepository().findLastNIndicatorsInstancesWithGeoCodeInIndicatorsSystemOrderedByLastUpdate(systemCode, geoCode,
                Integer.MAX_VALUE);

        return indicatorsInstances;
    }

    @Override
    public List<IndicatorInstanceLastValue> findLastValueNLastIndicatorsInstancesInIndicatorsSystemWithGeoCodeOrderedByLastUpdate(ServiceContext ctx, String systemCode, String geoCode,
            List<MeasureDimensionTypeEnum> measureValues, int numResults) throws MetamacException {
        // Validation
        InvocationValidator.checkFindLastValueNLastIndicatorsInstancesInIndicatorsSystemWithGeoCodeOrderedByLastUpdate(systemCode, geoCode, measureValues, numResults, null);

        List<IndicatorInstance> indicatorsInstances = getIndicatorInstanceLastValueCacheRepository().findLastNIndicatorsInstancesWithGeoCodeInIndicatorsSystemOrderedByLastUpdate(systemCode, geoCode,
                numResults);
        GeographicalValue geoValue = getGeographicalValueRepository().findGeographicalValueByCode(geoCode);

        List<IndicatorInstanceLastValue> latestValues = new ArrayList<IndicatorInstanceLastValue>();
        for (IndicatorInstance indicatorInstance : indicatorsInstances) {
            IndicatorInstanceLastValue lastValue = retrieveLastValueForIndicatorInstance(indicatorInstance, geoValue, measureValues);
            if (lastValue != null) {
                latestValues.add(lastValue);
            }
        }
        return latestValues;
    }

    @Override
    public List<IndicatorInstanceLastValue> findLastValueForIndicatorsInstancesWithGeoCodeOrderedByLastUpdate(ServiceContext ctx, String systemCode, List<String> instancesCodes, String geoCode,
            List<MeasureDimensionTypeEnum> measures) throws MetamacException {
        // Validation
        InvocationValidator.checkFindLastValueForIndicatorsInstancesWithGeoCodeOrderedByLastUpdate(systemCode, instancesCodes, geoCode, measures, null);

        GeographicalValue geoValue = getGeographicalValueRepository().findGeographicalValueByCode(geoCode);
        List<IndicatorInstance> indicatorsInstances = new ArrayList<IndicatorInstance>();
        for (String instanceCode : instancesCodes) {
            IndicatorInstance indicatorInstance = getIndicatorInstanceRepository().findIndicatorInstanceInPublishedIndicatorSystem(systemCode, instanceCode);
            if (indicatorInstance != null) {
                indicatorsInstances.add(indicatorInstance);
            }
        }

        List<IndicatorInstanceLastValue> latestValues = new ArrayList<IndicatorInstanceLastValue>();
        for (IndicatorInstance indicatorInstance : indicatorsInstances) {
            IndicatorInstanceLastValue lastValue = retrieveLastValueForIndicatorInstance(indicatorInstance, geoValue, measures);
            if (lastValue != null) {
                latestValues.add(lastValue);
            }
        }

        ServiceUtils.sortLastValuesCache(latestValues);

        return latestValues;
    }

    private IndicatorInstanceLastValue retrieveLastValueForIndicatorInstance(IndicatorInstance indicatorInstance, GeographicalValue geoValue, List<MeasureDimensionTypeEnum> measureValues) {
        String indicatorUuuid = indicatorInstance.getIndicator().getUuid();
        String publishedVersionNumber = indicatorInstance.getIndicator().getDiffusionVersion().getVersionNumber();
        IndicatorVersion diffusionVersion = getIndicatorVersionRepository().retrieveIndicatorVersion(indicatorUuuid, publishedVersionNumber);

        IndicatorInstanceLastValueCache lastValueCache = getIndicatorInstanceLastValueCacheRepository().retrieveLastValueCacheForIndicatorInstanceWithGeoCode(indicatorInstance.getUuid(),
                geoValue.getCode());
        String timeValueStr = lastValueCache.getLastTimeValue();
        try {
            TimeValue timeValue = TimeVariableUtils.parseTimeValue(timeValueStr);

            IndicatorInstanceLastValue lastValue = new IndicatorInstanceLastValue(indicatorInstance, geoValue, timeValue, lastValueCache.getLastDataUpdated());
            for (MeasureDimensionTypeEnum measure : measureValues) {
                ObservationExtendedDto observationDto = retrieveObservationExtended(diffusionVersion, geoValue, timeValue, measure);
                if (observationDto != null) {
                    lastValue.putObservation(measure, observationDto);
                }
            }
            return lastValue;
        } catch (MetamacException e) {
            LOG.warn("Found invalid time values in indicators instances last value " + timeValueStr);
            return null;
        }
    }

    private IndicatorVersionLastValue retrieveLastValueForIndicatorVersion(IndicatorVersion indicatorVersion, GeographicalValue geoValue, List<MeasureDimensionTypeEnum> measureValues) {

        IndicatorVersionLastValueCache lastValueCache = getIndicatorVersionLastValueCacheRepository().retrieveLastValueCacheForIndicatorVersionWithGeoCode(indicatorVersion.getIndicator().getUuid(),
                geoValue.getCode());
        String timeValueStr = lastValueCache.getLastTimeValue();
        try {
            TimeValue timeValue = TimeVariableUtils.parseTimeValue(timeValueStr);

            IndicatorVersionLastValue lastValue = new IndicatorVersionLastValue(indicatorVersion, geoValue, timeValue, indicatorVersion.getLastPopulateDate());
            for (MeasureDimensionTypeEnum measure : measureValues) {
                ObservationExtendedDto observationDto = retrieveObservationExtended(indicatorVersion, geoValue, timeValue, measure);
                if (observationDto != null) {
                    lastValue.putObservation(measure, observationDto);
                }
            }
            return lastValue;
        } catch (MetamacException e) {
            LOG.warn("Found invalid time values in indicator version last value " + timeValueStr);
            return null;
        }
    }

    @Override
    public void buildLastValuesCache(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {
        LOG.info("Updating last value cache data for indicator uuid:" + indicatorVersion.getIndicator().getUuid() + " version: " + indicatorVersion.getVersionNumber());
        deleteIndicatorVersionLastValuesCache(indicatorVersion);

        buildIndicatorVersionLatestValuesCache(ctx, indicatorVersion);

        List<String> instancesUuids = findAllIndicatorInstancesPublishedWithIndicator(indicatorVersion.getIndicator().getUuid());
        for (String indicatorInstanceUuid : instancesUuids) {
            IndicatorInstance instance = getIndicatorInstanceRepository().findIndicatorInstance(indicatorInstanceUuid);

            deleteIndicatorInstanceLastValuesCache(instance);
            buildIndicatorInstanceLatestValuesCache(ctx, instance, indicatorVersion);
        }
        LOG.info("Updated last value cache data for indicator uuid:" + indicatorVersion.getIndicator().getUuid() + " version: " + indicatorVersion.getVersionNumber());
    }

    @Override
    public void buildIndicatorVersionLatestValuesCache(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {
        List<GeographicalValue> geoValues = calculateGeographicalValuesInIndicatorVersion(indicatorVersion);
        List<TimeValue> timeValues = calculateTimeValuesInIndicatorVersion(ctx, indicatorVersion);

        List<GeographicalValue> geoValuesLeft = geoValues;

        while (!geoValuesLeft.isEmpty() && !timeValues.isEmpty()) {
            TimeValue lastTimeValue = TimeVariableUtils.findLatestTimeValue(timeValues);
            timeValues.remove(lastTimeValue);

            Set<GeographicalValue> foundGeoValues = getGeoValuesInObservations(indicatorVersion, geoValuesLeft, lastTimeValue.getTimeValue());

            for (GeographicalValue geoValue : foundGeoValues) {
                geoValuesLeft.remove(geoValue);

                IndicatorVersionLastValueCache cacheEntry = new IndicatorVersionLastValueCache(geoValue, indicatorVersion);
                cacheEntry.setLastTimeValue(lastTimeValue.getTimeValue());
                cacheEntry.setLastDataUpdated(indicatorVersion.getLastPopulateDate());
                getIndicatorVersionLastValueCacheRepository().save(cacheEntry);
            }
        }

    }

    private void deleteIndicatorVersionLastValuesCache(IndicatorVersion indicatorVersion) {
        getIndicatorVersionLastValueCacheRepository().deleteWithIndicatorVersion(indicatorVersion.getIndicator().getUuid());
    }

    @Override
    public void buildIndicatorInstanceLatestValuesCache(ServiceContext ctx, IndicatorInstance indicatorInstance) throws MetamacException {
        IndicatorVersion indicatorVersion = getIndicatorPublishedVersion(indicatorInstance.getIndicator().getUuid());
        buildIndicatorInstanceLatestValuesCache(ctx, indicatorInstance, indicatorVersion);
    }

    private void buildIndicatorInstanceLatestValuesCache(ServiceContext ctx, IndicatorInstance indicatorInstance, IndicatorVersion indicatorVersion) throws MetamacException {
        List<GeographicalValue> geoValues = calculateGeographicalValuesInIndicatorInstance(indicatorInstance, indicatorVersion);
        List<TimeValue> timeValues = calculateTimeValuesInIndicatorInstance(ctx, indicatorInstance, indicatorVersion);

        List<GeographicalValue> geoValuesLeft = geoValues;

        while (!geoValues.isEmpty() && !timeValues.isEmpty()) {
            TimeValue lastTimeValue = TimeVariableUtils.findLatestTimeValue(timeValues);
            timeValues.remove(lastTimeValue);

            Set<GeographicalValue> foundGeoValues = getGeoValuesInObservations(indicatorVersion, geoValuesLeft, lastTimeValue.getTimeValue());

            for (GeographicalValue geoValue : foundGeoValues) {
                geoValuesLeft.remove(geoValue);

                IndicatorInstanceLastValueCache cacheEntry = new IndicatorInstanceLastValueCache(geoValue, indicatorInstance);
                cacheEntry.setLastTimeValue(lastTimeValue.getTimeValue());
                cacheEntry.setLastDataUpdated(indicatorVersion.getLastPopulateDate());
                getIndicatorInstanceLastValueCacheRepository().save(cacheEntry);
            }
        }
    }

    private void deleteIndicatorInstanceLastValuesCache(IndicatorInstance indicatorInstance) {
        getIndicatorInstanceLastValueCacheRepository().deleteWithIndicatorInstance(indicatorInstance.getUuid());
    }

    private Set<GeographicalValue> getGeoValuesInObservations(IndicatorVersion indicatorVersion, List<GeographicalValue> geoValues, String timeValue) throws MetamacException {

        List<ConditionDimensionDto> conditions = new ArrayList<ConditionDimensionDto>();

        List<String> geoCodes = new ArrayList<String>();
        for (GeographicalValue geoValue : geoValues) {
            geoCodes.add(geoValue.getCode());
        }
        ConditionDimensionDto geoCondition = new ConditionDimensionDto();
        geoCondition.setDimensionId(GEO_DIMENSION);
        geoCondition.setCodesDimension(geoCodes);
        conditions.add(geoCondition);

        ConditionDimensionDto timeCondition = new ConditionDimensionDto();
        timeCondition.setDimensionId(TIME_DIMENSION);
        timeCondition.setCodesDimension(Arrays.asList(timeValue));
        conditions.add(timeCondition);

        Map<String, ObservationDto> observations;
        try {
            String datasetId = indicatorVersion.getDataRepositoryId();
            observations = datasetRepositoriesServiceFacade.findObservationsByDimensions(datasetId, conditions);
            Set<GeographicalValue> foundGeoCodes = new HashSet<GeographicalValue>();
            for (ObservationDto obs : observations.values()) {
                String geoCode = null;
                for (CodeDimensionDto codeDim : obs.getCodesDimension()) {
                    if (GEO_DIMENSION.equals(codeDim.getDimensionId())) {
                        geoCode = codeDim.getCodeDimensionId();
                    }
                }
                int indexFound = geoCodes.indexOf(geoCode);
                foundGeoCodes.add(geoValues.get(indexFound));
            }
            return foundGeoCodes;
        } catch (ApplicationException e) {
            throw new MetamacException(ServiceExceptionType.DATA_RETRIEVE_ERROR, e);
        }
    }

    /**
     * Retrieve observation from a specific IndicatorVersion
     * 
     * @param indicatorVersion
     * @param geoValue
     * @param timeValue
     * @param measureDimension
     * @return
     */
    private ObservationExtendedDto retrieveObservationExtended(IndicatorVersion indicatorVersion, GeographicalValue geoValue, TimeValue timeValue, MeasureDimensionTypeEnum measureDimension) {
        List<ConditionDimensionDto> conditions = new ArrayList<ConditionDimensionDto>();

        ConditionDimensionDto geoCondition = new ConditionDimensionDto();
        geoCondition.setDimensionId(GEO_DIMENSION);
        geoCondition.setCodesDimension(Arrays.asList(geoValue.getCode()));

        ConditionDimensionDto timeCondition = new ConditionDimensionDto();
        timeCondition.setDimensionId(TIME_DIMENSION);
        timeCondition.setCodesDimension(Arrays.asList(timeValue.getTimeValue()));

        ConditionDimensionDto measureCondition = new ConditionDimensionDto();
        measureCondition.setDimensionId(MEASURE_DIMENSION);
        measureCondition.setCodesDimension(Arrays.asList(measureDimension.getName()));

        conditions.add(geoCondition);
        conditions.add(timeCondition);
        conditions.add(measureCondition);

        Map<String, ObservationExtendedDto> singleObservation;
        try {
            String datasetId = indicatorVersion.getDataRepositoryId();
            singleObservation = datasetRepositoriesServiceFacade.findObservationsExtendedByDimensions(datasetId, conditions);
            if (singleObservation.values().size() > 0) {
                return (ObservationExtendedDto) singleObservation.values().toArray()[0];
            }
        } catch (ApplicationException e) {
            LOG.error("Error retrieving observation, which should be the only one", e);
        }
        return null;
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

        ConditionDimensionDto geoCondition = filterConditionsByType(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL, rawConditions, getCodesInGeographicalValues(geoValues));
        ConditionDimensionDto timeCondition = filterConditionsByType(IndicatorDataDimensionTypeEnum.TIME, rawConditions, getCodesInTimeValues(timeValues));
        ConditionDimensionDto measureCondition = filterConditionsByType(IndicatorDataDimensionTypeEnum.MEASURE, rawConditions, getCodesInMeasureValues(MeasureDimensionTypeEnum.values()));
        return Arrays.asList(geoCondition, timeCondition, measureCondition);
    }

    private ConditionDimensionDto filterConditionsByType(IndicatorDataDimensionTypeEnum type, List<ConditionDimensionDto> rawConditions, List<String> dimensionCodes) {

        Set<String> selectedCodes = new HashSet<String>();
        for (ConditionDimensionDto condition : rawConditions) {
            if (type.name().equals(condition.getDimensionId())) {
                for (String code : condition.getCodesDimension()) {
                    selectedCodes.add(code);
                }
            }
        }

        selectedCodes.retainAll(dimensionCodes);

        if (selectedCodes.isEmpty()) {
            selectedCodes.addAll(dimensionCodes);
        }

        ConditionDimensionDto newCondition = new ConditionDimensionDto();
        newCondition.setDimensionId(type.name());
        newCondition.setCodesDimension(new ArrayList<String>(selectedCodes));
        return newCondition;
    }

    private List<String> getCodesInGeographicalValues(List<GeographicalValue> geoValues) {
        List<String> geoCodes = new ArrayList<String>();
        for (GeographicalValue geoValue : geoValues) {
            geoCodes.add(geoValue.getCode());
        }
        return geoCodes;
    }

    private List<String> getCodesInTimeValues(List<TimeValue> timeValues) {
        List<String> codes = new ArrayList<String>();
        for (TimeValue timeValue : timeValues) {
            codes.add(timeValue.getTimeValue());
        }
        return codes;
    }

    private List<String> getCodesInMeasureValues(MeasureDimensionTypeEnum[] values) {
        List<String> codes = new ArrayList<String>();
        for (MeasureDimensionTypeEnum measure : values) {
            codes.add(measure.getName());
        }
        return codes;
    }

    /* Private methods */

    private List<String> findCodesForDimensionInIndicator(IndicatorVersion indicatorVersion, IndicatorDataDimensionTypeEnum dimension) throws ApplicationException {
        Map<String, List<String>> conditions = datasetRepositoriesServiceFacade.findCodeDimensions(indicatorVersion.getDataRepositoryId());
        return conditions.get(dimension.name());
    }

    private Map<String, ObservationDto> findObservationsByDimensions(ServiceContext ctx, IndicatorVersion indicatorVersion, List<ConditionDimensionDto> conditions) throws MetamacException,
            ApplicationException {
        String datasetId = indicatorVersion.getDataRepositoryId();
        if (datasetId != null) {
            return fillEmptyDataCross(datasetRepositoriesServiceFacade.findObservationsByDimensions(datasetId, conditions));
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

    private Map<String, ObservationDto> fillEmptyDataCross(Map<String, ObservationDto> observations) {
        if (observations != null) {
            Set<String> keys = observations.keySet();
            Set<String> geoCodes = new HashSet<String>();
            Set<String> timeCodes = new HashSet<String>();
            Set<String> measureCodes = new HashSet<String>();
            for (String key : keys) {
                String[] codes = key.split("#");
                geoCodes.add(codes[0]);
                timeCodes.add(codes[1]);
                measureCodes.add(codes[2]);
            }
            for (String geoCode : geoCodes) {
                for (String timeCode : timeCodes) {
                    for (String measureCode : measureCodes) {
                        String uniqueKey = geoCode + "#" + timeCode + "#" + measureCode;
                        ObservationDto obs = observations.get(uniqueKey);
                        if (obs == null) {
                            ObservationDto newObs = new ObservationDto();
                            newObs.setPrimaryMeasure(null);
                            CodeDimensionDto geoCodeDimDto = new CodeDimensionDto(GEO_DIMENSION, geoCode);
                            CodeDimensionDto timeCodeDimDto = new CodeDimensionDto(TIME_DIMENSION, timeCode);
                            CodeDimensionDto measureCodeDimDto = new CodeDimensionDto(MEASURE_DIMENSION, measureCode);
                            newObs.getCodesDimension().add(geoCodeDimDto);
                            newObs.getCodesDimension().add(timeCodeDimDto);
                            newObs.getCodesDimension().add(measureCodeDimDto);
                            observations.put(uniqueKey, newObs);
                        }
                    }
                }
            }
        }
        return observations;
    }

    private IndicatorVersion getIndicatorPublishedVersion(String indicatorUuid) throws MetamacException {
        Indicator indicator = getIndicatorRepository().retrieveIndicator(indicatorUuid);
        if (indicator.getIsPublished()) {
            return getIndicatorVersion(indicatorUuid, indicator.getDiffusionVersion().getVersionNumber());
        } else {
            throw new MetamacException(ServiceExceptionType.INDICATOR_IN_DIFFUSION_NOT_FOUND, indicatorUuid);
        }
    }

    private IndicatorVersion getIndicatorLastVersion(String indicatorUuid) throws MetamacException {
        Indicator indicator = getIndicatorRepository().retrieveIndicator(indicatorUuid);

        if (indicator.getProductionVersion() != null) {
            return getIndicatorVersion(indicatorUuid, indicator.getProductionVersion().getVersionNumber());
        } else {
            if (indicator.getIsPublished()) {
                return getIndicatorVersion(indicatorUuid, indicator.getDiffusionVersion().getVersionNumber());
            } else {
                throw new MetamacException(ServiceExceptionType.INDICATOR_VERSION_LAST_ARCHIVED, indicatorUuid);
            }
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

    private void checkIndicatorVersionPopulateCapabilities(IndicatorVersion indicatorVersion) throws MetamacException {
        List<DataSource> dataSources = indicatorVersion.getDataSources();
        if (dataSources.size() == 0) {
            throw new MetamacException(ServiceExceptionType.DATA_POPULATE_NO_DATASOURCES_ERROR, indicatorVersion.getIndicator().getUuid(), indicatorVersion.getVersionNumber());
        } else {
            for (DataSource dataSource : dataSources) {
                if (dataSource.getAbsoluteMethod() != null) {
                    if (indicatorVersion.getQuantity().getDecimalPlaces() == null) {
                        throw new MetamacException(ServiceExceptionType.DATA_POPULATE_NO_DECIMAL_PLACES, indicatorVersion.getIndicator().getUuid(), indicatorVersion.getVersionNumber());
                    }
                }
            }
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

    // No more inconsistent data, no more needs update, update last populate date
    private void markIndicatorVersionAsDataUpdated(IndicatorVersion indicatorVersion) {
        indicatorVersion.setNeedsUpdate(Boolean.FALSE);
        indicatorVersion.setInconsistentData(Boolean.FALSE);
        indicatorVersion.setLastPopulateDate(new DateTime());
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

                // check observation length
                if (observation.getPrimaryMeasure() != null && observation.getPrimaryMeasure().length() > MAX_MEASURE_LENGTH) {
                    throw new MetamacException(ServiceExceptionType.DATA_POPULATE_WRONG_OBSERVATION_VALUE_LENGTH, dataOperation.getDataSourceUuid(), observation.getPrimaryMeasure(),
                            MAX_MEASURE_LENGTH);
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
        datasetRepoDto.getDimensions().add(GEO_DIMENSION);
        datasetRepoDto.getDimensions().add(TIME_DIMENSION);
        datasetRepoDto.getDimensions().add(MEASURE_DIMENSION);

        AttributeDto obsConf = new AttributeDto();
        obsConf.setAttachmentLevel(AttributeAttachmentLevelEnum.OBSERVATION);
        obsConf.setAttributeId(OBS_CONF_ATTRIBUTE);

        AttributeDto code = new AttributeDto();
        code.setAttachmentLevel(AttributeAttachmentLevelEnum.OBSERVATION);
        code.setAttributeId(CODE_ATTRIBUTE);

        datasetRepoDto.getAttributes().add(obsConf);
        datasetRepoDto.getAttributes().add(code);

        List<String> languages = new ArrayList<String>();
        languages.add(DATASET_REPOSITORY_LOCALE);
        datasetRepoDto.setLanguages(languages);

        try {
            datasetRepoDto = datasetRepositoriesServiceFacade.createDatasetRepository(datasetRepoDto);
        } catch (ApplicationException e) {
            throw new MetamacException(e, ServiceExceptionType.DATA_POPULATE_DATASETREPO_CREATE_ERROR, indicatorUuid, indicatorVersion);
        }
        return datasetRepoDto;
    }

    private ObservationExtendedDto getObservationValue(DataOperation dataOperation, Data data, Map<String, String> varCodes, String geoValue, String timeValue) throws MetamacException {
        DataContent content = getValue(dataOperation, data, varCodes);
        String value = content.getValue();

        ObservationExtendedDto observation = new ObservationExtendedDto();
        observation.addCodesDimension(new CodeDimensionDto(GEO_DIMENSION, geoValue));
        observation.addCodesDimension(new CodeDimensionDto(TIME_DIMENSION, timeValue));
        observation.addCodesDimension(new CodeDimensionDto(MEASURE_DIMENSION, dataOperation.getMeasureDimension().name()));
        observation.addAttribute(createAttribute(CODE_ATTRIBUTE, DATASET_REPOSITORY_LOCALE, dataOperation.getDataSourceUuid()));

        // Check for dotted notation
        if (isSpecialString(value)) {
            String text = getSpecialStringMeaning(value);
            // Some Special Strings may not need to create an attribute
            if (!StringUtils.isEmpty(text)) {
                observation.addAttribute(createAttribute(OBS_CONF_ATTRIBUTE, DATASET_REPOSITORY_LOCALE, getSpecialStringMeaning(value)));
            }
            observation.setPrimaryMeasure(null);
        } else {
            Double numValue = null;
            try {
                numValue = Double.parseDouble(value);
            } catch (NumberFormatException e) {
                throw new MetamacException(ServiceExceptionType.DATA_POPULATE_OBSERVATION_FORMAT_ERROR, value);
            }
            String formattedValue = formatValue(numValue, dataOperation);
            observation.setPrimaryMeasure(formattedValue);
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
        observation.addCodesDimension(new CodeDimensionDto(GEO_DIMENSION, geoValue));
        observation.addCodesDimension(new CodeDimensionDto(TIME_DIMENSION, timeValue));
        observation.addCodesDimension(new CodeDimensionDto(MEASURE_DIMENSION, dataOperation.getMeasureDimension().name()));
        observation.addAttribute(createAttribute(CODE_ATTRIBUTE, DATASET_REPOSITORY_LOCALE, dataOperation.getDataSourceUuid()));

        String previousTimeValue = null;
        if (dataOperation.isAnnualMethod()) {
            previousTimeValue = TimeVariableUtils.calculatePreviousYearTimeValue(timeValue);
        } else if (dataOperation.isInterPeriodMethod()) {
            previousTimeValue = TimeVariableUtils.calculatePreviousTimeValue(timeValue);
        } else {
            throw new MetamacException(ServiceExceptionType.DATA_POPULATE_UNKNOWN_METHOD_TYPE, dataOperation.getMethodType());
        }

        if (previousTimeValue == null) {
            observation.addAttribute(createAttribute(OBS_CONF_ATTRIBUTE, DATASET_REPOSITORY_LOCALE, getSpecialStringMeaning(DOT_NOT_APPLICABLE)));
            observation.setPrimaryMeasure(null);
            return observation;
        }

        ConditionDimensionDto geoConditionDto = createCondition(GEO_DIMENSION, geoValue);
        ConditionDimensionDto timeConditionDto = createCondition(TIME_DIMENSION, previousTimeValue, timeValue);
        ConditionDimensionDto measureConditionDto = createCondition(MEASURE_DIMENSION, MeasureDimensionTypeEnum.ABSOLUTE.name());
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
            observation.addAttribute(createAttribute(OBS_CONF_ATTRIBUTE, DATASET_REPOSITORY_LOCALE, getSpecialStringMeaning(DOT_UNAVAILABLE)));
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
                    observation.addAttribute(createAttribute(OBS_CONF_ATTRIBUTE, DATASET_REPOSITORY_LOCALE, getSpecialStringMeaning(DOT_UNAVAILABLE)));
                    return observation;
                }
                Quantity quantity = dataOperation.getQuantity();
                calculatedValue = ((currentValue - previousValue) / previousValue) * quantity.getUnitMultiplier();
            } else if (dataOperation.isPuntualMethod()) {
                calculatedValue = currentValue - previousValue;
            } else {
                throw new MetamacException(ServiceExceptionType.DATA_POPULATE_UNKNOWN_METHOD_TYPE, dataOperation.getMethodType());
            }
            String formattedValue = formatValue(calculatedValue, dataOperation);
            observation.setPrimaryMeasure(formattedValue);
        }
        return observation;
    }

    private ObservationExtendedDto getObservationExtendedFromMap(String geoValue, String timeValue, String measureValue, Map<String, ObservationExtendedDto> observationsMap) {
        String generatedKey = generateObservationUniqueKey(geoValue, timeValue, measureValue);
        return observationsMap.get(generatedKey);
    }

    private String formatValue(Double value, DataOperation dataOperation) {
        DecimalFormat formatter = (DecimalFormat) (DecimalFormat.getInstance(Locale.ENGLISH));
        formatter.setGroupingUsed(false); // DO NOT use thousands separator

        if (RateDerivationRoundingEnum.UPWARD.equals(dataOperation.getRateRounding())) {
            formatter.setRoundingMode(RoundingMode.HALF_UP);
        } else if (RateDerivationRoundingEnum.DOWN.equals(dataOperation.getRateRounding())) {
            formatter.setRoundingMode(RoundingMode.DOWN); // Truncate
        }
        formatter.setMaximumFractionDigits(dataOperation.getQuantity().getDecimalPlaces());
        formatter.setMinimumFractionDigits(dataOperation.getQuantity().getDecimalPlaces());
        return formatter.format(value);
    }

    /*
     * Transform list of datasources to a list of simpler operations, sorted by precedence
     * Load operations must be executed prior calculated operation.
     */
    private List<DataOperation> transformDataSourcesForProcessing(List<DataSource> dataSources) {
        LinkedList<DataOperation> dataOperations = new LinkedList<DataOperation>();
        // Load operations are placed first
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
    private AttributeInstanceObservationDto createAttribute(String id, String locale, String value) {
        AttributeInstanceObservationDto attributeBasicDto = new AttributeInstanceObservationDto();
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
    private AttributeInstanceObservationDto mergeObsConfAttribute(ObservationExtendedDto obs1, ObservationExtendedDto obs2) {
        AttributeInstanceObservationDto attribute1 = null;
        AttributeInstanceObservationDto attribute2 = null;

        if (obs1.getPrimaryMeasure() == null) {
            attribute1 = getAttribute(OBS_CONF_ATTRIBUTE, obs1);
        }
        if (obs2.getPrimaryMeasure() == null) {
            attribute2 = getAttribute(OBS_CONF_ATTRIBUTE, obs2);
        }

        if (attribute1 != null && attribute2 != null) {
            return createAttribute(OBS_CONF_ATTRIBUTE, DATASET_REPOSITORY_LOCALE,
                    attribute1.getValue().getLocalisedLabel(DATASET_REPOSITORY_LOCALE) + ", " + attribute2.getValue().getLocalisedLabel(DATASET_REPOSITORY_LOCALE));
        }
        if (attribute1 != null) {
            return createAttribute(OBS_CONF_ATTRIBUTE, DATASET_REPOSITORY_LOCALE, attribute1.getValue().getLocalisedLabel(DATASET_REPOSITORY_LOCALE));
        }
        if (attribute2 != null) {
            return createAttribute(OBS_CONF_ATTRIBUTE, DATASET_REPOSITORY_LOCALE, attribute2.getValue().getLocalisedLabel(DATASET_REPOSITORY_LOCALE));
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
        CodeDimensionDto geoDimCode = new CodeDimensionDto(GEO_DIMENSION, geoValue);
        CodeDimensionDto timeDimCode = new CodeDimensionDto(TIME_DIMENSION, timeValue);
        CodeDimensionDto measureDimCode = new CodeDimensionDto(MEASURE_DIMENSION, measureValue);
        return DtoUtils.generateUniqueKey(Arrays.asList(geoDimCode, timeDimCode, measureDimCode));
    }

    /*
     * Helper to get certain attribute from an observation
     */
    private AttributeInstanceObservationDto getAttribute(String attributeId, ObservationExtendedDto observation) {
        for (AttributeInstanceObservationDto attr : observation.getAttributes()) {
            if (OBS_CONF_ATTRIBUTE.equals(attr.getAttributeId())) {
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

    private String getDataViewsRole() {
        return configurationService.getProperty(IndicatorsConfigurationConstants.DB_DATA_VIEWS_ROLE);
    }

}
