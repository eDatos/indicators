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
import es.gobcan.istac.indicators.web.server.ServiceContextHolder;
import es.gobcan.istac.indicators.web.shared.ArchiveIndicatorAction;
import es.gobcan.istac.indicators.web.shared.ArchiveIndicatorResult;

@Component
public class ArchiveIndicatorActionHandler extends AbstractActionHandler<ArchiveIndicatorAction, ArchiveIndicatorResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public ArchiveIndicatorActionHandler() {
        super(ArchiveIndicatorAction.class);
    }

    @Override
    public ArchiveIndicatorResult execute(ArchiveIndicatorAction action, ExecutionContext context) throws ActionException {
        try {
            IndicatorDto indicatorDto = indicatorsServiceFacade.archiveIndicator(ServiceContextHolder.getCurrentServiceContext(), action.getUuid());
            return new ArchiveIndicatorResult(indicatorDto);
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(ArchiveIndicatorAction action, ArchiveIndicatorResult result, ExecutionContext context) throws ActionException {

    }

}
