package es.gobcan.istac.indicators.core.externalitemscache.serviceapi;

import static org.junit.Assert.fail;

import org.fornax.cartridges.sculptor.framework.test.AbstractDbUnitJpaTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Spring based transactional test with DbUnit support.
 */
public class CategoryCacheServiceTest extends AbstractDbUnitJpaTests implements CategoryCacheServiceTestBase {

    @Autowired
    protected CategoryCacheService categoryCacheService;

    @Test
    public void testRetrieveCategoryCacheByCategoryCode() throws Exception {
        // TODO Auto-generated method stub
        fail("testRetrieveCategoryCacheByCategoryCode not implemented");
    }

    @Test
    public void testRetrieveCategoryCacheByCategoryElementCode() throws Exception {
        // TODO Auto-generated method stub
        fail("testRetrieveCategoryCacheByCategoryElementCode not implemented");
    }

    @Test
    public void testRetrieveAllCategories() throws Exception {
        // TODO Auto-generated method stub
        fail("testRetrieveAllCategories not implemented");
    }

    @Test
    public void testUpdateCategoryCacheAll() throws Exception {
        // TODO Auto-generated method stub
        fail("testUpdateCategoryCache not implemented");
    }

    @Test
    public void testCreateCategoryCacheByCategoryElement() throws Exception {
        // TODO Auto-generated method stub
        fail("testCreateCategoryCacheByCategoryElement not implemented");
    }

}
