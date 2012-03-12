package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.exception.CommonServiceExceptionType;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils;

import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.Dimension;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.Quantity;
import es.gobcan.istac.indicators.core.domain.RateDerivation;
import es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.RateDerivationMethodTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.VersiontTypeEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.util.IndicatorUtils;

public class InvocationValidator {

    public static void checkCreateIndicatorsSystem(IndicatorsSystemVersion indicatorsSystemVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkIndicatorsSystem(indicatorsSystemVersion, exceptions);
        ValidationUtils.checkMetadataEmpty(indicatorsSystemVersion.getIndicatorsSystem().getId(), "INDICATORS_SYSTEM.UUID", exceptions); // uuid never is null: it is initialized when create object
        ValidationUtils.checkMetadataEmpty(indicatorsSystemVersion.getId(), "INDICATORS_SYSTEM.UUID", exceptions);
        ValidationUtils.checkMetadataEmpty(indicatorsSystemVersion.getVersionNumber(), "INDICATORS_SYSTEM.VERSION_NUMBER", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateIndicatorsSystem(IndicatorsSystemVersion indicatorsSystemVersion, List<MetamacExceptionItem> exceptions)
            throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkIndicatorsSystem(indicatorsSystemVersion, exceptions);
        ValidationUtils.checkMetadataRequired(indicatorsSystemVersion.getId(), "INDICATORS_SYSTEM.UUID", exceptions); // uuid never is null: it is initialized when create object
        ValidationUtils.checkMetadataRequired(indicatorsSystemVersion.getIndicatorsSystem().getId(), "INDICATORS_SYSTEM.UUID", exceptions);
        ValidationUtils.checkMetadataRequired(indicatorsSystemVersion.getVersionNumber(), "INDICATORS_SYSTEM.VERSION_NUMBER", exceptions);
        // unmodifiable metadatas are checked in Dto2DoMapper

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveIndicatorsSystem(String uuid, String version, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);
        // version is optional

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveIndicatorsSystemPublished(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveIndicatorsSystemByCode(String code, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(code, "CODE", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveIndicatorsSystemPublishedByCode(String code, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(code, "CODE", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }
    
    public static void checkRetrieveIndicatorsSystemStructure(String uuid, String version, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);
        // version is optional

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkDeleteIndicatorsSystem(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkSendIndicatorsSystemToProductionValidation(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkSendIndicatorsSystemToDiffusionValidation(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRejectIndicatorsSystemValidation(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkPublishIndicatorsSystem(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkArchiveIndicatorsSystem(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkVersioningIndicatorsSystem(String uuid, VersiontTypeEnum versionType, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);
        ValidationUtils.checkParameterRequired(versionType, "VERSION_TYPE", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindIndicatorsSystems(List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // nothing to validate

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindIndicatorsSystemsPublished(List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // nothing to validate

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkCreateDimension(String indicatorsSystemUuid, Dimension dimension, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(indicatorsSystemUuid, "INDICATORS_SYSTEM_UUID", exceptions);
        checkDimension(dimension, exceptions);
        ValidationUtils.checkMetadataEmpty(dimension.getId(), "DIMENSION.UUID", exceptions); // uuid never is null: it is initialized when create object

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveDimension(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkDeleteDimension(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindDimensions(String indicatorsSystemUuid, String indicatorsSystemVersionNumber, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(indicatorsSystemUuid, "INDICATORS_SYSTEM_UUID", exceptions);
        ValidationUtils.checkParameterRequired(indicatorsSystemVersionNumber, "INDICATORS_SYSTEM_VERSION_NUMBER", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateDimension(Dimension dimension, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkDimension(dimension, exceptions);
        ValidationUtils.checkMetadataRequired(dimension.getId(), "DIMENSION.UUID", exceptions); // uuid never is null: it is initialized when create object
        // unmodifiable metadatas are checked in Dto2DoMapper

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateDimensionLocation(String uuid, String parentUuid, Long orderInLevel, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);
        ValidationUtils.checkParameterRequired(orderInLevel, "ORDER_IN_LEVEL", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkCreateIndicator(IndicatorVersion indicatorVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkIndicator(indicatorVersion, exceptions);
        ValidationUtils.checkMetadataEmpty(indicatorVersion.getId(), "INDICATOR.UUID", exceptions);
        ValidationUtils.checkMetadataEmpty(indicatorVersion.getIndicator().getId(), "INDICATOR.UUID", exceptions);
        ValidationUtils.checkMetadataEmpty(indicatorVersion.getVersionNumber(), "INDICATOR.VERSION_NUMBER", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateIndicator(IndicatorVersion indicatorVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkIndicator(indicatorVersion, exceptions);
        ValidationUtils.checkMetadataRequired(indicatorVersion.getId(), "INDICATORS.UUID", exceptions); // uuid never is null: it is initialized when create object
        ValidationUtils.checkMetadataRequired(indicatorVersion.getIndicator().getId(), "INDICATORS.UUID", exceptions);
        ValidationUtils.checkMetadataRequired(indicatorVersion.getVersionNumber(), "INDICATOR.VERSION_NUMBER", exceptions);
        // unmodifiable metadatas are checked in Dto2DoMapper

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveIndicator(String uuid, String version, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);
        // version is optional

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveIndicatorPublished(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }
    
    public static void checkRetrieveIndicatorByCode(String code, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(code, "CODE", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveIndicatorPublishedByCode(String code, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(code, "CODE", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkDeleteIndicator(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkSendIndicatorToProductionValidation(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkSendIndicatorToDiffusionValidation(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRejectIndicatorValidation(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkPublishIndicator(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkArchiveIndicator(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkVersioningIndicator(String uuid, VersiontTypeEnum versionType, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);
        ValidationUtils.checkParameterRequired(versionType, "VERSION_TYPE", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindIndicators(List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // nothing to validate

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindIndicatorsPublished(List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // nothing to validate

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkCreateDataSource(String indicatorUuid, DataSource dataSource, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(indicatorUuid, "INDICATOR_UUID", exceptions);
        checkDataSource(dataSource, exceptions);
        ValidationUtils.checkMetadataEmpty(dataSource.getId(), "DATA_SOURCE.UUID", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveDataSource(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkDeleteDataSource(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindDataSources(String indicatorUuid, String indicatorVersionNumber, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(indicatorUuid, "INDICATOR_UUID", exceptions);
        ValidationUtils.checkParameterRequired(indicatorVersionNumber, "INDICATOR_VERSION_NUMBER", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateDataSource(DataSource dataSource, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkDataSource(dataSource, exceptions); 
        ValidationUtils.checkMetadataRequired(dataSource.getId(), "DATA_SOURCE.UUID", exceptions);  // uuid never is null: it is initialized when create object
        // unmodifiable metadatas are checked in Dto2DoMapper

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkCreateIndicatorInstance(String indicatorsSystemUuid, IndicatorInstance indicatorInstance, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(indicatorsSystemUuid, "INDICATORS_SYSTEM_UUID", exceptions);
        checkIndicatorInstance(indicatorInstance, exceptions);
        ValidationUtils.checkMetadataEmpty(indicatorInstance.getId(), "INDICATOR_INSTANCE.UUID", exceptions); // uuid never is null: it is initialized when create object

        ExceptionUtils.throwIfException(exceptions);
    }
    
    public static void checkRetrieveIndicatorInstance(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkDeleteIndicatorInstance(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindIndicatorsInstances(String indicatorsSystemUuid, String indicatorsSystemVersionNumber, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(indicatorsSystemUuid, "INDICATORS_SYSTEM_UUID", exceptions);
        ValidationUtils.checkParameterRequired(indicatorsSystemVersionNumber, "INDICATORS_SYSTEM_VERSION_NUMBER", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateIndicatorInstance(IndicatorInstance indicatorInstance, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkIndicatorInstance(indicatorInstance, exceptions);
        ValidationUtils.checkMetadataRequired(indicatorInstance.getUuid(), "INDICATOR_INSTANCE.UUID", exceptions);   // uuid never is null: it is initialized when create object
        // unmodifiable metadatas are checked in Dto2DoMapper

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateIndicatorInstanceLocation(String uuid, String parentUuid, Long orderInLevel, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);
        ValidationUtils.checkParameterRequired(orderInLevel, "ORDER_IN_LEVEL", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveQuantityUnit(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindQuantityUnits(List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // nothing 
        
        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveSubject(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindSubjects(List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // nothing 
        
        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveGeographicalValue(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindGeographicalValues(List<MetamacExceptionItem> exceptions, String geographicalGranularityUuid) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // nothing 
        
        ExceptionUtils.throwIfException(exceptions);
    }
    
    public static void checkRetrieveGeographicalGranularity(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindGeographicalGranularities(List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // nothing 
        
        ExceptionUtils.throwIfException(exceptions);
    }

    private static void checkIndicatorsSystem(IndicatorsSystemVersion indicatorsSystemVersion, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkParameterRequired(indicatorsSystemVersion, "INDICATORS_SYSTEM", exceptions);
        ValidationUtils.checkParameterRequired(indicatorsSystemVersion.getIndicatorsSystem(), "INDICATORS_SYSTEM", exceptions);
        ValidationUtils.checkMetadataRequired(indicatorsSystemVersion.getIndicatorsSystem().getCode(), "INDICATORS_SYSTEM.CODE", exceptions);
        ValidationUtils.checkMetadataRequired(indicatorsSystemVersion.getTitle(), "INDICATORS_SYSTEM.TITLE", exceptions);
    }

    private static void checkDimension(Dimension dimension, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkParameterRequired(dimension, "DIMENSION", exceptions);
        ValidationUtils.checkMetadataRequired(dimension.getElementLevel(), "DIMENSION", exceptions);
        ValidationUtils.checkMetadataRequired(dimension.getTitle(), "DIMENSION.TITLE", exceptions);
        ValidationUtils.checkMetadataRequired(dimension.getElementLevel().getOrderInLevel(), "DIMENSION.ORDER_IN_LEVEL", exceptions);
        if (dimension.getElementLevel().getOrderInLevel() != null && dimension.getElementLevel().getOrderInLevel() < 0) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, "DIMENSION.ORDER_IN_LEVEL"));
        }
    }

    private static void checkIndicatorInstance(IndicatorInstance indicatorInstance, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkParameterRequired(indicatorInstance, "INDICATOR_INSTANCE", exceptions);
        ValidationUtils.checkParameterRequired(indicatorInstance.getElementLevel(), "INDICATOR_INSTANCE", exceptions);
        ValidationUtils.checkMetadataRequired(indicatorInstance.getTitle(), "INDICATOR_INSTANCE.TITLE", exceptions);
        ValidationUtils.checkMetadataRequired(indicatorInstance.getIndicator(), "INDICATOR_INSTANCE.INDICATOR_UUID", exceptions);
        if (ValidationUtils.isEmpty(indicatorInstance.getTimeGranularity()) && ValidationUtils.isEmpty(indicatorInstance.getTimeValue())) {
            // TODO ¿cómo poner si es requerido uno de ellos?
            exceptions.add(new MetamacExceptionItem(CommonServiceExceptionType.METADATA_REQUIRED, "INDICATOR_INSTANCE.TIME_GRANULARITY", "INDICATOR_INSTANCE.TIME_VALUE"));
        }
        if (!ValidationUtils.isEmpty(indicatorInstance.getTimeGranularity()) && !ValidationUtils.isEmpty(indicatorInstance.getTimeValue())) {
            exceptions.add(new MetamacExceptionItem(CommonServiceExceptionType.METADATA_UNEXPECTED, "INDICATOR_INSTANCE.TIME_VALUE"));
        }
        if (!ValidationUtils.isEmpty(indicatorInstance.getTimeValue())) {
            if (!TimeVariableUtils.isTimeValue(indicatorInstance.getTimeValue())) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, "INDICATOR_INSTANCE.TIME_VALUE"));
            }
        }
        if (!ValidationUtils.isEmpty(indicatorInstance.getGeographicalGranularity()) && !ValidationUtils.isEmpty(indicatorInstance.getGeographicalValue())) {
            exceptions.add(new MetamacExceptionItem(CommonServiceExceptionType.METADATA_UNEXPECTED, "INDICATOR_INSTANCE.GEOGRAPHICAL_VALUE_UUID"));
        }
        ValidationUtils.checkMetadataRequired(indicatorInstance.getElementLevel().getOrderInLevel(), "INDICATOR_INSTANCE.ORDER_IN_LEVEL", exceptions);
        if (indicatorInstance.getElementLevel().getOrderInLevel() != null && indicatorInstance.getElementLevel().getOrderInLevel() < 0) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, "INDICATOR_INSTANCE.ORDER_IN_LEVEL"));
        }
    }

    private static void checkIndicator(IndicatorVersion indicatorVersion, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkParameterRequired(indicatorVersion, "INDICATOR", exceptions);
        ValidationUtils.checkParameterRequired(indicatorVersion.getIndicator(), "INDICATOR", exceptions);
        ValidationUtils.checkMetadataRequired(indicatorVersion.getIndicator().getCode(), "INDICATOR.CODE", exceptions);
        ValidationUtils.checkMetadataRequired(indicatorVersion.getTitle(), "INDICATOR.TITLE", exceptions);
        ValidationUtils.checkMetadataRequired(indicatorVersion.getSubjectCode(), "INDICATOR.SUBJECT_CODE", exceptions);
        ValidationUtils.checkMetadataRequired(indicatorVersion.getSubjectTitle(), "INDICATOR.SUBJECT_TITLE", exceptions);
        checkQuantity(indicatorVersion.getQuantity(), "INDICATOR.QUANTITY", exceptions);
    }

    private static void checkQuantity(Quantity quantity, String parameterName, List<MetamacExceptionItem> exceptions) {
        
        ValidationUtils.checkMetadataRequired(quantity, parameterName, exceptions);
        if (ValidationUtils.isEmpty(quantity)) {
            return;
        }
        
        // checks required
        ValidationUtils.checkMetadataRequired(quantity.getQuantityType(), parameterName + ".TYPE", exceptions);
        ValidationUtils.checkMetadataRequired(quantity.getUnit(), parameterName + ".UNIT_UUID", exceptions);
        ValidationUtils.checkMetadataRequired(quantity.getUnitMultiplier(), parameterName + ".UNIT_MULTIPLIER", exceptions);
        if (IndicatorUtils.isRatioOrExtension(quantity.getQuantityType())) {
            ValidationUtils.checkMetadataRequired(quantity.getIsPercentage(), parameterName + ".IS_PERCENTAGE", exceptions);
        }
        if (IndicatorUtils.isIndexOrExtension(quantity.getQuantityType())) {
            if (ValidationUtils.isEmpty(quantity.getBaseValue()) && ValidationUtils.isEmpty(quantity.getBaseTime()) && ValidationUtils.isEmpty(quantity.getBaseLocation())) {
                exceptions.add(new MetamacExceptionItem(CommonServiceExceptionType.METADATA_REQUIRED, parameterName + ".BASE_VALUE", parameterName + ".BASE_TIME", parameterName + ".BASE_LOCATION_UUID"));
            }
            // must be filled only one of followings
            if (!ValidationUtils.isEmpty(quantity.getBaseValue())) {
                ValidationUtils.checkMetadataEmpty(quantity.getBaseTime(), parameterName + ".BASE_TIME", exceptions);
                ValidationUtils.checkMetadataEmpty(quantity.getBaseLocation(), parameterName + ".BASE_LOCATION_UUID", exceptions);
            } else if (!ValidationUtils.isEmpty(quantity.getBaseTime())) {
                ValidationUtils.checkMetadataEmpty(quantity.getBaseLocation(), parameterName + ".BASE_LOCATION_UUID", exceptions);
            }
        }
        if (IndicatorUtils.isChangeRateOrExtension(quantity.getQuantityType())) {
            ValidationUtils.checkMetadataRequired(quantity.getBaseQuantity(), parameterName + ".BASE_QUANTITY_INDICATOR_UUID", exceptions);
        }
        
        // Quantity: checks unexpected
        if (!IndicatorUtils.isMagnitudeOrExtension(quantity.getQuantityType())) {
            ValidationUtils.checkMetadataEmpty(quantity.getMinimum(), parameterName + ".MINIMUM", exceptions);
            ValidationUtils.checkMetadataEmpty(quantity.getMaximum(), parameterName + ".MAXIMUM", exceptions);
        }
        if (!IndicatorUtils.isFractionOrExtension(quantity.getQuantityType())) {
            ValidationUtils.checkMetadataEmpty(quantity.getNumerator(), parameterName + ".NUMERATOR_INDICATOR_UUID", exceptions);
            ValidationUtils.checkMetadataEmpty(quantity.getDenominator(), parameterName + ".DENOMINATOR_INDICATOR_UUID", exceptions);
        }
        if (!IndicatorUtils.isRatioOrExtension(quantity.getQuantityType())) {
            ValidationUtils.checkMetadataEmpty(quantity.getIsPercentage(), parameterName + ".IS_PERCENTAGE", exceptions);
            ValidationUtils.checkMetadataEmpty(quantity.getPercentageOf(), parameterName + ".PERCENTAGE_OF", exceptions);
        }
        if (!IndicatorUtils.isIndexOrExtension(quantity.getQuantityType())) {
            ValidationUtils.checkMetadataEmpty(quantity.getBaseValue(), parameterName + ".BASE_VALUE", exceptions);
            ValidationUtils.checkMetadataEmpty(quantity.getBaseTime(), parameterName + ".BASE_TIME", exceptions);
            ValidationUtils.checkMetadataEmpty(quantity.getBaseLocation(), parameterName + ".BASE_LOCATION_UUID", exceptions);
        }
        if (!IndicatorUtils.isChangeRateOrExtension(quantity.getQuantityType())) {
            ValidationUtils.checkMetadataEmpty(quantity.getBaseQuantity(), parameterName + ".BASE_QUANTITY_INDICATOR_UUID", exceptions);
        }
    }

    private static void checkDataSource(DataSource dataSource, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkParameterRequired(dataSource, "DATA_SOURCE", exceptions);
        ValidationUtils.checkMetadataRequired(dataSource.getQueryGpe(), "DATA_SOURCE.QUERY_GPE", exceptions);
        ValidationUtils.checkMetadataRequired(dataSource.getPx(), "DATA_SOURCE.PX", exceptions);
        ValidationUtils.checkMetadataRequired(dataSource.getTimeVariable(), "DATA_SOURCE.TIME_VARIABLE", exceptions);
        ValidationUtils.checkMetadataRequired(dataSource.getGeographicalVariable(), "DATA_SOURCE.GEOGRAPHICAL_VARIABLE", exceptions);
        checkRateDerivation(dataSource.getInterperiodRate(), "DATA_SOURCE.INTERPERIOD_RATE", exceptions);
        checkRateDerivation(dataSource.getAnnualRate(), "DATA_SOURCE.ANNUAL_RATE", exceptions);
    }
    
    private static void checkRateDerivation(RateDerivation rateDerivation, String parameterName, List<MetamacExceptionItem> exceptions) {
        
        ValidationUtils.checkMetadataRequired(rateDerivation, parameterName, exceptions);
        if (ValidationUtils.isEmpty(rateDerivation)) {
            return;
        }
        
        // checks required
        ValidationUtils.checkMetadataRequired(rateDerivation.getMethodType(), parameterName + ".METHOD_TYPE", exceptions);
        ValidationUtils.checkMetadataRequired(rateDerivation.getMethod(), parameterName + ".METHOD", exceptions);
        if (RateDerivationMethodTypeEnum.CALCULATE.equals(rateDerivation.getMethodType())) {
            ValidationUtils.checkMetadataRequired(rateDerivation.getRounding(), parameterName + ".ROUNDING", exceptions);
        }
        ValidationUtils.checkMetadataRequired(rateDerivation.getQuantity(), parameterName + ".QUANTITY", exceptions);
        if (!ValidationUtils.isEmpty(rateDerivation.getQuantity())) {
            if (!ValidationUtils.isEmpty(rateDerivation.getQuantity().getQuantityType())) {
                if (!QuantityTypeEnum.AMOUNT.equals(rateDerivation.getQuantity().getQuantityType()) && !QuantityTypeEnum.CHANGE_RATE.equals(rateDerivation.getQuantity().getQuantityType())) {
                    exceptions.add(new MetamacExceptionItem(CommonServiceExceptionType.METADATA_INCORRECT, parameterName + ".QUANTITY.TYPE"));            
                }
            }
            checkQuantity(rateDerivation.getQuantity(), parameterName + ".QUANTITY", exceptions);
            if (RateDerivationMethodTypeEnum.CALCULATE.equals(rateDerivation.getMethodType())) {
                ValidationUtils.checkMetadataRequired(rateDerivation.getQuantity().getDecimalPlaces(), parameterName + ".QUANTITY.DECIMAL_PLACES", exceptions);
                if (QuantityTypeEnum.CHANGE_RATE.equals(rateDerivation.getQuantity().getQuantityType())) {
                    ValidationUtils.checkMetadataRequired(rateDerivation.getQuantity().getBaseQuantity(), parameterName + ".QUANTITY.BASE_QUANTITY", exceptions);
                }
            }
        }
    }
}
