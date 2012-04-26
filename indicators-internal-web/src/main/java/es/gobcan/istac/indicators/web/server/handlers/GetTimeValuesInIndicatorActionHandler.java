package es.gobcan.istac.indicators.web.server.handlers;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.TimeValueDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.ServiceContextHelper;
import es.gobcan.istac.indicators.web.shared.GetTimeValuesInIndicatorAction;
import es.gobcan.istac.indicators.web.shared.GetTimeValuesInIndicatorResult;

@Component
public class GetTimeValuesInIndicatorActionHandler extends AbstractActionHandler<GetTimeValuesInIndicatorAction, GetTimeValuesInIndicatorResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public GetTimeValuesInIndicatorActionHandler() {
        super(GetTimeValuesInIndicatorAction.class);
    }

    @Override
    public GetTimeValuesInIndicatorResult execute(GetTimeValuesInIndicatorAction action, ExecutionContext context) throws ActionException {
        try {
            List<TimeValueDto> timeValues = indicatorsServiceFacade.retrieveTimeValuesInIndicator(ServiceContextHelper.getServiceContext(), action.getIndicatorUuid(), action.getIndicatorVersion());
            return new GetTimeValuesInIndicatorResult(timeValues);
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(GetTimeValuesInIndicatorAction action, GetTimeValuesInIndicatorResult result, ExecutionContext context) throws ActionException {

    }

}
