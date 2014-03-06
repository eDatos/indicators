package es.gobcan.istac.indicators.web.server.handlers;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction.OperationType;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.siemac.metamac.web.common.shared.exception.MetamacWebExceptionItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.criteria.GeographicalValueCriteriaPropertyEnum;
import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValuesAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValuesResult;

@Component
public class GetGeographicalValuesActionHandler extends SecurityActionHandler<GetGeographicalValuesAction, GetGeographicalValuesResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public GetGeographicalValuesActionHandler() {
        super(GetGeographicalValuesAction.class);
    }

    @Override
    public GetGeographicalValuesResult executeSecurityAction(GetGeographicalValuesAction action) throws ActionException {
        try {
            MetamacCriteria criteria = new MetamacCriteria();
            criteria.setPaginator(new MetamacCriteriaPaginator());
            criteria.getPaginator().setCountTotalResults(Boolean.TRUE);
            criteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);

            if (action.getGeographicalGranularityUuid() != null) {
                criteria.setRestriction(new MetamacCriteriaPropertyRestriction(GeographicalValueCriteriaPropertyEnum.GEOGRAPHICAL_GRANULARITY_UUID.name(), action.getGeographicalGranularityUuid(),
                        OperationType.EQ));
            }
            MetamacCriteriaResult<GeographicalValueDto> result = indicatorsServiceFacade.findGeographicalValues(ServiceContextHolder.getCurrentServiceContext(), criteria);
            if (result.getResults().size() != result.getPaginatorResult().getTotalResults().intValue()) {
                MetamacWebExceptionItem metamacWebExceptionItem = new MetamacWebExceptionItem("exception.web.geographical.value.result.limit",
                        "Error retrieving geographical values. Please contact system administrator.");
                List<MetamacWebExceptionItem> exceptionItems = new ArrayList<MetamacWebExceptionItem>();
                exceptionItems.add(metamacWebExceptionItem);
                throw new MetamacWebException(exceptionItems);
            } else {
                return new GetGeographicalValuesResult(result.getResults());
            }
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
