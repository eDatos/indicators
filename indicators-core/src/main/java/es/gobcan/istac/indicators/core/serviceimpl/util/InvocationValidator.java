package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;

import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsSystemDto;

public class InvocationValidator {
    
    public static void checkCreateIndicatorsSystem(IndicatorsSystemDto indicatorsSystemDto, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        checkMetadataEmpty(indicatorsSystemDto.getUuid(), "UUID", exceptions);
        checkMetadataEmpty(indicatorsSystemDto.getVersionNumber(), "VERSION_NUMBER", exceptions);
        checkMetadataRequired(indicatorsSystemDto.getCode(), "CODE", exceptions);
        checkMetadataRequired(indicatorsSystemDto.getTitle(), "TITLE", exceptions);
        
        throwIfException(exceptions);
    }
    
    public static void checkRetrieveIndicatorsSystem(String uuid, Long version, List<MetamacExceptionItem> exceptions) throws MetamacException {
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
    
    private static void checkMetadataEmpty(Object parameter, String parameterName, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (parameter != null) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.SERVICE_VALIDATION_METADATA_MUST_BE_EMPTY.getErrorCode(), ServiceExceptionType.SERVICE_VALIDATION_METADATA_MUST_BE_EMPTY.getMessageForReasonType(), parameterName));            
        }
    }
    
    @SuppressWarnings("rawtypes")
    private static void checkMetadataRequired(Object parameter, String parameterName, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (parameter == null) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.SERVICE_VALIDATION_METADATA_REQUIRED.getErrorCode(), ServiceExceptionType.SERVICE_VALIDATION_METADATA_REQUIRED.getMessageForReasonType(), parameterName));            
        } else if ((String.class.isInstance(parameter) && StringUtils.isBlank((String) parameter)) ||
                   (List.class.isInstance(parameter) && ((List) parameter).size() == 0) ||
                   (Set.class.isInstance(parameter) && ((Set) parameter).size() == 0)) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.SERVICE_VALIDATION_METADATA_REQUIRED.getErrorCode(), ServiceExceptionType.SERVICE_VALIDATION_METADATA_REQUIRED.getMessageForReasonType(), parameterName));
        }
    }
    
    private static void throwIfException(List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions != null && !exceptions.isEmpty()) {
            throw new MetamacException(exceptions);
        }
    }
}
