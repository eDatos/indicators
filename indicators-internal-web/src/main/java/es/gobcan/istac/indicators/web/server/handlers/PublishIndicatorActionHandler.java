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
            IndicatorDto indicatorDto = indicatorsServiceFacade.publishIndicator(ServiceContextHelper.getServiceContext(), action.getUuid());
            return new PublishIndicatorResult(indicatorDto);
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(PublishIndicatorAction action, PublishIndicatorResult result, ExecutionContext context) throws ActionException {

    }

}
