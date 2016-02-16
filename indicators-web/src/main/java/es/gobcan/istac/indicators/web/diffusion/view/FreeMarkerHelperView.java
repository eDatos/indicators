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

        String indicatorsExternalApiUrlBase = getIndicatorsExternalApiUrlBaseWithoutProtocol();
        model.put("indicatorsExternalApiUrlBase", indicatorsExternalApiUrlBase);

        String idxManagerSearchFormUrl = getIdxManagerSearchFormUrl();
        model.put("idxManagerSearchFormUrl", idxManagerSearchFormUrl);

        String jaxiUrlBase = removeLastSlashInUrl(configurationService.retrieveJaxiRemoteUrl());
        model.put("jaxiUrlBase", jaxiUrlBase);

        String metamacPortalUrlBase = getMetamacPortalExternalUrlBase();
        model.put("metamacPortalUrlBase", metamacPortalUrlBase);
        
        String metamacPortalPermalinksEndpoint = getMetamacPortalPermalinksEndpoint();
        model.put("metamacPortalPermalinksEndpoint", metamacPortalPermalinksEndpoint);

        super.doRender(model, request, response);
    }

    private String getIndicatorsExternalWebUrlBaseWithoutProtocol() throws MetamacException {
        String indicatorsExternalWebUrlBase = getConfigurationService().retrieveIndicatorsExternalWebApplicationUrlBase();
        return removeUrlProtocol(indicatorsExternalWebUrlBase);
    }

    private String getIndicatorsExternalApiUrlBaseWithoutProtocol() throws MetamacException {
        String indicatorsExternalApiUrlBase = removeLastSlashInUrl(getConfigurationService().retrieveIndicatorsExternalApiUrlBase());
        return removeUrlProtocol(indicatorsExternalApiUrlBase);
    }

    private String getIdxManagerSearchFormUrl() throws MetamacException {
        return getConfigurationService().retrieveIdxManagerSearchFormUrl();
    }
    
    private String getMetamacPortalExternalUrlBase() throws MetamacException {
        return removeUrlProtocol(removeLastSlashInUrl(configurationService.retrievePortalExternalUrlBase()));
    }
    
    private String getMetamacPortalPermalinksEndpoint() throws MetamacException {
        return removeUrlProtocol(removeLastSlashInUrl(configurationService.retrievePortalExternalApisPermalinksUrlBase()));
    }

    private String removeUrlProtocol(String url) {
        if (StringUtils.startsWith(url, HTTP)) {
            return StringUtils.removeStart(url, HTTP);
        } else if (StringUtils.startsWith(url, HTTPS)) {
            return StringUtils.removeStart(url, HTTPS);
        } else {
            return url;
        }
    }

    private String removeLastSlashInUrl(String url) {
        if (url.endsWith("/")) {
            return StringUtils.removeEnd(url, "/");
        }
        return url;
    }

    private static IndicatorsConfigurationService getConfigurationService() {
        if (configurationService == null) {
            configurationService = (IndicatorsConfigurationService) ApplicationContextProvider.getApplicationContext().getBean("configurationService");
        }
        return configurationService;
    }

}
