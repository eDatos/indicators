package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.web.server.utils.StatisticalVisualizerUtils;
import es.gobcan.istac.indicators.web.shared.GetIndicatorInstancePreviewUrlAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorInstancePreviewUrlResult;

@Component
public class GetIndicatorInstancePreviewUrlActionHandler extends SecurityActionHandler<GetIndicatorInstancePreviewUrlAction, GetIndicatorInstancePreviewUrlResult> {

    @Autowired
    private IndicatorsConfigurationService configurationService;

    public GetIndicatorInstancePreviewUrlActionHandler() {
        super(GetIndicatorInstancePreviewUrlAction.class);
    }

    @Override
    public GetIndicatorInstancePreviewUrlResult executeSecurityAction(GetIndicatorInstancePreviewUrlAction action) throws ActionException {
        try {
            String visualizerEndpoint = configurationService.retrievePortalInternalWebApplicationUrlBase();

            return new GetIndicatorInstancePreviewUrlResult(
                    StatisticalVisualizerUtils.buildIndicatorInstanceUrl(visualizerEndpoint, action.getIndicatorInstanceCode(), action.getIndicatorsSystemCode()));
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
