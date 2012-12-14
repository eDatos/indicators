package es.gobcan.istac.indicators.web.diffusion.apidoc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Template;
import freemarker.template.TemplateException;

@Controller
public class ApiDocController {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @RequestMapping(value = "/apidocs")
    public ModelAndView index(HttpServletRequest request) throws Exception {
        Map<String, String> viewModel = getViewModel(request);
        ModelAndView mv = new ModelAndView("apidocs/index", viewModel);
        return mv;
    }

    @RequestMapping(value = "/apidocs/apis")
    public void apis(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String templateName = "apidocs/apidocs.ftl";
        Map<String, String> viewModel = getViewModel(request);
        renderTemplate(templateName, viewModel, response);
    }

    @RequestMapping(value = "/apidocs/apis/indicators", produces = "application/json")
    public void indicatorApi(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String templateName = "apidocs/apidocs-indicators.ftl";
        Map<String, String> viewModel = getViewModel(request);
        renderTemplate(templateName, viewModel, response);
    }

    private void renderTemplate(String templateName, Map<String, String> viewModel, HttpServletResponse response) throws IOException, TemplateException {
        Template template = freeMarkerConfigurer.getConfiguration().getTemplate(templateName);
        response.setContentType("application/json");
        template.process(viewModel, response.getWriter());
    }

    private Map<String, String> getViewModel(HttpServletRequest request) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("appBaseUrl", getServerUrlWithContextPath(request));
        return parameters;
    }

    public String getServerUrlWithContextPath(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        return url.substring(0, url.length() - request.getPathInfo().length());
    }

}
