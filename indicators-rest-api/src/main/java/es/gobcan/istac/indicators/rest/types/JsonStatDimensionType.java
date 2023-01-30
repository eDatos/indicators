package es.gobcan.istac.indicators.rest.types;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.ALWAYS)
public class JsonStatDimensionType {

    private String label;
    private JsonStatCategoryType category;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public JsonStatCategoryType getCategory() {
        return category;
    }

    public void setCategory(JsonStatCategoryType category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).toString();
    }
}
