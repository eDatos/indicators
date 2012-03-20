package es.gobcan.istac.indicators.core.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Data Structure, contains info like chapter, rows, columns, variables ...
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataStructure {

    private String                    uuid;
    
    @JsonProperty("table")
    private String                    title;
    
    @JsonProperty
    private String                    uriPx;

    @JsonProperty
    private List<String>              stub;

    @JsonProperty
    private List<String>              heading;

    private Map<String, List<String>> categoryLabels;
    private Map<String, List<String>> categoryCodes;

    @JsonProperty
    private List<String>              temporals;

    @JsonProperty
    private List<String>              spatials;

    @JsonProperty
    private List<String>              notes;

    @JsonProperty
    private String                    source;

    @JsonProperty("categories")
    public void setCategories(List<Object> categories) {
        for (Object varDef : categories) {
            Map<String, Object> mapa = (Map) varDef;
            String var = (String) mapa.get("variable");
            List<String> labels = (List<String>) mapa.get("labels");
            List<String> codes = (List<String>) mapa.get("codes");
            addCategoryLabels(var, labels);
            addCategoryCodes(var, codes);
        }
    }

    private void addCategoryLabels(String var, List<String> labels) {
        if (categoryLabels == null) {
            categoryLabels = new HashMap<String, List<String>>();
        }
        categoryLabels.put(var, labels);
    }
    private void addCategoryCodes(String var, List<String> labels) {
        if (categoryCodes == null) {
            categoryCodes = new HashMap<String, List<String>>();
        }
        categoryCodes.put(var, labels);
    }
    
    
    public String getUuid() {
        return uuid;
    }
    
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUriPx() {
        return uriPx;
    }

    public void setUriPx(String uriPx) {
        this.uriPx = uriPx;
    }

    public List<String> getStub() {
        return stub;
    }

    public void setStub(List<String> stub) {
        this.stub = stub;
    }

    public List<String> getHeading() {
        return heading;
    }

    public void setHeading(List<String> heading) {
        this.heading = heading;
    }

    public Map<String, List<String>> getCategoryLabels() {
        return categoryLabels;
    }

    public void setCategoryLabels(Map<String, List<String>> categoryLabels) {
        this.categoryLabels = categoryLabels;
    }

    public Map<String, List<String>> getCategoryCodes() {
        return categoryCodes;
    }

    public void setCategoryCodes(Map<String, List<String>> categoryCodes) {
        this.categoryCodes = categoryCodes;
    }

    public List<String> getTemporals() {
        return temporals;
    }

    public void setTemporals(List<String> temporals) {
        this.temporals = temporals;
    }

    public List<String> getSpatials() {
        return spatials;
    }

    public void setSpatials(List<String> spatials) {
        this.spatials = spatials;
    }

    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}
