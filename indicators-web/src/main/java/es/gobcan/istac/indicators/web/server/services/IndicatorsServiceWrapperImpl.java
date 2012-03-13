package es.gobcan.istac.indicators.web.server.services;

import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemStructureDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;

@Component
public class IndicatorsServiceWrapperImpl implements IndicatorsServiceWrapper {

    private static final Logger log = LoggerFactory.getLogger(IndicatorsServiceWrapperImpl.class);
    
    @Autowired
    private IndicatorsServiceFacade indicatorsService;
    
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
    public IndicatorInstanceDto createIndicatorInstance(ServiceContext ctx, IndicatorsSystemDto indicatorsSystemDto, IndicatorInstanceDto indicatorInstanceDto) throws MetamacException {
        if (StringUtils.isEmpty(indicatorsSystemDto.getUuid())) {
            log.info("Working without persisting. Indicator with code " + indicatorsSystemDto.getCode() + " is going to be persisted");
            indicatorsSystemDto = indicatorsService.createIndicatorsSystem(ctx, indicatorsSystemDto);
        }
        return indicatorsService.createIndicatorInstance(ctx,indicatorsSystemDto.getUuid(), indicatorInstanceDto);
    }

}
