package es.gobcan.istac.indicators.rest.facadeimpl;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Categories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.rest.clients.SrmRestInternalFacade;
import es.gobcan.istac.indicators.rest.facadeapi.SubjectsRestFacade;
import es.gobcan.istac.indicators.rest.mapper.Do2TypeMapper;
import es.gobcan.istac.indicators.rest.types.SubjectBaseType;

@Service("themesRestFacade")
public class SubjectsRestFacadeImpl implements SubjectsRestFacade {

    @Autowired
    private Do2TypeMapper                  mapper                = null;

    @Autowired
    private final SrmRestInternalFacade    srmRestInternalFacade = null;

    @Autowired
    private IndicatorsConfigurationService configurationService;

    @Override
    public List<SubjectBaseType> retrieveCategoriesMarkAsSubjects() throws MetamacException {
        Categories categories = srmRestInternalFacade.retrieveCategoriesByCategoryScheme(configurationService.retrieveDefaultCategoryScheme());

        return mapper.subjectDoToBaseType(categories.getCategories());
    }

}
