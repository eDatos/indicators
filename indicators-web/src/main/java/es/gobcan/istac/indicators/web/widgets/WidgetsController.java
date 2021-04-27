package es.gobcan.istac.indicators.web.widgets;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.web.diffusion.BaseController;
import es.gobcan.istac.indicators.web.diffusion.WebConstants;
import es.gobcan.istac.indicators.web.diffusion.view.BreadcrumbList;

@Controller
public class WidgetsController extends BaseController {

    @Autowired
    private IndicatorsConfigurationService configurationService;

    @Autowired
    private MessageSource                  messageSource;

    private String removeLastSlashInUrl(String url) {
        if (url.endsWith("/")) {
            return StringUtils.removeEnd(url, "/");
        }
        return url;
    }

    @RequestMapping(value = "/widgets/creator", method = RequestMethod.GET)
    public ModelAndView creator(@RequestParam(value = "type", defaultValue = "lastData") String type, HttpServletRequest request) throws Exception {
        BreadcrumbList breadcrumbList = getBreadCrumbList(type, request.getLocale());
        String description = getTranslatedTypeDescription(type, request.getLocale());

        // View
        ModelAndView modelAndView = new ModelAndView(WebConstants.VIEW_WIDGETS_CREATOR);

        modelAndView.addObject("breadcrumbList", breadcrumbList);
        modelAndView.addObject("description", description);

        return modelAndView;
    }

    private BreadcrumbList getBreadCrumbList(String type, Locale locale) throws Exception {
        String widgetTypeListUrl = configurationService.retrieveWidgetsTypeListUrl();
        String queryToolsUrl = configurationService.retrieveWidgetsQueryToolsUrl();
        String opendataUrl = configurationService.retrieveWidgetsOpendataUrl();

        BreadcrumbList breadCrumbList = new BreadcrumbList();

        breadCrumbList.addBreadcrumb(translate("page.open-data.title", locale), opendataUrl);
        breadCrumbList.addBreadcrumb(translate("page.query-tools.title", locale), queryToolsUrl);
        breadCrumbList.addBreadcrumb(translate("page.widgets.title", locale), widgetTypeListUrl);
        breadCrumbList.addBreadcrumb(getTranslatedWidgetTypeLabel(type, locale), "");

        return breadCrumbList;
    }

    public String getTranslatedWidgetTypeLabel(String type, Locale locale) {
        return translate(MessageFormat.format("entity.widgets.type.{0}.label", type), locale);
    }

    public String getTranslatedTypeDescription(String type, Locale locale) {
        return translate(MessageFormat.format("entity.widgets.type.{0}.description", type), locale);
    }

    @RequestMapping(value = "/widgets/external/configuration", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> properties() throws Exception {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put(WebConstants.VISUALIZER_APPLICATION_EXTERNAL_URL_PROPERTY, getVisualizerApplicationExternalUrlVisualizer());
        properties.put(WebConstants.WIDGETS_TYPE_LIST_URL_PROPERTY, configurationService.retrieveWidgetsTypeListUrl());
        properties.put(WebConstants.WIDGETS_SPARKLINE_MAX, configurationService.retrieveWidgetsSparklineMax());
        properties.put(WebConstants.ANALYTICS_GOOGLE_TRACKING_ID, configurationService.retrieveAnalyticsGoogleTrackingId());
        return properties;
    }

    private String getVisualizerApplicationExternalUrlVisualizer() throws MetamacException {
        return removeLastSlashInUrl(configurationService.retrievePortalExternalWebApplicationUrlVisualizer());
    }

    @RequestMapping(value = "/widgets/uwa/{permalinkId}", method = RequestMethod.GET)
    public ModelAndView uwa(@PathVariable("permalinkId") String permalinkId) throws UnsupportedEncodingException, MetamacException {
        ModelAndView modelAndView = new ModelAndView(WebConstants.VIEW_WIDGETS_UWA);
        modelAndView.addObject("permalinkId", permalinkId);

        return modelAndView;
    }

    @RequestMapping(value = "/widgets/example", method = RequestMethod.GET)
    public ModelAndView example(ServletRequest request) throws UnsupportedEncodingException {
        String options = new String(request.getParameter("options").getBytes(), "UTF-8");
        ModelAndView modelAndView = new ModelAndView(WebConstants.VIEW_WIDGETS_EXAMPLE);
        modelAndView.addObject("options", options);

        return modelAndView;
    }

    private String translate(String code, Locale locale) {
        return messageSource.getMessage(code, null, locale);
    }
}
