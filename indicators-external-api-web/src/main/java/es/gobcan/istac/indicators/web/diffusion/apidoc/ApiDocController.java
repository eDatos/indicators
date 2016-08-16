package es.gobcan.istac.indicators.web.diffusion.apidoc;

import java.net.URI;
import java.util.HashMap;

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
        return new ModelAndView("apidocs/index", new HashMap<String, String>());
    }

    @RequestMapping(value = "/api/indicators/v1.0/docs", produces = "application/json")
    public ModelAndView indicatorApi(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/json");

        return new ModelAndView("apidocs/v1.0/swagger", new HashMap<String, String>());
    }

}
