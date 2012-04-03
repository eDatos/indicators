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

@Component
public class GetIndicatorsSystemListActionHandler extends AbstractActionHandler<GetIndicatorsSystemListAction, GetIndicatorsSystemListResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    @Autowired
    private StatisticalOperationsInternalWebServiceFacade statisticalOperationsInternalWebServiceFacade;


    public GetIndicatorsSystemListActionHandler() {
        super(GetIndicatorsSystemListAction.class);
    }

    @Override
    public GetIndicatorsSystemListResult execute(GetIndicatorsSystemListAction action, ExecutionContext context) throws ActionException {
        List<IndicatorsSystemDto> indicatorsSystemDtos = new ArrayList<IndicatorsSystemDto>();
        OperationBaseList operationBaseList = statisticalOperationsInternalWebServiceFacade.findOperationsIndicatorsSystem();
        if (operationBaseList.getOperation() != null) {
            for (OperationBase operationBase : operationBaseList.getOperation()) {
                // Check if operation (indicators system) exists in the DB
                try {
                    // If exists, update its values with the operation ones
                    IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(ServiceContextHelper.getServiceContext(), operationBase.getCode(), null);
                    // indicatorsSystemDto = DtoUtils.getIndicatorsSystemDtoFromOperationBase(indicatorsSystemDto, operationBase);
                    indicatorsSystemDtos.add(indicatorsSystemDto);
                } catch (MetamacException e) {
                    // If does not exist, create a new indicators system and set operation values
                    IndicatorsSystemDto indicatorsSystemDto = DtoUtils.getIndicatorsSystemDtoFromOperationBase(new IndicatorsSystemDto(), operationBase);
                    indicatorsSystemDtos.add(indicatorsSystemDto);
                }
            }
        }
        return new GetIndicatorsSystemListResult(indicatorsSystemDtos);
    }

    @Override
    public void undo(GetIndicatorsSystemListAction action, GetIndicatorsSystemListResult result, ExecutionContext context) throws ActionException {
    }

}
