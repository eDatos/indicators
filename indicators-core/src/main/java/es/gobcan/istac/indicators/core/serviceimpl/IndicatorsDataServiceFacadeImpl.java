package es.gobcan.istac.indicators.core.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.domain.DataDefinition;
import es.gobcan.istac.indicators.core.domain.DataStructure;
import es.gobcan.istac.indicators.core.dto.serviceapi.DataDefinitionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.DataDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.DataStructureDto;
import es.gobcan.istac.indicators.core.mapper.Do2DtoMapper;

/**
 * Implementation of IndicatorsDataServiceFacade.
 */
@Service("indicatorsDataServiceFacade")
public class IndicatorsDataServiceFacadeImpl extends IndicatorsDataServiceFacadeImplBase {
    @Autowired
    private Do2DtoMapper do2DtoMapper;

    public IndicatorsDataServiceFacadeImpl() {
    }

    @Override
    public List<DataDefinitionDto> findDataDefinitions(ServiceContext ctx) throws MetamacException {
        // Service call
        List<DataDefinition> dataDefs = getIndicatorsDataService().findDataDefinitions(ctx);
        
        //Transform
        List<DataDefinitionDto> dtos = new ArrayList<DataDefinitionDto>();
        for (DataDefinition basic : dataDefs) {
            dtos.add(do2DtoMapper.dataDefinitionDoToDto(basic));
        }
        return dtos;
    }
    
    @Override
    public DataDefinitionDto retrieveDataDefinition(ServiceContext ctx,String uuid) throws MetamacException {
        // Service call
        DataDefinition dataDef = getIndicatorsDataService().retrieveDataDefinition(ctx,uuid);
        
        //Transform
        DataDefinitionDto dto = null;
        if (dataDef != null) {
            dto = do2DtoMapper.dataDefinitionDoToDto(dataDef);
        }
        return dto;
    }

    @Override
    public DataStructureDto retrieveDataStructure(ServiceContext ctx, String uuid) throws MetamacException {
        //Service call
        DataStructure dataStruc = getIndicatorsDataService().retrieveDataStructure(ctx, uuid);
        
        //Transform
        return do2DtoMapper.dataStructureDoToDto(dataStruc);
    }

    @Override
    public void populateIndicatorData(ServiceContext ctx, String indicatorUuid, String version) throws MetamacException {
        getIndicatorsDataService().populateIndicatorData(ctx, indicatorUuid, version);
    }
}
