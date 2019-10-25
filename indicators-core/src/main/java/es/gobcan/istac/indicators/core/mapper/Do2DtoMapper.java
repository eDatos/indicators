package es.gobcan.istac.indicators.core.mapper;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.ent.domain.ExternalItem;
import org.siemac.metamac.core.common.exception.MetamacException;

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
import es.gobcan.istac.indicators.core.domain.UnitMultiplier;
import es.gobcan.istac.indicators.core.dto.DataDefinitionDto;
import es.gobcan.istac.indicators.core.dto.DataDto;
import es.gobcan.istac.indicators.core.dto.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.DataStructureDto;
import es.gobcan.istac.indicators.core.dto.DimensionDto;
import es.gobcan.istac.indicators.core.dto.ElementLevelDto;
import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.GeographicalValueBaseDto;
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
import es.gobcan.istac.indicators.core.dto.UnitMultiplierDto;
import es.gobcan.istac.indicators.core.repositoryimpl.finders.SubjectIndicatorResult;

public interface Do2DtoMapper extends CommonDo2DtoMapper {

    // Indicators systems
    IndicatorsSystemDto indicatorsSystemDoToDto(IndicatorsSystemVersion source);
    IndicatorsSystemSummaryDto indicatorsSystemDoToDtoSummary(IndicatorsSystemVersion source);

    // Dimensions
    DimensionDto dimensionDoToDto(Dimension source);

    // Indicators instances
    IndicatorInstanceDto indicatorInstanceDoToDto(IndicatorInstance source);

    // Indicators
    IndicatorDto indicatorDoToDto(ServiceContext ctx, IndicatorVersion source) throws MetamacException;
    IndicatorSummaryDto indicatorDoToDtoSummary(ServiceContext ctx, IndicatorVersion source) throws MetamacException;

    // Data sources
    DataSourceDto dataSourceDoToDto(DataSource source) throws MetamacException;

    // Elements levels
    List<ElementLevelDto> elementsLevelsDoToDto(List<ElementLevel> sources);

    // Quantity unit
    QuantityUnitDto quantityUnitDoToDto(QuantityUnit source);

    // Geographical value
    GeographicalValueDto geographicalValueDoToDto(GeographicalValue source);

    // Geographical value Base
    GeographicalValueBaseDto geographicalValueDoToBaseDto(GeographicalValue source);

    // Geographical granularity
    GeographicalGranularityDto geographicalGranularityDoToDto(GeographicalGranularity source);

    // Time value
    TimeGranularityDto timeGranularityDoToTimeGranularityDto(TimeGranularity source);

    // Time granularity
    TimeValueDto timeValueDoToTimeValueDto(TimeValue source);

    // Subject
    SubjectDto subjectDoToDto(Subject source);
    SubjectDto subjectDoToDto(SubjectIndicatorResult source);

    // DataDefinition
    DataDefinitionDto dataDefinitionDoToDto(DataDefinition source);

    // DataStructure
    DataStructureDto dataStructureDoToDto(DataStructure source);

    // Data
    DataDto dataDoToDto(Data source);

    // Unit multipliers
    UnitMultiplierDto unitMultiplierDoToDto(UnitMultiplier source);

    // External Item
    ExternalItemDto externalItemDoToDto(ExternalItem source) throws MetamacException;
}
