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
import es.gobcan.istac.indicators.web.shared.GetIndicatorAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorResult;

@Component
public class GetIndicatorActionHandler extends SecurityActionHandler<GetIndicatorAction, GetIndicatorResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public GetIndicatorActionHandler() {
        super(GetIndicatorAction.class);
    }

    @Override
    public GetIndicatorResult executeSecurityAction(GetIndicatorAction action) throws ActionException {
        try {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(ServiceContextHolder.getCurrentServiceContext(), action.getUuid(), null);
            return new GetIndicatorResult(indicatorDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(GetIndicatorAction action, GetIndicatorResult result, ExecutionContext context) throws ActionException {

    }

}
