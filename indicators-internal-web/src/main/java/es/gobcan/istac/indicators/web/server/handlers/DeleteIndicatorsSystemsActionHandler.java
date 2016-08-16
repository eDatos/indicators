package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorsSystemsAction;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorsSystemsResult;

@Component
public class DeleteIndicatorsSystemsActionHandler extends SecurityActionHandler<DeleteIndicatorsSystemsAction, DeleteIndicatorsSystemsResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public DeleteIndicatorsSystemsActionHandler() {
        super(DeleteIndicatorsSystemsAction.class);
    }

    @Override
    public DeleteIndicatorsSystemsResult executeSecurityAction(DeleteIndicatorsSystemsAction action) throws ActionException {
        try {
            for (String uuid : action.getUuids()) {
                indicatorsServiceFacade.deleteIndicatorsSystem(ServiceContextHolder.getCurrentServiceContext(), uuid);
            }
            return new DeleteIndicatorsSystemsResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
