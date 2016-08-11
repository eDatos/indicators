package es.gobcan.istac.indicators.core.vo;

import java.util.Map;

import com.arte.statistic.dataset.repository.dto.ObservationExtendedDto;

public class IndicatorObservationsExtendedVO extends IndicatorObservationsBaseVO {

    private Map<String, ObservationExtendedDto> observations;

    public Map<String, ObservationExtendedDto> getObservations() {
        return observations;
    }

    public void setObservations(Map<String, ObservationExtendedDto> observations) {
        this.observations = observations;
    }

}
