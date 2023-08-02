package es.gobcan.istac.indicators.web.server.rest;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.web.common.shared.criteria.ExternalResourceWebCriteria;
import org.siemac.metamac.web.common.shared.domain.ExternalItemsResult;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

public interface SrmRestInternalFacade {

    public static final String BEAN_ID = "srmRestInternalFacade";

    ExternalItemsResult retrieveAllCategoryElements(ServiceContext serviceContext, ExternalResourceWebCriteria condition, int firstResult, int maxResults) throws MetamacWebException;

}
