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

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.core.constants.IndicatorsConstants;
import es.gobcan.istac.indicators.core.domain.Data;
import es.gobcan.istac.indicators.core.domain.DataContent;
import es.gobcan.istac.indicators.core.domain.DataDefinition;
import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.DataSourceVariable;
import es.gobcan.istac.indicators.core.domain.DataStructure;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.Indicator;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorInstanceLastValue;
import es.gobcan.istac.indicators.core.domain.IndicatorInstanceLastValueCache;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionGeoCoverage;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionInformation;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionLastValue;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionLastValueCache;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionMeasureCoverage;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionTimeCoverage;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystem;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersionInformation;
import es.gobcan.istac.indicators.core.domain.MeasureValue;
import es.gobcan.istac.indicators.core.domain.Quantity;
import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.domain.Translation;
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
import es.gobcan.istac.indicators.core.vo.GeographicalCodeVO;
import es.gobcan.istac.indicators.core.vo.IndicatorObservationsExtendedVO;
import es.gobcan.istac.indicators.core.vo.IndicatorObservationsVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataFilterVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataGeoDimensionFilterVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataMeasureDimensionFilterVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataTimeDimensionFilterVO;

/**
 * Implementation of IndicatorsDataService.
 */
@Service("indicatorsDataService")
public class IndicatorsDataServiceImpl extends IndicatorsDataServiceImplBase {

