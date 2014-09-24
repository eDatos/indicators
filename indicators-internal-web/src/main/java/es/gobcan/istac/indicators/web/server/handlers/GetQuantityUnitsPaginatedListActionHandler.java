package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaConjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.QuantityUnitDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.utils.MetamacWebCriteriaUtils;
import es.gobcan.istac.indicators.web.shared.GetQuantityUnitsPaginatedListAction;
import es.gobcan.istac.indicators.web.shared.GetQuantityUnitsPaginatedListResult;

@Component
public class GetQuantityUnitsPaginatedListActionHandler extends SecurityActionHandler<GetQuantityUnitsPaginatedListAction, GetQuantityUnitsPaginatedListResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public GetQuantityUnitsPaginatedListActionHandler() {
        super(GetQuantityUnitsPaginatedListAction.class);
    }

    @Override
    public GetQuantityUnitsPaginatedListResult executeSecurityAction(GetQuantityUnitsPaginatedListAction action) throws ActionException {
        MetamacCriteria criteria = new MetamacCriteria();

        // Criteria
        MetamacCriteriaConjunctionRestriction restriction = new MetamacCriteriaConjunctionRestriction();
        restriction.getRestrictions().add(MetamacWebCriteriaUtils.buildMetamacCriteriaFromWebcriteria(action.getCriteria()));
        criteria.setRestriction(restriction);

        criteria.setOrdersBy(action.getCriteria().getOrders());

        criteria.setPaginator(new MetamacCriteriaPaginator());
        criteria.getPaginator().setFirstResult(action.getCriteria().getFirstResult());
        criteria.getPaginator().setMaximumResultSize(action.getCriteria().getMaxResults());
        criteria.getPaginator().setCountTotalResults(true);

        try {
            MetamacCriteriaResult<QuantityUnitDto> result = indicatorsServiceFacade.findQuantityUnits(ServiceContextHolder.getCurrentServiceContext(), criteria);
            return new GetQuantityUnitsPaginatedListResult(result.getResults(), result.getPaginatorResult().getTotalResults());
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
