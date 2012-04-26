package es.gobcan.istac.indicators.web.diffusion.indicators;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import es.gobcan.istac.indicators.web.diffusion.BaseController;
import es.gobcan.istac.indicators.web.diffusion.WebConstants;
import es.gobcan.istac.indicators.web.diffusion.utils.IndicatorsWebUtils;

@Controller
public class IndicatorsController extends BaseController {

    // TODO Esta página no se va mostrar. Si se muestra, implementar la paginación
    @RequestMapping(value = "/indicators", method = RequestMethod.GET)
    public ModelAndView indicators(UriComponentsBuilder uriComponentsBuilder) throws Exception {

        // Get json from API
        String urlPath = uriComponentsBuilder.path("/api/indicators/v1.0/indicators/?limit=1000").build().toUriString(); 
        String json =  IndicatorsWebUtils.getJson(uriComponentsBuilder, urlPath);

        // View
        ModelAndView modelAndView = new ModelAndView(WebConstants.VIEW_NAME_INDICATORS_LIST);
        modelAndView.addObject("indicators", json);
        
        return modelAndView;
    }
}