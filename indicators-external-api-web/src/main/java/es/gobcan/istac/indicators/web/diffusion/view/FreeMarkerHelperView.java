package es.gobcan.istac.indicators.web.diffusion.view;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.core.util.FreeMarkerUtil;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.core.common.util.WebUtils;
import org.siemac.metamac.core.common.util.swagger.SwaggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

/**
 * FreeMarker view implementation to expose additional helpers
 */
public class FreeMarkerHelperView extends FreeMarkerView {

    private static IndicatorsConfigurationService configurationService;

    protected Logger                              logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void doRender(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String indicatorsExternalApiUrlBase = getConfigurationService().retrieveIndicatorsExternalApiUrlBase();
        model.put("indicatorsExternalApiUrlBase", WebUtils.normalizeUrl(indicatorsExternalApiUrlBase));
        model.put("indicatorsExternalApiUrlBaseSwagger", SwaggerUtils.normalizeUrlForSwagger(indicatorsExternalApiUrlBase));
        model.put("organisation", getConfigurationService().retrieveOrganisation());
        model.put("faviconUrl", WebUtils.getFavicon());
        model.put("internationalizationUrlParamId", getConfigurationService().retrieveInternationalizationCookieId());
        String currentLocale = getCurrentLocale(request);
        model.put("locale", currentLocale);
        fillOptionalApiStyleHeaderUrl(model, getLocaleQueryParam(model, currentLocale));
        fillOptionalApiStyleFooterUrl(model, getLocaleQueryParam(model, currentLocale));

        super.doRender(model, request, response);
    }

    private String getCurrentLocale(HttpServletRequest request) {
        return RequestContextUtils.getLocaleResolver(request).resolveLocale(request).getLanguage();
    }

    private void fillOptionalApiStyleFooterUrl(Map<String, Object> model, String urlQueryParams) throws UnsupportedEncodingException, IOException {
        try {
            model.put("apiStyleFooter", FreeMarkerUtil.importHTMLFromUrl(getConfigurationService().retrieveApiStyleFooterUrl() + "?" + urlQueryParams));
        } catch (MetamacException e) {
            if (logger.isDebugEnabled()) {
                logger.debug(e.getHumanReadableMessage());
            }
        }
    }

    private void fillOptionalApiStyleHeaderUrl(Map<String, Object> model, String urlQueryParams) throws IOException {
        try {
            model.put("apiStyleHeader", FreeMarkerUtil.importHTMLFromUrl(getConfigurationService().retrieveApiStyleHeaderUrl() + "?" + urlQueryParams));
        } catch (MetamacException e) {
            if (logger.isDebugEnabled()) {
                logger.debug(e.getHumanReadableMessage());
            }
        }
    }

    private String getLocaleQueryParam(Map<String, Object> model, String locale) {
        String internationalizationUrlParamId = (String) model.getOrDefault("internationalizationUrlParamId", null);
        String result = String.format("appId=%s", "indicators-external");
        if (StringUtils.isNotBlank(internationalizationUrlParamId)) {
           result = result.concat(String.format("&%s=%s", internationalizationUrlParamId, locale));
        }
        return result;
    }

    private static IndicatorsConfigurationService getConfigurationService() {
        if (configurationService == null) {
            configurationService = (IndicatorsConfigurationService) ApplicationContextProvider.getApplicationContext().getBean("configurationService");
        }
        return configurationService;
    }

}
