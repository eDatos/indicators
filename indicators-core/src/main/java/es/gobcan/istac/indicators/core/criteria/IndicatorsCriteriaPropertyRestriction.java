package es.gobcan.istac.indicators.core.criteria;

public class IndicatorsCriteriaPropertyRestriction {

    public enum OperationType {
        EQ;
    }

    private static final long serialVersionUID = 1L;

    private String            propertyName;
    private OperationType     operationType;
    
    private String            stringValue;
    private Boolean           booleanValue;
    private Enum              enumValue;

    public IndicatorsCriteriaPropertyRestriction(String propertyName, String stringValue) {
        this.propertyName = propertyName;
        this.stringValue = stringValue;
        this.operationType = OperationType.EQ;
    }

    public IndicatorsCriteriaPropertyRestriction(String propertyName, Boolean booleanValue) {
        this.propertyName = propertyName;
        this.booleanValue = booleanValue;
        this.operationType = OperationType.EQ;
    }
    
    public IndicatorsCriteriaPropertyRestriction(String propertyName, Enum enumValue) {
        this.propertyName = propertyName;
        this.enumValue = enumValue;
        this.operationType = OperationType.EQ;
    }

    public String getPropertyName() {
        return this.propertyName;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public String getStringValue() {
        return this.stringValue;
    }

    public Boolean getBooleanValue() {
        return booleanValue;
    }
    
    public Enum getEnumValue() {
        return enumValue;
    }
}
