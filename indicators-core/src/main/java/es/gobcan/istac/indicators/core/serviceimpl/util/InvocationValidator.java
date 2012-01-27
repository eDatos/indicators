package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;

import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorSystemDto;

public class InvocationValidator {
        
    public static void checkCreateIndicatorSystem(IndicatorSystemDto indicatorSystemDto) throws MetamacException {
        checkCreateIndicatorSystem(indicatorSystemDto, null);
    }
    
    /**
     * Check indicatorSystem mandatory metadata for create it
     */
    private static void checkCreateIndicatorSystem(IndicatorSystemDto indicatorSystemDto, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        checkMetadataEmpty(indicatorSystemDto.getUuid(), "UUID", exceptions);
        checkMetadataEmpty(indicatorSystemDto.getVersionNumber(), "VERSION_NUMBER", exceptions);
        checkMetadataRequired(indicatorSystemDto.getCode(), "CODE", exceptions);
        checkMetadataRequired(indicatorSystemDto.getTitle(), "TITLE", exceptions);
        
        if (!exceptions.isEmpty()) {
            throw new MetamacException(exceptions);
        }
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
}
