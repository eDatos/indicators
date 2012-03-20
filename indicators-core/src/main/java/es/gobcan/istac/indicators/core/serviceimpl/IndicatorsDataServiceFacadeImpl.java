package es.gobcan.istac.indicators.core.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.domain.DataBasic;
import es.gobcan.istac.indicators.core.domain.DataStructure;
import es.gobcan.istac.indicators.core.dto.serviceapi.DataBasicDto;
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
    public List<DataBasicDto> findDataDefinitions(ServiceContext ctx) throws MetamacException {
        // Service call
        List<DataBasic> dataDefs = getIndicatorsDataService().findDataDefinitions(ctx);
        
        //Transform
        List<DataBasicDto> dtos = new ArrayList<DataBasicDto>();
        for (DataBasic basic : dataDefs) {
            dtos.add(do2DtoMapper.dataBasicDoToDto(basic));
        }
        return dtos;
    }
    
    @Override
    public DataBasicDto findDataDefinition(ServiceContext ctx,String uuid) throws MetamacException {
        // Service call
        DataBasic dataDef = getIndicatorsDataService().findDataDefinition(ctx,uuid);
        
        //Transform
        DataBasicDto dto = null;
        if (dataDef != null) {
            dto = do2DtoMapper.dataBasicDoToDto(dataDef);
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
    public DataDto retrieveData(ServiceContext ctx, String uuid) throws MetamacException {
        // TODO Auto-generated method stub
        return null;
    }
}
