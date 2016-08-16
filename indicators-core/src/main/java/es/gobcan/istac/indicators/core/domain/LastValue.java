package es.gobcan.istac.indicators.core.domain;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

import com.arte.statistic.dataset.repository.dto.ObservationExtendedDto;

import es.gobcan.istac.indicators.core.enume.domain.MeasureDimensionTypeEnum;


public abstract class LastValue {
    
    private GeographicalValue geographicalValue;
    private TimeValue timeValue;
    private Map<MeasureDimensionTypeEnum,ObservationExtendedDto> observations;
    private DateTime lastDataUpdated;
    
    public LastValue(GeographicalValue geographicalValue, TimeValue timeValue, Map<MeasureDimensionTypeEnum,ObservationExtendedDto> observations, DateTime lastDataUpdated) {
        super();
        this.geographicalValue = geographicalValue;
        this.timeValue = timeValue;
        this.observations = observations;
        this.lastDataUpdated = lastDataUpdated;
    }
    
    public LastValue(GeographicalValue geographicalValue, TimeValue timeValue, DateTime lastDatUpdated) {
        super();
        this.geographicalValue = geographicalValue;
        this.timeValue = timeValue;
        this.observations = new HashMap<MeasureDimensionTypeEnum, ObservationExtendedDto>();
        this.lastDataUpdated = lastDatUpdated;
    }
    
    public void putObservation(MeasureDimensionTypeEnum measure, ObservationExtendedDto obs) {
        observations.put(measure, obs);
    }

    public GeographicalValue getGeographicalValue() {
        return geographicalValue;
    }
    
    public TimeValue getTimeValue() {
        return timeValue;
    }
    
    public Map<MeasureDimensionTypeEnum, ObservationExtendedDto> getObservations() {
        return observations;
    }
    
    public DateTime getLastDataUpdated() {
        return lastDataUpdated;
    }
}
