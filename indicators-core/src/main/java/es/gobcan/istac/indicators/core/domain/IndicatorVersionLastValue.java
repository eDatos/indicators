package es.gobcan.istac.indicators.core.domain;

import org.joda.time.DateTime;



public class IndicatorVersionLastValue extends LastValue {

    private IndicatorVersion indicatorVersion;
    
    public IndicatorVersionLastValue(IndicatorVersion indicatorVersion, GeographicalValue geographicalValue, TimeValue timeValue, DateTime lastDataUpdated) {
        super(geographicalValue, timeValue, lastDataUpdated);
        this.indicatorVersion = indicatorVersion;
    }

    public IndicatorVersion getIndicatorVersion() {
        return indicatorVersion;
    }
    
    public void setIndicatorVersion(IndicatorVersion indicatorVersion) {
        this.indicatorVersion = indicatorVersion;
    }
    
}
