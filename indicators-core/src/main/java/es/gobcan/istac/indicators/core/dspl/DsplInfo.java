package es.gobcan.istac.indicators.core.dspl;



public class DsplInfo {
    private DsplLocalisedValue name;
    private DsplLocalisedValue description;
    private DsplLocalisedValue url;
    
    public DsplInfo() {
        name = new DsplLocalisedValue();
        description = new DsplLocalisedValue();
        url = new DsplLocalisedValue();
    }
    
    public DsplLocalisedValue getName() {
        return name;
    }
    
    public DsplLocalisedValue getDescription() {
        return description;
    }
    
    public DsplLocalisedValue getUrl() {
        return url;
    }
    
    @Override
    public String toString() {
        return "Info: "+name.toString();
    }
}
