package es.gobcan.istac.indicators.core.service;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Categories;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Category;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryElements;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryResourceInternal;

public interface SrmRestInternalService {

    public static final String BEAN_ID = "srmRestInternalService";

    public CategoryElements findCategoryElements(String query, String orderBy, String limit, String offset);
    public CategoryResourceInternal retrieveCategoryByCategoryElement(String categorySchemeUrn, String categoryElementCode) throws MetamacException;
    public Categories retrieveCategoriesByCategoryScheme(String categorySchemeUrn) throws MetamacException;
    public Categories retrieveCategoriesByCategoryScheme(String categorySchemeUrn, String query, String orderBy, String limit, String offset) throws MetamacException;
    public Category retrieveCategoryByCode(String categorySchemeUrn, String categoryCode) throws MetamacException;
    public String getQueryByCategoryElementCriteria(CategoryCriteriaPropertyRestriction categoryCriteria, String value);

}
