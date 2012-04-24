package es.gobcan.istac.indicators.rest.controller;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import es.gobcan.istac.indicators.rest.facadeapi.GeographicRestFacade;
import es.gobcan.istac.indicators.rest.types.MetadataGranularityType;

@Controller("geographicGranularitiesRestController")
@RequestMapping("/api/indicators/v1.0/geographicGranularities/*")
public class GeographicGranularitiesRestController extends AbstractRestController {

    @Autowired
    private GeographicRestFacade geographicRestFacade = null;
    
    /**
     * @throws Exception
     * @throws ApplicationException
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<MetadataGranularityType>> findGeographicGranularities(final UriComponentsBuilder uriComponentsBuilder) throws Exception {
        
        String baseURL = uriComponentsBuilder.build().toUriString();
        List<MetadataGranularityType> metadataGranularityTypes = geographicRestFacade.findGeographicGranilarities(baseURL);
        
        ResponseEntity<List<MetadataGranularityType>> response = new ResponseEntity<List<MetadataGranularityType>>(metadataGranularityTypes, null, HttpStatus.OK);
        return response;
    }
    
    /**
     * @throws Exception
     * @throws ApplicationException
     */
    @RequestMapping(value = "/{granularityCode}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<MetadataGranularityType> findGeographicGranularities(final UriComponentsBuilder uriComponentsBuilder,
                                                                                     final @PathVariable("granularityCode") String granularityCode) throws Exception {
        
        String baseURL = uriComponentsBuilder.build().toUriString();
        MetadataGranularityType metadataGranularityType = geographicRestFacade.retrieveGeographicGranilarity(baseURL, granularityCode);
        
        ResponseEntity<MetadataGranularityType> response = new ResponseEntity<MetadataGranularityType>(metadataGranularityType, null, HttpStatus.OK);
        return response;
    }
    
}
