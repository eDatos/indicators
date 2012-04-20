package es.gobcan.istac.indicators.rest.types;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

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
    "title",
    "childLink"
})
public class IndicatorInstanceType extends IndicatorInstanceBaseType implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4307766622180932870L;

    private LinkType          childLink        = null;

    public LinkType getChildLink() {
        return childLink;
    }

    public void setChildLink(LinkType childLink) {
        this.childLink = childLink;
    }

}
