package es.gobcan.istac.indicators.core.domain.jsonstat;

import org.codehaus.jackson.annotate.JsonProperty;

public class JsonStatDimension {

    @JsonProperty("label")
    private String           label;

    @JsonProperty("category")
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

}
