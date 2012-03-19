package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.fail;

import org.fornax.cartridges.sculptor.framework.test.AbstractDbUnitJpaTests;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Spring based transactional test with DbUnit support.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/include/indicators-data-service-facade-mockito.xml","classpath:spring/applicationContext-test.xml"})
public class IndicatorsDataServiceFacadeTest extends AbstractDbUnitJpaTests implements IndicatorsDataServiceFacadeTestBase {

    @Autowired
    protected IndicatorsDataServiceFacade indicatorsDataServiceFacade;
    
    @Autowired
    private IndicatorsDataService indicatorsDataService;
    

    @Before
    public void setUpMocks() {
        //init mocks
    }
    
    @Test
    @Override
    public void testFindDataDefinitions() throws Exception {
    }

    @Test
    @Override
    public void testRetrieveDataStructure() throws Exception {
        fail("not implemented");
    }

    @Test
    @Override
    public void testRetrieveData() throws Exception {
        fail("not implemented");
    }
    
    @Override
    protected String getDataSetFile() {
        return "dbunit/IndicatorsServiceFacadeIndicatorsTest.xml";
    }
}
