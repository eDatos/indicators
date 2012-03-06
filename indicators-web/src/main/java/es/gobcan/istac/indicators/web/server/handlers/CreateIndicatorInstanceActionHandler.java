package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.web.server.ServiceContextHelper;
import es.gobcan.istac.indicators.web.server.services.IndicatorsServiceWrapper;
import es.gobcan.istac.indicators.web.shared.CreateIndicatorInstanceAction;
import es.gobcan.istac.indicators.web.shared.CreateIndicatorInstanceResult;

@Component
public class CreateIndicatorInstanceActionHandler extends AbstractActionHandler<CreateIndicatorInstanceAction, CreateIndicatorInstanceResult> {

    @Autowired
    private IndicatorsServiceWrapper service;
    
    public CreateIndicatorInstanceActionHandler() {
        super(CreateIndicatorInstanceAction.class);
    }

    @Override
    public CreateIndicatorInstanceResult execute(CreateIndicatorInstanceAction action, ExecutionContext context) throws ActionException {
        try {
            IndicatorsSystemDto system = action.getIndicatorsSystem();
            IndicatorInstanceDto createdInstance = service.createIndicatorInstance(ServiceContextHelper.getServiceContext(), system, action.getIndicatorInstance());
            return new CreateIndicatorInstanceResult(createdInstance);
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(CreateIndicatorInstanceAction action, CreateIndicatorInstanceResult result, ExecutionContext context) throws ActionException {
    }

}
