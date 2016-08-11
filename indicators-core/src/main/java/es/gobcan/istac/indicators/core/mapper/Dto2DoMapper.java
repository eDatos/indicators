package es.gobcan.istac.indicators.core.mapper;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.core.domain.Data;
import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.Dimension;
import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.QuantityUnit;
import es.gobcan.istac.indicators.core.domain.UnitMultiplier;
import es.gobcan.istac.indicators.core.dto.DataDto;
import es.gobcan.istac.indicators.core.dto.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.DimensionDto;
import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.dto.QuantityUnitDto;
import es.gobcan.istac.indicators.core.dto.UnitMultiplierDto;

public interface Dto2DoMapper {

    // Indicators systems
    IndicatorsSystemVersion indicatorsSystemDtoToDo(ServiceContext ctx, IndicatorsSystemDto source) throws MetamacException;

    // Dimensions
    Dimension dimensionDtoToDo(ServiceContext ctx, DimensionDto dimensionDto) throws MetamacException;

    // Indicators instances
    IndicatorInstance indicatorInstanceDtoToDo(ServiceContext ctx, IndicatorInstanceDto source) throws MetamacException;

    // Indicators
    IndicatorVersion indicatorDtoToDo(ServiceContext ctx, IndicatorDto source) throws MetamacException;

    // Data sources
    DataSource dataSourceDtoToDo(ServiceContext ctx, DataSourceDto source) throws MetamacException;

    // Data
    Data dataDtoToDo(ServiceContext ctx, DataDto source);

    // International string
    InternationalString internationalStringDtoToDo(ServiceContext ctx, InternationalStringDto source, InternationalString target) throws MetamacException;

    // Quantity Units
    QuantityUnit quantityUnitDtoToDo(ServiceContext ctx, QuantityUnitDto source) throws MetamacException;

    // Geographical Value
    GeographicalValue geographicalValueDtoToDo(ServiceContext ctx, GeographicalValueDto source) throws MetamacException;

    // Geographical Granularity
    GeographicalGranularity geographicalGranularityDtoToDo(ServiceContext ctx, GeographicalGranularityDto source) throws MetamacException;

    // Unit Multipliers
    UnitMultiplier unitMultiplierDtoToDo(ServiceContext ctx, UnitMultiplierDto source) throws MetamacException;
}
