package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.ServiceContextHelper;
import es.gobcan.istac.indicators.web.shared.SendIndicatorToDiffusionValidationAction;
import es.gobcan.istac.indicators.web.shared.SendIndicatorToDiffusionValidationResult;

@Component
public class SendIndicatorToDiffusionValidationActionHandler extends AbstractActionHandler<SendIndicatorToDiffusionValidationAction, SendIndicatorToDiffusionValidationResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public SendIndicatorToDiffusionValidationActionHandler() {
        super(SendIndicatorToDiffusionValidationAction.class);
    }

    @Override
    public SendIndicatorToDiffusionValidationResult execute(SendIndicatorToDiffusionValidationAction action, ExecutionContext context) throws ActionException {
        try {
            IndicatorDto indicatorDto = indicatorsServiceFacade.sendIndicatorToDiffusionValidation(ServiceContextHelper.getServiceContext(), action.getUuid());
            return new SendIndicatorToDiffusionValidationResult(indicatorDto);
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(SendIndicatorToDiffusionValidationAction action, SendIndicatorToDiffusionValidationResult result, ExecutionContext context) throws ActionException {

    }

}
