package es.gobcan.istac.indicators.web.widgets;

import java.util.ArrayList;
import java.util.List;

import es.gobcan.istac.indicators.web.diffusion.WebConstants;
import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.diffusion.BaseController;

@Controller
public class WidgetsCreatorController extends BaseController {

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;
    
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

}
