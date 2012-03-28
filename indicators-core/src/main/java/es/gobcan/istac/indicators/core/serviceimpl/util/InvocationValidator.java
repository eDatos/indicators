package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils;
import org.siemac.metamac.core.common.util.CoreCommonUtil;

import es.gobcan.istac.indicators.core.criteria.GeographicalValueCriteriaPropertyEnum;
import es.gobcan.istac.indicators.core.criteria.IndicatorCriteriaPropertyEnum;
import es.gobcan.istac.indicators.core.criteria.IndicatorsCriteria;
import es.gobcan.istac.indicators.core.criteria.IndicatorsCriteriaPropertyRestriction;
import es.gobcan.istac.indicators.core.criteria.IndicatorsSystemCriteriaPropertyEnum;
import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.Dimension;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.Quantity;
import es.gobcan.istac.indicators.core.domain.RateDerivation;
import es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.RateDerivationMethodTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.VersionTypeEnum;
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

    public static void checkUpdateIndicatorsSystem(IndicatorsSystemVersion indicatorsSystemVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
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

    public static void checkVersioningIndicatorsSystem(String uuid, VersionTypeEnum versionType, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);
        ValidationUtils.checkParameterRequired(versionType, "VERSION_TYPE", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindIndicatorsSystems(IndicatorsCriteria criteria, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // Checks properties names of criteria restrictions
        if (criteria != null && criteria.getConjunctionRestriction() != null) {
            for (IndicatorsCriteriaPropertyRestriction propertyRestriction : criteria.getConjunctionRestriction().getRestrictions()) {
                try {
                    IndicatorsSystemCriteriaPropertyEnum.valueOf(propertyRestriction.getPropertyName());
                } catch (IllegalArgumentException e) {
                    exceptions.add(new MetamacExceptionItem(ServiceExceptionType.PARAMETER_INCORRECT, "CRITERIA"));
                }
            }
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindIndicatorsSystemsPublished(IndicatorsCriteria criteria, List<MetamacExceptionItem> exceptions) throws MetamacException {
        checkFindIndicatorsSystems(criteria, exceptions);
    }
    
    public static void checkRetrieveIndicatorsSystemPublishedForIndicator(String indicatorUuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(indicatorUuid, "INDICATOR_UUID", exceptions);

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

    public static void checkRetrieveDimensionsByIndicatorsSystem(String indicatorsSystemUuid, String indicatorsSystemVersionNumber, List<MetamacExceptionItem> exceptions) throws MetamacException {
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

    public static void checkVersioningIndicator(String uuid, VersionTypeEnum versionType, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);
        ValidationUtils.checkParameterRequired(versionType, "VERSION_TYPE", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindIndicators(IndicatorsCriteria criteria, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // Checks properties names of criteria restrictions
        if (criteria != null && criteria.getConjunctionRestriction() != null) {
            for (IndicatorsCriteriaPropertyRestriction propertyRestriction : criteria.getConjunctionRestriction().getRestrictions()) {
                try {
                    IndicatorCriteriaPropertyEnum.valueOf(propertyRestriction.getPropertyName());
                } catch (IllegalArgumentException e) {
                    exceptions.add(new MetamacExceptionItem(ServiceExceptionType.PARAMETER_INCORRECT, "CRITERIA"));
                }
            }
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindIndicatorsPublished(IndicatorsCriteria criteria, List<MetamacExceptionItem> exceptions) throws MetamacException {
        checkFindIndicators(criteria, exceptions);
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

    public static void checkRetrieveDataSourcesByIndicator(String indicatorUuid, String indicatorVersionNumber, List<MetamacExceptionItem> exceptions) throws MetamacException {
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
        ValidationUtils.checkMetadataRequired(dataSource.getId(), "DATA_SOURCE.UUID", exceptions); // uuid never is null: it is initialized when create object
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

    public static void checkRetrieveIndicatorsInstancesByIndicatorsSystem(String indicatorsSystemUuid, String indicatorsSystemVersionNumber, List<MetamacExceptionItem> exceptions) throws MetamacException {
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
        ValidationUtils.checkMetadataRequired(indicatorInstance.getUuid(), "INDICATOR_INSTANCE.UUID", exceptions); // uuid never is null: it is initialized when create object
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

        ValidationUtils.checkParameterRequired(code, "CODE", exceptions);

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

    public static void checkRetrieveGeographicalValue(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkFindGeographicalValues(List<MetamacExceptionItem> exceptions, IndicatorsCriteria criteria) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        // Checks properties names of criteria restrictions
        if (criteria != null && criteria.getConjunctionRestriction() != null) {
            for (IndicatorsCriteriaPropertyRestriction propertyRestriction : criteria.getConjunctionRestriction().getRestrictions()) {
                try {
                    GeographicalValueCriteriaPropertyEnum.valueOf(propertyRestriction.getPropertyName());
                } catch (IllegalArgumentException e) {
                    exceptions.add(new MetamacExceptionItem(ServiceExceptionType.PARAMETER_INCORRECT, "CRITERIA"));
                }
            }
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveGeographicalGranularity(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveGeographicalGranularities(List<MetamacExceptionItem> exceptions) throws MetamacException {
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
    
    public static void checkRetrieveDataDefinition(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException{
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }
    
    public static void checkRetrieveDataStructure(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }
    
    public static void checkPopulateIndicatorData(String indicatorUuid, String version, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(indicatorUuid, "INDICATOR_UUID", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }
    
    private static void checkIndicatorsSystem(IndicatorsSystemVersion indicatorsSystemVersion, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkParameterRequired(indicatorsSystemVersion, "INDICATORS_SYSTEM", exceptions);
        ValidationUtils.checkParameterRequired(indicatorsSystemVersion.getIndicatorsSystem(), "INDICATORS_SYSTEM", exceptions);
        ValidationUtils.checkMetadataRequired(indicatorsSystemVersion.getIndicatorsSystem().getCode(), "INDICATORS_SYSTEM.CODE", exceptions);
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
            // TODO ¿cómo poner la excepción si es requerido sólo uno de x atributos? 
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, "INDICATOR_INSTANCE.TIME_GRANULARITY", "INDICATOR_INSTANCE.TIME_VALUE"));
        }
        if (!ValidationUtils.isEmpty(indicatorInstance.getTimeGranularity()) && !ValidationUtils.isEmpty(indicatorInstance.getTimeValue())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_UNEXPECTED, "INDICATOR_INSTANCE.TIME_VALUE"));
        }
        if (!ValidationUtils.isEmpty(indicatorInstance.getTimeValue()) && !TimeVariableUtils.isTimeValue(indicatorInstance.getTimeValue())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, "INDICATOR_INSTANCE.TIME_VALUE"));
        }
        if (!ValidationUtils.isEmpty(indicatorInstance.getGeographicalGranularity()) && !ValidationUtils.isEmpty(indicatorInstance.getGeographicalValue())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_UNEXPECTED, "INDICATOR_INSTANCE.GEOGRAPHICAL_VALUE_UUID"));
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
        if (indicatorVersion.getIndicator().getCode() != null && !CoreCommonUtil.isSemanticIdentifier(indicatorVersion.getIndicator().getCode())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, "INDICATOR.CODE"));
        }

        // Quantity: do not validate required attributes of quantity, only when send to production validation
        checkQuantity(indicatorVersion.getQuantity(), "INDICATOR.QUANTITY", false, exceptions);
    }

    public static void checkQuantity(Quantity quantity, String parameterName, boolean checksRequired, List<MetamacExceptionItem> exceptions) {

        ValidationUtils.checkMetadataRequired(quantity, parameterName, exceptions);
        if (ValidationUtils.isEmpty(quantity)) {
            return;
        }

        // checks invalid
        if (!ValidationUtils.isEmpty(quantity.getBaseTime()) && !TimeVariableUtils.isTimeValue(quantity.getBaseTime())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, parameterName + ".BASE_TIME"));
        }

        // checks required
        if (checksRequired) {
            ValidationUtils.checkMetadataRequired(quantity.getQuantityType(), parameterName + ".TYPE", exceptions);
            ValidationUtils.checkMetadataRequired(quantity.getUnit(), parameterName + ".UNIT_UUID", exceptions);
            ValidationUtils.checkMetadataRequired(quantity.getUnitMultiplier(), parameterName + ".UNIT_MULTIPLIER", exceptions);
            if (IndicatorUtils.isRatioOrExtension(quantity.getQuantityType())) {
                ValidationUtils.checkMetadataRequired(quantity.getIsPercentage(), parameterName + ".IS_PERCENTAGE", exceptions);
            }
            if (IndicatorUtils.isIndexOrExtension(quantity.getQuantityType())) {
                if (ValidationUtils.isEmpty(quantity.getBaseValue()) && ValidationUtils.isEmpty(quantity.getBaseTime()) && ValidationUtils.isEmpty(quantity.getBaseLocation())) {
                    exceptions
                            .add(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, parameterName + ".BASE_VALUE", parameterName + ".BASE_TIME", parameterName + ".BASE_LOCATION_UUID"));
                }
            }
            if (IndicatorUtils.isChangeRateOrExtension(quantity.getQuantityType())) {
                ValidationUtils.checkMetadataRequired(quantity.getBaseQuantity(), parameterName + ".BASE_QUANTITY_INDICATOR_UUID", exceptions);
            }
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
        } else {
            // must be filled only one of followings
            if (!ValidationUtils.isEmpty(quantity.getBaseValue())) {
                ValidationUtils.checkMetadataEmpty(quantity.getBaseTime(), parameterName + ".BASE_TIME", exceptions);
                ValidationUtils.checkMetadataEmpty(quantity.getBaseLocation(), parameterName + ".BASE_LOCATION_UUID", exceptions);
            } else if (!ValidationUtils.isEmpty(quantity.getBaseTime())) {
                ValidationUtils.checkMetadataEmpty(quantity.getBaseLocation(), parameterName + ".BASE_LOCATION_UUID", exceptions);
            }
        }
        if (!IndicatorUtils.isChangeRateOrExtension(quantity.getQuantityType())) {
            ValidationUtils.checkMetadataEmpty(quantity.getBaseQuantity(), parameterName + ".BASE_QUANTITY_INDICATOR_UUID", exceptions);
        }
    }

    private static void checkDataSource(DataSource dataSource, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkParameterRequired(dataSource, "DATA_SOURCE", exceptions);
        ValidationUtils.checkMetadataRequired(dataSource.getDataGpeUuid(), "DATA_SOURCE.DATA_GPE_UUID", exceptions);
        ValidationUtils.checkMetadataRequired(dataSource.getPxUri(), "DATA_SOURCE.PX_URI", exceptions);
        ValidationUtils.checkMetadataRequired(dataSource.getSourceSurveyCode(), "DATA_SOURCE.SOURCE_SURVEY_CODE", exceptions);
        ValidationUtils.checkMetadataRequired(dataSource.getSourceSurveyTitle(), "DATA_SOURCE.SOURCE_SURVEY_TITLE", exceptions);
        ValidationUtils.checkMetadataRequired(dataSource.getPublishers(), "DATA_SOURCE.PUBLISHERS", exceptions);
        
        // Rates
        checkRateDerivation(dataSource.getAnnualPuntualRate(), "DATA_SOURCE.ANNUAL_PUNTUAL_RATE", Boolean.FALSE, exceptions);
        checkRateDerivation(dataSource.getAnnualPercentageRate(), "DATA_SOURCE.ANNUAL_PERCENTAGE_RATE", Boolean.TRUE, exceptions);
        checkRateDerivation(dataSource.getInterperiodPuntualRate(), "DATA_SOURCE.INTERPERIOD_PUNTUAL_RATE", Boolean.FALSE, exceptions);
        checkRateDerivation(dataSource.getInterperiodPercentageRate(), "DATA_SOURCE.INTERPERIOD_PERCENTAGE_RATE", Boolean.TRUE, exceptions);
        
        // Time
        if (ValidationUtils.isEmpty(dataSource.getTimeVariable()) && ValidationUtils.isEmpty(dataSource.getTimeValue())) {
            // TODO ¿cómo poner la excepción si es requerido sólo uno de x atributos? 
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, "DATA_SOURCE.TIME_VARIABLE", "DATA_SOURCE.TIME_VALUE"));
        }
        if (!ValidationUtils.isEmpty(dataSource.getTimeVariable())) {
            ValidationUtils.checkMetadataEmpty(dataSource.getTimeValue(), "DATA_SOURCE.TIME_VALUE", exceptions);
        }
        if (!ValidationUtils.isEmpty(dataSource.getTimeValue()) && !TimeVariableUtils.isTimeValue(dataSource.getTimeValue())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, "DATA_SOURCE.TIME_VALUE"));
        }
        
        // Geographical
        if (ValidationUtils.isEmpty(dataSource.getGeographicalVariable()) && ValidationUtils.isEmpty(dataSource.getGeographicalValue())) {
            // TODO ¿cómo poner la excepción si es requerido sólo uno de x atributos? 
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, "DATA_SOURCE.GEOGRAPHICAL_VARIABLE", "DATA_SOURCE.GEOGRAPHICAL_VALUE_UUID"));
        }
        if (!ValidationUtils.isEmpty(dataSource.getGeographicalVariable())) {
            ValidationUtils.checkMetadataEmpty(dataSource.getTimeValue(), "DATA_SOURCE.GEOGRAPHICAL_VALUE_UUID", exceptions);
        }
    }

    private static void checkRateDerivation(RateDerivation rateDerivation, String parameterName, Boolean isPercentage, List<MetamacExceptionItem> exceptions) {

        if (ValidationUtils.isEmpty(rateDerivation)) {
            // it is optional
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
                if (isPercentage && QuantityTypeEnum.CHANGE_RATE.equals(rateDerivation.getQuantity().getQuantityType())) {
                    // ok
                } else if (!isPercentage && QuantityTypeEnum.AMOUNT.equals(rateDerivation.getQuantity().getQuantityType())) {
                    // ok
                } else {
                    exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, parameterName + ".QUANTITY.TYPE"));
                }
            }
            checkQuantity(rateDerivation.getQuantity(), parameterName + ".QUANTITY", true, exceptions);
            if (RateDerivationMethodTypeEnum.CALCULATE.equals(rateDerivation.getMethodType()) && QuantityTypeEnum.CHANGE_RATE.equals(rateDerivation.getQuantity().getQuantityType())) {
                ValidationUtils.checkMetadataRequired(rateDerivation.getQuantity().getDecimalPlaces(), parameterName + ".QUANTITY.DECIMAL_PLACES", exceptions);
            }
        }
    }
}
