package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.web.shared.GetUserGuideUrlAction;
import es.gobcan.istac.indicators.web.shared.GetUserGuideUrlResult;

@Component
public class GetUserGuideUrlActionHandler extends SecurityActionHandler<GetUserGuideUrlAction, GetUserGuideUrlResult> {

    @Autowired
    private IndicatorsConfigurationService configurationService = null;

    public GetUserGuideUrlActionHandler() {
        super(GetUserGuideUrlAction.class);
    }

    @Override
    public GetUserGuideUrlResult executeSecurityAction(GetUserGuideUrlAction action) throws ActionException {
        try {
            String userGuideFileName = configurationService.retrieveIndicatorsUserGuideFilename();
            return new GetUserGuideUrlResult(userGuideFileName);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
