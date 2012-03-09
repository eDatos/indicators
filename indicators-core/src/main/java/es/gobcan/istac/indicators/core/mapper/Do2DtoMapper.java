package es.gobcan.istac.indicators.core.mapper;

import java.util.List;

import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.Dimension;
import es.gobcan.istac.indicators.core.domain.ElementLevel;
import es.gobcan.istac.indicators.core.domain.GeographicGranularity;
import es.gobcan.istac.indicators.core.domain.GeographicValue;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.QuantityUnit;
import es.gobcan.istac.indicators.core.dto.serviceapi.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.ElementLevelDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.GeographicGranularityDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.GeographicValueDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.QuantityUnitDto;

public interface Do2DtoMapper {
    
    // Indicators systems
	public IndicatorsSystemDto indicatorsSystemDoToDto(IndicatorsSystemVersion source);

	// Dimensions
    public DimensionDto dimensionDoToDto(Dimension source);

    // Indicators instances
    public IndicatorInstanceDto indicatorInstanceDoToDto(IndicatorInstance source);
    
    // Indicators
    public IndicatorDto indicatorDoToDto(IndicatorVersion source);

    // Data sources
    public DataSourceDto dataSourceDoToDto(DataSource source);

    // Elements levels
    public List<ElementLevelDto> elementsLevelsDoToDto(List<ElementLevel> sources);
    
    // Quantity unit
    public QuantityUnitDto quantityUnitDoToDto(QuantityUnit source);

    // Geographic value
    public GeographicValueDto geographicValueDoToDto(GeographicValue source);

    // Geographic granularity
    public GeographicGranularityDto geographicGranularityDoToDto(GeographicGranularity source);
}
