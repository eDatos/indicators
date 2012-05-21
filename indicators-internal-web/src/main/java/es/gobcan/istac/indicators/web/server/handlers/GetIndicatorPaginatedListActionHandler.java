package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.IndicatorSummaryDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.ServiceContextHolder;
import es.gobcan.istac.indicators.web.shared.GetIndicatorPaginatedListAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorPaginatedListResult;

@Component
public class GetIndicatorPaginatedListActionHandler extends AbstractActionHandler<GetIndicatorPaginatedListAction, GetIndicatorPaginatedListResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public GetIndicatorPaginatedListActionHandler() {
        super(GetIndicatorPaginatedListAction.class);
    }

    @Override
    public GetIndicatorPaginatedListResult execute(GetIndicatorPaginatedListAction action, ExecutionContext context) throws ActionException {
        MetamacCriteria criteria = new MetamacCriteria();
        criteria.setPaginator(new MetamacCriteriaPaginator());
        criteria.getPaginator().setFirstResult(action.getFirstResult());
        criteria.getPaginator().setMaximumResultSize(action.getMaxResults());
        criteria.getPaginator().setCountTotalResults(true);

        try {
            MetamacCriteriaResult<IndicatorSummaryDto> result = indicatorsServiceFacade.findIndicators(ServiceContextHolder.getCurrentServiceContext(), criteria);
            return new GetIndicatorPaginatedListResult(result.getResults(), result.getPaginatorResult().getTotalResults());
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(GetIndicatorPaginatedListAction action, GetIndicatorPaginatedListResult result, ExecutionContext context) throws ActionException {

    }

}
