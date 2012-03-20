package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doReturn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import es.gobcan.istac.indicators.core.dto.serviceapi.DataStructureDto;

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
        doReturn(dataBasic1).when(indicatorsDataService).findDataDefinition(any(ServiceContext.class),eq(dataBasic1.getUuid()));
        doReturn(dataStructure1).when(indicatorsDataService).retrieveDataStructure(any(ServiceContext.class),eq(dataStructure1.getUuid()));
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
    public void testFindDataDefinition() throws Exception {
        DataBasicDto dto = indicatorsDataServiceFacade.findDataDefinition(getServiceContext(),dataBasic1.getUuid());
        assertNotNull(dto);
        compareDoDto(dataBasic1, dto);
    }
    
    @Test
    @Override
    public void testRetrieveDataStructure() throws Exception {
        DataStructureDto dto = indicatorsDataServiceFacade.retrieveDataStructure(getServiceContext(), dataStructure1.getUuid());
        assertNotNull(dto);
        compareDoDto(dataStructure1, dto);
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
        assertEquals(dbo.getName(),dto.getName());
        assertEquals(dbo.getPxUri(),dto.getPxUri());
    }
    
    private void compareDoDto(DataStructure dbo, DataStructureDto dto) {
        assertEquals(dbo.getUuid(),dto.getUuid());
        assertEquals(dbo.getTitle(),dto.getTitle());
        assertEquals(dbo.getPxUri(),dto.getPxUri());
        assertEquals(dbo.getValueCodes(),dto.getValueCodes());
        assertEquals(dbo.getValueLabels(),dto.getValueLabels());
        List<String> variables = new ArrayList<String>(dbo.getValueCodes().keySet());
        assertEquals(variables,dto.getVariables());
        if (dbo.getSpatials() == null || dbo.getSpatials().size() == 0) {
            assertNull(dto.getSpatialVariable());
        } else {
            assertEquals(dbo.getSpatials().get(0),dto.getSpatialVariable());
            
        }
        if (dbo.getTemporals() == null || dbo.getTemporals().size() == 0) {
            assertNull(dto.getTemporalVariable());
        } else {
            assertEquals(dbo.getTemporals().get(0),dto.getTemporalVariable());
        }
    }
    
    private static void initializeObjects() {
        dataBasic1 = new DataBasic();
        dataBasic1.getUuid(); //generate uuid
        dataBasic1.setName("My Data");
        
        dataStructure1 = new DataStructure();
        dataStructure1.setUuid("1-1-1-1");
        dataStructure1.setTitle("Sociedades mercantiles");
        dataStructure1.setPxUri("urn:uuid:5213544-2313-121");
        Map<String,List<String>> codes = new HashMap<String, List<String>>();
        Map<String,List<String>> labels = new HashMap<String, List<String>>();
        codes.put("var1", getList("c1","c2","c3"));
        labels.put("var1", getList("l1","l2","l3"));
        codes.put("var2", getList("c1","c2","c3"));
        labels.put("var2", getList("l1","l2","l3"));
        codes.put("var3", getList("c1","c2","c3"));
        labels.put("var3", getList("l1","l2","l3"));
        dataStructure1.setValueCodes(codes);
        dataStructure1.setValueLabels(labels);
        dataStructure1.setHeading(getList("var1","var2"));
        dataStructure1.setStub(getList("var3"));
        dataStructure1.setTemporals(getList("var1"));
        dataStructure1.setSpatials(getList("var2"));
    }
    
}
