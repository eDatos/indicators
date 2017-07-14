package es.gobcan.istac.indicators.web.diffusion.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
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
        String indicatorsExternalWebUrlBase = getIndicatorsExternalWebUrlBaseWithoutProtocol();
        model.put("serverURL", indicatorsExternalWebUrlBase);

        String indicatorsExternalApiUrlBase = getIndicatorsExternalApiUrlBaseWithoutProtocol();
        model.put("indicatorsExternalApiUrlBase", indicatorsExternalApiUrlBase);

        String idxManagerSearchFormUrl = getIdxManagerSearchFormUrl();
        model.put("idxManagerSearchFormUrl", idxManagerSearchFormUrl);

        String visualizerExternalUrlBase = getVisualizerExternalUrlBase();
        model.put("visualizerExternalUrlBase", visualizerExternalUrlBase);

        model.put("visualizerApplicationExternalUrlBase", getVisualizerApplicationExternalUrlBase());
        model.put("analyticsGoogleTrackingId", getConfigurationService().retrieveAnalyticsGoogleTrackingId());

        String permalinksUrlBase = getPermalinksUrlBase();
        model.put("permalinksUrlBase", permalinksUrlBase);

        addStatisticalVisualizerUtils(model);

        super.doRender(model, request, response);
    }

    private void addStatisticalVisualizerUtils(Map<String, Object> model) {
        try {
            BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
            TemplateHashModel staticModels = wrapper.getStaticModels();
            TemplateHashModel statisticalVisualizerUtil = (TemplateHashModel) staticModels.get("es.gobcan.istac.indicators.core.util.StatisticalVisualizerUtils");
            model.put("statisticalVisualizerUtil", statisticalVisualizerUtil);
        } catch (TemplateModelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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

    private String getVisualizerExternalUrlBase() throws MetamacException {
        return removeUrlProtocol(removeLastSlashInUrl(configurationService.retrievePortalExternalUrlBase()));
    }

    private String getVisualizerApplicationExternalUrlBase() throws MetamacException {
        return removeLastSlashInUrl(getConfigurationService().retrievePortalExternalWebApplicationUrlVisualizer());
    }

    private String getPermalinksUrlBase() throws MetamacException {
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
