package es.gobcan.istac.indicators.rest.controller;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.gobcan.istac.indicators.rest.IndicatorsRestConstants;
import es.gobcan.istac.indicators.rest.component.UriLinks;
import es.gobcan.istac.indicators.rest.facadeapi.TimeRestFacade;
import es.gobcan.istac.indicators.rest.types.ListResultType;
import es.gobcan.istac.indicators.rest.types.MetadataGranularityType;

@Controller("timeGranularitiesRestController")
public class TimeGranularitiesRestController extends AbstractRestController {

    @Autowired
    private TimeRestFacade timeRestFacade = null;

    @Autowired
    private UriLinks       uriLinks;

    @RequestMapping(value = "/api/indicators/v1.0/timeGranularities", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ListResultType<MetadataGranularityType>> findTimeGranularities() throws MetamacException {
        List<MetadataGranularityType> items = timeRestFacade.findTimeGranularities();

        String selfLink = uriLinks.getTimeGranularitiesLink();
        ListResultType<MetadataGranularityType> itemsResultType = new ListResultType<MetadataGranularityType>(IndicatorsRestConstants.KIND_TIME_GRANULARITIES, selfLink, items);
        return new ResponseEntity<ListResultType<MetadataGranularityType>>(itemsResultType, null, HttpStatus.OK);
    }

}
