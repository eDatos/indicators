package es.gobcan.istac.indicators.core.domain.jsonstat;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonProperty;

public class JsonStatExtension {

    @JsonProperty("datasetid")
    String                 datasetId;
    @JsonProperty
    String                 lang;
    @JsonProperty
    String                 contact;
    @JsonProperty
    String                 survey;
    @JsonProperty("baseperiod")
    String                 basePeriod;
    @JsonProperty
    List<JsonStatMetadata> metadata;

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getSurvey() {
        return survey;
    }

    public void setSurvey(String survey) {
        this.survey = survey;
    }

    public String getBasePeriod() {
        return basePeriod;
    }

    public void setBasePeriod(String basePeriod) {
        this.basePeriod = basePeriod;
    }

    public List<JsonStatMetadata> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<JsonStatMetadata> metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).toString();
    }
}
