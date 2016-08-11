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
import es.gobcan.istac.indicators.web.shared.UpdateIndicatorAction;
import es.gobcan.istac.indicators.web.shared.UpdateIndicatorResult;

@Component
public class UpdateIndicatorActionHandler extends SecurityActionHandler<UpdateIndicatorAction, UpdateIndicatorResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public UpdateIndicatorActionHandler() {
        super(UpdateIndicatorAction.class);
    }

    @Override
    public UpdateIndicatorResult executeSecurityAction(UpdateIndicatorAction action) throws ActionException {
        try {
            IndicatorDto indicatorDto = indicatorsServiceFacade.updateIndicator(ServiceContextHolder.getCurrentServiceContext(), action.getIndicatorToUpdate());
            return new UpdateIndicatorResult(indicatorDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(UpdateIndicatorAction action, UpdateIndicatorResult result, ExecutionContext context) throws ActionException {
    }

}
