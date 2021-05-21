package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.shared.domain.ExternalItemsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.web.server.rest.StatisticalResoucesRestExternalFacade;
import es.gobcan.istac.indicators.web.shared.GetQueriesPaginatedListAction;
import es.gobcan.istac.indicators.web.shared.GetQueriesPaginatedListResult;

@Component
public class GetQueriesPaginatedListActionHandler extends SecurityActionHandler<GetQueriesPaginatedListAction, GetQueriesPaginatedListResult> {

    @Autowired
    StatisticalResoucesRestExternalFacade statisticalResoucesRestExternalFacade;
    
    public GetQueriesPaginatedListActionHandler() {
        super(GetQueriesPaginatedListAction.class);
    }

    @Override
    public GetQueriesPaginatedListResult executeSecurityAction(GetQueriesPaginatedListAction action) throws ActionException {

        ExternalItemsResult result = statisticalResoucesRestExternalFacade.findQueries(ServiceContextHolder.getCurrentServiceContext(), action.getFirstResult(), action.getMaxResults(), action.getCriteria());
        return new GetQueriesPaginatedListResult(result.getExternalItemDtos(), result.getFirstResult(), result.getTotalResults());
    }

}
