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
import es.gobcan.istac.indicators.web.shared.RejectIndicatorsSystemDiffusionValidationAction;
import es.gobcan.istac.indicators.web.shared.RejectIndicatorsSystemDiffusionValidationResult;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemDtoWeb;

@Component
public class RejectIndicatorsSystemDiffusionValidationActionHandler extends AbstractActionHandler<RejectIndicatorsSystemDiffusionValidationAction, RejectIndicatorsSystemDiffusionValidationResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public RejectIndicatorsSystemDiffusionValidationActionHandler() {
        super(RejectIndicatorsSystemDiffusionValidationAction.class);
    }

    @Override
    public RejectIndicatorsSystemDiffusionValidationResult execute(RejectIndicatorsSystemDiffusionValidationAction action, ExecutionContext context) throws ActionException {
        try {
            IndicatorsSystemDtoWeb indicatorsSystemDtoWeb = action.getIndicatorsSystemToReject();
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.rejectIndicatorsSystemDiffusionValidation(ServiceContextHolder.getCurrentServiceContext(),
                    indicatorsSystemDtoWeb.getUuid());
            return new RejectIndicatorsSystemDiffusionValidationResult(DtoUtils.updateIndicatorsSystemDtoWeb(indicatorsSystemDtoWeb, indicatorsSystemDto));
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(RejectIndicatorsSystemDiffusionValidationAction action, RejectIndicatorsSystemDiffusionValidationResult result, ExecutionContext context) throws ActionException {

    }

}
