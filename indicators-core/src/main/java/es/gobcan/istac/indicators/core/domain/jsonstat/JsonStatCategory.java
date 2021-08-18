package es.gobcan.istac.indicators.core.domain.jsonstat;

import java.util.Map;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonProperty;

public class JsonStatCategory {

    @JsonProperty
    private Map<String, Integer>      index;

    @JsonProperty
    private Map<String, String>       label;

    @JsonProperty
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

    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).toString();
    }
}
