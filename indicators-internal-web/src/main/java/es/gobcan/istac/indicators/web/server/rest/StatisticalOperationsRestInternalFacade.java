package es.gobcan.istac.indicators.web.server.rest;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operation;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operations;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;
import org.siemac.metamac.web.common.shared.domain.ExternalItemsResult;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

public interface StatisticalOperationsRestInternalFacade {

    /**
     * Retrieves operation
     */
    public Operation retrieveOperation(ServiceContext serviceContext, String operationCode) throws MetamacWebException;

    /**
     * Finds operations for Indicators Systems
     */
    public Operations findOperationsIndicatorsSystem(ServiceContext serviceContext, int firstResult, int maxResult) throws MetamacWebException;
    
    /**
     * Finds operations
     * 
     * @param serviceContext
     * @param firstResult
     * @param maxResult
     * @param criteria
     * @return
     * @throws MetamacWebException
     */
    public ExternalItemsResult findOperations(ServiceContext serviceContext, int firstResult, int maxResult, MetamacWebCriteria criteria) throws MetamacWebException; 
}
