package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.ServiceContextHolder;
import es.gobcan.istac.indicators.web.shared.UpdateIndicatorInstanceAction;
import es.gobcan.istac.indicators.web.shared.UpdateIndicatorInstanceResult;

@Component
public class UpdateIndicatorInstanceActionHandler extends AbstractActionHandler<UpdateIndicatorInstanceAction, UpdateIndicatorInstanceResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public UpdateIndicatorInstanceActionHandler() {
        super(UpdateIndicatorInstanceAction.class);
    }

    @Override
    public UpdateIndicatorInstanceResult execute(UpdateIndicatorInstanceAction action, ExecutionContext context) throws ActionException {
        try {
            IndicatorInstanceDto indicatorInstanceDto = indicatorsServiceFacade.updateIndicatorInstance(ServiceContextHolder.getCurrentServiceContext(), action.getIndicatorInstanceToUpdate());
            return new UpdateIndicatorInstanceResult(indicatorInstanceDto);
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(UpdateIndicatorInstanceAction action, UpdateIndicatorInstanceResult result, ExecutionContext context) throws ActionException {

    }

}
