package es.gobcan.istac.indicators.web.diffusion.apidoc;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.util.UriTemplate;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.rest.IndicatorsRestConstants;
import es.gobcan.istac.indicators.rest.util.RESTURIUtil;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Controller
public class ApiDocController {

    @Autowired
    private FreeMarkerConfigurer           freeMarkerConfigurer;

    @Autowired
    private IndicatorsConfigurationService indicatorsConfigurationService;

    @RequestMapping(value = "/api/indicators/images/*")
    public void apidocsImages(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.sendRedirect(request.getRequestURI().replace("/api/indicators/images", "/apidocs/images"));
    }

    @RequestMapping(value = "/api/indicators/v1.0")
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // Header: available methods
        final String rootUri = request.getRequestURL().toString();
        final URI uriIndicatorsSystems = new UriTemplate("{rootUri}{resource}").expand(rootUri, IndicatorsRestConstants.API_INDICATORS_INDICATORS_SYSTEMS);
        final URI uriIndicators = new UriTemplate("{rootUri}{resource}").expand(rootUri, IndicatorsRestConstants.API_INDICATORS_INDICATORS);
        final URI uriTimeGranularities = new UriTemplate("{rootUri}{resource}").expand(rootUri, IndicatorsRestConstants.API_INDICATORS_TIME_GRANULARITIES);
        final URI uriGeographicGranularities = new UriTemplate("{rootUri}{resource}").expand(rootUri, IndicatorsRestConstants.API_INDICATORS_GEOGRAPHIC_GRANULARITIES);
        final URI uriGeographicalValues = new UriTemplate("{rootUri}{resource}").expand(rootUri, IndicatorsRestConstants.API_INDICATORS_GEOGRAPHICAL_VALUES);
        final URI uriThemes = new UriTemplate("{rootUri}{resource}").expand(rootUri, IndicatorsRestConstants.API_INDICATORS_SUBJECTS);

        final String linkToIndicatorsSystems = RESTURIUtil.createLinkHeader(uriIndicatorsSystems.toASCIIString(), RESTURIUtil.REL_COLLECTION);
        final String linkToIndicators = RESTURIUtil.createLinkHeader(uriIndicators.toASCIIString(), RESTURIUtil.REL_COLLECTION);
        final String linkToTimeGranularities = RESTURIUtil.createLinkHeader(uriTimeGranularities.toASCIIString(), RESTURIUtil.REL_COLLECTION);
        final String linkToGeographicGranularities = RESTURIUtil.createLinkHeader(uriGeographicGranularities.toASCIIString(), RESTURIUtil.REL_COLLECTION);
        final String linkToThemes = RESTURIUtil.createLinkHeader(uriThemes.toASCIIString(), RESTURIUtil.REL_COLLECTION);

        response.addHeader(RESTURIUtil.LINK, RESTURIUtil.gatherLinkHeaders(linkToIndicatorsSystems, linkToIndicators, linkToGeographicGranularities, linkToThemes));

        // Body: Documentation
        Map<String, String> viewModel = getViewModel(request);
        ModelAndView mv = new ModelAndView("apidocs/index", viewModel);
        return mv;
    }

    @RequestMapping(value = "/api/indicators/v1.0/docs")
    public void apis(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String templateName = "apidocs/apidocs.ftl";
        Map<String, String> viewModel = getViewModel(request);
        renderTemplate(templateName, viewModel, response);
    }

    @RequestMapping(value = "/api/indicators/v1.0/docs/indicators", produces = "application/json")
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
