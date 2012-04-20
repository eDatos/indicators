package es.gobcan.istac.indicators.rest.types;

import java.io.Serializable;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

import es.gobcan.istac.indicators.core.enume.domain.TimeGranularityEnum;

@JsonPropertyOrder({
    "id",
    "kind",
    "selfLink",
    "parentLink",
    "timeGranularity",
    "timeValue",
    "geographicalGranularity",
    "geographicalValue",
    "decimalPlaces",
    "title"
})
public class IndicatorInstanceBaseType implements Serializable {
    
    /**
     * 
     */
    private static final long   serialVersionUID        = -2902494676944136458L;
    
    private String              id                      = null;
    private String              kind                    = null;
    private String              selfLink                = null;
    private LinkType            parentLink              = null;

    private TimeGranularityEnum timeGranularity         = null;
    private String              timeValue               = null;
    private String              geographicalGranularity = null;
    private String              geographicalValue       = null;
    private Integer             decimalPlaces           = null;
    private Map<String, String> title                   = null;

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

    public String getTimeValue() {
        return timeValue;
    }

    public void setTimeValue(String timeValue) {
        this.timeValue = timeValue;
    }

    public String getGeographicalGranularity() {
        return geographicalGranularity;
    }

    public void setGeographicalGranularity(String geographicalGranularity) {
        this.geographicalGranularity = geographicalGranularity;
    }

    public String getGeographicalValue() {
        return geographicalValue;
    }

    public void setGeographicalValue(String geographicalValue) {
        this.geographicalValue = geographicalValue;
    }

    public Map<String, String> getTitle() {
        return title;
    }

    public void setTitle(Map<String, String> title) {
        this.title = title;
    }

    public TimeGranularityEnum getTimeGranularity() {
        return timeGranularity;
    }

    public void setTimeGranularity(TimeGranularityEnum timeGranularity) {
        this.timeGranularity = timeGranularity;
    }

    public Integer getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(Integer decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

}
