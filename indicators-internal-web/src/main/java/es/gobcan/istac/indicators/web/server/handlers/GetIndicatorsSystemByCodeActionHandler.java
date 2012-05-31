package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.operations.internal.ws.v1_0.domain.OperationBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import es.gobcan.istac.indicators.web.server.utils.DtoUtils;
import es.gobcan.istac.indicators.web.server.ws.StatisticalOperationsInternalWebServiceFacade;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemByCodeAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemByCodeResult;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemDtoWeb;

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
        // Retrieve operation from WS
        OperationBase operationBase = statisticalOperationsInternalWebServiceFacade.retrieveOperation(action.getCode());
        // Check if operation (indicators system) exists in the DB
        try {
            // If exists, updates indicators system
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(ServiceContextHolder.getCurrentServiceContext(), action.getCode(),
                    action.getVersionNumber());
            return new GetIndicatorsSystemByCodeResult(DtoUtils.updateIndicatorsSystemDtoWeb(new IndicatorsSystemDtoWeb(), indicatorsSystemDto, operationBase));
        } catch (MetamacException e) {
            return new GetIndicatorsSystemByCodeResult(DtoUtils.createIndicatorsSystemDtoWeb(operationBase));
        }
    }

    @Override
    public void undo(GetIndicatorsSystemByCodeAction action, GetIndicatorsSystemByCodeResult result, ExecutionContext context) throws ActionException {
    }

}
