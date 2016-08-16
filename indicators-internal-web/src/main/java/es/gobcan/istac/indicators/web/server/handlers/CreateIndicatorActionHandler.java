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
import es.gobcan.istac.indicators.web.shared.CreateIndicatorAction;
import es.gobcan.istac.indicators.web.shared.CreateIndicatorResult;

@Component
public class CreateIndicatorActionHandler extends SecurityActionHandler<CreateIndicatorAction, CreateIndicatorResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public CreateIndicatorActionHandler() {
        super(CreateIndicatorAction.class);
    }

    @Override
    public CreateIndicatorResult executeSecurityAction(CreateIndicatorAction action) throws ActionException {
        try {
            IndicatorDto indicatorDto = indicatorsServiceFacade.createIndicator(ServiceContextHolder.getCurrentServiceContext(), action.getIndicator());
            return new CreateIndicatorResult(indicatorDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(CreateIndicatorAction action, CreateIndicatorResult result, ExecutionContext context) throws ActionException {

    }

}
