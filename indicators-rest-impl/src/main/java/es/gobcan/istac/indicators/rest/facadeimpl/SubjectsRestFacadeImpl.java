package es.gobcan.istac.indicators.rest.facadeimpl;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.externalitemscache.domain.CategoryCache;
import es.gobcan.istac.indicators.core.externalitemscache.serviceapi.CategoryCacheService;
import es.gobcan.istac.indicators.rest.IndicatorsRestConstants;
import es.gobcan.istac.indicators.rest.facadeapi.SubjectsRestFacade;
import es.gobcan.istac.indicators.rest.mapper.Do2TypeMapper;
import es.gobcan.istac.indicators.rest.types.SubjectBaseType;

@Service("themesRestFacade")
public class SubjectsRestFacadeImpl implements SubjectsRestFacade {

    @Autowired
    private Do2TypeMapper        mapper = null;

    @Autowired
    private CategoryCacheService categoryCacheService;

    @Override
    public List<SubjectBaseType> retrieveCategoriesMarkAsSubjects() throws MetamacException {

        List<CategoryCache> categoryCacheEntries = categoryCacheService.retrieveAllCategories(IndicatorsRestConstants.SERVICE_CONTEXT);

        return mapper.subjectDoToBaseType(categoryCacheEntries);
    }

}
