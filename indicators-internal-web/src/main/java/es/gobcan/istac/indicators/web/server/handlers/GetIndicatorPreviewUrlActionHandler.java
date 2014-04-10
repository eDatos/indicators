package es.gobcan.istac.indicators.web.server.handlers;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.web.server.utils.JaxiConstants;
import es.gobcan.istac.indicators.web.shared.GetIndicatorPreviewUrlAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorPreviewUrlResult;

@Component
public class GetIndicatorPreviewUrlActionHandler extends SecurityActionHandler<GetIndicatorPreviewUrlAction, GetIndicatorPreviewUrlResult> {

    @Autowired
    private IndicatorsConfigurationService configurationService;

    public GetIndicatorPreviewUrlActionHandler() {
        super(GetIndicatorPreviewUrlAction.class);
    }

    @Override
    public GetIndicatorPreviewUrlResult executeSecurityAction(GetIndicatorPreviewUrlAction action) throws ActionException {
        try {
            String indicatorUrl = configurationService.retrieveJaxiLocalUrlIndicator();
            // Add the indicator code in the URL
            indicatorUrl = StringUtils.replace(indicatorUrl, JaxiConstants.INDICATOR_CODE_PARAM, action.getIndicatorCode());
            return new GetIndicatorPreviewUrlResult(indicatorUrl);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
