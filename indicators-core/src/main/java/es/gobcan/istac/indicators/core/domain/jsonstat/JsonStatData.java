package es.gobcan.istac.indicators.core.domain.jsonstat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonProperty;

/************************************************************************************************************************
 * Esta clase, y las situadas en el mismo paquete, se utilizan para el mapeo de ficheros JSON-stat en objetos Java.
 * Se basan en una interpretación de la documentación sobre el formato JSON-stat existente en la web
 * https://json-stat.org/format/ y se centran en el correcto parseo de los ficheros JSON-stat proporcionados por IBESTAT
 ************************************************************************************************************************/
public class JsonStatData {

    @JsonProperty
    private String            version;

    @JsonProperty("class")
    private String            clazz;

    @JsonProperty
    private String            label;

    @JsonProperty
    private String            source;

    @JsonProperty
    private String            updated;

    @JsonProperty
    private JsonStatExtension extension;

    @JsonProperty
    private List<String>      note;

    private List<String>      value = new ArrayList();

    @JsonProperty("value")
    public void processValue(List<Object> values) {
        for (Object object : values) {
            value.add(object.toString());
        }
    }

    @JsonProperty
    private Map<String, String>            status;

    @JsonProperty
    private List<String>                   id;

    @JsonProperty
    private List<String>                   size;

    @JsonProperty
    private Map<String, List<String>>      role;

    private Map<String, JsonStatDimension> dimension;

    @JsonProperty("dimension")
    public void processDimension(Map<String, JsonStatDimension> dimensions) {
        dimension = dimensions;

        for (String key : dimensions.keySet()) {
            addVariable(dimensions.get(key).getLabel());
            addValueLabels(key, new ArrayList<>(dimensions.get(key).getCategory().getIndex().keySet()));
            addValueCodes(key, new ArrayList<>(dimensions.get(key).getCategory().getLabel().values()));
        }
    }

    @JsonProperty
    private String                    href;

    List<String>                      variables;
    private Map<String, List<String>> valueLabels;
    private Map<String, List<String>> valueCodes;

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

    public List<String> getVariables() {
        return variables;
    }

    public void setVariables(List<String> variables) {
        this.variables = variables;
    }

    private void addVariable(String variable) {
        if (variables == null) {
            variables = new ArrayList();
        }
        variables.add(variable);
    }

    public Map<String, List<String>> getValueLabels() {
        return valueLabels;
    }

    public void setValueLabels(Map<String, List<String>> valueLabels) {
        this.valueLabels = valueLabels;
    }

    private void addValueLabels(String var, List<String> labels) {
        if (valueLabels == null) {
            valueLabels = new HashMap<String, List<String>>();
        }
        valueLabels.put(var, labels);
    }

    public Map<String, List<String>> getValueCodes() {
        return valueCodes;
    }

    public void setValueCodes(Map<String, List<String>> valueCodes) {
        this.valueCodes = valueCodes;
    }

    private void addValueCodes(String var, List<String> labels) {
        if (valueCodes == null) {
            valueCodes = new HashMap<String, List<String>>();
        }
        valueCodes.put(var, labels);
    }

    public String getTemporalVariable() {
        return getRole("time");
    }

    public String getContVariable() {
        return getRole("metric");
    }

    public String getSpatialVariable() {
        return getRole("geo");
    }

    private String getRole(String requestedRole) {
        if (role.containsKey(requestedRole)) {
            return role.get(requestedRole).get(0);
        }
        return null;
    }

    public String getSurveyCode() {
        return extension.getDatasetId();
    }

    public String getSurveyTitle() {
        return extension.getSurvey();
    }

    public String getUriPx() {
        return extension.getMetadata().get(0).getHref();
    }

    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).toString();
    }
}
