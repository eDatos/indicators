package es.gobcan.istac.indicators.rest.clients;

import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.rest.clients.adapters.OperationIndicators;
import es.gobcan.istac.indicators.rest.mapper.MapperUtil;

public class StatisticalOperationsInternalRestInternalFacadeImpl implements StatisticalOperationsRestInternalFacade {

    @Autowired
    private RestApiLocatorInternal restApiLocator;

    @Override
    public OperationIndicators retrieveOperationById(String operationCode) throws Exception {
        Operation operation = restApiLocator.getStatisticalOperationsRestFacadeV10().retrieveOperationById(operationCode);
        return MapperUtil.getOperationIndicators(operation);
    }
}
