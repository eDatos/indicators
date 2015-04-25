package es.gobcan.istac.indicators.rest.facadeimpl;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.repositoryimpl.finders.SubjectIndicatorResult;
import es.gobcan.istac.indicators.rest.facadeapi.SubjectsRestFacade;
import es.gobcan.istac.indicators.rest.mapper.Do2TypeMapper;
import es.gobcan.istac.indicators.rest.serviceapi.IndicatorsApiService;
import es.gobcan.istac.indicators.rest.types.SubjectBaseType;

@Service("themesRestFacade")
public class SubjectsRestFacadeImpl implements SubjectsRestFacade {

    @Autowired
    private IndicatorsApiService indicatorsApiService = null;

    @Autowired
    private Do2TypeMapper        mapper               = null;

    @Override
    public List<SubjectBaseType> retrieveSubjects(final String baseUrl) throws MetamacException {
        List<SubjectIndicatorResult> subjects = indicatorsApiService.retrieveSubjectsInIndicators();
        return mapper.subjectDoToBaseType(subjects, baseUrl);
    }

}
