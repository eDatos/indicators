package es.gobcan.istac.indicators.core.mapper;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.core.domain.Data;
import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.Dimension;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.dto.DataDto;
import es.gobcan.istac.indicators.core.dto.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.DimensionDto;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;

public interface Dto2DoMapper {

    // Indicators systems
    public IndicatorsSystemVersion indicatorsSystemDtoToDo(ServiceContext ctx, IndicatorsSystemDto source) throws MetamacException;
    
    // Dimensions
    public Dimension dimensionDtoToDo(ServiceContext ctx, DimensionDto dimensionDto) throws MetamacException;
    
    // Indicators instances
    public IndicatorInstance indicatorInstanceDtoToDo(ServiceContext ctx, IndicatorInstanceDto source) throws MetamacException;
    
    // Indicators
    public IndicatorVersion indicatorDtoToDo(ServiceContext ctx, IndicatorDto source) throws MetamacException;
    
    // Data sources
    public DataSource dataSourceDtoToDo(ServiceContext ctx, DataSourceDto source) throws MetamacException;
    
    //Data 
    public Data dataDtoToDo(ServiceContext ctx, DataDto source);
}
