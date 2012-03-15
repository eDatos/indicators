package es.gobcan.istac.indicators.core.mapper;

import java.util.List;

import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.Dimension;
import es.gobcan.istac.indicators.core.domain.ElementLevel;
import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.QuantityUnit;
import es.gobcan.istac.indicators.core.domain.Subject;
import es.gobcan.istac.indicators.core.dto.serviceapi.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.ElementLevelDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.QuantityUnitDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.SubjectDto;
import es.gobcan.istac.indicators.core.repositoryimpl.finders.SubjectIndicatorResult;

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

    // Geographical value
    public GeographicalValueDto geographicalValueDoToDto(GeographicalValue source);

    // Geographical granularity
    public GeographicalGranularityDto geographicalGranularityDoToDto(GeographicalGranularity source);

    // Subject
    public SubjectDto subjectDoToDto(Subject source);
    public SubjectDto subjectDoToDto(SubjectIndicatorResult source);
}
