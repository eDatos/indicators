package es.gobcan.istac.indicators.rest.types;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.ALWAYS)
public class JsonStatCategoryType {

    private Map<String, Long> index = new HashMap<>();
    private Map<String, String> label = new HashMap<>();
    private Map<String, JsonStatUnitType> unit = new HashMap<>();

    public Map<String, Long> getIndex() {
        return index;
    }

    public void setIndex(Map<String, Long> index) {
        this.index = index;
    }

    public Map<String, String> getLabel() {
        return label;
    }

    public void setLabel(Map<String, String> label) {
        this.label = label;
    }

    public Map<String, JsonStatUnitType> getUnit() {
        return unit;
    }

    public void setUnit(Map<String, JsonStatUnitType> unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).toString();
    }
}
