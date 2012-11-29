package es.gobcan.istac.indicators.rest.facadeimpl;

import es.gobcan.istac.indicators.core.domain.Subject;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsService;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsSystemsService;
import es.gobcan.istac.indicators.rest.RestConstants;
import es.gobcan.istac.indicators.rest.facadeapi.SubjectsRestFacade;
import es.gobcan.istac.indicators.rest.mapper.Do2TypeMapper;
import es.gobcan.istac.indicators.rest.types.SubjectBaseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("themesRestFacade")
public class SubjectsRestFacadeImpl implements SubjectsRestFacade {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IndicatorsService indicatorsService = null;

    @Autowired
    private IndicatorsSystemsService indicatorsSystemsService = null;

    @Autowired
    private Do2TypeMapper mapper = null;

    @Override
    public List<SubjectBaseType> retrieveSubjects(final String baseUrl) throws Exception {
        List<Subject> subjects = indicatorsService.retrieveSubjects(RestConstants.SERVICE_CONTEXT);
        List<SubjectBaseType> subjectTypes = mapper.subjectDoToBaseType(subjects, baseUrl);
        return subjectTypes;
    }

}
