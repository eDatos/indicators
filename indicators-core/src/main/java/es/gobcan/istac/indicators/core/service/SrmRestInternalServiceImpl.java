package es.gobcan.istac.indicators.core.service;

import static org.siemac.metamac.rest.api.constants.RestApiConstants.BLANK;
import static org.siemac.metamac.rest.api.constants.RestApiConstants.QUOTE;
import static org.siemac.metamac.rest.api.constants.RestApiConstants.WILDCARD_ALL;
import static org.siemac.metamac.rest.api.constants.RestApiConstants.WILDCARD_LATEST;
import static org.siemac.metamac.rest.api.utils.RestCriteriaUtils.fieldComparison;

import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.jaxrs.client.WebClient;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.GeneratorUrnUtils;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.rest.common.v1_0.domain.ComparisonOperator;
import org.siemac.metamac.rest.common.v1_0.domain.LogicalOperator;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Categories;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Category;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryElements;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Code;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodeResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codelist;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Concepts;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure;
import org.siemac.metamac.srm.rest.common.SrmRestConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(SrmRestInternalService.BEAN_ID)
public class SrmRestInternalServiceImpl implements SrmRestInternalService {

    public static final String AND                       = "AND";
    public static final String ID_CODES_CODELIST         = "ID";
    public static final String VARIABLE_ELEMENT_URN_CODE = "VARIABLE_ELEMENT_URN";
    private static Logger      logger                    = LoggerFactory.getLogger(SrmRestInternalServiceImpl.class);

    @Autowired
    private RestApiLocator     restApiLocator;

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
    public HashMap<String, CategoryResourceInternal> retrieveSrmCategoryResoourcesByCategoryScheme(String categorySchemeUrn) throws MetamacException {
        HashMap<String, CategoryResourceInternal> categoriesByCategoryElement = new HashMap<String, CategoryResourceInternal>();

        Categories categories = retrieveCategoriesByCategoryScheme(categorySchemeUrn);

        for (CategoryResourceInternal category : categories.getCategories()) {
            if (category.getCategoryElement() != null) {
                CategoryResourceInternal existingCategory = categoriesByCategoryElement.get(category.getCategoryElement().getId());
                if (existingCategory == null) {
                    categoriesByCategoryElement.put(category.getCategoryElement().getId(), category);
                }
            }
        }

        return categoriesByCategoryElement;
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
        logger.error("Error", e);
        return RestExceptionUtils.toRestException(e, WebClient.client(restApiLocator.getSrmRestInternalFacadeV10()));
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
    public Map<String, String> retrieveVariableElementsIdByCodesOfCodelists(String codelistUrn) throws MetamacException {
        return retrieveVariableElementsIdByCodesOfCodelists(codelistUrn, null);
    }

    @Override
    public Map<String, String> retrieveVariableElementsIdByCodesOfCodelists(String codelistUrn, Codes codes) throws MetamacException {
        Map<String, String> variableElementsByCodesOfCodelist = new HashMap<String, String>();

        if (codes == null) {
            codes = retrieveCodesOfCodelist(codelistUrn, true);
        }

        for (CodeResourceInternal code : codes.getCodes()) {
            variableElementsByCodesOfCodelist.put(code.getId(), code.getVariableElement().getId());
        }
        return variableElementsByCodesOfCodelist;
    }

    @Override
    public Map<String, String> retrieveGeographicalElementsIdByCodesOfCodelists(String codelistUrn) throws MetamacException {
        return retrieveGeographicalElementsIdByCodesOfCodelists(codelistUrn, null);
    }

    @Override
    public Map<String, String> retrieveGeographicalElementsIdByCodesOfCodelists(String codelistUrn, Codes codes) throws MetamacException {
        Map<String, String> geographicalElementsByCodesOfCodelist = new HashMap<String, String>();

        if (codes == null) {
            codes = retrieveCodesOfCodelist(codelistUrn, true);
        }

        for (CodeResourceInternal code : codes.getCodes()) {
            geographicalElementsByCodesOfCodelist.put(code.getVariableElement().getId(), code.getId());
        }
        return geographicalElementsByCodesOfCodelist;
    }

    @Override
    public Codes retrieveCodelistCodesByCode(String code, String defaultTerritoryVariableUrn, int numResults) throws MetamacException {
        // Calls like this: /codelists/~all/~all/~latest/codes?query=id EQ '35003' AND VARIABLE_ELEMENT_URN LIKE 'VR_TERRITORIO'&fields=+variableElement

        String fields = SrmRestConstants.FIELD_INCLUDE_VARIABLE_ELEMENT;

        StringBuilder queryBuilder = new StringBuilder(ID_CODES_CODELIST);
        queryBuilder.append(BLANK).append(ComparisonOperator.EQ).append(BLANK).append(QUOTE).append(code).append(QUOTE).append(BLANK).append(LogicalOperator.AND).append(BLANK)
                .append(VARIABLE_ELEMENT_URN_CODE).append(BLANK).append(ComparisonOperator.LIKE).append(BLANK).append(QUOTE).append(defaultTerritoryVariableUrn).append(QUOTE);

        return restApiLocator.getSrmRestInternalFacadeV10().findCodes(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_LATEST, queryBuilder.toString(), null, String.valueOf(numResults), null, null, null, null,
                null, fields);

    }

    @Override
    public Codes retrieveCodesOfCodelist(String codelistUrn, boolean retrieveLastVersion) {

        String[] params = UrnUtils.splitUrnItemScheme(codelistUrn);
        String agencyId = params[0];
        String resourceId = params[1];
        String version = retrieveLastVersion ? "~latest" : params[2];
        String fields = SrmRestConstants.FIELD_INCLUDE_VARIABLE_ELEMENT;

        return restApiLocator.getSrmRestInternalFacadeV10().findCodes(agencyId, resourceId, version, null, null, null, null, null, null, null, null, fields);

    }

    @Override
    public Codelist retrieveCodelistLastVersion(String codelistUrn) {

        String[] params = UrnUtils.splitUrnItemScheme(codelistUrn);
        String agencyId = params[0];
        String resourceId = params[1];
        String version = "~latest";

        return restApiLocator.getSrmRestInternalFacadeV10().retrieveCodelist(agencyId, resourceId, version);

    }

    @Override
    public Concepts retrieveConceptsOfConceptScheme(String conceptSchemeUrn) throws MetamacException {

        String[] params = UrnUtils.splitUrnItemScheme(conceptSchemeUrn);
        String agencyId = params[0];
        String resourceId = params[1];
        String version = params[2];
        return restApiLocator.getSrmRestInternalFacadeV10().findConcepts(agencyId, resourceId, version, null, null, null, null, null);

    }

    @Override
    public DataStructure retrieveDsdByUrn(String urn) throws MetamacException {
        try {
            String[] dataStructureComponents = GeneratorUrnUtils.extractVersionableArtefactParts(urn);
            String agencyId = dataStructureComponents[0];
            String dsdId = dataStructureComponents[1];
            String version = dataStructureComponents[2];
            return restApiLocator.getSrmRestInternalFacadeV10().retrieveDataStructure(agencyId, dsdId, version, null);
        } catch (Exception e) {
            logger.error("Unable to find dsd by urn:" + urn, e);
            throw toRestException(e);
        }
    }

}
