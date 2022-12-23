package es.gobcan.istac.indicators.web.diffusion.indicators;

import static es.gobcan.istac.indicators.web.diffusion.view.WebUtils.PARAM_APP_NAME;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.util.UriComponentsBuilder;

import es.gobcan.istac.indicators.web.diffusion.BaseController;
import es.gobcan.istac.indicators.web.diffusion.WebConstants;
import es.gobcan.istac.indicators.web.diffusion.view.BreadcrumbList;

@Controller
public class IndicatorsController extends BaseController {

    @RequestMapping(value = "/indicators", method = RequestMethod.GET)
    public ModelAndView indicators(UriComponentsBuilder uriComponentsBuilder, HttpServletRequest request) throws Exception {

        // View
        ModelAndView modelAndView = new ModelAndView(WebConstants.VIEW_NAME_INDICATORS_LIST);

        Locale currentLocale = RequestContextUtils.getLocaleResolver(request).resolveLocale(request);
        modelAndView.addObject("breadcrumbList", new BreadcrumbList(translate("entity.indicators", currentLocale)));
        modelAndView.addObject(PARAM_APP_NAME, translate("app.title", currentLocale));

        return modelAndView;
    }

}