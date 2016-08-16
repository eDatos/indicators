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

import es.gobcan.istac.indicators.rest.IndicatorsRestConstants;
import es.gobcan.istac.indicators.rest.component.UriLinks;
import es.gobcan.istac.indicators.rest.facadeapi.IndicatorSystemRestFacade;
import es.gobcan.istac.indicators.rest.types.DataType;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemType;
import es.gobcan.istac.indicators.rest.types.LinkType;
import es.gobcan.istac.indicators.rest.types.PagedResultType;
import es.gobcan.istac.indicators.rest.types.RestCriteriaPaginator;
import es.gobcan.istac.indicators.rest.util.IndicatorInstancesPaginatedResponseUtil;
import es.gobcan.istac.indicators.rest.util.IndicatorsSystemsPaginatedResponseUtil;
import es.gobcan.istac.indicators.rest.util.RequestUtil;

@Controller("indicatorsSystemsRestController")
public class IndicatorsSystemsRestController extends AbstractRestController {

    @Autowired
    private IndicatorSystemRestFacade indicatorSystemRestFacade = null;

    @Autowired
    private UriLinks                  uriLinks;

    @RequestMapping(value = "/api/indicators/v1.0/indicatorsSystems", method = RequestMethod.GET)
    @ResponseBody
    // @formatter:off
    public ResponseEntity<PagedResultType<IndicatorsSystemBaseType>> findIndicatorsSystems(@RequestParam(required = false, value = "limit") final Integer limit,
                                                                                            @RequestParam(required = false, value = "offset") final Integer offset
                                                                                          ) throws MetamacException {
        // @formatter:on

        RestCriteriaPaginator paginator = new RestCriteriaPaginator(limit, offset);
        PagedResultType<IndicatorsSystemBaseType> indicatorsSystemBaseTypes = indicatorSystemRestFacade.findIndicatorsSystems(paginator);

        String indicatorsSystemsLink = uriLinks.getIndicatorsSystemsLink();
        IndicatorsSystemsPaginatedResponseUtil.createPaginationLinks(indicatorsSystemBaseTypes, indicatorsSystemsLink, limit, offset);
        return new ResponseEntity<PagedResultType<IndicatorsSystemBaseType>>(indicatorsSystemBaseTypes, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/indicators/v1.0/indicatorsSystems/{idIndicatorSystem}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<IndicatorsSystemType> retrieveIndicatorsSystem(@PathVariable("idIndicatorSystem") final String idIndicatorSystem) throws MetamacException {
        IndicatorsSystemType indicatorsSystemBaseType = indicatorSystemRestFacade.retrieveIndicatorsSystem(idIndicatorSystem);
        return new ResponseEntity<IndicatorsSystemType>(indicatorsSystemBaseType, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/indicators/v1.0/indicatorsSystems/{idIndicatorSystem}/indicatorsInstances", method = RequestMethod.GET)
    @ResponseBody
    // @formatter:off
    public ResponseEntity<PagedResultType<IndicatorInstanceBaseType>> retrieveIndicatorsInstances(@PathVariable("idIndicatorSystem") String idIndicatorSystem,
                                                                                                    @RequestParam(required = false, value = "q") String q,
                                                                                                    @RequestParam(required = false, value = "order") String order,
                                                                                                    @RequestParam(required = false, value = "limit") final Integer limit,
                                                                                                    @RequestParam(required = false, value = "offset") final Integer offset,
                                                                                                    @RequestParam(required = false, value = "fields") String fields,
                                                                                                    @RequestParam(required = false, value = "representation") String representation,
                                                                                                    @RequestParam(required = false, value = "granularity") String granularity) throws MetamacException {
        // @formatter:on

        Map<String, List<String>> selectedRepresentations = RequestUtil.parseParamExpression(representation);
        Map<String, List<String>> selectedGranularities = RequestUtil.parseParamExpression(granularity);
        PagedResultType<IndicatorInstanceBaseType> indicatorInstanceTypes = indicatorSystemRestFacade.retrievePaginatedIndicatorsInstances(idIndicatorSystem, q, order, limit, offset, fields,
                selectedRepresentations, selectedGranularities);

        String indicatorInstancesLink = uriLinks.getIndicatorInstancesLink(idIndicatorSystem);
        String indicatorSystemLink = uriLinks.getIndicatorSystemLink(idIndicatorSystem);
        IndicatorInstancesPaginatedResponseUtil
                .createPaginationLinks(indicatorInstanceTypes, indicatorInstancesLink, indicatorSystemLink, q, order, limit, offset, fields, representation, granularity);
        return new ResponseEntity<PagedResultType<IndicatorInstanceBaseType>>(indicatorInstanceTypes, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/indicators/v1.0/indicatorsSystems/{idIndicatorSystem}/indicatorsInstances/{idIndicatorInstance}", method = RequestMethod.GET)
    @ResponseBody
    // @formatter:off
    public ResponseEntity<IndicatorInstanceType> retrieveIndicatorsInstance(@PathVariable("idIndicatorSystem") final String idIndicatorSystem,
                                                                            @PathVariable("idIndicatorInstance") final String idIndicatorInstance
                                                                            ) throws MetamacException {
        // @formatter:on

        IndicatorInstanceType indicatorInstanceType = indicatorSystemRestFacade.retrieveIndicatorInstanceByCode(idIndicatorSystem, idIndicatorInstance);
        return new ResponseEntity<IndicatorInstanceType>(indicatorInstanceType, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/indicators/v1.0/indicatorsSystems/{idIndicatorSystem}/indicatorsInstances/{idIndicatorInstance}/data", method = RequestMethod.GET)
    @ResponseBody
    // @formatter:off
    public ResponseEntity<DataType> retrieveIndicatorsInstanceData(@PathVariable("idIndicatorSystem") String idIndicatorSystem,
                                                                    @PathVariable("idIndicatorInstance") String idIndicatorInstance,
                                                                    @RequestParam(required = false, value = "representation") String representation,
                                                                    @RequestParam(required = false, value = "granularity") String granularity,
                                                                    @RequestParam(required = false, value = "fields") String fields) throws MetamacException {

        // @formatter:on

        Map<String, List<String>> selectedRepresentations = RequestUtil.parseParamExpression(representation);
        Map<String, List<String>> selectedGranularities = RequestUtil.parseParamExpression(granularity);
        boolean includeObservationsAttributes = fields != null ? !fields.contains("-observationsMetadata") : true;
        DataType dataType = indicatorSystemRestFacade.retrieveIndicatorInstanceDataByCode(idIndicatorSystem, idIndicatorInstance, selectedRepresentations, selectedGranularities,
                includeObservationsAttributes);

        String selfLink = uriLinks.getIndicatorInstanceDataSelfLink(idIndicatorSystem, idIndicatorInstance, fields, representation, granularity);
        LinkType parentLink = new LinkType(IndicatorsRestConstants.KIND_INDICATOR_INSTANCE, uriLinks.getIndicatorInstanceLink(idIndicatorSystem, idIndicatorInstance));
        dataType.addHeader(selfLink, parentLink, IndicatorsRestConstants.KIND_INDICATOR_INSTANCE_DATA);

        return new ResponseEntity<DataType>(dataType, HttpStatus.OK);
    }

}
