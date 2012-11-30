package es.gobcan.istac.indicators.rest.controller;


import es.gobcan.istac.indicators.rest.facadeapi.GeographicalValuesRestFacade;
import es.gobcan.istac.indicators.rest.types.GeographicalValueType;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller("geographicalValuesRestController")
@RequestMapping("/api/indicators/v1.0/geographicalValues")
public class GeographicalValuesRestController extends AbstractRestController {

    @Autowired
    private GeographicalValuesRestFacade geographicalValuesRestFacade;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<GeographicalValueType>> findGeographicalValues(@RequestParam(value = "subjectCode", required = false) String subjectCode,
                                                                              @RequestParam(value = "systemCode", required = false) String systemCode,
                                                                              @RequestParam(value = "geographicalGranularityCode", required = true) String geographicalGranularityCode) throws MetamacException {

        List<GeographicalValueType> items = null;
        if (subjectCode != null) {
            items = geographicalValuesRestFacade.findGeographicalValuesBySubjectCode(subjectCode, geographicalGranularityCode);
        } else if (systemCode != null) {
            items = geographicalValuesRestFacade.findGeographicalValuesByIndicatorsSystemCode(systemCode, geographicalGranularityCode);
        } else {
            items = new ArrayList<GeographicalValueType>();
        }
        ResponseEntity<List<GeographicalValueType>> response = new ResponseEntity<List<GeographicalValueType>>(items, HttpStatus.OK);
        return response;
    }


}