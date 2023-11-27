package es.gobcan.istac.indicators.core.service;

import static org.siemac.metamac.rest.api.utils.RestCriteriaUtils.fieldComparison;

import java.util.HashMap;
import java.util.Map;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.rest.common.v1_0.domain.ComparisonOperator;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Categories;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Category;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryElements;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Code;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodeResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codes;
import org.siemac.metamac.srm.rest.common.SrmRestConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(SrmRestInternalService.BEAN_ID)
public class SrmRestInternalServiceImpl implements SrmRestInternalService {

    private static Logger  logger = LoggerFactory.getLogger(SrmRestInternalServiceImpl.class);

    @Autowired
    private RestApiLocator restApiLocator;

    @Override
    public CategoryElements findCategoryElements(String query, String orderBy, String limit, String offset) {
        try {
            return restApiLocator.getSrmRestInternalFacadeV10().findCategoryElements(query, orderBy, limit, offset);
        } catch (Exception e) {
            logger.error("Unable to find Category elements", e);
            throw toRestException(e);
        }
    }

    @Override
    public CategoryResourceInternal retrieveCategoryByCategoryElement(String categorySchemeUrn, String categoryElementCode) throws MetamacException {
        String fields = "+categoryElement";

        String[] params = UrnUtils.splitUrnItemScheme(categorySchemeUrn);
        String agencyId = params[0];
        String resourceId = params[1];
        String version = params[2];

        Categories categories = restApiLocator.getSrmRestInternalFacadeV10().findCategories(agencyId, resourceId, version, null, null, null, null, fields);
        if (categories.getCategories() != null && !categories.getCategories().isEmpty()) {
            for (CategoryResourceInternal category : categories.getCategories()) {
                if (category.getCategoryElement() != null && category.getCategoryElement().getId().equals(categoryElementCode)) {
                    return category;
                }
            }
        }

        return null;
    }

    @Override
    public Categories retrieveCategoriesByCategoryScheme(String categorySchemeUrn) throws MetamacException {
        return retrieveCategoriesByCategoryScheme(categorySchemeUrn, null, null, null, null);
    }

    @Override
    public Categories retrieveCategoriesByCategoryScheme(String categorySchemeUrn, String query, String orderBy, String limit, String offset) throws MetamacException {
        String fields = "+categoryElement";

        String[] params = UrnUtils.splitUrnItemScheme(categorySchemeUrn);
        String agencyId = params[0];
        String resourceId = params[1];
        String version = params[2];

        return restApiLocator.getSrmRestInternalFacadeV10().findCategories(agencyId, resourceId, version, query, orderBy, limit, offset, fields);

    }

    @Override
    public Category retrieveCategoryByCode(String categorySchemeUrn, String categoryCode) throws MetamacException {
        String fields = "+categoryElement";

        String[] params = UrnUtils.splitUrnItemScheme(categorySchemeUrn);
        String agencyId = params[0];
        String resourceId = params[1];
        String version = params[2];

        return restApiLocator.getSrmRestInternalFacadeV10().retrieveCategory(agencyId, resourceId, version, categoryCode);

    }

    @Override
    public String getQueryByCategoryElementCriteria(CategoryCriteriaPropertyRestriction categoryCriteria, String value) {
        return fieldComparison(categoryCriteria, ComparisonOperator.EQ, value);
    }

    private RestException toRestException(Exception e) {
        throw toRestException(e);
    }

    @Override
    public Code retrieveCodeOfCodelist(String codeUrn) throws MetamacException {
        String[] params = UrnUtils.splitUrnItem(codeUrn);
        String agencyId = params[0];
        String resourceId = params[1];
        String version = params[2];
        String codeId = params[3];

        return restApiLocator.getSrmRestInternalFacadeV10().retrieveCode(agencyId, resourceId, version, codeId);
    }

    @Override
    public Map<String, String> retrieveVariableElementsByCodesOfCodelists(String codelistUrn) throws MetamacException {
        Map<String, String> variableElementsByCodesOfCodelist = new HashMap<String, String>();
        String[] params = UrnUtils.splitUrnItemScheme(codelistUrn);
        String agencyId = params[0];
        String resourceId = params[1];
        String version = params[2];
        String fields = SrmRestConstants.FIELD_INCLUDE_VARIABLE_ELEMENT;

        Codes codes = restApiLocator.getSrmRestInternalFacadeV10().findCodes(agencyId, resourceId, version, null, null, null, null, null, null, null, null, fields);

        for (CodeResourceInternal code : codes.getCodes()) {
            variableElementsByCodesOfCodelist.put(code.getId(), code.getVariableElement().getId());
        }
        return variableElementsByCodesOfCodelist;
    }

}
