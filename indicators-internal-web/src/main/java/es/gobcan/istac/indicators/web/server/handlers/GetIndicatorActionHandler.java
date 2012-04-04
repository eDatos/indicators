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
import es.gobcan.istac.indicators.web.shared.GetIndicatorAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorResult;

@Component
public class GetIndicatorActionHandler extends AbstractActionHandler<GetIndicatorAction, GetIndicatorResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;
    
    
    public GetIndicatorActionHandler() {
        super(GetIndicatorAction.class);
    }

    @Override
    public GetIndicatorResult execute(GetIndicatorAction action, ExecutionContext context) throws ActionException {
        try {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(ServiceContextHelper.getServiceContext(), action.getUuid(), null);
            return new GetIndicatorResult(indicatorDto);
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(GetIndicatorAction action, GetIndicatorResult result, ExecutionContext context) throws ActionException {

    }

}
