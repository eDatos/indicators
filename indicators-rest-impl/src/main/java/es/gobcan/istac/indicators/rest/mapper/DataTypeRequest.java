package es.gobcan.istac.indicators.rest.mapper;

import java.util.List;
import java.util.Map;

import es.gobcan.istac.edatos.dataset.repository.dto.ObservationDto;
import es.gobcan.istac.edatos.dataset.repository.dto.ObservationExtendedDto;

import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;

public class DataTypeRequest {

    private IndicatorInstance                     indicatorInstance = null;
    private IndicatorVersion                      indicatorVersion  = null;
    private List<String>                          geographicalCodes = null;
    private List<String>                          timeCodes         = null;
    private List<String>                          measureCodes      = null;
    private Map<String, ? extends ObservationDto> observationMap    = null;

    public DataTypeRequest(IndicatorInstance indicatorInstance, List<String> geographicalCodes, List<String> timeValues, List<String> measureValues,
            Map<String, ? extends ObservationDto> observationMap) {
        super();
        this.indicatorInstance = indicatorInstance;
        this.geographicalCodes = geographicalCodes;
        this.timeCodes = timeValues;
        this.measureCodes = measureValues;
        this.observationMap = observationMap;
    }

    public DataTypeRequest(IndicatorVersion indicatorVersion, List<String> geographicalCodes, List<String> timeValues, List<String> measureValues, Map<String, ? extends ObservationDto> observationMap) {
        super();
        this.indicatorVersion = indicatorVersion;
        this.geographicalCodes = geographicalCodes;
        this.timeCodes = timeValues;
        this.measureCodes = measureValues;
        this.observationMap = observationMap;
    }

    public IndicatorVersion getIndicatorVersion() {
        return indicatorVersion;
    }

    public void setIndicatorVersion(IndicatorVersion indicatorVersion) {
        this.indicatorVersion = indicatorVersion;
    }

    public IndicatorInstance getIndicatorInstance() {
        return indicatorInstance;
    }

    public void setIndicatorInstance(IndicatorInstance indicatorInstance) {
        this.indicatorInstance = indicatorInstance;
    }

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

    public Map<String, ? extends ObservationDto> getObservationMap() {
        return observationMap;
    }

    public void setObservationMap(Map<String, ObservationExtendedDto> observationMap) {
        this.observationMap = observationMap;
    }

}
