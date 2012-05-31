package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.PublishIndicatorResultDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import es.gobcan.istac.indicators.web.shared.PublishIndicatorAction;
import es.gobcan.istac.indicators.web.shared.PublishIndicatorResult;

@Component
public class PublishIndicatorActionHandler extends AbstractActionHandler<PublishIndicatorAction, PublishIndicatorResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public PublishIndicatorActionHandler() {
        super(PublishIndicatorAction.class);
    }

    @Override
    public PublishIndicatorResult execute(PublishIndicatorAction action, ExecutionContext context) throws ActionException {
        try {
            PublishIndicatorResultDto publishResult = indicatorsServiceFacade.publishIndicator(ServiceContextHolder.getCurrentServiceContext(), action.getUuid());
            if (publishResult.getPublicationFailedReason() == null) {
                return new PublishIndicatorResult(publishResult.getIndicator());
            } else {
                throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItems(publishResult.getPublicationFailedReason().getExceptionItems()));
            }
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(PublishIndicatorAction action, PublishIndicatorResult result, ExecutionContext context) throws ActionException {

    }

}
