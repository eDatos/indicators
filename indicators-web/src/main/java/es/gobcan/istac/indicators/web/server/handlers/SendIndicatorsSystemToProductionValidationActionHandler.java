package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.ServiceContextHelper;
import es.gobcan.istac.indicators.web.shared.SendIndicatorsSystemToProductionValidationAction;
import es.gobcan.istac.indicators.web.shared.SendIndicatorsSystemToProductionValidationResult;


@Component
public class SendIndicatorsSystemToProductionValidationActionHandler extends AbstractActionHandler<SendIndicatorsSystemToProductionValidationAction, SendIndicatorsSystemToProductionValidationResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;
    
    public SendIndicatorsSystemToProductionValidationActionHandler() {
        super(SendIndicatorsSystemToProductionValidationAction.class);
    }

    @Override
    public SendIndicatorsSystemToProductionValidationResult execute(SendIndicatorsSystemToProductionValidationAction action, ExecutionContext context) throws ActionException {
        try {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.sendIndicatorsSystemToProductionValidation(ServiceContextHelper.getServiceContext(), action.getUuid());
            return new SendIndicatorsSystemToProductionValidationResult(indicatorsSystemDto);
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(SendIndicatorsSystemToProductionValidationAction action, SendIndicatorsSystemToProductionValidationResult result, ExecutionContext context) throws ActionException {
        
    }

}
