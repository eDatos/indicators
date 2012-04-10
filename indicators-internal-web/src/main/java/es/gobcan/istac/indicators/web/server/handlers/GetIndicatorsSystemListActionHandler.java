package es.gobcan.istac.indicators.web.server.handlers;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.operations.internal.ws.v1_0.domain.OperationBase;
import org.siemac.metamac.statistical.operations.internal.ws.v1_0.domain.OperationBaseList;
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
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemListAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemListResult;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemDtoWeb;

@Component
public class GetIndicatorsSystemListActionHandler extends AbstractActionHandler<GetIndicatorsSystemListAction, GetIndicatorsSystemListResult> {

    @Autowired
    private IndicatorsServiceFacade                       indicatorsServiceFacade;

    @Autowired
    private StatisticalOperationsInternalWebServiceFacade statisticalOperationsInternalWebServiceFacade;

    public GetIndicatorsSystemListActionHandler() {
        super(GetIndicatorsSystemListAction.class);
    }

    @Override
    public GetIndicatorsSystemListResult execute(GetIndicatorsSystemListAction action, ExecutionContext context) throws ActionException {
        List<IndicatorsSystemDtoWeb> indicatorsSystemDtos = new ArrayList<IndicatorsSystemDtoWeb>();
        OperationBaseList operationBaseList = statisticalOperationsInternalWebServiceFacade.findOperationsIndicatorsSystem();
        if (operationBaseList != null && operationBaseList.getOperation() != null) {
            for (OperationBase operationBase : operationBaseList.getOperation()) {
                // Check if operation (indicators system) exists in the DB
                try {
                    // If exists, updates indicators system
                    IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(ServiceContextHelper.getServiceContext(), operationBase.getCode(), null);
                    indicatorsSystemDtos.add(DtoUtils.updateIndicatorsSystemDtoWeb(new IndicatorsSystemDtoWeb(), indicatorsSystemDto, operationBase));
                } catch (MetamacException e) {
                    indicatorsSystemDtos.add(DtoUtils.createIndicatorsSystemDtoWeb(operationBase));
                }

            }
        }
        return new GetIndicatorsSystemListResult(indicatorsSystemDtos);
    }

    @Override
    public void undo(GetIndicatorsSystemListAction action, GetIndicatorsSystemListResult result, ExecutionContext context) throws ActionException {
    }

}
