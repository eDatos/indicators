package es.gobcan.istac.indicators.rest.controller;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import es.gobcan.istac.indicators.rest.facadeapi.ThemesRestFacade;
import es.gobcan.istac.indicators.rest.types.ThemeType;

@Controller("themesRestController")
@RequestMapping("/api/indicators/v1.0/themes/*")
public class ThemesRestController extends AbstractRestController {

    @Autowired
    private ThemesRestFacade themesRestFacade = null;
    
    /**
     * @throws Exception
     * @throws ApplicationException
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<ThemeType>> findGeographicGranularities(final UriComponentsBuilder uriComponentsBuilder) throws Exception {
        
        String baseURL = uriComponentsBuilder.build().toUriString();
        List<ThemeType> themeTypes = themesRestFacade.retrieveThemes(baseURL);
        
        ResponseEntity<List<ThemeType>> response = new ResponseEntity<List<ThemeType>>(themeTypes, null, HttpStatus.OK);
        return response;
    }
    
}
