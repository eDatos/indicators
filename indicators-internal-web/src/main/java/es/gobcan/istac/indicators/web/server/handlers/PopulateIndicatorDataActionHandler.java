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
import es.gobcan.istac.indicators.web.shared.PopulateIndicatorDataAction;
import es.gobcan.istac.indicators.web.shared.PopulateIndicatorDataResult;

@Component
public class PopulateIndicatorDataActionHandler extends SecurityActionHandler<PopulateIndicatorDataAction, PopulateIndicatorDataResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public PopulateIndicatorDataActionHandler() {
        super(PopulateIndicatorDataAction.class);
    }

    @Override
    public PopulateIndicatorDataResult executeSecurityAction(PopulateIndicatorDataAction action) throws ActionException {
        try {
            indicatorsServiceFacade.planifyPopulateIndicatorData(ServiceContextHolder.getCurrentServiceContext(), action.getIndicatorUuid());
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(ServiceContextHolder.getCurrentServiceContext(), action.getIndicatorUuid(), null);

            return new PopulateIndicatorDataResult(indicatorDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(PopulateIndicatorDataAction action, PopulateIndicatorDataResult result, ExecutionContext context) throws ActionException {

    }

}
