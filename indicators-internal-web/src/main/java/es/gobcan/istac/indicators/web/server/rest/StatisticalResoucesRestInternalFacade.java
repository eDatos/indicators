package es.gobcan.istac.indicators.web.server.rest;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;
import org.siemac.metamac.web.common.shared.domain.ExternalItemsResult;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

import es.gobcan.istac.indicators.core.dto.DataStructureDto;

public interface StatisticalResoucesRestInternalFacade {

    
    /**
     * @param serviceContext
     * @param firstResult
     * @param maxResult
     * @param criteria
     * @return
     * @throws MetamacWebException
     */
    public ExternalItemsResult findQueries(ServiceContext serviceContext, int firstResult, int maxResult, MetamacWebCriteria criteria) throws MetamacWebException;

    
    /**
     * @param jserviceContext
     * @param queryUrn
     * @return
     * @throws MetamacWebException
     */
    public DataStructureDto retrieveDataDefinitionFromQuery(ServiceContext jserviceContext, String queryUrn) throws MetamacWebException; 
}
