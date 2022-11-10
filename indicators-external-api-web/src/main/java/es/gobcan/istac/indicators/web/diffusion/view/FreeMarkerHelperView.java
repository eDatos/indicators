package es.gobcan.istac.indicators.web.diffusion.view;

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

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.core.util.FreeMarkerUtil;

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

        try {
            model.put("internationalizationUrlParamId", getConfigurationService().retrieveInternationalizationCookieId());
        } catch (MetamacException ignore) {
            logger.info("The optional property 'internationalizationUrlParamId' could not be initialized.");
        }

        String locale = getCurrentLocale(request);

        fillOptionalApiStyleHeaderUrl(model, locale);
        fillOptionalApiStyleFooterUrl(model, locale);
        fillOptionalApiStyleCssUrl(model);

        super.doRender(model, request, response);
    }

    private String getCurrentLocale(HttpServletRequest request) {
        return RequestContextUtils.getLocaleResolver(request).resolveLocale(request).getLanguage();
    }

    private void fillOptionalApiStyleCssUrl(Map<String, Object> model) {
        try {
            model.put("apiStyleCssUrl", getConfigurationService().retrieveApiStyleCssUrl());
        } catch (MetamacException e) {
            if (logger.isDebugEnabled()) {
                logger.debug(e.getHumanReadableMessage());
            }
        }
    }

    private void fillOptionalApiStyleFooterUrl(Map<String, Object> model, String locale) throws UnsupportedEncodingException, IOException {
        try {
            String urlQueryParams = getLocaleQueryParam(model, locale);
            model.put("apiStyleFooter", FreeMarkerUtil.importHTMLFromUrl(getConfigurationService().retrieveApiStyleFooterUrl() + "?" + urlQueryParams));
        } catch (MetamacException e) {
            if (logger.isDebugEnabled()) {
                logger.debug(e.getHumanReadableMessage());
            }
        }
    }

    private void fillOptionalApiStyleHeaderUrl(Map<String, Object> model, String locale) throws UnsupportedEncodingException, IOException {
        try {
            String urlQueryParams = getLocaleQueryParam(model, locale);
            model.put("apiStyleHeader", FreeMarkerUtil.importHTMLFromUrl(getConfigurationService().retrieveApiStyleHeaderUrl() + "?" + urlQueryParams));
        } catch (MetamacException e) {
            if (logger.isDebugEnabled()) {
                logger.debug(e.getHumanReadableMessage());
            }
        }
    }

    private String getLocaleQueryParam(Map<String, Object> model, String locale) {
        String internationalizationUrlParamId = (String) model.getOrDefault("internationalizationUrlParamId", null);
        return StringUtils.isNotBlank(internationalizationUrlParamId) ? String.format("%s=%s", internationalizationUrlParamId, locale) : "";
    }

    private static IndicatorsConfigurationService getConfigurationService() {
        if (configurationService == null) {
            configurationService = (IndicatorsConfigurationService) ApplicationContextProvider.getApplicationContext().getBean("configurationService");
        }
        return configurationService;
    }

}
