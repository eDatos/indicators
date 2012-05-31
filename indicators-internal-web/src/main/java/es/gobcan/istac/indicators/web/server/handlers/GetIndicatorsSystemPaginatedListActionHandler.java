package es.gobcan.istac.indicators.web.server.handlers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction.OperationType;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.operations.internal.ws.v1_0.domain.FindOperationsResult;
import org.siemac.metamac.statistical.operations.internal.ws.v1_0.domain.OperationBase;
import org.siemac.metamac.statistical.operations.internal.ws.v1_0.domain.OperationBaseList;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.criteria.IndicatorsSystemCriteriaPropertyEnum;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemSummaryDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import es.gobcan.istac.indicators.web.server.utils.DtoUtils;
import es.gobcan.istac.indicators.web.server.ws.StatisticalOperationsInternalWebServiceFacade;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemPaginatedListAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemPaginatedListResult;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemSummaryDtoWeb;

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
        List<IndicatorsSystemSummaryDtoWeb> indicatorsSystemDtos = new ArrayList<IndicatorsSystemSummaryDtoWeb>();
        int totalResults = 0;
        FindOperationsResult findOperationsResult = statisticalOperationsInternalWebServiceFacade.findOperationsIndicatorsSystem(action.getFirstResult(), action.getMaxResults());
        OperationBaseList operationBaseList = findOperationsResult.getOperations();
        if (operationBaseList != null && operationBaseList.getOperation() != null) {
            totalResults = findOperationsResult.getTotalResults().intValue();
            for (OperationBase operationBase : operationBaseList.getOperation()) {
                // Check if operation (indicators system) exists in the DB
                MetamacCriteria criteria = new MetamacCriteria();
                criteria.setPaginator(new MetamacCriteriaPaginator());
                criteria.getPaginator().setMaximumResultSize(1);
                MetamacCriteriaPropertyRestriction restriction = new MetamacCriteriaPropertyRestriction(IndicatorsSystemCriteriaPropertyEnum.CODE.name(), operationBase.getCode(), OperationType.EQ);
                criteria.setRestriction(restriction);
                try {
                    MetamacCriteriaResult<IndicatorsSystemSummaryDto> result = indicatorsServiceFacade.findIndicatorsSystems(ServiceContextHolder.getCurrentServiceContext(), criteria);
                    if (!CollectionUtils.isEmpty(result.getResults())) {
                        // If exists, updates indicators system
                        IndicatorsSystemSummaryDto indicatorsSystemSummaryDto = result.getResults().get(0);
                        indicatorsSystemDtos.add(DtoUtils.updateIndicatorsSystemSummaryDtoWeb(new IndicatorsSystemSummaryDtoWeb(), indicatorsSystemSummaryDto, operationBase));
                    } else {
                        // If not, create a new indicators system
                        indicatorsSystemDtos.add(DtoUtils.createIndicatorsSystemSummaryDtoWeb(operationBase));
                    }
                } catch (MetamacException e) {
                    throw WebExceptionUtils.createMetamacWebException(e);
                }
            }
        }
        return new GetIndicatorsSystemPaginatedListResult(indicatorsSystemDtos, totalResults);
    }

    @Override
    public void undo(GetIndicatorsSystemPaginatedListAction action, GetIndicatorsSystemPaginatedListResult result, ExecutionContext context) throws ActionException {

    }

}
