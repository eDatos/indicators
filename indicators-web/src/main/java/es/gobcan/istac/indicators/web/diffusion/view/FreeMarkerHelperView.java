package es.gobcan.istac.indicators.web.diffusion.view;

import static es.gobcan.istac.indicators.web.diffusion.view.WebUtils.PARAM_APP_NAME;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.core.conf.MetadataProperties;
import es.gobcan.istac.indicators.core.util.FreeMarkerUtil;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;

/**
 * FreeMarker view implementation to expose additional helpers
 */
public class FreeMarkerHelperView extends FreeMarkerView {

    private static final String                   HTTP  = "http:";
    private static final String                   HTTPS = "https:";

    private static IndicatorsConfigurationService configurationService;
    private static MetadataProperties             metadataProperties;

    @Override
    protected void doRender(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        model.put("serverURL", getIndicatorsExternalWebUrlBaseWithoutProtocol());
        model.put("indicatorsExternalApiUrlBase", getIndicatorsExternalApiUrlBaseWithoutProtocol());
        model.put("srmExternalApiUrlBase", getSrmExternalApiUrlBaseWithoutProtocol());
        model.put("defaultCategorySchemeUrn", getDefaultCategorySchemeUrn());
        model.put("visualizerExternalUrlBase", getVisualizerExternalUrlBase());
        model.put("visualizerApplicationExternalUrlBase", getVisualizerApplicationExternalUrlBase());
        model.put("analyticsGoogleTrackingId", getConfigurationService().retrieveAnalyticsGoogleTrackingId());
        model.put("permalinksUrlBase", getPermalinksUrlBase());
        model.put("permalinksUrlBaseWithProtocol", getPermalinksUrlBaseWithProtocol());
        model.put("internationalizationLanguages", getinternationalizationLanguages());
        model.put("apiKey", getConfigurationService().retrieveIndicatorsWebRestApiKey());
        try {
            model.put("internationalizationUrlParamId", getConfigurationService().retrieveInternationalizationCookieId());
        } catch (MetamacException metamacException) {
            logger.debug("The optional property 'internationalizationUrlParamId' could not be initialized.");
        }

        try {
            model.put("firstTerritory", getConfigurationService().retrieveFirstTerritory());
        } catch (MetamacException metamacException) {
            model.put("firstTerritory", "");
            logger.debug("The optional property 'firstTerritory' could not be initialized.");
        }

        try {
            model.put("captchaExternalApiUrlBase", getCaptchaExternalApiUrlBase());
        } catch (MetamacException metamacException) {
            // Ignore property if it doesn't exist
            logger.debug("The captcha will not be operational because the property 'captchaExternalApiUrlBase' could not be initialized.");
        }
        model.put("organisation", getConfigurationService().retrieveOrganisation());
        model.put("faviconUrl", getConfigurationService().retrieveAppStyleFaviconUrl());
        model.put("defaultLocale", getMetadataProperties().getDefaultInternationalizationLanguage());

        addStatisticalVisualizerUtils(model);

        Locale locale = getCurrentLocale(request);
        fillOptionalPortalDefaultStyleCssUrl(model);
        fillOptionalPortalDefaultStyleHeaderUrl(model, locale);
        fillOptionalPortalDefaultStyleFooterUrl(model, locale);

        super.doRender(model, request, response);
    }

    private List<String> getinternationalizationLanguages() {
        try {
            return getConfigurationService().retrieveInternationalizationLanguages();
        } catch (MetamacException e) {
            logger.info("The optional property 'internationalizationLanguages' could not be initialized.");
            return new ArrayList<>();
        }
    }

    private Locale getCurrentLocale(HttpServletRequest request) {
        return RequestContextUtils.getLocaleResolver(request).resolveLocale(request);
    }

    private void fillOptionalPortalDefaultStyleCssUrl(Map<String, Object> model) {
        try {
            model.put("portalDefaultStyleCssUrl", getConfigurationService().retrievePortalDefaultStyleCssUrl());
        } catch (MetamacException e) {
            if (logger.isDebugEnabled()) {
                logger.debug(e.getHumanReadableMessage());
            }
        }
    }

