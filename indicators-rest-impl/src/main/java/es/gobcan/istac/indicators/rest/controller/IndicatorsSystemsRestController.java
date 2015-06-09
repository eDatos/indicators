package es.gobcan.istac.indicators.rest.controller;

import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
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
import es.gobcan.istac.indicators.rest.types.DataType;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemType;
import es.gobcan.istac.indicators.rest.types.PagedResultType;
import es.gobcan.istac.indicators.rest.types.RestCriteriaPaginator;
import es.gobcan.istac.indicators.rest.util.IndicatorInstancesPaginatedResponseUtil;
import es.gobcan.istac.indicators.rest.util.IndicatorsSystemsPaginatedResponseUtil;
import es.gobcan.istac.indicators.rest.util.RequestUtil;

@Controller("indicatorsSystemsRestController")
public class IndicatorsSystemsRestController extends AbstractRestController {

    @Autowired
    private IndicatorSystemRestFacade indicatorSystemRestFacade = null;

    @RequestMapping(value = "/api/indicators/v1.0/indicatorsSystems", method = RequestMethod.GET)
    @ResponseBody
    // @formatter:off
    public ResponseEntity<PagedResultType<IndicatorsSystemBaseType>> findIndicatorsSystems(final UriComponentsBuilder uriComponentsBuilder,
                                                                                            @RequestParam(required = false, value = "limit") final Integer limit,
                                                                                            @RequestParam(required = false, value = "offset") final Integer offset) throws MetamacException {
        // @formatter:on

        String baseURL = uriComponentsBuilder.build().toUriString();
        RestCriteriaPaginator paginator = new RestCriteriaPaginator(limit, offset);
        PagedResultType<IndicatorsSystemBaseType> indicatorsSystemBaseTypes = indicatorSystemRestFacade.findIndicatorsSystems(baseURL, paginator);

        baseURL = uriComponentsBuilder.pathSegment(RestConstants.API_INDICATORS_VERSION, RestConstants.API_INDICATORS_INDICATORS_SYSTEMS).build().toUriString();
        IndicatorsSystemsPaginatedResponseUtil.createPaginationLinks(indicatorsSystemBaseTypes, baseURL, limit, offset);
        return new ResponseEntity<PagedResultType<IndicatorsSystemBaseType>>(indicatorsSystemBaseTypes, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/indicators/v1.0/indicatorsSystems/{idIndicatorSystem}", method = RequestMethod.GET)
    @ResponseBody
    // @formatter:off
    public ResponseEntity<IndicatorsSystemType> retrieveIndicatorsSystem(final UriComponentsBuilder uriComponentsBuilder,
                                                                            @PathVariable("idIndicatorSystem") final String idIndicatorSystem)
            throws Exception {
        // @formatter:on

        String baseURL = uriComponentsBuilder.build().toUriString();
        IndicatorsSystemType indicatorsSystemBaseType = indicatorSystemRestFacade.retrieveIndicatorsSystem(baseURL, idIndicatorSystem);
        return new ResponseEntity<IndicatorsSystemType>(indicatorsSystemBaseType, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/indicators/v1.0/indicatorsSystems/{idIndicatorSystem}/indicatorsInstances", method = RequestMethod.GET)
    @ResponseBody
    // @formatter:off
    public ResponseEntity<PagedResultType<IndicatorInstanceBaseType>> retrieveIndicatorsInstances(final UriComponentsBuilder uriComponentsBuilder,
                                                                                                    @PathVariable("idIndicatorSystem") String idIndicatorSystem,
                                                                                                    @RequestParam(required = false, value = "q") String q,
                                                                                                    @RequestParam(required = false, value = "order") String order,
                                                                                                    @RequestParam(required = false, value = "limit") final Integer limit,
                                                                                                    @RequestParam(required = false, value = "offset") final Integer offset,
                                                                                                    @RequestParam(required = false, value = "fields") String fields,
                                                                                                    @RequestParam(required = false, value = "representation") String representation,
                                                                                                    @RequestParam(required = false, value = "granularity") String granularity) throws Exception {
        // @formatter:on

        String baseURL = uriComponentsBuilder.build().toUriString();

        Map<String, List<String>> selectedRepresentations = RequestUtil.parseParamExpression(representation);
        Map<String, List<String>> selectedGranularities = RequestUtil.parseParamExpression(granularity);
        PagedResultType<IndicatorInstanceBaseType> indicatorInstanceTypes = indicatorSystemRestFacade.retrievePaginatedIndicatorsInstances(baseURL, idIndicatorSystem, q, order, limit, offset, fields,
                selectedRepresentations, selectedGranularities);

        baseURL = uriComponentsBuilder
                .pathSegment(RestConstants.API_INDICATORS_VERSION, RestConstants.API_INDICATORS_INDICATORS_SYSTEMS, idIndicatorSystem, RestConstants.API_INDICATORS_INDICATORS_INSTANCES).build()
                .toUriString();
        IndicatorInstancesPaginatedResponseUtil.createPaginationLinks(indicatorInstanceTypes, baseURL, q, order, limit, offset, fields, representation, granularity);
        return new ResponseEntity<PagedResultType<IndicatorInstanceBaseType>>(indicatorInstanceTypes, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/indicators/v1.0/indicatorsSystems/{idIndicatorSystem}/indicatorsInstances/{idIndicatorInstance}", method = RequestMethod.GET)
    @ResponseBody
    // @formatter:off
    public ResponseEntity<IndicatorInstanceType> retrieveIndicatorsInstance(final UriComponentsBuilder uriComponentsBuilder,
                                                                                @PathVariable("idIndicatorSystem") final String idIndicatorSystem,
                                                                                @PathVariable("idIndicatorInstance") final String idIndicatorInstance) throws Exception {
        // @formatter:on

        String baseURL = uriComponentsBuilder.build().toUriString();
        IndicatorInstanceType indicatorInstanceType = indicatorSystemRestFacade.retrieveIndicatorInstanceByCode(baseURL, idIndicatorSystem, idIndicatorInstance);
        return new ResponseEntity<IndicatorInstanceType>(indicatorInstanceType, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/indicators/v1.0/indicatorsSystems/{idIndicatorSystem}/indicatorsInstances/{idIndicatorInstance}/data", method = RequestMethod.GET)
    @ResponseBody
    // @formatter:off
    public ResponseEntity<DataType> retrieveIndicatorsInstanceData(final UriComponentsBuilder uriComponentsBuilder,
                                                                    @PathVariable("idIndicatorSystem") String idIndicatorSystem,
                                                                    @PathVariable("idIndicatorInstance") String idIndicatorInstance,
                                                                    @RequestParam(required = false, value = "representation") String representation,
                                                                    @RequestParam(required = false, value = "granularity") String granularity,
                                                                    @RequestParam(required = false, value = "fields") String fields) throws Exception {

        // @formatter:on

        String baseURL = uriComponentsBuilder.build().toUriString();
        Map<String, List<String>> selectedRepresentations = RequestUtil.parseParamExpression(representation);
        Map<String, List<String>> selectedGranularities = RequestUtil.parseParamExpression(granularity);
        boolean includeObservationsAttributes = fields != null ? !fields.contains("-observationsMetadata") : true;
        DataType dataType = indicatorSystemRestFacade.retrieveIndicatorInstanceDataByCode(baseURL, idIndicatorSystem, idIndicatorInstance, selectedRepresentations, selectedGranularities,
                includeObservationsAttributes);

        baseURL = uriComponentsBuilder
                .pathSegment(RestConstants.API_INDICATORS_VERSION, RestConstants.API_INDICATORS_INDICATORS_SYSTEMS, idIndicatorSystem, RestConstants.API_INDICATORS_INDICATORS_INSTANCES,
                        idIndicatorInstance, RestConstants.API_INDICATORS_INDICATORS_INSTANCES_DATA).build().toUriString();

        dataType.addHeader(baseURL, fields, representation, granularity, RestConstants.KIND_INDICATOR_INSTANCE_DATA);
        return new ResponseEntity<DataType>(dataType, HttpStatus.OK);
    }

}
