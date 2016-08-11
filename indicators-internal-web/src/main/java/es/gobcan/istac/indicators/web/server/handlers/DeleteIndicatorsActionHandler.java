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
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorsAction;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorsResult;

@Component
public class DeleteIndicatorsActionHandler extends SecurityActionHandler<DeleteIndicatorsAction, DeleteIndicatorsResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public DeleteIndicatorsActionHandler() {
        super(DeleteIndicatorsAction.class);
    }

    @Override
    public DeleteIndicatorsResult executeSecurityAction(DeleteIndicatorsAction action) throws ActionException {
        try {
            for (String uuid : action.getUuids()) {
                indicatorsServiceFacade.deleteIndicator(ServiceContextHolder.getCurrentServiceContext(), uuid);
            }
            return new DeleteIndicatorsResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(DeleteIndicatorsAction action, DeleteIndicatorsResult result, ExecutionContext context) throws ActionException {
    }

}
