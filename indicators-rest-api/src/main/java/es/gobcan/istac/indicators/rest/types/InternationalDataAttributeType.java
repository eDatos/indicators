package es.gobcan.istac.indicators.rest.types;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"id", "value"})
public class InternationalDataAttributeType implements Serializable {

    private static final long            serialVersionUID = 1L;

    private String                       id;
    private List<Map<String, String>>    value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Map<String, String>> getValue() {
        return value;
    }

    public void setValue(List<Map<String, String>> value) {
        this.value = value;
    }
}
