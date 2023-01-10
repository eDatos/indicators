package es.gobcan.istac.indicators.web.diffusion;

import static es.gobcan.istac.indicators.web.diffusion.view.WebUtils.PARAM_APP_NAME;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

@Controller
public class IndexController extends BaseController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request) {

        Locale currentLocale = RequestContextUtils.getLocaleResolver(request).resolveLocale(request);

        // View
        ModelAndView modelAndView = new ModelAndView(WebConstants.VIEW_NAME_INDEX);
        modelAndView.addObject(PARAM_APP_NAME, translate("app.title", currentLocale));
        return modelAndView;
    }

}
