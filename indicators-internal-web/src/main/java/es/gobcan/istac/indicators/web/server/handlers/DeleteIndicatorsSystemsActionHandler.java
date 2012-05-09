package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.ServiceContextHolder;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorsSystemsAction;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorsSystemsResult;

@Component
public class DeleteIndicatorsSystemsActionHandler extends AbstractActionHandler<DeleteIndicatorsSystemsAction, DeleteIndicatorsSystemsResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public DeleteIndicatorsSystemsActionHandler() {
        super(DeleteIndicatorsSystemsAction.class);
    }

    @Override
    public DeleteIndicatorsSystemsResult execute(DeleteIndicatorsSystemsAction action, ExecutionContext context) throws ActionException {
        try {
            for (String uuid : action.getUuids()) {
                indicatorsServiceFacade.deleteIndicatorsSystem(ServiceContextHolder.getCurrentServiceContext(), uuid);
            }
            return new DeleteIndicatorsSystemsResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(DeleteIndicatorsSystemsAction action, DeleteIndicatorsSystemsResult result, ExecutionContext context) throws ActionException {
    }

}
