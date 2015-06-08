package es.gobcan.istac.indicators.rest.types;

import java.io.Serializable;
import java.util.Map;

public class GeographicalValueType implements Serializable {

    private static final long   serialVersionUID = -8383077859978270324L;

    private String              code;
    private Map<String, String> title;
    private String              granularityCode;
    private Double              latitude;
    private Double              longitude;

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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

}
