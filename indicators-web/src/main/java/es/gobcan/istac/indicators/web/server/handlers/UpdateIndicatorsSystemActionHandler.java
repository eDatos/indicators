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
import es.gobcan.istac.indicators.web.shared.UpdateIndicatorsSystemAction;
import es.gobcan.istac.indicators.web.shared.UpdateIndicatorsSystemResult;

@Component
public class UpdateIndicatorsSystemActionHandler extends AbstractActionHandler<UpdateIndicatorsSystemAction, UpdateIndicatorsSystemResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;
    
    
    public UpdateIndicatorsSystemActionHandler() {
        super(UpdateIndicatorsSystemAction.class);
    }

    @Override
    public UpdateIndicatorsSystemResult execute(UpdateIndicatorsSystemAction action, ExecutionContext context) throws ActionException {
        try {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.updateIndicatorsSystem(ServiceContextHelper.getServiceContext(), action.getIndicatorsSystemToUpdate());
            return new UpdateIndicatorsSystemResult(indicatorsSystemDto);
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(UpdateIndicatorsSystemAction action, UpdateIndicatorsSystemResult result, ExecutionContext context) throws ActionException {

    }

}
