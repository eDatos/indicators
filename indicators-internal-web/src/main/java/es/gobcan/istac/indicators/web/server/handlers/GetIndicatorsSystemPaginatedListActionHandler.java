package es.gobcan.istac.indicators.web.server.handlers;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.operations.internal.ws.v1_0.domain.FindOperationsResult;
import org.siemac.metamac.statistical.operations.internal.ws.v1_0.domain.OperationBase;
import org.siemac.metamac.statistical.operations.internal.ws.v1_0.domain.OperationBaseList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.ServiceContextHolder;
import es.gobcan.istac.indicators.web.server.utils.DtoUtils;
import es.gobcan.istac.indicators.web.server.ws.StatisticalOperationsInternalWebServiceFacade;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemPaginatedListAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemPaginatedListResult;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemDtoWeb;

@Component
public class GetIndicatorsSystemPaginatedListActionHandler extends AbstractActionHandler<GetIndicatorsSystemPaginatedListAction, GetIndicatorsSystemPaginatedListResult> {

    @Autowired
    private IndicatorsServiceFacade                       indicatorsServiceFacade;

    @Autowired
    private StatisticalOperationsInternalWebServiceFacade statisticalOperationsInternalWebServiceFacade;

    public GetIndicatorsSystemPaginatedListActionHandler() {
        super(GetIndicatorsSystemPaginatedListAction.class);
    }

    @Override
    public GetIndicatorsSystemPaginatedListResult execute(GetIndicatorsSystemPaginatedListAction action, ExecutionContext context) throws ActionException {
        List<IndicatorsSystemDtoWeb> indicatorsSystemDtos = new ArrayList<IndicatorsSystemDtoWeb>();
        int totalResults = 0;
        FindOperationsResult findOperationsResult = statisticalOperationsInternalWebServiceFacade.findOperationsIndicatorsSystem(action.getFirstResult(), action.getMaxResults());
        OperationBaseList operationBaseList = findOperationsResult.getOperations();
        if (operationBaseList != null && operationBaseList.getOperation() != null) {
            totalResults = findOperationsResult.getTotalResults().intValue();
            for (OperationBase operationBase : operationBaseList.getOperation()) {
                // Check if operation (indicators system) exists in the DB
                try {
                    // If exists, updates indicators system
                    IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(ServiceContextHolder.getCurrentServiceContext(), operationBase.getCode(), null);
                    indicatorsSystemDtos.add(DtoUtils.updateIndicatorsSystemDtoWeb(new IndicatorsSystemDtoWeb(), indicatorsSystemDto, operationBase));
                } catch (MetamacException e) {
                    indicatorsSystemDtos.add(DtoUtils.createIndicatorsSystemDtoWeb(operationBase));
                }

            }
        }
        return new GetIndicatorsSystemPaginatedListResult(indicatorsSystemDtos, totalResults);
    }

    @Override
    public void undo(GetIndicatorsSystemPaginatedListAction action, GetIndicatorsSystemPaginatedListResult result, ExecutionContext context) throws ActionException {

    }

}
