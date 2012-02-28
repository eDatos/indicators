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
import es.gobcan.istac.indicators.web.shared.UpdateIndicatorInstanceAction;
import es.gobcan.istac.indicators.web.shared.UpdateIndicatorInstanceResult;

@Component
public class UpdateIndicatorInstanceHandler extends AbstractActionHandler<UpdateIndicatorInstanceAction, UpdateIndicatorInstanceResult> {

    @Autowired
    private IndicatorsServiceWrapper service;
    
    public UpdateIndicatorInstanceHandler() {
        super(UpdateIndicatorInstanceAction.class);
    }

    @Override
    public UpdateIndicatorInstanceResult execute(UpdateIndicatorInstanceAction action, ExecutionContext context) throws ActionException {
        try {
            service.updateIndicatorInstance(ServiceContextHelper.getServiceContext(), action.getIndicatorInstance());
            return new UpdateIndicatorInstanceResult();
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(UpdateIndicatorInstanceAction action, UpdateIndicatorInstanceResult result, ExecutionContext context) throws ActionException {

    }

}
