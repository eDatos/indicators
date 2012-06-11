package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.UpdateIndicatorInstanceAction;
import es.gobcan.istac.indicators.web.shared.UpdateIndicatorInstanceResult;

@Component
public class UpdateIndicatorInstanceActionHandler extends SecurityActionHandler<UpdateIndicatorInstanceAction, UpdateIndicatorInstanceResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public UpdateIndicatorInstanceActionHandler() {
        super(UpdateIndicatorInstanceAction.class);
    }

    @Override
    public UpdateIndicatorInstanceResult executeSecurityAction(UpdateIndicatorInstanceAction action) throws ActionException {
        try {
            IndicatorInstanceDto indicatorInstanceDto = indicatorsServiceFacade.updateIndicatorInstance(ServiceContextHolder.getCurrentServiceContext(), action.getIndicatorInstanceToUpdate());
            return new UpdateIndicatorInstanceResult(indicatorInstanceDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(UpdateIndicatorInstanceAction action, UpdateIndicatorInstanceResult result, ExecutionContext context) throws ActionException {

    }

}
