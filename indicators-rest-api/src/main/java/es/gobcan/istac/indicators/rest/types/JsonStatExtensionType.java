package es.gobcan.istac.indicators.rest.types;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.ALWAYS)
public class JsonStatExtensionType {

    String indicatorId;
    String indicatorUuid;
    String lang;

    public String getIndicatorId() {
        return indicatorId;
    }

    public void setIndicatorId(String indicatorId) {
        this.indicatorId = indicatorId;
    }

    public String getIndicatorUuid() {
        return indicatorUuid;
    }

    public void setIndicatorUuid(String indicatorUuid) {
        this.indicatorUuid = indicatorUuid;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).toString();
    }
}
