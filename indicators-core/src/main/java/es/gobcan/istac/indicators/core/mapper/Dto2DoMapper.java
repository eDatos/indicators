package es.gobcan.istac.indicators.core.mapper;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.Dimension;
import es.gobcan.istac.indicators.core.domain.Indicator;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystem;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.dto.serviceapi.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;

public interface Dto2DoMapper {

    
    // TODO no recibe target
    
    // Indicators systems
    public IndicatorsSystem indicatorsSystemDtoToDo(ServiceContext ctx, IndicatorsSystemDto source, IndicatorsSystem target) throws MetamacException;
    public IndicatorsSystemVersion indicatorsSystemDtoToDo(ServiceContext ctx, IndicatorsSystemDto source, IndicatorsSystemVersion target) throws MetamacException;
    public IndicatorsSystemVersion indicatorsSystemDtoToDo(ServiceContext ctx, IndicatorsSystemDto source) throws MetamacException;
    
    // Dimensions
    public Dimension dimensionDtoToDo(ServiceContext ctx, DimensionDto dimensionDto) throws MetamacException;
    public void dimensionDtoToDo(ServiceContext ctx, DimensionDto source, Dimension target) throws MetamacException;
    
    // Indicators instances
    public IndicatorInstance indicatorInstanceDtoToDo(ServiceContext ctx, IndicatorInstanceDto source) throws MetamacException;
    public void indicatorInstanceDtoToDo(ServiceContext ctx, IndicatorInstanceDto source, IndicatorInstance target) throws MetamacException;
    
    // Indicators
    public Indicator indicatorDtoToDo(ServiceContext ctx, IndicatorDto source, Indicator target) throws MetamacException;
    public IndicatorVersion indicatorDtoToDo(ServiceContext ctx, IndicatorDto source, IndicatorVersion target) throws MetamacException;
    public IndicatorVersion indicatorDtoToDo(ServiceContext ctx, IndicatorDto source) throws MetamacException;
    
    // Data sources
    public DataSource dataSourceDtoToDo(ServiceContext ctx, DataSourceDto source) throws MetamacException;
}
