package es.gobcan.istac.indicators.rest.types;

import java.io.Serializable;
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
    "notes",
    "dimension",
    "childLink"
})
public class IndicatorType extends IndicatorBaseType implements Serializable {

    /**
     * 
     */
    private static final long                  serialVersionUID = 8209440349762494817L;

    private Map<String, MetadataDimensionType> dimension        = null;
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
}
