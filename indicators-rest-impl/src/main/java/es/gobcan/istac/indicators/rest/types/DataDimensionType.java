package es.gobcan.istac.indicators.rest.types;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"representationIndex"})
public class DataDimensionType {

    private Map<String, Integer> representationIndex = null;

    public Map<String, Integer> getRepresentationIndex() {
        return representationIndex;
    }

    public void setRepresentationIndex(Map<String, Integer> representationIndex) {
        this.representationIndex = representationIndex;
    }

}