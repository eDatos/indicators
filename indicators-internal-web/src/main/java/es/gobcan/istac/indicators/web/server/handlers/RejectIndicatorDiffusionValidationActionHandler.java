package es.gobcan.istac.indicators.web.server.handlers;

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
import es.gobcan.istac.indicators.web.shared.RejectIndicatorDiffusionValidationAction;
import es.gobcan.istac.indicators.web.shared.RejectIndicatorDiffusionValidationResult;

@Component
public class RejectIndicatorDiffusionValidationActionHandler extends AbstractActionHandler<RejectIndicatorDiffusionValidationAction, RejectIndicatorDiffusionValidationResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public RejectIndicatorDiffusionValidationActionHandler() {
        super(RejectIndicatorDiffusionValidationAction.class);
    }

    @Override
    public RejectIndicatorDiffusionValidationResult execute(RejectIndicatorDiffusionValidationAction action, ExecutionContext context) throws ActionException {
        try {
            IndicatorDto indicatorDto = indicatorsServiceFacade.rejectIndicatorDiffusionValidation(ServiceContextHelper.getServiceContext(), action.getUuid());
            return new RejectIndicatorDiffusionValidationResult(indicatorDto);
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(RejectIndicatorDiffusionValidationAction action, RejectIndicatorDiffusionValidationResult result, ExecutionContext context) throws ActionException {

    }

}