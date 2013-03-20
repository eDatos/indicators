package es.gobcan.istac.indicators.web.widgets;

import es.gobcan.istac.indicators.web.diffusion.BaseController;
import es.gobcan.istac.indicators.web.diffusion.WebConstants;
import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class WidgetsController extends BaseController {

    @Autowired
    private ConfigurationService configurationService;
    
    @RequestMapping(value = "/widgets/creator", method = RequestMethod.GET)
    public ModelAndView creator(@RequestParam(value="type", defaultValue = "lastData") String type) throws Exception {
        String breadCrumb = getBreadCrumb(type);

        // View
        ModelAndView modelAndView = new ModelAndView("widgets/creator");

        String jaxiUrlBase = configurationService.getProperties().getProperty(WebConstants.JAXI_URL_PROPERTY);
        if (jaxiUrlBase.endsWith("/")) {
            jaxiUrlBase = StringUtils.removeEnd(jaxiUrlBase, "/");
        }

        modelAndView.addObject("jaxiUrlBase", jaxiUrlBase);
        modelAndView.addObject("breadcrumb", breadCrumb);

        return modelAndView;
    }

    private String getBreadCrumb(String type) {
        String widgetTypeListUrl = configurationService.getProperty(WebConstants.WIDGETS_TYPE_LIST_URL_PROPERTY);
        String queryToolsUrl = configurationService.getProperty(WebConstants.WIDGETS_QUERY_TOOLS_URL_PROPERTY);
        return "<li><a href='" + queryToolsUrl + "'>Herramientas de consulta</a></li><li><a href='" + widgetTypeListUrl + "'>Widgets</a></li><li><strong>" + getTypeLabel(type) + "</strong></li>";
    }

    public String getTypeLabel(String type) {
        if(type.equals("temporal")) {
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
        properties.put(WebConstants.JAXI_URL_PROPERTY, configurationService.getProperties().getProperty(WebConstants.JAXI_URL_PROPERTY));
        properties.put(WebConstants.WIDGETS_TYPE_LIST_URL_PROPERTY, configurationService.getProperties().getProperty(WebConstants.WIDGETS_TYPE_LIST_URL_PROPERTY));
        return properties;
    }

    @RequestMapping(value = "/widgets/uwa", method = RequestMethod.GET)
    public ModelAndView uwa(ServletRequest request) throws UnsupportedEncodingException {
        String options = new String(request.getParameter("options").getBytes(), "UTF-8");
        ModelAndView modelAndView = new ModelAndView("widgets/uwa");
        modelAndView.addObject("options", options);
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
