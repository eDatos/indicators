package es.gobcan.istac.indicators.rest.types;

import java.io.Serializable;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({
    "id",
    "kind",
    "selfLink",
    "parentLink",
    "title",
    "conceptDescription",
    "subjectCode",
    "subjectTitle",
    "dimension",
    "decimalPlaces",
    "childLink"
})
public class IndicatorInstanceType extends IndicatorInstanceBaseType implements Serializable {

    /**
     * 
     */
    private static final long                  serialVersionUID = 4307766622180932870L;

    private Map<String, MetadataDimensionType> dimension        = null;
    private Integer                            decimalPlaces    = null;
    private String                             subjectCode      = null;
    private Map<String, String>                subjectTitle     = null;
    private LinkType                           childLink        = null;

    public Map<String, MetadataDimensionType> getDimension() {
        return dimension;
    }

    public void setDimension(Map<String, MetadataDimensionType> dimension) {
        this.dimension = dimension;
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

    public String getSubjectCode() {
        return subjectCode;
    }
    
    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }
    
    public Map<String, String> getSubjectTitle() {
        return subjectTitle;
    }
    
    public void setSubjectTitle(Map<String, String> subjectTitle) {
        this.subjectTitle = subjectTitle;
    }
}
