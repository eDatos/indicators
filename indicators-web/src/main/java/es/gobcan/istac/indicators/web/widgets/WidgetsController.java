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
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
public class WidgetsController extends BaseController {

    @Autowired
    private ConfigurationService configurationService;
    
    @RequestMapping(value = "/widgets/creator", method = RequestMethod.GET)
    public ModelAndView creator() throws Exception {
        // View
        ModelAndView modelAndView = new ModelAndView("widgets/creator");

        String jaxiUrlBase = configurationService.getProperties().getProperty(WebConstants.JAXI_URL_PROPERTY);
        if (jaxiUrlBase.endsWith("/")) {
            jaxiUrlBase = StringUtils.removeEnd(jaxiUrlBase, "/");
        }
        modelAndView.addObject("jaxiUrlBase", jaxiUrlBase);

        return modelAndView;
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
