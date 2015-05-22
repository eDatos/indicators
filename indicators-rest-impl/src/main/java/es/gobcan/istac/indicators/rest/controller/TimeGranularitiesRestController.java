package es.gobcan.istac.indicators.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.gobcan.istac.indicators.rest.facadeapi.TimeRestFacade;
import es.gobcan.istac.indicators.rest.types.MetadataGranularityType;

@Controller("timeGranularitiesRestController")
public class TimeGranularitiesRestController extends AbstractRestController {

    @Autowired
    private TimeRestFacade timeRestFacade = null;

    @RequestMapping(value = "/api/indicators/v1.0/timeGranularities", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<MetadataGranularityType>> findTimeGranularities() throws Exception {
        List<MetadataGranularityType> items = timeRestFacade.findTimeGranularities();
        return new ResponseEntity<List<MetadataGranularityType>>(items, null, HttpStatus.OK);
    }

}
