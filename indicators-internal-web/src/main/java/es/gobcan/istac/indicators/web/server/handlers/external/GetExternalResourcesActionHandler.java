package es.gobcan.istac.indicators.web.server.handlers.external;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.CommonServiceExceptionType;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.constants.CommonSharedConstants;
import org.siemac.metamac.web.common.shared.criteria.SrmExternalResourceRestCriteria;
import org.siemac.metamac.web.common.shared.criteria.SrmItemRestCriteria;
import org.siemac.metamac.web.common.shared.domain.ExternalItemsResult;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.web.server.rest.SrmRestInternalFacade;
import es.gobcan.istac.indicators.web.shared.external.GetExternalResourcesAction;
import es.gobcan.istac.indicators.web.shared.external.GetExternalResourcesResult;

@Component
public class GetExternalResourcesActionHandler extends SecurityActionHandler<GetExternalResourcesAction, GetExternalResourcesResult> {

    @Autowired
    private SrmRestInternalFacade          srmRestInternalFacade;

    @Autowired
    private IndicatorsConfigurationService configurationService;

    public GetExternalResourcesActionHandler() {
        super(GetExternalResourcesAction.class);
    }

    @Override
    public GetExternalResourcesResult executeSecurityAction(GetExternalResourcesAction action) throws ActionException {

        ServiceContext serviceContext = ServiceContextHolder.getCurrentServiceContext();

        ExternalItemsResult result = null;
        switch (action.getExternalResourceWebCriteria().getExternalArtifactType()) {
            case CATEGORY_ELEMENT:
                result = srmRestInternalFacade.retrieveCategoryElementsByCategoryScheme(serviceContext, retrieveDefaultCategorySchemeProperty(serviceContext), action.getExternalResourceWebCriteria(),
                        action.getFirstResult(), action.getMaxResults());
                break;
            case CODELIST:
                result = srmRestInternalFacade.findCodelists(serviceContext, (SrmExternalResourceRestCriteria) action.getExternalResourceWebCriteria(), action.getFirstResult(),
                        action.getMaxResults());
                break;
            case CODE:
                result = srmRestInternalFacade.findCodes(serviceContext, (SrmItemRestCriteria) action.getExternalResourceWebCriteria(), action.getFirstResult(), action.getMaxResults());
                break;
            default:
                throw new MetamacWebException(CommonSharedConstants.EXCEPTION_UNKNOWN, "An unknown exception has ocurred. Please contact system administrator.");
        }
        return new GetExternalResourcesResult(result);
    }

    private String retrieveDefaultCategorySchemeProperty(ServiceContext ctx) throws MetamacWebException {
        try {
            return configurationService.retrieveDefaultCategoryScheme();
        } catch (Exception e) {
            throw WebExceptionUtils.createMetamacWebExceptionTranslated(ctx, CommonServiceExceptionType.CONFIGURATION_PROPERTY_INVALID, e.getMessage());
        }
    }
}
