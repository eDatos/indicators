package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.UnitMultiplierDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.GetUnitMultipliersPaginatedListAction;
import es.gobcan.istac.indicators.web.shared.GetUnitMultipliersPaginatedListResult;

@Component
public class GetUnitMultipliersPaginatedListActionHandler extends SecurityActionHandler<GetUnitMultipliersPaginatedListAction, GetUnitMultipliersPaginatedListResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public GetUnitMultipliersPaginatedListActionHandler() {
        super(GetUnitMultipliersPaginatedListAction.class);
    }

    @Override
    public GetUnitMultipliersPaginatedListResult executeSecurityAction(GetUnitMultipliersPaginatedListAction action) throws ActionException {
        MetamacCriteria criteria = new MetamacCriteria();
        criteria.setPaginator(new MetamacCriteriaPaginator());
        criteria.getPaginator().setFirstResult(action.getFirstResult());
        criteria.getPaginator().setMaximumResultSize(action.getMaxResults());
        criteria.getPaginator().setCountTotalResults(true);

        try {
            MetamacCriteriaResult<UnitMultiplierDto> result = indicatorsServiceFacade.findUnitMultipliers(ServiceContextHolder.getCurrentServiceContext(), criteria);
            return new GetUnitMultipliersPaginatedListResult(result.getResults(), result.getPaginatorResult().getTotalResults());
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
