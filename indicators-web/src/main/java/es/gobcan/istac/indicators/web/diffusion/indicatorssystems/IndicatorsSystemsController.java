package es.gobcan.istac.indicators.web.diffusion.indicatorssystems;

import es.gobcan.istac.indicators.rest.facadeapi.IndicatorSystemRestFacade;
import es.gobcan.istac.indicators.rest.types.ElementLevelType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemType;
import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import es.gobcan.istac.indicators.web.diffusion.BaseController;
import es.gobcan.istac.indicators.web.diffusion.WebConstants;

import java.util.List;

@Controller
public class IndicatorsSystemsController extends BaseController {

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private IndicatorSystemRestFacade indicatorSystemRestFacade = null;

    // TODO Esta página no se va mostrar. Si se muestra, implementar la paginación
    @RequestMapping(value = "/indicatorsSystems", method = RequestMethod.GET)
    public ModelAndView indicatorsSystems(UriComponentsBuilder uriComponentsBuilder) throws Exception {
        // View
        ModelAndView modelAndView = new ModelAndView(WebConstants.VIEW_NAME_INDICATORS_SYSTEMS_LIST);
        return modelAndView;
    }

    @RequestMapping(value = "/indicatorsSystems/{code}", method = RequestMethod.GET)
    public ModelAndView indicatorsSystem(UriComponentsBuilder uriComponentsBuilder, @PathVariable("code") String code, Model model) throws Exception {

        // View
        ModelAndView modelAndView = new ModelAndView(WebConstants.VIEW_NAME_INDICATORS_SYSTEM_VIEW);


        String baseURL = uriComponentsBuilder.build().toUriString();
        IndicatorsSystemType indicator = indicatorSystemRestFacade.retrieveIndicatorsSystem(baseURL, code);

        int numberOfFixedDigitsInNumeration = numberOfFixedDigitsInNumeration(indicator);

        // Jaxi URL
        String jaxiUrlBase = configurationService.getProperties().getProperty(WebConstants.JAXI_URL_PROPERTY);
        if (jaxiUrlBase.endsWith("/")) {
            jaxiUrlBase = StringUtils.removeEnd(jaxiUrlBase, "/");
        }
        modelAndView.addObject("indicatorsSystemCode", code);
        modelAndView.addObject("jaxiUrlBase", jaxiUrlBase);
        modelAndView.addObject("indicator", indicator);
        modelAndView.addObject("numberOfFixedDigitsInNumeration", numberOfFixedDigitsInNumeration);

        return modelAndView;
    }

    private int numberOfFixedDigitsInNumeration(IndicatorsSystemType indicator) {
        Integer totalIndicatorInstances = countIndicatorInstances(indicator.getElements());
        int fixedDigits = totalIndicatorInstances.toString().length();
        if (fixedDigits < 2) {
            fixedDigits = 2;
        }
        return fixedDigits;
    }

    private int countIndicatorInstances(List<ElementLevelType> elements) {
        int total = 0;
        if (elements != null) {
            for(ElementLevelType element : elements){
                if ("indicators#indicatorInstance".equals(element.getKind())) {
                    total += 1;
                } else {
                    total += countIndicatorInstances(element.getElements());
                }
            }
        }
        return total;
    }

}