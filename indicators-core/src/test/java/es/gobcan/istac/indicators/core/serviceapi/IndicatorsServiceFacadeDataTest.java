package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

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
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.gobcan.istac.indicators.core.domain.DataDefinition;
import es.gobcan.istac.indicators.core.domain.DataStructure;
import es.gobcan.istac.indicators.core.dto.DataDefinitionDto;
import es.gobcan.istac.indicators.core.dto.DataStructureDto;

/**
 * Spring based transactional test with DbUnit support.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/include/indicators-data-service-facade-mockito.xml", "classpath:spring/applicationContext-test.xml"})
@TransactionConfiguration(defaultRollback = true, transactionManager = "txManager")
@Transactional
public class IndicatorsServiceFacadeDataTest extends IndicatorsBaseTest {

    @Autowired
    protected IndicatorsServiceFacade indicatorsDataServiceFacade;

    @Autowired
    private IndicatorsDataService     indicatorsDataService;

    /* Objects */
    private static DataDefinition     dataDefinition1;
    private static DataStructure      dataStructure1;

    static {
        initializeObjects();
    }

    @Before
    public void setUpMocks() throws MetamacException {
        // init mocks
        when(indicatorsDataService.retrieveDataDefinitions(any(ServiceContext.class))).thenReturn(Arrays.asList(new DataDefinition[]{dataDefinition1}));
        when(indicatorsDataService.retrieveDataDefinition(any(ServiceContext.class), eq(dataDefinition1.getUuid()))).thenReturn(dataDefinition1);
        when(indicatorsDataService.retrieveDataStructure(any(ServiceContext.class), eq(dataStructure1.getUuid()))).thenReturn(dataStructure1);
    }

    @Test
    public void testRetrieveDataDefinitions() throws Exception {
        List<DataDefinitionDto> dtos = indicatorsDataServiceFacade.retrieveDataDefinitions(getServiceContextAdministrador());
        assertNotNull(dtos);
        compareDoDto(dataDefinition1, dtos.get(0));
    }

    @Test
    public void testRetrieveDataDefinition() throws Exception {
        DataDefinitionDto dto = indicatorsDataServiceFacade.retrieveDataDefinition(getServiceContextAdministrador(), dataDefinition1.getUuid());
        assertNotNull(dto);
        compareDoDto(dataDefinition1, dto);
    }

    @Test
    public void testFindDataDefinitionNotFound() throws Exception {
        DataDefinitionDto dto = indicatorsDataServiceFacade.retrieveDataDefinition(getServiceContextAdministrador(), "NOT_EXIST");
        assertNull(dto);
    }

    @Test
    public void testRetrieveDataStructure() throws Exception {
        DataStructureDto dto = indicatorsDataServiceFacade.retrieveDataStructure(getServiceContextAdministrador(), dataStructure1.getUuid());
        assertNotNull(dto);
        compareDoDto(dataStructure1, dto);
    }

    @Test
    public void testPopulateIndicatorData() throws Exception {
        // Tests in IndicatorsDataServiceTest
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/EmptyDatabase.xml";
    }

    private void compareDoDto(DataDefinition dbo, DataDefinitionDto dto) {
        assertEquals(dbo.getUuid(), dto.getUuid());
        assertEquals(dbo.getName(), dto.getName());
        assertEquals(dbo.getPxUri(), dto.getPxUri());
    }

    private void compareDoDto(DataStructure dbo, DataStructureDto dto) {
        assertEquals(dbo.getUuid(), dto.getUuid());
        assertEquals(dbo.getTitle(), dto.getTitle());
        assertEquals(dbo.getPxUri(), dto.getQueryUrn());
        assertEquals(dbo.getSurveyCode(), dto.getSurveyCode());
        assertEquals(dbo.getSurveyTitle(), dto.getSurveyTitle());
        assertEquals(dbo.getPublishers(), dto.getPublishers());

        assertEquals(dbo.getValueCodes(), dto.getValueCodes());
        assertEquals(dbo.getValueLabels(), dto.getValueLabels());
        List<String> variables = new ArrayList<String>(dbo.getValueCodes().keySet());
        assertEquals(variables, dto.getVariables());
        assertEquals(dbo.getSpatialVariables(), dto.getSpatialVariables());
        assertEquals(dbo.getTemporalVariable(), dto.getTemporalVariable());
        assertEquals(dbo.getContVariable(), dto.getContVariable());

    }

    private static void initializeObjects() {
        dataDefinition1 = new DataDefinition();
        dataDefinition1.getUuid(); // generate uuid
        dataDefinition1.setName("My Data");

        dataStructure1 = new DataStructure();
        dataStructure1.setUuid("1-1-1-1");
        dataStructure1.setTitle("Sociedades mercantiles");
        dataStructure1.setPxUri("urn:uuid:5213544-2313-121");
        Map<String, List<String>> codes = new HashMap<String, List<String>>();
        Map<String, List<String>> labels = new HashMap<String, List<String>>();
        codes.put("var1", getList("c1", "c2", "c3"));
        labels.put("var1", getList("l1", "l2", "l3"));
        codes.put("var2", getList("c1", "c2", "c3"));
        labels.put("var2", getList("l1", "l2", "l3"));
        codes.put("var3", getList("c1", "c2", "c3"));
        labels.put("var3", getList("l1", "l2", "l3"));
        dataStructure1.setValueCodes(codes);
        dataStructure1.setValueLabels(labels);
        dataStructure1.setHeading(getList("var1", "var2"));
        dataStructure1.setStub(getList("var3"));
        dataStructure1.setTemporalVariable("var1");
        dataStructure1.setSpatialVariables(Arrays.asList("var2"));
        dataStructure1.setSurveyCode("SCODE");
        dataStructure1.setSurveyTitle("Survey title");
        dataStructure1.setPublishers(getList("pub1", "pub2"));

    }
}