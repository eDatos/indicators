package es.gobcan.istac.indicators.core.domain.jsonstat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonProperty;

public class JsonStatData {

    /*
     * Attribute: version
     * Required
     * Data type: String
     * Parents: Root
     * Children: None
     */
    @JsonProperty
    private String            version;

    /*
     * Attribute: class
     * Optional
     * Data type: String
     * Parents: Root
     * Children: None
     */
    @JsonProperty("class")
    private String            clazz;

    /*
     * Attribute: category
     * Required
     * Data type: Object
     * Parents: Root, dimension ID and relation ID array element
     * Children: index, label, child, coordinates and unit
     */
    @JsonProperty
    private String            label;

    /*
     * Attribute: source
     * Optional
     * Data type: String
     * Parents: Root, dimension ID and relation ID array element
     * Children: None
     */
    @JsonProperty
    private String            source;

    /*
     * Attribute: updated
     * Optional
     * Data type: String
     * Parents: Root, dimension ID and relation ID array element
     * Children: None
     */
    @JsonProperty
    private String            updated;

    @JsonProperty
    private JsonStatExtension extension;

    /*
     * Attribute: note
     * Optional
     * Data type: Array / Object
     * Parents: None, dimension ID, category and relation ID
     * Children: None
     */
    @JsonProperty
    private List<String>      note;

    /*
     * Attribute: value
     * Required
     * Data type: Array / Object
     * Parents: Root, Relation ID array element
     * Children: None
     */
    private List<String>      value = new ArrayList();

    @JsonProperty("value")
    public void processValue(List<Object> values) {
        for (Object object : values) {
            value.add(object.toString());
        }
    }

    /*
     * Attribute: status
     * Optional
     * Data type: Array / Object / String
     * Parents: Root, Relation ID array element
     * Children: None
     */
    @JsonProperty
    private Map<String, String>            status;

    /*
     * Attribute: id
     * Required
     * Data type: Array
     * Parents: Root, Relation ID array element
     * Children: None
     */
    @JsonProperty
    private List<String>                   id;

    /*
     * Attribute: size
     * Required
     * Data type: Array
     * Parents: Root, Relation ID array element
     * Children: None
     */
    @JsonProperty
    private List<String>                   size;

    /*
     * Attribute: role
     * Optional
     * Data type: Array
     * Parents: Root whet class "dataset", Relation ID array element
     * Children: time, geo and metric
     */
    @JsonProperty
    private Map<String, List<String>>      role;

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
     * Attribute: dimension
     * Required
     * Data type: Object
     * Parents: Root, Relation ID array element
     * Children: dimension ID
     */
    @JsonProperty
    private Map<String, JsonStatDimension> dimension;

    /*
     * Attribute: dimension ID
     * Required
     * Data type: Object
     * Parents: dimension
     * Children: category, label and class
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
    private String                         href;

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

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }

    public Map<String, String> getStatus() {
        return status;
    }

    public void setStatus(Map<String, String> status) {
        this.status = status;
    }

    public Map<String, List<String>> getRole() {
        return role;
    }

    public void setRole(Map<String, List<String>> role) {
        this.role = role;
    }

    public List<String> getNote() {
        return note;
    }

    public void setNote(List<String> note) {
        this.note = note;
    }

    public Map<String, JsonStatDimension> getDimension() {
        return dimension;
    }

    public void setDimension(Map<String, JsonStatDimension> dimension) {
        this.dimension = dimension;
    }

    public JsonStatDimension getDimension(String key) {
        return dimension.get(key);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public JsonStatExtension getExtension() {
        return extension;
    }

    public void setExtension(JsonStatExtension extension) {
        this.extension = extension;
    }

    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).toString();
    }

}
