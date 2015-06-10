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
import es.gobcan.istac.indicators.rest.facadeapi.SubjectsRestFacade;
import es.gobcan.istac.indicators.rest.types.ListResultType;
import es.gobcan.istac.indicators.rest.types.SubjectBaseType;

@Controller("subjectsRestController")
public class SubjectsRestController extends AbstractRestController {

    @Autowired
    private SubjectsRestFacade subjectsRestFacade = null;

    @Autowired
    private UriLinks           uriLinks;

    @RequestMapping(value = "/api/indicators/v1.0/subjects", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ListResultType<SubjectBaseType>> findSubjects() throws MetamacException {
        List<SubjectBaseType> subjectTypes = subjectsRestFacade.retrieveSubjects();

        String selfLink = uriLinks.getSubjectsLink();
        ListResultType<SubjectBaseType> itemsResultType = new ListResultType<SubjectBaseType>(IndicatorsRestConstants.KIND_SUBJECTS, selfLink, subjectTypes);
        return new ResponseEntity<ListResultType<SubjectBaseType>>(itemsResultType, null, HttpStatus.OK);
    }
}
