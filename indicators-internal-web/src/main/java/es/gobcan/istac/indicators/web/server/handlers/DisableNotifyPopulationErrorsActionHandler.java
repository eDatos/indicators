package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.DisableNotifyPopulationErrorsAction;
import es.gobcan.istac.indicators.web.shared.DisableNotifyPopulationErrorsResult;

@Component
public class DisableNotifyPopulationErrorsActionHandler extends SecurityActionHandler<DisableNotifyPopulationErrorsAction, DisableNotifyPopulationErrorsResult> {

    private final Logger            LOG = LoggerFactory.getLogger(DisableNotifyPopulationErrorsActionHandler.class);

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public DisableNotifyPopulationErrorsActionHandler() {
        super(DisableNotifyPopulationErrorsAction.class);
    }

    @Override
    public DisableNotifyPopulationErrorsResult executeSecurityAction(DisableNotifyPopulationErrorsAction action) throws ActionException {
        try {
            for(String uuid : action.getUuids()) {
                indicatorsServiceFacade.disableNotifyPopulationErrors(ServiceContextHolder.getCurrentServiceContext(), uuid);
            }
            return new DisableNotifyPopulationErrorsResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(DisableNotifyPopulationErrorsAction action, DisableNotifyPopulationErrorsResult result, ExecutionContext context) throws ActionException {

    }

}
