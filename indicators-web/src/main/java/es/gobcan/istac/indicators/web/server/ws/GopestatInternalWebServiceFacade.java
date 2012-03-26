package es.gobcan.istac.indicators.web.server.ws;

import org.siemac.metamac.gopestat.internal.ws.v1_0.domain.OperationBase;
import org.siemac.metamac.gopestat.internal.ws.v1_0.domain.OperationBaseList;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

public interface GopestatInternalWebServiceFacade {

    /**
     * Retrieves operation
     */
    public OperationBase retrieveOperation(String operationCode) throws MetamacWebException;

    /**
     * Finds operations of type "indicators system"
     */
    public OperationBaseList findOperationsIndicatorsSystem() throws MetamacWebException;
}