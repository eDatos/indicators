package es.gobcan.istac.indicators.web.diffusion.indicators;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import es.gobcan.istac.indicators.web.diffusion.BaseController;
import es.gobcan.istac.indicators.web.diffusion.WebConstants;
import es.gobcan.istac.indicators.web.diffusion.view.BreadcrumbList;

@Controller
public class IndicatorsController extends BaseController {

    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = "/indicators", method = RequestMethod.GET)
    public ModelAndView indicators(UriComponentsBuilder uriComponentsBuilder, HttpServletRequest request) throws Exception {

        // View
        ModelAndView modelAndView = new ModelAndView(WebConstants.VIEW_NAME_INDICATORS_LIST);

        modelAndView.addObject("breadcrumbList", new BreadcrumbList(translate("entity.indicators", request.getLocale())));

        return modelAndView;
    }

    private String translate(String code, Locale locale) {
        return messageSource.getMessage(code, null, locale);
    }

}