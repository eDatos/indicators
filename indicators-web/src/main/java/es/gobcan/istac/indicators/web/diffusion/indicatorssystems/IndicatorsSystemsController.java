package es.gobcan.istac.indicators.web.diffusion.indicatorssystems;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import es.gobcan.istac.indicators.rest.types.ElementLevelType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemType;
import es.gobcan.istac.indicators.web.diffusion.BaseController;
import es.gobcan.istac.indicators.web.diffusion.WebConstants;
import es.gobcan.istac.indicators.web.diffusion.view.BreadcrumbList;
import es.gobcan.istac.indicators.web.rest.clients.RestApiLocatorExternal;

@Controller
public class IndicatorsSystemsController extends BaseController {

    @Autowired
    private RestApiLocatorExternal restApiLocatorExternal;

    @RequestMapping(value = "/indicatorsSystems", method = RequestMethod.GET)
    public ModelAndView indicatorsSystems(UriComponentsBuilder uriComponentsBuilder) throws Exception {
        // View
        ModelAndView modelAndView = new ModelAndView(WebConstants.VIEW_NAME_INDICATORS_SYSTEMS_LIST);
        modelAndView.addObject("breadcrumbList", new BreadcrumbList("Sistemas de indicadores"));

        return modelAndView;
    }

    @RequestMapping(value = "/indicatorsSystems/{code}", method = RequestMethod.GET)
    public ModelAndView indicatorsSystem(UriComponentsBuilder uriComponentsBuilder, @PathVariable("code") String code, Model model) throws Exception {

        // View
        ModelAndView modelAndView = new ModelAndView(WebConstants.VIEW_NAME_INDICATORS_SYSTEM_VIEW);

        IndicatorsSystemType indicator = restApiLocatorExternal.getIndicatorsSystemsByCode(code);

        modelAndView.addObject("breadcrumbList", new BreadcrumbList("Sistemas de indicadores", code));

        int numberOfFixedDigitsInNumeration = numberOfFixedDigitsInNumeration(indicator);

        modelAndView.addObject("indicatorsSystemCode", code);
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
            for (ElementLevelType element : elements) {
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