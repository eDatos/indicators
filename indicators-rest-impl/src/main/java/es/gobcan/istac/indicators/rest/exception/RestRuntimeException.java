package es.gobcan.istac.indicators.rest.exception;

import org.springframework.core.style.ToStringCreator;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

public final class RestRuntimeException extends RuntimeException {

    private static final long serialVersionUID = -6923191073167494679L;
    private HttpStatus        httpStatus       = null;

    public RestRuntimeException(final HttpStatus httpStatus) {
        super();
        Assert.notNull(httpStatus);

        this.httpStatus = httpStatus;
    }

    public RestRuntimeException(final HttpStatus httpStatus, final String message, final Throwable cause) {
        super(message, cause);

        Assert.notNull(httpStatus);

        this.httpStatus = httpStatus;
    }

    public RestRuntimeException(final HttpStatus httpStatus, final String message) {
        super(message);

        Assert.notNull(httpStatus);

        this.httpStatus = httpStatus;
    }

    public RestRuntimeException(final HttpStatus httpStatus, final Throwable cause) {
        super(cause);

        Assert.notNull(httpStatus);

        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
    
    @Override
    public String toString() {
        return new ToStringCreator(this).append("httpStatus", httpStatus).append("message", getMessage()).toString();
    }

}
