package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.ServiceContextHelper;
import es.gobcan.istac.indicators.web.shared.GetIndicatorListAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorListResult;

@Component
public class GetIndicatorListActionHandler extends AbstractActionHandler<GetIndicatorListAction, GetIndicatorListResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public GetIndicatorListActionHandler() {
        super(GetIndicatorListAction.class);
    }

    @Override
    public GetIndicatorListResult execute(GetIndicatorListAction action, ExecutionContext context) throws ActionException {
        try {
            MetamacCriteriaResult<IndicatorDto> result = indicatorsServiceFacade.findIndicators(ServiceContextHelper.getServiceContext(), null);
            return new GetIndicatorListResult(result.getResults());
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(GetIndicatorListAction action, GetIndicatorListResult result, ExecutionContext context) throws ActionException {

    }

}
