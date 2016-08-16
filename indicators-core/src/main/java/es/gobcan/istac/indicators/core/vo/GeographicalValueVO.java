package es.gobcan.istac.indicators.core.vo;

import org.siemac.metamac.core.common.ent.domain.InternationalString;

public class GeographicalValueVO {

    private String                    code;
    private Double                    latitude;
    private Double                    longitude;
    private String                    order;
    private InternationalString       title;
    private GeographicalGranularityVO granularity;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public InternationalString getTitle() {
        return title;
    }

    public void setTitle(InternationalString title) {
        this.title = title;
    }

    public GeographicalGranularityVO getGranularity() {
        return granularity;
    }

    public void setGranularity(GeographicalGranularityVO granularity) {
        this.granularity = granularity;
    }

    @Override
    public int hashCode() {
        return getCode().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof GeographicalValueVO)) {
            return false;
        }
        GeographicalValueVO other = (GeographicalValueVO) obj;
        return getCode().equals(other.getCode());
    }
}
