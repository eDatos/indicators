package es.gobcan.istac.indicators.web.server.handlers;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.TimeGranularityDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.ServiceContextHolder;
import es.gobcan.istac.indicators.web.shared.GetTimeGranularitiesInIndicatorAction;
import es.gobcan.istac.indicators.web.shared.GetTimeGranularitiesInIndicatorResult;

@Component
public class GetTimeGranularitiesInIndicatorActionHandler extends AbstractActionHandler<GetTimeGranularitiesInIndicatorAction, GetTimeGranularitiesInIndicatorResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public GetTimeGranularitiesInIndicatorActionHandler() {
        super(GetTimeGranularitiesInIndicatorAction.class);
    }

    @Override
    public GetTimeGranularitiesInIndicatorResult execute(GetTimeGranularitiesInIndicatorAction action, ExecutionContext context) throws ActionException {
        try {
            List<TimeGranularityDto> timeGranularityDtos = indicatorsServiceFacade.retrieveTimeGranularitiesInIndicator(ServiceContextHolder.getCurrentServiceContext(), action.getIndicatorUuid(),
                    action.getIndicatorVersion());
            return new GetTimeGranularitiesInIndicatorResult(timeGranularityDtos);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(GetTimeGranularitiesInIndicatorAction action, GetTimeGranularitiesInIndicatorResult result, ExecutionContext context) throws ActionException {

    }

}
