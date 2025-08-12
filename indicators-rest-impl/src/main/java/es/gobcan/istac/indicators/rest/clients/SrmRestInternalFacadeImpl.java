package es.gobcan.istac.indicators.rest.clients;

import java.util.HashMap;
import java.util.Map;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Categories;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Category;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Code;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.core.service.SrmRestInternalService;
import es.gobcan.istac.indicators.rest.mapper.SrmRestObjectsMapper;

@Component(SrmRestInternalFacade.BEAN_ID)
public class SrmRestInternalFacadeImpl implements SrmRestInternalFacade {

    @Autowired
    private SrmRestInternalService srmRestInternalService;

    @Override
    public CategoryResourceInternal retrieveCategoryByCategoryElement(String categorySchemeUrn, String categoryElementCode) throws MetamacException {
        return srmRestInternalService.retrieveCategoryByCategoryElement(categorySchemeUrn, categoryElementCode);
    }

    @Override
    public Categories retrieveCategoriesByCategoryScheme(String categorySchemeUrn) throws MetamacException {
        return srmRestInternalService.retrieveCategoriesByCategoryScheme(categorySchemeUrn);
    }

    @Override
    public Category retrieveCategoryByCode(String categorySchemeUrn, String categoryCode) throws MetamacException {
        return srmRestInternalService.retrieveCategoryByCode(categorySchemeUrn, categoryCode);
    }

    @Override
    public HashMap<String, CategoryResourceInternal> retrieveSrmCategoryResoourcesByCategoryScheme(String categorySchemeUrn) throws MetamacException {
        return srmRestInternalService.retrieveSrmCategoryResoourcesByCategoryScheme(categorySchemeUrn);
    }

    @Override
    public Code retrieveCodeOfCodelistByUrn(String codeUrn) throws MetamacException {
        return srmRestInternalService.retrieveCodeOfCodelist(codeUrn);
    }

    @Override
    public Map<String, String> retrieveVariableElementsIdByCodesOfCodelists(String codelistUrn) throws MetamacException {
        return srmRestInternalService.retrieveVariableElementsIdByCodesOfCodelists(codelistUrn);
    }

    @Override
    public Map<String, String> retrieveGeographicalElementsIdByCodesOfCodelists(String codelistUrn) throws MetamacException {
        return srmRestInternalService.retrieveGeographicalElementsIdByCodesOfCodelists(codelistUrn);
    }

    @Override
    public Codes retrieveCodelistCodesByCode(String code, String defaultTerritoryVariableUrn, int numResults) throws MetamacException {
        return srmRestInternalService.retrieveCodelistCodesByCode(code, defaultTerritoryVariableUrn, numResults);
    }

    @Override
    public void retrieveCodelistVariableElementInformation(String codelistUrn, SrmRestObjectsMapper srmRestObjectsMapper) throws MetamacException {
        Codes codes = srmRestInternalService.retrieveCodesOfCodelist(codelistUrn, true);
        srmRestObjectsMapper.setGeographicalVariableElementsByCode(srmRestInternalService.retrieveVariableElementsIdByCodesOfCodelists(codelistUrn, codes));
        srmRestObjectsMapper.setGeographicalCodesByVariableElement(srmRestInternalService.retrieveGeographicalElementsIdByCodesOfCodelists(codelistUrn, codes));
    }

}
