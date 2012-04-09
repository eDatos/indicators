package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.ServiceContextHelper;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorsAction;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorsResult;

@Component
public class DeleteIndicatorsActionHandler extends AbstractActionHandler<DeleteIndicatorsAction, DeleteIndicatorsResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public DeleteIndicatorsActionHandler() {
        super(DeleteIndicatorsAction.class);
    }

    @Override
    public DeleteIndicatorsResult execute(DeleteIndicatorsAction action, ExecutionContext context) throws ActionException {
        try {
            for (String uuid : action.getUuids()) {
                indicatorsServiceFacade.deleteIndicator(ServiceContextHelper.getServiceContext(), uuid);
            }
            return new DeleteIndicatorsResult();
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(DeleteIndicatorsAction action, DeleteIndicatorsResult result, ExecutionContext context) throws ActionException {
    }

}
