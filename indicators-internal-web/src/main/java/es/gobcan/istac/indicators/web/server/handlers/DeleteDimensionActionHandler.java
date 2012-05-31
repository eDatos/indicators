package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import es.gobcan.istac.indicators.web.shared.DeleteDimensionAction;
import es.gobcan.istac.indicators.web.shared.DeleteDimensionResult;

@Component
public class DeleteDimensionActionHandler extends AbstractActionHandler<DeleteDimensionAction, DeleteDimensionResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public DeleteDimensionActionHandler() {
        super(DeleteDimensionAction.class);
    }

    @Override
    public DeleteDimensionResult execute(DeleteDimensionAction action, ExecutionContext context) throws ActionException {
        try {
            indicatorsServiceFacade.deleteDimension(ServiceContextHolder.getCurrentServiceContext(), action.getDimensionUuid());
            return new DeleteDimensionResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(DeleteDimensionAction action, DeleteDimensionResult result, ExecutionContext context) throws ActionException {

    }

}
