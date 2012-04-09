package es.gobcan.istac.indicators.web.indicatorssystems;

import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.BaseController;
import es.gobcan.istac.indicators.web.WebConstants;

@Controller
public class IndicatorsSystemsController extends BaseController {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    // TODO tratamiento de excepciones
    // TODO paginación
    @RequestMapping(value = "/indicators-systems", method = RequestMethod.GET)
    public ModelAndView indicatorsSystems() throws Exception {
        
        // Retrieve all indicators system published
        List<IndicatorsSystemDto> indicatorsSystemsDto = indicatorsServiceFacade.findIndicatorsSystemsPublished(getServiceContext(), null);
        
        // To Json
        ObjectMapper mapper = new ObjectMapper();
        String indicatorsSystemsJson = mapper.writeValueAsString(indicatorsSystemsDto); // TODO reducir tamaño del json

        // View
        ModelAndView modelAndView = new ModelAndView(WebConstants.VIEW_NAME_INDICATORS_SYSTEMS_LIST);
        modelAndView.addObject("indicatorsSystems", indicatorsSystemsJson);
 
        return modelAndView;
    }
}