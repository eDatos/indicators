package es.gobcan.istac.indicators.core.dspl;


public class DsplTableColumn extends DsplNode {
    
    private String type;
    private String format;
    
    public DsplTableColumn(String id, String type) {
        super(id);
        this.type = type;
    }
    
    public DsplTableColumn(String id, String type, String format) {
        this(id,type);
        this.format = format;
    }
    
    
    public String getType() {
        return type;
    }
    
    public String getFormat() {
        return format;
    }
}
