package es.gobcan.istac.indicators.web.diffusion.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;

/**
 * FreeMarker view implementation to expose additional helpers
 */
public class FreeMarkerHelperView extends FreeMarkerView {

    private static final String                   HTTP  = "http:";
    private static final String                   HTTPS = "https:";

    private static IndicatorsConfigurationService configurationService;

    @Override
    protected void doRender(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String indicatorsExternalWebUrlBase = getIndicatorsExternalWebUrlBaseWithoutProtocol();
        model.put("serverURL", indicatorsExternalWebUrlBase);
        super.doRender(model, request, response);
    }

    private String getIndicatorsExternalWebUrlBaseWithoutProtocol() throws MetamacException {
        String indicatorsExternalWebUrlBase = getConfigurationService().retrieveIndicatorsExternalWebUrlBase();
        return removeUrlProtocol(indicatorsExternalWebUrlBase);
    }

    private String removeUrlProtocol(String url) {
        if (StringUtils.startsWith(url, HTTP)) {
            return StringUtils.removeStart(HTTP, url);
        } else if (StringUtils.startsWith(url, HTTPS)) {
            return StringUtils.removeStart(HTTPS, url);
        } else {
            return url;
        }
    }

    private static IndicatorsConfigurationService getConfigurationService() {
        if (configurationService == null) {
            configurationService = (IndicatorsConfigurationService) ApplicationContextProvider.getApplicationContext().getBean("configurationService");
        }
        return configurationService;
    }

}
