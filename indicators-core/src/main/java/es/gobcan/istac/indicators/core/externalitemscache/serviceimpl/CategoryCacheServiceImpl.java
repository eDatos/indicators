package es.gobcan.istac.indicators.core.externalitemscache.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Categories;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryResourceInternal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.core.externalitemscache.domain.CategoryCache;
import es.gobcan.istac.indicators.core.externalitemscache.domain.CategoryCacheProperties;
import es.gobcan.istac.indicators.core.mapper.InternationalString2InternationalStringMapper;
import es.gobcan.istac.indicators.core.service.SrmRestInternalService;

/**
 * Implementation of CategoryCacheService.
 */
@Service("categoryCacheService")
public class CategoryCacheServiceImpl extends CategoryCacheServiceImplBase {

    @Autowired
    private SrmRestInternalService                        srmRestInternalService;

    @Autowired
    private IndicatorsConfigurationService                configurationService;

    @Autowired
    private InternationalString2InternationalStringMapper internationalString2InternationalStringMapper;

    public CategoryCacheServiceImpl() {
        // without implement
    }

    public CategoryCache retrieveCategoryCacheByCategoryCode(ServiceContext ctx, String code) throws MetamacException {

        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CategoryCache.class).withProperty(CategoryCacheProperties.categoryCode()).ignoreCaseEq(code).distinctRoot()
                .build();

        PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);
        PagedResult<CategoryCache> categoryCache = getCategoryCacheRepository().findByCondition(conditions, pagingParameter);

        if (categoryCache.getValues().size() == 1) {
            return categoryCache.getValues().iterator().next();
        }

        return null;

    }

    public CategoryCache retrieveCategoryCacheByCategoryElementCode(ServiceContext ctx, String code) throws MetamacException {

        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CategoryCache.class).withProperty(CategoryCacheProperties.categoryElementCode()).ignoreCaseEq(code).distinctRoot()
                .build();

        PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);
        PagedResult<CategoryCache> categoryCache = getCategoryCacheRepository().findByCondition(conditions, pagingParameter);

        if (categoryCache.getValues().size() == 1) {
            return categoryCache.getValues().iterator().next();
        }

        return null;

    }

    @Override
    public List<CategoryCache> retrieveAllCategories(ServiceContext ctx) throws MetamacException {
        return getCategoryCacheRepository().findAll();
    }

    @Override
    public List<MetamacExceptionItem> updateCategoryCacheAll(ServiceContext ctx, List<String> categoryElementsInIndicators) throws MetamacException {
        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        List<CategoryCache> categoryCacheUpdated = new ArrayList<>();

        HashMap<String, CategoryResourceInternal> categoriesByCategoryElement = getCategoriesByDefaultCategoryScheme();

        for (String categoryElement : categoryElementsInIndicators) {
            CategoryResourceInternal category = categoriesByCategoryElement.get(categoryElement);
            if (category != null) {
                categoryCacheUpdated.add(createCategoryCacheEntry(category.getNestedId() != null ? category.getNestedId() : category.getId(),
                        internationalString2InternationalStringMapper.internationalString2InternationalString(category.getName()), categoryElement, ctx.getUserId()));
            }
        }

        deleteAllCacheEntries();

        createCategoryCacheEntries(categoryCacheUpdated);

        return exceptionItems;

    }

    private void deleteAllCacheEntries() {
        List<CategoryCache> categoryCacheActualEntries = getCategoryCacheRepository().findAll();

        for (CategoryCache categoryCache : categoryCacheActualEntries) {
            getCategoryCacheRepository().delete(categoryCache);
        }
    }

    private void createCategoryCacheEntries(List<CategoryCache> categoryCacheEntries) {

        for (CategoryCache categoryCache : categoryCacheEntries) {
            getCategoryCacheRepository().save(categoryCache);
        }
    }

    private HashMap<String, CategoryResourceInternal> getCategoriesByDefaultCategoryScheme() throws MetamacException {
        HashMap<String, CategoryResourceInternal> categoriesByCategoryElement = new HashMap<String, CategoryResourceInternal>();

        Categories categories = srmRestInternalService.retrieveCategoriesByCategoryScheme(configurationService.retrieveDefaultCategoryScheme());

        for (CategoryResourceInternal category : categories.getCategories()) {
            if (category.getCategoryElement() != null) {
                categoriesByCategoryElement.put(category.getCategoryElement().getId(), category);
            }
        }

        return categoriesByCategoryElement;
    }

    private CategoryCache createCategoryCacheEntry(String categoryCode, InternationalString categoryTitle, String categoryElementCode, String userId) {
        CategoryCache categoryCache = new CategoryCache();

        categoryCache.setCategoryCode(categoryCode);
        categoryCache.setTitle(categoryTitle);
        categoryCache.setCategoryElementCode(categoryElementCode);
        categoryCache.setCreatedDate(new DateTime());
        categoryCache.setCreatedBy(userId);

        return categoryCache;
    }
}
