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
        String breadCrumb = getBreadCrumb(type);

        // View
        ModelAndView modelAndView = new ModelAndView("widgets/creator");

        String jaxiUrlBase = removeLastSlashInUrl(configurationService.retrieveJaxiRemoteUrl());
        String metamacPortalUrlBase = removeLastSlashInUrl(configurationService.retrievePortalExternalUrlBase());
        String metamacPortalPermalinksEndpoint = removeLastSlashInUrl(configurationService.retrievePortalExternalApisPermalinksUrlBase());
        String indicatorsExternalApiUrlBase = removeLastSlashInUrl(configurationService.retrieveIndicatorsExternalApiUrlBase());

        modelAndView.addObject("metamacPortalPermalinksEndpoint", metamacPortalPermalinksEndpoint);
        modelAndView.addObject("jaxiUrlBase", jaxiUrlBase);
        modelAndView.addObject("metamacPortalUrlBase", metamacPortalUrlBase);
        modelAndView.addObject("indicatorsExternalApiUrlBase", indicatorsExternalApiUrlBase);
        modelAndView.addObject("breadcrumb", breadCrumb);

        return modelAndView;
    }

    private String getBreadCrumb(String type) throws Exception {
        String widgetTypeListUrl = configurationService.retrieveWidgetsTypeListUrl();
        String queryToolsUrl = configurationService.retrieveWidgetsQueryToolsUrl();
        return "<li><a href='" + queryToolsUrl + "'>Herramientas de consulta</a></li><li><a href='" + widgetTypeListUrl + "'>Widgets</a></li><li><strong>" + getTypeLabel(type) + "</strong></li>";
    }

    public String getTypeLabel(String type) {
        if (type.equals("temporal")) {
            return "Temporal";
        } else if (type.equals("recent")) {
            return "Último dato más reciente";
        } else {
            return "Último dato";
        }
    }

    @RequestMapping(value = "/widgets/external/configuration", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> properties() throws Exception {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put(WebConstants.JAXI_URL_PROPERTY, configurationService.retrieveJaxiRemoteUrl());
        properties.put(WebConstants.WIDGETS_TYPE_LIST_URL_PROPERTY, configurationService.retrieveWidgetsTypeListUrl());
        properties.put(WebConstants.WIDGETS_SPARKLINE_MAX, configurationService.retrieveWidgetsSparklineMax());
        return properties;
    }

    @RequestMapping(value = "/widgets/uwa/{permalinkId}", method = RequestMethod.GET)
    public ModelAndView uwa(@PathVariable("permalinkId") String permalinkId) throws UnsupportedEncodingException, MetamacException {
        ModelAndView modelAndView = new ModelAndView("widgets/uwa");
        modelAndView.addObject("permalinkId", permalinkId);

        String metamacPortalPermalinksEndpoint = removeLastSlashInUrl(configurationService.retrievePortalExternalApisPermalinksUrlBase());
        modelAndView.addObject("metamacPortalPermalinksEndpoint", metamacPortalPermalinksEndpoint);

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
