package es.gobcan.istac.indicators.rest.types;

import java.io.Serializable;
import java.util.List;

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
    "geographicalGranularities",
    "timeGranularities",
    "geographicalValues",
    "timeValues",
    "measureValues",
    "childLink"
})
public class IndicatorType extends IndicatorBaseType implements Serializable {

    /**
     * 
     */
    private static final long                 serialVersionUID          = 8209440349762494817L;

    private List<GeographicalGranularityType> geographicalGranularities = null;
    private List<TimeGranularityType>         timeGranularities         = null;
    private List<GeographicalValueType>       geographicalValues        = null;
    private List<TimeValueType>               timeValues                = null;
    private List<MeasureValueType>            measureValues             = null;
    private LinkType                          childLink                 = null;

    public List<GeographicalGranularityType> getGeographicalGranularities() {
        return geographicalGranularities;
    }

    public void setGeographicalGranularities(List<GeographicalGranularityType> geographicalGranularities) {
        this.geographicalGranularities = geographicalGranularities;
    }

    public List<TimeGranularityType> getTimeGranularities() {
        return timeGranularities;
    }

    public void setTimeGranularities(List<TimeGranularityType> timeGranularities) {
        this.timeGranularities = timeGranularities;
    }

    public List<GeographicalValueType> getGeographicalValues() {
        return geographicalValues;
    }

    public void setGeographicalValues(List<GeographicalValueType> geographicalValues) {
        this.geographicalValues = geographicalValues;
    }

    public List<TimeValueType> getTimeValues() {
        return timeValues;
    }

    public void setTimeValues(List<TimeValueType> timeValues) {
        this.timeValues = timeValues;
    }

    public List<MeasureValueType> getMeasureValues() {
        return measureValues;
    }

    public void setMeasureValues(List<MeasureValueType> measureValues) {
        this.measureValues = measureValues;
    }

    public LinkType getChildLink() {
        return childLink;
    }

    public void setChildLink(LinkType childLink) {
        this.childLink = childLink;
    }
}
