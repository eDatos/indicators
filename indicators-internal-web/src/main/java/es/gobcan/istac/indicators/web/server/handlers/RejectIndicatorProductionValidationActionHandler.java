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
import es.gobcan.istac.indicators.web.shared.RejectIndicatorProductionValidationAction;
import es.gobcan.istac.indicators.web.shared.RejectIndicatorProductionValidationResult;

@Component
public class RejectIndicatorProductionValidationActionHandler extends SecurityActionHandler<RejectIndicatorProductionValidationAction, RejectIndicatorProductionValidationResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public RejectIndicatorProductionValidationActionHandler() {
        super(RejectIndicatorProductionValidationAction.class);
    }

    @Override
    public RejectIndicatorProductionValidationResult executeSecurityAction(RejectIndicatorProductionValidationAction action) throws ActionException {
        try {
            IndicatorDto indicatorDto = indicatorsServiceFacade.rejectIndicatorProductionValidation(ServiceContextHolder.getCurrentServiceContext(), action.getUuid());
            return new RejectIndicatorProductionValidationResult(indicatorDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(RejectIndicatorProductionValidationAction action, RejectIndicatorProductionValidationResult result, ExecutionContext context) throws ActionException {

    }

}
