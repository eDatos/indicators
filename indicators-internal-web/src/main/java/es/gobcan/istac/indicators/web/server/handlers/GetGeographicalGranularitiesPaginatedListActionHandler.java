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

import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.GetGeographicalGranularitiesPaginatedListAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalGranularitiesPaginatedListResult;

@Component
public class GetGeographicalGranularitiesPaginatedListActionHandler extends SecurityActionHandler<GetGeographicalGranularitiesPaginatedListAction, GetGeographicalGranularitiesPaginatedListResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public GetGeographicalGranularitiesPaginatedListActionHandler() {
        super(GetGeographicalGranularitiesPaginatedListAction.class);
    }

    @Override
    public GetGeographicalGranularitiesPaginatedListResult executeSecurityAction(GetGeographicalGranularitiesPaginatedListAction action) throws ActionException {
        MetamacCriteria criteria = new MetamacCriteria();
        criteria.setPaginator(new MetamacCriteriaPaginator());
        criteria.getPaginator().setFirstResult(action.getFirstResult());
        criteria.getPaginator().setMaximumResultSize(action.getMaxResults());
        criteria.getPaginator().setCountTotalResults(true);

        try {
            MetamacCriteriaResult<GeographicalGranularityDto> result = indicatorsServiceFacade.findGeographicalGranularities(ServiceContextHolder.getCurrentServiceContext(), criteria);
            return new GetGeographicalGranularitiesPaginatedListResult(result.getResults(), result.getPaginatorResult().getTotalResults());
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
