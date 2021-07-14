package es.gobcan.istac.indicators.core.domain.jsonstat;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonStatDataStructure {

    /*
     * Attribute: version
     * Required
     * Data type: String
     * Parents: Root
     * Children: None
     */
    @JsonProperty
    private String       version;

    /*
     * Attribute: class
     * Optional
     * Data type: String
     * Parents: Root
     * Children: None
     */
    @JsonProperty("class")
    private String       clazz;

    /*
     * Attribute: id
     * Required
     * Data type: Array
     * Parents: Root, Relation ID array element
     * Children: None
     */
    @JsonProperty
    private List<String> id;

    /*
     * Attribute: size
     * Required
     * Data type: Array
     * Parents: Root, Relation ID array element
     * Children: None
     */
    @JsonProperty
    private List<String> size;

    /*
     * Attribute: role
     * Optional
     * Data type: Array
     * Parents: Root whet class "dataset", Relation ID array element
     * Children: time, geo and metric
     */
    // TODO EDATOS-3388

    /*
     * Attribute: role - time
     * Optional
     * Data type: Array
     * Parents: Role
     * Children: None
     */
    // TODO EDATOS-3388

    /*
     * Attribute: role - geo
     * Optional
     * Data type: Array
     * Parents: Role
     * Children: None
     */
    // TODO EDATOS-3388

    /*
     * Attribute: role - metric
     * Optional
     * Data type: Array
     * Parents: Role
     * Children: None
     */
    // TODO EDATOS-3388

    /*
     * Attribute: value
     * Required
     * Data type: Array / Object
     * Parents: Root, Relation ID array element
     * Children: None
     */
    // TODO EDATOS-3388

    /*
     * Attribute: status
     * Optional
     * Data type: Array / Object / String
     * Parents: Root, Relation ID array element
     * Children: None
     */
    // TODO EDATOS-3388

    /*
     * Attribute: dimension
     * Required
     * Data type: Object
     * Parents: Root, Relation ID array element
     * Children: dimension ID
     */
    // TODO EDATOS-3388

    /*
     * Attribute: dimension ID
     * Required
     * Data type: Object
     * Parents: dimension
     * Children: category, label and class
     */
    // TODO EDATOS-3388

    /*
     * Attribute: category
     * Required
     * Data type: Object
     * Parents: Root, dimension ID and relation ID array element
     * Children: index, label, child, coordinates and unit
     */
    // TODO EDATOS-3388

    /*
     * Attribute: index
     * Optional
     * Data type: Object / Array
     * Parents: category
     * Children: None
     */
    // TODO EDATOS-3388

    /*
     * Attribute: label
     * Optional
     * Data type: String / Array
     * Parents: Root, dimension ID, category, unit category ID and relation ID array element
     * Children: None
     */
    // TODO EDATOS-3388

    /*
     * Attribute: child
     * Optional
     * Data type: Object
     * Parents: category
     * Children: None
     */
    // TODO EDATOS-3388

    /*
     * Attribute: coordinates
     * Optional
     * Data type: Object
     * Parents: category
     * Children: None
     */
    // TODO EDATOS-3388

    /*
     * Attribute: unit
     * Optional
     * Data type: Object
     * Parents: category
     * Children: category ID
     */
    // TODO EDATOS-3388

    /*
     * Attribute: decimals
     * Optional
     * Data type: Number
     * Parents: Unit category ID
     * Children: None
     */
    // TODO EDATOS-3388

    /*
     * Attribute: symbol
     * Optional
     * Data type: String
     * Parents: Unit category ID
     * Children: None
     */
    // TODO EDATOS-3388

    /*
     * Attribute: position
     * Optional
     * Data type: String
     * Parents: Unit category ID
     * Children: None
     */
    // TODO EDATOS-3388

    /*
     * Attribute: updated
     * Optional
     * Data type: String
     * Parents: Root, dimension ID and relation ID array element
     * Children: None
     */
    @JsonProperty
    private String       updated;

    /*
     * Attribute: source
     * Optional
     * Data type: String
     * Parents: Root, dimension ID and relation ID array element
     * Children: None
     */
    @JsonProperty
    private String       source;

    /*
     * Attribute: extension
     * Optional
     * Data type: Object
     * Parents: Root, dimension ID and relation ID array element
     * Children: Open?
     */
    // TODO EDATOS-3388

    /*
     * Attribute: href
     * Optional
     * Data type: String
     * Parents: Root, dimension ID and relation ID array element
     * Children: None
     */
    @JsonProperty
    private String       href;

    /*
     * Attribute: link
     * Optional
     * Data type: Object
     * Parents: Root, dimension ID and relation ID array element
     * Children: relation ID
     */
    // TODO EDATOS-3388

    /*
     * Attribute: relation ID
     * Optional
     * Data type: Array
     * Parents: link
     * Children: None
     */
    // TODO EDATOS-3388

    /*
     * Attribute: type
     * Optional
     * Data type: String
     * Parents: Relation ID array element
     * Children: None
     */
    // TODO EDATOS-3388

    /*
     * Attribute: error
     * Optional
     * Data type: Array
     * Parents: Root
     * Children: Open
     */
    // TODO EDATOS-3388

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public List<String> getId() {
        return id;
    }

    public void setId(List<String> id) {
        this.id = id;
    }

    public List<String> getSize() {
        return size;
    }

    public void setSize(List<String> size) {
        this.size = size;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
