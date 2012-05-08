package es.gobcan.istac.indicators.rest.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
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
                                                                             @RequestParam(required=false, value="offset") final Integer offset,
                                                                             @RequestParam(required=false, value="subjectCode") final String subjectCode) throws Exception {
        
        String baseURL = uriComponentsBuilder.build().toUriString();
        RestCriteriaPaginator paginator = new RestCriteriaPaginator(limit, offset);
        PagedResultType<IndicatorBaseType> indicatorBaseTypes = indicatorRestFacade.findIndicators(baseURL, subjectCode, paginator);
        
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
                                                                   @PathVariable("indicatorCode") final String indicatorCode,
                                                                   @RequestParam(required=false, value="representation") final String representation,
                                                                   @RequestParam(required=false, value="granularity") final String granularity) throws Exception {
        String baseURL = uriComponentsBuilder.build().toUriString();
        
        Map<String, List<String>> selectedRepresentations = parseParamExpression(representation);
        Map<String, List<String>> selectedGranularities = parseParamExpression(granularity);
        DataType dataType = indicatorRestFacade.retrieveIndicatorData(baseURL, indicatorCode, selectedRepresentations, selectedGranularities);
        ResponseEntity<DataType> response = new ResponseEntity<DataType>(dataType, HttpStatus.OK);
        return response;
    }
    
    @SuppressWarnings("unchecked")
    private Map<String, List<String>> parseParamExpression(String paramExpression) {
        if (StringUtils.isBlank(paramExpression)) {
            return MapUtils.EMPTY_MAP;
        }

        // dimExpression = MOTIVOS_ESTANCIA[000|001|002]:ISLAS_DESTINO_PRINCIPAL[005|006]
        Pattern patternDimension = Pattern.compile("(\\w+)\\[((\\w\\|?)+)\\]");
        Pattern patternCode = Pattern.compile("(\\w+)\\|?");

        Matcher matcherDimension = patternDimension.matcher(paramExpression);

        Map<String, List<String>> selectedDimension = new HashMap<String, List<String>>();
        while (matcherDimension.find()) {
            String dimIdentifier = matcherDimension.group(1);
            String codes = matcherDimension.group(2);
            Matcher matcherCode = patternCode.matcher(codes);
            while (matcherCode.find()) {
                List<String> codeDimensions = selectedDimension.get(dimIdentifier);
                if (codeDimensions == null) {
                    codeDimensions = new ArrayList<String>();
                    selectedDimension.put(dimIdentifier, codeDimensions);
                }
                String codeIdentifier = matcherCode.group(1);
                codeDimensions.add(codeIdentifier);
            }
        }
        return selectedDimension;
    }
    
}
