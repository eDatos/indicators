package es.gobcan.istac.indicators.core.domain.jsonstat;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;

public class JsonStatCategory {

    @JsonProperty("index")
    private Map<String, Integer>      index;

    @JsonProperty("label")
    private Map<String, String>       label;

    @JsonProperty("unit")
    private Map<String, JsonStatUnit> unit;

    public Map<String, Integer> getIndex() {
        return index;
    }

    public void setIndex(Map<String, Integer> index) {
        this.index = index;
    }

    public Integer getIndex(String key) {
        return index.get(key);
    }

    public Map<String, String> getLabel() {
        return label;
    }

    public void setLabel(Map<String, String> label) {
        this.label = label;
    }

    public String getLabel(String key) {
        return label.get(key);
    }

    public Map<String, JsonStatUnit> getUnit() {
        return unit;
    }

    public void setUnit(Map<String, JsonStatUnit> unit) {
        this.unit = unit;
    }

    public JsonStatUnit getUnit(String key) {
        return unit.get(key);
    }
}
