package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import es.gobcan.istac.indicators.web.shared.SendIndicatorToProductionValidationAction;
import es.gobcan.istac.indicators.web.shared.SendIndicatorToProductionValidationResult;

@Component
public class SendIndicatorToProductionValidationActionHandler extends AbstractActionHandler<SendIndicatorToProductionValidationAction, SendIndicatorToProductionValidationResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public SendIndicatorToProductionValidationActionHandler() {
        super(SendIndicatorToProductionValidationAction.class);
    }

    @Override
    public SendIndicatorToProductionValidationResult execute(SendIndicatorToProductionValidationAction action, ExecutionContext context) throws ActionException {
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
