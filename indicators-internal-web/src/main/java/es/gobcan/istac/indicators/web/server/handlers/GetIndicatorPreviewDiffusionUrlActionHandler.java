package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.web.server.utils.StatisticalVisualizerUtils;
import es.gobcan.istac.indicators.web.shared.GetIndicatorPreviewDiffusionUrlAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorPreviewDiffusionUrlResult;

@Component
public class GetIndicatorPreviewDiffusionUrlActionHandler extends SecurityActionHandler<GetIndicatorPreviewDiffusionUrlAction, GetIndicatorPreviewDiffusionUrlResult> {

    @Autowired
    private IndicatorsConfigurationService configurationService;

    public GetIndicatorPreviewDiffusionUrlActionHandler() {
        super(GetIndicatorPreviewDiffusionUrlAction.class);
    }

    @Override
    public GetIndicatorPreviewDiffusionUrlResult executeSecurityAction(GetIndicatorPreviewDiffusionUrlAction action) throws ActionException {
        try {
            String visualizerEndpoint = configurationService.retrievePortalExternalWebApplicationUrlBase();
            return new GetIndicatorPreviewDiffusionUrlResult(StatisticalVisualizerUtils.buildIndicatorUrl(visualizerEndpoint, action.getIndicatorCode()));
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
