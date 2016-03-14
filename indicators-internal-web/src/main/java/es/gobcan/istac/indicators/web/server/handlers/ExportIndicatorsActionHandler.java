package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaConjunctionRestriction;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.utils.MetamacWebCriteriaUtils;
import es.gobcan.istac.indicators.web.shared.ExportIndicatorsAction;
import es.gobcan.istac.indicators.web.shared.ExportIndicatorsResult;

@Component
public class ExportIndicatorsActionHandler extends SecurityActionHandler<ExportIndicatorsAction, ExportIndicatorsResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public ExportIndicatorsActionHandler() {
        super(ExportIndicatorsAction.class);
    }

    @Override
    public ExportIndicatorsResult executeSecurityAction(ExportIndicatorsAction action) throws ActionException {       
        MetamacCriteria criteria = new MetamacCriteria();
        
        // Criteria
        MetamacCriteriaConjunctionRestriction restriction = new MetamacCriteriaConjunctionRestriction();
        restriction.getRestrictions().add(MetamacWebCriteriaUtils.buildMetamacCriteriaFromWebcriteria(action.getCriteria()));
        criteria.setRestriction(restriction);
        
        criteria.setOrdersBy(action.getCriteria().getOrders());
        
        try {
            String fileName = indicatorsServiceFacade.exportIndicatorsTsv(ServiceContextHolder.getCurrentServiceContext(), criteria);
            return new ExportIndicatorsResult(fileName);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
