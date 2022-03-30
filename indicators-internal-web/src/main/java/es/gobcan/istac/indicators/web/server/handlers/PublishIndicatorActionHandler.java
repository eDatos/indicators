package es.gobcan.istac.indicators.web.server.handlers;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import es.gobcan.istac.indicators.core.dto.PublishIndicatorResultDto;
import es.gobcan.istac.indicators.core.mapper.Dto2DoMapper;
import es.gobcan.istac.indicators.core.service.NoticesRestInternalService;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.PublishIndicatorAction;
import es.gobcan.istac.indicators.web.shared.PublishIndicatorResult;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PublishIndicatorActionHandler extends SecurityActionHandler<PublishIndicatorAction, PublishIndicatorResult> {

    private final Logger               LOG = LoggerFactory.getLogger(PublishIndicatorActionHandler.class);

    @Autowired
    private IndicatorsServiceFacade    indicatorsServiceFacade;

    @Autowired
    private NoticesRestInternalService noticesRestInternalService;

    @Autowired
    private Dto2DoMapper               dto2DoMapper;

    public PublishIndicatorActionHandler() {
        super(PublishIndicatorAction.class);
    }

    @Override
    public PublishIndicatorResult executeSecurityAction(PublishIndicatorAction action) throws ActionException {
        ServiceContext serviceContext = ServiceContextHolder.getCurrentServiceContext();
        PublishIndicatorResultDto publishResult;

        try {
            publishResult = indicatorsServiceFacade.publishIndicator(ServiceContextHolder.getCurrentServiceContext(), action.getUuid());
            if (publishResult.getPublicationFailedReason() != null) {
                LOG.error("Error publishing indicator ", publishResult.getPublicationFailedReason());
                throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItems(null, publishResult.getPublicationFailedReason().getExceptionItems()));
            }
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }

        MetamacWebException resultException = null;
        if (publishResult.getNotificationFailedReason() != null) {
            resultException = WebExceptionUtils.createMetamacWebException(publishResult.getNotificationFailedReason());
            try {
                noticesRestInternalService.createIndicatorStreamMessageErrorBackgroundNotification(dto2DoMapper.indicatorDtoToDo(serviceContext, publishResult.getIndicator()));
            } catch (MetamacException mapperException) {
                throw WebExceptionUtils.createMetamacWebException(mapperException);
            }
        }

        return new PublishIndicatorResult(publishResult.getIndicator(), resultException);
    }

    @Override
    public void undo(PublishIndicatorAction action, PublishIndicatorResult result, ExecutionContext context) throws ActionException {

    }

}
