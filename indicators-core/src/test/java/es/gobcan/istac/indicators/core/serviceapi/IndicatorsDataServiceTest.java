package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.gobcan.istac.indicators.core.domain.DataDefinition;
import es.gobcan.istac.indicators.core.domain.DataStructure;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
/**
 * Spring based transactional test with DbUnit support.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/include/indicators-data-service-mockito.xml","classpath:spring/applicationContext-test.xml"})
public class IndicatorsDataServiceTest extends IndicatorsBaseTest implements IndicatorsDataServiceTestBase {

    private static final String CONSULTA1_UUID = "2d4887dc-52f0-4c17-abbb-ef1fdc62e868";
    private static final String CONSULTA2_UUID = "1-1-1-1-1";
    private static final String CONSULTA3_UUID = "2-2-2-2-2";
    private static final String CONSULTA1_JSON_STRUC = readFile("json/query_structure_1.json");
    
    private static final String INDICATOR1_UUID = "Indicator-1";
    private static final String INDICATOR1_DS_GPE_UUID = "Indicator-1-v1-DataSource-1-GPE";
    private static final String INDICATOR1_GPE_JSON_DATA = readFile("json/query_data_1.json");
    private static final String INDICATOR1_VERSION= "1.000";
    
    private static final String INDICATOR2_UUID = "Indicator-2";
    private static final String INDICATOR2_DS_GPE_UUID = "Indicator-2-v1-DataSource-1-GPE";
    private static final String INDICATOR2_GPE_JSON_DATA = readFile("json/query_data_1.json");
    private static final String INDICATOR2_VERSION= "1.000";
    
    private static final String INDICATOR3_UUID = "Indicator-3";
    private static final String INDICATOR3_DS_GPE_UUID = "Indicator-3-v1-DataSource-1-GPE";
    private static final String INDICATOR3_GPE_JSON_DATA = readFile("json/query_data_1.json");
    private static final String INDICATOR3_VERSION= "1.000";
    
    private static final String INDICATOR4_UUID = "Indicator-4";
    private static final String INDICATOR4_DS_GPE_UUID = "Indicator-4-v1-DataSource-1-GPE";
    private static final String INDICATOR4_GPE_JSON_DATA = readFile("json/query_data_1.json");
    private static final String INDICATOR4_VERSION= "1.000";
    
    @Autowired
    protected IndicatorsDataService indicatorsDataService;
    
    @Autowired
    private IndicatorsDataProviderService indicatorsDataProviderService;


    @Before
    public void initMock() throws MetamacException {
        when(indicatorsDataProviderService.retrieveDataStructureJson(Matchers.any(ServiceContext.class), Matchers.eq(CONSULTA1_UUID))).thenReturn(CONSULTA1_JSON_STRUC);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR2_DS_GPE_UUID))).thenReturn(INDICATOR2_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR3_DS_GPE_UUID))).thenReturn(INDICATOR3_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR4_DS_GPE_UUID))).thenReturn(INDICATOR4_GPE_JSON_DATA);
    }
    
    @Test
    @Override
    public void testFindDataDefinitions() throws Exception {
        List<DataDefinition> dataDefs = indicatorsDataService.findDataDefinitions(getServiceContext());
        assertEquals(1, dataDefs.size());
        assertTrue(1 == dataDefs.get(0).getId());
    }
    
    @Test
    public void testRetrieveDataDefinitionUuidNull() throws Exception {
        try {
            DataDefinition dataDef = indicatorsDataService.retrieveDataDefinition(getServiceContext(),null);
            fail("parameter required");
        } catch(MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("UUID", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }
    
    @Test
    @Override
    public void testRetrieveDataDefinition() throws Exception {
        DataDefinition dataDef = indicatorsDataService.retrieveDataDefinition(getServiceContext(),CONSULTA1_UUID);
        assertTrue(1 == dataDef.getId());
    }
    
    @Test
    public void testRetrieveDataDefinitionNotExist() throws Exception {
        try {
            indicatorsDataService.retrieveDataDefinition(getServiceContext(),"NOT_EXIST");
            fail("Should throws an exception");
        } catch (MetamacException e) {
            assertNotNull(e.getExceptionItems());
            assertEquals(1,e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DATA_DEFINITION_RETRIEVE_ERROR.getCode(),e.getExceptionItems().get(0).getCode());
        }
    }
    
    @Test
    public void testRetrieveDataDefinitionArchived() throws Exception {
        try {
            indicatorsDataService.retrieveDataDefinition(getServiceContext(),CONSULTA2_UUID);
            fail("Should throws an exception");
        } catch (MetamacException e) {
            assertNotNull(e.getExceptionItems());
            assertEquals(1,e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DATA_DEFINITION_RETRIEVE_ERROR.getCode(),e.getExceptionItems().get(0).getCode());
        }
    }
    
    @Test
    public void testRetrieveDataDefinitionPublishedUnavailable() throws Exception {
        try {
            indicatorsDataService.retrieveDataDefinition(getServiceContext(),CONSULTA3_UUID);
        } catch (MetamacException e) {
            assertNotNull(e.getExceptionItems());
            assertEquals(1,e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DATA_DEFINITION_RETRIEVE_ERROR.getCode(),e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    @Override
    public void testRetrieveDataStructure() throws Exception {
        DataStructure dataStruc = indicatorsDataService.retrieveDataStructure(getServiceContext(), CONSULTA1_UUID);
        assertEquals("Sociedades mercantiles que amplían capital Gran PX.",dataStruc.getTitle());
        assertEquals("urn:uuid:bf800d7a-53cd-49a9-a90e-da2f1be18f0e",dataStruc.getPxUri());
        
        String[] stubs = new String[] {"Periodos","Provincias por CC.AA."};
        compareCollection(stubs, dataStruc.getStub());
        
        String[] headings = new String[] {"Naturaleza jurídica","Indicadores"};
        compareCollection(headings, dataStruc.getHeading());

        String[] labels = new String[] {"2011 Enero (p)","2010 TOTAL","2010 Diciembre (p)","2010 Noviembre (p)","2010 Octubre (p)","2010 Septiembre (p)","ESPAÑA"," ANDALUCÍA","  Almería","  Cádiz","  Córdoba","  Granada","  Huelva","  Jaén","  Málaga","  Sevilla"," ARAGÓN","  Huesca","  Teruel","  Zaragoza"," ASTURIAS (PRINCIPADO DE)"," BALEARS (ILLES)"," CANARIAS","  Palmas (Las)","  Santa Cruz de Tenerife"," CANTABRIA"," CASTILLA Y LEÓN","  Ávila","  Burgos","  León","  Palencia","  Salamanca","  Segovia","  Soria","  Valladolid","  Zamora"," CASTILLA-LA MANCHA","  Albacete","  Ciudad Real","  Cuenca","  Guadalajara","  Toledo"," CATALUÑA","  Barcelona","  Girona","  Lleida","  Tarragona"," COMUNITAT VALENCIANA","  Alicante/Alacant","  Castellón/Castelló","  Valencia/València"," EXTREMADURA","  Badajoz","  Cáceres"," GALICIA","  Coruña (A)","  Lugo","  Ourense","  Pontevedra"," MADRID (COMUNIDAD DE)"," MURCIA (REGIÓN DE)"," NAVARRA (COMUNIDAD FORAL DE)"," PAÍS VASCO","  Álava","  Guipúzcoa","  Vizcaya"," RIOJA (LA)"," CEUTA"," MELILLA","TOTAL DE SOCIEDADES", "Número de sociedades"};
        compareCollection(labels, dataStruc.getValueLabels().values());
   		
        String[] codes = new String[] {"2011M01","2010","2010M12","2010M11","2010M10","2010M09","ES","ES61","ES611","ES612","ES613","ES614","ES615","ES616","ES617","ES618","ES24","ES241","ES242","ES243","ES12","ES53","ES70","ES701","ES702","ES13","ES41","ES411","ES412","ES413","ES414","ES415","ES416","ES417","ES418","ES419","ES42","ES421","ES422","ES423","ES424","ES425","ES51","ES511","ES512","ES513","ES514","ES52","ES521","ES522","ES523","ES43","ES431","ES432","ES11","ES111","ES112","ES113","ES114","ES30","ES62","ES22","ES21","ES211","ES212","ES213","ES23","ES63","ES64","T","NumSoc"};
        compareCollection(codes, dataStruc.getValueCodes().values());
        
        String[] temporals = new String[] {"Periodos"};
        compareCollection(temporals, dataStruc.getTemporals());
        
        String[] spatials = new String[] {};
        compareCollection(spatials, dataStruc.getSpatials());
        
        String contVariable = "Indicadores";
        assertEquals(contVariable,dataStruc.getContVariable());
        
        String[] notes = new String[] {"(p) Dato provisional#(..) Dato no disponible#En Otras sociedades se incluyen:#Desde 2003 las Sociedades Comanditarias y Sociedades Colectivas.#Hasta 2002 las Sociedades de Responsabilidad Limitada y Sociedades Colectivas.#Hasta 1998 el capital suscrito se mide en millones de pesetas."};
        compareCollection(notes, dataStruc.getNotes());
        
        String source = "Instituto Canario de Estadística (ISTAC) a partir de datos del Instituto Nacional de Estadística (INE)."; 
        assertEquals(source, dataStruc.getSource());
        
        String surveyCode = "C00040A"; 
        assertEquals(surveyCode, dataStruc.getSurveyCode());
        
        String surveyTitle = "Estadística de Sociedades Mercantiles"; 
        assertEquals(surveyTitle, dataStruc.getSurveyTitle());
        
        String[] publishers = {"Instituto Canario de Estadística (ISTAC)"};
        compareCollection(publishers, dataStruc.getPublishers());
        
    }

    @Test
    public void testRetrieveDataStructureUuidNull() throws Exception {
        try {
            DataStructure dataStruc = indicatorsDataService.retrieveDataStructure(getServiceContext(), null);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("UUID", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Override
    public void testPopulateIndicatorData() throws Exception {
        indicatorsDataService.populateIndicatorData(getServiceContext(), INDICATOR1_UUID, INDICATOR1_VERSION);
    }
    @Test
    public void testPopulateIndicatorDataSpatialVariableTemporalValue() throws Exception {
        indicatorsDataService.populateIndicatorData(getServiceContext(), INDICATOR2_UUID, INDICATOR2_VERSION);
    }
    
    @Test
    public void testPopulateIndicatorDataSpatialValueTemporalVariable() throws Exception {
        indicatorsDataService.populateIndicatorData(getServiceContext(), INDICATOR3_UUID, INDICATOR3_VERSION);
    }
    
    @Test
    public void testPopulateIndicatorDataSpatialValueTemporalValue() throws Exception {
        indicatorsDataService.populateIndicatorData(getServiceContext(), INDICATOR4_UUID, INDICATOR4_VERSION);
    }
    
    @Test
    public void testPopulateIndicatorDataUuuidNull() throws Exception {
        try {
            indicatorsDataService.populateIndicatorData(getServiceContext(), null, "version");
            fail("parameter required");
        } catch(MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("INDICATOR_UUID", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
        
    }
    
    @Override
    protected String getDataSetFile() {
        return "dbunit/IndicatorsDataServiceTest.xml";
    }
    
    private void compareCollection(String[] expected, Collection<List<String>> collection) {
        List<String> values = new ArrayList<String>();
        for (List<String> vals : collection) {
            values.addAll(vals);
        }
        compareCollection(expected, values);
    }
    private void compareCollection(String[] expected, List<String> collection) {
        for (String elem: expected) {
            assertTrue(collection.contains(elem));
        }
        assertEquals(expected.length,collection.size());
    }
}
