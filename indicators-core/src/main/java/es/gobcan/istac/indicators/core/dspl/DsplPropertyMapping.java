package es.gobcan.istac.indicators.core.dspl;


public class DsplPropertyMapping {
    private String propertyRef;
    private String lang;
    private String columnName;
    
    public DsplPropertyMapping(String propertyRef, String lang, String columnName) {
        super();
        this.propertyRef = propertyRef;
        this.lang = lang;
        this.columnName = columnName;
    }

    
    public String getPropertyRef() {
        return propertyRef;
    }

    
    public String getLang() {
        return lang;
    }

    
    public String getColumnName() {
        return columnName;
    }
    
    
}
