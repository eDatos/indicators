package es.gobcan.istac.indicators.web.server.services;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemStructureDto;

public interface IndicatorsServiceWrapper {

    /**
     * Creates an indicator
     */
    public abstract IndicatorDto createIndicator(ServiceContext ctx, IndicatorDto indicatorDto) throws MetamacException;
    
    /**
     * Retrieves an indicator. If versionNumber is not provided, retrieves last version
     */
    public IndicatorDto retrieveIndicator(ServiceContext ctx, String code) throws MetamacException;
    
    /**
     * Retrieves an indicator. If versionNumber is not provided, retrieves last version
     */
    public IndicatorDto retrieveIndicatorByCode(ServiceContext ctx, String code) throws MetamacException;

    /**
     * Updates metadata of an indicator. This version can not be published or archived
     */
    public abstract void updateIndicator(ServiceContext ctx, IndicatorDto indicatorDto) throws MetamacException;

    /**
     * Deletes a version of an indicator. Version to remove must be not published nor archived
     */
    public abstract void deleteIndicator(ServiceContext ctx, String uuid) throws MetamacException;

    /**
     * Finds indicators. Retrieves last versions
     */
    public abstract List<IndicatorDto> findIndicators(ServiceContext ctx) throws MetamacException;

    /**
     * Updates metadata of an indicators system. This version can not be published or archived
     */
    public abstract void updateIndicatorsSystem(ServiceContext ctx, IndicatorsSystemDto indicatorsSystemDto) throws MetamacException;

    /**
     * Retrieves an indicators system by code. Retrieves last version
     */
    public abstract IndicatorsSystemDto retrieveIndicatorsSystemByCode(ServiceContext ctx, String code) throws MetamacException;

    /**
     * Finds indicators systems. Retrieves last versions
     */
    public abstract List<IndicatorsSystemDto> findIndicatorsSystems(ServiceContext ctx) throws MetamacException;

    /**
     * Retrieves indicators system structure: dimensions and indicators instances by levels. If versionNumber is not provided, retrieves last version
     */
    public abstract IndicatorsSystemStructureDto retrieveIndicatorsSystemStructureByCode(ServiceContext ctx, String code) throws MetamacException;

    /**
     * Creates a dimension
     */
    public abstract DimensionDto createDimension(ServiceContext ctx, IndicatorsSystemDto indicatorsSystem, DimensionDto dimensionDto) throws MetamacException;

    /**
     * Updates metadata of dimension
     */
    public abstract void updateDimension(ServiceContext ctx, DimensionDto dimensionDto) throws MetamacException;

    /**
     * Updates the location of dimension
     */
    public abstract void updateDimensionLocation(ServiceContext ctx, String uuid, String parentTargetUuid, Long orderInLevel) throws MetamacException;

    /**
     * Deletes dimension
     */
    public abstract void deleteDimension(ServiceContext ctx, String uuid) throws MetamacException;

    /**
     * Creates a indicator instance
     */
    public abstract IndicatorInstanceDto createIndicatorInstance(ServiceContext ctx, IndicatorsSystemDto indicatorsSystemDto, IndicatorInstanceDto indicatorInstanceDto) throws MetamacException;

    /**
     * Updates metadata of indicator instance
     */
    public abstract void updateIndicatorInstance(ServiceContext ctx, IndicatorInstanceDto indicatorInstanceDto) throws MetamacException;

    /**
     * Updates the location of indicator instance
     */
    public abstract void updateIndicatorInstanceLocation(ServiceContext ctx, String uuid, String parentTargetUuid, Long orderInLevel) throws MetamacException;

    /**
     * Deletes indicator instance
     */
    public abstract void deleteIndicatorInstance(ServiceContext ctx, String uuid) throws MetamacException;

}