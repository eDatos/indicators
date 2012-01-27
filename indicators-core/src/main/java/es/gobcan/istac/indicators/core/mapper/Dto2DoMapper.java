package es.gobcan.istac.indicators.core.mapper;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.core.domain.IndicatorSystem;
import es.gobcan.istac.indicators.core.domain.IndicatorSystemVersion;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorSystemDto;

public interface Dto2DoMapper {

    public IndicatorSystem indicatorSystemDtoToDo(IndicatorSystemDto source, IndicatorSystem target, ServiceContext ctx) throws MetamacException;
    public IndicatorSystemVersion indicatorSystemDtoToDo(IndicatorSystemDto source, IndicatorSystemVersion target, ServiceContext ctx) throws MetamacException;
    public IndicatorSystemVersion indicatorSystemDtoToDo(IndicatorSystemDto source, ServiceContext ctx) throws MetamacException;
}
