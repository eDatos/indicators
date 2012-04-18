package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.ServiceContextHelper;
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

        try {
            MetamacCriteriaResult<IndicatorDto> result = indicatorsServiceFacade.findIndicators(ServiceContextHelper.getServiceContext(), criteria);
            return new GetIndicatorPaginatedListResult(result.getResults());
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(GetIndicatorPaginatedListAction action, GetIndicatorPaginatedListResult result, ExecutionContext context) throws ActionException {

    }

}
