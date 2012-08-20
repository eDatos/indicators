package es.gobcan.istac.indicators.rest.clients;

import org.siemac.metamac.rest.statistical_operations.v1_0.domain.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("statisticalOperationsRestInternalFacade")
public class StatisticalOperationsRestInternalFacadeImpl implements StatisticalOperationsRestInternalFacade {

    @Autowired
    private RestApiLocator restApiLocator;

    @Override
    public Operation retrieveOperationById(String operationCode) throws Exception {
        return restApiLocator.getStatisticalOperationsRestFacadeV10().retrieveOperationById(operationCode);
    }
}
