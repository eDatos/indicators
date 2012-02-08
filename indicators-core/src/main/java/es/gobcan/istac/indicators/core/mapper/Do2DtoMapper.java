package es.gobcan.istac.indicators.core.mapper;

import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;

public interface Do2DtoMapper {
    
	public IndicatorsSystemDto indicatorsSystemDoToDto(IndicatorsSystemVersion indicatorsSystemVersion);
}
