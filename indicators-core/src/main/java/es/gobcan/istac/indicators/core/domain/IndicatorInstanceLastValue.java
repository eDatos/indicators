package es.gobcan.istac.indicators.core.domain;

import org.joda.time.DateTime;


public class IndicatorInstanceLastValue extends LastValue {

    private IndicatorInstance indicatorInstance;
    
    public IndicatorInstanceLastValue(IndicatorInstance indicatorInstance, GeographicalValue geographicalValue, TimeValue timeValue, DateTime lastDataUpdated) {
        super(geographicalValue, timeValue, lastDataUpdated);
        this.indicatorInstance = indicatorInstance;
    }

    public IndicatorInstance getIndicatorInstance() {
        return indicatorInstance;
    }
    
    public void setIndicatorInstance(IndicatorInstance indicatorInstance) {
        this.indicatorInstance = indicatorInstance;
    }
}
