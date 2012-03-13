package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.ServiceContextHelper;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemListAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemListResult;

@Component
public class GetIndicatorsSystemListActionHandler extends AbstractActionHandler<GetIndicatorsSystemListAction, GetIndicatorsSystemListResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    
    public GetIndicatorsSystemListActionHandler() {
        super(GetIndicatorsSystemListAction.class);
    }

    @Override
    public GetIndicatorsSystemListResult execute(GetIndicatorsSystemListAction action, ExecutionContext context) throws ActionException {
        try {
            return new GetIndicatorsSystemListResult(indicatorsServiceFacade.findIndicatorsSystems(ServiceContextHelper.getServiceContext()));
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(GetIndicatorsSystemListAction action, GetIndicatorsSystemListResult result, ExecutionContext context) throws ActionException {
    }

}
