package es.gobcan.istac.indicators.rest.types;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({
    "code",
    "message"
})
public class ErrorType {

    private String code    = null;
    private String message = null;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
