package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.web.shared.GetUserGuideUrlAction;
import es.gobcan.istac.indicators.web.shared.GetUserGuideUrlResult;

@Component
public class GetUserGuideUrlActionHandler extends SecurityActionHandler<GetUserGuideUrlAction, GetUserGuideUrlResult> {

    private static String        PROP_DATA_URL             = "environment.indicators.data";
    private static String        PROP_USER_GUIDE_FILE_NAME = "indicators.user.guide.file.name";

    @Autowired
    private ConfigurationService configurationService      = null;

    public GetUserGuideUrlActionHandler() {
        super(GetUserGuideUrlAction.class);
    }

    @Override
    public GetUserGuideUrlResult executeSecurityAction(GetUserGuideUrlAction action) throws ActionException {
        String dataUrl = configurationService.getConfig().getString(PROP_DATA_URL);
        String userGuideFileName = configurationService.getConfig().getString(PROP_USER_GUIDE_FILE_NAME);
        return new GetUserGuideUrlResult(dataUrl + "/docs/" + userGuideFileName);
    }
}
