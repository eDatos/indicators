package es.gobcan.istac.indicators.core.mapper;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.core.domain.Dimension;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystem;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;

public interface Dto2DoMapper {

    public IndicatorsSystem indicatorsSystemDtoToDo(IndicatorsSystemDto source, IndicatorsSystem target, ServiceContext ctx) throws MetamacException;
    public IndicatorsSystemVersion indicatorsSystemDtoToDo(IndicatorsSystemDto source, IndicatorsSystemVersion target, ServiceContext ctx) throws MetamacException;
    public IndicatorsSystemVersion indicatorsSystemDtoToDo(IndicatorsSystemDto source, ServiceContext ctx) throws MetamacException;
    
    public Dimension dimensionDtoToDo(DimensionDto dimensionDto);
    public void dimensionDtoToDo(DimensionDto dimensionDto, Dimension dimension);
}
