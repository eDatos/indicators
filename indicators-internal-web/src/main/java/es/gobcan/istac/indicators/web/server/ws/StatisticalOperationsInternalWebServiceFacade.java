package es.gobcan.istac.indicators.web.server.ws;

import org.siemac.metamac.statistical.operations.internal.ws.v1_0.domain.FindOperationsResult;
import org.siemac.metamac.statistical.operations.internal.ws.v1_0.domain.OperationBase;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

public interface StatisticalOperationsInternalWebServiceFacade {

    /**
     * Retrieves operation
     */
    public OperationBase retrieveOperation(String operationCode) throws MetamacWebException;

    /**
     * Finds operations of type "indicators system"
     */
    public FindOperationsResult findOperationsIndicatorsSystem(int firstResult, int maxResult) throws MetamacWebException;
}