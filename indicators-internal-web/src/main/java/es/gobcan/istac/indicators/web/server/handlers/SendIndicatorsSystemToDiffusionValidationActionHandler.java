package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.utils.DtoUtils;
import es.gobcan.istac.indicators.web.shared.SendIndicatorsSystemToDiffusionValidationAction;
import es.gobcan.istac.indicators.web.shared.SendIndicatorsSystemToDiffusionValidationResult;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemDtoWeb;

@Component
public class SendIndicatorsSystemToDiffusionValidationActionHandler extends SecurityActionHandler<SendIndicatorsSystemToDiffusionValidationAction, SendIndicatorsSystemToDiffusionValidationResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public SendIndicatorsSystemToDiffusionValidationActionHandler() {
        super(SendIndicatorsSystemToDiffusionValidationAction.class);
    }

    @Override
    public SendIndicatorsSystemToDiffusionValidationResult executeSecurityAction(SendIndicatorsSystemToDiffusionValidationAction action) throws ActionException {
        try {
            IndicatorsSystemDtoWeb indicatorsSystemDtoWeb = action.getSystemToSend();
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.sendIndicatorsSystemToDiffusionValidation(ServiceContextHolder.getCurrentServiceContext(),
                    indicatorsSystemDtoWeb.getUuid());
            return new SendIndicatorsSystemToDiffusionValidationResult(DtoUtils.updateIndicatorsSystemDtoWeb(indicatorsSystemDtoWeb, indicatorsSystemDto));
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(SendIndicatorsSystemToDiffusionValidationAction action, SendIndicatorsSystemToDiffusionValidationResult result, ExecutionContext context) throws ActionException {

    }

}
