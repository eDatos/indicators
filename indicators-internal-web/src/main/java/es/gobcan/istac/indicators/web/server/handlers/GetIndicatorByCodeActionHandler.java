package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import es.gobcan.istac.indicators.web.shared.GetIndicatorByCodeAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorByCodeResult;

@Component
public class GetIndicatorByCodeActionHandler extends AbstractActionHandler<GetIndicatorByCodeAction, GetIndicatorByCodeResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public GetIndicatorByCodeActionHandler() {
        super(GetIndicatorByCodeAction.class);
    }

    @Override
    public GetIndicatorByCodeResult execute(GetIndicatorByCodeAction action, ExecutionContext context) throws ActionException {
        try {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicatorByCode(ServiceContextHolder.getCurrentServiceContext(), action.getCode(), action.getVersionNumber());
            return new GetIndicatorByCodeResult(indicatorDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(GetIndicatorByCodeAction action, GetIndicatorByCodeResult result, ExecutionContext context) throws ActionException {

    }
}
