package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.core.util.StatisticalVisualizerUtils;
import es.gobcan.istac.indicators.web.shared.GetIndicatorPreviewProductionUrlAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorPreviewProductionUrlResult;

@Component
public class GetIndicatorPreviewProductionUrlActionHandler extends SecurityActionHandler<GetIndicatorPreviewProductionUrlAction, GetIndicatorPreviewProductionUrlResult> {

    @Autowired
    private IndicatorsConfigurationService configurationService;

    public GetIndicatorPreviewProductionUrlActionHandler() {
        super(GetIndicatorPreviewProductionUrlAction.class);
    }

    @Override
    public GetIndicatorPreviewProductionUrlResult executeSecurityAction(GetIndicatorPreviewProductionUrlAction action) throws ActionException {
        try {
            String visualizerEndpoint = configurationService.retrievePortalInternalWebApplicationUrlBase();
            return new GetIndicatorPreviewProductionUrlResult(StatisticalVisualizerUtils.buildIndicatorUrl(visualizerEndpoint, action.getIndicatorCode()));
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
