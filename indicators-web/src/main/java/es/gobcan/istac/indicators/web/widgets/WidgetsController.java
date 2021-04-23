package es.gobcan.istac.indicators.web.widgets;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
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

    private String removeLastSlashInUrl(String url) {
        if (url.endsWith("/")) {
            return StringUtils.removeEnd(url, "/");
        }
        return url;
    }

    @RequestMapping(value = "/widgets/creator", method = RequestMethod.GET)
    public ModelAndView creator(@RequestParam(value = "type", defaultValue = "lastData") String type) throws Exception {
        BreadcrumbList breadcrumbList = getBreadCrumbList(type);
        String description = getTypeDescription(type);

        // View
        ModelAndView modelAndView = new ModelAndView("widgets/creator");

        modelAndView.addObject("breadcrumbList", breadcrumbList);
        modelAndView.addObject("description", description);

        return modelAndView;
    }

    private BreadcrumbList getBreadCrumbList(String type) throws Exception {
        String widgetTypeListUrl = configurationService.retrieveWidgetsTypeListUrl();
        String queryToolsUrl = configurationService.retrieveWidgetsQueryToolsUrl();
        String opendataUrl = configurationService.retrieveWidgetsOpendataUrl();

        BreadcrumbList breadCrumbList = new BreadcrumbList();
        breadCrumbList.addBreadcrumb("Datos abiertos", opendataUrl);
        breadCrumbList.addBreadcrumb("Herramientas de consulta", queryToolsUrl);
        breadCrumbList.addBreadcrumb("Widgets", widgetTypeListUrl);
        breadCrumbList.addBreadcrumb(getTypeLabel(type), "");

        return breadCrumbList;
    }

    public String getTypeLabel(String type) {
        if (type.equals("temporal")) {
            return "Gráfico de evolución";
        } else if (type.equals("recent")) {
            return "Últimos indicadores actualizados";
        } else {
            return "Últimos datos";
        }
    }

    public String getTypeDescription(String type) {
        if (type.equals("temporal")) {
            return "Visualiza un gráfico con la serie de datos de un indicador a seleccionar para los territorios que sean elegidos";
        } else if (type.equals("recent")) {
            return "Visualiza una tabla con los últimos indicadores actualizados de un territorio específico";
        } else {
            return "Visualiza una tabla con los últimos datos de una lista de indicadores seleccionados de un territorio específico";
        }
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
        ModelAndView modelAndView = new ModelAndView("widgets/uwa");
        modelAndView.addObject("permalinkId", permalinkId);

        return modelAndView;
    }

    @RequestMapping(value = "/widgets/example", method = RequestMethod.GET)
    public ModelAndView example(ServletRequest request) throws UnsupportedEncodingException {
        String options = new String(request.getParameter("options").getBytes(), "UTF-8");
        ModelAndView modelAndView = new ModelAndView("widgets/example");
        modelAndView.addObject("options", options);

        return modelAndView;
    }

}
