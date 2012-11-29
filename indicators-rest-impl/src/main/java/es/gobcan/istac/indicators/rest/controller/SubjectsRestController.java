package es.gobcan.istac.indicators.rest.controller;

import es.gobcan.istac.indicators.rest.facadeapi.SubjectsRestFacade;
import es.gobcan.istac.indicators.rest.types.SubjectBaseType;
import org.fornax.cartridges.sculptor.framework.errorhandling.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Controller("subjectsRestController")
@RequestMapping("/api/indicators/v1.0/subjects/*")
public class SubjectsRestController extends AbstractRestController {

    @Autowired
    private SubjectsRestFacade subjectsRestFacade = null;
    
    /**
     * @throws Exception
     * @throws ApplicationException
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<SubjectBaseType>> findSubjects(final UriComponentsBuilder uriComponentsBuilder) throws Exception {
        String baseURL = uriComponentsBuilder.build().toUriString();
        List<SubjectBaseType> subjectTypes = subjectsRestFacade.retrieveSubjects(baseURL);
        ResponseEntity<List<SubjectBaseType>> response = new ResponseEntity<List<SubjectBaseType>>(subjectTypes, null, HttpStatus.OK);
        return response;
    }
    
}
