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
import es.gobcan.istac.indicators.web.shared.RejectIndicatorsSystemValidationAction;
import es.gobcan.istac.indicators.web.shared.RejectIndicatorsSystemValidationResult;


@Component
public class RejectIndicatorsSystemValidationActionHandler extends AbstractActionHandler<RejectIndicatorsSystemValidationAction, RejectIndicatorsSystemValidationResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;
    
    public RejectIndicatorsSystemValidationActionHandler() {
        super(RejectIndicatorsSystemValidationAction.class);
    }

    @Override
    public RejectIndicatorsSystemValidationResult execute(RejectIndicatorsSystemValidationAction action, ExecutionContext context) throws ActionException {
        try {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.rejectIndicatorsSystemValidation(ServiceContextHelper.getServiceContext(), action.getUuid());
            return new RejectIndicatorsSystemValidationResult(indicatorsSystemDto);
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(RejectIndicatorsSystemValidationAction action, RejectIndicatorsSystemValidationResult result, ExecutionContext context) throws ActionException {
        
    }

}
