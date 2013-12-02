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

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.criteria.IndicatorsSystemCriteriaPropertyEnum;
import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.IndicatorSummaryDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemSummaryDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.rest.StatisticalOperationsRestInternalFacade;
import es.gobcan.istac.indicators.web.server.utils.DtoUtils;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValuesPaginatedListAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValuesPaginatedListResult;
import es.gobcan.istac.indicators.web.shared.GetIndicatorPaginatedListResult;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemPaginatedListAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemPaginatedListResult;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemSummaryDtoWeb;

@Component
public class GetGeographicalValuesPaginatedListActionHandler extends SecurityActionHandler<GetGeographicalValuesPaginatedListAction, GetGeographicalValuesPaginatedListResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public GetGeographicalValuesPaginatedListActionHandler() {
        super(GetGeographicalValuesPaginatedListAction.class);
    }

    @Override
    public GetGeographicalValuesPaginatedListResult executeSecurityAction(GetGeographicalValuesPaginatedListAction action) throws ActionException {
        MetamacCriteria criteria = new MetamacCriteria();
        criteria.setPaginator(new MetamacCriteriaPaginator());
        criteria.getPaginator().setFirstResult(action.getFirstResult());
        criteria.getPaginator().setMaximumResultSize(action.getMaxResults());
        criteria.getPaginator().setCountTotalResults(true);

        try {
            MetamacCriteriaResult<GeographicalValueDto> result = indicatorsServiceFacade.findGeographicalValues(ServiceContextHolder.getCurrentServiceContext(), criteria);
            return new GetGeographicalValuesPaginatedListResult(result.getResults(), result.getPaginatorResult().getTotalResults());
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
