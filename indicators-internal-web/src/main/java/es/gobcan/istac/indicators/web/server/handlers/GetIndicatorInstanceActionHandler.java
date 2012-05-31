package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import es.gobcan.istac.indicators.web.shared.GetIndicatorInstanceAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorInstanceResult;

@Component
public class GetIndicatorInstanceActionHandler extends AbstractActionHandler<GetIndicatorInstanceAction, GetIndicatorInstanceResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public GetIndicatorInstanceActionHandler() {
        super(GetIndicatorInstanceAction.class);
    }

    @Override
    public GetIndicatorInstanceResult execute(GetIndicatorInstanceAction action, ExecutionContext context) throws ActionException {
        try {
            IndicatorInstanceDto indicatorInstanceDto = indicatorsServiceFacade.retrieveIndicatorInstance(ServiceContextHolder.getCurrentServiceContext(), action.getUuid());
            return new GetIndicatorInstanceResult(indicatorInstanceDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(GetIndicatorInstanceAction action, GetIndicatorInstanceResult result, ExecutionContext context) throws ActionException {

    }

}
