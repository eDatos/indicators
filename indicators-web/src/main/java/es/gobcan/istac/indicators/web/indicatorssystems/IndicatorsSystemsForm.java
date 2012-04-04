package es.gobcan.istac.indicators.web.indicatorssystems;

import java.util.List;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;

public class IndicatorsSystemsForm {

    private List<IndicatorsSystemDto> indicatorsSystemsDto;
    
    
    public List<IndicatorsSystemDto> getIndicatorsSystemsDto() {
        return indicatorsSystemsDto;
    }
    
    public void setIndicatorsSystemsDto(List<IndicatorsSystemDto> indicatorsSystemsDto) {
        this.indicatorsSystemsDto = indicatorsSystemsDto;
    }
}