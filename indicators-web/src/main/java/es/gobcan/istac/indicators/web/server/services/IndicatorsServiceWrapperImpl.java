package es.gobcan.istac.indicators.web.server.services;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemStructureDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.QuantityUnitDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;

@Component
public class IndicatorsServiceWrapperImpl implements IndicatorsServiceWrapper {

    private static final Logger log = LoggerFactory.getLogger(IndicatorsServiceWrapperImpl.class);
    
    @Autowired
    private IndicatorsServiceFacade indicatorsService;
    
    @Override
    public IndicatorDto createIndicator(ServiceContext ctx, IndicatorDto indicatorDto) throws MetamacException {
        return indicatorsService.createIndicator(ctx, indicatorDto);
    }

    @Override
    public IndicatorDto updateIndicator(ServiceContext ctx, IndicatorDto indicatorDto) throws MetamacException {
        return indicatorsService.updateIndicator(ctx, indicatorDto);
    }
    
    @Override
    public IndicatorDto retrieveIndicator(ServiceContext ctx, String uuid) throws MetamacException {
        return indicatorsService.retrieveIndicator(ctx, uuid, null);
    }
    
    @Override
    public IndicatorDto retrieveIndicatorByCode(ServiceContext ctx, String code) throws MetamacException {
        return indicatorsService.retrieveIndicatorByCode(ctx, code, null);
    }

    @Override
    public void deleteIndicator(ServiceContext ctx, String uuid) throws MetamacException {
        indicatorsService.deleteIndicator(ctx, uuid);
    }

    /**
     * Sends indicator to production validation
     */
    /*public void sendIndicatorToProductionValidation(ServiceContext ctx, String uuid) throws MetamacException;*/

    /**
     * Sends indicator to diffusion validation
     */
    /*public void sendIndicatorToDiffusionValidation(ServiceContext ctx, String uuid) throws MetamacException;*/

    /**
     * Rejects validation of indicator
     */
    /*public void rejectIndicatorValidation(ServiceContext ctx, String uuid) throws MetamacException;*/

    /**
     * Publishes indicator
     */
    /*public void publishIndicator(ServiceContext ctx, String uuid) throws MetamacException {
     *      //TODO: Remember fire event updatePublishedIndicatorsCache
     * }
     */

    /**
     * Archives indicator
     */
   /* public void archiveIndicator(ServiceContext ctx, String uuid) throws MetamacException;*/

    /**
     * Creates a version on draft of an indicator in diffusion. Returns new version created
     */
    /*public IndicatorDto versioningIndicator(ServiceContext ctx, String uuid, VersiontTypeEnum versionType) throws MetamacException;*/

    @Override
    public List<IndicatorDto> findIndicators(ServiceContext ctx) throws MetamacException {
        return indicatorsService.findIndicators(ctx);
    }

    /**
     * Finds published indicators
     */
    /*public List<IndicatorDto> findIndicatorsPublished(ServiceContext ctx) throws MetamacException;*/

    /**
     * Creates a data source
     */
    /*public DataSourceDto createDataSource(ServiceContext ctx, String indicatorUuid, DataSourceDto dataSourceDto) throws MetamacException;*/

    /**
     * Updates metadata of data source
     */
    /*public void updateDataSource(ServiceContext ctx, DataSourceDto dataSourceDto) throws MetamacException;*/

    /**
     * Retrieves data source
     */
//    public DataSourceDto retrieveDataSource(ServiceContext ctx, String uuid) throws MetamacException;

    /**
     * Deletes data source
     */
//    public void deleteDataSource(ServiceContext ctx, String uuid) throws MetamacException;

    /**
     * Find data sources
     */
//    public List<DataSourceDto> findDataSources(ServiceContext ctx, String indicatorUuid, String indicatorVersionNumber) throws MetamacException;


    @Override
    public IndicatorsSystemDto updateIndicatorsSystem(ServiceContext ctx, IndicatorsSystemDto indicatorsSystemDto) throws MetamacException {
        return indicatorsService.updateIndicatorsSystem(ctx, indicatorsSystemDto);
    }

    /**
     * Retrieves an indicators system published
     */
//    public IndicatorsSystemDto retrieveIndicatorsSystemPublished(ServiceContext ctx, String uuid) throws MetamacException;

    @Override
    public IndicatorsSystemDto retrieveIndicatorsSystemByCode(ServiceContext ctx, String code) throws MetamacException {
        return indicatorsService.retrieveIndicatorsSystemByCode(ctx, code, null);
    }

    /**
     * Retrieves an indicators system by code. Retrieves diffusion version
     */
//    public IndicatorsSystemDto retrieveIndicatorsSystemPublishedByCode(ServiceContext ctx, String code) throws MetamacException;


    /**
     * Sends indicators system to production validation
     */
//    public void sendIndicatorsSystemToProductionValidation(ServiceContext ctx, String uuid) throws MetamacException;

    /**
     * Sends indicators system to diffusion validation
     */
//    public void sendIndicatorsSystemToDiffusionValidation(ServiceContext ctx, String uuid) throws MetamacException;

