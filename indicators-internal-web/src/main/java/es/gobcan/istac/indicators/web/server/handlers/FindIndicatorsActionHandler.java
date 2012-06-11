package es.gobcan.istac.indicators.web.server.handlers;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaConjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction.OperationType;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.criteria.IndicatorCriteriaPropertyEnum;
import es.gobcan.istac.indicators.core.dto.IndicatorSummaryDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.FindIndicatorsAction;
import es.gobcan.istac.indicators.web.shared.FindIndicatorsResult;
import es.gobcan.istac.indicators.web.shared.criteria.IndicatorCriteria;

@Component
public class FindIndicatorsActionHandler extends SecurityActionHandler<FindIndicatorsAction, FindIndicatorsResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public FindIndicatorsActionHandler() {
        super(FindIndicatorsAction.class);
    }

    @Override
    public FindIndicatorsResult executeSecurityAction(FindIndicatorsAction action) throws ActionException {
        IndicatorCriteria indicatorCriteria = action.getCriteria();

        MetamacCriteria criteria = new MetamacCriteria();
        criteria.setPaginator(new MetamacCriteriaPaginator());
        criteria.getPaginator().setFirstResult(0);
        criteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
        criteria.getPaginator().setCountTotalResults(true);

        MetamacCriteriaConjunctionRestriction conjuction = new MetamacCriteriaConjunctionRestriction();
        if (!StringUtils.isBlank(indicatorCriteria.getCode())) {
            conjuction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.CODE.name(), indicatorCriteria.getCode(), OperationType.ILIKE));
        }
        if (!StringUtils.isBlank(indicatorCriteria.getTitle())) {
            conjuction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.TITLE.name(), indicatorCriteria.getTitle(), OperationType.ILIKE));
        }
        criteria.setRestriction(conjuction);

        try {
            MetamacCriteriaResult<IndicatorSummaryDto> result = indicatorsServiceFacade.findIndicators(ServiceContextHolder.getCurrentServiceContext(), criteria);
            return new FindIndicatorsResult(result.getResults());
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(FindIndicatorsAction action, FindIndicatorsResult result, ExecutionContext context) throws ActionException {

    }

}
