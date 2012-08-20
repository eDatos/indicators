package es.gobcan.istac.indicators.rest.clients;

import org.siemac.metamac.rest.statistical_operations.v1_0.domain.Operation;

public interface StatisticalOperationsRestInternalFacade {

    /**
     * Retrieves operation
     */
    public Operation retrieveOperationById(String operationCode) throws Exception;

}
