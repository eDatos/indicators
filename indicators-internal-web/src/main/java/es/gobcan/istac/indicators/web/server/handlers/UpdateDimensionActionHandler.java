package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.DimensionDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import es.gobcan.istac.indicators.web.shared.UpdateDimensionAction;
import es.gobcan.istac.indicators.web.shared.UpdateDimensionResult;

@Component
public class UpdateDimensionActionHandler extends AbstractActionHandler<UpdateDimensionAction, UpdateDimensionResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public UpdateDimensionActionHandler() {
        super(UpdateDimensionAction.class);
    }

    @Override
    public UpdateDimensionResult execute(UpdateDimensionAction action, ExecutionContext context) throws ActionException {
        try {
            DimensionDto dimensionDto = indicatorsServiceFacade.updateDimension(ServiceContextHolder.getCurrentServiceContext(), action.getDimensionToUpdate());
            return new UpdateDimensionResult(dimensionDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(UpdateDimensionAction action, UpdateDimensionResult result, ExecutionContext context) throws ActionException {

    }

}
