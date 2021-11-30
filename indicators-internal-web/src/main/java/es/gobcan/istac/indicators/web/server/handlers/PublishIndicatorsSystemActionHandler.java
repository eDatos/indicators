package es.gobcan.istac.indicators.web.server.handlers;

import es.gobcan.istac.indicators.core.dto.PublishIndicatorsSystemResultDto;
import es.gobcan.istac.indicators.core.mapper.Dto2DoMapper;
import es.gobcan.istac.indicators.core.service.NoticesRestInternalService;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.utils.DtoUtils;
import es.gobcan.istac.indicators.web.shared.PublishIndicatorsSystemAction;
import es.gobcan.istac.indicators.web.shared.PublishIndicatorsSystemResult;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemDtoWeb;

@Component
public class PublishIndicatorsSystemActionHandler extends SecurityActionHandler<PublishIndicatorsSystemAction, PublishIndicatorsSystemResult> {

    @Autowired
    private IndicatorsServiceFacade    indicatorsServiceFacade;

    @Autowired
    private NoticesRestInternalService noticesRestInternalService;

    @Autowired
    private Dto2DoMapper               dto2DoMapper;

    public PublishIndicatorsSystemActionHandler() {
        super(PublishIndicatorsSystemAction.class);
    }

    @Override
    public PublishIndicatorsSystemResult executeSecurityAction(PublishIndicatorsSystemAction action) throws ActionException {

        ServiceContext serviceContext = ServiceContextHolder.getCurrentServiceContext();

        IndicatorsSystemDtoWeb indicatorsSystemDtoWeb = action.getIndicatorsSystemToPublish();
        PublishIndicatorsSystemResultDto result;
        try {
            result = indicatorsServiceFacade.publishIndicatorsSystem(ServiceContextHolder.getCurrentServiceContext(), indicatorsSystemDtoWeb.getUuid());
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }

        MetamacWebException resultException = null;
        if (result.getNotificationFailedReason() != null) {
            resultException = WebExceptionUtils.createMetamacWebException(result.getNotificationFailedReason());
            try {
                noticesRestInternalService.createIndicatorsSystemStreamMessageErrorBackgroundNotification(dto2DoMapper.indicatorsSystemDtoToDo(serviceContext, indicatorsSystemDtoWeb));
            } catch (MetamacException mapperException) {
                throw WebExceptionUtils.createMetamacWebException(mapperException);
            }
        }

        return new PublishIndicatorsSystemResult(DtoUtils.updateIndicatorsSystemDtoWeb(indicatorsSystemDtoWeb, result.getIndicatorsSystem()), resultException);
    }

    @Override
    public void undo(PublishIndicatorsSystemAction action, PublishIndicatorsSystemResult result, ExecutionContext context) throws ActionException {

    }

}
