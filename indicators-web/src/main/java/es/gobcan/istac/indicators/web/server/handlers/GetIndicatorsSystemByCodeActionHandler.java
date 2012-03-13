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
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemResult;

@Component
public class GetIndicatorsSystemByCodeActionHandler extends AbstractActionHandler<GetIndicatorsSystemAction, GetIndicatorsSystemResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;
    
    
    public GetIndicatorsSystemByCodeActionHandler() {
        super(GetIndicatorsSystemAction.class);
    }

    @Override
    public GetIndicatorsSystemResult execute(GetIndicatorsSystemAction action, ExecutionContext context) throws ActionException {
        try {
            IndicatorsSystemDto indSys = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(ServiceContextHelper.getServiceContext(), action.getCode(), null);
            return new GetIndicatorsSystemResult(indSys);
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(GetIndicatorsSystemAction action, GetIndicatorsSystemResult result, ExecutionContext context) throws ActionException {
    }

}