    @Autowired
    private IndicatorsConfigurationService   configurationService;
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
                LOG.error("Error populating indicator " + indicatorUuid + " " + indicator.getDiffusionVersion().getVersionNumber(), e);
                exceptionItems.addAll(e.getExceptionItems());
            }
        }
        if (indicator.getProductionVersion() != null) {
            try {
                indicator = populateIndicatorVersionData(ctx, indicatorUuid, indicator.getProductionVersion().getVersionNumber());
            } catch (MetamacException e) {
                LOG.error("Error populating indicator " + indicatorUuid + " " + indicator.getProductionVersion().getVersionNumber(), e);
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

                rebuildCoveragesCache(ctx, indicatorVersion);

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

                    rebuildCoveragesCache(ctx, indicatorVersion);

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

    private void manageDatabaseViewForLastVersion(IndicatorVersion indicatorVersion) throws MetamacException {
        if (indicatorVersion.getIsLastVersion()) {
            createOrReplaceLastVersionDatabaseView(indicatorVersion);
            assignIndicatorDataOracleRolePermissionsToView(indicatorVersion.getIndicator().getViewCode());
        }
    }

    private void assignIndicatorDataOracleRolePermissionsToView(String viewCode) throws MetamacException {
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

            checkIndicatorVersionHasDataPopulated(indicatorVersion);

            datasetRepositoriesServiceFacade.deleteDatasetRepository(indicatorVersion.getDataRepositoryId());
            indicatorVersion.setDataRepositoryId(null);
            indicatorVersion.setDataRepositoryTableName(null);
            getIndicatorVersionRepository().save(indicatorVersion);

            deleteIndicatorVersionLastValuesCache(indicatorVersion);
        } catch (Exception e) {
            if (e instanceof MetamacException) {
                throw (MetamacException) e;
            } else {
                throw new MetamacException(e, ServiceExceptionType.DATA_DELETE_ERROR, indicatorUuid, indicatorVersionNumber);
            }
        }
    }

    /* Data finders */

    @Override
    public IndicatorObservationsVO findObservationsInIndicatorPublished(ServiceContext ctx, String indicatorUuid, IndicatorsDataFilterVO dataFilter) throws MetamacException {
        IndicatorVersion indicatorVersion = getIndicatorPublishedVersion(indicatorUuid);

        return findObservationsInIndicatorVersion(ctx, indicatorVersion, dataFilter);
    }

    @Override
    public IndicatorObservationsVO findObservationsInIndicatorLastVersion(ServiceContext ctx, String indicatorUuid, IndicatorsDataFilterVO dataFilter) throws MetamacException {
        IndicatorVersion indicatorVersion = getIndicatorLastVersion(indicatorUuid);

        return findObservationsInIndicatorVersion(ctx, indicatorVersion, dataFilter);
    }

    private IndicatorObservationsVO findObservationsInIndicatorVersion(ServiceContext ctx, IndicatorVersion indicatorVersion, IndicatorsDataFilterVO dataFilter) throws MetamacException {

        List<String> geoCodes = retrieveGeographicalCodesFiltered(ctx, indicatorVersion, dataFilter.getGeoFilter());
        List<String> timeCodes = retrieveTimeValuesFiltered(ctx, indicatorVersion, dataFilter.getTimeFilter());
        List<String> measureCodes = retrieveMeasureValuesFiltered(ctx, indicatorVersion, dataFilter.getMeasureFilter());

        List<ConditionDimensionDto> newConditions = createConditionsByValues(geoCodes, timeCodes, measureCodes);

        Map<String, ObservationDto> observations = null;
        try {
            observations = findObservationsByDimensions(ctx, indicatorVersion, newConditions);
        } catch (ApplicationException e) {
            throw new MetamacException(e, ServiceExceptionType.DATA_FIND_OBSERVATIONS_ERROR, indicatorVersion.getIndicator().getUuid());
        }

        return buildIndicatorsObservations(geoCodes, timeCodes, measureCodes, observations);
    }

    @Override
    public IndicatorObservationsExtendedVO findObservationsExtendedByDimensionsInIndicatorPublished(ServiceContext ctx, String indicatorUuid, IndicatorsDataFilterVO dataFilter)
            throws MetamacException {
        IndicatorVersion indicatorVersion = getIndicatorPublishedVersion(indicatorUuid);

        return findObservationsExtendedInIndicatorVersion(ctx, indicatorVersion, dataFilter);
    }

    @Override
    public IndicatorObservationsExtendedVO findObservationsExtendedByDimensionsInIndicatorLastVersion(ServiceContext ctx, String indicatorUuid, IndicatorsDataFilterVO dataFilter)
            throws MetamacException {
        IndicatorVersion indicatorVersion = getIndicatorLastVersion(indicatorUuid);
        return findObservationsExtendedInIndicatorVersion(ctx, indicatorVersion, dataFilter);
    }

    private IndicatorObservationsExtendedVO findObservationsExtendedInIndicatorVersion(ServiceContext ctx, IndicatorVersion indicatorVersion, IndicatorsDataFilterVO dataFilter)
            throws MetamacException {

        List<String> geoCodes = retrieveGeographicalCodesFiltered(ctx, indicatorVersion, dataFilter.getGeoFilter());
        List<String> timeCodes = retrieveTimeValuesFiltered(ctx, indicatorVersion, dataFilter.getTimeFilter());
        List<String> measureCodes = retrieveMeasureValuesFiltered(ctx, indicatorVersion, dataFilter.getMeasureFilter());

        List<ConditionDimensionDto> newConditions = createConditionsByValues(geoCodes, timeCodes, measureCodes);

        Map<String, ObservationExtendedDto> observations = null;
        try {
            observations = findObservationsExtendedByDimensions(ctx, indicatorVersion, newConditions);
        } catch (ApplicationException e) {
            throw new MetamacException(e, ServiceExceptionType.DATA_FIND_OBSERVATIONS_ERROR, indicatorVersion.getIndicator().getUuid());
        }

        return buildIndicatorsObservationsExtended(geoCodes, timeCodes, measureCodes, observations);
    }

    @Override
    public IndicatorObservationsVO findObservationsInIndicatorInstanceWithPublishedIndicator(ServiceContext ctx, String indicatorInstanceUuid, IndicatorsDataFilterVO dataFilter)
            throws MetamacException {
        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);
        IndicatorVersion indicatorVersion = getIndicatorPublishedVersion(indInstance.getIndicator().getUuid());

        return findObservationsInIndicatorInstanceWithIndicatorVersion(ctx, indInstance, indicatorVersion, dataFilter);
    }

    @Override
    public IndicatorObservationsVO findObservationsInIndicatorInstanceWithLastVersionIndicator(ServiceContext ctx, String indicatorInstanceUuid, IndicatorsDataFilterVO dataFilter)
            throws MetamacException {
        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);
        IndicatorVersion indicatorVersion = getIndicatorLastVersion(indInstance.getIndicator().getUuid());

        return findObservationsInIndicatorInstanceWithIndicatorVersion(ctx, indInstance, indicatorVersion, dataFilter);
    }

    private IndicatorObservationsVO findObservationsInIndicatorInstanceWithIndicatorVersion(ServiceContext ctx, IndicatorInstance indicatorInstance, IndicatorVersion indicatorVersion,
            IndicatorsDataFilterVO dataFilter) throws MetamacException {

        List<String> geoCodes = retrieveGeographicalCodesInstanceFiltered(ctx, indicatorInstance, indicatorVersion, dataFilter.getGeoFilter());
        List<String> timeCodes = retrieveTimeValuesInstanceFiltered(ctx, indicatorInstance, indicatorVersion, dataFilter.getTimeFilter());
        List<String> measureCodes = retrieveMeasureValuesInstanceFiltered(ctx, indicatorInstance, indicatorVersion, dataFilter.getMeasureFilter());

        List<ConditionDimensionDto> newConditions = createConditionsByValues(geoCodes, timeCodes, measureCodes);

        Map<String, ObservationDto> observations = null;
        try {
            observations = findObservationsByDimensions(ctx, indicatorVersion, newConditions);
        } catch (ApplicationException e) {
            throw new MetamacException(e, ServiceExceptionType.DATA_INSTANCES_FIND_OBSERVATIONS_ERROR, indicatorInstance.getUuid());
        }

        return buildIndicatorsObservations(geoCodes, timeCodes, measureCodes, observations);
    }

    private IndicatorObservationsVO buildIndicatorsObservations(List<String> geoCodes, List<String> timeCodes, List<String> measureCodes, Map<String, ObservationDto> observations) {
        IndicatorObservationsVO indicatorObservations = new IndicatorObservationsVO();
        indicatorObservations.setGeographicalCodes(geoCodes);
        indicatorObservations.setTimeCodes(timeCodes);
        indicatorObservations.setMeasureCodes(measureCodes);
        indicatorObservations.setObservations(observations);
        return indicatorObservations;
    }

    private List<ConditionDimensionDto> createConditionsByValues(List<String> geoCodes, List<String> timeCodes, List<String> measureCodes) {
        List<ConditionDimensionDto> conditions = new ArrayList<ConditionDimensionDto>();

        ConditionDimensionDto geoCondition = new ConditionDimensionDto();
        geoCondition.setDimensionId(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name());
        geoCondition.setCodesDimension(geoCodes);
        conditions.add(geoCondition);

        ConditionDimensionDto timeCondition = new ConditionDimensionDto();
        timeCondition.setDimensionId(IndicatorDataDimensionTypeEnum.TIME.name());
        timeCondition.setCodesDimension(timeCodes);
        conditions.add(timeCondition);

        ConditionDimensionDto measureCondition = new ConditionDimensionDto();
        measureCondition.setDimensionId(IndicatorDataDimensionTypeEnum.MEASURE.name());
        measureCondition.setCodesDimension(measureCodes);
        conditions.add(measureCondition);

        return conditions;
    }

    private List<String> retrieveGeographicalCodesInstanceFiltered(ServiceContext ctx, IndicatorInstance indInstance, IndicatorVersion indicatorVersion, IndicatorsDataGeoDimensionFilterVO geoFilter)
            throws MetamacException {
        List<GeographicalCodeVO> coverage = getIndicatorsCoverageService().retrieveGeographicalCodesInIndicatorInstanceWithIndicatorVersion(ctx, indInstance, indicatorVersion);
        return filterGeoCodes(geoFilter, coverage);
    }

    private List<String> retrieveGeographicalCodesFiltered(ServiceContext ctx, IndicatorVersion indicatorVersion, IndicatorsDataGeoDimensionFilterVO geoFilter) throws MetamacException {
        List<GeographicalCodeVO> coverage = getIndicatorsCoverageService().retrieveGeographicalCodesInIndicatorVersion(ctx, indicatorVersion);
        return filterGeoCodes(geoFilter, coverage);
    }

    private List<String> filterGeoCodes(IndicatorsDataGeoDimensionFilterVO filter, List<GeographicalCodeVO> coverage) {
        if (filter != null) {
            List<String> filteredCodes = new ArrayList<String>();
            boolean codeFilterEnabled = !filter.getCodes().isEmpty();
            boolean granularityFilterEnabled = !filter.getGranularityCodes().isEmpty();

            for (GeographicalCodeVO geoCodeVO : coverage) {
                if (granularityFilterEnabled && !filter.getGranularityCodes().contains(geoCodeVO.getGranularityCode())) {
                    continue;
                }
                if (codeFilterEnabled && !filter.getCodes().contains(geoCodeVO.getCode())) {
                    continue;
                }
                filteredCodes.add(geoCodeVO.getCode());
            }
            return filteredCodes;
        }
        return getCodesInGeographicalCodes(coverage);
    }

    private List<String> retrieveTimeValuesInstanceFiltered(ServiceContext ctx, IndicatorInstance indInstance, IndicatorVersion indicatorVersion, IndicatorsDataTimeDimensionFilterVO filter)
            throws MetamacException {
        List<TimeValue> coverage = getIndicatorsCoverageService().retrieveTimeValuesInIndicatorInstanceWithIndicatorVersion(ctx, indInstance, indicatorVersion);
        return filterTimeCodes(filter, coverage);
    }

    private List<String> retrieveTimeValuesFiltered(ServiceContext ctx, IndicatorVersion indicatorVersion, IndicatorsDataTimeDimensionFilterVO filter) throws MetamacException {
        List<TimeValue> coverage = getIndicatorsCoverageService().retrieveTimeValuesInIndicatorVersion(ctx, indicatorVersion);
        return filterTimeCodes(filter, coverage);
    }

    protected List<String> filterTimeCodes(IndicatorsDataTimeDimensionFilterVO filter, List<TimeValue> coverage) {
        if (filter != null) {
            List<String> filteredCodes = new ArrayList<String>();
            boolean codeFilterEnabled = !filter.getCodes().isEmpty();
            boolean granularityFilterEnabled = !filter.getGranularityCodes().isEmpty();

            for (TimeValue timeValue : coverage) {
                if (granularityFilterEnabled && !filter.getGranularityCodes().contains(timeValue.getGranularity().name())) {
                    continue;
                }
                if (codeFilterEnabled && !filter.getCodes().contains(timeValue.getTimeValue())) {
                    continue;
                }
                filteredCodes.add(timeValue.getTimeValue());
            }
            return filteredCodes;
        }
        return getCodesInTimeValues(coverage);
    }

    private List<String> retrieveMeasureValuesInstanceFiltered(ServiceContext ctx, IndicatorInstance indInstance, IndicatorVersion indicatorVersion, IndicatorsDataMeasureDimensionFilterVO filter)
            throws MetamacException {
        List<MeasureValue> coverage = getIndicatorsCoverageService().retrieveMeasureValuesInIndicatorVersion(ctx, indicatorVersion);
        return filterMeasureCodes(filter, coverage);
    }

    private List<String> retrieveMeasureValuesFiltered(ServiceContext ctx, IndicatorVersion indicatorVersion, IndicatorsDataMeasureDimensionFilterVO filter) throws MetamacException {
        List<MeasureValue> coverage = getIndicatorsCoverageService().retrieveMeasureValuesInIndicatorVersion(ctx, indicatorVersion);
        return filterMeasureCodes(filter, coverage);
    }

    protected List<String> filterMeasureCodes(IndicatorsDataMeasureDimensionFilterVO filter, List<MeasureValue> coverage) {
        if (filter != null && !filter.getCodes().isEmpty()) {
            return filterMeasureValuesByCodes(coverage, filter.getCodes());
        }
        return getCodesInMeasureValues(coverage);
    }

    private List<String> filterMeasureValuesByCodes(List<MeasureValue> coverage, List<String> filterCodes) {
        List<String> filteredCoverage = new ArrayList<String>();
        for (MeasureValue measureValue : coverage) {
            if (filterCodes.contains(measureValue.getMeasureValue().name())) {
                filteredCoverage.add(measureValue.getMeasureValue().name());
            }
        }
        return filteredCoverage;
    }

    @Override
    public IndicatorObservationsExtendedVO findObservationsExtendedByDimensionsInIndicatorInstanceWithPublishedIndicator(ServiceContext ctx, String indicatorInstanceUuid,
            IndicatorsDataFilterVO dataFilter) throws MetamacException {
        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);
        IndicatorVersion indicatorVersion = getIndicatorPublishedVersion(indInstance.getIndicator().getUuid());

        return findObservationsExtendedInIndicatorInstanceWithIndicatorVersion(ctx, indInstance, indicatorVersion, dataFilter);
    }

    @Override
    public IndicatorObservationsExtendedVO findObservationsExtendedByDimensionsInIndicatorInstanceWithLastVersionIndicator(ServiceContext ctx, String indicatorInstanceUuid,
            IndicatorsDataFilterVO dataFilter) throws MetamacException {
        IndicatorInstance indInstance = getIndicatorInstance(indicatorInstanceUuid);
        IndicatorVersion indicatorVersion = getIndicatorLastVersion(indInstance.getIndicator().getUuid());

        return findObservationsExtendedInIndicatorInstanceWithIndicatorVersion(ctx, indInstance, indicatorVersion, dataFilter);
    }

    private IndicatorObservationsExtendedVO findObservationsExtendedInIndicatorInstanceWithIndicatorVersion(ServiceContext ctx, IndicatorInstance indicatorInstance, IndicatorVersion indicatorVersion,
            IndicatorsDataFilterVO dataFilter) throws MetamacException {

        List<String> geoCodes = retrieveGeographicalCodesInstanceFiltered(ctx, indicatorInstance, indicatorVersion, dataFilter.getGeoFilter());
        List<String> timeCodes = retrieveTimeValuesInstanceFiltered(ctx, indicatorInstance, indicatorVersion, dataFilter.getTimeFilter());
        List<String> measureCodes = retrieveMeasureValuesInstanceFiltered(ctx, indicatorInstance, indicatorVersion, dataFilter.getMeasureFilter());

        List<ConditionDimensionDto> newConditions = createConditionsByValues(geoCodes, timeCodes, measureCodes);

        Map<String, ObservationExtendedDto> observations = null;
        try {
            observations = findObservationsExtendedByDimensions(ctx, indicatorVersion, newConditions);
        } catch (ApplicationException e) {
            throw new MetamacException(e, ServiceExceptionType.DATA_INSTANCES_FIND_OBSERVATIONS_ERROR, indicatorInstance.getUuid());
        }

        return buildIndicatorsObservationsExtended(geoCodes, timeCodes, measureCodes, observations);
    }

    protected IndicatorObservationsExtendedVO buildIndicatorsObservationsExtended(List<String> geoCodes, List<String> timeCodes, List<String> measureCodes,
            Map<String, ObservationExtendedDto> observations) {
        IndicatorObservationsExtendedVO indicatorObservations = new IndicatorObservationsExtendedVO();
        indicatorObservations.setGeographicalCodes(geoCodes);
        indicatorObservations.setTimeCodes(timeCodes);
        indicatorObservations.setMeasureCodes(measureCodes);
        indicatorObservations.setObservations(observations);
        return indicatorObservations;
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

            deleteIndicatorInstanceLastValuesCache(indicatorInstanceUuid);
            buildIndicatorInstanceLatestValuesCache(ctx, instance);
        }
        LOG.info("Updated last value cache data for indicator uuid:" + indicatorVersion.getIndicator().getUuid() + " version: " + indicatorVersion.getVersionNumber());
    }

    @Override
    public void buildIndicatorVersionLatestValuesCache(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {
        List<String> geoCodes = getCodesInGeographicalCodes(getIndicatorsCoverageService().retrieveGeographicalCodesInIndicatorVersion(ctx, indicatorVersion));
        List<TimeValue> timeValues = getIndicatorsCoverageService().retrieveTimeValuesInIndicatorVersion(ctx, indicatorVersion);

        List<String> geoCodesLeft = geoCodes;

        while (!geoCodesLeft.isEmpty() && !timeValues.isEmpty()) {
            TimeValue lastTimeValue = TimeVariableUtils.findLatestTimeValue(timeValues);
            timeValues.remove(lastTimeValue);

            Set<String> foundGeoCodes = getGeoValuesInObservations(indicatorVersion, geoCodesLeft, lastTimeValue.getTimeValue());

            for (String geoCode : foundGeoCodes) {
                geoCodesLeft.remove(geoCode);

                IndicatorVersionLastValueCache cacheEntry = new IndicatorVersionLastValueCache(geoCode, indicatorVersion);
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

        List<String> geoCodes = getCodesInGeographicalCodes(getIndicatorsCoverageService().retrieveGeographicalCodesInIndicatorVersion(ctx, indicatorVersion));

        List<TimeValue> timeValues = getIndicatorsCoverageService().retrieveTimeValuesInIndicatorInstanceWithPublishedIndicator(ctx, indicatorInstance.getUuid());

        List<String> geoValuesLeft = geoCodes;

        while (!geoValuesLeft.isEmpty() && !timeValues.isEmpty()) {
            TimeValue lastTimeValue = TimeVariableUtils.findLatestTimeValue(timeValues);
            timeValues.remove(lastTimeValue);

            Set<String> foundGeoCodes = getGeoValuesInObservations(indicatorVersion, geoValuesLeft, lastTimeValue.getTimeValue());

            for (String geoCode : foundGeoCodes) {
                geoValuesLeft.remove(geoCode);

                IndicatorInstanceLastValueCache cacheEntry = new IndicatorInstanceLastValueCache(geoCode, indicatorInstance);
                cacheEntry.setLastTimeValue(lastTimeValue.getTimeValue());
                cacheEntry.setLastDataUpdated(indicatorVersion.getLastPopulateDate());
                getIndicatorInstanceLastValueCacheRepository().save(cacheEntry);
            }
        }
    }

    private void deleteIndicatorInstanceLastValuesCache(String indicatorInstanceUuid) {
        getIndicatorInstanceLastValueCacheRepository().deleteWithIndicatorInstance(indicatorInstanceUuid);
    }

    protected void rebuildCoveragesCache(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {
        rebuildGeoCoverageCache(ctx, indicatorVersion);

        rebuildMeasureCoverageCache(ctx, indicatorVersion);

        rebuildTimeCoverageCache(ctx, indicatorVersion);
    }

    private void rebuildGeoCoverageCache(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {
        LOG.info("Updating geo coverage cache for indicator uuid:" + indicatorVersion.getIndicator().getUuid() + " version: " + indicatorVersion.getVersionNumber());

        getIndicatorVersionGeoCoverageRepository().deleteCoverageForIndicatorVersion(indicatorVersion);

        buildIndicatorVersionGeoCoverageCache(ctx, indicatorVersion);

        LOG.info("Updated geo coverage cache for indicator uuid:" + indicatorVersion.getIndicator().getUuid() + " version: " + indicatorVersion.getVersionNumber());
    }

    private void buildIndicatorVersionGeoCoverageCache(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {
        List<GeographicalValue> geoValues = calculateGeographicalValuesInIndicatorVersionFromData(indicatorVersion);

        for (GeographicalValue geoValue : geoValues) {
            IndicatorVersionGeoCoverage geoCoverage = new IndicatorVersionGeoCoverage(geoValue, indicatorVersion);
            getIndicatorVersionGeoCoverageRepository().save(geoCoverage);
        }
    }

    private List<GeographicalValue> calculateGeographicalValuesInIndicatorVersionFromData(IndicatorVersion indicatorVersion) throws MetamacException {
        checkIndicatorVersionHasDataPopulated(indicatorVersion);
        try {
            List<String> geographicalCodesInIndicator = findCodesForDimensionInIndicator(indicatorVersion, IndicatorDataDimensionTypeEnum.GEOGRAPHICAL);
            List<GeographicalValue> geographicalValuesInIndicator = getGeographicalValueRepository().findGeographicalValuesByCodes(geographicalCodesInIndicator);
            ServiceUtils.sortGeographicalValuesList(geographicalValuesInIndicator);
            return geographicalValuesInIndicator;
        } catch (ApplicationException e) {
            throw new MetamacException(e, ServiceExceptionType.INDICATOR_FIND_DIMENSION_CODES_ERROR, indicatorVersion.getIndicator().getUuid(),
                    ServiceExceptionParameters.INDICATOR_DATA_DIMENSION_TYPE_GEOGRAPHICAL);
        }
    }

    private void rebuildMeasureCoverageCache(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {
        LOG.info("Updating measure coverage cache for indicator uuid:" + indicatorVersion.getIndicator().getUuid() + " version: " + indicatorVersion.getVersionNumber());

        getIndicatorVersionMeasureCoverageRepository().deleteCoverageForIndicatorVersion(indicatorVersion);

        buildIndicatorVersionMeasureCoverageCache(ctx, indicatorVersion);

        LOG.info("Updated measure coverage cache for indicator uuid:" + indicatorVersion.getIndicator().getUuid() + " version: " + indicatorVersion.getVersionNumber());
    }

    private void buildIndicatorVersionMeasureCoverageCache(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {
        List<MeasureValue> measureValues = calculateMeasureValuesInIndicatorVersionFromData(ctx, indicatorVersion);
        for (MeasureValue measureValue : measureValues) {
            IndicatorVersionMeasureCoverage measureCoverage = new IndicatorVersionMeasureCoverage(measureValue.getMeasureValue().getName(), indicatorVersion);
            Translation translation = getMeasureValueTranslation(measureValue);
            measureCoverage.setTranslation(translation);
            getIndicatorVersionMeasureCoverageRepository().save(measureCoverage);
        }
    }

    protected Translation getMeasureValueTranslation(MeasureValue measureValue) {
        String translationCode = new StringBuilder().append(IndicatorsConstants.TRANSLATION_MEASURE_DIMENSION).append(".").append(measureValue.getMeasureValue().name()).toString();
        Translation translation = getTranslationRepository().findTranslationByCode(translationCode);
        return translation;
    }

    private List<MeasureValue> calculateMeasureValuesInIndicatorVersionFromData(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {
        checkIndicatorVersionHasDataPopulated(indicatorVersion);
        try {
            List<String> measureCodesInIndicator = findCodesForDimensionInIndicator(indicatorVersion, IndicatorDataDimensionTypeEnum.MEASURE);
            List<MeasureValue> measureValuesInIndicator = getIndicatorsSystemsService().retrieveMeasuresValues(ctx, measureCodesInIndicator);
            return measureValuesInIndicator;
        } catch (ApplicationException e) {
            throw new MetamacException(e, ServiceExceptionType.INDICATOR_FIND_DIMENSION_CODES_ERROR, indicatorVersion.getIndicator().getUuid(),
                    ServiceExceptionParameters.INDICATOR_DATA_DIMENSION_TYPE_TIME);
        }
    }

    private void rebuildTimeCoverageCache(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {
        LOG.info("Updating time coverage cache for indicator uuid:" + indicatorVersion.getIndicator().getUuid() + " version: " + indicatorVersion.getVersionNumber());

        getIndicatorVersionTimeCoverageRepository().deleteCoverageForIndicatorVersion(indicatorVersion);

        buildIndicatorVersionTimeCoverageCache(ctx, indicatorVersion);

        LOG.info("Updated time coverage cache for indicator uuid:" + indicatorVersion.getIndicator().getUuid() + " version: " + indicatorVersion.getVersionNumber());
    }

    private void buildIndicatorVersionTimeCoverageCache(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {
        List<String> timeCodes = calculateTimeCodesInIndicatorVersionFromData(ctx, indicatorVersion);

        List<TimeGranularityEnum> granularities = getTimeCodesGranularities(timeCodes);

        Map<TimeGranularityEnum, Translation> granularitiesTranslations = getTimeGranularitiesTranslations(granularities);

        Map<String, Translation> translations = getTimeCodesTranslations(timeCodes);

        for (String timeCode : timeCodes) {
            TimeValue timeValue = TimeVariableUtils.parseTimeValue(timeCode);
            TimeGranularityEnum timeGranularity = timeValue.getGranularity();

            IndicatorVersionTimeCoverage timeCoverage = new IndicatorVersionTimeCoverage(timeCode, indicatorVersion);
            timeCoverage.setTimeGranularity(timeGranularity.getName());
            timeCoverage.setTranslation(translations.get(timeCode));
            timeCoverage.setGranularityTranslation(granularitiesTranslations.get(timeGranularity));

            getIndicatorVersionTimeCoverageRepository().save(timeCoverage);
        }
    }

    private Map<String, Translation> getTimeCodesTranslations(List<String> timeCodes) throws MetamacException {
        List<String> translationCodes = new ArrayList<String>();
        for (String timeCode : timeCodes) {
            String translationCode = TimeVariableUtils.getTimeValueTranslationCode(timeCode);
            translationCodes.add(translationCode);
        }
        Map<String, Translation> translations = getTranslationRepository().findTranslationsByCodes(translationCodes);
        Map<String, Translation> translationsByTimeCode = new HashMap<String, Translation>();
        for (String timeCode : timeCodes) {
            String translationCode = TimeVariableUtils.getTimeValueTranslationCode(timeCode);
            translationsByTimeCode.put(timeCode, translations.get(translationCode));
        }
        return translationsByTimeCode;
    }

    private Map<TimeGranularityEnum, Translation> getTimeGranularitiesTranslations(List<TimeGranularityEnum> granularities) throws MetamacException {
        List<String> translationCodes = new ArrayList<String>();
        for (TimeGranularityEnum granularity : granularities) {
            String translationCode = TimeVariableUtils.getTimeGranularityTranslationCode(granularity);
            translationCodes.add(translationCode);
        }

        Map<String, Translation> translations = getTranslationRepository().findTranslationsByCodes(translationCodes);
        Map<TimeGranularityEnum, Translation> translationsByGranularity = new HashMap<TimeGranularityEnum, Translation>();
        for (TimeGranularityEnum granularity : granularities) {
            String translationCode = TimeVariableUtils.getTimeGranularityTranslationCode(granularity);
            translationsByGranularity.put(granularity, translations.get(translationCode));
        }
        return translationsByGranularity;
    }

    private List<TimeGranularityEnum> getTimeCodesGranularities(List<String> timeCodes) throws MetamacException {
        Set<TimeGranularityEnum> granularities = new HashSet<TimeGranularityEnum>();
        for (String timeCode : timeCodes) {
            granularities.add(TimeVariableUtils.guessTimeGranularity(timeCode));
        }

        return new ArrayList<TimeGranularityEnum>(granularities);
    }

    private List<String> calculateTimeCodesInIndicatorVersionFromData(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {
        checkIndicatorVersionHasDataPopulated(indicatorVersion);
        try {
            return findCodesForDimensionInIndicator(indicatorVersion, IndicatorDataDimensionTypeEnum.TIME);
        } catch (ApplicationException e) {
            throw new MetamacException(e, ServiceExceptionType.INDICATOR_FIND_DIMENSION_CODES_ERROR, indicatorVersion.getIndicator().getUuid(),
                    ServiceExceptionParameters.INDICATOR_DATA_DIMENSION_TYPE_TIME);
        }
    }

    private Set<String> getGeoValuesInObservations(IndicatorVersion indicatorVersion, List<String> geoCodes, String timeValue) throws MetamacException {

        List<ConditionDimensionDto> conditions = new ArrayList<ConditionDimensionDto>();

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
            Set<String> foundGeoCodes = new HashSet<String>();
            for (ObservationDto obs : observations.values()) {
                String geoCode = null;
                for (CodeDimensionDto codeDim : obs.getCodesDimension()) {
                    if (GEO_DIMENSION.equals(codeDim.getDimensionId())) {
                        geoCode = codeDim.getCodeDimensionId();
                    }
                }
                foundGeoCodes.add(geoCode);
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

    private List<String> getCodesInGeographicalCodes(List<GeographicalCodeVO> geoValueCodes) {
        List<String> geoCodes = new ArrayList<String>();
        for (GeographicalCodeVO geoValue : geoValueCodes) {
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

    private List<String> getCodesInMeasureValues(List<MeasureValue> values) {
        List<String> codes = new ArrayList<String>();
        for (MeasureValue measure : values) {
            codes.add(measure.getMeasureValue().getName());
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
        checkIndicatorVersionHasDataPopulated(indicatorVersion);

        return fillEmptyDataCross(datasetRepositoriesServiceFacade.findObservationsByDimensions(indicatorVersion.getDataRepositoryId(), conditions));
    }

    private Map<String, ObservationExtendedDto> findObservationsExtendedByDimensions(ServiceContext ctx, IndicatorVersion indicatorVersion, List<ConditionDimensionDto> conditions)
            throws MetamacException, ApplicationException {

        checkIndicatorVersionHasDataPopulated(indicatorVersion);

        String datasetId = indicatorVersion.getDataRepositoryId();
        return datasetRepositoriesServiceFacade.findObservationsExtendedByDimensions(datasetId, conditions);
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

    private String getDataViewsRole() throws MetamacException {
        return configurationService.retrieveDbDataViewsRole();
    }

    private void checkIndicatorVersionHasDataPopulated(IndicatorVersion indicatorVersion) throws MetamacException {
        if (indicatorVersion.getDataRepositoryId() == null) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_NOT_POPULATED, indicatorVersion.getIndicator().getUuid(), indicatorVersion.getVersionNumber());
        }
    }

}
