package es.gobcan.istac.indicators.core.mapper;

import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsSystemDto;

public interface Do2DtoMapper {
    
	public IndicatorsSystemDto indicatorsSystemDoToDto(IndicatorsSystemVersion indicatorsSystemVersion);
}
