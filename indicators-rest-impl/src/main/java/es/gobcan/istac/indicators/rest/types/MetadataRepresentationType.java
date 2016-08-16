package es.gobcan.istac.indicators.rest.types;

import java.io.Serializable;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"code", "title", "latitude", "longitude", "quantity"})
public class MetadataRepresentationType implements Serializable {

    private static final long   serialVersionUID = -7592282289916205999L;
    private String              code             = null;
    private Map<String, String> title            = null;

    private Double              latitude         = null;
    private Double              longitude        = null;
    private QuantityType        quantity         = null;

    private String              granularityCode;

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

    public QuantityType getQuantity() {
        return quantity;
    }

    public void setQuantity(QuantityType quantity) {
        this.quantity = quantity;
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

    public String getGranularityCode() {
        return granularityCode;
    }

    public void setGranularityCode(String granularityCode) {
        this.granularityCode = granularityCode;
    }
}