package es.gobcan.istac.indicators.rest.controller;

import es.gobcan.istac.indicators.rest.RestConstants;
import es.gobcan.istac.indicators.rest.util.RESTURIUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;

@Controller("indicatorsDiscoveryRestController")
@RequestMapping("/api/indicators/v1.0/*")
public class IndicatorsDiscoveryRestController extends AbstractRestController {
   
    // API
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public final void adminRoot(final HttpServletRequest request, final HttpServletResponse response) {
        final String rootUri = request.getRequestURL().toString();

        final URI uriIndicatorsSystems = new UriTemplate("{rootUri}{resource}").expand(rootUri, RestConstants.API_INDICATORS_INDICATORS_SYSTEMS);
        final URI uriIndicators = new UriTemplate("{rootUri}{resource}").expand(rootUri, RestConstants.API_INDICATORS_INDICATORS);
        final URI uriGeographicGranularities= new UriTemplate("{rootUri}{resource}").expand(rootUri, RestConstants.API_INDICATORS_GEOGRAPHIC_GRANULARITIES);
        final URI uriThemes= new UriTemplate("{rootUri}{resource}").expand(rootUri, RestConstants.API_INDICATORS_SUBJECTS);
        
        final String linkToIndicatorsSystems = RESTURIUtil.createLinkHeader(uriIndicatorsSystems.toASCIIString(), RESTURIUtil.REL_COLLECTION);
        final String linkToIndicators = RESTURIUtil.createLinkHeader(uriIndicators.toASCIIString(), RESTURIUtil.REL_COLLECTION);
        final String linkToGeographicGranularities = RESTURIUtil.createLinkHeader(uriGeographicGranularities.toASCIIString(), RESTURIUtil.REL_COLLECTION);
        final String linkToThemes = RESTURIUtil.createLinkHeader(uriThemes.toASCIIString(), RESTURIUtil.REL_COLLECTION);

        response.addHeader(RESTURIUtil.LINK, RESTURIUtil.gatherLinkHeaders(linkToIndicatorsSystems, linkToIndicators, linkToGeographicGranularities, linkToThemes));
    }

}
