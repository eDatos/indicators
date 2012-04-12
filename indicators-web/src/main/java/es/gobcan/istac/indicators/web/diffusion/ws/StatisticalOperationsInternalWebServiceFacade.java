package es.gobcan.istac.indicators.web.diffusion.ws;

import org.siemac.metamac.statistical.operations.internal.ws.v1_0.MetamacExceptionFault;
import org.siemac.metamac.statistical.operations.internal.ws.v1_0.domain.OperationBase;
import org.siemac.metamac.statistical.operations.internal.ws.v1_0.domain.OperationBaseList;

public interface StatisticalOperationsInternalWebServiceFacade {

    /**
     * Retrieves operation
     */
    public OperationBase retrieveOperation(String operationCode) throws MetamacExceptionFault;

    /**
     * Finds operations of type "indicators system"
     */
    public OperationBaseList findOperationsIndicatorsSystem() throws MetamacExceptionFault;
}