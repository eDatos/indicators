package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.PlanifyPopulateIndicatorDataAction;
import es.gobcan.istac.indicators.web.shared.PlanifyPopulateIndicatorDataResult;

@Component
public class PlanifyPopulateIndicatorDataActionHandler extends SecurityActionHandler<PlanifyPopulateIndicatorDataAction, PlanifyPopulateIndicatorDataResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public PlanifyPopulateIndicatorDataActionHandler() {
        super(PlanifyPopulateIndicatorDataAction.class);
    }

    @Override
    public PlanifyPopulateIndicatorDataResult executeSecurityAction(PlanifyPopulateIndicatorDataAction action) throws ActionException {
        try {
            indicatorsServiceFacade.planifyPopulateIndicatorData(ServiceContextHolder.getCurrentServiceContext(), action.getIndicatorUuid());
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(ServiceContextHolder.getCurrentServiceContext(), action.getIndicatorUuid(), null);

            return new PlanifyPopulateIndicatorDataResult(indicatorDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(PlanifyPopulateIndicatorDataAction action, PlanifyPopulateIndicatorDataResult result, ExecutionContext context) throws ActionException {

    }

}
