package es.gobcan.istac.indicators.core.mapper;

import es.gobcan.istac.indicators.core.domain.IndicatorSystemVersion;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorSystemDto;

public interface Do2DtoMapper {
    
	public IndicatorSystemDto indicatorSystemDoToDto(IndicatorSystemVersion indicatorSystemVersion);
}
