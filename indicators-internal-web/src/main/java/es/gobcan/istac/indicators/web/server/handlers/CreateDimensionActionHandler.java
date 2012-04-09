package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.operations.internal.ws.v1_0.domain.OperationBase;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.ServiceContextHelper;
import es.gobcan.istac.indicators.web.server.utils.DtoUtils;
import es.gobcan.istac.indicators.web.server.ws.StatisticalOperationsInternalWebServiceFacade;
import es.gobcan.istac.indicators.web.shared.CreateDimensionAction;
import es.gobcan.istac.indicators.web.shared.CreateDimensionResult;

@Component
public class CreateDimensionActionHandler extends AbstractActionHandler<CreateDimensionAction, CreateDimensionResult> {

    @Autowired
    private IndicatorsServiceFacade                       indicatorsServiceFacade;

    @Autowired
    private StatisticalOperationsInternalWebServiceFacade statisticalOperationsInternalWebServiceFacade;

    public CreateDimensionActionHandler() {
        super(CreateDimensionAction.class);
    }

    @Override
    public CreateDimensionResult execute(CreateDimensionAction action, ExecutionContext context) throws ActionException {
        IndicatorsSystemDto indicatorsSystemDto = null;
        // Check if operation (indicators system) exists in the DB
        try {
            // If exists, create dimension
            indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(ServiceContextHelper.getServiceContext(), action.getIndicatorsSystem().getCode(), null);
        } catch (MetamacException e) {
            // If does not exist, create a new indicators system and set operation values
            try {
                // Retrieve operation from WS
                OperationBase operationBase = statisticalOperationsInternalWebServiceFacade.retrieveOperation(action.getIndicatorsSystem().getCode());
                // Set values to indicators system
                indicatorsSystemDto = DtoUtils.getIndicatorsSystemDtoFromOperationBase(new IndicatorsSystemDto(), operationBase);
                // Create indicators system
                indicatorsSystemDto = indicatorsServiceFacade.createIndicatorsSystem(ServiceContextHelper.getServiceContext(), indicatorsSystemDto);
            } catch (MetamacException e1) {
                throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e1.getExceptionItems()));
            }
        }
        // Create Dimension
        try {
            DimensionDto dimensionDto = indicatorsServiceFacade.createDimension(ServiceContextHelper.getServiceContext(), indicatorsSystemDto.getUuid(), action.getDimension());
            return new CreateDimensionResult(dimensionDto);
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(CreateDimensionAction action, CreateDimensionResult result, ExecutionContext context) throws ActionException {

    }

}
