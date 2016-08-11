package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.ArchiveIndicatorAction;
import es.gobcan.istac.indicators.web.shared.ArchiveIndicatorResult;

@Component
public class ArchiveIndicatorActionHandler extends SecurityActionHandler<ArchiveIndicatorAction, ArchiveIndicatorResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public ArchiveIndicatorActionHandler() {
        super(ArchiveIndicatorAction.class);
    }

    @Override
    public ArchiveIndicatorResult executeSecurityAction(ArchiveIndicatorAction action) throws ActionException {
        try {
            IndicatorDto indicatorDto = indicatorsServiceFacade.archiveIndicator(ServiceContextHolder.getCurrentServiceContext(), action.getUuid());
            return new ArchiveIndicatorResult(indicatorDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(ArchiveIndicatorAction action, ArchiveIndicatorResult result, ExecutionContext context) throws ActionException {

    }

}
