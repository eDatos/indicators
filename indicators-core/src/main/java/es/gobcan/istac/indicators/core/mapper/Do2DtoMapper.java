package es.gobcan.istac.indicators.core.mapper;

import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.Dimension;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.dto.serviceapi.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;

public interface Do2DtoMapper {
    
    // Indicators systems
	public IndicatorsSystemDto indicatorsSystemDoToDto(IndicatorsSystemVersion indicatorsSystemVersion);

	// Dimensions
    public DimensionDto dimensionDoToDto(Dimension dimension);

    // Indicators
    public IndicatorDto indicatorDoToDto(IndicatorVersion indicatorVersionCreated);

    // Data sources
    public DataSourceDto dataSourceDoToDto(DataSource dataSource);
}
