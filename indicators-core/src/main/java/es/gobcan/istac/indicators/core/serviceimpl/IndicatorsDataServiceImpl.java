package es.gobcan.istac.indicators.core.serviceimpl;

import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.domain.Data;
import es.gobcan.istac.indicators.core.domain.DataDefinition;
import es.gobcan.istac.indicators.core.domain.DataStructure;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsDataProviderService;
import es.gobcan.istac.indicators.core.serviceimpl.util.InvocationValidator;

/**
 * Implementation of IndicatorsDataService.
 */
@Service("indicatorsDataService")
public class IndicatorsDataServiceImpl extends IndicatorsDataServiceImplBase {

    @Autowired
    private IndicatorsDataProviderService indicatorsDataProviderService;
    
    public IndicatorsDataServiceImpl() {
    }
    
    
    public void setIndicatorsDataProviderService(IndicatorsDataProviderService indicatorsDataProviderService) {
        this.indicatorsDataProviderService = indicatorsDataProviderService;
    }

    @Override
    public List<DataDefinition> findDataDefinitions(ServiceContext ctx) throws MetamacException {
        // Validation
        InvocationValidator.checkFindDataDefinitions();
        
        // Find db
        List<DataDefinition> dataDefinitions = getDataGpeRepository().findCurrentDataDefinitions();
        return dataDefinitions;
    }
    
    @Override
    public DataDefinition retrieveDataDefinition(ServiceContext ctx, String uuid) throws MetamacException {
        // Validation
        InvocationValidator.checkFindDataDefinition(uuid);
        
        // Find db
        DataDefinition dataDefinition = getDataGpeRepository().findCurrentDataDefinition(uuid);
        return dataDefinition;
    }

    @Override
    public DataStructure retrieveDataStructure(ServiceContext ctx, String uuid) throws MetamacException {
        try {
            //Validation
            InvocationValidator.checkRetrieveDataStructure(uuid);
            //Call jaxi for query structure
            String json = indicatorsDataProviderService.retrieveDataStructureJson(ctx, uuid);
            return jsonToDataStructure(json);
        } catch (Exception e) {
            //TODO: We must keep e stacktrace somehow, logging exception or adding cause to metamacexception
            throw new MetamacException(ServiceExceptionType.DATA_STRUCTURE_RETRIEVE_ERROR, uuid);
        }
    }

    @Override
    public Data retrieveData(ServiceContext ctx, String uuid) throws MetamacException {
        //Validation
        InvocationValidator.checkRetrieveDataStructure(uuid);
        // TODO Auto-generated method stub
        return null;
    }
    
    /*
     * Private methods that get info from jaxi
     */
    private DataStructure jsonToDataStructure(String json) throws Exception {
        DataStructure target = new DataStructure();
        ObjectMapper mapper = new ObjectMapper();
        target = mapper.readValue(json, DataStructure.class);
        return target;
    }
}
