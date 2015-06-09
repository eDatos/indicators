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
import org.springframework.web.util.UriComponentsBuilder;

import es.gobcan.istac.indicators.rest.RestConstants;
import es.gobcan.istac.indicators.rest.facadeapi.GeographicRestFacade;
import es.gobcan.istac.indicators.rest.types.ListResultType;
import es.gobcan.istac.indicators.rest.types.MetadataGranularityType;
import es.gobcan.istac.indicators.rest.util.UriUtils;

@Controller("geographicGranularitiesRestController")
public class GeographicGranularitiesRestController extends AbstractRestController {

    @Autowired
    private GeographicRestFacade geographicRestFacade = null;

    @RequestMapping(value = "/api/indicators/v1.0/geographicGranularities", method = RequestMethod.GET)
    @ResponseBody
    //@formatter:off
    public ResponseEntity<ListResultType<MetadataGranularityType>> findGeographicGranularities(final UriComponentsBuilder uriComponentsBuilder,
                                                                                                @RequestParam(value = "subjectCode", required = false) String subjectCode,
                                                                                                @RequestParam(value = "systemCode", required = false) String systemCode
                                                                                              ) throws MetamacException {
    //@formatter:on

        List<MetadataGranularityType> items = null;
        if (subjectCode != null) {
            items = geographicRestFacade.findGeographicGranularitiesBySubjectCode(subjectCode);
        } else if (systemCode != null) {
            items = geographicRestFacade.findGeographicGranularitiesByIndicatorsSystemCode(systemCode);
        } else {
            items = geographicRestFacade.findGeographicGranularities();
        }

        String selfLink = UriUtils.getGeographicalGranularitiesSelfLink(uriComponentsBuilder, subjectCode, systemCode);
        ListResultType<MetadataGranularityType> itemsResultType = new ListResultType<MetadataGranularityType>(RestConstants.KIND_GEOGRAPHICAL_GRANULARITIES, selfLink, items);
        return new ResponseEntity<ListResultType<MetadataGranularityType>>(itemsResultType, null, HttpStatus.OK);
    }

}
