package es.gobcan.istac.indicators.web.server.handlers;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.gopestat.internal.ws.v1_0.MetamacExceptionFault;
import org.siemac.metamac.gopestat.internal.ws.v1_0.domain.OperationBase;
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
import es.gobcan.istac.indicators.web.server.utils.DtoUtils;
import es.gobcan.istac.indicators.web.server.utils.WSExceptionUtils;
import es.gobcan.istac.indicators.web.server.ws.WebservicesLocator;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemByCodeAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemByCodeResult;

@Component
public class GetIndicatorsSystemByCodeActionHandler extends AbstractActionHandler<GetIndicatorsSystemByCodeAction, GetIndicatorsSystemByCodeResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;
    
    @Autowired
    private WebservicesLocator webservicesLocator;
    
    
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
            OperationBase operationBase;
            try {
                operationBase = webservicesLocator.getGopestatInternalInterface().retrieveOperation(action.getCode());
                IndicatorsSystemDto indicatorsSystemDto = DtoUtils.getIndicatorsSystemDtoFromOperationBase(new IndicatorsSystemDto(), operationBase);
                return new GetIndicatorsSystemByCodeResult(indicatorsSystemDto);
            } catch (MetamacExceptionFault e1) {
                List<MetamacExceptionItem> metamacExceptionItems = WSExceptionUtils.getMetamacExceptionItems(e1.getFaultInfo().getExceptionItems());
                throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(metamacExceptionItems));
            }
        }
    }

    @Override
    public void undo(GetIndicatorsSystemByCodeAction action, GetIndicatorsSystemByCodeResult result, ExecutionContext context) throws ActionException {
    }

}
