package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorInstanceAction;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorInstanceResult;

@Component
public class DeleteIndicatorInstanceActionHandler extends SecurityActionHandler<DeleteIndicatorInstanceAction, DeleteIndicatorInstanceResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public DeleteIndicatorInstanceActionHandler() {
        super(DeleteIndicatorInstanceAction.class);
    }

    @Override
    public DeleteIndicatorInstanceResult executeSecurityAction(DeleteIndicatorInstanceAction action) throws ActionException {
        try {
            indicatorsServiceFacade.deleteIndicatorInstance(ServiceContextHolder.getCurrentServiceContext(), action.getIndicatorInstanceUuid());
            return new DeleteIndicatorInstanceResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(DeleteIndicatorInstanceAction action, DeleteIndicatorInstanceResult result, ExecutionContext context) throws ActionException {

    }

}
