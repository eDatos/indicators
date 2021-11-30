package es.gobcan.istac.indicators.web.server;

import com.gwtplatform.dispatch.shared.ActionException;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.mapper.Dto2DoMapper;
import es.gobcan.istac.indicators.core.service.NoticesRestInternalService;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.core.serviceimpl.result.SendStreamMessageResult;
import es.gobcan.istac.indicators.web.server.utils.DtoUtils;
import es.gobcan.istac.indicators.web.shared.*;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemDtoWeb;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReSendIndicatorsSystemStreamMessageActionHandler extends SecurityActionHandler<ReSendIndicatorsSystemStreamMessageAction, ReSendIndicatorsSystemStreamMessageResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    @Autowired
    private NoticesRestInternalService noticesRestInternalService;

    @Autowired
    private Dto2DoMapper dto2DoMapper;

    public ReSendIndicatorsSystemStreamMessageActionHandler() {
        super(ReSendIndicatorsSystemStreamMessageAction.class);
    }

    @Override
    public ReSendIndicatorsSystemStreamMessageResult executeSecurityAction(ReSendIndicatorsSystemStreamMessageAction action) throws ActionException {

        ServiceContext serviceContext = ServiceContextHolder.getCurrentServiceContext();

        SendStreamMessageResult result;
        IndicatorsSystemDto indicatorsSystemDto;
        MetamacWebException indicatorsSystemStreamMessageException = null;
        try {
            result = indicatorsServiceFacade.resendIndicatorsSystem(serviceContext, action.getIndicatorsSystemToResend().getCode());
            indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(serviceContext, action.getIndicatorsSystemToResend().getCode(), null);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }

        if (!result.isOk()) {
            MetamacException e = result.getMainException();
            List<MetamacExceptionItem> list = new ArrayList<>();
            for (MetamacException exception : result.getSecondaryExceptions()) {
                list.addAll(exception.getExceptionItems());
            }
            e.getExceptionItems().addAll(list);
            indicatorsSystemStreamMessageException = WebExceptionUtils.createMetamacWebException(e);
            try {
                noticesRestInternalService.createIndicatorsSystemStreamMessageErrorBackgroundNotification(dto2DoMapper.indicatorsSystemDtoToDo(serviceContext, indicatorsSystemDto));
            } catch (MetamacException mapperException) {
                throw WebExceptionUtils.createMetamacWebException(e);
            }
        }

        IndicatorsSystemDtoWeb indicatorsSystemDtoWeb = action.getIndicatorsSystemToResend();
        return new ReSendIndicatorsSystemStreamMessageResult(DtoUtils.updateIndicatorsSystemDtoWeb(indicatorsSystemDtoWeb, indicatorsSystemDto), indicatorsSystemStreamMessageException);
    }
}