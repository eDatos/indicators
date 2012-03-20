package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

import java.util.Arrays;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.gobcan.istac.indicators.core.domain.DataBasic;
import es.gobcan.istac.indicators.core.domain.DataStructure;
import es.gobcan.istac.indicators.core.dto.serviceapi.DataBasicDto;

/**
 * Spring based transactional test with DbUnit support.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/include/indicators-data-service-facade-mockito.xml","classpath:spring/applicationContext-test.xml"})
public class IndicatorsDataServiceFacadeTest extends IndicatorsBaseTest implements IndicatorsDataServiceFacadeTestBase {

    @Autowired
    protected IndicatorsDataServiceFacade indicatorsDataServiceFacade;
    
    @Autowired
    private IndicatorsDataService indicatorsDataService;
    
    /* Objects */
    private static DataBasic dataBasic1;
    private static DataStructure dataStructure1;
    
    static {
        initializeObjects();
    }

    @Before
    public void setUpMocks() throws MetamacException {
        //init mocks
        doReturn(Arrays.asList(new DataBasic[] {dataBasic1})).when(indicatorsDataService).findDataDefinitions(any(ServiceContext.class));
    }
    
    @Test
    @Override
    public void testFindDataDefinitions() throws Exception {
        List<DataBasicDto> dtos = indicatorsDataServiceFacade.findDataDefinitions(getServiceContext());
        assertNotNull(dtos);
        compareDoDto(dataBasic1, dtos.get(0));
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
        return "dbunit/EmptyDatabase.xml";
    }
    
    private void compareDoDto(DataBasic dbo, DataBasicDto dto) {
        assertEquals(dbo.getUuid(),dto.getUuid());
        assertEquals(dbo.getTitle(),dto.getTitle());
    }
    
    private static void initializeObjects() {
        dataBasic1 = new DataBasic();
        dataBasic1.getUuid(); //generate uuid
        dataBasic1.setTitle("My Data");
        
        dataStructure1 = new DataStructure();
        
    }
}
