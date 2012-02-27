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
import es.gobcan.istac.indicators.core.domain.Indicator;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystem;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.dto.serviceapi.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.enume.domain.VersiontTypeEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

public class InvocationValidator {

    public static void checkCreateIndicatorsSystem(IndicatorsSystem indicatorsSystem, IndicatorsSystemVersion indicatorsSystemVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkIndicatorsSystem(indicatorsSystem, indicatorsSystemVersion, exceptions);
        ValidationUtils.checkMetadataEmpty(indicatorsSystem.getId(), "INDICATORS_SYSTEM.UUID", exceptions); // uuid never is null: it is initialized when create object
        ValidationUtils.checkMetadataEmpty(indicatorsSystemVersion.getId(), "INDICATORS_SYSTEM.UUID", exceptions);
        ValidationUtils.checkMetadataEmpty(indicatorsSystemVersion.getVersionNumber(), "INDICATORS_SYSTEM.VERSION_NUMBER", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateIndicatorsSystem(IndicatorsSystemDto indicatorsSystemDto, IndicatorsSystemVersion indicatorsSystemInProduction, List<MetamacExceptionItem> exceptions)
            throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

//        checkIndicatorsSystem(indicatorsSystemDto, exceptions); // TODO
        ValidationUtils.checkMetadataRequired(indicatorsSystemDto.getUuid(), "INDICATORS_SYSTEM.UUID", exceptions); // TODO uuid never is null: it is initialized when create object
        ValidationUtils.checkMetadataRequired(indicatorsSystemDto.getVersionNumber(), "INDICATORS_SYSTEM.VERSION_NUMBER", exceptions);
        ValidationUtils.checkMetadataUnmodifiable(indicatorsSystemInProduction.getIndicatorsSystem().getCode(), indicatorsSystemDto.getCode(), "INDICATORS_SYSTEM.CODE", exceptions);

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

    public static void checkUpdateDimension(DimensionDto dimensionDto, Dimension dimension, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

//        checkDimension(dimensionDto, exceptions); // TODO update
        ValidationUtils.checkMetadataRequired(dimensionDto.getUuid(), "DIMENSION.UUID", exceptions); // TODO id required
        ValidationUtils.checkMetadataUnmodifiable(dimensionDto.getParentUuid(), dimension.getElementLevel().getParentUuid(), "DIMENSION.PARENT_UUID", exceptions);
        ValidationUtils.checkMetadataUnmodifiable(dimensionDto.getOrderInLevel(), dimension.getElementLevel().getOrderInLevel(), "DIMENSION.ORDER_IN_LEVEL", exceptions);

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

    public static void checkCreateIndicator(Indicator indicator, IndicatorVersion indicatorVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkIndicator(indicator, indicatorVersion, exceptions);
        ValidationUtils.checkMetadataEmpty(indicator.getId(), "INDICATOR.UUID", exceptions);
        ValidationUtils.checkMetadataEmpty(indicatorVersion.getId(), "INDICATOR.UUID", exceptions);
        ValidationUtils.checkMetadataEmpty(indicatorVersion.getVersionNumber(), "INDICATOR.VERSION_NUMBER", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateIndicator(IndicatorDto indicatorDto, IndicatorVersion indicatorInProduction, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

//        checkIndicator(indicatorDto, exceptions); // TODO
        ValidationUtils.checkMetadataRequired(indicatorDto.getUuid(), "INDICATORS.UUID", exceptions); // TODO id required
        ValidationUtils.checkMetadataRequired(indicatorDto.getVersionNumber(), "INDICATOR.VERSION_NUMBER", exceptions);
        ValidationUtils.checkMetadataUnmodifiable(indicatorInProduction.getIndicator().getCode(), indicatorDto.getCode(), "INDICATOR.CODE", exceptions);

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

    public static void checkUpdateDataSource(DataSourceDto dataSourceDto, DataSource dataSource, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

//        checkDataSource(dataSourceDto, exceptions); // TODO update 
        ValidationUtils.checkMetadataRequired(dataSourceDto.getUuid(), "DATA_SOURCE.UUID", exceptions); // TODO id required
        ValidationUtils.checkMetadataUnmodifiable(dataSourceDto.getQueryGpe(), dataSource.getQueryGpe(), "DATA_SOURCE.QUERY_GPE", exceptions);
        ValidationUtils.checkMetadataUnmodifiable(dataSourceDto.getPx(), dataSource.getPx(), "DATA_SOURCE.PX", exceptions);

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

    public static void checkUpdateIndicatorInstance(IndicatorInstanceDto indicatorInstanceDto, IndicatorInstance indicatorInstance, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

//        checkIndicatorInstance(indicatorInstanceDto, exceptions); // TODO update
        ValidationUtils.checkMetadataRequired(indicatorInstanceDto.getUuid(), "INDICATOR_INSTANCE.UUID", exceptions); // TODO id required
        ValidationUtils.checkMetadataUnmodifiable(indicatorInstanceDto.getIndicatorUuid(), indicatorInstance.getIndicator().getUuid(), "INDICATOR_INSTANCE.INDICATOR_UUID", exceptions);
        ValidationUtils.checkMetadataUnmodifiable(indicatorInstanceDto.getParentUuid(), indicatorInstance.getElementLevel().getParentUuid(), "INDICATOR_INSTANCE.PARENT_UUID", exceptions);
        ValidationUtils.checkMetadataUnmodifiable(indicatorInstanceDto.getOrderInLevel(), indicatorInstance.getElementLevel().getOrderInLevel(), "INDICATOR_INSTANCE.ORDER_IN_LEVEL", exceptions);

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

    private static void checkIndicatorsSystem(IndicatorsSystem indicatorsSystem, IndicatorsSystemVersion indicatorsSystemVersion, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkParameterRequired(indicatorsSystem, "INDICATORS_SYSTEM", exceptions);
        ValidationUtils.checkParameterRequired(indicatorsSystemVersion, "INDICATORS_SYSTEM", exceptions);
        ValidationUtils.checkMetadataRequired(indicatorsSystem.getCode(), "INDICATORS_SYSTEM.CODE", exceptions);
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
        if (ValidationUtils.isEmpty(indicatorInstance.getTemporaryGranularityId()) && ValidationUtils.isEmpty(indicatorInstance.getTemporaryValue())) {
            exceptions.add(new MetamacExceptionItem(CommonServiceExceptionType.METADATA_REQUIRED, "INDICATOR_INSTANCE.TEMPORARY"));
        }
        ValidationUtils.checkMetadataRequired(indicatorInstance.getElementLevel().getOrderInLevel(), "INDICATOR_INSTANCE.ORDER_IN_LEVEL", exceptions);
        if (indicatorInstance.getElementLevel().getOrderInLevel() != null && indicatorInstance.getElementLevel().getOrderInLevel() < 0) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, "INDICATOR_INSTANCE.ORDER_IN_LEVEL"));
        }
    }

    // TODO revisar qué metadatos son requeridos
    // TODO Quantity: cuáles son los metadatos obligatorios? Ojo! Depende del tipo de Quantity
    // TODO Quantity es obligatorio
    private static void checkIndicator(Indicator indicator, IndicatorVersion indicatorVersion, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkParameterRequired(indicator, "INDICATOR", exceptions);
        ValidationUtils.checkParameterRequired(indicatorVersion, "INDICATOR", exceptions);
        ValidationUtils.checkMetadataRequired(indicator.getCode(), "INDICATOR.CODE", exceptions);
        ValidationUtils.checkMetadataRequired(indicatorVersion.getName(), "INDICATOR.NAME", exceptions);
    }

    private static void checkDataSource(DataSource dataSource, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkParameterRequired(dataSource, "DATA_SOURCE", exceptions);
        ValidationUtils.checkMetadataRequired(dataSource.getQueryGpe(), "DATA_SOURCE.QUERY_GPE", exceptions);
        ValidationUtils.checkMetadataRequired(dataSource.getPx(), "DATA_SOURCE.PX", exceptions);
        ValidationUtils.checkMetadataRequired(dataSource.getTemporaryVariable(), "DATA_SOURCE.TEMPORARY_VARIABLE", exceptions);
        ValidationUtils.checkMetadataRequired(dataSource.getGeographicVariable(), "DATA_SOURCE.GEOGRAPHIC_VARIABLE", exceptions);
    }
}
