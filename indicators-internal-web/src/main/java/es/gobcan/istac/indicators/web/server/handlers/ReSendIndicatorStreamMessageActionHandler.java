package es.gobcan.istac.indicators.web.server.handlers;

import com.gwtplatform.dispatch.shared.ActionException;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.mapper.Dto2DoMapper;
import es.gobcan.istac.indicators.core.service.NoticesRestInternalService;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.core.serviceimpl.result.SendStreamMessageResult;
import es.gobcan.istac.indicators.web.shared.ReSendIndicatorStreamMessageAction;
import es.gobcan.istac.indicators.web.shared.ReSendIndicatorStreamMessageResult;
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
public class ReSendIndicatorStreamMessageActionHandler extends SecurityActionHandler<ReSendIndicatorStreamMessageAction, ReSendIndicatorStreamMessageResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    @Autowired
    private NoticesRestInternalService noticesRestInternalService;

    @Autowired
    private Dto2DoMapper dto2DoMapper;

    public ReSendIndicatorStreamMessageActionHandler() {
        super(ReSendIndicatorStreamMessageAction.class);
    }

    @Override
    public ReSendIndicatorStreamMessageResult executeSecurityAction(ReSendIndicatorStreamMessageAction action) throws ActionException {

        ServiceContext serviceContext = ServiceContextHolder.getCurrentServiceContext();

        SendStreamMessageResult result;
        IndicatorDto indicatorDto;
        MetamacWebException indicatorStreamMessageException = null;
        try {
            result = indicatorsServiceFacade.resendIndicator(serviceContext, action.getIndicatorCode());
            indicatorDto = indicatorsServiceFacade.retrieveIndicatorByCode(serviceContext, action.getIndicatorCode(), null);
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
            indicatorStreamMessageException = WebExceptionUtils.createMetamacWebException(e);
            try {
                noticesRestInternalService.createIndicatorStreamMessageErrorBackgroundNotification(dto2DoMapper.indicatorDtoToDo(serviceContext, indicatorDto));
            } catch (MetamacException mapperException) {
                throw WebExceptionUtils.createMetamacWebException(e);
            }
        }

        return new ReSendIndicatorStreamMessageResult(indicatorDto, indicatorStreamMessageException);
    }
}