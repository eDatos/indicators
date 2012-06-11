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
import es.gobcan.istac.indicators.web.shared.VersioningIndicatorAction;
import es.gobcan.istac.indicators.web.shared.VersioningIndicatorResult;

@Component
public class VersioningIndicatorActionHandler extends SecurityActionHandler<VersioningIndicatorAction, VersioningIndicatorResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public VersioningIndicatorActionHandler() {
        super(VersioningIndicatorAction.class);
    }

    @Override
    public VersioningIndicatorResult executeSecurityAction(VersioningIndicatorAction action) throws ActionException {
        try {
            IndicatorDto indicatorDto = indicatorsServiceFacade.versioningIndicator(ServiceContextHolder.getCurrentServiceContext(), action.getUuid(), action.getVersionType());
            return new VersioningIndicatorResult(indicatorDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(VersioningIndicatorAction action, VersioningIndicatorResult result, ExecutionContext context) throws ActionException {

    }

}
