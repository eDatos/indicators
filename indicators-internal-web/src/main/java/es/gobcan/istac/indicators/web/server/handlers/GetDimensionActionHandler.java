package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.DimensionDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.GetDimensionAction;
import es.gobcan.istac.indicators.web.shared.GetDimensionResult;

@Component
public class GetDimensionActionHandler extends SecurityActionHandler<GetDimensionAction, GetDimensionResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public GetDimensionActionHandler() {
        super(GetDimensionAction.class);
    }

    @Override
    public GetDimensionResult executeSecurityAction(GetDimensionAction action) throws ActionException {
        try {
            DimensionDto dimensionDto = indicatorsServiceFacade.retrieveDimension(ServiceContextHolder.getCurrentServiceContext(), action.getUuid());
            return new GetDimensionResult(dimensionDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(GetDimensionAction action, GetDimensionResult result, ExecutionContext context) throws ActionException {

    }

}
