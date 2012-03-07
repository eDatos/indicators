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
import es.gobcan.istac.indicators.web.server.ServiceContextHelper;
import es.gobcan.istac.indicators.web.server.services.IndicatorsServiceWrapper;
import es.gobcan.istac.indicators.web.shared.UpdateIndicatorAction;
import es.gobcan.istac.indicators.web.shared.UpdateIndicatorResult;

@Component
public class UpdateIndicatorActionHandler extends AbstractActionHandler<UpdateIndicatorAction, UpdateIndicatorResult> {

    @Autowired
    private IndicatorsServiceWrapper service;
    
    public UpdateIndicatorActionHandler() {
        super(UpdateIndicatorAction.class);
    }

    @Override
    public UpdateIndicatorResult execute(UpdateIndicatorAction action, ExecutionContext context) throws ActionException {
        try {
            IndicatorDto indicatorDto = service.updateIndicator(ServiceContextHelper.getServiceContext(),action.getIndicatorToUpdate());
            return new UpdateIndicatorResult(indicatorDto);
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(UpdateIndicatorAction action, UpdateIndicatorResult result, ExecutionContext context) throws ActionException {
    }

}
