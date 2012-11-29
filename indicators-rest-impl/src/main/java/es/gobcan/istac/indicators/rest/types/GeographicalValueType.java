package es.gobcan.istac.indicators.rest.types;

import java.util.Map;

public class GeographicalValueType {

    private String code;
    private Map<String, String> title;
    private String granularityCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Map<String, String> getTitle() {
        return title;
    }

    public void setTitle(Map<String, String> title) {
        this.title = title;
    }

    public String getGranularityCode() {
        return granularityCode;
    }

    public void setGranularityCode(String granularityCode) {
        this.granularityCode = granularityCode;
    }

}
