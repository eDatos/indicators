package es.gobcan.istac.indicators.rest.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"id", "kind", "selfLink", "title", "elements"})
public class ElementLevelType {

    private String                 id       = null;
    private String                 kind     = null;
    private String                 selfLink = null;
    private Map<String, String>    title    = null;

    private List<ElementLevelType> elements = new ArrayList<ElementLevelType>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Map<String, String> getTitle() {
        return title;
    }

    public void setTitle(Map<String, String> title) {
        this.title = title;
    }

    public List<ElementLevelType> getElements() {
        return elements;
    }

    public void setElements(List<ElementLevelType> elements) {
        this.elements = elements;
    }

}
