package es.gobcan.istac.indicators.rest.controller;

import es.gobcan.istac.indicators.rest.facadeapi.IndicatorRestFacade;
import es.gobcan.istac.indicators.rest.types.DataType;
import es.gobcan.istac.indicators.rest.types.IndicatorBaseType;
import es.gobcan.istac.indicators.rest.types.PagedResultType;
import es.gobcan.istac.indicators.rest.types.RestCriteriaPaginator;
import es.gobcan.istac.indicators.rest.util.RequestUtil;
import org.fornax.cartridges.sculptor.framework.errorhandling.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Controller("indicatorsRestController")
@RequestMapping("/api/indicators/v1.0/indicators/*")
public class IndicatorsRestController extends AbstractRestController {

    @Autowired
    private IndicatorRestFacade indicatorRestFacade = null;

    /**
     * @throws Exception
     * @throws ApplicationException
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<PagedResultType<IndicatorBaseType>> findIndicators(final UriComponentsBuilder uriComponentsBuilder,
                                                                             @RequestParam(required = false, value = "q") final String q,
                                                                             @RequestParam(required = false, value = "order") final String order,
                                                                             @RequestParam(required = false, value = "limit") final Integer limit,
                                                                             @RequestParam(required = false, value = "offset") final Integer offset,
                                                                             @RequestParam(required = false, value = "fields") final String fields
                                                                             ) throws Exception {

        String baseURL = uriComponentsBuilder.build().toUriString();

        RestCriteriaPaginator paginator = new RestCriteriaPaginator(limit, offset);
        PagedResultType<IndicatorBaseType> indicatorsBaseType = indicatorRestFacade.findIndicators(baseURL, q, order, paginator, fields);
        ResponseEntity<PagedResultType<IndicatorBaseType>> response = new ResponseEntity<PagedResultType<IndicatorBaseType>>(indicatorsBaseType, HttpStatus.OK);

        return response;
    }

    /**
     * @throws Exception
     * @throws ApplicationException
     */
    @RequestMapping(value = "/{indicatorCode}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<IndicatorBaseType> retrieveIndicator(final UriComponentsBuilder uriComponentsBuilder,
                                                               @PathVariable("indicatorCode") final String indicatorCode) throws Exception {
        String baseURL = uriComponentsBuilder.build().toUriString();
        IndicatorBaseType indicatorBaseType = indicatorRestFacade.retrieveIndicator(baseURL, indicatorCode);

        ResponseEntity<IndicatorBaseType> response = new ResponseEntity<IndicatorBaseType>(indicatorBaseType, null, HttpStatus.OK);
        return response;
    }

    /**
     * @throws Exception
     * @throws ApplicationException
     */
    @RequestMapping(value = "/{indicatorCode}/data", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<DataType> retrieveIndicatorData(final UriComponentsBuilder uriComponentsBuilder,
                                                          @PathVariable("indicatorCode") final String indicatorCode,
                                                          @RequestParam(required = false, value = "representation") final String representation,
                                                          @RequestParam(required = false, value = "granularity") final String granularity) throws Exception {
        String baseURL = uriComponentsBuilder.build().toUriString();

        Map<String, List<String>> selectedRepresentations = RequestUtil.parseParamExpression(representation);
        Map<String, List<String>> selectedGranularities = RequestUtil.parseParamExpression(granularity);
        DataType dataType = indicatorRestFacade.retrieveIndicatorData(baseURL, indicatorCode, selectedRepresentations, selectedGranularities);
        ResponseEntity<DataType> response = new ResponseEntity<DataType>(dataType, HttpStatus.OK);
        return response;
    }


}
