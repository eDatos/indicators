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
import es.gobcan.istac.indicators.web.shared.SendIndicatorToDiffusionValidationAction;
import es.gobcan.istac.indicators.web.shared.SendIndicatorToDiffusionValidationResult;

@Component
public class SendIndicatorToDiffusionValidationActionHandler extends SecurityActionHandler<SendIndicatorToDiffusionValidationAction, SendIndicatorToDiffusionValidationResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public SendIndicatorToDiffusionValidationActionHandler() {
        super(SendIndicatorToDiffusionValidationAction.class);
    }

    @Override
    public SendIndicatorToDiffusionValidationResult executeSecurityAction(SendIndicatorToDiffusionValidationAction action) throws ActionException {
        try {
            IndicatorDto indicatorDto = indicatorsServiceFacade.sendIndicatorToDiffusionValidation(ServiceContextHolder.getCurrentServiceContext(), action.getUuid());
            return new SendIndicatorToDiffusionValidationResult(indicatorDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(SendIndicatorToDiffusionValidationAction action, SendIndicatorToDiffusionValidationResult result, ExecutionContext context) throws ActionException {

    }

}
