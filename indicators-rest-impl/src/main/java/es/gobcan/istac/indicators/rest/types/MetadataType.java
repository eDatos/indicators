package es.gobcan.istac.indicators.rest.types;

import java.util.Map;

public class MetadataType {

    private Map<String, MetadataDimensionType> dimension;
    private Map<String, MetadataAttributeType> attribute;

    public Map<String, MetadataDimensionType> getDimension() {
        return dimension;
    }

    public void setDimension(Map<String, MetadataDimensionType> dimension) {
        this.dimension = dimension;
    }

    public Map<String, MetadataAttributeType> getAttribute() {
        return attribute;
    }

    public void setAttribute(Map<String, MetadataAttributeType> attribute) {
        this.attribute = attribute;
    }

}
