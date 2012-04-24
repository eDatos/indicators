package es.gobcan.istac.indicators.rest.types;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({
    "id",
    "kind",
    "selfLink",
    "code",
    "version",
    "title",
    "acronym",
    "subjectCode",
    "systemSurveyLinks",
    "quantity",
    "conceptDescription",
    "notes"
})
public class IndicatorBaseType implements Serializable {

    /**
     * 
     */
    private static final long   serialVersionUID   = 6882302798656723943L;

    private String              id                 = null;
    private String              kind               = null;
    private String              selfLink           = null;
    private String              code               = null;
    private String              version            = null;
    private Map<String, String> title              = null;
    private Map<String, String> acronym            = null;

    private String              subjectCode        = null;
    private List<LinkType>      systemSurveyLinks  = null;

    private QuantityType        quantity           = null;
    private Map<String, String> conceptDescription = null;
    private Map<String, String> notes              = null;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, String> getTitle() {
        return title;
    }

    public void setTitle(Map<String, String> title) {
        this.title = title;
    }

    public Map<String, String> getAcronym() {
        return acronym;
    }

    public void setAcronym(Map<String, String> acronym) {
        this.acronym = acronym;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public List<LinkType> getSystemSurveyLinks() {
        return systemSurveyLinks;
    }

    public void setSystemSurveyLinks(List<LinkType> systemSurveyLinks) {
        this.systemSurveyLinks = systemSurveyLinks;
    }

    public QuantityType getQuantity() {
        return quantity;
    }

    public void setQuantity(QuantityType quantity) {
        this.quantity = quantity;
    }

    public Map<String, String> getConceptDescription() {
        return conceptDescription;
    }

    public void setConceptDescription(Map<String, String> conceptDescription) {
        this.conceptDescription = conceptDescription;
    }

    public Map<String, String> getNotes() {
        return notes;
    }

    public void setNotes(Map<String, String> notes) {
        this.notes = notes;
    }

}