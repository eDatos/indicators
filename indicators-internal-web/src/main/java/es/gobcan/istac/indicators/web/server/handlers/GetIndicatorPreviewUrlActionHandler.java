package es.gobcan.istac.indicators.web.server.handlers;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.constants.IndicatorsConfigurationConstants;
import es.gobcan.istac.indicators.web.server.utils.JaxiConstants;
import es.gobcan.istac.indicators.web.shared.GetIndicatorPreviewUrlAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorPreviewUrlResult;

@Component
public class GetIndicatorPreviewUrlActionHandler extends SecurityActionHandler<GetIndicatorPreviewUrlAction, GetIndicatorPreviewUrlResult> {

    @Autowired
    private ConfigurationService configurationService;

    public GetIndicatorPreviewUrlActionHandler() {
        super(GetIndicatorPreviewUrlAction.class);
    }

    @Override
    public GetIndicatorPreviewUrlResult executeSecurityAction(GetIndicatorPreviewUrlAction action) throws ActionException {

        String indicatorUrl = configurationService.getProperty(IndicatorsConfigurationConstants.JAXI_URL_INDICATOR);
        // Add the indicator code in the URL
        indicatorUrl = StringUtils.replace(indicatorUrl, JaxiConstants.INDICATOR_CODE_PARAM, action.getIndicatorCode());

        return new GetIndicatorPreviewUrlResult(indicatorUrl);
    }

}
