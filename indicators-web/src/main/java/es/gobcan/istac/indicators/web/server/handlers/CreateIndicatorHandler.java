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
import es.gobcan.istac.indicators.web.shared.CreateIndicatorAction;
import es.gobcan.istac.indicators.web.shared.CreateIndicatorResult;

@Component
public class CreateIndicatorHandler extends AbstractActionHandler<CreateIndicatorAction, CreateIndicatorResult>{
    
    @Autowired 
    private IndicatorsServiceWrapper service;
    
    
    public CreateIndicatorHandler() {
        super(CreateIndicatorAction.class);
    }
    
    @Override
    public CreateIndicatorResult execute(CreateIndicatorAction action, ExecutionContext context) throws ActionException {
        try {
            service.createIndicator(ServiceContextHelper.getServiceContext(), action.getIndicator());
            return new CreateIndicatorResult();
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(CreateIndicatorAction action, CreateIndicatorResult result, ExecutionContext context) throws ActionException {
        
    }

}
