package es.gobcan.istac.indicators.rest.controller;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public ResponseEntity<List<MetadataGranularityType>> findGeographicGranularities(@RequestParam(value = "subjectCode", required = false) String subjectCode,
                                                                                     @RequestParam(value = "systemCode", required = false) String systemCode) throws Exception {
        
        List<MetadataGranularityType> items = null;
        if (subjectCode != null) {
            items = geographicRestFacade.findGeographicGranularitiesBySubjectCode(subjectCode);
        } else if (systemCode != null) {
            items = geographicRestFacade.findGeographicGranularitiesByIndicatorsSystemCode(systemCode);
        } else {
            items = geographicRestFacade.findGeographicGranularities();
        }
        
        ResponseEntity<List<MetadataGranularityType>> response = new ResponseEntity<List<MetadataGranularityType>>(items, null, HttpStatus.OK);
        return response;
    }
//    
//    /**
//     * @throws Exception
//     * @throws ApplicationException
//     */
//    @RequestMapping(value = "/{granularityCode}", method = RequestMethod.GET)
//    @ResponseBody
//    public ResponseEntity<MetadataGranularityType> findGeographicGranularities(final UriComponentsBuilder uriComponentsBuilder,
//                                                                                     final @PathVariable("granularityCode") String granularityCode) throws Exception {
//        
//        String baseURL = uriComponentsBuilder.build().toUriString();
//        MetadataGranularityType metadataGranularityType = geographicRestFacade.retrieveGeographicGranilarity(baseURL, granularityCode);
//        
//        ResponseEntity<MetadataGranularityType> response = new ResponseEntity<MetadataGranularityType>(metadataGranularityType, null, HttpStatus.OK);
//        return response;
//    }
    
}
