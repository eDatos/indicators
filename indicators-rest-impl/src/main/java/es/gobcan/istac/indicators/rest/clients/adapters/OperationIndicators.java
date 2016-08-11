package es.gobcan.istac.indicators.rest.clients.adapters;

import java.util.Map;

public class OperationIndicators {

    private String              id;
    private Map<String, String> title;
    private Map<String, String> acronym;
    private Map<String, String> description;
    private Map<String, String> objective;
    private String              uri;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Map<String, String> getTitle() {
        return title;
    }

    public void setTitle(Map<String, String> title) {
        this.title = title;
    }

    public Map<String, String> getAcronym() {
        return acronym;
    }

    public void setAcronym(Map<String, String> acronym) {
        this.acronym = acronym;
    }

    public Map<String, String> getDescription() {
        return description;
    }

    public void setDescription(Map<String, String> description) {
        this.description = description;
    }

    public Map<String, String> getObjective() {
        return objective;
    }

    public void setObjective(Map<String, String> objective) {
        this.objective = objective;
    }

}
