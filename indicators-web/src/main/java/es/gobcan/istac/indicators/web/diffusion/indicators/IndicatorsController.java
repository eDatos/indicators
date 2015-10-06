package es.gobcan.istac.indicators.web.diffusion.indicators;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.web.diffusion.BaseController;
import es.gobcan.istac.indicators.web.diffusion.WebConstants;

@Controller
public class IndicatorsController extends BaseController {

    @Autowired
    private IndicatorsConfigurationService configurationService;
    
    private String removeLastSlashInUrl(String url) {
        if (url.endsWith("/")) {
            return StringUtils.removeEnd(url, "/");
        }
        return url;
    }
   
    // Esta página no se va mostrar. es solo de prueba
    @RequestMapping(value = "/indicators", method = RequestMethod.GET)
    public ModelAndView indicators(UriComponentsBuilder uriComponentsBuilder) throws Exception {

        // View
        ModelAndView modelAndView = new ModelAndView(WebConstants.VIEW_NAME_INDICATORS_LIST);
        
        String indicatorsExternalApiUrlBase = removeLastSlashInUrl(configurationService.retrieveIndicatorsExternalApiUrlBase());
        modelAndView.addObject("indicatorsExternalApiUrlBase", indicatorsExternalApiUrlBase);
        
        String indicatorsWidgetsSearchFormUrl = configurationService.retrieveWidgetsSearchFormUrl();
        modelAndView.addObject("indicatorsWidgetsSearchFormUrl", indicatorsWidgetsSearchFormUrl);
        
        return modelAndView;
    }
}