package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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

import com.arte.statistic.dataset.repository.dto.AttributeBasicDto;
import com.arte.statistic.dataset.repository.dto.CodeDimensionDto;
import com.arte.statistic.dataset.repository.dto.ConditionDimensionDto;
import com.arte.statistic.dataset.repository.dto.ConditionObservationDto;
import com.arte.statistic.dataset.repository.dto.InternationalStringDto;
import com.arte.statistic.dataset.repository.dto.LocalisedStringDto;
import com.arte.statistic.dataset.repository.dto.ObservationDto;
import com.arte.statistic.dataset.repository.dto.ObservationExtendedDto;
import com.arte.statistic.dataset.repository.service.DatasetRepositoriesServiceFacade;
import com.arte.statistic.dataset.repository.util.DtoUtils;

import es.gobcan.istac.indicators.core.domain.DataDefinition;
import es.gobcan.istac.indicators.core.domain.DataStructure;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.enume.domain.MeasureDimensionTypeEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsAsserts;
import es.gobcan.istac.indicators.core.serviceimpl.IndicatorsDataServiceImpl;

/**
 * Spring based transactional test with DbUnit support.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/include/indicators-data-service-mockito.xml", "classpath:spring/applicationContext-test.xml"})
public class IndicatorsDataServiceTest extends IndicatorsBaseTest implements IndicatorsDataServiceTestBase {

    private static final String              CONSULTA1_UUID           = "2d4887dc-52f0-4c17-abbb-ef1fdc62e868";
    private static final String              CONSULTA2_UUID           = "1-1-1-1-1";
    private static final String              CONSULTA3_UUID           = "2-2-2-2-2";
    private static final String              CONSULTA1_JSON_STRUC     = readFile("json/query_structure_1.json");

    private static final String              INDICATOR1_UUID          = "Indicator-1";
    private static final String              INDICATOR1_DS_GPE_UUID   = "Indicator-1-v1-DataSource-1-GPE-TIME-GEO";
    private static final String              INDICATOR1_GPE_JSON_DATA = readFile("json/query_data_temporal_spatials.json");
    private static final String              INDICATOR1_VERSION       = "1.000";

    private static final String              INDICATOR2_UUID          = "Indicator-2";
    private static final String              INDICATOR2_DS_GPE_UUID   = "Indicator-2-v1-DataSource-1-GPE-GEO";
    private static final String              INDICATOR2_GPE_JSON_DATA = readFile("json/query_data_spatials.json");
    private static final String              INDICATOR2_VERSION       = "1.000";

    private static final String              INDICATOR3_UUID          = "Indicator-3";
    private static final String              INDICATOR3_DS_UUID       = "Indicator-3-v1-DataSource-1";
    private static final String              INDICATOR3_DS_GPE_UUID   = "Indicator-3-v1-DataSource-1-GPE-TIME";
    private static final String              INDICATOR3_GPE_JSON_DATA = readFile("json/query_data_temporals.json");
    private static final String              INDICATOR3_VERSION       = "1.000";

    private static final String              INDICATOR4_UUID          = "Indicator-4";
    private static final String              INDICATOR4_DS_GPE_UUID   = "Indicator-4-v1-DataSource-1-GPE-NOTIME-NOGEO";
    private static final String              INDICATOR4_GPE_JSON_DATA = readFile("json/query_data_fixed.json");
    private static final String              INDICATOR4_VERSION       = "1.000";

    private static final String              INDICATOR5_UUID          = "Indicator-5";
    private static final String              INDICATOR5_DS_GPE_UUID   = "Indicator-5-v1-DataSource-1-GPE-NOTIME-NOGEO-CONTVARIABLE";
    private static final String              INDICATOR5_GPE_JSON_DATA = readFile("json/query_data_fixed_contvariable.json");
    private static final String              INDICATOR5_VERSION       = "1.000";

    private static final String              INDICATOR6_UUID          = "Indicator-6";
    private static final String              INDICATOR6_DS_GPE_UUID   = "Indicator-6-v1-DataSource-1-GPE-DOTS";
    private static final String              INDICATOR6_GPE_JSON_DATA = readFile("json/query_data_dots.json");
    private static final String              INDICATOR6_VERSION       = "1.000";

    private static final String              INDICATOR7_UUID          = "Indicator-7";
    private static final String              INDICATOR7_DS_GPE_UUID   = "Indicator-7-v1-DataSource-1-GPE-TIME";
    private static final String              INDICATOR7_GPE_JSON_DATA = readFile("json/query_data_temporals_calculate.json");
    private static final String              INDICATOR7_VERSION       = "1.000";
    
    private static final String              INDICATOR8_UUID          = "Indicator-8";
    private static final String              INDICATOR8_DS1_GPE_UUID   = "Indicator-8-v1-DataSource-1-GPE-TIME";
    private static final String              INDICATOR8_DS2_GPE_UUID   = "Indicator-8-v1-DataSource-2-GPE-TIME";
    private static final String              INDICATOR8_GPE_JSON_DATA1 = readFile("json/query_data_increase_temporal_part1.json");
    private static final String              INDICATOR8_GPE_JSON_DATA2 = readFile("json/query_data_increase_temporal_part2.json");
    private static final String              INDICATOR8_VERSION       = "1.000";

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
        when(indicatorsDataProviderService.retrieveDataStructureJson(Matchers.any(ServiceContext.class), Matchers.eq(CONSULTA1_UUID))).thenReturn(CONSULTA1_JSON_STRUC);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR2_DS_GPE_UUID))).thenReturn(INDICATOR2_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR3_DS_GPE_UUID))).thenReturn(INDICATOR3_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR4_DS_GPE_UUID))).thenReturn(INDICATOR4_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR5_DS_GPE_UUID))).thenReturn(INDICATOR5_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR6_DS_GPE_UUID))).thenReturn(INDICATOR6_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR7_DS_GPE_UUID))).thenReturn(INDICATOR7_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR8_DS1_GPE_UUID))).thenReturn(INDICATOR8_GPE_JSON_DATA1);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR8_DS2_GPE_UUID))).thenReturn(INDICATOR8_GPE_JSON_DATA2);
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

    @Test
    @Override
    public void testRetrieveDataStructure() throws Exception {
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

        String[] temporals = new String[]{"Periodos"};
        checkElementsInCollection(temporals, dataStruc.getTemporals());

        String[] spatials = new String[]{};
        checkElementsInCollection(spatials, dataStruc.getSpatials());

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
    @Override
    public void testPopulateIndicatorData() throws Exception {
        indicatorsDataService.populateIndicatorData(getServiceContext(), INDICATOR1_UUID, INDICATOR1_VERSION);
        Map<String, List<String>> dimensionCodes = new HashMap<String, List<String>>();
        dimensionCodes.put(IndicatorsDataServiceImpl.TIME_DIMENSION, Arrays.asList("2011M01", "2010", "2010M12", "2010M11", "2010M10", "2010M09"));
        dimensionCodes.put(IndicatorsDataServiceImpl.GEO_DIMENSION, Arrays.asList("ES", "ES61", "ES611", "ES612", "ES613"));
        dimensionCodes.put(IndicatorsDataServiceImpl.MEASURE_DIMENSION, Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE.name()));

        checkDataDimensions(dimensionCodes, INDICATOR1_UUID, INDICATOR1_VERSION);
        List<String> data = Arrays.asList("3.585", "497", "56", "60", "49", "34.413", "4.546", "422", "487", "410", "2.471", "329", "36", "25", "38", "2.507", "347", "31", "44", "27", "2.036", "297",
                "20", "46", "26", "2.156", "321", "41", "29", "19");
        checkDataObservations(dimensionCodes, INDICATOR1_UUID, INDICATOR1_VERSION, data);
    }

    @Test
    public void testPopulateIndicatorDataSpatialVariableTemporalValue() throws Exception {
        indicatorsDataService.populateIndicatorData(getServiceContext(), INDICATOR2_UUID, INDICATOR2_VERSION);
        Map<String, List<String>> dimensionCodes = new HashMap<String, List<String>>();
        dimensionCodes.put(IndicatorsDataServiceImpl.TIME_DIMENSION, Arrays.asList("2010"));
        dimensionCodes.put(IndicatorsDataServiceImpl.GEO_DIMENSION, Arrays.asList("ES", "ES61", "ES611", "ES612", "ES613"));
        dimensionCodes.put(IndicatorsDataServiceImpl.MEASURE_DIMENSION, Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE.name()));

        checkDataDimensions(dimensionCodes, INDICATOR2_UUID, INDICATOR2_VERSION);
        List<String> data = Arrays.asList("3.585", "497", "56", "60", "49");
        checkDataObservations(dimensionCodes, INDICATOR2_UUID, INDICATOR2_VERSION, data);
    }

    @Test
    public void testPopulateIndicatorDataSpatialValueTemporalVariable() throws Exception {
        indicatorsDataService.populateIndicatorData(getServiceContext(), INDICATOR3_UUID, INDICATOR3_VERSION);
        Map<String, List<String>> dimensionCodes = new HashMap<String, List<String>>();
        dimensionCodes.put(IndicatorsDataServiceImpl.TIME_DIMENSION, Arrays.asList("2011M01", "2010", "2010M12", "2010M11", "2010M10", "2010M09"));
        dimensionCodes.put(IndicatorsDataServiceImpl.GEO_DIMENSION, Arrays.asList("ES"));
        dimensionCodes.put(IndicatorsDataServiceImpl.MEASURE_DIMENSION, Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE.name()));

        checkDataDimensions(dimensionCodes, INDICATOR3_UUID, INDICATOR3_VERSION);
        List<String> data = Arrays.asList("3.585", "34.413", "2.471", "2.507", "2.036", "2.156");
        checkDataObservations(dimensionCodes, INDICATOR3_UUID, INDICATOR3_VERSION, data);
    }

    @Test
    public void testPopulateIndicatorDataSpatialValueTemporalValue() throws Exception {
        indicatorsDataService.populateIndicatorData(getServiceContext(), INDICATOR4_UUID, INDICATOR4_VERSION);
        Map<String, List<String>> dimensionCodes = new HashMap<String, List<String>>();
        dimensionCodes.put(IndicatorsDataServiceImpl.TIME_DIMENSION, Arrays.asList("2010"));
        dimensionCodes.put(IndicatorsDataServiceImpl.GEO_DIMENSION, Arrays.asList("ES"));
        dimensionCodes.put(IndicatorsDataServiceImpl.MEASURE_DIMENSION, Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE.name()));

        checkDataDimensions(dimensionCodes, INDICATOR4_UUID, INDICATOR4_VERSION);
        List<String> data = Arrays.asList("3.585");
        checkDataObservations(dimensionCodes, INDICATOR4_UUID, INDICATOR4_VERSION, data);
    }

    @Test
    public void testPopulateIndicatorDataContVariable() throws Exception {
        indicatorsDataService.populateIndicatorData(getServiceContext(), INDICATOR5_UUID, INDICATOR5_VERSION);
        Map<String, List<String>> dimensionCodes = new HashMap<String, List<String>>();
        dimensionCodes.put(IndicatorsDataServiceImpl.TIME_DIMENSION, Arrays.asList("2010"));
        dimensionCodes.put(IndicatorsDataServiceImpl.GEO_DIMENSION, Arrays.asList("ES"));
        dimensionCodes.put(IndicatorsDataServiceImpl.MEASURE_DIMENSION, Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE.name()));

        checkDataDimensions(dimensionCodes, INDICATOR5_UUID, INDICATOR5_VERSION);
        List<String> data = Arrays.asList("3.585");
        checkDataObservations(dimensionCodes, INDICATOR5_UUID, INDICATOR5_VERSION, data);
    }

    @Test
    public void testPopulateIndicatorDataDots() throws Exception {
        indicatorsDataService.populateIndicatorData(getServiceContext(), INDICATOR6_UUID, INDICATOR6_VERSION);
        Map<String, List<String>> dimensionCodes = new HashMap<String, List<String>>();
        dimensionCodes.put(IndicatorsDataServiceImpl.TIME_DIMENSION, Arrays.asList("2011M01", "2010", "2010M12", "2010M11", "2010M10", "2010M09"));
        dimensionCodes.put(IndicatorsDataServiceImpl.GEO_DIMENSION, Arrays.asList("ES", "ES61", "ES611", "ES612", "ES613"));
        dimensionCodes.put(IndicatorsDataServiceImpl.MEASURE_DIMENSION, Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE.name()));

        checkDataDimensions(dimensionCodes, INDICATOR6_UUID, INDICATOR6_VERSION);
        List<String> data = Arrays.asList("3.585", "497", "56", "60", "49", null, null, null, null, null, null, "329", "36", "25", "38", "2.507", "347", "31", "44", "27", "2.036", "297", "20", "46",
                "26", "2.156", "321", "41", "29", "19");
        checkDataObservations(dimensionCodes, INDICATOR6_UUID, INDICATOR6_VERSION, data);

        Map<String, AttributeBasicDto> mapAttributes = new HashMap<String, AttributeBasicDto>();
        {
            String key = generateObservationUniqueKey("ES", "2010", MeasureDimensionTypeEnum.ABSOLUTE.name());
            mapAttributes.put(key, createAttribute(IndicatorsDataServiceImpl.OBS_CONF_ATTR, IndicatorsDataServiceImpl.OBS_CONF_LOC, "No procede"));
        }
        {
            String key = generateObservationUniqueKey("ES61", "2010", MeasureDimensionTypeEnum.ABSOLUTE.name());
            mapAttributes.put(key, createAttribute(IndicatorsDataServiceImpl.OBS_CONF_ATTR, IndicatorsDataServiceImpl.OBS_CONF_LOC, "Dato no disponible"));
        }
        {
            String key = generateObservationUniqueKey("ES611", "2010", MeasureDimensionTypeEnum.ABSOLUTE.name());
            mapAttributes.put(key, createAttribute(IndicatorsDataServiceImpl.OBS_CONF_ATTR, IndicatorsDataServiceImpl.OBS_CONF_LOC, "Dato oculto por impreciso o baja calidad"));
        }
        {
            String key = generateObservationUniqueKey("ES612", "2010", MeasureDimensionTypeEnum.ABSOLUTE.name());
            mapAttributes.put(key, createAttribute(IndicatorsDataServiceImpl.OBS_CONF_ATTR, IndicatorsDataServiceImpl.OBS_CONF_LOC, "Dato oculto por secreto estadístico"));
        }
        {
            String key = generateObservationUniqueKey("ES613", "2010", MeasureDimensionTypeEnum.ABSOLUTE.name());
            mapAttributes.put(key, createAttribute(IndicatorsDataServiceImpl.OBS_CONF_ATTR, IndicatorsDataServiceImpl.OBS_CONF_LOC, "Dato incluido en otra categoría"));
        }
        {
            String key = generateObservationUniqueKey("ES", "2010M12", MeasureDimensionTypeEnum.ABSOLUTE.name());
            mapAttributes.put(key, createAttribute(IndicatorsDataServiceImpl.OBS_CONF_ATTR, IndicatorsDataServiceImpl.OBS_CONF_LOC, "Dato no disponible por vacaciones o festivos"));
        }

        checkDataAttributes(dimensionCodes, INDICATOR6_UUID, INDICATOR6_VERSION, IndicatorsDataServiceImpl.OBS_CONF_ATTR, mapAttributes);
    }

    @Test
    public void testPopulateIndicatorDataCodeAttribute() throws Exception {
        indicatorsDataService.populateIndicatorData(getServiceContext(), INDICATOR3_UUID, INDICATOR3_VERSION);
        Map<String, List<String>> dimensionCodes = new HashMap<String, List<String>>();

        dimensionCodes.put(IndicatorsDataServiceImpl.TIME_DIMENSION, Arrays.asList("2011M01", "2010", "2010M12", "2010M11", "2010M10", "2010M09"));
        dimensionCodes.put(IndicatorsDataServiceImpl.GEO_DIMENSION, Arrays.asList("ES"));
        dimensionCodes.put(IndicatorsDataServiceImpl.MEASURE_DIMENSION, Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE.name()));
        List<String> data = Arrays.asList("3.585", "34.413", "2.471", "2.507", "2.036", "2.156");
        checkDataDimensions(dimensionCodes, INDICATOR3_UUID, INDICATOR3_VERSION);
        checkDataObservations(dimensionCodes, INDICATOR3_UUID, INDICATOR3_VERSION, data);

        Map<String, AttributeBasicDto> mapAttributes = new HashMap<String, AttributeBasicDto>();
        // All observations must have CODE attribute
        for (String geoValue : dimensionCodes.get(IndicatorsDataServiceImpl.GEO_DIMENSION)) {
            for (String timeValue : dimensionCodes.get(IndicatorsDataServiceImpl.TIME_DIMENSION)) {
                for (String measureValue : dimensionCodes.get(IndicatorsDataServiceImpl.MEASURE_DIMENSION)) {
                    String key = generateObservationUniqueKey(geoValue, timeValue, measureValue);
                    mapAttributes.put(key, createAttribute(IndicatorsDataServiceImpl.CODE_ATTR, IndicatorsDataServiceImpl.CODE_ATTR_LOC, INDICATOR3_DS_UUID));
                }
            }
        }

        checkDataAttributes(dimensionCodes, INDICATOR3_UUID, INDICATOR3_VERSION, IndicatorsDataServiceImpl.CODE_ATTR, mapAttributes);
    }

    @Test
    public void testPopulateIndicatorDataCalculate() throws Exception {
        indicatorsDataService.populateIndicatorData(getServiceContext(), INDICATOR7_UUID, INDICATOR7_VERSION);
        Map<String, List<String>> dimensionCodes = new HashMap<String, List<String>>();

        dimensionCodes.put(IndicatorsDataServiceImpl.TIME_DIMENSION,
                Arrays.asList("2010", "2010M12", "2010M11", "2010M10","2009", "2009M12", "2009M11"));
        dimensionCodes.put(IndicatorsDataServiceImpl.GEO_DIMENSION, Arrays.asList("ES"));
        dimensionCodes.put(IndicatorsDataServiceImpl.MEASURE_DIMENSION, Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE.name(),
                                                                                      MeasureDimensionTypeEnum.ANNUAL_PERCENTAGE_RATE.name(),
                                                                                      MeasureDimensionTypeEnum.INTERPERIOD_PERCENTAGE_RATE.name(),
                                                                                      MeasureDimensionTypeEnum.ANNUAL_PUNTUAL_RATE.name(),
                                                                                      MeasureDimensionTypeEnum.INTERPERIOD_PUNTUAL_RATE.name()));
        /* ABSOLUTE */
        List<String> absolute = Arrays.asList("34.413", "2.471", "2.507", "2.036", "30.413", "1.952", "1.925");
        List<String> annualPercentageRate = Arrays.asList("13,152", "26,588", "30,234", null, null, null, null);
        List<String> interperiodPercentageRate = Arrays.asList("13,15", "-1,44", "23,13", null, null,"1,40",null);
        List<String> annualPuntualRate = Arrays.asList("4.000", "519", "582", null, null, null, null);
        List<String> interperiodPuntualRate = Arrays.asList("4.000", "-36", "471", null, null, "27",null);
        
        List<String> data = new ArrayList<String>();
        data.addAll(absolute);
        data.addAll(annualPercentageRate);
        data.addAll(interperiodPercentageRate);
        data.addAll(annualPuntualRate);
        data.addAll(interperiodPuntualRate);
        
        checkDataDimensions(dimensionCodes, INDICATOR7_UUID, INDICATOR7_VERSION);
        checkDataObservations(dimensionCodes, INDICATOR7_UUID, INDICATOR7_VERSION, data);
    }
    
    @Test
    public void testPopulateIndicatorDataMultiDataSource() throws Exception {
        indicatorsDataService.populateIndicatorData(getServiceContext(), INDICATOR8_UUID, INDICATOR8_VERSION);
        Map<String, List<String>> dimensionCodes = new HashMap<String, List<String>>();

        dimensionCodes.put(IndicatorsDataServiceImpl.TIME_DIMENSION,
                Arrays.asList("2010", "2010M12", "2010M11", "2010M10","2009", "2009M12", "2009M11", "2009M10"));
        dimensionCodes.put(IndicatorsDataServiceImpl.GEO_DIMENSION, Arrays.asList("ES","ES61"));
        dimensionCodes.put(IndicatorsDataServiceImpl.MEASURE_DIMENSION, Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE.name(),
                                                                                      MeasureDimensionTypeEnum.ANNUAL_PERCENTAGE_RATE.name(),
                                                                                      MeasureDimensionTypeEnum.INTERPERIOD_PERCENTAGE_RATE.name(),
                                                                                      MeasureDimensionTypeEnum.ANNUAL_PUNTUAL_RATE.name(),
                                                                                      MeasureDimensionTypeEnum.INTERPERIOD_PUNTUAL_RATE.name()));
        //DATA
        List<String> absolute = Arrays.asList("34.413", "4.546", "2.471", "329", "2.507", "347","2.036", "297",
                                              "33.413", "3.546", "1.471", "229", "1.507", "247", "1.036", "197");
        List<String> annualPercentageRate = Arrays.asList("2,993", "28,201", "67,981", "43,668", "66,357", "40,486", "96,525", "50,761", 
                                                         null, null, null, null,null, null, null, null);
        List<String> interperiodPercentageRate = Arrays.asList("2,99", "28,20", "-1,44", "-5,19", "23,13", "16,84", null, null,
                                                                null, null, "-2,39", "-7,29", "45,46", "25,38", null,null);
        List<String> annualPuntualRate = Arrays.asList("1.000", "1.000", "1.000", "100", "1.000", "100", "1.000", "100",
                                                        null, null, null, null, null, null, null, null);
        List<String> interperiodPuntualRate = Arrays.asList("1.000", "1.000", "-36", "-18", "471", "50", null, null,
                                                            null, null, "-36", "-18", "471", "50", null, null);
        
        List<String> data = new ArrayList<String>();
        data.addAll(absolute);
        data.addAll(annualPercentageRate);
        data.addAll(interperiodPercentageRate);
        data.addAll(annualPuntualRate);
        data.addAll(interperiodPuntualRate);
        
        checkDataDimensions(dimensionCodes, INDICATOR8_UUID, INDICATOR8_VERSION);
        checkDataObservations(dimensionCodes, INDICATOR8_UUID, INDICATOR8_VERSION, data);
    }

    @Test
    public void testPopulateIndicatorDataUuuidNull() throws Exception {
        try {
            indicatorsDataService.populateIndicatorData(getServiceContext(), null, "version");
            fail("parameter required");
        } catch (MetamacException e) {
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

    protected String getDataSetDSRepoFile() {
        return "dbunit/IndicatorsDataServiceTest_DataSetRepository.xml";
    }

    private void checkElementsInCollection(String[] expected, Collection<List<String>> collection) {
        List<String> values = new ArrayList<String>();
        for (List<String> vals : collection) {
            values.addAll(vals);
        }
        checkElementsInCollection(expected, values);
    }
    private void checkElementsInCollection(String[] expected, List<String> collection) {
        for (String elem : expected) {
            assertTrue(collection.contains(elem));
        }
        assertEquals(expected.length, collection.size());
    }

    private void checkDataDimensions(Map<String, List<String>> dimCodes, String indicatorUuid, String indicatorVersion) throws Exception {
        IndicatorVersion indicator = indicatorsService.retrieveIndicator(getServiceContext(), indicatorUuid, indicatorVersion);
        assertNotNull(indicator);
        assertNotNull(indicator.getDataRepositoryId());
        List<ConditionObservationDto> conditionObservationsList = datasetRepositoriesServiceFacade.findCodeDimensions(indicator.getDataRepositoryId());
        // Retrieve all dataset's code dimensions
        Map<String, List<String>> repoDimCodes = new HashMap<String, List<String>>();
        for (ConditionObservationDto condObs : conditionObservationsList) {
            for (CodeDimensionDto codeDim : condObs.getCodesDimension()) {
                List<String> codes = repoDimCodes.get(codeDim.getDimensionId());
                if (codes == null) {
                    codes = new ArrayList<String>();
                }
                codes.add(codeDim.getCodeDimensionId());
                repoDimCodes.put(codeDim.getDimensionId(), codes);
            }
        }
        // Compare with expected code dimensions
        for (String dimension : dimCodes.keySet()) {
            List<String> dataSetCodes = repoDimCodes.get(dimension);
            assertNotNull(dataSetCodes);
            assertNotNull(dimCodes.get(dimension));
            Set<String> dimCodesSet = new HashSet<String>(dimCodes.get(dimension));
            Set<String> dataSetCodesSet = new HashSet<String>(dataSetCodes);
            assertEquals(dimCodesSet, dataSetCodesSet);
        }

        assertEquals(dimCodes.keySet().size(), repoDimCodes.keySet().size());
    }

    private void checkDataObservations(Map<String, List<String>> dimCodes, String indicatorUuid, String indicatorVersion, List<String> plainData) throws Exception {
        IndicatorVersion indicator = indicatorsService.retrieveIndicator(getServiceContext(), indicatorUuid, indicatorVersion);
        assertNotNull(indicator);
        assertNotNull(indicator.getDataRepositoryId());
        int index = 0;
        for (String measureCode : dimCodes.get(IndicatorsDataServiceImpl.MEASURE_DIMENSION)) {
            for (String timeCode : dimCodes.get(IndicatorsDataServiceImpl.TIME_DIMENSION)) {
                for (String geoCode : dimCodes.get(IndicatorsDataServiceImpl.GEO_DIMENSION)) {
                    ConditionObservationDto condition = new ConditionObservationDto();
                    condition.addCodesDimension(new CodeDimensionDto(IndicatorsDataServiceImpl.TIME_DIMENSION, timeCode));
                    condition.addCodesDimension(new CodeDimensionDto(IndicatorsDataServiceImpl.GEO_DIMENSION, geoCode));
                    condition.addCodesDimension(new CodeDimensionDto(IndicatorsDataServiceImpl.MEASURE_DIMENSION, measureCode));

                    Map<String, ObservationDto> observations = datasetRepositoriesServiceFacade.findObservations(indicator.getDataRepositoryId(), Arrays.asList(condition));
                    assertNotNull(observations);
                    assertEquals(1, observations.keySet().size());
                    List<ObservationDto> observationsList = new ArrayList<ObservationDto>(observations.values());
                    assertEquals(plainData.get(index), observationsList.get(0).getPrimaryMeasure());
                    index++;
                }
            }
        }
    }

    private void checkDataAttributes(Map<String, List<String>> dimCodes, String indicatorUuid, String indicatorVersion, String attrId, Map<String, AttributeBasicDto> expectedAttributes) throws Exception {
        IndicatorVersion indicator = indicatorsService.retrieveIndicator(getServiceContext(), indicatorUuid, indicatorVersion);
        assertNotNull(indicator);
        assertNotNull(indicator.getDataRepositoryId());

        ConditionDimensionDto geoCondition = createCondition(IndicatorsDataServiceImpl.GEO_DIMENSION, dimCodes.get(IndicatorsDataServiceImpl.GEO_DIMENSION));
        ConditionDimensionDto timeCondition = createCondition(IndicatorsDataServiceImpl.TIME_DIMENSION, dimCodes.get(IndicatorsDataServiceImpl.TIME_DIMENSION));
        ConditionDimensionDto measureCondition = createCondition(IndicatorsDataServiceImpl.MEASURE_DIMENSION, dimCodes.get(IndicatorsDataServiceImpl.MEASURE_DIMENSION));

        Map<String, ObservationExtendedDto> observations = datasetRepositoriesServiceFacade.findObservationsExtendedByDimensions(indicator.getDataRepositoryId(),
                Arrays.asList(geoCondition, timeCondition, measureCondition));

        for (Entry<String, ObservationExtendedDto> entry : observations.entrySet()) {
            String key = entry.getKey();
            ObservationExtendedDto obs = entry.getValue();

            AttributeBasicDto expectedAttr = expectedAttributes.get(key);
            AttributeBasicDto actualAttr = findAttribute(obs, attrId);

            IndicatorsAsserts.assertEqualsAttributeBasic(expectedAttr, actualAttr);
        }
    }

    private AttributeBasicDto findAttribute(ObservationExtendedDto observation, String attrId) {
        for (AttributeBasicDto attr : observation.getAttributes()) {
            if (attrId.equals(attr.getAttributeId())) {
                return attr;
            }
        }
        return null;
    }

    /*
     * Helper for generate id for observation maps
     */
    private String generateObservationUniqueKey(String geoValue, String timeValue, String measureValue) {
        CodeDimensionDto geoDimCode = new CodeDimensionDto(IndicatorsDataServiceImpl.GEO_DIMENSION, geoValue);
        CodeDimensionDto timeDimCode = new CodeDimensionDto(IndicatorsDataServiceImpl.TIME_DIMENSION, timeValue);
        CodeDimensionDto measureDimCode = new CodeDimensionDto(IndicatorsDataServiceImpl.MEASURE_DIMENSION, measureValue);
        return DtoUtils.generateUniqueKey(Arrays.asList(geoDimCode, timeDimCode, measureDimCode));
    }

    /*
     * Helper for attributes
     */
    private AttributeBasicDto createAttribute(String id, String locale, String value) {
        AttributeBasicDto attributeBasicDto = new AttributeBasicDto();
        attributeBasicDto.setAttributeId(id);
        InternationalStringDto intStr = new InternationalStringDto();
        LocalisedStringDto locStr = new LocalisedStringDto();
        locStr.setLocale(locale);
        locStr.setLabel(value);
        intStr.addText(locStr);
        attributeBasicDto.setValue(intStr);
        return attributeBasicDto;
    }

    /*
     * Helper for ConditionDimension
     */
    private ConditionDimensionDto createCondition(String dimensionId, List<String> codeDimensions) {
        ConditionDimensionDto condition = new ConditionDimensionDto();
        condition.setDimensionId(dimensionId);
        condition.setCodesDimension(codeDimensions);
        return condition;
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
