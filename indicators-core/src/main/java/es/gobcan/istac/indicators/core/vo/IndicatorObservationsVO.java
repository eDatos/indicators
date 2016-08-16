package es.gobcan.istac.indicators.core.vo;

import java.util.Map;

import com.arte.statistic.dataset.repository.dto.ObservationDto;

public class IndicatorObservationsVO extends IndicatorObservationsBaseVO {

    private Map<String, ObservationDto> observations;

    public Map<String, ObservationDto> getObservations() {
        return observations;
    }

    public void setObservations(Map<String, ObservationDto> observations) {
        this.observations = observations;
    }

}
