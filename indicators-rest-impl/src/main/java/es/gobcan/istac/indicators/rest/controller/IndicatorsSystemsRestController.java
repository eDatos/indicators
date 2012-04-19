package es.gobcan.istac.indicators.rest.controller;

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
import org.springframework.web.util.UriComponentsBuilder;

import es.gobcan.istac.indicators.rest.RestConstants;
import es.gobcan.istac.indicators.rest.facadeapi.IndicatorSystemRestFacade;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceDataType;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemType;
import es.gobcan.istac.indicators.rest.types.NoPagedResultType;
import es.gobcan.istac.indicators.rest.types.PagedResultType;
import es.gobcan.istac.indicators.rest.types.RestCriteriaPaginator;
import es.gobcan.istac.indicators.rest.util.HttpHeaderUtil;

@Controller("indicatorsSystemsRestController")
@RequestMapping("/api/indicators/v1.0/*")
public class IndicatorsSystemsRestController extends AbstractRestController {

    @Autowired
    private IndicatorSystemRestFacade indicatorSystemRestFacade = null;
    
    /**
     * @throws Exception
     * @throws ApplicationException
     */
    @RequestMapping(value = "/indicatorsSystems", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<PagedResultType<IndicatorsSystemBaseType>> findIndicatorsSystems(final UriComponentsBuilder uriComponentsBuilder,
                                                                                           @RequestParam(required=false, value="limit") final Integer limit,
                                                                                           @RequestParam(required=false, value="offset") final Integer offset) throws Exception {
        
        String baseURL = uriComponentsBuilder.build().toUriString();
        RestCriteriaPaginator paginator = new RestCriteriaPaginator(limit, offset);
        PagedResultType<IndicatorsSystemBaseType> indicatorsSystemBaseTypes = indicatorSystemRestFacade.findIndicatorsSystems(baseURL, paginator);
        
        baseURL = uriComponentsBuilder.path(RestConstants.API_INDICATORS_BASE).path(RestConstants.API_SLASH).path(RestConstants.API_INDICATORS_INDICATORS_SYSTEMS).build().toUriString();
        HttpHeaders headers = HttpHeaderUtil.createPagedHeaders(baseURL, indicatorsSystemBaseTypes);
        ResponseEntity<PagedResultType<IndicatorsSystemBaseType>> response = new ResponseEntity<PagedResultType<IndicatorsSystemBaseType>>(indicatorsSystemBaseTypes, headers, HttpStatus.OK);
        return response;
    }

    /**
     * @throws Exception
     * @throws ApplicationException
     */
    @RequestMapping(value = "/indicatorsSystems/{idIndicatorSystem}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<IndicatorsSystemType> retrieveIndicatorsSystem(final UriComponentsBuilder uriComponentsBuilder,
                                                                         @PathVariable("idIndicatorSystem") final String idIndicatorSystem) throws Exception {

        String baseURL = uriComponentsBuilder.build().toUriString();
        IndicatorsSystemType indicatorsSystemBaseType = indicatorSystemRestFacade.retrieveIndicatorsSystem(baseURL, idIndicatorSystem);
        ResponseEntity<IndicatorsSystemType> response = new ResponseEntity<IndicatorsSystemType>(indicatorsSystemBaseType, HttpStatus.OK);
        return response;
    }

    /**
     * @throws Exception
     * @throws ApplicationException
     */
    @RequestMapping(value = "/indicatorsSystems/{idIndicatorSystem}/indicatorsInstances", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<NoPagedResultType<IndicatorInstanceType>> retrieveIndicatorsInstances(final UriComponentsBuilder uriComponentsBuilder,
                                                                                   @PathVariable("idIndicatorSystem") final String idIndicatorSystem) throws Exception {
        String baseURL = uriComponentsBuilder.build().toUriString();
        NoPagedResultType<IndicatorInstanceType> indicatorInstanceTypes = indicatorSystemRestFacade.retrieveIndicatorsInstances(baseURL, idIndicatorSystem);
        ResponseEntity<NoPagedResultType<IndicatorInstanceType>> response = new ResponseEntity<NoPagedResultType<IndicatorInstanceType>>(indicatorInstanceTypes, HttpStatus.OK);
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
        String baseURL = uriComponentsBuilder.build().toUriString();
        IndicatorInstanceType indicatorInstanceType = indicatorSystemRestFacade.retrieveIndicatorsInstance(baseURL, idIndicatorSystem, uuidIndicatorInstance);
        ResponseEntity<IndicatorInstanceType> response = new ResponseEntity<IndicatorInstanceType>(indicatorInstanceType, HttpStatus.OK);
        return response;
    }
    
    /**
     * @throws Exception
     * @throws ApplicationException
     */
    @RequestMapping(value = "/indicatorsSystems/{idIndicatorSystem}/indicatorsInstances/{uuidIndicatorInstance}/data", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<IndicatorInstanceDataType> retrieveIndicatorsInstanceData(final UriComponentsBuilder uriComponentsBuilder,
                                                                    @PathVariable("idIndicatorSystem") final String idIndicatorSystem,
                                                                    @PathVariable("uuidIndicatorInstance") final String uuidIndicatorInstance) throws Exception {
        String baseURL = uriComponentsBuilder.build().toUriString();
        IndicatorInstanceDataType indicatorInstanceDataType = indicatorSystemRestFacade.retrieveIndicatorsInstanceData(baseURL, idIndicatorSystem, uuidIndicatorInstance);
        ResponseEntity<IndicatorInstanceDataType> response = new ResponseEntity<IndicatorInstanceDataType>(indicatorInstanceDataType, HttpStatus.OK);
        return response;
    }
    
}
