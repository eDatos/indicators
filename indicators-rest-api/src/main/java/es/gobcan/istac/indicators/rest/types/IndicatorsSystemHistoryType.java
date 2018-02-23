package es.gobcan.istac.indicators.rest.types;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"indicatorSystemId", "version", "publicationDate"})
public class IndicatorsSystemHistoryType implements Serializable {

    private static final long serialVersionUID  = -104741058158065023L;

    private String            indicatorSystemId = null;
    private String            version           = null;
    private Date              publicationDate   = null;

    public String getIndicatorSystemId() {
        return indicatorSystemId;
    }

    public void setIndicatorSystemId(String indicatorSystemId) {
        this.indicatorSystemId = indicatorSystemId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }
}
