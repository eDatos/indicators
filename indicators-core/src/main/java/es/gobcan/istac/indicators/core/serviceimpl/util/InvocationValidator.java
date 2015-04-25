package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.core.common.util.CoreCommonUtil;

import com.arte.statistic.dataset.repository.util.ValidationUtils;

import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.DataSourceVariable;
import es.gobcan.istac.indicators.core.domain.Dimension;
import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.Indicator;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.Quantity;
import es.gobcan.istac.indicators.core.domain.QuantityUnit;
import es.gobcan.istac.indicators.core.domain.RateDerivation;
import es.gobcan.istac.indicators.core.domain.UnitMultiplier;
import es.gobcan.istac.indicators.core.enume.domain.MeasureDimensionTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.RateDerivationMethodTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.TimeGranularityEnum;
import es.gobcan.istac.indicators.core.enume.domain.VersionTypeEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionParameters;
import es.gobcan.istac.indicators.core.error.ServiceExceptionParametersInternal;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.util.IndicatorUtils;

public class InvocationValidator {

    // --------------------------------------------------------------------------------------------
    // INDICATOR SYSTEM
    // --------------------------------------------------------------------------------------------

    public static void checkCreateIndicatorsSystem(IndicatorsSystemVersion indicatorsSystemVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkIndicatorsSystem(indicatorsSystemVersion, exceptions);
        if (indicatorsSystemVersion != null) {
            // uuid never is null: it is initialized when create object
            IndicatorsValidationUtils.checkMetadataEmpty(indicatorsSystemVersion.getIndicatorsSystem().getId(), ServiceExceptionParameters.INDICATORS_SYSTEM_UUID, exceptions);
            IndicatorsValidationUtils.checkMetadataEmpty(indicatorsSystemVersion.getId(), ServiceExceptionParameters.INDICATORS_SYSTEM_UUID, exceptions);
            IndicatorsValidationUtils.checkMetadataEmpty(indicatorsSystemVersion.getVersionNumber(), ServiceExceptionParameters.INDICATORS_SYSTEM_VERSION_NUMBER, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateIndicatorsSystem(IndicatorsSystemVersion indicatorsSystemVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkIndicatorsSystem(indicatorsSystemVersion, exceptions);
        if (indicatorsSystemVersion != null) {
            // uuid never is null: it is initialized when create object
            IndicatorsValidationUtils.checkMetadataRequired(indicatorsSystemVersion.getId(), ServiceExceptionParameters.INDICATORS_SYSTEM_UUID, exceptions);
            IndicatorsValidationUtils.checkMetadataRequired(indicatorsSystemVersion.getIndicatorsSystem().getId(), ServiceExceptionParameters.INDICATORS_SYSTEM_UUID, exceptions);
            IndicatorsValidationUtils.checkMetadataRequired(indicatorsSystemVersion.getVersionNumber(), ServiceExceptionParameters.INDICATORS_SYSTEM_VERSION_NUMBER, exceptions);
            // unmodifiable metadatas are checked in Dto2DoMapper
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveIndicatorsSystem(String uuid, String version, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);
        // version is optional

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveIndicatorsSystemPublished(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveIndicatorsSystemByCode(String code, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(code, ServiceExceptionParameters.CODE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveIndicatorsSystemPublishedByCode(String code, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(code, ServiceExceptionParameters.CODE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveIndicatorsSystemStructure(String uuid, String version, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);
        // version is optional

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkDeleteIndicatorsSystem(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkSendIndicatorsSystemToProductionValidation(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRejectIndicatorsSystemProductionValidation(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkSendIndicatorsSystemToDiffusionValidation(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRejectIndicatorsSystemDiffusionValidation(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkPublishIndicatorsSystem(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkArchiveIndicatorsSystem(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkVersioningIndicatorsSystem(String uuid, VersionTypeEnum versionType, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(versionType, ServiceExceptionParameters.INDICATORS_SYSTEM_VERSION_TYPE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindIndicatorsSystems(List<ConditionalCriteria> conditions, PagingParameter pagingParameter, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // Note: properties names of criteria restrictions are checked in MetamacCriteria2SculptorCriteriaMapper

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindIndicatorsSystemsPublished(List<ConditionalCriteria> conditions, PagingParameter pagingParameter, List<MetamacExceptionItem> exceptions) throws MetamacException {
        checkFindIndicatorsSystems(conditions, pagingParameter, exceptions);
    }

    public static void checkRetrieveIndicatorsSystemPublishedForIndicator(String indicatorUuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(indicatorUuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindIndicatorsSystemHistory(String uuid, int maxResults, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.INDICATORS_SYSTEM_UUID, exceptions);
        if (maxResults <= 0) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.NUM_RESULTS));
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindIndicatorsSystemsHistory(List<ConditionalCriteria> conditions, PagingParameter pagingParameter, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // Note: properties names of criteria restrictions are checked in MetamacCriteria2SculptorCriteriaMapper

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkExportIndicatorsSystemPublishedToDspl(String indicatorsSystemUuid, InternationalString title, InternationalString description, List<MetamacExceptionItem> exceptions)
            throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(indicatorsSystemUuid, ServiceExceptionParameters.INDICATORS_SYSTEM_UUID, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(title, ServiceExceptionParameters.DSPL_DATASET_TITLE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkExportIndicatorsSystemPublishedToDsplFiles(String indicatorsSystemUuid, InternationalString title, InternationalString description, List<MetamacExceptionItem> exceptions)
            throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(indicatorsSystemUuid, ServiceExceptionParameters.INDICATORS_SYSTEM_UUID, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(title, ServiceExceptionParameters.DSPL_DATASET_TITLE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    // --------------------------------------------------------------------------------------------
    // DIMENSION
    // --------------------------------------------------------------------------------------------

    public static void checkCreateDimension(String indicatorsSystemUuid, Dimension dimension, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(indicatorsSystemUuid, ServiceExceptionParameters.INDICATORS_SYSTEM_UUID, exceptions);
        checkDimension(dimension, exceptions);
        if (dimension != null) {
            // uuid never is null: it is initialized when create object
            IndicatorsValidationUtils.checkMetadataEmpty(dimension.getId(), ServiceExceptionParameters.DIMENSION_UUID, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveDimension(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkDeleteDimension(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveDimensionsByIndicatorsSystem(String indicatorsSystemUuid, String indicatorsSystemVersionNumber, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(indicatorsSystemUuid, ServiceExceptionParameters.INDICATORS_SYSTEM_UUID, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(indicatorsSystemVersionNumber, ServiceExceptionParameters.INDICATORS_SYSTEM_VERSION_NUMBER, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateDimension(Dimension dimension, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkDimension(dimension, exceptions);
        if (dimension != null) {
            // uuid never is null: it is initialized when create object
            IndicatorsValidationUtils.checkMetadataRequired(dimension.getId(), ServiceExceptionParameters.DIMENSION_UUID, exceptions);
            // unmodifiable metadatas are checked in Dto2DoMapper
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateDimensionLocation(String uuid, String parentUuid, Long orderInLevel, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(orderInLevel, ServiceExceptionParameters.DIMENSION_ORDER_IN_LEVEL, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveIndicatorsSystemByDimension(ServiceContext ctx, String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    // --------------------------------------------------------------------------------------------
    // INDICATOR
    // --------------------------------------------------------------------------------------------

    public static void checkCreateIndicator(IndicatorVersion indicatorVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkIndicator(indicatorVersion, exceptions);
        if (indicatorVersion != null) {
            IndicatorsValidationUtils.checkMetadataEmpty(indicatorVersion.getId(), ServiceExceptionParameters.INDICATOR_UUID, exceptions);
            IndicatorsValidationUtils.checkMetadataEmpty(indicatorVersion.getIndicator().getId(), ServiceExceptionParameters.INDICATOR_UUID, exceptions);
            IndicatorsValidationUtils.checkMetadataEmpty(indicatorVersion.getVersionNumber(), ServiceExceptionParameters.INDICATOR_VERSION_NUMBER, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateIndicator(Indicator indicator, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkMetadataRequired(indicator, ServiceExceptionParameters.INDICATOR, exceptions);
        if (indicator != null) {
            // uuid never is null: it is initialized when create object
            IndicatorsValidationUtils.checkMetadataRequired(indicator.getId(), ServiceExceptionParameters.INDICATOR_UUID, exceptions);
            // unmodifiable metadatas are checked in Dto2DoMapper
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateIndicatorVersion(IndicatorVersion indicatorVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkIndicator(indicatorVersion, exceptions);
        if (indicatorVersion != null) {
            // uuid never is null: it is initialized when create object
            IndicatorsValidationUtils.checkMetadataRequired(indicatorVersion.getId(), ServiceExceptionParameters.INDICATOR_UUID, exceptions);
            IndicatorsValidationUtils.checkMetadataRequired(indicatorVersion.getIndicator().getId(), ServiceExceptionParameters.INDICATOR_UUID, exceptions);
            IndicatorsValidationUtils.checkMetadataRequired(indicatorVersion.getVersionNumber(), ServiceExceptionParameters.INDICATOR_VERSION_NUMBER, exceptions);
            // unmodifiable metadatas are checked in Dto2DoMapper
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveIndicator(String uuid, String version, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);
        // version is optional

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveIndicatorPublished(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveIndicatorByCode(String code, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(code, ServiceExceptionParameters.CODE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveIndicatorPublishedByCode(String code, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(code, ServiceExceptionParameters.CODE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkDeleteIndicator(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkSendIndicatorToProductionValidation(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRejectIndicatorProductionValidation(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkSendIndicatorToDiffusionValidation(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRejectIndicatorDiffusionValidation(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkPublishIndicator(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkArchiveIndicator(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkVersioningIndicator(String uuid, VersionTypeEnum versionType, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(versionType, ServiceExceptionParameters.INDICATOR_VERSION_TYPE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindIndicators(List<ConditionalCriteria> conditions, PagingParameter pagingParameter, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // Note: properties names of criteria restrictions are checked in MetamacCriteria2SculptorCriteriaMapper

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindIndicatorsPublished(List<ConditionalCriteria> conditions, PagingParameter pagingParameter, List<MetamacExceptionItem> exceptions) throws MetamacException {
        checkFindIndicators(conditions, pagingParameter, exceptions);
    }

    // --------------------------------------------------------------------------------------------
    // DATA SOURCES
    // --------------------------------------------------------------------------------------------

    public static void checkCreateDataSource(String indicatorUuid, DataSource dataSource, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(indicatorUuid, ServiceExceptionParameters.INDICATOR_UUID, exceptions);
        checkDataSource(dataSource, exceptions);
        if (dataSource != null) {
            IndicatorsValidationUtils.checkMetadataEmpty(dataSource.getId(), ServiceExceptionParameters.DATA_SOURCE_UUID, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveDataSource(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkDeleteDataSource(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveDataSourcesByIndicator(String indicatorUuid, String indicatorVersionNumber, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(indicatorUuid, ServiceExceptionParameters.INDICATOR_UUID, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(indicatorVersionNumber, ServiceExceptionParameters.INDICATOR_VERSION_NUMBER, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateDataSource(DataSource dataSource, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkDataSource(dataSource, exceptions);
        if (dataSource != null) {
            // uuid never is null: it is initialized when create object
            IndicatorsValidationUtils.checkMetadataRequired(dataSource.getId(), ServiceExceptionParameters.DATA_SOURCE_UUID, exceptions);
            // unmodifiable metadatas are checked in Dto2DoMapper
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    // --------------------------------------------------------------------------------------------
    // INDICATOR INSTANCES
    // --------------------------------------------------------------------------------------------

    public static void checkCreateIndicatorInstance(String indicatorsSystemUuid, IndicatorInstance indicatorInstance, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(indicatorsSystemUuid, ServiceExceptionParameters.INDICATORS_SYSTEM_UUID, exceptions);
        checkIndicatorInstance(indicatorInstance, exceptions);
        if (indicatorInstance != null) {
            // uuid never is null: it is initialized when create object
            IndicatorsValidationUtils.checkMetadataEmpty(indicatorInstance.getId(), ServiceExceptionParameters.INDICATOR_INSTANCE_UUID, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindIndicatorsInstancesInPublishedIndicatorsSystems(List<ConditionalCriteria> conditions, PagingParameter pagingParameter, List<MetamacExceptionItem> exceptions)
            throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // Note: properties names of criteria restrictions are checked in MetamacCriteria2SculptorCriteriaMapper

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindIndicatorsInstancesInLastVersionIndicatorsSystems(List<ConditionalCriteria> conditions, PagingParameter pagingParameter, List<MetamacExceptionItem> exceptions)
            throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // Note: properties names of criteria restrictions are checked in MetamacCriteria2SculptorCriteriaMapper

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveIndicatorInstance(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveIndicatorInstancePublishedByCode(String code, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(code, ServiceExceptionParameters.CODE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkDeleteIndicatorInstance(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveIndicatorsInstancesByIndicatorsSystem(String indicatorsSystemUuid, String indicatorsSystemVersionNumber, List<MetamacExceptionItem> exceptions)
            throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(indicatorsSystemUuid, ServiceExceptionParameters.INDICATORS_SYSTEM_UUID, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(indicatorsSystemVersionNumber, ServiceExceptionParameters.INDICATORS_SYSTEM_VERSION_NUMBER, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindIndicatorsInstancesInPublishedIndicatorSystemWithSubjectCode(String subjectCode, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(subjectCode, ServiceExceptionParameters.INDICATOR_SUBJECT_CODE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateIndicatorInstance(IndicatorInstance indicatorInstance, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkIndicatorInstance(indicatorInstance, exceptions);
        if (indicatorInstance != null) {
            IndicatorsValidationUtils.checkMetadataRequired(indicatorInstance.getUuid(), ServiceExceptionParameters.INDICATOR_INSTANCE_UUID, exceptions);
            // unmodifiable metadatas are checked in Dto2DoMapper
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateIndicatorInstanceLocation(String uuid, String parentUuid, Long orderInLevel, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(orderInLevel, ServiceExceptionParameters.INDICATOR_INSTANCE_ORDER_IN_LEVEL, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveIndicatorsSystemByIndicatorInstance(ServiceContext ctx, String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    // --------------------------------------------------------------------------------------------
    // SUBJECTS
    // --------------------------------------------------------------------------------------------

    public static void checkRetrieveSubject(String code, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(code, ServiceExceptionParameters.CODE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveSubjects(List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // nothing

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveSubjectsInPublishedIndicators(List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // nothing

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveSubjectsInLastVersionIndicators(List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // nothing

        ExceptionUtils.throwIfException(exceptions);
    }

    // --------------------------------------------------------------------------------------------
    // TIME VALUE
    // --------------------------------------------------------------------------------------------

    public static void checkRetrieveTimeValue(String timeValue, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(timeValue, ServiceExceptionParameters.TIME_VALUE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveTimeValues(List<String> timeValues, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(timeValues, ServiceExceptionParameters.TIME_VALUE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    // --------------------------------------------------------------------------------------------
    // TIME GRANULARITY
    // --------------------------------------------------------------------------------------------

    public static void checkRetrieveTimeGranularity(TimeGranularityEnum timeGranularity, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(timeGranularity, ServiceExceptionParameters.TIME_GRANULARITY, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveMeasureValue(MeasureDimensionTypeEnum measureValue, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(measureValue, ServiceExceptionParameters.MEASURE_VALUE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    // --------------------------------------------------------------------------------------------
    // GEOGRAPHICAL VALUE
    // --------------------------------------------------------------------------------------------

    public static void checkRetrieveGeographicalValue(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveGeographicalValueByCode(String code, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(code, ServiceExceptionParameters.CODE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindGeographicalValues(List<MetamacExceptionItem> exceptions, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // Note: properties names of criteria restrictions are checked in MetamacCriteria2SculptorCriteriaMapper

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkCreateGeographicalValue(List<MetamacExceptionItem> exceptions, GeographicalValue geographicalValue) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkGeographicalValue(geographicalValue, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkDeleteGeographicalValue(List<MetamacExceptionItem> exceptions, String geographicalValueUuid) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(geographicalValueUuid, ServiceExceptionParameters.GEOGRAPHICAL_VALUE_UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateGeographicalValue(List<MetamacExceptionItem> exceptions, GeographicalValue geographicalValue) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkGeographicalValue(geographicalValue, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    private static void checkGeographicalValue(GeographicalValue geographicalValue, List<MetamacExceptionItem> exceptions) {
        IndicatorsValidationUtils.checkParameterRequired(geographicalValue, ServiceExceptionParameters.GEOGRAPHICAL_VALUE, exceptions);
        if (geographicalValue == null) {
            return;
        }
        IndicatorsValidationUtils.checkMetadataRequired(geographicalValue.getCode(), ServiceExceptionParameters.GEOGRAPHICAL_VALUE_CODE, exceptions);
        IndicatorsValidationUtils.checkMetadataRequired(geographicalValue.getGranularity(), ServiceExceptionParameters.GEOGRAPHICAL_VALUE_GRANULARITY, exceptions);
        IndicatorsValidationUtils.checkMetadataRequired(geographicalValue.getOrder(), ServiceExceptionParameters.GEOGRAPHICAL_VALUE_ORDER, exceptions);
    }

    // --------------------------------------------------------------------------------------------
    // GEOGRAPHICAL GRANULARITY
    // --------------------------------------------------------------------------------------------

    public static void checkFindGeographicalGranularities(List<MetamacExceptionItem> exceptions, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // Note: properties names of criteria restrictions are checked in MetamacCriteria2SculptorCriteriaMapper

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveGeographicalGranularity(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveGeographicalGranularityByCode(String code, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(code, ServiceExceptionParameters.CODE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveGeographicalGranularities(List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // nothing

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkCreateGeographicalGranularity(List<MetamacExceptionItem> exceptions, GeographicalGranularity geographicalGranularity) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkGeographicalGranularity(geographicalGranularity, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkDeleteGeographicalGranularity(List<MetamacExceptionItem> exceptions, String geographicalGranularityUuid) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(geographicalGranularityUuid, ServiceExceptionParameters.GEOGRAPHICAL_GRANULARITY_UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateGeographicalGranularity(List<MetamacExceptionItem> exceptions, GeographicalGranularity geographicalGranularity) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkGeographicalGranularity(geographicalGranularity, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    private static void checkGeographicalGranularity(GeographicalGranularity geographicalGranularity, List<MetamacExceptionItem> exceptions) {
        IndicatorsValidationUtils.checkParameterRequired(geographicalGranularity, ServiceExceptionParameters.GEOGRAPHICAL_GRANULARITY, exceptions);
        if (geographicalGranularity == null) {
            return;
        }
        IndicatorsValidationUtils.checkMetadataRequired(geographicalGranularity.getCode(), ServiceExceptionParameters.GEOGRAPHICAL_GRANULARITY_CODE, exceptions);
    }

    // --------------------------------------------------------------------------------------------
    // UNIT MULTIPLIERS
    // --------------------------------------------------------------------------------------------

    public static void checkFindUnitMultipliers(List<MetamacExceptionItem> exceptions, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // Note: properties names of criteria restrictions are checked in MetamacCriteria2SculptorCriteriaMapper

        ExceptionUtils.throwIfException(exceptions);

    }

    public static void checkRetrieveUnitMultiplier(String unitMultiplierUuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(unitMultiplierUuid, ServiceExceptionParameters.UNIT_MULTIPLIER_UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveUnitMultiplier(Integer unitMultiplier, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(unitMultiplier, ServiceExceptionParameters.UNIT_MULTIPLIER, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveUnitsMultipliers(List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // nothing

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkCreateUnitMultiplier(List<MetamacExceptionItem> exceptions, UnitMultiplier unitMultiplier) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkUnitMultiplier(unitMultiplier, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkDeleteUnitMultiplier(List<MetamacExceptionItem> exceptions, String unitMultiplierUuid) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(unitMultiplierUuid, ServiceExceptionParameters.UNIT_MULTIPLIER_UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateUnitMultiplier(List<MetamacExceptionItem> exceptions, UnitMultiplier unitMultiplier) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkUnitMultiplier(unitMultiplier, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    private static void checkUnitMultiplier(UnitMultiplier unitMultiplier, List<MetamacExceptionItem> exceptions) {
        IndicatorsValidationUtils.checkParameterRequired(unitMultiplier, ServiceExceptionParameters.UNIT_MULTIPLIER, exceptions);
        if (unitMultiplier == null) {
            return;
        }
        IndicatorsValidationUtils.checkMetadataRequired(unitMultiplier.getUnitMultiplier(), ServiceExceptionParameters.UNIT_MULTIPLIER_VALUE, exceptions);
    }

    // --------------------------------------------------------------------------------------------
    // QUANTITY UNITS
    // --------------------------------------------------------------------------------------------

    public static void checkFindQuantityUnits(List<MetamacExceptionItem> exceptions, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // Note: properties names of criteria restrictions are checked in MetamacCriteria2SculptorCriteriaMapper

        ExceptionUtils.throwIfException(exceptions);

    }

    public static void checkRetrieveQuantityUnit(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveQuantityUnits(List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // nothing

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkCreateQuantityUnit(List<MetamacExceptionItem> exceptions, QuantityUnit quantityUnits) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkQuantityUnit(quantityUnits, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkDeleteQuantityUnit(List<MetamacExceptionItem> exceptions, String quantityUnitsUuid) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(quantityUnitsUuid, ServiceExceptionParameters.QUANTITY_UNIT_UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateQuantityUnit(List<MetamacExceptionItem> exceptions, QuantityUnit quantityUnits) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkQuantityUnit(quantityUnits, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    private static void checkQuantityUnit(QuantityUnit quantityUnits, List<MetamacExceptionItem> exceptions) {
        IndicatorsValidationUtils.checkParameterRequired(quantityUnits, ServiceExceptionParameters.QUANTITY_UNIT, exceptions);
        if (quantityUnits == null) {
            return;
        }
        IndicatorsValidationUtils.checkMetadataRequired(quantityUnits.getTitle(), ServiceExceptionParameters.QUANTITY_UNIT_TITLE, exceptions);
    }

    // --------------------------------------------------------------------------------------------
    // DATA DEFINITIONS
    // --------------------------------------------------------------------------------------------

    public static void checkRetrieveDataDefinitionsOperationsCodes(List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // nothing

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveDataDefinitions(List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // nothing

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveDataDefinition(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindDataDefinitionsByOperationCode(String operationCode, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(operationCode, ServiceExceptionParameters.CODE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    // --------------------------------------------------------------------------------------------
    // DATA STRUCTURE
    // --------------------------------------------------------------------------------------------

    public static void checkRetrieveDataStructure(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkPopulateIndicatorData(String indicatorUuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(indicatorUuid, ServiceExceptionParameters.INDICATOR_UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkPopulateIndicatorVersionData(String indicatorUuid, String indicatorVersionNumber, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(indicatorUuid, ServiceExceptionParameters.INDICATOR_UUID, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(indicatorVersionNumber, ServiceExceptionParameters.INDICATOR_VERSION_NUMBER, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateIndicatorsData(List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // nothing

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkDeleteIndicatorData(String indicatorUuid, String indicatorVersionNumber, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(indicatorUuid, ServiceExceptionParameters.INDICATOR_UUID, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(indicatorVersionNumber, ServiceExceptionParameters.INDICATOR_VERSION_NUMBER, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveGeographicalGranularitiesInIndicator(IndicatorVersion indicatorVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(indicatorVersion, ServiceExceptionParameters.INDICATOR, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveGeographicalGranularitiesInIndicatorPublished(String indicatorUuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(indicatorUuid, ServiceExceptionParameters.INDICATOR_UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveGeographicalGranularitiesInIndicatorsPublishedWithSubjectCode(String subjectCode, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(subjectCode, ServiceExceptionParameters.SUBJECT_CODE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveGeographicalGranularitiesInIndicatorInstance(String indicatorInstanceUuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(indicatorInstanceUuid, ServiceExceptionParameters.INDICATOR_INSTANCE_UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveGeographicalGranularitiesInIndicatorsInstanceInPublishedIndicatorsSystem(String systemCode, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(systemCode, ServiceExceptionParameters.INDICATORS_SYSTEM_CODE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveGeographicalValuesByGranularityInIndicator(String indicatorUuid, String indicatorVersionNumber, String granularityUuid, List<MetamacExceptionItem> exceptions)
            throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(indicatorUuid, ServiceExceptionParameters.INDICATOR_UUID, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(indicatorVersionNumber, ServiceExceptionParameters.INDICATOR_VERSION_NUMBER, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(granularityUuid, ServiceExceptionParameters.GEOGRAPHICAL_GRANULARITY_UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveGeographicalValuesByGranularityInIndicatorPublished(String indicatorUuid, String granularityUuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(indicatorUuid, ServiceExceptionParameters.INDICATOR_UUID, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(granularityUuid, ServiceExceptionParameters.GEOGRAPHICAL_GRANULARITY_UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveGeographicalValuesByGranularityInIndicatorPublishedWithSubjectCode(String subjectCode, String granularityUuid, List<MetamacExceptionItem> exceptions)
            throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(subjectCode, ServiceExceptionParameters.SUBJECT_CODE, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(granularityUuid, ServiceExceptionParameters.GEOGRAPHICAL_GRANULARITY_UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveGeographicalValuesInPublishedIndicatorsWithSubjectCode(String subjectCode, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(subjectCode, ServiceExceptionParameters.SUBJECT_CODE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveGeographicalValuesByGranularityInIndicatorInstance(String indicatorInstanceUuid, String granularityUuid, List<MetamacExceptionItem> exceptions)
            throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(indicatorInstanceUuid, ServiceExceptionParameters.INDICATOR_INSTANCE_UUID, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(granularityUuid, ServiceExceptionParameters.GEOGRAPHICAL_GRANULARITY_UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveGeographicalValuesByGranularityInIndicatorsInstancesInPublishedIndicatorsSystem(String systemCode, String granularityUuid, List<MetamacExceptionItem> exceptions)
            throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(systemCode, ServiceExceptionParameters.INDICATORS_SYSTEM_CODE, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(granularityUuid, ServiceExceptionParameters.GEOGRAPHICAL_GRANULARITY_UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveGeographicalValuesInIndicatorsInstancesInPublishedIndicatorsSystem(String systemCode, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(systemCode, ServiceExceptionParameters.INDICATORS_SYSTEM_CODE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveGeographicalValuesInIndicatorVersion(IndicatorVersion indicatorVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(indicatorVersion, ServiceExceptionParameters.INDICATOR, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveGeographicalValuesInIndicatorPublished(String indicatorUuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(indicatorUuid, ServiceExceptionParameters.INDICATOR_UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveGeographicalCodesInIndicatorVersion(IndicatorVersion indicatorVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(indicatorVersion, ServiceExceptionParameters.INDICATOR, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveGeographicalValuesInIndicatorInstance(String indicatorInstanceUuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(indicatorInstanceUuid, ServiceExceptionParameters.INDICATOR_INSTANCE_UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveGeographicalCodesInIndicatorInstance(String indicatorInstanceUuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(indicatorInstanceUuid, ServiceExceptionParameters.INDICATOR_INSTANCE_UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveTimeGranularitiesInIndicator(String indicatorUuid, String indicatorVersionNumber, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(indicatorUuid, ServiceExceptionParameters.INDICATOR_UUID, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(indicatorVersionNumber, ServiceExceptionParameters.INDICATOR_VERSION_NUMBER, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveTimeGranularitiesInIndicatorPublished(String indicatorUuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(indicatorUuid, ServiceExceptionParameters.INDICATOR_UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveTimeGranularitiesInIndicatorInstance(String indicatorInstanceUuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(indicatorInstanceUuid, ServiceExceptionParameters.INDICATOR_INSTANCE_UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveTimeValuesByGranularityInIndicator(String indicatorUuid, String indicatorVersionNumber, TimeGranularityEnum granularity, List<MetamacExceptionItem> exceptions)
            throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(indicatorUuid, ServiceExceptionParameters.INDICATOR_UUID, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(indicatorVersionNumber, ServiceExceptionParameters.INDICATOR_VERSION_NUMBER, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(granularity, ServiceExceptionParameters.TIME_GRANULARITY, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveTimeValuesByGranularityInIndicatorPublished(String indicatorUuid, TimeGranularityEnum granularity, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(indicatorUuid, ServiceExceptionParameters.INDICATOR_UUID, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(granularity, ServiceExceptionParameters.TIME_GRANULARITY, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveTimeValuesByGranularityInIndicatorInstance(String indicatorInstanceUuid, TimeGranularityEnum granularity, List<MetamacExceptionItem> exceptions)
            throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(indicatorInstanceUuid, ServiceExceptionParameters.INDICATOR_INSTANCE_UUID, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(granularity, ServiceExceptionParameters.TIME_GRANULARITY, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveTimeValuesInIndicator(IndicatorVersion indicatorVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(indicatorVersion, ServiceExceptionParameters.INDICATOR, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveTimeValuesInIndicatorPublished(String indicatorUuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(indicatorUuid, ServiceExceptionParameters.INDICATOR_UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveTimeValuesInIndicatorInstance(String indicatorInstanceUuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(indicatorInstanceUuid, ServiceExceptionParameters.INDICATOR_INSTANCE_UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveMeasureValuesInIndicator(IndicatorVersion indicatorVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(indicatorVersion, ServiceExceptionParameters.INDICATOR, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveMeasureValuesInIndicatorPublished(String indicatorUuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(indicatorUuid, ServiceExceptionParameters.INDICATOR_UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveMeasureValuesInIndicatorInstance(String indicatorInstanceUuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(indicatorInstanceUuid, ServiceExceptionParameters.INDICATOR_INSTANCE_UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindGeographicalValuesInIndicatorsVersionsWithSubject(String subjectCode, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(subjectCode, ServiceExceptionParameters.SUBJECT_CODE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindIndicatorsVersionsWithGeoCodeAndSubjectCodeOrderedByLastUpdate(String subjectCode, String geoCode, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(subjectCode, ServiceExceptionParameters.SUBJECT_CODE, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(geoCode, ServiceExceptionParameters.GEO_CODE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindIndicatorsVersionsPublishedWithSubjectCodeAndGeoCodeOrderedByLastUpdate(String subjectCode, String geoCode, List<MetamacExceptionItem> exceptions)
            throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(subjectCode, ServiceExceptionParameters.SUBJECT_CODE, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(geoCode, ServiceExceptionParameters.GEO_CODE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindLastValueNLastIndicatorsVersionsWithSubjectCodeAndGeoCodeOrderedByLastUpdate(String subjectCode, String geoCode, List<MeasureDimensionTypeEnum> measureValues,
            int numResults, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(subjectCode, ServiceExceptionParameters.SUBJECT_CODE, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(geoCode, ServiceExceptionParameters.GEO_CODE, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(measureValues, ServiceExceptionParameters.MEASURE_VALUES, exceptions);
        if (numResults <= 0) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.NUM_RESULTS));
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindLastValueForIndicatorsVersionsWithGeoCodeOrderedByLastUpdate(List<String> indicatorsCodes, String geoCode, List<MeasureDimensionTypeEnum> measureValues,
            List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(indicatorsCodes, ServiceExceptionParameters.INDICATOR_CODES, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(geoCode, ServiceExceptionParameters.GEO_CODE, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(measureValues, ServiceExceptionParameters.MEASURE_VALUES, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindLastValueIndicatorsVersionsWithSubjectCodeOrderedByLastUpdate(String subjectCode, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(subjectCode, ServiceExceptionParameters.SUBJECT_CODE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindGeographicalValuesInIndicatorsInstancesWithSubject(String subjectCode, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(subjectCode, ServiceExceptionParameters.SUBJECT_CODE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindIndicatorsInstancesWithGeoCodeAndSubjectCodeOrderedByLastUpdate(String subjectCode, String geoCode, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(subjectCode, ServiceExceptionParameters.SUBJECT_CODE, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(geoCode, ServiceExceptionParameters.GEO_CODE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindLastValueIndicatorsInstancesWithSubjectCodeOrderedByLastUpdate(String subjectCode, String geoCode, List<MeasureDimensionTypeEnum> measureValues,
            List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(subjectCode, ServiceExceptionParameters.SUBJECT_CODE, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(geoCode, ServiceExceptionParameters.GEO_CODE, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(measureValues, ServiceExceptionParameters.MEASURE_VALUES, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindIndicatorsInstancesInPublishedIndicatorsSystemWithGeoCodeOrderedByLastUpdate(String systemCode, String geoCode, List<MetamacExceptionItem> exceptions)
            throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(systemCode, ServiceExceptionParameters.INDICATORS_SYSTEM_CODE, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(geoCode, ServiceExceptionParameters.GEO_CODE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindLastValueNLastIndicatorsInstancesInIndicatorsSystemWithGeoCodeOrderedByLastUpdate(String systemCode, String geoCode, List<MeasureDimensionTypeEnum> measureValues,
            int n, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(systemCode, ServiceExceptionParameters.INDICATORS_SYSTEM_CODE, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(geoCode, ServiceExceptionParameters.GEO_CODE, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(measureValues, ServiceExceptionParameters.MEASURE_VALUES, exceptions);
        if (n <= 0) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.NUM_RESULTS));
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindLastValueNLastIndicatorsInstancesWithSubjectCodeAndGeoCodeOrderedByLastUpdate(String subjectCode, String geoCode, List<MeasureDimensionTypeEnum> measureValues, int n,
            List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(subjectCode, ServiceExceptionParameters.SUBJECT_CODE, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(geoCode, ServiceExceptionParameters.GEO_CODE, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(measureValues, ServiceExceptionParameters.MEASURE_VALUES, exceptions);
        if (n <= 0) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.NUM_RESULTS));
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindLastValueForIndicatorsInstancesWithGeoCodeOrderedByLastUpdate(String systemCode, List<String> indicatorInstancesCodes, String geoCode,
            List<MeasureDimensionTypeEnum> measures, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        IndicatorsValidationUtils.checkParameterRequired(indicatorInstancesCodes, ServiceExceptionParameters.SUBJECT_CODE, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(geoCode, ServiceExceptionParameters.GEO_CODE, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(measures, ServiceExceptionParameters.MEASURE_VALUES, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    private static void checkIndicatorsSystem(IndicatorsSystemVersion indicatorsSystemVersion, List<MetamacExceptionItem> exceptions) {
        IndicatorsValidationUtils.checkParameterRequired(indicatorsSystemVersion, ServiceExceptionParameters.INDICATORS_SYSTEM, exceptions);
        if (indicatorsSystemVersion == null) {
            return;
        }
        IndicatorsValidationUtils.checkParameterRequired(indicatorsSystemVersion.getIndicatorsSystem(), ServiceExceptionParameters.INDICATORS_SYSTEM, exceptions);
        IndicatorsValidationUtils.checkMetadataRequired(indicatorsSystemVersion.getIndicatorsSystem().getCode(), ServiceExceptionParameters.INDICATORS_SYSTEM_CODE, exceptions);
    }

    private static void checkDimension(Dimension dimension, List<MetamacExceptionItem> exceptions) {
        IndicatorsValidationUtils.checkParameterRequired(dimension, ServiceExceptionParameters.DIMENSION, exceptions);
        if (dimension == null) {
            return;
        }
        IndicatorsValidationUtils.checkMetadataRequired(dimension.getElementLevel(), ServiceExceptionParameters.DIMENSION, exceptions);
        IndicatorsValidationUtils.checkMetadataRequired(dimension.getTitle(), ServiceExceptionParameters.DIMENSION_TITLE, exceptions);
        IndicatorsValidationUtils.checkMetadataRequired(dimension.getElementLevel().getOrderInLevel(), ServiceExceptionParameters.DIMENSION_ORDER_IN_LEVEL, exceptions);
        if (dimension.getElementLevel().getOrderInLevel() != null && dimension.getElementLevel().getOrderInLevel() < 0) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.DIMENSION_ORDER_IN_LEVEL));
        }
    }

    private static void checkIndicatorInstance(IndicatorInstance indicatorInstance, List<MetamacExceptionItem> exceptions) {
        IndicatorsValidationUtils.checkParameterRequired(indicatorInstance, ServiceExceptionParameters.INDICATOR_INSTANCE, exceptions);
        if (indicatorInstance == null) {
            return;
        }
        IndicatorsValidationUtils.checkParameterRequired(indicatorInstance.getElementLevel(), ServiceExceptionParameters.INDICATOR_INSTANCE, exceptions);
        IndicatorsValidationUtils.checkParameterRequired(indicatorInstance.getCode(), ServiceExceptionParameters.INDICATOR_INSTANCE_CODE, exceptions);
        IndicatorsValidationUtils.checkMetadataRequired(indicatorInstance.getTitle(), ServiceExceptionParameters.INDICATOR_INSTANCE_TITLE, exceptions);
        IndicatorsValidationUtils.checkMetadataRequired(indicatorInstance.getIndicator(), ServiceExceptionParameters.INDICATOR_INSTANCE_INDICATOR_UUID, exceptions);
        if (IndicatorsValidationUtils.isEmpty(indicatorInstance.getTimeGranularity()) && IndicatorsValidationUtils.isEmpty(indicatorInstance.getTimeValues())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.INDICATOR_INSTANCE_TIME_GRANULARITY,
                    ServiceExceptionParameters.INDICATOR_INSTANCE_TIME_VALUES));
        }
        if (!IndicatorsValidationUtils.isEmpty(indicatorInstance.getTimeGranularity()) && !IndicatorsValidationUtils.isEmpty(indicatorInstance.getTimeValues())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_UNEXPECTED, ServiceExceptionParameters.INDICATOR_INSTANCE_TIME_VALUES));
        }
        if (!IndicatorsValidationUtils.isEmpty(indicatorInstance.getTimeValues())) {
            for (String timeValueStr : indicatorInstance.getTimeValuesAsList()) {
                if (!TimeVariableUtils.isTimeValue(timeValueStr)) {
                    exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.INDICATOR_INSTANCE_TIME_VALUES));
                }
            }
        }
        if (!IndicatorsValidationUtils.isEmpty(indicatorInstance.getGeographicalGranularity())
                && (indicatorInstance.getGeographicalValues() != null && indicatorInstance.getGeographicalValues().size() > 0)) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_UNEXPECTED, ServiceExceptionParameters.INDICATOR_INSTANCE_GEOGRAPHICAL_VALUES));
        }

        if (indicatorInstance.getGeographicalValues() != null && indicatorInstance.getGeographicalValues().size() > 0) {
            // check duplicated entries
        }

        IndicatorsValidationUtils.checkMetadataRequired(indicatorInstance.getElementLevel().getOrderInLevel(), ServiceExceptionParameters.INDICATOR_INSTANCE_ORDER_IN_LEVEL, exceptions);
        if (indicatorInstance.getElementLevel().getOrderInLevel() != null && indicatorInstance.getElementLevel().getOrderInLevel() < 0) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.INDICATOR_INSTANCE_ORDER_IN_LEVEL));
        }
    }

    private static void checkIndicator(IndicatorVersion indicatorVersion, List<MetamacExceptionItem> exceptions) {
        IndicatorsValidationUtils.checkParameterRequired(indicatorVersion, ServiceExceptionParameters.INDICATOR, exceptions);
        if (indicatorVersion == null) {
            return;
        }
        IndicatorsValidationUtils.checkParameterRequired(indicatorVersion.getIndicator(), ServiceExceptionParameters.INDICATOR, exceptions);
        IndicatorsValidationUtils.checkMetadataRequired(indicatorVersion.getIndicator().getCode(), ServiceExceptionParameters.INDICATOR_CODE, exceptions);
        IndicatorsValidationUtils.checkMetadataRequired(indicatorVersion.getIndicator().getViewCode(), ServiceExceptionParameters.INDICATOR_VIEW_CODE, exceptions);
        IndicatorsValidationUtils.checkMetadataRequired(indicatorVersion.getTitle(), ServiceExceptionParameters.INDICATOR_TITLE, exceptions);
        IndicatorsValidationUtils.checkMetadataRequired(indicatorVersion.getSubjectCode(), ServiceExceptionParameters.INDICATOR_SUBJECT_CODE, exceptions);
        IndicatorsValidationUtils.checkMetadataRequired(indicatorVersion.getSubjectTitle(), ServiceExceptionParameters.INDICATOR_SUBJECT_TITLE, exceptions);
        if (indicatorVersion.getIndicator().getCode() != null && !CoreCommonUtil.matchMetamacID(indicatorVersion.getIndicator().getCode())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.INDICATOR_CODE));
        }
        if (indicatorVersion.getIndicator().getViewCode() != null && !ValidationUtils.matchOracleObjectIdentifier(indicatorVersion.getIndicator().getViewCode())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.INDICATOR_VIEW_CODE));
        }

        // Quantity: do not validate required attributes of quantity, only when send to production validation
        checkQuantity(indicatorVersion.getQuantity(), ServiceExceptionParameters.INDICATOR, false, exceptions);
    }

    public static void checkQuantity(Quantity quantity, String parameterName, boolean checksRequired, List<MetamacExceptionItem> exceptions) {

        IndicatorsValidationUtils.checkMetadataRequired(quantity, parameterName, exceptions);
        if (IndicatorsValidationUtils.isEmpty(quantity)) {
            return;
        }

        // checks invalid
        if (!IndicatorsValidationUtils.isEmpty(quantity.getBaseTime()) && !TimeVariableUtils.isTimeValue(quantity.getBaseTime())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, parameterName + ServiceExceptionParametersInternal.QUANTITY_BASE_TIME));
        }
        if (!IndicatorsValidationUtils.isEmpty(quantity.getDecimalPlaces()) && quantity.getDecimalPlaces().intValue() > 10) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, parameterName + ServiceExceptionParametersInternal.QUANTITY_DECIMAL_PLACES));
        }

        // checks required
        if (checksRequired) {
            IndicatorsValidationUtils.checkMetadataRequired(quantity.getQuantityType(), parameterName + ServiceExceptionParametersInternal.QUANTITY_TYPE, exceptions);
            IndicatorsValidationUtils.checkMetadataRequired(quantity.getUnit(), parameterName + ServiceExceptionParametersInternal.QUANTITY_UNIT_UUID, exceptions);
            IndicatorsValidationUtils.checkMetadataRequired(quantity.getUnitMultiplier(), parameterName + ServiceExceptionParametersInternal.QUANTITY_UNIT_MULTIPLIER, exceptions);
            IndicatorsValidationUtils.checkMetadataRequired(quantity.getDecimalPlaces(), parameterName + ServiceExceptionParametersInternal.QUANTITY_DECIMAL_PLACES, exceptions);
            if (IndicatorUtils.isRatioOrExtension(quantity.getQuantityType())) {
                IndicatorsValidationUtils.checkMetadataRequired(quantity.getIsPercentage(), parameterName + ServiceExceptionParametersInternal.QUANTITY_IS_PERCENTAGE, exceptions);
            }
            if (IndicatorUtils.isIndexOrExtension(quantity.getQuantityType())) {
                IndicatorsValidationUtils.checkMetadataRequired(quantity.getBaseValue(), parameterName + ServiceExceptionParametersInternal.QUANTITY_BASE_VALUE, exceptions);
                if (IndicatorsValidationUtils.isEmpty(quantity.getBaseTime()) && IndicatorsValidationUtils.isEmpty(quantity.getBaseLocation())) {
                    exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, parameterName + ServiceExceptionParametersInternal.QUANTITY_BASE_TIME, parameterName
                            + ServiceExceptionParametersInternal.QUANTITY_BASE_LOCATION_UUID));
                }
            }
            if (IndicatorUtils.isChangeRateOrExtension(quantity.getQuantityType())) {
                IndicatorsValidationUtils.checkMetadataRequired(quantity.getBaseQuantity(), parameterName + ServiceExceptionParametersInternal.QUANTITY_BASE_QUANTITY_INDICATOR_UUID, exceptions);
            }
        }

        // Quantity: checks unexpected
        if (!IndicatorUtils.isMagnitudeOrExtension(quantity.getQuantityType())) {
            IndicatorsValidationUtils.checkMetadataEmpty(quantity.getMinimum(), parameterName + ServiceExceptionParametersInternal.QUANTITY_MINIMUM, exceptions);
            IndicatorsValidationUtils.checkMetadataEmpty(quantity.getMaximum(), parameterName + ServiceExceptionParametersInternal.QUANTITY_MAXIMUM, exceptions);
        }
        if (!IndicatorUtils.isFractionOrExtension(quantity.getQuantityType())) {
            IndicatorsValidationUtils.checkMetadataEmpty(quantity.getNumerator(), parameterName + ServiceExceptionParametersInternal.QUANTITY_NUMERATOR_INDICATOR_UUID, exceptions);
            IndicatorsValidationUtils.checkMetadataEmpty(quantity.getDenominator(), parameterName + ServiceExceptionParametersInternal.QUANTITY_DENOMINATOR_INDICATOR_UUID, exceptions);
        }
        if (!IndicatorUtils.isRatioOrExtension(quantity.getQuantityType())) {
            IndicatorsValidationUtils.checkMetadataEmpty(quantity.getIsPercentage(), parameterName + ServiceExceptionParametersInternal.QUANTITY_IS_PERCENTAGE, exceptions);
            IndicatorsValidationUtils.checkMetadataEmpty(quantity.getPercentageOf(), parameterName + ServiceExceptionParametersInternal.QUANTITY_PERCENTAGE_OF, exceptions);
        }
        if (!IndicatorUtils.isIndexOrExtension(quantity.getQuantityType())) {
            IndicatorsValidationUtils.checkMetadataEmpty(quantity.getBaseValue(), parameterName + ServiceExceptionParametersInternal.QUANTITY_BASE_VALUE, exceptions);
            IndicatorsValidationUtils.checkMetadataEmpty(quantity.getBaseTime(), parameterName + ServiceExceptionParametersInternal.QUANTITY_BASE_TIME, exceptions);
            IndicatorsValidationUtils.checkMetadataEmpty(quantity.getBaseLocation(), parameterName + ServiceExceptionParametersInternal.QUANTITY_BASE_LOCATION_UUID, exceptions);
        } else {
            // must be filled only one of followings
            if (!IndicatorsValidationUtils.isEmpty(quantity.getBaseTime())) {
                IndicatorsValidationUtils.checkMetadataEmpty(quantity.getBaseLocation(), parameterName + ServiceExceptionParametersInternal.QUANTITY_BASE_LOCATION_UUID, exceptions);
            }
            if (!IndicatorsValidationUtils.isEmpty(quantity.getBaseLocation())) {
                IndicatorsValidationUtils.checkMetadataEmpty(quantity.getBaseTime(), parameterName + ServiceExceptionParametersInternal.QUANTITY_BASE_TIME, exceptions);
            }
        }
        if (!IndicatorUtils.isChangeRateOrExtension(quantity.getQuantityType())) {
            IndicatorsValidationUtils.checkMetadataEmpty(quantity.getBaseQuantity(), parameterName + ServiceExceptionParametersInternal.QUANTITY_BASE_QUANTITY_INDICATOR_UUID, exceptions);
        }
    }

    // --------------------------------------------------------------------------------------------
    // OTHER PRIVATE METHODS
    // --------------------------------------------------------------------------------------------

    private static Boolean isRateDerivationMethodTypeLoad(RateDerivation rateDerivation) {
        return rateDerivation != null && RateDerivationMethodTypeEnum.LOAD.equals(rateDerivation.getMethodType());
    }

    private static Boolean isDatasourceAbsoluteMethodEqualsRateDerivationMethod(DataSource dataSource, RateDerivation rateDerivation) {
        if (rateDerivation == null) {
            return Boolean.FALSE;
        }
        return dataSource.getAbsoluteMethod().equals(rateDerivation.getMethod());
    }

    private static void checkDataSource(DataSource dataSource, List<MetamacExceptionItem> exceptions) {
        IndicatorsValidationUtils.checkParameterRequired(dataSource, ServiceExceptionParameters.DATA_SOURCE, exceptions);
        if (dataSource == null) {
            return;
        }
        IndicatorsValidationUtils.checkMetadataRequired(dataSource.getDataGpeUuid(), ServiceExceptionParameters.DATA_SOURCE_DATA_GPE_UUID, exceptions);
        IndicatorsValidationUtils.checkMetadataRequired(dataSource.getPxUri(), ServiceExceptionParameters.DATA_SOURCE_PX_URI, exceptions);
        IndicatorsValidationUtils.checkMetadataRequired(dataSource.getSourceSurveyCode(), ServiceExceptionParameters.DATA_SOURCE_SOURCE_SURVEY_CODE, exceptions);
        IndicatorsValidationUtils.checkMetadataRequired(dataSource.getSourceSurveyTitle(), ServiceExceptionParameters.DATA_SOURCE_SOURCE_SURVEY_TITLE, exceptions);
        IndicatorsValidationUtils.checkMetadataRequired(dataSource.getPublishers(), ServiceExceptionParameters.DATA_SOURCE_PUBLISHERS, exceptions);

        if (IndicatorsValidationUtils.isEmpty(dataSource.getAbsoluteMethod())) {
            // If absoluteMethod is null, any rate must be load
            if (!isRateDerivationMethodTypeLoad(dataSource.getAnnualPuntualRate()) && !isRateDerivationMethodTypeLoad(dataSource.getAnnualPercentageRate())
                    && !isRateDerivationMethodTypeLoad(dataSource.getInterperiodPuntualRate()) && !isRateDerivationMethodTypeLoad(dataSource.getInterperiodPercentageRate())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.DATA_SOURCE_ABSOLUTE_METHOD));
            }
        } else {
            // All method in rates must be differents
            if (isDatasourceAbsoluteMethodEqualsRateDerivationMethod(dataSource, dataSource.getAnnualPuntualRate())
                    || isDatasourceAbsoluteMethodEqualsRateDerivationMethod(dataSource, dataSource.getAnnualPercentageRate())
                    || isDatasourceAbsoluteMethodEqualsRateDerivationMethod(dataSource, dataSource.getInterperiodPuntualRate())
                    || isDatasourceAbsoluteMethodEqualsRateDerivationMethod(dataSource, dataSource.getInterperiodPercentageRate())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.DATA_SOURCE_ABSOLUTE_METHOD));
            }
        }
        // Rates
        checkRateDerivation(dataSource.getAnnualPuntualRate(), ServiceExceptionParameters.DATA_SOURCE_ANNUAL_PUNTUAL_RATE, Boolean.FALSE, exceptions);
        checkRateDerivation(dataSource.getAnnualPercentageRate(), ServiceExceptionParameters.DATA_SOURCE_ANNUAL_PERCENTAGE_RATE, Boolean.TRUE, exceptions);
        checkRateDerivation(dataSource.getInterperiodPuntualRate(), ServiceExceptionParameters.DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE, Boolean.FALSE, exceptions);
        checkRateDerivation(dataSource.getInterperiodPercentageRate(), ServiceExceptionParameters.DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE, Boolean.TRUE, exceptions);

        // Time
        if (IndicatorsValidationUtils.isEmpty(dataSource.getTimeVariable()) && IndicatorsValidationUtils.isEmpty(dataSource.getTimeValue())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.DATA_SOURCE_TIME_VARIABLE, ServiceExceptionParameters.DATA_SOURCE_TIME_VALUE));
        }
        if (!IndicatorsValidationUtils.isEmpty(dataSource.getTimeVariable())) {
            IndicatorsValidationUtils.checkMetadataEmpty(dataSource.getTimeValue(), ServiceExceptionParameters.DATA_SOURCE_TIME_VALUE, exceptions);
        }
        if (!IndicatorsValidationUtils.isEmpty(dataSource.getTimeValue()) && !TimeVariableUtils.isTimeValue(dataSource.getTimeValue())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.DATA_SOURCE_TIME_VALUE));
        }

        // Geographical
        if (IndicatorsValidationUtils.isEmpty(dataSource.getGeographicalVariable()) && IndicatorsValidationUtils.isEmpty(dataSource.getGeographicalValue())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.DATA_SOURCE_GEOGRAPHICAL_VARIABLE,
                    ServiceExceptionParameters.DATA_SOURCE_GEOGRAPHICAL_VALUE_UUID));
        }
        if (!IndicatorsValidationUtils.isEmpty(dataSource.getGeographicalVariable())) {
            IndicatorsValidationUtils.checkMetadataEmpty(dataSource.getGeographicalValue(), ServiceExceptionParameters.DATA_SOURCE_GEOGRAPHICAL_VALUE_UUID, exceptions);
        }

        // Other variables
        if (dataSource.getOtherVariables() != null) {
            for (DataSourceVariable dataSourceVariable : dataSource.getOtherVariables()) {
                IndicatorsValidationUtils.checkMetadataRequired(dataSourceVariable.getVariable(), ServiceExceptionParameters.DATA_SOURCE_OTHER_VARIABLE_VARIABLE, exceptions);
                IndicatorsValidationUtils.checkMetadataRequired(dataSourceVariable.getCategory(), ServiceExceptionParameters.DATA_SOURCE_OTHER_VARIABLE_CATEGORY, exceptions);
            }
        }
    }

    private static void checkRateDerivation(RateDerivation rateDerivation, String parameterName, Boolean isPercentage, List<MetamacExceptionItem> exceptions) {

        if (IndicatorsValidationUtils.isEmpty(rateDerivation)) {
            // it is optional
            return;
        }

        // checks required
        IndicatorsValidationUtils.checkMetadataRequired(rateDerivation.getMethodType(), parameterName + ServiceExceptionParametersInternal.RATE_METHOD_TYPE, exceptions);
        IndicatorsValidationUtils.checkMetadataRequired(rateDerivation.getMethod(), parameterName + ServiceExceptionParametersInternal.RATE_METHOD, exceptions);
        IndicatorsValidationUtils.checkMetadataRequired(rateDerivation.getRounding(), parameterName + ServiceExceptionParametersInternal.RATE_ROUNDING, exceptions);
        IndicatorsValidationUtils.checkMetadataRequired(rateDerivation.getQuantity(), parameterName + ServiceExceptionParametersInternal.QUANTITY, exceptions);
        if (!IndicatorsValidationUtils.isEmpty(rateDerivation.getQuantity())) {
            if (!IndicatorsValidationUtils.isEmpty(rateDerivation.getQuantity().getQuantityType())) {
                if (isPercentage && QuantityTypeEnum.CHANGE_RATE.equals(rateDerivation.getQuantity().getQuantityType())) {
                    // ok
                } else if (!isPercentage && QuantityTypeEnum.AMOUNT.equals(rateDerivation.getQuantity().getQuantityType())) {
                    // ok
                } else {
                    exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, parameterName + ServiceExceptionParametersInternal.QUANTITY_TYPE));
                }
            }
            checkQuantity(rateDerivation.getQuantity(), parameterName, true, exceptions);
        }
    }
}