    private void fillOptionalPortalDefaultStyleFooterUrl(Map<String, Object> model, Locale locale) throws UnsupportedEncodingException, IOException {
        try {
            String urlQueryParams = buildHeaderQuery(model, locale);
            model.put("portalDefaultStyleFooter", FreeMarkerUtil.importHTMLFromUrl(getConfigurationService().retrievePortalDefaultStyleFooterUrl() + "?" + urlQueryParams));
        } catch (MetamacException e) {
            if (logger.isDebugEnabled()) {
                logger.debug(e.getHumanReadableMessage());
            }
        }
    }

    private void fillOptionalPortalDefaultStyleHeaderUrl(Map<String, Object> model, Locale locale) throws UnsupportedEncodingException, IOException {
        try {

            final String urlQueryParams = buildHeaderQuery(model, locale);
            model.put("portalDefaultStyleHeader", FreeMarkerUtil.importHTMLFromUrl(getConfigurationService().retrievePortalDefaultStyleHeaderUrl() + urlQueryParams));
        } catch (MetamacException e) {
            if (logger.isDebugEnabled()) {
                logger.debug(e.getHumanReadableMessage());
            }
        }
    }

    private void addStatisticalVisualizerUtils(Map<String, Object> model) throws TemplateModelException {
        BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
        TemplateHashModel staticModels = wrapper.getStaticModels();
        TemplateHashModel statisticalVisualizerUtil = (TemplateHashModel) staticModels.get("es.gobcan.istac.indicators.core.util.StatisticalVisualizerUtils");
        model.put("statisticalVisualizerUtil", statisticalVisualizerUtil);
    }

    private String getIndicatorsExternalWebUrlBaseWithoutProtocol() throws MetamacException {
        String indicatorsExternalWebUrlBase = getConfigurationService().retrieveIndicatorsExternalWebApplicationUrlBase();
        return removeUrlProtocol(indicatorsExternalWebUrlBase);
    }

    private String getIndicatorsExternalApiUrlBaseWithoutProtocol() throws MetamacException {
        String indicatorsExternalApiUrlBase = removeLastSlashInUrl(getConfigurationService().retrieveIndicatorsExternalApiUrlBase());
        return removeUrlProtocol(indicatorsExternalApiUrlBase);
    }

    private String getSrmExternalApiUrlBaseWithoutProtocol() throws MetamacException {
        String srmExternalApiUrlBase = removeLastSlashInUrl(getConfigurationService().retrieveSrmExternalApiUrlBase());
        return removeUrlProtocol(srmExternalApiUrlBase);
    }

    private String getDefaultCategorySchemeUrn() throws MetamacException {
        String defaultCategorySchemeUrn = removeLastSlashInUrl(getConfigurationService().retrieveDefaultCategoryScheme());
        return removeUrlProtocol(defaultCategorySchemeUrn);
    }

    private String getVisualizerExternalUrlBase() throws MetamacException {
        return removeUrlProtocol(removeLastSlashInUrl(getConfigurationService().retrievePortalExternalUrlBase()));
    }

    private String getVisualizerApplicationExternalUrlBase() throws MetamacException {
        return removeLastSlashInUrl(getConfigurationService().retrievePortalExternalWebApplicationUrlVisualizer());
    }

    private String getPermalinksUrlBase() throws MetamacException {
        return removeUrlProtocol(removeLastSlashInUrl(configurationService.retrievePortalExternalApisPermalinksUrlBase()));
    }

    private String getPermalinksUrlBaseWithProtocol() throws MetamacException {
        return removeLastSlashInUrl(getConfigurationService().retrievePortalExternalApisPermalinksUrlBase());
    }

    private String getCaptchaExternalApiUrlBase() throws MetamacException {
        return removeLastSlashInUrl(getConfigurationService().retrieveCaptchaExternalApiUrlBase());
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

    private static MetadataProperties getMetadataProperties() {
        if (metadataProperties == null) {
            metadataProperties = (MetadataProperties) ApplicationContextProvider.getApplicationContext().getBean("metadataProperties");
        }
        return metadataProperties;
    }

    public static String buildHeaderQuery(Map<String, Object> model, Locale locale) {
        String internationalizationUrlParamId = (String) model.getOrDefault("internationalizationUrlParamId", null);
        BreadcrumbList breadcrumbList = (BreadcrumbList) model.get("breadcrumbList");
        String appName = (String) model.get(PARAM_APP_NAME);
        return WebUtils.buildHeaderQuery(appName, breadcrumbList, internationalizationUrlParamId, locale);
    }

}
