package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.oracle.OracleDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.dataset.repository.service.DatasetRepositoriesServiceFacade;

import es.gobcan.istac.indicators.core.domain.DataDefinition;
import es.gobcan.istac.indicators.core.domain.DataStructure;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

/**
 * Spring based transactional test with DbUnit support.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/include/indicators-data-service-mockito.xml", "classpath:spring/applicationContext-test.xml"})
@TransactionConfiguration(defaultRollback=false,transactionManager="txManager")
@Transactional
public class IndicatorsDataServiceDataGpeTest extends IndicatorsDataBaseTest {

    private static final String              CONSULTA1_UUID                           = "2d4887dc-52f0-4c17-abbb-ef1fdc62e868";
    private static final String              CONSULTA2_UUID                           = "1-1-1-1-1";
    private static final String              CONSULTA1_JSON_STRUC                     = readFile("json/structure_1.json");
    private static final String              CONSULTA3_UUID                           = "2-2-2-2-2";
    private static final String              CONSULTA4_UUID                           = "4-4-4-4-4";
    private static final String              CONSULTA4_JSON_STRUC                     = readFile("json/structure_wrong_3.json");

    /* Has geographic and time variables */
    private static final String              INDICATOR1_UUID                          = "Indicator-1";
    private static final String              INDICATOR1_DS_GPE_UUID                   = "Indicator-1-v1-DataSource-1-GPE-TIME-GEO";
    private static final String              INDICATOR1_GPE_JSON_DATA                 = readFile("json/data_temporal_spatials.json");
    private static final String              INDICATOR1_VERSION                       = "1.000";

    /* Has no geographic and temporal variables */
    private static final String              INDICATOR4_UUID                          = "Indicator-4";
    private static final String              INDICATOR4_DS_GPE_UUID                   = "Indicator-4-v1-DataSource-1-GPE-NOTIME-NOGEO";
    private static final String              INDICATOR4_GPE_JSON_DATA                 = readFile("json/data_fixed.json");
    private static final String              INDICATOR4_VERSION                       = "1.000";

    @Autowired
    protected IndicatorsDataService          indicatorsDataService;

    @Autowired
    private IndicatorsDataProviderService    indicatorsDataProviderService;

    @Autowired
    private DatasetRepositoriesServiceFacade datasetRepositoriesServiceFacade;

    @Autowired
    private IndicatorsService                indicatorsService;

    @Before
    public void initMock() throws MetamacException {
    }

    @Test
    public void testRetrieveDataDefinitions() throws Exception {
        List<DataDefinition> dataDefs = indicatorsDataService.retrieveDataDefinitions(getServiceContextAdministrador());
        assertEquals(1, dataDefs.size());
        assertTrue(1 == dataDefs.get(0).getId());
    }

    @Test
    public void testRetrieveDataDefinitionUuidNull() throws Exception {
        try {
            indicatorsDataService.retrieveDataDefinition(getServiceContextAdministrador(), null);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("UUID", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveDataDefinition() throws Exception {
        when(indicatorsDataProviderService.retrieveDataStructureJson(Matchers.any(ServiceContext.class), Matchers.eq(CONSULTA1_UUID))).thenReturn(CONSULTA1_JSON_STRUC);

        DataDefinition dataDef = indicatorsDataService.retrieveDataDefinition(getServiceContextAdministrador(), CONSULTA1_UUID);
        assertTrue(1 == dataDef.getId());
    }

    @Test
    public void testRetrieveDataDefinitionNotExist() throws Exception {
        try {
            indicatorsDataService.retrieveDataDefinition(getServiceContextAdministrador(), "NOT_EXIST");
            fail("Should throws an exception");
        } catch (MetamacException e) {
            assertNotNull(e.getExceptionItems());
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DATA_DEFINITION_RETRIEVE_ERROR.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testRetrieveDataDefinitionArchived() throws Exception {
        try {
            indicatorsDataService.retrieveDataDefinition(getServiceContextAdministrador(), CONSULTA2_UUID);
            fail("Should throws an exception");
        } catch (MetamacException e) {
            assertNotNull(e.getExceptionItems());
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DATA_DEFINITION_RETRIEVE_ERROR.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    /*
     * Some query exist but the date shows is no longer available
     */
    @Test
    public void testRetrieveDataDefinitionPublishedUnavailable() throws Exception {
        try {
            indicatorsDataService.retrieveDataDefinition(getServiceContextAdministrador(), CONSULTA3_UUID);
        } catch (MetamacException e) {
            assertNotNull(e.getExceptionItems());
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DATA_DEFINITION_RETRIEVE_ERROR.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }
    
    /*
     * Checking conversion from json to Data object
     * @see es.gobcan.istac.indicators.core.serviceapi.IndicatorsDataServiceTestBase#testRetrieveDataStructure()
     */
    @Test
    public void testRetrieveDataStructure() throws Exception {
        when(indicatorsDataProviderService.retrieveDataStructureJson(Matchers.any(ServiceContext.class), Matchers.eq(CONSULTA1_UUID))).thenReturn(CONSULTA1_JSON_STRUC);

        DataStructure dataStruc = indicatorsDataService.retrieveDataStructure(getServiceContextAdministrador(), CONSULTA1_UUID);
        assertEquals("Sociedades mercantiles que amplían capital Gran PX.", dataStruc.getTitle());
        assertEquals("urn:uuid:bf800d7a-53cd-49a9-a90e-da2f1be18f0e", dataStruc.getPxUri());

        String[] stubs = new String[]{"Periodos", "Provincias por CC.AA."};
        checkElementsInCollection(stubs, dataStruc.getStub());

        String[] headings = new String[]{"Naturaleza jurídica", "Indicadores"};
        checkElementsInCollection(headings, dataStruc.getHeading());

        String[] labels = new String[]{"2011 Enero (p)", "2010 TOTAL", "2010 Diciembre (p)", "2010 Noviembre (p)", "2010 Octubre (p)", "2010 Septiembre (p)", "ESPAÑA", " ANDALUCÍA", "  Almería",
                "  Cádiz", "  Córdoba", "  Granada", "  Huelva", "  Jaén", "  Málaga", "  Sevilla", " ARAGÓN", "  Huesca", "  Teruel", "  Zaragoza", " ASTURIAS (PRINCIPADO DE)", " BALEARS (ILLES)",
                " CANARIAS", "  Palmas (Las)", "  Santa Cruz de Tenerife", " CANTABRIA", " CASTILLA Y LEÓN", "  Ávila", "  Burgos", "  León", "  Palencia", "  Salamanca", "  Segovia", "  Soria",
                "  Valladolid", "  Zamora", " CASTILLA-LA MANCHA", "  Albacete", "  Ciudad Real", "  Cuenca", "  Guadalajara", "  Toledo", " CATALUÑA", "  Barcelona", "  Girona", "  Lleida",
                "  Tarragona", " COMUNITAT VALENCIANA", "  Alicante/Alacant", "  Castellón/Castelló", "  Valencia/València", " EXTREMADURA", "  Badajoz", "  Cáceres", " GALICIA", "  Coruña (A)",
                "  Lugo", "  Ourense", "  Pontevedra", " MADRID (COMUNIDAD DE)", " MURCIA (REGIÓN DE)", " NAVARRA (COMUNIDAD FORAL DE)", " PAÍS VASCO", "  Álava", "  Guipúzcoa", "  Vizcaya",
                " RIOJA (LA)", " CEUTA", " MELILLA", "TOTAL DE SOCIEDADES", "Número de sociedades"};
        checkElementsInCollection(labels, dataStruc.getValueLabels().values());

        String[] codes = new String[]{"2011M01", "2010", "2010M12", "2010M11", "2010M10", "2010M09", "ES", "ES61", "ES611", "ES612", "ES613", "ES614", "ES615", "ES616", "ES617", "ES618", "ES24",
                "ES241", "ES242", "ES243", "ES12", "ES53", "ES70", "ES701", "ES702", "ES13", "ES41", "ES411", "ES412", "ES413", "ES414", "ES415", "ES416", "ES417", "ES418", "ES419", "ES42", "ES421",
                "ES422", "ES423", "ES424", "ES425", "ES51", "ES511", "ES512", "ES513", "ES514", "ES52", "ES521", "ES522", "ES523", "ES43", "ES431", "ES432", "ES11", "ES111", "ES112", "ES113",
                "ES114", "ES30", "ES62", "ES22", "ES21", "ES211", "ES212", "ES213", "ES23", "ES63", "ES64", "T", "NumSoc"};
        checkElementsInCollection(codes, dataStruc.getValueCodes().values());

        String temporalVar = "Periodos";
        assertEquals(temporalVar, dataStruc.getTemporalVariable());

        String spatialVar = null;
        assertEquals(spatialVar, dataStruc.getSpatialVariable());

        String[] notes = new String[]{"(p) Dato provisional#(..) Dato no disponible#En Otras sociedades se incluyen:#Desde 2003 las Sociedades Comanditarias y Sociedades Colectivas.#Hasta 2002 las Sociedades de Responsabilidad Limitada y Sociedades Colectivas.#Hasta 1998 el capital suscrito se mide en millones de pesetas."};
        checkElementsInCollection(notes, dataStruc.getNotes());

        String source = "Instituto Canario de Estadística (ISTAC) a partir de datos del Instituto Nacional de Estadística (INE).";
        assertEquals(source, dataStruc.getSource());

        String surveyCode = "C00040A";
        assertEquals(surveyCode, dataStruc.getSurveyCode());

        String surveyTitle = "Estadística de Sociedades Mercantiles";
        assertEquals(surveyTitle, dataStruc.getSurveyTitle());

        String[] publishers = {"Instituto Canario de Estadística (ISTAC)"};
        checkElementsInCollection(publishers, dataStruc.getPublishers());

    }

    @Test
    public void testRetrieveDataStructureUuidNull() throws Exception {
        try {
            indicatorsDataService.retrieveDataStructure(getServiceContextAdministrador(), null);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("UUID", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveDataStructureRetrieveError() throws Exception {
        when(indicatorsDataProviderService.retrieveDataStructureJson(Matchers.any(ServiceContext.class), Matchers.eq(CONSULTA4_UUID))).thenReturn(CONSULTA4_JSON_STRUC);
        try {
            indicatorsDataService.retrieveDataStructure(getServiceContextAdministrador(), CONSULTA4_UUID);
            fail("Should throws a retrieve error exception");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DATA_STRUCTURE_RETRIEVE_ERROR.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(CONSULTA4_UUID, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }


    @Override
    protected String getDataSetFile() {
        return "dbunit/IndicatorsDataServiceTest_DataGpe.xml";
    }

    protected String getDataSetDSRepoFile() {
        return "dbunit/IndicatorsDataServiceTest_DataSetRepository.xml";
    }

    /* Utils for indicators base data */
    @Override
    protected IndicatorsService getIndicatorsService() {
        return indicatorsService;
    }

    @Override
    protected DatasetRepositoriesServiceFacade getDatasetRepositoriesServiceFacade() {
        return datasetRepositoriesServiceFacade;
    }

}
