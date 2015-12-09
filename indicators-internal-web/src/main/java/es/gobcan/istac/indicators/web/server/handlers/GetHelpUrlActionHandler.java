package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.web.shared.GetHelpUrlAction;
import es.gobcan.istac.indicators.web.shared.GetHelpUrlResult;

@Component
public class GetHelpUrlActionHandler extends SecurityActionHandler<GetHelpUrlAction, GetHelpUrlResult> {

    @Autowired
    private IndicatorsConfigurationService configurationService = null;

    public GetHelpUrlActionHandler() {
        super(GetHelpUrlAction.class);
    }

    @Override
    public GetHelpUrlResult executeSecurityAction(GetHelpUrlAction action) throws ActionException {
        try {
            return new GetHelpUrlResult(configurationService.retrieveHelpUrl());
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
