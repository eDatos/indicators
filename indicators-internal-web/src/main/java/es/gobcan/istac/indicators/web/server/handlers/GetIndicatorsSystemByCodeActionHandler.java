package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.operations.internal.ws.v1_0.domain.OperationBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.ServiceContextHelper;
import es.gobcan.istac.indicators.web.server.utils.DtoUtils;
import es.gobcan.istac.indicators.web.server.ws.StatisticalOperationsInternalWebServiceFacade;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemByCodeAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemByCodeResult;

@Component
public class GetIndicatorsSystemByCodeActionHandler extends AbstractActionHandler<GetIndicatorsSystemByCodeAction, GetIndicatorsSystemByCodeResult> {

    @Autowired
    private IndicatorsServiceFacade                       indicatorsServiceFacade;

    @Autowired
    private StatisticalOperationsInternalWebServiceFacade statisticalOperationsInternalWebServiceFacade;

    public GetIndicatorsSystemByCodeActionHandler() {
        super(GetIndicatorsSystemByCodeAction.class);
    }

    @Override
    public GetIndicatorsSystemByCodeResult execute(GetIndicatorsSystemByCodeAction action, ExecutionContext context) throws ActionException {
        // Check if operation (indicators system) exists in the DB
        try {
            // If exists, return indicators system
            // TODO Values should be updated with the operation ones????
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(ServiceContextHelper.getServiceContext(), action.getCode(), null);
            return new GetIndicatorsSystemByCodeResult(indicatorsSystemDto);
        } catch (MetamacException e) {
            // If does not exist, create a new indicators system and set operation values
            // Retrieve operation from WS
            OperationBase operationBase = statisticalOperationsInternalWebServiceFacade.retrieveOperation(action.getCode());
            IndicatorsSystemDto indicatorsSystemDto = DtoUtils.getIndicatorsSystemDtoFromOperationBase(new IndicatorsSystemDto(), operationBase);
            return new GetIndicatorsSystemByCodeResult(indicatorsSystemDto);
        }
    }

    @Override
    public void undo(GetIndicatorsSystemByCodeAction action, GetIndicatorsSystemByCodeResult result, ExecutionContext context) throws ActionException {
    }

}
