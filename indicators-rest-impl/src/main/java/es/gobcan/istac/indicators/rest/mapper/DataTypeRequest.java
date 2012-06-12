package es.gobcan.istac.indicators.rest.mapper;

import java.util.List;
import java.util.Map;

import com.arte.statistic.dataset.repository.dto.ObservationExtendedDto;

import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.MeasureValue;
import es.gobcan.istac.indicators.core.domain.TimeValue;

public class DataTypeRequest {

    private IndicatorInstance                   indicatorInstance  = null;
    private IndicatorVersion                    indicatorVersion   = null;
    private List<GeographicalValue>             geographicalValues = null;
    private List<TimeValue>                     timeValues         = null;
    private List<MeasureValue>                  measureValues      = null;
    private Map<String, ObservationExtendedDto> observationMap     = null;

    public DataTypeRequest(IndicatorInstance indicatorInstance, List<GeographicalValue> geographicalValues, List<TimeValue> timeValues, List<MeasureValue> measureValues,
            Map<String, ObservationExtendedDto> observationMap) {
        super();
        this.indicatorInstance = indicatorInstance;
        this.geographicalValues = geographicalValues;
        this.timeValues = timeValues;
        this.measureValues = measureValues;
        this.observationMap = observationMap;
    }

    public DataTypeRequest(IndicatorVersion indicatorVersion, List<GeographicalValue> geographicalValues, List<TimeValue> timeValues, List<MeasureValue> measureValues,
            Map<String, ObservationExtendedDto> observationMap) {
        super();
        this.indicatorVersion = indicatorVersion;
        this.geographicalValues = geographicalValues;
        this.timeValues = timeValues;
        this.measureValues = measureValues;
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

    public List<GeographicalValue> getGeographicalValues() {
        return geographicalValues;
    }

    public void setGeographicalValues(List<GeographicalValue> geographicalValues) {
        this.geographicalValues = geographicalValues;
    }

    public List<TimeValue> getTimeValues() {
        return timeValues;
    }

    public void setTimeValues(List<TimeValue> timeValues) {
        this.timeValues = timeValues;
    }

    public List<MeasureValue> getMeasureValues() {
        return measureValues;
    }

    public void setMeasureValues(List<MeasureValue> measureValues) {
        this.measureValues = measureValues;
    }

    public Map<String, ObservationExtendedDto> getObservationMap() {
        return observationMap;
    }

    public void setObservationMap(Map<String, ObservationExtendedDto> observationMap) {
        this.observationMap = observationMap;
    }

}
