package es.gobcan.istac.indicators.web.client.enums;


public enum GeographicalSelectionTypeEnum {

    GRANULARITY,
    VALUE;
    
    private GeographicalSelectionTypeEnum() {
        
    }
    
    public String getName() {
        return name();
    }
    
}
