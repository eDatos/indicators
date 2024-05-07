package es.gobcan.istac.indicators.web.server.rest;

import static org.siemac.metamac.rest.api.constants.RestApiConstants.WILDCARD_ALL;
import static org.siemac.metamac.rest.api.utils.RestCriteriaUtils.appendConditionToQuery;
import static org.siemac.metamac.rest.api.utils.RestCriteriaUtils.fieldComparison;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.CommonServiceExceptionParameters;
import org.siemac.metamac.rest.common.v1_0.domain.ComparisonOperator;
import org.siemac.metamac.rest.common.v1_0.domain.LogicalOperator;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Categories;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codelists;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.web.common.server.rest.utils.RestExceptionUtils;
import org.siemac.metamac.web.common.shared.criteria.ExternalResourceWebCriteria;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;
import org.siemac.metamac.web.common.shared.criteria.SrmExternalResourceRestCriteria;
import org.siemac.metamac.web.common.shared.criteria.SrmItemRestCriteria;
import org.siemac.metamac.web.common.shared.criteria.base.HasSimpleCriteria;
import org.siemac.metamac.web.common.shared.domain.ExternalItemsResult;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.core.service.RestApiLocator;
import es.gobcan.istac.indicators.core.service.SrmRestInternalService;
import es.gobcan.istac.indicators.web.server.utils.ExternalItemWebUtils;
import es.gobcan.istac.indicators.web.server.utils.RestQueryUtils;

@Component(SrmRestInternalFacade.BEAN_ID)
public class SrmRestInternalFacadeImpl implements SrmRestInternalFacade {

    private static Logger          logger = LoggerFactory.getLogger(SrmRestInternalFacadeImpl.class);

    @Autowired
    private RestApiLocator         restApiLocator;

    @Autowired
    private RestExceptionUtils     restExceptionUtils;

    @Autowired
    private SrmRestInternalService srmRestInternalService;

    @Override
    public ExternalItemsResult retrieveCategoryElementsByCategoryScheme(ServiceContext serviceContext, String categorySchemeUrn, ExternalResourceWebCriteria condition, int firstResult, int maxResults)
            throws MetamacWebException {
        String limit = String.valueOf(maxResults);
        String offset = String.valueOf(firstResult);
        String orderBy = null;
        String query = null;
        if (condition != null) {
            query = buildCategoryQueryCode(condition);
        }

        try {
            List<ResourceInternal> categoryElements = new ArrayList<ResourceInternal>();

            Categories categories = srmRestInternalService.retrieveCategoriesByCategoryScheme(categorySchemeUrn, query, orderBy, limit, offset);

            for (CategoryResourceInternal category : categories.getCategories()) {
                if (category.getCategoryElement() != null) {
                    categoryElements.add(category.getCategoryElement());
                }
            }

            ExternalItemsResult result = ExternalItemWebUtils.getCategoryElementsAsExternalItemsResult(categoryElements);
            result.setFirstResult(categories.getOffset() != null ? categories.getOffset().intValue() : 0);
            result.setTotalResults(categories.getOffset() != null ? categories.getTotal().intValue() : 0);

            return result;

        } catch (Exception e) {
            logger.error("Unable to find category elements for {} from srm internal api", categorySchemeUrn, e);
            throw manageSrmInternalRestException(serviceContext, e);
        }
    }

    //
    // CODELISTS
    //

    @Override
    public ExternalItemsResult findCodelists(ServiceContext serviceContext, SrmExternalResourceRestCriteria criteria, int firstResult, int maxResults) throws MetamacWebException {

        String limit = String.valueOf(maxResults);
        String offset = String.valueOf(firstResult);
        String orderBy = null;
        String query = RestQueryUtils.buildCodelistQuery(criteria);

        try {
            Codelists codelists = restApiLocator.getSrmRestInternalFacadeV10().findCodelists(query, orderBy, limit, offset);
            return ExternalItemWebUtils.getCodelistsAsExternalItemsResult(codelists);
        } catch (Exception e) {
            throw manageSrmInternalRestException(serviceContext, e);
        }
    }

    //
    // CODES
    //

    @Override
    public ExternalItemsResult findCodes(ServiceContext serviceContext, SrmItemRestCriteria itemWebCriteria, int firstResult, int maxResults) throws MetamacWebException {

        String limit = String.valueOf(maxResults);
        String offset = String.valueOf(firstResult);
        String orderBy = null;
        String query = RestQueryUtils.buildCodeQuery(itemWebCriteria);
        String fields = null;

        try {
            Codes codes = restApiLocator.getSrmRestInternalFacadeV10().findCodes(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, query, orderBy, limit, offset, null, null, null, null, fields);
            return ExternalItemWebUtils.getCodesAsExternalItemsResult(codes);
        } catch (Exception e) {
            throw manageSrmInternalRestException(serviceContext, e);
        }
    }

    private MetamacWebException manageSrmInternalRestException(ServiceContext ctx, Exception e) throws MetamacWebException {
        return restExceptionUtils.manageMetamacRestException(ctx, e, CommonServiceExceptionParameters.API_SRM_INTERNAL, restApiLocator.getSrmRestInternalFacadeV10());
    }

    // -------------------------------------------------------------------------------------------------------------
    // CODE
    // -------------------------------------------------------------------------------------------------------------

    public static String buildCategoryQueryCode(MetamacWebCriteria webCriteria) {
        StringBuilder queryBuilder = new StringBuilder();
        if (webCriteria != null) {
            addSimpleRestCriteria(queryBuilder, webCriteria, CategoryCriteriaPropertyRestriction.CATEGORY_ELEMENT_SHORT_NAME, CategoryCriteriaPropertyRestriction.CATEGORY_ELEMENT_CODE,
                    CategoryCriteriaPropertyRestriction.CATEGORY_ELEMENT_URN);
        }
        appendConditionToQuery(queryBuilder, "CATEGORY_ELEMENT_CODE IS_NOT_NULL");
        return queryBuilder.toString();
    }

    @SuppressWarnings("rawtypes")
    private static void addSimpleRestCriteria(StringBuilder queryBuilder, HasSimpleCriteria criteria, Enum... fields) {
        String simpleCriteria = criteria.getCriteria();
        if (StringUtils.isNotBlank(simpleCriteria)) {
            StringBuilder conditionBuilder = new StringBuilder();

            List<String> conditions = new ArrayList<String>();
            for (Enum field : fields) {
                conditions.add(fieldComparison(field, ComparisonOperator.ILIKE, simpleCriteria));
            }

            conditionBuilder.append("(");
            for (int i = 0; i < conditions.size(); i++) {
                if (i > 0) {
                    conditionBuilder.append(" ").append(LogicalOperator.OR).append(" ");
                }
                conditionBuilder.append(conditions.get(i));
            }
            conditionBuilder.append(")");
            appendConditionToQuery(queryBuilder, conditionBuilder.toString());
        }
    }
}
