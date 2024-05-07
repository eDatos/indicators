package es.gobcan.istac.indicators.web.server.rest;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.web.common.shared.criteria.ExternalResourceWebCriteria;
import org.siemac.metamac.web.common.shared.criteria.SrmExternalResourceRestCriteria;
import org.siemac.metamac.web.common.shared.criteria.SrmItemRestCriteria;
import org.siemac.metamac.web.common.shared.domain.ExternalItemsResult;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

public interface SrmRestInternalFacade {

    public static final String BEAN_ID = "srmRestInternalFacade";

    ExternalItemsResult retrieveCategoryElementsByCategoryScheme(ServiceContext serviceContext, String categorySchemeUrn, ExternalResourceWebCriteria condition, int firstResult, int maxResults)
            throws MetamacWebException;
    ExternalItemsResult findCodelists(ServiceContext serviceContext, SrmExternalResourceRestCriteria criteria, int firstResult, int maxResults) throws MetamacWebException;
    ExternalItemsResult findCodes(ServiceContext serviceContext, SrmItemRestCriteria itemWebCriteria, int firstResult, int maxResults) throws MetamacWebException;

}
