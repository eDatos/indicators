package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.EnableNotifyPopulationErrorsAction;
import es.gobcan.istac.indicators.web.shared.EnableNotifyPopulationErrorsResult;

@Component
public class EnableNotifyPopulationErrorsActionHandler extends SecurityActionHandler<EnableNotifyPopulationErrorsAction, EnableNotifyPopulationErrorsResult> {

    private final Logger            LOG = LoggerFactory.getLogger(EnableNotifyPopulationErrorsActionHandler.class);

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public EnableNotifyPopulationErrorsActionHandler() {
        super(EnableNotifyPopulationErrorsAction.class);
    }

    @Override
    public EnableNotifyPopulationErrorsResult executeSecurityAction(EnableNotifyPopulationErrorsAction action) throws ActionException {
        try {
            for (String uuid : action.getUuids()) {
                indicatorsServiceFacade.enableNotifyPopulationErrors(ServiceContextHolder.getCurrentServiceContext(), uuid);
            }
            return new EnableNotifyPopulationErrorsResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(EnableNotifyPopulationErrorsAction action, EnableNotifyPopulationErrorsResult result, ExecutionContext context) throws ActionException {

    }

}
