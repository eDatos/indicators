package es.gobcan.istac.indicators.rest.types;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({
    "formatId",
    "formatSize",
    "dimension",
    "observation"
})
public class DataType implements Serializable {

    /**
     * 
     */
    private static final long              serialVersionUID = 8269216607592124587L;
    private List<String>                   observation      = null;
    private List<String>                   formatId         = null;
    private List<Integer>                  formatSize       = null;
    private Map<String, DataDimensionType> dimension        = null;

    public List<String> getObservation() {
        return observation;
    }

    public void setObservation(List<String> observation) {
        this.observation = observation;
    }

    public List<String> getFormatId() {
        return formatId;
    }

    public void setFormatId(List<String> formatId) {
        this.formatId = formatId;
    }

    public List<Integer> getFormatSize() {
        return formatSize;
    }

    public void setFormatSize(List<Integer> formatSize) {
        this.formatSize = formatSize;
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
