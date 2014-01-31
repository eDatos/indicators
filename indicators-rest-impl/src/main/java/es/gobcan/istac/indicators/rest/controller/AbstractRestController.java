package es.gobcan.istac.indicators.rest.controller;

import javax.servlet.http.HttpServletRequest;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.rest.exception.RestRuntimeException;
import es.gobcan.istac.indicators.rest.types.ErrorType;

public abstract class AbstractRestController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorType> handleException(Exception ex, HttpServletRequest request) {
        logger.error(ex.getMessage(), ex);

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
        if (RestRuntimeException.class.isInstance(ex)) {
            RestRuntimeException restRuntimeException = (RestRuntimeException) ex;
            httpStatus = restRuntimeException.getHttpStatus();
            message = ex.getMessage();
        } else if (MetamacException.class.isInstance(ex)) {
            MetamacException metamacException = (MetamacException) ex;
            MetamacExceptionItem metamacExceptionItem = metamacException.getExceptionItems().get(0);
            message = metamacExceptionItem.getMessage();
            if (ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND_WITH_CODE.getCode().equals(metamacExceptionItem.getCode())
                    || ServiceExceptionType.INDICATOR_INSTANCE_NOT_FOUND_WITH_CODE.getCode().equals(metamacExceptionItem.getCode())
                    || ServiceExceptionType.INDICATOR_NOT_FOUND_WITH_CODE.getCode().equals(metamacExceptionItem.getCode())
                    || ServiceExceptionType.GEOGRAPHICAL_VALUE_NOT_FOUND_WITH_CODE.getCode().equals(metamacExceptionItem.getCode())
                    || ServiceExceptionType.GEOGRAPHICAL_GRANULARITY_NOT_FOUND_WITH_CODE.getCode().equals(metamacExceptionItem.getCode())) {
                httpStatus = HttpStatus.NOT_FOUND;
            } else {
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }

        ErrorType errorType = new ErrorType();
        errorType.setCode("" + httpStatus.value());
        errorType.setMessage(message);
        ResponseEntity<ErrorType> responseEntity = new ResponseEntity<ErrorType>(errorType, httpStatus);
        return responseEntity;
    }

}
