package es.gobcan.istac.indicators.web.diffusion.view;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
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

    @Override
    protected void doRender(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        model.put("serverURL", getIndicatorsExternalWebUrlBaseWithoutProtocol());
        model.put("indicatorsExternalApiUrlBase", getIndicatorsExternalApiUrlBaseWithoutProtocol());
        model.put("visualizerExternalUrlBase", getVisualizerExternalUrlBase());
        model.put("visualizerApplicationExternalUrlBase", getVisualizerApplicationExternalUrlBase());
        model.put("analyticsGoogleTrackingId", getConfigurationService().retrieveAnalyticsGoogleTrackingId());
        model.put("permalinksUrlBase", getPermalinksUrlBase());
        model.put("permalinksUrlBaseWithProtocol", getPermalinksUrlBaseWithProtocol());
        model.put("captchaExternalApiUrlBase", getCaptchaExternalApiUrlBase());
        model.put("organisation", getConfigurationService().retrieveOrganisation());
        model.put("faviconUrl", getConfigurationService().retrieveAppStyleFaviconUrl());

        addStatisticalVisualizerUtils(model);

        fillOptionalPortalDefaultStyleCssUrl(model);
        fillOptionalPortalDefaultStyleHeaderUrl(model);
        fillOptionalPortalDefaultStyleFooterUrl(model);

        super.doRender(model, request, response);
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

    private void fillOptionalPortalDefaultStyleFooterUrl(Map<String, Object> model) throws UnsupportedEncodingException, IOException {
        try {
            model.put("portalDefaultStyleFooter", FreeMarkerUtil.importHTMLFromUrl(getConfigurationService().retrievePortalDefaultStyleFooterUrl()));
        } catch (MetamacException e) {
            if (logger.isDebugEnabled()) {
                logger.debug(e.getHumanReadableMessage());
            }
        }
    }

    private void fillOptionalPortalDefaultStyleHeaderUrl(Map<String, Object> model) throws UnsupportedEncodingException, IOException {
        try {
            // Model filled on the controller. For example es.gobcan.istac.indicators.web.widgets.WidgetsController
            BreadcrumbList breadcrumbList = (BreadcrumbList) model.get("breadcrumbList");
            String urlQueryParams = "";
            if (breadcrumbList != null) {
                urlQueryParams = breadcrumbList.getPortalUrlQueryParams();
            }
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

    private String getVisualizerExternalUrlBase() throws MetamacException {
        return removeUrlProtocol(removeLastSlashInUrl(configurationService.retrievePortalExternalUrlBase()));
    }

    private String getVisualizerApplicationExternalUrlBase() throws MetamacException {
        return removeLastSlashInUrl(getConfigurationService().retrievePortalExternalWebApplicationUrlVisualizer());
    }

    private String getPermalinksUrlBase() throws MetamacException {
        return removeUrlProtocol(removeLastSlashInUrl(configurationService.retrievePortalExternalApisPermalinksUrlBase()));
    }

    private String getPermalinksUrlBaseWithProtocol() throws MetamacException {
        return removeLastSlashInUrl(configurationService.retrievePortalExternalApisPermalinksUrlBase());
    }

    private String getCaptchaExternalApiUrlBase() throws MetamacException {
        return removeLastSlashInUrl(configurationService.retrieveCaptchaExternalApiUrlBase());
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
