package es.gobcan.istac.indicators.rest.controller;

import java.util.List;

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
public class GeographicGranularitiesRestController extends AbstractRestController {

    @Autowired
    private final GeographicRestFacade geographicRestFacade = null;

    @RequestMapping(value = "/api/indicators/v1.0/geographicGranularities", method = RequestMethod.GET)
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

}
