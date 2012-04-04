package es.gobcan.istac.indicators.web.server.services;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemStructureDto;

public interface IndicatorsServiceWrapper {

    /**
     * Retrieves indicators system structure: dimensions and indicators instances by levels. If versionNumber is not provided, retrieves last version
     */
    public IndicatorsSystemStructureDto retrieveIndicatorsSystemStructureByCode(ServiceContext ctx, String code) throws MetamacException;

    /**
     * Creates a dimension
     */
    public DimensionDto createDimension(ServiceContext ctx, IndicatorsSystemDto indicatorsSystem, DimensionDto dimensionDto) throws MetamacException;

    /**
     * Creates a indicator instance
     */
    public IndicatorInstanceDto createIndicatorInstance(ServiceContext ctx, IndicatorsSystemDto indicatorsSystemDto, IndicatorInstanceDto indicatorInstanceDto) throws MetamacException;

}