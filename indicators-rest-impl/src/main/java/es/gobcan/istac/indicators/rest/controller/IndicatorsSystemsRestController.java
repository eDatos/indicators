package es.gobcan.istac.indicators.rest.controller;

import es.gobcan.istac.indicators.rest.RestConstants;
import es.gobcan.istac.indicators.rest.facadeapi.IndicatorSystemRestFacade;
import es.gobcan.istac.indicators.rest.types.*;
import es.gobcan.istac.indicators.rest.util.HttpHeaderUtil;
import es.gobcan.istac.indicators.rest.util.RequestUtil;
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
            @RequestParam(required = false, value = "limit") final Integer limit, @RequestParam(required = false, value = "offset") final Integer offset) throws Exception {

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
    public ResponseEntity<IndicatorsSystemType> retrieveIndicatorsSystem(final UriComponentsBuilder uriComponentsBuilder, @PathVariable("idIndicatorSystem") final String idIndicatorSystem)
            throws Exception {

        String baseURL = uriComponentsBuilder.build().toUriString();
        IndicatorsSystemType indicatorsSystemBaseType = indicatorSystemRestFacade.retrieveIndicatorsSystem(baseURL, idIndicatorSystem);
        ResponseEntity<IndicatorsSystemType> response = new ResponseEntity<IndicatorsSystemType>(indicatorsSystemBaseType, HttpStatus.OK);
        return response;
    }

    @RequestMapping(value = "/api/indicators/v1.0/indicatorsSystems/{idIndicatorSystem}/indicatorsInstances", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ListResultType<IndicatorInstanceBaseType>> retrieveIndicatorsInstances(final UriComponentsBuilder uriComponentsBuilder,
            @PathVariable("idIndicatorSystem") String idIndicatorSystem, @RequestParam(required = false, value = "q") String q, @RequestParam(required = false, value = "order") String order,
            @RequestParam(required = false, value = "fields") String fields, @RequestParam(required = false, value = "representation") String representation,
            @RequestParam(required = false, value = "granularity") String granularity) throws Exception {
        String baseURL = uriComponentsBuilder.build().toUriString();
        Map<String, List<String>> selectedRepresentations = RequestUtil.parseParamExpression(representation);
        Map<String, List<String>> selectedGranularities = RequestUtil.parseParamExpression(granularity);
        ListResultType<IndicatorInstanceBaseType> indicatorInstanceTypes = indicatorSystemRestFacade.retrieveIndicatorsInstances(baseURL, idIndicatorSystem, q, order, fields, selectedRepresentations,
                selectedGranularities);
        ResponseEntity<ListResultType<IndicatorInstanceBaseType>> response = new ResponseEntity<ListResultType<IndicatorInstanceBaseType>>(indicatorInstanceTypes, HttpStatus.OK);

        return response;
    }

    @RequestMapping(value = "/api/indicators/v1.0/indicatorsSystems/{idIndicatorSystem}/indicatorsInstances/{idIndicatorInstance}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<IndicatorInstanceType> retrieveIndicatorsInstance(final UriComponentsBuilder uriComponentsBuilder, @PathVariable("idIndicatorSystem") final String idIndicatorSystem,
            @PathVariable("idIndicatorInstance") final String idIndicatorInstance) throws Exception {
        String baseURL = uriComponentsBuilder.build().toUriString();
        IndicatorInstanceType indicatorInstanceType = indicatorSystemRestFacade.retrieveIndicatorInstanceByCode(baseURL, idIndicatorSystem, idIndicatorInstance);
        ResponseEntity<IndicatorInstanceType> response = new ResponseEntity<IndicatorInstanceType>(indicatorInstanceType, HttpStatus.OK);
        return response;
    }

    @RequestMapping(value = "/api/indicators/v1.0/indicatorsSystems/{idIndicatorSystem}/indicatorsInstances/{idIndicatorInstance}/data", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<DataType> retrieveIndicatorsInstanceData(final UriComponentsBuilder uriComponentsBuilder, @PathVariable("idIndicatorSystem") String idIndicatorSystem,
            @PathVariable("idIndicatorInstance") String idIndicatorInstance, @RequestParam(required = false, value = "representation") String representation,
            @RequestParam(required = false, value = "granularity") String granularity, @RequestParam(required = false, value = "fields") String fields) throws Exception {
        String baseURL = uriComponentsBuilder.build().toUriString();
        Map<String, List<String>> selectedRepresentations = RequestUtil.parseParamExpression(representation);
        Map<String, List<String>> selectedGranularities = RequestUtil.parseParamExpression(granularity);
        boolean includeObservationsAttributes = fields != null ? !fields.contains("-observationsMetadata") : true;
        DataType dataType = indicatorSystemRestFacade.retrieveIndicatorInstanceDataByCode(baseURL, idIndicatorSystem, idIndicatorInstance, selectedRepresentations, selectedGranularities,
                includeObservationsAttributes);
        ResponseEntity<DataType> response = new ResponseEntity<DataType>(dataType, HttpStatus.OK);
        return response;
    }

}
