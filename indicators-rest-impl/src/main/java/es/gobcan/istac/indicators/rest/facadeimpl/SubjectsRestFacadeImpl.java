package es.gobcan.istac.indicators.rest.facadeimpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.repositoryimpl.finders.SubjectIndicatorResult;
import es.gobcan.istac.indicators.rest.facadeapi.SubjectsRestFacade;
import es.gobcan.istac.indicators.rest.mapper.Do2TypeMapper;
import es.gobcan.istac.indicators.rest.serviceapi.IndicatorsApiService;
import es.gobcan.istac.indicators.rest.types.SubjectBaseType;

@Service("themesRestFacade")
public class SubjectsRestFacadeImpl implements SubjectsRestFacade {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IndicatorsApiService indicatorsApiService = null;

    @Autowired
    private Do2TypeMapper mapper = null;

    @Override
    public List<SubjectBaseType> retrieveSubjects(final String baseUrl) throws Exception {
        List<SubjectIndicatorResult> subjects = indicatorsApiService.retrieveSubjectsInIndicators();
        List<SubjectBaseType> subjectTypes = mapper.subjectDoToBaseType(subjects, baseUrl);
        return subjectTypes;
    }

}
