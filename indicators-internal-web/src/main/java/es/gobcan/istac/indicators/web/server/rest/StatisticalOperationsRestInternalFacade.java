package es.gobcan.istac.indicators.web.server.rest;

import org.siemac.metamac.statistical_operations.rest.internal.v1_0.domain.Operation;
import org.siemac.metamac.statistical_operations.rest.internal.v1_0.domain.Operations;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

public interface StatisticalOperationsRestInternalFacade {

    /**
     * Retrieves operation
     */
    public Operation retrieveOperation(String operationCode) throws MetamacWebException;

    /**
     * Finds operations
     */
    public Operations findOperationsIndicatorsSystem(int firstResult, int maxResult) throws MetamacWebException;

}
