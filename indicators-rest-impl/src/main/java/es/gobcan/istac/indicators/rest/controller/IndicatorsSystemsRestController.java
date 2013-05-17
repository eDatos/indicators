package es.gobcan.istac.indicators.rest.controller;

import es.gobcan.istac.indicators.rest.RestConstants;
import es.gobcan.istac.indicators.rest.facadeapi.IndicatorSystemRestFacade;
import es.gobcan.istac.indicators.rest.types.*;
import es.gobcan.istac.indicators.rest.util.HttpHeaderUtil;
import es.gobcan.istac.indicators.rest.util.RequestUtil;
import org.fornax.cartridges.sculptor.framework.errorhandling.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Controller("indicatorsSystemsRestController")
public class IndicatorsSystemsRestController extends AbstractRestController {

    @Autowired
    private IndicatorSystemRestFacade indicatorSystemRestFacade = null;

    @RequestMapping(value = "/api/indicators/v1.0/indicatorsSystems", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<PagedResultType<IndicatorsSystemBaseType>> findIndicatorsSystems(final UriComponentsBuilder uriComponentsBuilder,
                                                                                           @RequestParam(required = false, value = "limit") final Integer limit,
                                                                                           @RequestParam(required = false, value = "offset") final Integer offset) throws Exception {

        String baseURL = uriComponentsBuilder.build().toUriString();
        RestCriteriaPaginator paginator = new RestCriteriaPaginator(limit, offset);
        PagedResultType<IndicatorsSystemBaseType> indicatorsSystemBaseTypes = indicatorSystemRestFacade.findIndicatorsSystems(baseURL, paginator);

        baseURL = uriComponentsBuilder.path(RestConstants.API_INDICATORS_BASE).path(RestConstants.API_SLASH).path(RestConstants.API_INDICATORS_INDICATORS_SYSTEMS).build().toUriString();
        HttpHeaders headers = HttpHeaderUtil.createPagedHeaders(baseURL, indicatorsSystemBaseTypes);
        ResponseEntity<PagedResultType<IndicatorsSystemBaseType>> response = new ResponseEntity<PagedResultType<IndicatorsSystemBaseType>>(indicatorsSystemBaseTypes, headers, HttpStatus.OK);
        return response;
    }

    @RequestMapping(value = "/api/indicators/v1.0/indicatorsSystems/{idIndicatorSystem}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<IndicatorsSystemType> retrieveIndicatorsSystem(final UriComponentsBuilder uriComponentsBuilder,
                                                                         @PathVariable("idIndicatorSystem") final String idIndicatorSystem) throws Exception {

        String baseURL = uriComponentsBuilder.build().toUriString();
        IndicatorsSystemType indicatorsSystemBaseType = indicatorSystemRestFacade.retrieveIndicatorsSystem(baseURL, idIndicatorSystem);
        ResponseEntity<IndicatorsSystemType> response = new ResponseEntity<IndicatorsSystemType>(indicatorsSystemBaseType, HttpStatus.OK);
        return response;
    }


    @RequestMapping(value = "/api/indicators/v1.0/indicatorsSystems/{idIndicatorSystem}/indicatorsInstances", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<PagedResultType<IndicatorInstanceBaseType>> retrieveIndicatorsInstances(final UriComponentsBuilder uriComponentsBuilder,
                                                                                                  @PathVariable("idIndicatorSystem") final String idIndicatorSystem,
                                                                                                  @RequestParam(required = false, value = "q") final String q,
                                                                                                  @RequestParam(required = false, value = "order") final String order,
                                                                                                  @RequestParam(required = false, value = "limit") final Integer limit,
                                                                                                  @RequestParam(required = false, value = "offset") final Integer offset,
                                                                                                  @RequestParam(required = false, value = "fields") final String fields) throws Exception {
        String baseURL = uriComponentsBuilder.build().toUriString();
        PagedResultType<IndicatorInstanceBaseType> indicatorInstanceTypes = indicatorSystemRestFacade.retrieveIndicatorsInstances(baseURL, idIndicatorSystem, q, order, limit, offset, fields);
        ResponseEntity<PagedResultType<IndicatorInstanceBaseType>> response = new ResponseEntity<PagedResultType<IndicatorInstanceBaseType>>(indicatorInstanceTypes, HttpStatus.OK);

        return response;
    }

    @RequestMapping(value = "/api/indicators/v1.0/indicatorsSystems/{idIndicatorSystem}/indicatorsInstances/{idIndicatorInstance}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<IndicatorInstanceType> retrieveIndicatorsInstance(final UriComponentsBuilder uriComponentsBuilder,
                                                                            @PathVariable("idIndicatorSystem") final String idIndicatorSystem,
                                                                            @PathVariable("idIndicatorInstance") final String idIndicatorInstance) throws Exception {
        String baseURL = uriComponentsBuilder.build().toUriString();
        IndicatorInstanceType indicatorInstanceType = indicatorSystemRestFacade.retrieveIndicatorInstanceByCode(baseURL, idIndicatorSystem, idIndicatorInstance);
        ResponseEntity<IndicatorInstanceType> response = new ResponseEntity<IndicatorInstanceType>(indicatorInstanceType, HttpStatus.OK);
        return response;
    }

    @RequestMapping(value = "/api/indicators/v1.0/indicatorsSystems/{idIndicatorSystem}/indicatorsInstances/{idIndicatorInstance}/data", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<DataType> retrieveIndicatorsInstanceData(final UriComponentsBuilder uriComponentsBuilder,
                                                                   @PathVariable("idIndicatorSystem") final String idIndicatorSystem,
                                                                   @PathVariable("idIndicatorInstance") final String idIndicatorInstance,
                                                                   @RequestParam(required = false, value = "representation") final String representation,
                                                                   @RequestParam(required = false, value = "granularity") final String granularity) throws Exception {
        String baseURL = uriComponentsBuilder.build().toUriString();
        Map<String, List<String>> selectedRepresentations = RequestUtil.parseParamExpression(representation);
        Map<String, List<String>> selectedGranularities = RequestUtil.parseParamExpression(granularity);
        DataType dataType = indicatorSystemRestFacade.retrieveIndicatorInstanceDataByCode(baseURL, idIndicatorSystem, idIndicatorInstance, selectedRepresentations, selectedGranularities);
        ResponseEntity<DataType> response = new ResponseEntity<DataType>(dataType, HttpStatus.OK);
        return response;
    }

}
