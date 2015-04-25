package es.gobcan.istac.indicators.rest.types;

import java.io.Serializable;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"code", "value"})
public class AttributeType implements Serializable {

    /**
     *
     */
    private static final long   serialVersionUID = 7203753322975868294L;

    private String              code             = null;
    private Map<String, String> value            = null;

    public String getCode() {
        return code;
    }

    public void setCode(String id) {
        this.code = id;
    }

    public Map<String, String> getValue() {
        return value;
    }

    public void setValue(Map<String, String> value) {
        this.value = value;
    }

}
