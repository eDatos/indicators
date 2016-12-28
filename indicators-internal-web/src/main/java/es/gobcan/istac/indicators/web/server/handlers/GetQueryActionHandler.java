package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.DataStructureDto;
import es.gobcan.istac.indicators.web.server.rest.StatisticalResoucesRestInternalFacade;
import es.gobcan.istac.indicators.web.shared.GetQueryAction;
import es.gobcan.istac.indicators.web.shared.GetQueryResult;

@Component
public class GetQueryActionHandler extends SecurityActionHandler<GetQueryAction, GetQueryResult> {

    @Autowired
    StatisticalResoucesRestInternalFacade statisticalResoucesRestExternalFacade;

    public GetQueryActionHandler() {
        super(GetQueryAction.class);
    }

    @Override
    public GetQueryResult executeSecurityAction(GetQueryAction action) throws ActionException {
        DataStructureDto dataDefinitionFromQuery = statisticalResoucesRestExternalFacade.retrieveDataDefinitionFromQuery(ServiceContextHolder.getCurrentServiceContext(), action.getQueryUrn());

        return new GetQueryResult(dataDefinitionFromQuery);
    }

}
