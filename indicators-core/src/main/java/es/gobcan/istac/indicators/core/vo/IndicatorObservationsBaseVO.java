package es.gobcan.istac.indicators.core.vo;

import java.util.List;

public class IndicatorObservationsBaseVO {

    private List<String> geographicalCodes;
    private List<String> timeCodes;
    private List<String> measureCodes;

    public List<String> getGeographicalCodes() {
        return geographicalCodes;
    }

    public void setGeographicalCodes(List<String> geographicalCodes) {
        this.geographicalCodes = geographicalCodes;
    }

    public List<String> getTimeCodes() {
        return timeCodes;
    }

    public void setTimeCodes(List<String> timeCodes) {
        this.timeCodes = timeCodes;
    }

    public List<String> getMeasureCodes() {
        return measureCodes;
    }

    public void setMeasureCodes(List<String> measureCodes) {
        this.measureCodes = measureCodes;
    }

}
