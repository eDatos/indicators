package es.gobcan.istac.indicators.rest.clients;

import es.gobcan.istac.indicators.rest.clients.adapters.OperationIndicators;

public interface StatisticalOperationsRestInternalFacade {

    /**
     * Retrieves operation
     */
    public OperationIndicators retrieveOperationById(String operationCode) throws Exception;

}
