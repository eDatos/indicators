package es.gobcan.istac.indicators.rest.clients.adapters;

import java.util.Map;

import org.siemac.metamac.rest.statistical_operations.v1_0.domain.Operation;

import es.gobcan.istac.indicators.rest.mapper.MapperUtil;


public class OperationIndicators {
    private String id;
    private Map<String,String> title;
    private Map<String,String>  acronym;
    private Map<String,String>  description;
    private Map<String,String>  objective;
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
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
