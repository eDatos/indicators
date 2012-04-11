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

import es.gobcan.istac.indicators.core.dto.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.ServiceContextHelper;
import es.gobcan.istac.indicators.web.server.utils.DtoUtils;
import es.gobcan.istac.indicators.web.server.ws.StatisticalOperationsInternalWebServiceFacade;
import es.gobcan.istac.indicators.web.shared.CreateIndicatorInstanceAction;
import es.gobcan.istac.indicators.web.shared.CreateIndicatorInstanceResult;

@Component
public class CreateIndicatorInstanceActionHandler extends AbstractActionHandler<CreateIndicatorInstanceAction, CreateIndicatorInstanceResult> {

    @Autowired
    private IndicatorsServiceFacade                       indicatorsServiceFacade;

    @Autowired
    private StatisticalOperationsInternalWebServiceFacade statisticalOperationsInternalWebServiceFacade;

    public CreateIndicatorInstanceActionHandler() {
        super(CreateIndicatorInstanceAction.class);
    }

    @Override
    public CreateIndicatorInstanceResult execute(CreateIndicatorInstanceAction action, ExecutionContext context) throws ActionException {
        IndicatorsSystemDto indicatorsSystemDto = null;
        // Check if operation (indicators system) exists in the DB
        try {
            // If exists, create instance
            indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(ServiceContextHelper.getServiceContext(), action.getIndicatorsSystem().getCode(), null);
        } catch (MetamacException e) {
            // If does not exist, create a new indicators system and set operation values
            try {
                // Retrieve operation from WS
                OperationBase operationBase = statisticalOperationsInternalWebServiceFacade.retrieveOperation(action.getIndicatorsSystem().getCode());
                // Set values to indicators system
                indicatorsSystemDto = DtoUtils.createIndicatorsSystemDtoWeb(operationBase);
                // Create indicators system
                indicatorsSystemDto = indicatorsServiceFacade.createIndicatorsSystem(ServiceContextHelper.getServiceContext(), indicatorsSystemDto);
            } catch (MetamacException e2) {
                throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e2.getExceptionItems()));
            }
        }
        // Create instance
        try {
            IndicatorInstanceDto indicatorInstanceDto = indicatorsServiceFacade.createIndicatorInstance(ServiceContextHelper.getServiceContext(), indicatorsSystemDto.getUuid(),
                    action.getIndicatorInstance());
            return new CreateIndicatorInstanceResult(indicatorInstanceDto);
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(CreateIndicatorInstanceAction action, CreateIndicatorInstanceResult result, ExecutionContext context) throws ActionException {

    }

}
