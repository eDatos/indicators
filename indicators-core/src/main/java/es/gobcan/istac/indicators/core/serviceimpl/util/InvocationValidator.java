package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils;
import org.siemac.metamac.core.common.util.CoreCommonUtil;

import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.DataSourceVariable;
import es.gobcan.istac.indicators.core.domain.Dimension;
import es.gobcan.istac.indicators.core.domain.Indicator;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.Quantity;
import es.gobcan.istac.indicators.core.domain.RateDerivation;
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

    public static void checkCreateIndicatorsSystem(IndicatorsSystemVersion indicatorsSystemVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkIndicatorsSystem(indicatorsSystemVersion, exceptions);
        // uuid never is null: it is initialized when create object
        ValidationUtils.checkMetadataEmpty(indicatorsSystemVersion.getIndicatorsSystem().getId(), ServiceExceptionParameters.INDICATORS_SYSTEM_UUID, exceptions);
        ValidationUtils.checkMetadataEmpty(indicatorsSystemVersion.getId(), ServiceExceptionParameters.INDICATORS_SYSTEM_UUID, exceptions);
        ValidationUtils.checkMetadataEmpty(indicatorsSystemVersion.getVersionNumber(), ServiceExceptionParameters.INDICATORS_SYSTEM_VERSION_NUMBER, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateIndicatorsSystem(IndicatorsSystemVersion indicatorsSystemVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkIndicatorsSystem(indicatorsSystemVersion, exceptions);
        // uuid never is null: it is initialized when create object
        ValidationUtils.checkMetadataRequired(indicatorsSystemVersion.getId(), ServiceExceptionParameters.INDICATORS_SYSTEM_UUID, exceptions);
        ValidationUtils.checkMetadataRequired(indicatorsSystemVersion.getIndicatorsSystem().getId(), ServiceExceptionParameters.INDICATORS_SYSTEM_UUID, exceptions);
        ValidationUtils.checkMetadataRequired(indicatorsSystemVersion.getVersionNumber(), ServiceExceptionParameters.INDICATORS_SYSTEM_VERSION_NUMBER, exceptions);
        // unmodifiable metadatas are checked in Dto2DoMapper

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveIndicatorsSystem(String uuid, String version, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);
        // version is optional

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveIndicatorsSystemPublished(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveIndicatorsSystemByCode(String code, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(code, ServiceExceptionParameters.CODE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveIndicatorsSystemPublishedByCode(String code, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(code, ServiceExceptionParameters.CODE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveIndicatorsSystemStructure(String uuid, String version, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);
        // version is optional

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkDeleteIndicatorsSystem(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkSendIndicatorsSystemToProductionValidation(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRejectIndicatorsSystemProductionValidation(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkSendIndicatorsSystemToDiffusionValidation(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRejectIndicatorsSystemDiffusionValidation(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkPublishIndicatorsSystem(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkArchiveIndicatorsSystem(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkVersioningIndicatorsSystem(String uuid, VersionTypeEnum versionType, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);
        ValidationUtils.checkParameterRequired(versionType, ServiceExceptionParameters.INDICATORS_SYSTEM_VERSION_TYPE, exceptions);

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

        ValidationUtils.checkParameterRequired(indicatorUuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkCreateDimension(String indicatorsSystemUuid, Dimension dimension, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(indicatorsSystemUuid, ServiceExceptionParameters.INDICATORS_SYSTEM_UUID, exceptions);
        checkDimension(dimension, exceptions);
        ValidationUtils.checkMetadataEmpty(dimension.getId(), ServiceExceptionParameters.DIMENSION_UUID, exceptions); // uuid never is null: it is initialized when create object

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveDimension(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkDeleteDimension(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveDimensionsByIndicatorsSystem(String indicatorsSystemUuid, String indicatorsSystemVersionNumber, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(indicatorsSystemUuid, ServiceExceptionParameters.INDICATORS_SYSTEM_UUID, exceptions);
        ValidationUtils.checkParameterRequired(indicatorsSystemVersionNumber, ServiceExceptionParameters.INDICATORS_SYSTEM_VERSION_NUMBER, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateDimension(Dimension dimension, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkDimension(dimension, exceptions);
        ValidationUtils.checkMetadataRequired(dimension.getId(), ServiceExceptionParameters.DIMENSION_UUID, exceptions); // uuid never is null: it is initialized when create object
        // unmodifiable metadatas are checked in Dto2DoMapper

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateDimensionLocation(String uuid, String parentUuid, Long orderInLevel, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);
        ValidationUtils.checkParameterRequired(orderInLevel, ServiceExceptionParameters.DIMENSION_ORDER_IN_LEVEL, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveIndicatorsSystemByDimension(ServiceContext ctx, String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkCreateIndicator(IndicatorVersion indicatorVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkIndicator(indicatorVersion, exceptions);
        ValidationUtils.checkMetadataEmpty(indicatorVersion.getId(), ServiceExceptionParameters.INDICATOR_UUID, exceptions);
        ValidationUtils.checkMetadataEmpty(indicatorVersion.getIndicator().getId(), ServiceExceptionParameters.INDICATOR_UUID, exceptions);
        ValidationUtils.checkMetadataEmpty(indicatorVersion.getVersionNumber(), ServiceExceptionParameters.INDICATOR_VERSION_NUMBER, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateIndicator(Indicator indicator, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkMetadataRequired(indicator.getId(), ServiceExceptionParameters.INDICATOR_UUID, exceptions); // uuid never is null: it is initialized when create object
        // unmodifiable metadatas are checked in Dto2DoMapper

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateIndicatorVersion(IndicatorVersion indicatorVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkIndicator(indicatorVersion, exceptions);
        ValidationUtils.checkMetadataRequired(indicatorVersion.getId(), ServiceExceptionParameters.INDICATOR_UUID, exceptions); // uuid never is null: it is initialized when create object
        ValidationUtils.checkMetadataRequired(indicatorVersion.getIndicator().getId(), ServiceExceptionParameters.INDICATOR_UUID, exceptions);
        ValidationUtils.checkMetadataRequired(indicatorVersion.getVersionNumber(), ServiceExceptionParameters.INDICATOR_VERSION_NUMBER, exceptions);
        // unmodifiable metadatas are checked in Dto2DoMapper

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveIndicator(String uuid, String version, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);
        // version is optional

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveIndicatorPublished(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveIndicatorByCode(String code, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(code, ServiceExceptionParameters.CODE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveIndicatorPublishedByCode(String code, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(code, ServiceExceptionParameters.CODE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkDeleteIndicator(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkSendIndicatorToProductionValidation(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRejectIndicatorProductionValidation(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkSendIndicatorToDiffusionValidation(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRejectIndicatorDiffusionValidation(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkPublishIndicator(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkArchiveIndicator(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkVersioningIndicator(String uuid, VersionTypeEnum versionType, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);
        ValidationUtils.checkParameterRequired(versionType, ServiceExceptionParameters.INDICATOR_VERSION_TYPE, exceptions);

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

    public static void checkCreateDataSource(String indicatorUuid, DataSource dataSource, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(indicatorUuid, ServiceExceptionParameters.INDICATOR_UUID, exceptions);
        checkDataSource(dataSource, exceptions);
        ValidationUtils.checkMetadataEmpty(dataSource.getId(), ServiceExceptionParameters.DATA_SOURCE_UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveDataSource(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkDeleteDataSource(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveDataSourcesByIndicator(String indicatorUuid, String indicatorVersionNumber, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(indicatorUuid, ServiceExceptionParameters.INDICATOR_UUID, exceptions);
        ValidationUtils.checkParameterRequired(indicatorVersionNumber, ServiceExceptionParameters.INDICATOR_VERSION_NUMBER, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateDataSource(DataSource dataSource, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkDataSource(dataSource, exceptions);
        ValidationUtils.checkMetadataRequired(dataSource.getId(), ServiceExceptionParameters.DATA_SOURCE_UUID, exceptions); // uuid never is null: it is initialized when create object
        // unmodifiable metadatas are checked in Dto2DoMapper

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkCreateIndicatorInstance(String indicatorsSystemUuid, IndicatorInstance indicatorInstance, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(indicatorsSystemUuid, ServiceExceptionParameters.INDICATORS_SYSTEM_UUID, exceptions);
        checkIndicatorInstance(indicatorInstance, exceptions);
        ValidationUtils.checkMetadataEmpty(indicatorInstance.getId(), ServiceExceptionParameters.INDICATOR_INSTANCE_UUID, exceptions); // uuid never is null: it is initialized when create object

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveIndicatorInstance(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveIndicatorInstancePublishedByCode(String code, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(code, ServiceExceptionParameters.CODE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkDeleteIndicatorInstance(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveIndicatorsInstancesByIndicatorsSystem(String indicatorsSystemUuid, String indicatorsSystemVersionNumber, List<MetamacExceptionItem> exceptions)
            throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(indicatorsSystemUuid, ServiceExceptionParameters.INDICATORS_SYSTEM_UUID, exceptions);
        ValidationUtils.checkParameterRequired(indicatorsSystemVersionNumber, ServiceExceptionParameters.INDICATORS_SYSTEM_VERSION_NUMBER, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateIndicatorInstance(IndicatorInstance indicatorInstance, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkIndicatorInstance(indicatorInstance, exceptions);
        ValidationUtils.checkMetadataRequired(indicatorInstance.getUuid(), ServiceExceptionParameters.INDICATOR_INSTANCE_UUID, exceptions);
        // unmodifiable metadatas are checked in Dto2DoMapper

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateIndicatorInstanceLocation(String uuid, String parentUuid, Long orderInLevel, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);
        ValidationUtils.checkParameterRequired(orderInLevel, ServiceExceptionParameters.INDICATOR_INSTANCE_ORDER_IN_LEVEL, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveIndicatorsSystemByIndicatorInstance(ServiceContext ctx, String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveQuantityUnit(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveQuantityUnits(List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // nothing

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveSubject(String code, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(code, ServiceExceptionParameters.CODE, exceptions);

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

    public static void checkRetrieveTimeValue(String timeValue, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(timeValue, ServiceExceptionParameters.TIME_VALUE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }
    
    public static void checkRetrieveTimeGranularity(TimeGranularityEnum timeGranularity, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(timeGranularity, ServiceExceptionParameters.TIME_GRANULARITY, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }
    
    public static void checkRetrieveMeasureValue(MeasureDimensionTypeEnum measureValue, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(measureValue, ServiceExceptionParameters.MEASURE_VALUE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }
    
    public static void checkRetrieveGeographicalValue(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }
    
    public static void checkRetrieveGeographicalValueByCode(String code, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(code, ServiceExceptionParameters.CODE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindGeographicalValues(List<MetamacExceptionItem> exceptions, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
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

        ValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }
    
    public static void checkRetrieveGeographicalGranularityByCode(String code, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(code, ServiceExceptionParameters.CODE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveGeographicalGranularities(List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // nothing

        ExceptionUtils.throwIfException(exceptions);
    }
    
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

        ValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }
    
    public static void checkFindDataDefinitionsByOperationCode(String operationCode, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        ValidationUtils.checkParameterRequired(operationCode, ServiceExceptionParameters.CODE, exceptions);
        
        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveDataStructure(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, ServiceExceptionParameters.UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkPopulateIndicatorData(String indicatorUuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(indicatorUuid, ServiceExceptionParameters.INDICATOR_UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }
    
    public static void checkPopulateIndicatorVersionData(String indicatorUuid, String indicatorVersionNumber, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        ValidationUtils.checkParameterRequired(indicatorUuid, ServiceExceptionParameters.INDICATOR_UUID, exceptions);
        ValidationUtils.checkParameterRequired(indicatorVersionNumber, ServiceExceptionParameters.INDICATOR_VERSION_NUMBER, exceptions);
        
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

        ValidationUtils.checkParameterRequired(indicatorUuid, ServiceExceptionParameters.INDICATOR_UUID, exceptions);
        ValidationUtils.checkParameterRequired(indicatorVersionNumber, ServiceExceptionParameters.INDICATOR_VERSION_NUMBER, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveGeographicalGranularitiesInIndicator(String indicatorUuid, String indicatorVersionNumber, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(indicatorUuid, ServiceExceptionParameters.INDICATOR_UUID, exceptions);
        ValidationUtils.checkParameterRequired(indicatorVersionNumber, ServiceExceptionParameters.INDICATOR_VERSION_NUMBER, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveGeographicalGranularitiesInIndicatorPublished(String indicatorUuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(indicatorUuid, ServiceExceptionParameters.INDICATOR_UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveGeographicalGranularitiesInIndicatorInstance(String indicatorInstanceUuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(indicatorInstanceUuid, ServiceExceptionParameters.INDICATOR_INSTANCE_UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveGeographicalValuesByGranularityInIndicator(String indicatorUuid, String indicatorVersionNumber, String granularityUuid, List<MetamacExceptionItem> exceptions)
            throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(indicatorUuid, ServiceExceptionParameters.INDICATOR_UUID, exceptions);
        ValidationUtils.checkParameterRequired(indicatorVersionNumber, ServiceExceptionParameters.INDICATOR_VERSION_NUMBER, exceptions);
        ValidationUtils.checkParameterRequired(granularityUuid, ServiceExceptionParameters.GEOGRAPHICAL_GRANULARITY_UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveGeographicalValuesByGranularityInIndicatorPublished(String indicatorUuid, String granularityUuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(indicatorUuid, ServiceExceptionParameters.INDICATOR_UUID, exceptions);
        ValidationUtils.checkParameterRequired(granularityUuid, ServiceExceptionParameters.GEOGRAPHICAL_GRANULARITY_UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveGeographicalValuesInIndicator(String indicatorUuid, String indicatorVersionNumber, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(indicatorUuid, ServiceExceptionParameters.INDICATOR_UUID, exceptions);
        ValidationUtils.checkParameterRequired(indicatorVersionNumber, ServiceExceptionParameters.INDICATOR_VERSION_NUMBER, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveGeographicalValuesInIndicatorPublished(String indicatorUuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(indicatorUuid, ServiceExceptionParameters.INDICATOR_UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveGeographicalValuesInIndicatorInstance(String indicatorInstanceUuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(indicatorInstanceUuid, ServiceExceptionParameters.INDICATOR_INSTANCE_UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveTimeGranularitiesInIndicator(String indicatorUuid, String indicatorVersionNumber, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(indicatorUuid, ServiceExceptionParameters.INDICATOR_UUID, exceptions);
        ValidationUtils.checkParameterRequired(indicatorVersionNumber, ServiceExceptionParameters.INDICATOR_VERSION_NUMBER, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveTimeGranularitiesInIndicatorPublished(String indicatorUuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(indicatorUuid, ServiceExceptionParameters.INDICATOR_UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveTimeGranularitiesInIndicatorInstance(String indicatorInstanceUuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(indicatorInstanceUuid, ServiceExceptionParameters.INDICATOR_INSTANCE_UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveTimeValuesByGranularityInIndicator(String indicatorUuid, String indicatorVersionNumber, TimeGranularityEnum granularity, List<MetamacExceptionItem> exceptions)
            throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(indicatorUuid, ServiceExceptionParameters.INDICATOR_UUID, exceptions);
        ValidationUtils.checkParameterRequired(indicatorVersionNumber, ServiceExceptionParameters.INDICATOR_VERSION_NUMBER, exceptions);
        ValidationUtils.checkParameterRequired(granularity, ServiceExceptionParameters.TIME_GRANULARITY, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveTimeValuesByGranularityInIndicatorPublished(String indicatorUuid, TimeGranularityEnum granularity, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(indicatorUuid, ServiceExceptionParameters.INDICATOR_UUID, exceptions);
        ValidationUtils.checkParameterRequired(granularity, ServiceExceptionParameters.TIME_GRANULARITY, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveTimeValuesInIndicator(String indicatorUuid, String indicatorVersionNumber, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(indicatorUuid, ServiceExceptionParameters.INDICATOR_UUID, exceptions);
        ValidationUtils.checkParameterRequired(indicatorVersionNumber, ServiceExceptionParameters.INDICATOR_VERSION_NUMBER, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveTimeValuesInIndicatorPublished(String indicatorUuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(indicatorUuid, ServiceExceptionParameters.INDICATOR_UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveTimeValuesInIndicatorInstance(String indicatorInstanceUuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(indicatorInstanceUuid, ServiceExceptionParameters.INDICATOR_INSTANCE_UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveMeasureValuesInIndicator(String indicatorUuid, String indicatorVersionNumber, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(indicatorUuid, ServiceExceptionParameters.INDICATOR_UUID, exceptions);
        ValidationUtils.checkParameterRequired(indicatorVersionNumber, ServiceExceptionParameters.INDICATOR_VERSION_NUMBER, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveMeasureValuesInIndicatorPublished(String indicatorUuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(indicatorUuid, ServiceExceptionParameters.INDICATOR_UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveMeasureValuesInIndicatorInstance(String indicatorInstanceUuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(indicatorInstanceUuid, ServiceExceptionParameters.INDICATOR_INSTANCE_UUID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }
    
    public static void checkRetrieveUnitMultiplier(Integer unitMultiplier, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(unitMultiplier, ServiceExceptionParameters.UNIT_MULTIPLIER, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveUnitsMultipliers(List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // nothing

        ExceptionUtils.throwIfException(exceptions);
    }

    private static void checkIndicatorsSystem(IndicatorsSystemVersion indicatorsSystemVersion, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkParameterRequired(indicatorsSystemVersion, ServiceExceptionParameters.INDICATORS_SYSTEM, exceptions);
        ValidationUtils.checkParameterRequired(indicatorsSystemVersion.getIndicatorsSystem(), ServiceExceptionParameters.INDICATORS_SYSTEM, exceptions);
        ValidationUtils.checkMetadataRequired(indicatorsSystemVersion.getIndicatorsSystem().getCode(), ServiceExceptionParameters.INDICATORS_SYSTEM_CODE, exceptions);
    }

    private static void checkDimension(Dimension dimension, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkParameterRequired(dimension, ServiceExceptionParameters.DIMENSION, exceptions);
        ValidationUtils.checkMetadataRequired(dimension.getElementLevel(), ServiceExceptionParameters.DIMENSION, exceptions);
        ValidationUtils.checkMetadataRequired(dimension.getTitle(), ServiceExceptionParameters.DIMENSION_TITLE, exceptions);
        ValidationUtils.checkMetadataRequired(dimension.getElementLevel().getOrderInLevel(), ServiceExceptionParameters.DIMENSION_ORDER_IN_LEVEL, exceptions);
        if (dimension.getElementLevel().getOrderInLevel() != null && dimension.getElementLevel().getOrderInLevel() < 0) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.DIMENSION_ORDER_IN_LEVEL));
        }
    }

    private static void checkIndicatorInstance(IndicatorInstance indicatorInstance, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkParameterRequired(indicatorInstance, ServiceExceptionParameters.INDICATOR_INSTANCE, exceptions);
        ValidationUtils.checkParameterRequired(indicatorInstance.getElementLevel(), ServiceExceptionParameters.INDICATOR_INSTANCE, exceptions);
        ValidationUtils.checkParameterRequired(indicatorInstance.getCode(), ServiceExceptionParameters.INDICATOR_INSTANCE_CODE, exceptions);
        ValidationUtils.checkMetadataRequired(indicatorInstance.getTitle(), ServiceExceptionParameters.INDICATOR_INSTANCE_TITLE, exceptions);
        ValidationUtils.checkMetadataRequired(indicatorInstance.getIndicator(), ServiceExceptionParameters.INDICATOR_INSTANCE_INDICATOR_UUID, exceptions);
        if (ValidationUtils.isEmpty(indicatorInstance.getTimeGranularity()) && ValidationUtils.isEmpty(indicatorInstance.getTimeValue())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.INDICATOR_INSTANCE_TIME_GRANULARITY,
                    ServiceExceptionParameters.INDICATOR_INSTANCE_TIME_VALUE));
        }
        if (!ValidationUtils.isEmpty(indicatorInstance.getTimeGranularity()) && !ValidationUtils.isEmpty(indicatorInstance.getTimeValue())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_UNEXPECTED, ServiceExceptionParameters.INDICATOR_INSTANCE_TIME_VALUE));
        }
        if (!ValidationUtils.isEmpty(indicatorInstance.getTimeValue()) && !TimeVariableUtils.isTimeValue(indicatorInstance.getTimeValue())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.INDICATOR_INSTANCE_TIME_VALUE));
        }
        if (!ValidationUtils.isEmpty(indicatorInstance.getGeographicalGranularity()) && !ValidationUtils.isEmpty(indicatorInstance.getGeographicalValue())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_UNEXPECTED, ServiceExceptionParameters.INDICATOR_INSTANCE_GEOGRAPHICAL_VALUE_UUID));
        }
        ValidationUtils.checkMetadataRequired(indicatorInstance.getElementLevel().getOrderInLevel(), ServiceExceptionParameters.INDICATOR_INSTANCE_ORDER_IN_LEVEL, exceptions);
        if (indicatorInstance.getElementLevel().getOrderInLevel() != null && indicatorInstance.getElementLevel().getOrderInLevel() < 0) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.INDICATOR_INSTANCE_ORDER_IN_LEVEL));
        }
    }

    private static void checkIndicator(IndicatorVersion indicatorVersion, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkParameterRequired(indicatorVersion, ServiceExceptionParameters.INDICATOR, exceptions);
        ValidationUtils.checkParameterRequired(indicatorVersion.getIndicator(), ServiceExceptionParameters.INDICATOR, exceptions);
        ValidationUtils.checkMetadataRequired(indicatorVersion.getIndicator().getCode(), ServiceExceptionParameters.INDICATOR_CODE, exceptions);
        ValidationUtils.checkMetadataRequired(indicatorVersion.getTitle(), ServiceExceptionParameters.INDICATOR_TITLE, exceptions);
        ValidationUtils.checkMetadataRequired(indicatorVersion.getSubjectCode(), ServiceExceptionParameters.INDICATOR_SUBJECT_CODE, exceptions);
        ValidationUtils.checkMetadataRequired(indicatorVersion.getSubjectTitle(), ServiceExceptionParameters.INDICATOR_SUBJECT_TITLE, exceptions);
        if (indicatorVersion.getIndicator().getCode() != null && !CoreCommonUtil.isSemanticIdentifier(indicatorVersion.getIndicator().getCode())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.INDICATOR_CODE));
        }

        // Quantity: do not validate required attributes of quantity, only when send to production validation
        checkQuantity(indicatorVersion.getQuantity(), ServiceExceptionParameters.INDICATOR, false, exceptions);
    }

    public static void checkQuantity(Quantity quantity, String parameterName, boolean checksRequired, List<MetamacExceptionItem> exceptions) {

        ValidationUtils.checkMetadataRequired(quantity, parameterName, exceptions);
        if (ValidationUtils.isEmpty(quantity)) {
            return;
        }

        // checks invalid
        if (!ValidationUtils.isEmpty(quantity.getBaseTime()) && !TimeVariableUtils.isTimeValue(quantity.getBaseTime())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, parameterName + ServiceExceptionParametersInternal.QUANTITY_BASE_TIME));
        }
        if (!ValidationUtils.isEmpty(quantity.getDecimalPlaces()) && quantity.getDecimalPlaces().intValue() > 10) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, parameterName + ServiceExceptionParametersInternal.QUANTITY_DECIMAL_PLACES));
        }

        // checks required
        if (checksRequired) {
            ValidationUtils.checkMetadataRequired(quantity.getQuantityType(), parameterName + ServiceExceptionParametersInternal.QUANTITY_TYPE, exceptions);
            ValidationUtils.checkMetadataRequired(quantity.getUnit(), parameterName + ServiceExceptionParametersInternal.QUANTITY_UNIT_UUID, exceptions);
            ValidationUtils.checkMetadataRequired(quantity.getUnitMultiplier(), parameterName + ServiceExceptionParametersInternal.QUANTITY_UNIT_MULTIPLIER, exceptions);
            if (IndicatorUtils.isRatioOrExtension(quantity.getQuantityType())) {
                ValidationUtils.checkMetadataRequired(quantity.getIsPercentage(), parameterName + ServiceExceptionParametersInternal.QUANTITY_IS_PERCENTAGE, exceptions);
            }
            if (IndicatorUtils.isIndexOrExtension(quantity.getQuantityType())) {
                if (ValidationUtils.isEmpty(quantity.getBaseValue()) && ValidationUtils.isEmpty(quantity.getBaseTime()) && ValidationUtils.isEmpty(quantity.getBaseLocation())) {
                    exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, parameterName + ServiceExceptionParametersInternal.QUANTITY_BASE_VALUE, parameterName
                            + ServiceExceptionParametersInternal.QUANTITY_BASE_TIME, parameterName + ServiceExceptionParametersInternal.QUANTITY_BASE_LOCATION_UUID));
                }
            }
            if (IndicatorUtils.isChangeRateOrExtension(quantity.getQuantityType())) {
                ValidationUtils.checkMetadataRequired(quantity.getBaseQuantity(), parameterName + ServiceExceptionParametersInternal.QUANTITY_BASE_QUANTITY_INDICATOR_UUID, exceptions);
            }
        }

        // Quantity: checks unexpected
        if (!IndicatorUtils.isMagnitudeOrExtension(quantity.getQuantityType())) {
            ValidationUtils.checkMetadataEmpty(quantity.getMinimum(), parameterName + ServiceExceptionParametersInternal.QUANTITY_MINIMUM, exceptions);
            ValidationUtils.checkMetadataEmpty(quantity.getMaximum(), parameterName + ServiceExceptionParametersInternal.QUANTITY_MAXIMUM, exceptions);
        }
        if (!IndicatorUtils.isFractionOrExtension(quantity.getQuantityType())) {
            ValidationUtils.checkMetadataEmpty(quantity.getNumerator(), parameterName + ServiceExceptionParametersInternal.QUANTITY_NUMERATOR_INDICATOR_UUID, exceptions);
            ValidationUtils.checkMetadataEmpty(quantity.getDenominator(), parameterName + ServiceExceptionParametersInternal.QUANTITY_DENOMINATOR_INDICATOR_UUID, exceptions);
        }
        if (!IndicatorUtils.isRatioOrExtension(quantity.getQuantityType())) {
            ValidationUtils.checkMetadataEmpty(quantity.getIsPercentage(), parameterName + ServiceExceptionParametersInternal.QUANTITY_IS_PERCENTAGE, exceptions);
            ValidationUtils.checkMetadataEmpty(quantity.getPercentageOf(), parameterName + ServiceExceptionParametersInternal.QUANTITY_PERCENTAGE_OF, exceptions);
        }
        if (!IndicatorUtils.isIndexOrExtension(quantity.getQuantityType())) {
            ValidationUtils.checkMetadataEmpty(quantity.getBaseValue(), parameterName + ServiceExceptionParametersInternal.QUANTITY_BASE_VALUE, exceptions);
            ValidationUtils.checkMetadataEmpty(quantity.getBaseTime(), parameterName + ServiceExceptionParametersInternal.QUANTITY_BASE_TIME, exceptions);
            ValidationUtils.checkMetadataEmpty(quantity.getBaseLocation(), parameterName + ServiceExceptionParametersInternal.QUANTITY_BASE_LOCATION_UUID, exceptions);
        } else {
            // must be filled only one of followings
            if (!ValidationUtils.isEmpty(quantity.getBaseValue())) {
                ValidationUtils.checkMetadataEmpty(quantity.getBaseTime(), parameterName + ServiceExceptionParametersInternal.QUANTITY_BASE_TIME, exceptions);
                ValidationUtils.checkMetadataEmpty(quantity.getBaseLocation(), parameterName + ServiceExceptionParametersInternal.QUANTITY_BASE_LOCATION_UUID, exceptions);
            } else if (!ValidationUtils.isEmpty(quantity.getBaseTime())) {
                ValidationUtils.checkMetadataEmpty(quantity.getBaseLocation(), parameterName + ServiceExceptionParametersInternal.QUANTITY_BASE_LOCATION_UUID, exceptions);
            }
        }
        if (!IndicatorUtils.isChangeRateOrExtension(quantity.getQuantityType())) {
            ValidationUtils.checkMetadataEmpty(quantity.getBaseQuantity(), parameterName + ServiceExceptionParametersInternal.QUANTITY_BASE_QUANTITY_INDICATOR_UUID, exceptions);
        }
    }

    private static Boolean isRateDerivationMethodTypeLoad(RateDerivation rateDerivation) {
        return rateDerivation != null && RateDerivationMethodTypeEnum.LOAD.equals(rateDerivation.getMethodType());
    }

    private static void checkDataSource(DataSource dataSource, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkParameterRequired(dataSource, ServiceExceptionParameters.DATA_SOURCE, exceptions);
        ValidationUtils.checkMetadataRequired(dataSource.getDataGpeUuid(), ServiceExceptionParameters.DATA_SOURCE_DATA_GPE_UUID, exceptions);
        ValidationUtils.checkMetadataRequired(dataSource.getPxUri(), ServiceExceptionParameters.DATA_SOURCE_PX_URI, exceptions);
        ValidationUtils.checkMetadataRequired(dataSource.getSourceSurveyCode(), ServiceExceptionParameters.DATA_SOURCE_SOURCE_SURVEY_CODE, exceptions);
        ValidationUtils.checkMetadataRequired(dataSource.getSourceSurveyTitle(), ServiceExceptionParameters.DATA_SOURCE_SOURCE_SURVEY_TITLE, exceptions);
        ValidationUtils.checkMetadataRequired(dataSource.getPublishers(), ServiceExceptionParameters.DATA_SOURCE_PUBLISHERS, exceptions);

        if (ValidationUtils.isEmpty(dataSource.getAbsoluteMethod())) {
            // If absoluteMethod is null, any rate must be load
            if (!isRateDerivationMethodTypeLoad(dataSource.getAnnualPuntualRate()) && !isRateDerivationMethodTypeLoad(dataSource.getAnnualPercentageRate())
                    && !isRateDerivationMethodTypeLoad(dataSource.getInterperiodPuntualRate()) && !isRateDerivationMethodTypeLoad(dataSource.getInterperiodPercentageRate())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.DATA_SOURCE_ABSOLUTE_METHOD));
            }
        }
        // Rates
        checkRateDerivation(dataSource.getAnnualPuntualRate(), ServiceExceptionParameters.DATA_SOURCE_ANNUAL_PUNTUAL_RATE, Boolean.FALSE, exceptions);
        checkRateDerivation(dataSource.getAnnualPercentageRate(), ServiceExceptionParameters.DATA_SOURCE_ANNUAL_PERCENTAGE_RATE, Boolean.TRUE, exceptions);
        checkRateDerivation(dataSource.getInterperiodPuntualRate(), ServiceExceptionParameters.DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE, Boolean.FALSE, exceptions);
        checkRateDerivation(dataSource.getInterperiodPercentageRate(), ServiceExceptionParameters.DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE, Boolean.TRUE, exceptions);

        // Time
        if (ValidationUtils.isEmpty(dataSource.getTimeVariable()) && ValidationUtils.isEmpty(dataSource.getTimeValue())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.DATA_SOURCE_TIME_VARIABLE, ServiceExceptionParameters.DATA_SOURCE_TIME_VALUE));
        }
        if (!ValidationUtils.isEmpty(dataSource.getTimeVariable())) {
            ValidationUtils.checkMetadataEmpty(dataSource.getTimeValue(), ServiceExceptionParameters.DATA_SOURCE_TIME_VALUE, exceptions);
        }
        if (!ValidationUtils.isEmpty(dataSource.getTimeValue()) && !TimeVariableUtils.isTimeValue(dataSource.getTimeValue())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.DATA_SOURCE_TIME_VALUE));
        }

        // Geographical
        if (ValidationUtils.isEmpty(dataSource.getGeographicalVariable()) && ValidationUtils.isEmpty(dataSource.getGeographicalValue())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.DATA_SOURCE_GEOGRAPHICAL_VARIABLE,
                    ServiceExceptionParameters.DATA_SOURCE_GEOGRAPHICAL_VALUE_UUID));
        }
        if (!ValidationUtils.isEmpty(dataSource.getGeographicalVariable())) {
            ValidationUtils.checkMetadataEmpty(dataSource.getTimeValue(), ServiceExceptionParameters.DATA_SOURCE_GEOGRAPHICAL_VALUE_UUID, exceptions);
        }

        // Other variables
        if (dataSource.getOtherVariables() != null) {
            for (DataSourceVariable dataSourceVariable : dataSource.getOtherVariables()) {
                ValidationUtils.checkMetadataRequired(dataSourceVariable.getVariable(), ServiceExceptionParameters.DATA_SOURCE_OTHER_VARIABLE_VARIABLE, exceptions);
                ValidationUtils.checkMetadataRequired(dataSourceVariable.getCategory(), ServiceExceptionParameters.DATA_SOURCE_OTHER_VARIABLE_CATEGORY, exceptions);
            }
        }
    }
    private static void checkRateDerivation(RateDerivation rateDerivation, String parameterName, Boolean isPercentage, List<MetamacExceptionItem> exceptions) {

        if (ValidationUtils.isEmpty(rateDerivation)) {
            // it is optional
            return;
        }

        // checks required
        ValidationUtils.checkMetadataRequired(rateDerivation.getMethodType(), parameterName + ServiceExceptionParametersInternal.RATE_METHOD_TYPE, exceptions);
        ValidationUtils.checkMetadataRequired(rateDerivation.getMethod(), parameterName + ServiceExceptionParametersInternal.RATE_METHOD, exceptions);
        if (RateDerivationMethodTypeEnum.CALCULATE.equals(rateDerivation.getMethodType())) {
            ValidationUtils.checkMetadataRequired(rateDerivation.getRounding(), parameterName + ServiceExceptionParametersInternal.RATE_ROUNDING, exceptions);
        }
        ValidationUtils.checkMetadataRequired(rateDerivation.getQuantity(), parameterName + ServiceExceptionParametersInternal.QUANTITY, exceptions);
        if (!ValidationUtils.isEmpty(rateDerivation.getQuantity())) {
            if (!ValidationUtils.isEmpty(rateDerivation.getQuantity().getQuantityType())) {
                if (isPercentage && QuantityTypeEnum.CHANGE_RATE.equals(rateDerivation.getQuantity().getQuantityType())) {
                    // ok
                } else if (!isPercentage && QuantityTypeEnum.AMOUNT.equals(rateDerivation.getQuantity().getQuantityType())) {
                    // ok
                } else {
                    exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, parameterName + ServiceExceptionParametersInternal.QUANTITY_TYPE));
                }
            }
            checkQuantity(rateDerivation.getQuantity(), parameterName, true, exceptions);
            if (RateDerivationMethodTypeEnum.CALCULATE.equals(rateDerivation.getMethodType()) && QuantityTypeEnum.CHANGE_RATE.equals(rateDerivation.getQuantity().getQuantityType())) {
                ValidationUtils.checkMetadataRequired(rateDerivation.getQuantity().getDecimalPlaces(), parameterName + ServiceExceptionParametersInternal.QUANTITY_DECIMAL_PLACES, exceptions);
            }
        }
    }
}
