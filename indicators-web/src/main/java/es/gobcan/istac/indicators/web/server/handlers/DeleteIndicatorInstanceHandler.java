package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.web.server.ServiceContextHelper;
import es.gobcan.istac.indicators.web.server.services.IndicatorsServiceWrapper;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorInstanceAction;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorInstanceResult;

@Component
public class DeleteIndicatorInstanceHandler extends AbstractActionHandler<DeleteIndicatorInstanceAction, DeleteIndicatorInstanceResult> {

    @Autowired
    private IndicatorsServiceWrapper service;
    
    public DeleteIndicatorInstanceHandler() {
        super(DeleteIndicatorInstanceAction.class);
    }

    @Override
    public DeleteIndicatorInstanceResult execute(DeleteIndicatorInstanceAction action, ExecutionContext context) throws ActionException {
        try {
            service.deleteIndicatorInstance(ServiceContextHelper.getServiceContext(),action.getIndicatorInstanceUuid());
            return new DeleteIndicatorInstanceResult();
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(DeleteIndicatorInstanceAction action, DeleteIndicatorInstanceResult result, ExecutionContext context) throws ActionException {

    }

}
