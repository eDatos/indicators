package es.gobcan.istac.indicators.core.mapper;

import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.core.domain.Dimension;
import es.gobcan.istac.indicators.core.domain.Indicator;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystem;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;

public interface Dto2DoMapper {

    // Indicators systems
    public IndicatorsSystem indicatorsSystemDtoToDo(IndicatorsSystemDto source, IndicatorsSystem target) throws MetamacException;
    public IndicatorsSystemVersion indicatorsSystemDtoToDo(IndicatorsSystemDto source, IndicatorsSystemVersion target) throws MetamacException;
    public IndicatorsSystemVersion indicatorsSystemDtoToDo(IndicatorsSystemDto source) throws MetamacException;
    
    // Dimensions
    public Dimension dimensionDtoToDo(DimensionDto source);
    public void dimensionDtoToDo(DimensionDto source, Dimension target);
    
    // Indicators
    public Indicator indicatorDtoToDo(IndicatorDto source, Indicator target) throws MetamacException;
    public IndicatorVersion indicatorDtoToDo(IndicatorDto source, IndicatorVersion target) throws MetamacException;
    public IndicatorVersion indicatorDtoToDo(IndicatorDto source) throws MetamacException;
}
