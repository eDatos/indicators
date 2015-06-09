package es.gobcan.istac.indicators.rest.controller;

import java.util.List;
import java.util.Map;

import org.fornax.cartridges.sculptor.framework.errorhandling.ApplicationException;
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
import es.gobcan.istac.indicators.rest.facadeapi.IndicatorRestFacade;
import es.gobcan.istac.indicators.rest.types.DataType;
import es.gobcan.istac.indicators.rest.types.IndicatorBaseType;
import es.gobcan.istac.indicators.rest.types.PagedResultType;
import es.gobcan.istac.indicators.rest.types.RestCriteriaPaginator;
import es.gobcan.istac.indicators.rest.util.IndicatorsPaginatedResponseUtil;
import es.gobcan.istac.indicators.rest.util.RequestUtil;

@Controller("indicatorsRestController")
public class IndicatorsRestController extends AbstractRestController {

    @Autowired
    private IndicatorRestFacade indicatorRestFacade = null;

    /**
     * @throws Exception
     * @throws ApplicationException
     */
    @RequestMapping(value = "/api/indicators/v1.0/indicators", method = RequestMethod.GET)
    @ResponseBody
    // @formatter:off
    public ResponseEntity<PagedResultType<IndicatorBaseType>> findIndicators(final UriComponentsBuilder uriComponentsBuilder,
                                                                             @RequestParam(required = false, value = "q") final String q,
                                                                             @RequestParam(required = false, value = "order") final String order,
                                                                             @RequestParam(required = false, value = "limit") final Integer limit,
                                                                             @RequestParam(required = false, value = "offset") final Integer offset,
                                                                             @RequestParam(required = false, value = "fields") final String fields,
                                                                             @RequestParam(required = false, value = "representation") String representation
                                                                             ) throws Exception {
        // @formatter:on

        String baseURL = uriComponentsBuilder.build().toUriString();

        RestCriteriaPaginator paginator = new RestCriteriaPaginator(limit, offset);
        Map<String, List<String>> selectedRepresentations = RequestUtil.parseParamExpression(representation);
        PagedResultType<IndicatorBaseType> indicatorsBaseType = indicatorRestFacade.findIndicators(baseURL, q, order, paginator, fields, selectedRepresentations);

        baseURL = uriComponentsBuilder.path(RestConstants.API_INDICATORS_BASE).path(RestConstants.API_SLASH).path(RestConstants.API_INDICATORS_INDICATORS).build().toUriString();
        IndicatorsPaginatedResponseUtil.createPaginationLinks(indicatorsBaseType, baseURL, q, order, limit, offset, fields, representation);

        return new ResponseEntity<PagedResultType<IndicatorBaseType>>(indicatorsBaseType, HttpStatus.OK);
    }

    /**
     * @throws Exception
     * @throws ApplicationException
     */
    @RequestMapping(value = "/api/indicators/v1.0/indicators/{indicatorCode}", method = RequestMethod.GET)
    @ResponseBody
    // @formatter:off
    public ResponseEntity<IndicatorBaseType> retrieveIndicator(final UriComponentsBuilder uriComponentsBuilder,
                                                                @PathVariable("indicatorCode") final String indicatorCode) throws Exception {
        // @formatter:on
        String baseURL = uriComponentsBuilder.build().toUriString();
        IndicatorBaseType indicatorBaseType = indicatorRestFacade.retrieveIndicator(baseURL, indicatorCode);

        return new ResponseEntity<IndicatorBaseType>(indicatorBaseType, null, HttpStatus.OK);
    }

    /**
     * @throws Exception
     * @throws ApplicationException
     */
    @RequestMapping(value = "/api/indicators/v1.0/indicators/{indicatorCode}/data", method = RequestMethod.GET)
    @ResponseBody
    // @formatter:off
    public ResponseEntity<DataType> retrieveIndicatorData(final UriComponentsBuilder uriComponentsBuilder,
                                                          @PathVariable("indicatorCode") final String indicatorCode,
                                                          @RequestParam(required = false, value = "representation") final String representation,
                                                          @RequestParam(required = false, value = "granularity") final String granularity,
                                                          @RequestParam(required = false, value = "fields") final String fields) throws MetamacException {
        // @formatter:on

        String baseURL = uriComponentsBuilder.build().toUriString();

        Map<String, List<String>> selectedRepresentations = RequestUtil.parseParamExpression(representation);
        Map<String, List<String>> selectedGranularities = RequestUtil.parseParamExpression(granularity);

        boolean includeObservationMetadata = fields != null ? !fields.contains("-observationsMetadata") : true;
        DataType dataType = indicatorRestFacade.retrieveIndicatorData(baseURL, indicatorCode, selectedRepresentations, selectedGranularities, includeObservationMetadata);

        baseURL = uriComponentsBuilder.pathSegment(RestConstants.API_INDICATORS_BASE, RestConstants.API_INDICATORS_INDICATORS, indicatorCode, RestConstants.API_INDICATORS_INDICATORS_DATA).build()
                .toUriString();

        dataType.addHeader(baseURL, fields, representation, granularity, RestConstants.KIND_INDICATOR_DATA);

        return new ResponseEntity<DataType>(dataType, HttpStatus.OK);
    }

}
