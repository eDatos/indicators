package es.gobcan.istac.indicators.rest.controller;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fornax.cartridges.sculptor.framework.errorhandling.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriTemplate;

import es.gobcan.istac.indicators.rest.facadeapi.IndicatorSystemRestFacade;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemType;
import es.gobcan.istac.indicators.rest.types.PagedResultType;
import es.gobcan.istac.indicators.rest.types.RestCriteriaPaginator;
import es.gobcan.istac.indicators.rest.util.HttpHeaderUtil;
import es.gobcan.istac.indicators.rest.util.RESTURIUtil;

@Controller("indicatorsSystemsRestController")
@RequestMapping("/api/indicators/v1.0/*")
public class IndicatorsSystemsRestController extends AbstractRestController {

    @Autowired
    private IndicatorSystemRestFacade indicatorSystemRestFacade = null;
    
    // API
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public final void adminRoot(final HttpServletRequest request, final HttpServletResponse response) {
        final String rootUri = request.getRequestURL().toString();

        final URI uri = new UriTemplate("{rootUri}{resource}").expand(rootUri, "indicatorsSystems");
        final String linkToIndicatorsSystems = RESTURIUtil.createLinkHeader(uri.toASCIIString(), RESTURIUtil.REL_COLLECTION);

        response.addHeader(RESTURIUtil.LINK, RESTURIUtil.gatherLinkHeaders(linkToIndicatorsSystems));
    }

    /**
     * @throws Exception
     * @throws ApplicationException
     */
    @RequestMapping(value = "/indicatorsSystems", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<PagedResultType<IndicatorsSystemBaseType>> findIndicatorsSystems(final UriComponentsBuilder uriComponentsBuilder,
                                                                                           @RequestParam(required=false, value="limit") final Integer limit,
                                                                                           @RequestParam(required=false, value="offset") final Integer offset) throws Exception {
        RestCriteriaPaginator paginator = new RestCriteriaPaginator(limit, offset);
        PagedResultType<IndicatorsSystemBaseType> indicatorsSystemBaseTypes = indicatorSystemRestFacade.findIndicatorsSystems(uriComponentsBuilder, paginator);
        
        HttpHeaders headers = HttpHeaderUtil.createPagedHeaders(uriComponentsBuilder, indicatorsSystemBaseTypes);
        ResponseEntity<PagedResultType<IndicatorsSystemBaseType>> response = new ResponseEntity<PagedResultType<IndicatorsSystemBaseType>>(indicatorsSystemBaseTypes, headers, HttpStatus.OK);
        return response;
    }

    /**
     * @throws Exception
     * @throws ApplicationException
     */
    @RequestMapping(value = "/indicatorsSystems/{idIndicatorSystem}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<IndicatorsSystemType> retrieveIndicatorsSystem(@PathVariable("idIndicatorSystem") final String idIndicatorSystem,
                                                                         final UriComponentsBuilder uriComponentsBuilder) throws Exception {

        IndicatorsSystemType indicatorsSystemBaseType = indicatorSystemRestFacade.retrieveIndicatorsSystem(uriComponentsBuilder, idIndicatorSystem);
        ResponseEntity<IndicatorsSystemType> response = new ResponseEntity<IndicatorsSystemType>(indicatorsSystemBaseType, HttpStatus.OK);
        return response;
    }

    /**
     * @throws Exception
     * @throws ApplicationException
     */
    @RequestMapping(value = "/indicatorsSystems/{idIndicatorSystem}/indicatorsInstances", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<IndicatorInstanceType>> retrieveIndicatorsInstances(final UriComponentsBuilder uriComponentsBuilder,
                                                                                   @PathVariable("idIndicatorSystem") final String idIndicatorSystem) throws Exception {
        List<IndicatorInstanceType> indicatorInstanceTypes = indicatorSystemRestFacade.retrieveIndicatorsInstances(uriComponentsBuilder, idIndicatorSystem);
        ResponseEntity<List<IndicatorInstanceType>> response = new ResponseEntity<List<IndicatorInstanceType>>(indicatorInstanceTypes, HttpStatus.OK);
        return response;
    }
    
    /**
     * @throws Exception
     * @throws ApplicationException
     */
    @RequestMapping(value = "/indicatorsSystems/{idIndicatorSystem}/indicatorsInstances/{uuidIndicatorInstance}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<IndicatorInstanceType> retrieveIndicatorsInstance(final UriComponentsBuilder uriComponentsBuilder,
                                                                    @PathVariable("idIndicatorSystem") final String idIndicatorSystem,
                                                                    @PathVariable("uuidIndicatorInstance") final String uuidIndicatorInstance) throws Exception {
        IndicatorInstanceType indicatorInstanceType = indicatorSystemRestFacade.retrieveIndicatorsInstance(uriComponentsBuilder, idIndicatorSystem, uuidIndicatorInstance);
        ResponseEntity<IndicatorInstanceType> response = new ResponseEntity<IndicatorInstanceType>(indicatorInstanceType, HttpStatus.OK);
        return response;
    }
    
    // FALTA OBTENER LOS DATOS DE UNA INSTANCIA
}
