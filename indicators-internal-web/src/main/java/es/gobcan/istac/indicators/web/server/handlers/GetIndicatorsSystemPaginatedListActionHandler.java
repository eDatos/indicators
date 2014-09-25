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
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operations;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.criteria.IndicatorsSystemCriteriaPropertyEnum;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemSummaryDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.rest.StatisticalOperationsRestInternalFacade;
import es.gobcan.istac.indicators.web.server.utils.DtoUtils;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemPaginatedListAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemPaginatedListResult;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemSummaryDtoWeb;

@Component
public class GetIndicatorsSystemPaginatedListActionHandler extends SecurityActionHandler<GetIndicatorsSystemPaginatedListAction, GetIndicatorsSystemPaginatedListResult> {

    @Autowired
    private IndicatorsServiceFacade                 indicatorsServiceFacade;

    @Autowired
    private StatisticalOperationsRestInternalFacade statisticalOperationsRestInternalFacade;

    public GetIndicatorsSystemPaginatedListActionHandler() {
        super(GetIndicatorsSystemPaginatedListAction.class);
    }

    @Override
    public GetIndicatorsSystemPaginatedListResult executeSecurityAction(GetIndicatorsSystemPaginatedListAction action) throws ActionException {
        List<IndicatorsSystemSummaryDtoWeb> indicatorsSystemSummaryDtoWebs = new ArrayList<IndicatorsSystemSummaryDtoWeb>();
        int totalResults = 0;
        int firstResult = 0;
        Operations result = statisticalOperationsRestInternalFacade.findOperationsIndicatorsSystem(ServiceContextHolder.getCurrentServiceContext(), action.getFirstResult(), action.getMaxResults());
        if (result != null && result.getOperations() != null) {
            firstResult = result.getOffset().intValue();
            totalResults = result.getTotal().intValue();
            for (Resource resource : result.getOperations()) {
                // Check if operation (indicators system) exists in the DB
                MetamacCriteria criteria = new MetamacCriteria();
                criteria.setPaginator(new MetamacCriteriaPaginator());
                criteria.getPaginator().setMaximumResultSize(1);
                MetamacCriteriaPropertyRestriction restriction = new MetamacCriteriaPropertyRestriction(IndicatorsSystemCriteriaPropertyEnum.CODE.name(), resource.getId(), OperationType.EQ);
                criteria.setRestriction(restriction);
                try {
                    IndicatorsSystemSummaryDtoWeb indicatorsSystemSummaryDtoWeb = null;
                    MetamacCriteriaResult<IndicatorsSystemSummaryDto> systems = indicatorsServiceFacade.findIndicatorsSystems(ServiceContextHolder.getCurrentServiceContext(), criteria);
                    if (!CollectionUtils.isEmpty(systems.getResults())) {
                        // If exists, updates indicators system
                        IndicatorsSystemSummaryDto indicatorsSystemSummaryDto = systems.getResults().get(0);
                        indicatorsSystemSummaryDtoWeb = DtoUtils.updateIndicatorsSystemSummaryDtoWeb(new IndicatorsSystemSummaryDtoWeb(), indicatorsSystemSummaryDto, resource);
                    } else {
                        // If not, create a new indicators system
                        indicatorsSystemSummaryDtoWeb = DtoUtils.createIndicatorsSystemSummaryDtoWeb(resource);
                    }
                    indicatorsSystemSummaryDtoWebs.add(indicatorsSystemSummaryDtoWeb);
                } catch (MetamacException e) {
                    throw WebExceptionUtils.createMetamacWebException(e);
                }
            }
        }
        return new GetIndicatorsSystemPaginatedListResult(indicatorsSystemSummaryDtoWebs, firstResult, totalResults);
    }
}
