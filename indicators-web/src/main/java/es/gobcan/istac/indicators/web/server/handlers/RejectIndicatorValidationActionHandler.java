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
import es.gobcan.istac.indicators.web.shared.RejectIndicatorValidationAction;
import es.gobcan.istac.indicators.web.shared.RejectIndicatorValidationResult;

@Component
public class RejectIndicatorValidationActionHandler extends AbstractActionHandler<RejectIndicatorValidationAction, RejectIndicatorValidationResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;
    
    public RejectIndicatorValidationActionHandler() {
        super(RejectIndicatorValidationAction.class);
    }

    @Override
    public RejectIndicatorValidationResult execute(RejectIndicatorValidationAction action, ExecutionContext context) throws ActionException {
        try {
            IndicatorDto indicatorDto = indicatorsServiceFacade.rejectIndicatorValidation(ServiceContextHelper.getServiceContext(), action.getUuid());
            return new RejectIndicatorValidationResult(indicatorDto);
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(RejectIndicatorValidationAction action, RejectIndicatorValidationResult result, ExecutionContext context) throws ActionException {
        
    }
    
}
