package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.ServiceContextHolder;
import es.gobcan.istac.indicators.web.server.utils.DtoUtils;
import es.gobcan.istac.indicators.web.shared.SendIndicatorsSystemToProductionValidationAction;
import es.gobcan.istac.indicators.web.shared.SendIndicatorsSystemToProductionValidationResult;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemDtoWeb;

@Component
public class SendIndicatorsSystemToProductionValidationActionHandler extends AbstractActionHandler<SendIndicatorsSystemToProductionValidationAction, SendIndicatorsSystemToProductionValidationResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public SendIndicatorsSystemToProductionValidationActionHandler() {
        super(SendIndicatorsSystemToProductionValidationAction.class);
    }

    @Override
    public SendIndicatorsSystemToProductionValidationResult execute(SendIndicatorsSystemToProductionValidationAction action, ExecutionContext context) throws ActionException {
        IndicatorsSystemDtoWeb indicatorsSystemDtoWeb = action.getSystemToSend();

        IndicatorsSystemDto indicatorsSystemDto = null;
        // If system does not exist, create and send to production validation
        try {
            indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(ServiceContextHolder.getCurrentServiceContext(), indicatorsSystemDtoWeb.getCode(), null);
        } catch (MetamacException e) {
            try {
                indicatorsSystemDto = indicatorsServiceFacade.createIndicatorsSystem(ServiceContextHolder.getCurrentServiceContext(), indicatorsSystemDtoWeb);
                indicatorsSystemDto = indicatorsServiceFacade.sendIndicatorsSystemToProductionValidation(ServiceContextHolder.getCurrentServiceContext(), indicatorsSystemDto.getUuid());
                return new SendIndicatorsSystemToProductionValidationResult(DtoUtils.updateIndicatorsSystemDtoWeb(indicatorsSystemDtoWeb, indicatorsSystemDto));
            } catch (MetamacException e1) {
                throw WebExceptionUtils.createMetamacWebException(e);
            }
        }
        // If exists, send to production validation
        try {
            indicatorsSystemDto = indicatorsServiceFacade.sendIndicatorsSystemToProductionValidation(ServiceContextHolder.getCurrentServiceContext(), indicatorsSystemDto.getUuid());
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
        return new SendIndicatorsSystemToProductionValidationResult(DtoUtils.updateIndicatorsSystemDtoWeb(indicatorsSystemDtoWeb, indicatorsSystemDto));
    }

    @Override
    public void undo(SendIndicatorsSystemToProductionValidationAction action, SendIndicatorsSystemToProductionValidationResult result, ExecutionContext context) throws ActionException {

    }

}
