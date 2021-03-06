package es.gobcan.istac.indicators.rest.types;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"kind", "selfLink", "parentLink", "format", "dimension", "observation", "attribute"})
public class DataType implements Serializable {

    private static final long                serialVersionUID = 8269216607592124587L;

    private String                           kind             = null;
    private String                           selfLink         = null;
    private LinkType                         parentLink       = null;
    private List<String>                     format           = null;
    private Map<String, DataDimensionType>   dimension        = null;
    private List<String>                     observation      = null;
    private List<Map<String, AttributeType>> attribute        = null;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    public LinkType getParentLink() {
        return parentLink;
    }

    public void setParentLink(LinkType parentLink) {
        this.parentLink = parentLink;
    }

    public List<String> getFormat() {
        return format;
    }

    public void setFormat(List<String> format) {
        this.format = format;
    }

    public Map<String, DataDimensionType> getDimension() {
        return dimension;
    }

    public void setDimension(Map<String, DataDimensionType> dimension) {
        this.dimension = dimension;
    }

    public List<String> getObservation() {
        return observation;
    }

    public void setObservation(List<String> observation) {
        this.observation = observation;
    }

    public List<Map<String, AttributeType>> getAttribute() {
        return attribute;
    }

    public void setAttribute(List<Map<String, AttributeType>> attribute) {
        this.attribute = attribute;
    }

    public void addHeader(String selfLink, LinkType parentLink, String dataKind) {
        this.setKind(dataKind);
        this.setSelfLink(selfLink);
        this.setParentLink(parentLink);
    }
}
