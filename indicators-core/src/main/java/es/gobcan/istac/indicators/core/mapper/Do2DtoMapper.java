package es.gobcan.istac.indicators.core.mapper;

import es.gobcan.istac.indicators.core.domain.Dimension;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;

public interface Do2DtoMapper {
    
	public IndicatorsSystemDto indicatorsSystemDoToDto(IndicatorsSystemVersion indicatorsSystemVersion);

    public DimensionDto dimensionDoToDto(Dimension dimension);
}
