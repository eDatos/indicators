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
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import es.gobcan.istac.indicators.web.server.utils.DtoUtils;
import es.gobcan.istac.indicators.web.shared.RejectIndicatorsSystemProductionValidationAction;
import es.gobcan.istac.indicators.web.shared.RejectIndicatorsSystemProductionValidationResult;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemDtoWeb;

@Component
public class RejectIndicatorsSystemProductionValidationActionHandler extends AbstractActionHandler<RejectIndicatorsSystemProductionValidationAction, RejectIndicatorsSystemProductionValidationResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public RejectIndicatorsSystemProductionValidationActionHandler() {
        super(RejectIndicatorsSystemProductionValidationAction.class);
    }

    @Override
    public RejectIndicatorsSystemProductionValidationResult execute(RejectIndicatorsSystemProductionValidationAction action, ExecutionContext context) throws ActionException {
        try {
            IndicatorsSystemDtoWeb indicatorsSystemDtoWeb = action.getIndicatorsSystemToReject();
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.rejectIndicatorsSystemProductionValidation(ServiceContextHolder.getCurrentServiceContext(),
                    indicatorsSystemDtoWeb.getUuid());
            return new RejectIndicatorsSystemProductionValidationResult(DtoUtils.updateIndicatorsSystemDtoWeb(indicatorsSystemDtoWeb, indicatorsSystemDto));
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(RejectIndicatorsSystemProductionValidationAction action, RejectIndicatorsSystemProductionValidationResult result, ExecutionContext context) throws ActionException {

    }

}
