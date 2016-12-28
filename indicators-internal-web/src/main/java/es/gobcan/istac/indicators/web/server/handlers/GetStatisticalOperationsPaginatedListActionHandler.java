package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.shared.domain.ExternalItemsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.web.server.rest.StatisticalOperationsRestInternalFacade;
import es.gobcan.istac.indicators.web.shared.GetStatisticalOperationsPaginatedListAction;
import es.gobcan.istac.indicators.web.shared.GetStatisticalOperationsPaginatedListResult;

@Component
public class GetStatisticalOperationsPaginatedListActionHandler extends SecurityActionHandler<GetStatisticalOperationsPaginatedListAction, GetStatisticalOperationsPaginatedListResult> {

    @Autowired
    StatisticalOperationsRestInternalFacade statisticalOperationsRestInternalFacade;

    public GetStatisticalOperationsPaginatedListActionHandler() {
        super(GetStatisticalOperationsPaginatedListAction.class);
    }

    @Override
    public GetStatisticalOperationsPaginatedListResult executeSecurityAction(GetStatisticalOperationsPaginatedListAction action) throws ActionException {
        ExternalItemsResult result = statisticalOperationsRestInternalFacade.findOperations(ServiceContextHolder.getCurrentServiceContext(), action.getFirstResult(), action.getMaxResults(), action.getCriteria());
        return new GetStatisticalOperationsPaginatedListResult(result.getExternalItemDtos(), result.getFirstResult(), result.getTotalResults());
    }

}
