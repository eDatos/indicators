package es.gobcan.istac.indicators.rest.clients;

import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.rest.clients.adapters.OperationIndicators;

public interface StatisticalOperationsRestInternalFacade {

    /**
     * Retrieves operation
     */
    OperationIndicators retrieveOperationById(String operationCode) throws MetamacException;

}
