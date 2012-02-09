package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.ArrayList; 
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;

import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorsSystemVersionEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

public class InvocationValidator {
    
    public static void checkCreateIndicatorsSystem(IndicatorsSystemDto indicatorsSystemDto, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        checkMetadataEmpty(indicatorsSystemDto.getUuid(), "UUID", exceptions);
        checkMetadataEmpty(indicatorsSystemDto.getVersionNumber(), "VERSION_NUMBER", exceptions);
        checkIndicatorsSystem(indicatorsSystemDto, exceptions);
        
        throwIfException(exceptions);
    }
    
    public static void checkUpdateIndicatorsSystem(IndicatorsSystemDto indicatorsSystemDto, IndicatorsSystemVersion indicatorsSystemInProduction, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        checkMetadataRequired(indicatorsSystemDto.getUuid(), "UUID", exceptions);
        checkMetadataRequired(indicatorsSystemDto.getVersionNumber(), "VERSION_NUMBER", exceptions);
        checkIndicatorsSystem(indicatorsSystemDto, exceptions);
        checkMetadataUnmodifiable(indicatorsSystemInProduction.getIndicatorsSystem().getCode(), indicatorsSystemDto.getCode(), "CODE", exceptions);
        
        throwIfException(exceptions);
    }
    
    public static void checkRetrieveIndicatorsSystem(String uuid, String version, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        checkMetadataRequired(uuid, "UUID", exceptions);
        // version is optional
        
        throwIfException(exceptions);
    }
    
    public static void checkRetrieveIndicatorsSystemPublished(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        checkMetadataRequired(uuid, "UUID", exceptions);
        
        throwIfException(exceptions);
    }
    
    public static void checkDeleteIndicatorsSystem(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        checkMetadataRequired(uuid, "UUID", exceptions);
        
        throwIfException(exceptions);
    }

    public static void checkSendIndicatorsSystemToProductionValidation(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        checkMetadataRequired(uuid, "UUID", exceptions);
        
        throwIfException(exceptions);        
    }
    
    public static void checkSendIndicatorsSystemToDiffusionValidation(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        checkMetadataRequired(uuid, "UUID", exceptions);
        
        throwIfException(exceptions);        
    }
    
    public static void checkRefuseIndicatorsSystemValidation(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        checkMetadataRequired(uuid, "UUID", exceptions);
        
        throwIfException(exceptions);        
    }
    
    public static void checkPublishIndicatorsSystem(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        checkMetadataRequired(uuid, "UUID", exceptions);
        
        throwIfException(exceptions);        
    }
    
    public static void checkArchiveIndicatorsSystem(String uuid, List<MetamacExceptionItem> exceptions) throws MetamacException {
        
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        checkMetadataRequired(uuid, "UUID", exceptions);
        
        throwIfException(exceptions);        
    }
    
    public static void checkVersioningIndicatorsSystem(String uuid, IndicatorsSystemVersionEnum versionType, List<MetamacExceptionItem> exceptions) throws MetamacException {
        
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        checkMetadataRequired(uuid, "UUID", exceptions);
        checkMetadataRequired(versionType, "VERSION_TYPE", exceptions);
        
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
    
    private static void checkMetadataEmpty(Object parameter, String parameterName, List<MetamacExceptionItem> exceptions) {
        if (parameter != null) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.SERVICE_VALIDATION_METADATA_MUST_BE_EMPTY.getErrorCode(), ServiceExceptionType.SERVICE_VALIDATION_METADATA_MUST_BE_EMPTY.getMessageForReasonType(), parameterName));            
        }
    }
    
    private static void checkIndicatorsSystem(IndicatorsSystemDto indicatorsSystemDto, List<MetamacExceptionItem> exceptions) {
        checkMetadataRequired(indicatorsSystemDto.getCode(), "CODE", exceptions);
        checkMetadataRequired(indicatorsSystemDto.getTitle(), "TITLE", exceptions);
    }
    
    @SuppressWarnings("rawtypes")
    private static void checkMetadataRequired(Object parameter, String parameterName, List<MetamacExceptionItem> exceptions) {
        if (parameter == null) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.SERVICE_VALIDATION_METADATA_REQUIRED.getErrorCode(), ServiceExceptionType.SERVICE_VALIDATION_METADATA_REQUIRED.getMessageForReasonType(), parameterName));            
        } else if ((String.class.isInstance(parameter) && StringUtils.isBlank((String) parameter)) ||
                   (List.class.isInstance(parameter) && ((List) parameter).size() == 0) ||
                   (Set.class.isInstance(parameter) && ((Set) parameter).size() == 0)) {
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
    private static void throwIfException(List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions != null && !exceptions.isEmpty()) {
            throw new MetamacException(exceptions);
        }
    }
}
