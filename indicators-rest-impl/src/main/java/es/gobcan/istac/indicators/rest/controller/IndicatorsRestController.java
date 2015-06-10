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
import es.gobcan.istac.indicators.rest.facadeapi.IndicatorRestFacade;
import es.gobcan.istac.indicators.rest.types.DataType;
import es.gobcan.istac.indicators.rest.types.IndicatorBaseType;
import es.gobcan.istac.indicators.rest.types.LinkType;
import es.gobcan.istac.indicators.rest.types.PagedResultType;
import es.gobcan.istac.indicators.rest.types.RestCriteriaPaginator;
import es.gobcan.istac.indicators.rest.util.IndicatorsPaginatedResponseUtil;
import es.gobcan.istac.indicators.rest.util.RequestUtil;

@Controller("indicatorsRestController")
public class IndicatorsRestController extends AbstractRestController {

    @Autowired
    private IndicatorRestFacade indicatorRestFacade = null;

    @Autowired
    private UriLinks            uriLinks;

    @RequestMapping(value = "/api/indicators/v1.0/indicators", method = RequestMethod.GET)
    @ResponseBody
    // @formatter:off
    public ResponseEntity<PagedResultType<IndicatorBaseType>> findIndicators(@RequestParam(required = false, value = "q") final String q,
                                                                             @RequestParam(required = false, value = "order") final String order,
                                                                             @RequestParam(required = false, value = "limit") final Integer limit,
                                                                             @RequestParam(required = false, value = "offset") final Integer offset,
                                                                             @RequestParam(required = false, value = "fields") final String fields,
                                                                             @RequestParam(required = false, value = "representation") String representation
                                                                             ) throws MetamacException {
        // @formatter:on

        RestCriteriaPaginator paginator = new RestCriteriaPaginator(limit, offset);
        Map<String, List<String>> selectedRepresentations = RequestUtil.parseParamExpression(representation);
        PagedResultType<IndicatorBaseType> indicatorsBaseType = indicatorRestFacade.findIndicators(q, order, paginator, fields, selectedRepresentations);

        String indicatorsLink = uriLinks.getIndicatorsLink();
        IndicatorsPaginatedResponseUtil.createPaginationLinks(indicatorsBaseType, indicatorsLink, q, order, limit, offset, fields, representation);

        return new ResponseEntity<PagedResultType<IndicatorBaseType>>(indicatorsBaseType, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/indicators/v1.0/indicators/{indicatorCode}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<IndicatorBaseType> retrieveIndicator(@PathVariable("indicatorCode") final String indicatorCode) throws MetamacException {
        IndicatorBaseType indicatorBaseType = indicatorRestFacade.retrieveIndicator(indicatorCode);
        return new ResponseEntity<IndicatorBaseType>(indicatorBaseType, null, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/indicators/v1.0/indicators/{indicatorCode}/data", method = RequestMethod.GET)
    @ResponseBody
    // @formatter:off
    public ResponseEntity<DataType> retrieveIndicatorData(@PathVariable("indicatorCode") final String indicatorCode,
                                                          @RequestParam(required = false, value = "representation") final String representation,
                                                          @RequestParam(required = false, value = "granularity") final String granularity,
                                                          @RequestParam(required = false, value = "fields") final String fields) throws MetamacException {
        // @formatter:on

        Map<String, List<String>> selectedRepresentations = RequestUtil.parseParamExpression(representation);
        Map<String, List<String>> selectedGranularities = RequestUtil.parseParamExpression(granularity);

        boolean includeObservationMetadata = fields != null ? !fields.contains("-observationsMetadata") : true;
        DataType dataType = indicatorRestFacade.retrieveIndicatorData(indicatorCode, selectedRepresentations, selectedGranularities, includeObservationMetadata);

        String selfLink = uriLinks.getIndicatorDataSelfLink(indicatorCode, fields, representation, granularity);
        LinkType parentLink = new LinkType(IndicatorsRestConstants.KIND_INDICATOR, uriLinks.getIndicatorLink(indicatorCode));

        dataType.addHeader(selfLink, parentLink, IndicatorsRestConstants.KIND_INDICATOR_DATA);
        return new ResponseEntity<DataType>(dataType, HttpStatus.OK);
    }
}
