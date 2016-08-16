package es.gobcan.istac.indicators.core.dspl;


public enum DsplDataType {
    STRING,
    FLOAT,
    INTEGER,
    BOOLEAN,
    DATE;
    
    public String getDsplName() {
        return name().toLowerCase();
    }
}
