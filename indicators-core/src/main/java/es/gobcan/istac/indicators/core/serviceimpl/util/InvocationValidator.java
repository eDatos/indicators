package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils;

import es.gobcan.istac.indicators.core.domain.Dimension;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.dto.serviceapi.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.enume.domain.VersiontTypeEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

public class InvocationValidator {

    public static void checkCreateIndicatorsSystem(IndicatorsSystemDto indicatorsSystemDto, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkIndicatorsSystem(indicatorsSystemDto, exceptions);
        ValidationUtils.checkMetadataEmpty(indicatorsSystemDto.getUuid(), "INDICATORS_SYSTEM.UUID", exceptions);
        ValidationUtils.checkMetadataEmpty(indicatorsSystemDto.getVersionNumber(), "INDICATORS_SYSTEM.VERSION_NUMBER", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateIndicatorsSystem(IndicatorsSystemDto indicatorsSystemDto, IndicatorsSystemVersion indicatorsSystemInProduction, List<MetamacExceptionItem> exceptions)
            throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkIndicatorsSystem(indicatorsSystemDto, exceptions);
        ValidationUtils.checkMetadataRequired(indicatorsSystemDto.getUuid(), "INDICATORS_SYSTEM.UUID", exceptions);
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

    public static void checkCreateDimension(String indicatorsSystemUuid, DimensionDto dimensionDto, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(indicatorsSystemUuid, "INDICATORS_SYSTEM_UUID", exceptions);
        checkDimension(dimensionDto, exceptions);
        ValidationUtils.checkMetadataEmpty(dimensionDto.getUuid(), "DIMENSION.UUID", exceptions);
        ValidationUtils.checkMetadataEmpty(dimensionDto.getSubdimensions(), "DIMENSION.SUBDIMENSIONS", exceptions);

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

    public static void checkFindDimensions(String indicatorsSystemUuid, String indicatorsSystemVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(indicatorsSystemUuid, "INDICATORS_SYSTEM_UUID", exceptions);
        ValidationUtils.checkParameterRequired(indicatorsSystemVersion, "INDICATORS_SYSTEM_VERSION", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateDimension(DimensionDto dimensionDto, Dimension dimension, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkDimension(dimensionDto, exceptions);
        ValidationUtils.checkMetadataRequired(dimensionDto.getUuid(), "DIMENSION.UUID", exceptions);
        ValidationUtils.checkMetadataUnmodifiable(dimensionDto.getParentDimensionUuid(), dimension.getParent() != null ? dimension.getParent().getUuid() : null, "DIMENSION.PARENT_DIMENSION_UUID",
                exceptions);
        ValidationUtils.checkMetadataUnmodifiable(dimensionDto.getOrderInLevel(), dimension.getOrderInLevel(), "DIMENSION.ORDER_IN_LEVEL", exceptions);

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

    public static void checkCreateIndicator(IndicatorDto indicatorDto, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkIndicator(indicatorDto, exceptions);
        ValidationUtils.checkMetadataEmpty(indicatorDto.getUuid(), "INDICATOR.UUID", exceptions);
        ValidationUtils.checkMetadataEmpty(indicatorDto.getVersionNumber(), "INDICATOR.VERSION_NUMBER", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateIndicator(IndicatorDto indicatorDto, IndicatorVersion indicatorInProduction, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkIndicator(indicatorDto, exceptions);
        ValidationUtils.checkMetadataRequired(indicatorDto.getUuid(), "INDICATORS.UUID", exceptions);
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

    public static void checkDeleteIndicator(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);

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
    
    public static void checkCreateDataSource(String indicatorUuid, DataSourceDto dataSourceDto, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(indicatorUuid, "INDICATOR_UUID", exceptions);
        checkDataSource(dataSourceDto, exceptions);
        ValidationUtils.checkMetadataEmpty(dataSourceDto.getUuid(), "DATA_SOURCE.UUID", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }
//
//    public static void checkRetrieveDataSource(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
//
//        if (exceptions == null) {
//            exceptions = new ArrayList<MetamacExceptionItem>();
//        }
//
//        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);
//
//        ExceptionUtils.throwIfException(exceptions);
//    }

    public static void checkDeleteDataSource(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {

        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(uuid, "UUID", exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }
//
//    public static void checkFindDataSources(String indicatorsSystemUuid, String indicatorsSystemVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
//        if (exceptions == null) {
//            exceptions = new ArrayList<MetamacExceptionItem>();
//        }
//
//        ValidationUtils.checkParameterRequired(indicatorsSystemUuid, "INDICATORS_SYSTEM_UUID", exceptions);
//        ValidationUtils.checkParameterRequired(indicatorsSystemVersion, "INDICATORS_SYSTEM_VERSION", exceptions);
//
//        ExceptionUtils.throwIfException(exceptions);
//    }
//
//    public static void checkUpdateDataSource(DataSourceDto dataSourceDto, DataSource dataSource, List<MetamacExceptionItem> exceptions) throws MetamacException {
//        if (exceptions == null) {
//            exceptions = new ArrayList<MetamacExceptionItem>();
//        }
//
//        checkDataSource(dataSourceDto, exceptions);
//        ValidationUtils.checkMetadataRequired(dataSourceDto.getUuid(), "DATA_SOURCE.UUID", exceptions);
//        ValidationUtils.checkMetadataUnmodifiable(dataSourceDto.getParentDataSourceUuid(), dataSource.getParent() != null ? dataSource.getParent().getUuid() : null, "DATA_SOURCE.PARENT_DATA_SOURCE_UUID",
//                exceptions);
//        ValidationUtils.checkMetadataUnmodifiable(dataSourceDto.getOrderInLevel(), dataSource.getOrderInLevel(), "DATA_SOURCE.ORDER_IN_LEVEL", exceptions);
//
//        ExceptionUtils.throwIfException(exceptions);
//    }

    private static void checkIndicatorsSystem(IndicatorsSystemDto indicatorsSystemDto, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkParameterRequired(indicatorsSystemDto, "INDICATORS_SYSTEM", exceptions);
        ValidationUtils.checkMetadataRequired(indicatorsSystemDto.getCode(), "INDICATORS_SYSTEM.CODE", exceptions);
        ValidationUtils.checkMetadataRequired(indicatorsSystemDto.getTitle(), "INDICATORS_SYSTEM.TITLE", exceptions);
    }

    private static void checkDimension(DimensionDto dimensionDto, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkParameterRequired(dimensionDto, "DIMENSION", exceptions);
        ValidationUtils.checkMetadataRequired(dimensionDto.getTitle(), "DIMENSION.TITLE", exceptions);
        ValidationUtils.checkMetadataRequired(dimensionDto.getOrderInLevel(), "DIMENSION.ORDER_IN_LEVEL", exceptions);
        if (dimensionDto.getOrderInLevel() != null && dimensionDto.getOrderInLevel() < 0) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.SERVICE_METADATA_INCORRECT, "DIMENSION.ORDER_IN_LEVEL"));
        }
    }

    // TODO revisar qué metadatos son requeridos
    private static void checkIndicator(IndicatorDto indicatorDto, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkParameterRequired(indicatorDto, "INDICATOR", exceptions);
        ValidationUtils.checkMetadataRequired(indicatorDto.getCode(), "INDICATOR.CODE", exceptions);
        ValidationUtils.checkMetadataRequired(indicatorDto.getName(), "INDICATOR.NAME", exceptions);
    }

    private static void checkDataSource(DataSourceDto dataSourceDto, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkParameterRequired(dataSourceDto, "DATA_SOURCE", exceptions);
        ValidationUtils.checkMetadataRequired(dataSourceDto.getQueryGpe(), "DATA_SOURCE.QUERY_GPE", exceptions);
        ValidationUtils.checkMetadataRequired(dataSourceDto.getPx(), "DATA_SOURCE.PX", exceptions);
        ValidationUtils.checkMetadataRequired(dataSourceDto.getTemporaryVariable(), "DATA_SOURCE.TEMPORARY_VARIABLE", exceptions);
        ValidationUtils.checkMetadataRequired(dataSourceDto.getGeographicVariable(), "DATA_SOURCE.GEOGRAPHIC_VARIABLE", exceptions);
    }
}
