package es.gobcan.istac.indicators.core.domain;

import java.util.ArrayList;
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

    @JsonProperty
    private String                    uuid;

    @JsonProperty
    private String                    title;

    @JsonProperty("uriPx")
    private String                    pxUri;

    @JsonProperty
    private List<String>              stub;

    @JsonProperty
    private List<String>              heading;

    private Map<String, List<String>> valueLabels;
    private Map<String, List<String>> valueCodes;

    @JsonProperty
    private String                    temporalVariable;

    private List<String>              spatialVariables;

    @JsonProperty
    private String                    contVariable;

    @JsonProperty
    private List<String>              notes;

    @JsonProperty
    private String                    source;

    @JsonProperty
    private String                    surveyCode;

    @JsonProperty
    private String                    surveyTitle;

    @JsonProperty
    private List<String>              publishers;

    @SuppressWarnings("unchecked")
    @JsonProperty("categories")
    public void processCategories(List<Object> categories) {
        for (Object varDef : categories) {
            Map<String, Object> mapa = (Map<String, Object>) varDef;
            String var = (String) mapa.get("variable");
            List<String> labels = (List<String>) mapa.get("labels");
            List<String> codes = (List<String>) mapa.get("codes");
            addValueLabels(var, labels);
            addValueCodes(var, codes);
        }
    }

    @JsonProperty("spatials")
    public void processSpatials(List<String> spatials) {
        if (spatials == null || spatials.size() == 0) {
            spatialVariables = new ArrayList<String>();
        } else {
            spatialVariables = new ArrayList<String>(spatials);
        }
    }

    @JsonProperty("temporals")
    public void processTemporals(List<String> temporals) {
        if (temporals == null || temporals.size() == 0) {
            temporalVariable = null;
        } else {
            temporalVariable = temporals.get(0);
        }
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

    public String getPxUri() {
        return pxUri;
    }

    public void setPxUri(String pxUri) {
        this.pxUri = pxUri;
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

    public List<String> getVariables() {
        return new ArrayList<String>(valueCodes.keySet());
    }

    public Map<String, List<String>> getValueLabels() {
        return valueLabels;
    }

    public void setValueLabels(Map<String, List<String>> valueLabels) {
        this.valueLabels = valueLabels;
    }

    public Map<String, List<String>> getValueCodes() {
        return valueCodes;
    }

    public void setValueCodes(Map<String, List<String>> valueCodes) {
        this.valueCodes = valueCodes;
    }

    public String getTemporalVariable() {
        return temporalVariable;
    }

    public void setTemporalVariable(String temporalVariable) {
        this.temporalVariable = temporalVariable;
    }

    public List<String> getSpatialVariables() {
        return spatialVariables;
    }

    public void setSpatialVariables(List<String> spatialVariable) {
        this.spatialVariables = spatialVariable != null ? spatialVariable : new ArrayList<String>();
    }

    public String getContVariable() {
        return contVariable;
    }

    public void setContVariable(String contVariable) {
        this.contVariable = contVariable;
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

    public String getSurveyCode() {
        return surveyCode;
    }

    public void setSurveyCode(String surveyCode) {
        this.surveyCode = surveyCode;
    }

    public String getSurveyTitle() {
        return surveyTitle;
    }

    public void setSurveyTitle(String surveyTitle) {
        this.surveyTitle = surveyTitle;
    }

    public List<String> getPublishers() {
        return publishers;
    }

    public void setPublishers(List<String> publishers) {
        this.publishers = publishers;
    }

    private void addValueLabels(String var, List<String> labels) {
        if (valueLabels == null) {
            valueLabels = new HashMap<String, List<String>>();
        }
        valueLabels.put(var, labels);
    }

    private void addValueCodes(String var, List<String> labels) {
        if (valueCodes == null) {
            valueCodes = new HashMap<String, List<String>>();
        }
        valueCodes.put(var, labels);
    }

}
