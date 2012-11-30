package es.gobcan.istac.indicators.web.server.handlers;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.web.server.utils.JaxiConstants;
import es.gobcan.istac.indicators.web.shared.GetIndicatorInstancePreviewUrlAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorInstancePreviewUrlResult;

@Component
public class GetIndicatorInstancePreviewUrlActionHandler extends SecurityActionHandler<GetIndicatorInstancePreviewUrlAction, GetIndicatorInstancePreviewUrlResult> {

    @Autowired
    private ConfigurationService configurationService;

    public GetIndicatorInstancePreviewUrlActionHandler() {
        super(GetIndicatorInstancePreviewUrlAction.class);
    }

    @Override
    public GetIndicatorInstancePreviewUrlResult executeSecurityAction(GetIndicatorInstancePreviewUrlAction action) throws ActionException {
        String jaxiUrl = configurationService.getProperty(JaxiConstants.APPLICATION_URL);

        String indicatorInstanceUrl = configurationService.getProperty(JaxiConstants.INDICATOR_URL);
        // Add the instance code and the system code in the URL
        indicatorInstanceUrl = StringUtils.replace(indicatorInstanceUrl, JaxiConstants.INSTANCE_CODE_PARAM, action.getIndicatorInstanceCode());
        indicatorInstanceUrl = StringUtils.replace(indicatorInstanceUrl, JaxiConstants.SYSTEM_CODE_PARAM, action.getIndicatorsSystemCode());

        String url = jaxiUrl + indicatorInstanceUrl;

        return new GetIndicatorInstancePreviewUrlResult(url);
    }

}
