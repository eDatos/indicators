package es.gobcan.istac.indicators.rest.types;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({
    "format",
    "dimension",
    "observation"
})
public class DataType implements Serializable {

    /**
     * 
     */
    private static final long              serialVersionUID = 8269216607592124587L;
    private List<String>                   format           = null;
    private Map<String, DataDimensionType> dimension        = null;
    private List<String>                   observation      = null;

    public List<String> getObservation() {
        return observation;
    }

    public void setObservation(List<String> observation) {
        this.observation = observation;
    }

    public List<String> getFormat() {
        return format;
    }

    public void setFormat(List<String> format) {
        this.format = format;
    }

    public Map<String, DataDimensionType> getDimension() {
        return dimension;
    }

    public void setDimension(Map<String, DataDimensionType> dimension) {
        this.dimension = dimension;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}