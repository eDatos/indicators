package es.gobcan.istac.indicators.core.domain.jsonstat;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonProperty;

public class JsonStatDimension {

    @JsonProperty
    private String           label;

    @JsonProperty
    private JsonStatCategory category;

    public String getLabel() {
        return label;
    }

    public JsonStatCategory getCategory() {
        return category;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setCategory(JsonStatCategory category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).toString();
    }
}
