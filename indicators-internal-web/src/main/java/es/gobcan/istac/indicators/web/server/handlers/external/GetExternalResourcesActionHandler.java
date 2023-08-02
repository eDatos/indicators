package es.gobcan.istac.indicators.web.server.handlers.external;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.shared.constants.CommonSharedConstants;
import org.siemac.metamac.web.common.shared.domain.ExternalItemsResult;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.web.server.rest.SrmRestInternalFacade;
import es.gobcan.istac.indicators.web.shared.external.GetExternalResourcesAction;
import es.gobcan.istac.indicators.web.shared.external.GetExternalResourcesResult;

@Component
public class GetExternalResourcesActionHandler extends SecurityActionHandler<GetExternalResourcesAction, GetExternalResourcesResult> {

    @Autowired
    private SrmRestInternalFacade srmRestInternalFacade;

    public GetExternalResourcesActionHandler() {
        super(GetExternalResourcesAction.class);
    }

    @Override
    public GetExternalResourcesResult executeSecurityAction(GetExternalResourcesAction action) throws ActionException {

        ServiceContext serviceContext = ServiceContextHolder.getCurrentServiceContext();

        ExternalItemsResult result = null;
        switch (action.getExternalResourceWebCriteria().getExternalArtifactType()) {
            case CATEGORY_ELEMENT:
                result = srmRestInternalFacade.retrieveAllCategoryElements(serviceContext, action.getExternalResourceWebCriteria(), action.getFirstResult(), action.getMaxResults());
                break;
            default:
                throw new MetamacWebException(CommonSharedConstants.EXCEPTION_UNKNOWN, "An unknown exception has ocurred. Please contact system administrator.");
        }
        return new GetExternalResourcesResult(result);
    }
}
