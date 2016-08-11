package es.gobcan.istac.indicators.rest.types;

import java.io.Serializable;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"id", "kind", "selfLink", "parentLink", "code", "version", "title", "acronym", "subjectCode", "systemSurveyLinks", "quantity", "conceptDescription", "notes", "dimension",
        "attribute", "childLink"})
public class IndicatorType extends IndicatorBaseType implements Serializable {

    private static final long                  serialVersionUID = 8209440349762494817L;

    private Map<String, MetadataDimensionType> dimension        = null;
    private Map<String, MetadataAttributeType> attribute        = null;
    private Integer                            decimalPlaces    = null;

    private LinkType                           childLink        = null;
    private LinkType                           parentLink       = null;

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

    public LinkType getParentLink() {
        return parentLink;
    }

    public void setParentLink(LinkType parentLink) {
        this.parentLink = parentLink;
    }

    public LinkType getChildLink() {
        return childLink;
    }

    public void setChildLink(LinkType childLink) {
        this.childLink = childLink;
    }

    public Integer getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(Integer decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }
}
