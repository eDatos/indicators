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
import es.gobcan.istac.indicators.rest.facadeapi.IndicatorRestFacade;
import es.gobcan.istac.indicators.rest.types.DataType;
import es.gobcan.istac.indicators.rest.types.IndicatorBaseType;
import es.gobcan.istac.indicators.rest.types.PagedResultType;
import es.gobcan.istac.indicators.rest.types.RestCriteriaPaginator;
import es.gobcan.istac.indicators.rest.util.HttpHeaderUtil;

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
                                                                             @RequestParam(required=false, value="limit") final Integer limit,
                                                                             @RequestParam(required=false, value="offset") final Integer offset) throws Exception {
        
        String baseURL = uriComponentsBuilder.build().toUriString();
        RestCriteriaPaginator paginator = new RestCriteriaPaginator(limit, offset);
        PagedResultType<IndicatorBaseType> indicatorBaseTypes = indicatorRestFacade.findIndicators(baseURL, paginator);
        
        baseURL = uriComponentsBuilder.path(RestConstants.API_INDICATORS_BASE).path(RestConstants.API_SLASH).path(RestConstants.API_INDICATORS_INDICATORS_SYSTEMS).build().toUriString();
        HttpHeaders headers = HttpHeaderUtil.createPagedHeaders(baseURL, indicatorBaseTypes);
        ResponseEntity<PagedResultType<IndicatorBaseType>> response = new ResponseEntity<PagedResultType<IndicatorBaseType>>(indicatorBaseTypes, headers, HttpStatus.OK);
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
    public ResponseEntity<DataType> retrieveIndicatorsInstanceData(final UriComponentsBuilder uriComponentsBuilder,
                                                                    @PathVariable("indicatorCode") final String indicatorCode) throws Exception {
        String baseURL = uriComponentsBuilder.build().toUriString();
        DataType dataType = indicatorRestFacade.retrieveIndicatorData(baseURL, indicatorCode);
        ResponseEntity<DataType> response = new ResponseEntity<DataType>(dataType, HttpStatus.OK);
        return response;
    }
    
}
