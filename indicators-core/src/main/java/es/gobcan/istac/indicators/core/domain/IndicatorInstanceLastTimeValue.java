package es.gobcan.istac.indicators.core.domain;


public class IndicatorInstanceLastTimeValue {

    private IndicatorInstance indicatorInstance;
    private String timeValue;

    public IndicatorInstanceLastTimeValue(IndicatorInstance indicatorInstance, String timeValue) {
        super();
        this.indicatorInstance = indicatorInstance;
        this.timeValue = timeValue;
    }

    public IndicatorInstance getIndicatorInstance() {
        return indicatorInstance;
    }
    
    public String getTimeValue() {
        return timeValue;
    }
}
