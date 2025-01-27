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
import org.siemac.metamac.core.common.ent.domain.ExternalItem;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Categories;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryResourceInternal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.externalitemscache.domain.CategoryCache;
import es.gobcan.istac.indicators.core.externalitemscache.domain.CategoryCacheProperties;
import es.gobcan.istac.indicators.core.mapper.InternationalString2InternationalStringMapper;
import es.gobcan.istac.indicators.core.notices.ServiceNoticeAction;
import es.gobcan.istac.indicators.core.service.NoticesRestInternalService;
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

    @Qualifier("txManager")
    @Autowired
    private PlatformTransactionManager                    platformTransactionManager;

    @Autowired
    NoticesRestInternalService                            noticesRestInternalService;

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
    public void updateCategoryCacheAll(ServiceContext ctx, List<String> categoryElementsInIndicators) throws MetamacException {
        List<CategoryCache> categoryCacheUpdated = new ArrayList<>();

        HashMap<String, CategoryResourceInternal> categoriesByCategoryElement = getCategoriesByDefaultCategoryScheme(ctx, true);

        for (String categoryElement : categoryElementsInIndicators) {
            CategoryResourceInternal category = categoriesByCategoryElement.get(categoryElement);
            if (category != null) {
                categoryCacheUpdated.add(createCategoryCacheEntry(category.getNestedId() != null ? category.getNestedId() : category.getId(),
                        internationalString2InternationalStringMapper.internationalString2InternationalString(category.getName()), categoryElement, ctx.getUserId()));
            }
        }

        deleteAllCacheEntries();

        createCategoryCacheEntries(categoryCacheUpdated);

    }

    public void createCategoryCacheByCategoryElement(ServiceContext ctx, ExternalItem categoryElement) throws MetamacException {

        String query = srmRestInternalService.getQueryByCategoryElementCriteria(CategoryCriteriaPropertyRestriction.CATEGORY_ELEMENT_CODE, categoryElement.getCode());
        Categories categories = srmRestInternalService.retrieveCategoriesByCategoryScheme(configurationService.retrieveDefaultCategoryScheme(), query, null, null, null);

        if (categories != null && !categories.getCategories().isEmpty()) {
            CategoryResourceInternal category = categories.getCategories().get(0);
            CategoryCache categoryCacheNew = createCategoryCacheEntry(category.getNestedId() != null ? category.getNestedId() : category.getId(),
                    internationalString2InternationalStringMapper.internationalString2InternationalString(category.getName()), categoryElement.getCode(), ctx.getUserId());
            getCategoryCacheRepository().save(categoryCacheNew);
        }
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

    private HashMap<String, CategoryResourceInternal> getCategoriesByDefaultCategoryScheme(ServiceContext ctx, boolean sendNotification) throws MetamacException {
        HashMap<String, CategoryResourceInternal> categoriesByCategoryElement = new HashMap<String, CategoryResourceInternal>();

        Categories categories = srmRestInternalService.retrieveCategoriesByCategoryScheme(configurationService.retrieveDefaultCategoryScheme());

        for (CategoryResourceInternal category : categories.getCategories()) {
            if (category.getCategoryElement() != null) {
                CategoryResourceInternal existingCategory = categoriesByCategoryElement.get(category.getCategoryElement().getId());
                if (existingCategory == null) {
                    categoriesByCategoryElement.put(category.getCategoryElement().getId(), category);
                } else if (sendNotification)
                    sendNotificationDuplicateCategoryElement(ctx, category, existingCategory);
            }
        }

        return categoriesByCategoryElement;
    }

    private void sendNotificationDuplicateCategoryElement(ServiceContext ctx, CategoryResourceInternal category, CategoryResourceInternal existingCategory) {
        String codeCategory = existingCategory.getNestedId() != null ? existingCategory.getNestedId() : existingCategory.getId();
        MetamacException e = new MetamacException(ServiceExceptionType.UPDATE_CATEGORY_CACHE_JOB_DUPLICATE_CAT_ELEMENT_ERROR, category.getCategoryElement().getId(), codeCategory);
        noticesRestInternalService.createUpdateCategoryCacheErrorNotification(ctx.getUserId(), ServiceNoticeAction.UPDATE_CATEGORY_CACHE_JOB, e);
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
