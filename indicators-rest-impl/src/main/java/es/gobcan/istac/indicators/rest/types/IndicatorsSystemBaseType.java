package es.gobcan.istac.indicators.rest.types;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.joda.time.DateTime;

@JsonPropertyOrder({"id", "kind", "selfLink", "code", "version", "publicationDate", "title", "acronym", "statisticalOperationLink", "description", "objective"})
public class IndicatorsSystemBaseType implements Serializable {

    private static final long   serialVersionUID         = 5785494152459693755L;

    private String              id                       = null;
    private String              kind                     = null;
    private String              selfLink                 = null;

    private String              code                     = null;
    private String              version                  = null;
    private DateTime            publicationDate          = null;

    private Map<String, String> title                    = null;
    private Map<String, String> acronym                  = null;
    private LinkType            statisticalOperationLink = null;
    private Map<String, String> description              = null;
    private Map<String, String> objective                = null;

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

    public DateTime getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(DateTime publicationDate) {
        this.publicationDate = publicationDate;
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

    public Map<String, String> getDescription() {
        return description;
    }

    public void setDescription(Map<String, String> description) {
        this.description = description;
    }

    public Map<String, String> getObjective() {
        return objective;
    }

    public void setObjective(Map<String, String> objective) {
        this.objective = objective;
    }

    public LinkType getStatisticalOperationLink() {
        return statisticalOperationLink;
    }

    public void setStatisticalOperationLink(LinkType statisticalOperationLink) {
        this.statisticalOperationLink = statisticalOperationLink;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
