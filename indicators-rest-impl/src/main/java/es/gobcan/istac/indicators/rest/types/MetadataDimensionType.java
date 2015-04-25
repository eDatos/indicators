package es.gobcan.istac.indicators.rest.types;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"code", "title", "granularity", "representation"})
public class MetadataDimensionType implements Serializable {

    private static final long                serialVersionUID = 4899374311088453009L;
    private String                           code             = null;
    private Map<String, List<String>>        title            = null;
    private List<MetadataGranularityType>    granularity      = null;
    private List<MetadataRepresentationType> representation   = null;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Map<String, List<String>> getTitle() {
        return title;
    }

    public void setTitle(Map<String, List<String>> title) {
        this.title = title;
    }

    public List<MetadataGranularityType> getGranularity() {
        return granularity;
    }

    public void setGranularity(List<MetadataGranularityType> granularity) {
        this.granularity = granularity;
    }

    public List<MetadataRepresentationType> getRepresentation() {
        return representation;
    }

    public void setRepresentation(List<MetadataRepresentationType> representation) {
        this.representation = representation;
    }

}