package es.gobcan.istac.indicators.web.server.handlers;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.TimeValueDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.GetTimeValuesByGranularityInIndicatorAction;
import es.gobcan.istac.indicators.web.shared.GetTimeValuesByGranularityInIndicatorResult;

@Component
public class GetTimeValuesByGranularityInIndicatorActionHandler extends SecurityActionHandler<GetTimeValuesByGranularityInIndicatorAction, GetTimeValuesByGranularityInIndicatorResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public GetTimeValuesByGranularityInIndicatorActionHandler() {
        super(GetTimeValuesByGranularityInIndicatorAction.class);
    }

    @Override
    public GetTimeValuesByGranularityInIndicatorResult executeSecurityAction(GetTimeValuesByGranularityInIndicatorAction action) throws ActionException {
        try {
            List<TimeValueDto> timeValues = indicatorsServiceFacade.retrieveTimeValuesByGranularityInIndicator(ServiceContextHolder.getCurrentServiceContext(), action.getIndicatorUuid(),
                    action.getIndicatorVersion(), action.getTimeGranularity());

            return new GetTimeValuesByGranularityInIndicatorResult(timeValues);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
