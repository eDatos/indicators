package es.gobcan.istac.indicators.core.domain;


public class IndicatorVersionLastTimeValue {

    private IndicatorVersion indicatorVersion;
    private String timeValue;

        
    public IndicatorVersionLastTimeValue(IndicatorVersion indicatorVersion, String timeValue) {
        super();
        this.indicatorVersion = indicatorVersion;
        this.timeValue = timeValue;
    }

    public IndicatorVersion getIndicatorVersion() {
        return indicatorVersion;
    }
    
    public String getTimeValue() {
        return timeValue;
    }
}
