package es.gobcan.istac.indicators.rest.types;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.siemac.metamac.rest.utils.RestUtils;

import es.gobcan.istac.indicators.rest.RestConstants;

@JsonPropertyOrder({"kind", "selfLink", "format", "dimension", "observation", "attribute"})
public class DataType implements Serializable {

    private static final long                serialVersionUID = 8269216607592124587L;

    private String                           kind             = null;
    private String                           selfLink         = null;
    private List<String>                     format           = null;
    private Map<String, DataDimensionType>   dimension        = null;
    private List<String>                     observation      = null;
    private List<Map<String, AttributeType>> attribute        = null;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    public List<String> getFormat() {
        return format;
    }

    public void setFormat(List<String> format) {
        this.format = format;
    }

    public Map<String, DataDimensionType> getDimension() {
        return dimension;
    }

    public void setDimension(Map<String, DataDimensionType> dimension) {
        this.dimension = dimension;
    }

    public List<String> getObservation() {
        return observation;
    }

    public void setObservation(List<String> observation) {
        this.observation = observation;
    }

    public List<Map<String, AttributeType>> getAttribute() {
        return attribute;
    }

    public void setAttribute(List<Map<String, AttributeType>> attribute) {
        this.attribute = attribute;
    }

    public void addHeader(String baseURL, String fields, String representation, String granularity, String dataKind) {
        this.setKind(dataKind);
        this.setSelfLink(toPaginatedLink(baseURL, fields, representation, granularity));
    }

    private static String toPaginatedLink(String baseURL, String fields, String representation, String granularity) {
        String link = baseURL;
        link = RestUtils.createLinkWithQueryParam(link, RestConstants.PARAMETER_FIELDS, fields);
        link = RestUtils.createLinkWithQueryParam(link, RestConstants.PARAMETER_REPRESENTATION, representation);
        link = RestUtils.createLinkWithQueryParam(link, RestConstants.PARAMETER_GRANULARITY, granularity);
        return link;
    }
}
