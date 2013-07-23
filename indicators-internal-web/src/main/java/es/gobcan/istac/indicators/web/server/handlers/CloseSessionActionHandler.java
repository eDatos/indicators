package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.constants.IndicatorsConfigurationConstants;
import es.gobcan.istac.indicators.web.shared.CloseSessionAction;
import es.gobcan.istac.indicators.web.shared.CloseSessionResult;

@Component
public class CloseSessionActionHandler extends AbstractActionHandler<CloseSessionAction, CloseSessionResult> {

    @Autowired
    private ConfigurationService configurationService        = null;

    public CloseSessionActionHandler() {
        super(CloseSessionAction.class);
    }

    @Override
    public CloseSessionResult execute(CloseSessionAction action, ExecutionContext context) throws ActionException {
        String casServiceLogoutUrl = configurationService.getConfig().getString(IndicatorsConfigurationConstants.SECURITY_CAS_SERVICE_LOGOUT_URL);
        ServiceContextHolder.getCurrentRequest().getSession(false).invalidate();
        return new CloseSessionResult(casServiceLogoutUrl);
    }

    @Override
    public void undo(CloseSessionAction action, CloseSessionResult result, ExecutionContext context) throws ActionException {

    }

}
