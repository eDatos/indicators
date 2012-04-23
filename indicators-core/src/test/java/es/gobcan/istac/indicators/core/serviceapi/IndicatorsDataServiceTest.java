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

import com.arte.statistic.dataset.repository.service.DatasetRepositoriesServiceFacade;

import es.gobcan.istac.indicators.core.domain.DataDefinition;
import es.gobcan.istac.indicators.core.domain.DataStructure;
import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

/**
 * Spring based transactional test with DbUnit support.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/include/indicators-data-service-mockito.xml", "classpath:spring/applicationContext-test.xml"})
public class IndicatorsDataServiceTest extends IndicatorsDataBaseTest implements IndicatorsDataServiceTestBase {

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
    @Override
    public void testRetrieveDataDefinitions() throws Exception {
        List<DataDefinition> dataDefs = indicatorsDataService.retrieveDataDefinitions(getServiceContext());
        assertEquals(1, dataDefs.size());
        assertTrue(1 == dataDefs.get(0).getId());
    }

    @Test
    public void testRetrieveDataDefinitionUuidNull() throws Exception {
        try {
            indicatorsDataService.retrieveDataDefinition(getServiceContext(), null);
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
    public void testRetrieveDataDefinition() throws Exception {
        when(indicatorsDataProviderService.retrieveDataStructureJson(Matchers.any(ServiceContext.class), Matchers.eq(CONSULTA1_UUID))).thenReturn(CONSULTA1_JSON_STRUC);

        DataDefinition dataDef = indicatorsDataService.retrieveDataDefinition(getServiceContext(), CONSULTA1_UUID);
        assertTrue(1 == dataDef.getId());
    }

    @Test
    public void testRetrieveDataDefinitionNotExist() throws Exception {
        try {
            indicatorsDataService.retrieveDataDefinition(getServiceContext(), "NOT_EXIST");
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
            indicatorsDataService.retrieveDataDefinition(getServiceContext(), CONSULTA2_UUID);
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
            indicatorsDataService.retrieveDataDefinition(getServiceContext(), CONSULTA3_UUID);
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
    @Override
    public void testRetrieveDataStructure() throws Exception {
        when(indicatorsDataProviderService.retrieveDataStructureJson(Matchers.any(ServiceContext.class), Matchers.eq(CONSULTA1_UUID))).thenReturn(CONSULTA1_JSON_STRUC);

        DataStructure dataStruc = indicatorsDataService.retrieveDataStructure(getServiceContext(), CONSULTA1_UUID);
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
            indicatorsDataService.retrieveDataStructure(getServiceContext(), null);
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
            indicatorsDataService.retrieveDataStructure(getServiceContext(), CONSULTA4_UUID);
            fail("Should throws a retrieve error exception");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DATA_STRUCTURE_RETRIEVE_ERROR.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(CONSULTA4_UUID, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Override
    public void testRetrieveGeographicalGranularitiesInIndicator() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContext(), INDICATOR1_UUID, INDICATOR1_VERSION);
        
        List<GeographicalGranularity> granularities = indicatorsDataService.retrieveGeographicalGranularitiesInIndicator(getServiceContext(), INDICATOR1_UUID, INDICATOR1_VERSION);
        List<String> codes = getGeographicalGranularitiesCodes(granularities);
        String[] expectedCodes = new String[] {"COUNTRIES","COMMUNITIES","PROVINCES"};
        checkElementsInCollection(expectedCodes, codes);
    }
    
    @Test
    public void testRetrieveGeographicalGranularitiesInIndicatorFixedValue() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR4_DS_GPE_UUID))).thenReturn(INDICATOR4_GPE_JSON_DATA);
        
        indicatorsDataService.populateIndicatorData(getServiceContext(), INDICATOR4_UUID, INDICATOR4_VERSION);
        
        List<GeographicalGranularity> granularities = indicatorsDataService.retrieveGeographicalGranularitiesInIndicator(getServiceContext(), INDICATOR4_UUID, INDICATOR4_VERSION);
        List<String> codes = getGeographicalGranularitiesCodes(granularities);
        String[] expectedCodes = new String[] {"COUNTRIES"};
        checkElementsInCollection(expectedCodes, codes);
    }
    
    @Test
    @Override
    public void testRetrieveTimeValuesInIndicator() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContext(), INDICATOR1_UUID, INDICATOR1_VERSION);
        
        List<String> timeValues = indicatorsDataService.retrieveTimeValuesInIndicator(getServiceContext(), INDICATOR1_UUID, INDICATOR1_VERSION);
        String[] expectedValues = new String[] {"2011M01", "2010", "2010M12", "2010M11", "2010M10", "2010M09"};
        checkElementsInCollection(expectedValues, timeValues);
    }
    
    @Test
    public void testRetrieveTimeValuesInIndicatorFixedValue() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR4_DS_GPE_UUID))).thenReturn(INDICATOR4_GPE_JSON_DATA);
        
        indicatorsDataService.populateIndicatorData(getServiceContext(), INDICATOR4_UUID, INDICATOR4_VERSION);
        
        List<String> timeValues = indicatorsDataService.retrieveTimeValuesInIndicator(getServiceContext(), INDICATOR4_UUID, INDICATOR4_VERSION);
        String[] expectedValues = new String[] {"2010"};
        checkElementsInCollection(expectedValues, timeValues);
    }
    
    
    @Override
    public void testRetrieveGeographicalGranularitiesInIndicatorPublished() throws Exception {
        //TODO: pending
    }
    
    @Override
    public void testRetrieveGeographicalValuesWithGranularityInIndicator() throws Exception {
        //TODO: pending
    }
    
    @Override
    public void testPopulateIndicatorData() throws Exception {
        /* See IndicatorsDataServicePopulateTest.java */
        
    }
    
    @Override
    public void testUpdateIndicatorsData() throws Exception {
        /* See IndicatorsDataServiceBatchUpdateTest.java */
    }

    @Override
    public void testRetrieveGeographicalValuesWithGranularityInIndicatorPublished() throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    public void testRetrieveGeographicalValuesInIndicatorInstance() throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    public void testRetrieveTimeGranularitiesInIndicator() throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    public void testRetrieveTimeGranularitiesInIndicatorPublished() throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    public void testRetrieveTimeValuesWithGranularityInIndicator() throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    public void testRetrieveTimeValuesWithGranularityInIndicatorPublished() throws Exception {
        // TODO Auto-generated method stub
    }
    
    @Override
    public void testRetrieveTimeValuesInIndicatorPublished() throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    public void testRetrieveTimeValuesInIndicatorInstance() throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    public void testFindObservationsByDimensionsInIndicatorInstance() throws Exception {
        // TODO Auto-generated method stub

    }
    @Override
    public void testFindObservationsByDimensionsInIndicatorPublished() throws Exception {
        // TODO Auto-generated method stub
    }

    public void testFindObservationsExtendedByDimensionsInIndicatorInstance() throws Exception {
        // TODO Auto-generated method stub
    }

    public void testFindObservationsExtendedByDimensionsInIndicatorPublished() throws Exception {
        // TODO Auto-generated method stub
    }
    
    @Override
    public void testRetrieveGeographicalValuesInIndicator() throws Exception {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void testRetrieveGeographicalValuesInIndicatorPublished() throws Exception {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void testRetrieveMeasureValuesInIndicator() throws Exception {
        // TODO Auto-generated method stub
    }
    
    @Override
    public void testRetrieveMeasureValuesInIndicatorInstance() throws Exception {
        // TODO Auto-generated method stub
    }
    
    @Override
    public void testRetrieveMeasureValuesInIndicatorPublished() throws Exception {
        // TODO Auto-generated method stub
    }
    
    @Override
    public void testRetrieveGeographicalGranularitiesInIndicatorInstance() throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    public void testRetrieveTimeGranularitiesInIndicatorInstance() throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/IndicatorsDataServiceTest.xml";
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

    /*
     * DBUnit methods and helpers
     */
    private JdbcTemplate             jdbcTemplate;
    private DataSourceDatabaseTester databaseTester = null;

    @Autowired
    @Qualifier("dataSourceDatasetRepository")
    public void setDatasourceDSRepository(DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public void setUpDatabaseTester() throws Exception {
        super.setUpDatabaseTester();
        setUpDatabaseTester(getClass(), jdbcTemplate.getDataSource(), getDataSetDSRepoFile());
    }

    private void setUpDatabaseTester(Class<?> clazz, DataSource dataSource, String datasetFileName) throws Exception {
        // Setup database tester
        if (databaseTester == null) {
            databaseTester = new OracleDataSourceDatabaseTester(dataSource, dataSource.getConnection().getMetaData().getUserName());
        }

        // Create dataset
        ReplacementDataSet dataSetReplacement = new ReplacementDataSet((new FlatXmlDataSetBuilder()).build(clazz.getClassLoader().getResource(datasetFileName)));
        dataSetReplacement.addReplacementObject("[NULL]", null);
        dataSetReplacement.addReplacementObject("[null]", null);
        dataSetReplacement.addReplacementObject("[UNIQUE_SEQUENCE]", (new Date()).getTime());

        // DbUnit inserts and updates rows in the order they are found in your dataset. You must therefore order your tables and rows appropriately in your datasets to prevent foreign keys constraint
        // violation.
        // Since version 2.0, the DatabaseSequenceFilter can now be used to automatically determine the tables order using foreign/exported keys information.
        /*
         * ITableFilter filter = new DatabaseSequenceFilter(databaseTester.getConnection());
         * IDataSet dataset = new (filter, dataSetReplacement);
         */

        // Delete all data (dbunit not delete TBL_LOCALISED_STRINGS...)
        initializeDatabase(databaseTester.getConnection().getConnection());

        databaseTester.setSetUpOperation(DatabaseOperation.REFRESH);
        databaseTester.setTearDownOperation(DatabaseOperation.DELETE);
        databaseTester.setDataSet(dataSetReplacement);
        databaseTester.onSetup();
    }

    private void initializeDatabase(Connection connection) throws Exception {
        // Remove tables content
        List<String> tableNamesToDelete = getDSRepoTablesToDelete();
        for (String tableNameToDelete : tableNamesToDelete) {
            connection.prepareStatement("DELETE FROM " + tableNameToDelete).execute();
        }

        List<String> sequencesNamesToDrop = getDSRepoSequencesToDrop();
        for (String sequenceNameToDrop : sequencesNamesToDrop) {
            connection.prepareStatement("DROP SEQUENCE " + sequenceNameToDrop).execute();
        }

        List<String> tableNamesToDrop = getDSRepoTablesToDrop();
        for (String tableNameToDrop : tableNamesToDrop) {
            connection.prepareStatement("DROP TABLE " + tableNameToDrop).execute();
        }

        // Restart sequences
        List<String> sequences = getDSRepoSequencesToRestart();
        if (sequences != null) {
            for (String sequence : sequences) {
                restartSequence(databaseTester.getConnection(), sequence);
            }
        }
    }

    private List<String> getDSRepoTablesToDrop() throws Exception {
        List<String> dynamicTables = new ArrayList<String>();
        for (String tableName : databaseTester.getConnection().createDataSet().getTableNames()) {
            if (tableName.startsWith("DATA_")) {
                dynamicTables.add(tableName);
            }
        }
        return dynamicTables;
    }

    private List<String> getDSRepoSequencesToDrop() throws Exception {
        List<String> dynamicTables = new ArrayList<String>();
        for (String tableName : databaseTester.getConnection().createDataSet().getTableNames()) {
            if (tableName.startsWith("DATA_")) {
                String seqName = tableName.replaceFirst("DATA", "SEQ");
                dynamicTables.add(seqName);
            }
        }
        return dynamicTables;
    }

    private List<String> getDSRepoTablesToDelete() throws Exception {
        List<String> dynamicTables = new ArrayList<String>();
        for (String tableName : databaseTester.getConnection().createDataSet().getTableNames()) {
            if (tableName.startsWith("TB_")) {
                dynamicTables.add(tableName);
            }
        }
        return dynamicTables;
    }

    private List<String> getDSRepoSequencesToRestart() {
        List<String> sequences = new ArrayList<String>();
        sequences.add("SEQ_DATASETS");
        sequences.add("SEQ_DASET_DIMS");
        sequences.add("SEQ_ATTRIBUTES");
        sequences.add("SEQ_ATTR_DIMS");
        sequences.add("SEQ_I18NSTRS");
        sequences.add("SEQ_L10NSTRS");
        return sequences;
    }

    @Override
    public void tearDownDatabaseTester() throws Exception {
        super.tearDownDatabaseTester();
        if (databaseTester != null) {
            initializeDatabase(databaseTester.getConnection().getConnection());
            databaseTester.onTearDown();
        }
    }

    /**
     * DatasourceTester with support for Oracle data types.
     */

    private class OracleDataSourceDatabaseTester extends DataSourceDatabaseTester {

        public OracleDataSourceDatabaseTester(DataSource dataSource, String schema) {
            super(dataSource, schema);
        }

        @Override
        public IDatabaseConnection getConnection() throws Exception {
            IDatabaseConnection connection = super.getConnection();

            connection.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new OracleDataTypeFactory());
            return connection;
        }
    }
}
