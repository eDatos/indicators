package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical_operations.rest.internal.v1_0.domain.Operation;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.DimensionDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.rest.StatisticalOperationsRestInternalFacade;
import es.gobcan.istac.indicators.web.server.utils.DtoUtils;
import es.gobcan.istac.indicators.web.shared.CreateDimensionAction;
import es.gobcan.istac.indicators.web.shared.CreateDimensionResult;

@Component
public class CreateDimensionActionHandler extends SecurityActionHandler<CreateDimensionAction, CreateDimensionResult> {

    @Autowired
    private IndicatorsServiceFacade                 indicatorsServiceFacade;

    @Autowired
    private StatisticalOperationsRestInternalFacade statisticalOperationsRestInternalFacade;

    public CreateDimensionActionHandler() {
        super(CreateDimensionAction.class);
    }

    @Override
    public CreateDimensionResult executeSecurityAction(CreateDimensionAction action) throws ActionException {
        IndicatorsSystemDto indicatorsSystemDto = null;
        // Check if operation (indicators system) exists in the DB
        try {
            // If exists, create dimension
            indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(ServiceContextHolder.getCurrentServiceContext(), action.getIndicatorsSystem().getCode(), null);
        } catch (MetamacException e) {
            // If does not exist, create a new indicators system and set operation values
            try {
                // Retrieve operation from WS
                Operation operationBase = statisticalOperationsRestInternalFacade.retrieveOperation(action.getIndicatorsSystem().getCode());
                // Set values to indicators system
                indicatorsSystemDto = DtoUtils.createIndicatorsSystemDtoWeb(operationBase);
                // Create indicators system
                indicatorsSystemDto = indicatorsServiceFacade.createIndicatorsSystem(ServiceContextHolder.getCurrentServiceContext(), indicatorsSystemDto);
            } catch (MetamacException e1) {
                throw WebExceptionUtils.createMetamacWebException(e1);
            }
        }
        // Create Dimension
        try {
            DimensionDto dimensionDto = indicatorsServiceFacade.createDimension(ServiceContextHolder.getCurrentServiceContext(), indicatorsSystemDto.getUuid(), action.getDimension());
            return new CreateDimensionResult(dimensionDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(CreateDimensionAction action, CreateDimensionResult result, ExecutionContext context) throws ActionException {

    }

}
