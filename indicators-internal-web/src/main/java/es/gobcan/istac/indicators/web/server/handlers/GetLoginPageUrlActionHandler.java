package es.gobcan.istac.indicators.web.server.handlers;

import org.jasig.cas.client.util.CommonUtils;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.constants.IndicatorsConfigurationConstants;
import es.gobcan.istac.indicators.web.shared.GetLoginPageUrlAction;
import es.gobcan.istac.indicators.web.shared.GetLoginPageUrlResult;

@Component
public class GetLoginPageUrlActionHandler extends AbstractActionHandler<GetLoginPageUrlAction, GetLoginPageUrlResult> {

    @Autowired
    private ConfigurationService configurationService       = null;

    public GetLoginPageUrlActionHandler() {
        super(GetLoginPageUrlAction.class);
    }

    @Override
    public GetLoginPageUrlResult execute(GetLoginPageUrlAction action, ExecutionContext context) throws ActionException {
        String casServiceLoginUrl = configurationService.getConfig().getString(IndicatorsConfigurationConstants.SECURITY_CAS_SERVICE_LOGIN_URL);
        String serviceParameterName = "service";
        String serviceUrl = action.getServiceUrl();
        boolean renew = false;
        boolean gateway = false;

        String url = CommonUtils.constructRedirectUrl(casServiceLoginUrl, serviceParameterName, serviceUrl, renew, gateway);
        return new GetLoginPageUrlResult(url);
    }

    @Override
    public void undo(GetLoginPageUrlAction action, GetLoginPageUrlResult result, ExecutionContext context) throws ActionException {

    }

}
