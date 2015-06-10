package es.gobcan.istac.indicators.rest.types;

import java.util.List;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"id", "kind", "selfLink", "parentLink", "code", "version", "publicationDate", "title", "acronym", "statisticalOperationLink", "description", "objective", "elements", "childLink"})
public class IndicatorsSystemType extends IndicatorsSystemBaseType {

    private static final long      serialVersionUID = -5856767616725665032L;

    private List<ElementLevelType> elements         = null;
    private LinkType               childLink        = null;
    private LinkType               parentLink       = null;

    public LinkType getParentLink() {
        return parentLink;
    }

    public void setParentLink(LinkType parentLink) {
        this.parentLink = parentLink;
    }

    public List<ElementLevelType> getElements() {
        return elements;
    }

    public void setElements(List<ElementLevelType> elements) {
        this.elements = elements;
    }

    public LinkType getChildLink() {
        return childLink;
    }

    public void setChildLink(LinkType childLink) {
        this.childLink = childLink;
    }

}
