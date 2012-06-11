package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.SendIndicatorToProductionValidationAction;
import es.gobcan.istac.indicators.web.shared.SendIndicatorToProductionValidationResult;

@Component
public class SendIndicatorToProductionValidationActionHandler extends SecurityActionHandler<SendIndicatorToProductionValidationAction, SendIndicatorToProductionValidationResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public SendIndicatorToProductionValidationActionHandler() {
        super(SendIndicatorToProductionValidationAction.class);
    }

    @Override
    public SendIndicatorToProductionValidationResult executeSecurityAction(SendIndicatorToProductionValidationAction action) throws ActionException {
        try {
            IndicatorDto indicatorDto = indicatorsServiceFacade.sendIndicatorToProductionValidation(ServiceContextHolder.getCurrentServiceContext(), action.getUuid());
            return new SendIndicatorToProductionValidationResult(indicatorDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(SendIndicatorToProductionValidationAction action, SendIndicatorToProductionValidationResult result, ExecutionContext context) throws ActionException {

    }

}
