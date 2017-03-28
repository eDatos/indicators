package es.gobcan.istac.indicators.core.vo;

import java.util.Map;

import es.gobcan.istac.edatos.dataset.repository.dto.ObservationDto;

public class IndicatorObservationsVO extends IndicatorObservationsBaseVO {

    private Map<String, ObservationDto> observations;

    public Map<String, ObservationDto> getObservations() {
        return observations;
    }

    public void setObservations(Map<String, ObservationDto> observations) {
        this.observations = observations;
    }

}
