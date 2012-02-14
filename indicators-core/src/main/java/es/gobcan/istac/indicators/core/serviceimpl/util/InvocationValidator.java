package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;

import es.gobcan.istac.indicators.core.domain.Dimension;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
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
        checkMetadataEmpty(indicatorsSystemDto.getUuid(), "INDICATORS_SYSTEM.UUID", exceptions);
        checkMetadataEmpty(indicatorsSystemDto.getVersionNumber(), "INDICATORS_SYSTEM.VERSION_NUMBER", exceptions);
        
        throwIfException(exceptions);
    }
    
    public static void checkUpdateIndicatorsSystem(IndicatorsSystemDto indicatorsSystemDto, IndicatorsSystemVersion indicatorsSystemInProduction, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        checkIndicatorsSystem(indicatorsSystemDto, exceptions);
        checkMetadataRequired(indicatorsSystemDto.getUuid(), "INDICATORS_SYSTEM.UUID", exceptions);
        checkMetadataRequired(indicatorsSystemDto.getVersionNumber(), "INDICATORS_SYSTEM.VERSION_NUMBER", exceptions);
        checkMetadataUnmodifiable(indicatorsSystemInProduction.getIndicatorsSystem().getCode(), indicatorsSystemDto.getCode(), "INDICATORS_SYSTEM.CODE", exceptions);
        
        throwIfException(exceptions);
    }
    
    public static void checkRetrieveIndicatorsSystem(String uuid, String version, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        checkParameterRequired(uuid, "UUID", exceptions);
        // version is optional
        
        throwIfException(exceptions);
    }
    
    public static void checkRetrieveIndicatorsSystemPublished(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        checkParameterRequired(uuid, "UUID", exceptions);
        
        throwIfException(exceptions);
    }
    
    public static void checkDeleteIndicatorsSystem(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        checkParameterRequired(uuid, "UUID", exceptions);
        
        throwIfException(exceptions);
    }

    public static void checkSendIndicatorsSystemToProductionValidation(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        checkParameterRequired(uuid, "UUID", exceptions);
        
        throwIfException(exceptions);        
    }
    
    public static void checkSendIndicatorsSystemToDiffusionValidation(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        checkParameterRequired(uuid, "UUID", exceptions);
        
        throwIfException(exceptions);        
    }
    
    public static void checkRejectIndicatorsSystemValidation(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        checkParameterRequired(uuid, "UUID", exceptions);
        
        throwIfException(exceptions);        
    }
    
    public static void checkPublishIndicatorsSystem(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        checkParameterRequired(uuid, "UUID", exceptions);
        
        throwIfException(exceptions);        
    }
    
    public static void checkArchiveIndicatorsSystem(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        checkParameterRequired(uuid, "UUID", exceptions);
        
        throwIfException(exceptions);        
    }
    
    public static void checkVersioningIndicatorsSystem(String uuid, VersiontTypeEnum versionType, List<MetamacExceptionItem> exceptions) throws MetamacException {
        
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        checkParameterRequired(uuid, "UUID", exceptions);
        checkParameterRequired(versionType, "VERSION_TYPE", exceptions);
        
        throwIfException(exceptions);        
    }
    
    public static void checkFindIndicatorsSystems(List<MetamacExceptionItem> exceptions) throws MetamacException {
        
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        // nothing to validate
        
        throwIfException(exceptions);        
    }
    
    public static void checkFindIndicatorsSystemsPublished(List<MetamacExceptionItem> exceptions) throws MetamacException {
        
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        // nothing to validate
        
        throwIfException(exceptions);        
    }
    
    public static void checkCreateDimension(String indicatorsSystemUuid, DimensionDto dimensionDto, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        checkParameterRequired(indicatorsSystemUuid, "INDICATORS_SYSTEM_UUID", exceptions);
        checkDimension(dimensionDto, exceptions);
        checkMetadataEmpty(dimensionDto.getUuid(), "DIMENSION.UUID", exceptions);
        checkMetadataEmpty(dimensionDto.getSubdimensions(), "DIMENSION.SUBDIMENSIONS", exceptions);

        throwIfException(exceptions);
    }

    public static void checkRetrieveDimension(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        checkParameterRequired(uuid, "UUID", exceptions);

        throwIfException(exceptions);
    }

    public static void checkDeleteDimension(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        checkParameterRequired(uuid, "UUID", exceptions);

        throwIfException(exceptions);
    }

    public static void checkFindDimensions(String indicatorsSystemUuid, String indicatorsSystemVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        checkParameterRequired(indicatorsSystemUuid, "INDICATORS_SYSTEM_UUID", exceptions);
        checkParameterRequired(indicatorsSystemVersion, "INDICATORS_SYSTEM_VERSION", exceptions);

        throwIfException(exceptions);        
    }

    public static void checkUpdateDimension(DimensionDto dimensionDto, Dimension dimension, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        checkDimension(dimensionDto, exceptions);
        checkMetadataRequired(dimensionDto.getUuid(), "DIMENSION.UUID", exceptions);
        checkMetadataUnmodifiable(dimensionDto.getParentDimensionUuid(), dimension.getParent() != null ? dimension.getParent().getUuid() : null, "DIMENSION.PARENT_DIMENSION_UUID", exceptions);
        checkMetadataUnmodifiable(dimensionDto.getOrderInLevel(), dimension.getOrderInLevel(), "DIMENSION.ORDER_IN_LEVEL", exceptions);
        
        throwIfException(exceptions); 
    }
    
    public static void checkUpdateDimensionLocation(String uuid, String parentUuid, Long orderInLevel, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        checkParameterRequired(uuid, "UUID", exceptions);
        checkParameterRequired(orderInLevel, "ORDER_IN_LEVEL", exceptions);
        
        throwIfException(exceptions);
    }
    
    public static void checkCreateIndicator(IndicatorDto indicatorDto, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        checkIndicator(indicatorDto, exceptions);
        checkMetadataEmpty(indicatorDto.getUuid(), "INDICATOR.UUID", exceptions);
        checkMetadataEmpty(indicatorDto.getVersionNumber(), "INDICATOR.VERSION_NUMBER", exceptions);
        
        throwIfException(exceptions);
    }
    
    public static void checkRetrieveIndicator(String uuid, String version, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        checkParameterRequired(uuid, "UUID", exceptions);
        // version is optional
        
        throwIfException(exceptions);
    }
    
    public static void checkRetrieveIndicatorPublished(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        checkParameterRequired(uuid, "UUID", exceptions);
        
        throwIfException(exceptions);
    }
    
    @SuppressWarnings("rawtypes")
    private static void checkMetadataEmpty(Object parameter, String parameterName, List<MetamacExceptionItem> exceptions) {
        if (parameter == null) {
            return;
        } else if (List.class.isInstance(parameter) && ((List) parameter).size() == 0) {
            return;
        }
        exceptions.add(new MetamacExceptionItem(ServiceExceptionType.SERVICE_VALIDATION_METADATA_MUST_BE_EMPTY.getErrorCode(), ServiceExceptionType.SERVICE_VALIDATION_METADATA_MUST_BE_EMPTY.getMessageForReasonType(), parameterName));            
    }
    
    private static void checkIndicatorsSystem(IndicatorsSystemDto indicatorsSystemDto, List<MetamacExceptionItem> exceptions) {
        checkParameterRequired(indicatorsSystemDto, "INDICATORS_SYSTEM", exceptions);
        checkMetadataRequired(indicatorsSystemDto.getCode(), "INDICATORS_SYSTEM.CODE", exceptions);
        checkMetadataRequired(indicatorsSystemDto.getTitle(), "INDICATORS_SYSTEM.TITLE", exceptions);
    }

    private static void checkDimension(DimensionDto dimensionDto, List<MetamacExceptionItem> exceptions) {
        checkParameterRequired(dimensionDto, "DIMENSION", exceptions);
        checkMetadataRequired(dimensionDto.getTitle(), "DIMENSION.TITLE", exceptions);
        checkMetadataRequired(dimensionDto.getOrderInLevel(), "DIMENSION.ORDER_IN_LEVEL", exceptions);
        if (dimensionDto.getOrderInLevel() != null && dimensionDto.getOrderInLevel() < 0) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.SERVICE_VALIDATION_METADATA_INCORRECT.getErrorCode(), ServiceExceptionType.SERVICE_VALIDATION_METADATA_INCORRECT.getMessageForReasonType(), "DIMENSION.ORDER_IN_LEVEL"));
        }
    }
    
    // TODO revisar qué metadatos son requeridos
    private static void checkIndicator(IndicatorDto indicatorDto, List<MetamacExceptionItem> exceptions) {
        checkParameterRequired(indicatorDto, "INDICATOR", exceptions);
        checkMetadataRequired(indicatorDto.getCode(), "INDICATOR.CODE", exceptions);
        checkMetadataRequired(indicatorDto.getName(), "INDICATOR.NAME", exceptions);
    }
    
    private static void checkParameterRequired(Object parameter, String parameterName, List<MetamacExceptionItem> exceptions) {
        if (isEmpty(parameter)) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.SERVICE_INVALID_PARAMETER_REQUIRED.getErrorCode(), ServiceExceptionType.SERVICE_VALIDATION_METADATA_REQUIRED.getMessageForReasonType(), parameterName));            
        }
    }
    
    private static void checkMetadataRequired(Object parameter, String parameterName, List<MetamacExceptionItem> exceptions) {
        if (isEmpty(parameter)) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.SERVICE_VALIDATION_METADATA_REQUIRED.getErrorCode(), ServiceExceptionType.SERVICE_VALIDATION_METADATA_REQUIRED.getMessageForReasonType(), parameterName));            
        }
    }

    private static void checkMetadataUnmodifiable(String original, String actual, String parameterName, List<MetamacExceptionItem> exceptions) {
        if ((original == null && actual != null) || 
            (original != null && actual == null) ||
            (original != null && !original.equals(actual))) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.SERVICE_VALIDATION_METADATA_UNMODIFIABLE.getErrorCode(), ServiceExceptionType.SERVICE_VALIDATION_METADATA_UNMODIFIABLE.getMessageForReasonType(), parameterName));
        }
    }
    
    private static void checkMetadataUnmodifiable(Long original, Long actual, String parameterName, List<MetamacExceptionItem> exceptions) {
        if ((original == null && actual != null) || 
            (original != null && actual == null) ||
            (original != null && !original.equals(actual))) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.SERVICE_VALIDATION_METADATA_UNMODIFIABLE.getErrorCode(), ServiceExceptionType.SERVICE_VALIDATION_METADATA_UNMODIFIABLE.getMessageForReasonType(), parameterName));
        }
    }
    
    private static void throwIfException(List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions != null && !exceptions.isEmpty()) {
            throw new MetamacException(exceptions);
        }
    }
    
    @SuppressWarnings("rawtypes")
    private static Boolean isEmpty(Object parameter) {
        if (parameter == null) {
            return Boolean.TRUE;
        } else if ((String.class.isInstance(parameter) && StringUtils.isBlank((String) parameter)) ||
                   (List.class.isInstance(parameter) && ((List) parameter).size() == 0) ||
                   (Set.class.isInstance(parameter) && ((Set) parameter).size() == 0)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
