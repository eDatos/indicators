package es.gobcan.istac.indicators.rest.types;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

import java.io.Serializable;
import java.util.Map;

@JsonPropertyOrder({
        "id",
        "kind",
        "selfLink",
        "parentLink",
        "title",
        "conceptDescription"
})
public class IndicatorInstanceBaseType implements Serializable {

    private static final long serialVersionUID = -2902494676944136458L;

    private String id;
    private String kind;
    private String selfLink;
    private LinkType parentLink;
    private String systemCode;

    private Map<String, String> title;
    private Map<String, String> conceptDescription;


    private MetadataType metadata;
    private DataType data;

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

    public LinkType getParentLink() {
        return parentLink;
    }

    public void setParentLink(LinkType parentLink) {
        this.parentLink = parentLink;
    }

    public Map<String, String> getTitle() {
        return title;
    }

    public void setTitle(Map<String, String> title) {
        this.title = title;
    }

    public Map<String, String> getConceptDescription() {
        return conceptDescription;
    }

    public void setConceptDescription(Map<String, String> conceptDescription) {
        this.conceptDescription = conceptDescription;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public MetadataType getMetadata() {
        return metadata;
    }

    public void setMetadata(MetadataType metadata) {
        this.metadata = metadata;
    }

    public DataType getData() {
        return data;
    }

    public void setData(DataType data) {
        this.data = data;
    }

}
