package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.web.server.ServiceContextHelper;
import es.gobcan.istac.indicators.web.server.services.IndicatorsServiceWrapper;
import es.gobcan.istac.indicators.web.shared.CreateDimensionAction;
import es.gobcan.istac.indicators.web.shared.CreateDimensionResult;

@Component
public class CreateDimensionHandler extends AbstractActionHandler<CreateDimensionAction, CreateDimensionResult> {

    @Autowired
    private IndicatorsServiceWrapper service;
    
    public CreateDimensionHandler() {
        super(CreateDimensionAction.class);
    }

    @Override
    public CreateDimensionResult execute(CreateDimensionAction action, ExecutionContext context) throws ActionException {
        try {
            IndicatorsSystemDto system = action.getIndicatorsSystem();
            DimensionDto createdDim = service.createDimension(ServiceContextHelper.getServiceContext(),system, action.getDimension());
            return new CreateDimensionResult(createdDim);
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(CreateDimensionAction action, CreateDimensionResult result, ExecutionContext context) throws ActionException {

    }

}
