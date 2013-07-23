package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.constants.IndicatorsConfigurationConstants;
import es.gobcan.istac.indicators.web.shared.GetUserGuideUrlAction;
import es.gobcan.istac.indicators.web.shared.GetUserGuideUrlResult;

@Component
public class GetUserGuideUrlActionHandler extends SecurityActionHandler<GetUserGuideUrlAction, GetUserGuideUrlResult> {

    @Autowired
    private ConfigurationService configurationService = null;

    public GetUserGuideUrlActionHandler() {
        super(GetUserGuideUrlAction.class);
    }

    @Override
    public GetUserGuideUrlResult executeSecurityAction(GetUserGuideUrlAction action) throws ActionException {
        String dataUrl = configurationService.getConfig().getString(IndicatorsConfigurationConstants.DATA_URL);
        String userGuideFileName = configurationService.getConfig().getString(IndicatorsConfigurationConstants.USER_GUIDE_FILENAME);
        return new GetUserGuideUrlResult(dataUrl + "/docs/" + userGuideFileName);
    }
}
