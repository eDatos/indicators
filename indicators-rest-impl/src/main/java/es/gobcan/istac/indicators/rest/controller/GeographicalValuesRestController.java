package es.gobcan.istac.indicators.rest.controller;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.gobcan.istac.indicators.rest.IndicatorsRestConstants;
import es.gobcan.istac.indicators.rest.component.UriLinks;
import es.gobcan.istac.indicators.rest.facadeapi.GeographicalValuesRestFacade;
import es.gobcan.istac.indicators.rest.types.GeographicalValueType;
import es.gobcan.istac.indicators.rest.types.ListResultType;

@Controller("geographicalValuesRestController")
public class GeographicalValuesRestController extends AbstractRestController {

    @Autowired
    private GeographicalValuesRestFacade geographicalValuesRestFacade;

    @Autowired
    private UriLinks                     uriLinks;

    @RequestMapping(value = "/api/indicators/v1.0/geographicalValues", method = RequestMethod.GET)
    @ResponseBody
    // @formatter:off
    public ResponseEntity<ListResultType<GeographicalValueType>> findGeographicalValues(@RequestParam(value = "subjectCode", required = false) String subjectCode,
                                                                                            @RequestParam(value = "systemCode", required = false) String systemCode,
                                                                                            @RequestParam(value = "geographicalGranularityCode", required = true) String geographicalGranularityCode
                                                                                            ) throws MetamacException {
    // @formatter:on

        List<GeographicalValueType> items = null;
        if (subjectCode != null) {
            items = geographicalValuesRestFacade.findGeographicalValuesBySubjectCode(subjectCode, geographicalGranularityCode);
        } else if (systemCode != null) {
            items = geographicalValuesRestFacade.findGeographicalValuesByIndicatorsSystemCode(systemCode, geographicalGranularityCode);
        } else {
            items = geographicalValuesRestFacade.findGeographicalValuesByGranularity(geographicalGranularityCode);
        }

        String selfLink = uriLinks.getGeographicalValuesSelfLink(subjectCode, systemCode, geographicalGranularityCode);
        ListResultType<GeographicalValueType> itemsResultType = new ListResultType<GeographicalValueType>(IndicatorsRestConstants.KIND_GEOGRAPHICAL_VALUES, selfLink, items);
        return new ResponseEntity<ListResultType<GeographicalValueType>>(itemsResultType, HttpStatus.OK);
    }

}
