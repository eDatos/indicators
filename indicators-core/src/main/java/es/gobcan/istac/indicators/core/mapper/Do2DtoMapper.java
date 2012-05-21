package es.gobcan.istac.indicators.core.mapper;

import java.util.List;

import es.gobcan.istac.indicators.core.domain.Data;
import es.gobcan.istac.indicators.core.domain.DataDefinition;
import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.DataStructure;
import es.gobcan.istac.indicators.core.domain.Dimension;
import es.gobcan.istac.indicators.core.domain.ElementLevel;
import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.QuantityUnit;
import es.gobcan.istac.indicators.core.domain.Subject;
import es.gobcan.istac.indicators.core.domain.TimeGranularity;
import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.dto.DataDefinitionDto;
import es.gobcan.istac.indicators.core.dto.DataDto;
import es.gobcan.istac.indicators.core.dto.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.DataStructureDto;
import es.gobcan.istac.indicators.core.dto.DimensionDto;
import es.gobcan.istac.indicators.core.dto.ElementLevelDto;
import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.IndicatorSummaryDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemSummaryDto;
import es.gobcan.istac.indicators.core.dto.QuantityUnitDto;
import es.gobcan.istac.indicators.core.dto.SubjectDto;
import es.gobcan.istac.indicators.core.dto.TimeGranularityDto;
import es.gobcan.istac.indicators.core.dto.TimeValueDto;
import es.gobcan.istac.indicators.core.repositoryimpl.finders.SubjectIndicatorResult;

public interface Do2DtoMapper {

    // Indicators systems
    public IndicatorsSystemDto indicatorsSystemDoToDto(IndicatorsSystemVersion source);
    public IndicatorsSystemSummaryDto indicatorsSystemDoToDtoSummary(IndicatorsSystemVersion source);

    // Dimensions
    public DimensionDto dimensionDoToDto(Dimension source);

    // Indicators instances
    public IndicatorInstanceDto indicatorInstanceDoToDto(IndicatorInstance source);

    // Indicators
    public IndicatorDto indicatorDoToDto(IndicatorVersion source);
    public IndicatorSummaryDto indicatorDoToDtoSummary(IndicatorVersion source);

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

    // Time value
    public TimeGranularityDto timeGranularityDoToTimeGranularityDto(TimeGranularity source);

    // Time granularity
    public TimeValueDto timeValueDoToTimeValueDto(TimeValue source);

    // Subject
    public SubjectDto subjectDoToDto(Subject source);
    public SubjectDto subjectDoToDto(SubjectIndicatorResult source);

    // DataDefinition
    public DataDefinitionDto dataDefinitionDoToDto(DataDefinition source);

    // DataStructure
    public DataStructureDto dataStructureDoToDto(DataStructure source);

    // Data
    public DataDto dataDoToDto(Data source);
}
