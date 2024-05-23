package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.shared.domain.ExternalItemsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.web.server.rest.StatisticalResoucesRestExternalFacade;
import es.gobcan.istac.indicators.web.shared.GetDatasetsPaginatedListAction;
import es.gobcan.istac.indicators.web.shared.GetDatasetsPaginatedListResult;

@Component
public class GetDatasetsPaginatedListActionHandler extends SecurityActionHandler<GetDatasetsPaginatedListAction, GetDatasetsPaginatedListResult> {

    @Autowired
    StatisticalResoucesRestExternalFacade statisticalResoucesRestExternalFacade;

    public GetDatasetsPaginatedListActionHandler() {
        super(GetDatasetsPaginatedListAction.class);
    }

    @Override
    public GetDatasetsPaginatedListResult executeSecurityAction(GetDatasetsPaginatedListAction action) throws ActionException {

        ExternalItemsResult result = statisticalResoucesRestExternalFacade.findDatasets(ServiceContextHolder.getCurrentServiceContext(), action.getFirstResult(), action.getMaxResults(),
                action.getCriteria());
        return new GetDatasetsPaginatedListResult(result.getExternalItemDtos(), result.getFirstResult(), result.getTotalResults());
    }

}
