package es.gobcan.istac.indicators.rest.types;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"id", "value"})
public class DataAttributeType implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