    /**
     * Rejects validation of indicators system
     */
//    public void rejectIndicatorsSystemValidation(ServiceContext ctx, String uuid) throws MetamacException;

    /**
     * Publishes indicators system
     */
//    public void publishIndicatorsSystem(ServiceContext ctx, String uuid) throws MetamacException;

    /**
     * Archives indicators system
     */
//    public void archiveIndicatorsSystem(ServiceContext ctx, String uuid) throws MetamacException;

    /**
     * Creates a version on draft of an indicators system in diffusion. Returns new version created
     */
//    public IndicatorsSystemDto versioningIndicatorsSystem(ServiceContext ctx, String uuid, VersiontTypeEnum versionType) throws MetamacException;

    /* (non-Javadoc)
     * @see es.gobcan.istac.indicators.web.server.services.IndicatorsSystemsServiceWrapper#findIndicatorsSystems(org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext)
     */
    @Override
    public List<IndicatorsSystemDto> findIndicatorsSystems(ServiceContext ctx) throws MetamacException {
        return indicatorsService.findIndicatorsSystems(ctx);
    }

    /**
     * Finds published indicators systems
     */
//    public List<IndicatorsSystemDto> findIndicatorsSystemsPublished(ServiceContext ctx) throws MetamacException;

    @Override
    public IndicatorsSystemStructureDto retrieveIndicatorsSystemStructureByCode(ServiceContext ctx, String code) throws MetamacException {
        IndicatorsSystemDto system = indicatorsService.retrieveIndicatorsSystemByCode(ctx,code,null);
        IndicatorsSystemStructureDto structure = null;
        if (!StringUtils.isEmpty(system.getUuid())) {
            structure = indicatorsService.retrieveIndicatorsSystemStructure(ctx, system.getUuid(), null);
        }
        return structure;
    }

    @Override
    public DimensionDto createDimension(ServiceContext ctx, IndicatorsSystemDto indicatorsSystemDto, DimensionDto dimensionDto) throws MetamacException {
        if (StringUtils.isEmpty(indicatorsSystemDto.getUuid())) {
            log.info("Operando sobre sistema sin persistir, se procede a persistir sistema: "+indicatorsSystemDto.getCode());
            indicatorsSystemDto = indicatorsService.createIndicatorsSystem(ctx, indicatorsSystemDto);
        }
        return indicatorsService.createDimension(ctx,indicatorsSystemDto.getUuid(), dimensionDto);
    }

    @Override
    public DimensionDto updateDimension(ServiceContext ctx, DimensionDto dimensionDto) throws MetamacException {
        return indicatorsService.updateDimension(ctx, dimensionDto);
    }

    @Override
    public DimensionDto updateDimensionLocation(ServiceContext ctx, String uuid, String parentTargetUuid, Long orderInLevel) throws MetamacException {
        return indicatorsService.updateDimensionLocation(ctx, uuid, parentTargetUuid, orderInLevel);
    }

    @Override
    public void deleteDimension(ServiceContext ctx, String uuid) throws MetamacException {
        indicatorsService.deleteDimension(ctx, uuid);
    }

    @Override
    public IndicatorInstanceDto createIndicatorInstance(ServiceContext ctx, IndicatorsSystemDto indicatorsSystemDto, IndicatorInstanceDto indicatorInstanceDto) throws MetamacException {
        if (StringUtils.isEmpty(indicatorsSystemDto.getUuid())) {
            log.info("Working without persisting. Indicator with code " + indicatorsSystemDto.getCode() + " is going to be persisted");
            indicatorsSystemDto = indicatorsService.createIndicatorsSystem(ctx, indicatorsSystemDto);
        }
        return indicatorsService.createIndicatorInstance(ctx,indicatorsSystemDto.getUuid(), indicatorInstanceDto);
    }

    @Override
    public IndicatorInstanceDto updateIndicatorInstance(ServiceContext ctx, IndicatorInstanceDto indicatorInstanceDto) throws MetamacException {
        return indicatorsService.updateIndicatorInstance(ctx, indicatorInstanceDto);
    }

    @Override
    public IndicatorInstanceDto updateIndicatorInstanceLocation(ServiceContext ctx, String uuid, String parentTargetUuid, Long orderInLevel) throws MetamacException {
        return indicatorsService.updateIndicatorInstanceLocation(ctx, uuid, parentTargetUuid, orderInLevel);
    }

    @Override
    public void deleteIndicatorInstance(ServiceContext ctx, String uuid) throws MetamacException {
        indicatorsService.deleteIndicatorInstance(ctx, uuid);
    }

    @Override
    public List<QuantityUnitDto> getQuantityUnits(ServiceContext ctx) throws MetamacException {
        return indicatorsService.findQuantityUnits(ctx);
    }

    @Override
    public List<GeographicalGranularityDto> getGeographicalGranularities(ServiceContext ctx) throws MetamacException {
        return indicatorsService.findGeographicalGranularities(ctx);
    }

    @Override
    public List<GeographicalValueDto> getGeographicalValues(ServiceContext ctx, String geographicalGranularityUuid) throws MetamacException {
        return indicatorsService.findGeographicalValues(ctx, geographicalGranularityUuid);
    }

}
