package es.gobcan.istac.indicators.rest.types;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"representation"})
public class DataDimensionType {

    private DataRepresentationType representation = null;

    public DataRepresentationType getRepresentation() {
        return representation;
    }

    public void setRepresentation(DataRepresentationType representation) {
        this.representation = representation;
    }
}