package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.gobcan.istac.edatos.dataset.repository.service.DatasetRepositoriesServiceFacade;
import es.gobcan.istac.indicators.core.domain.DataContent;
import es.gobcan.istac.indicators.core.domain.DataDefinition;
import es.gobcan.istac.indicators.core.domain.DataStructure;
import es.gobcan.istac.indicators.core.domain.jsonstat.JsonStatData;
import es.gobcan.istac.indicators.core.error.ServiceExceptionParameters;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceimpl.util.JsonStatUtils;

/**
 * Spring based transactional test with DbUnit support.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/include/indicators-data-service-mockito.xml", "classpath:spring/applicationContext-test.xml"})
@TransactionConfiguration(defaultRollback = true, transactionManager = "txManager")
@Transactional
public class IndicatorsDataServiceDataGpeTest extends IndicatorsDataBaseTest {

    private static final String              CONSULTA1_UUID           = "1-1-1-1-1";
    private static final String              CONSULTA1_OPERATION_CODE = "E10352A";
    private static final String              CONSULTA1_JSON_STRUC     = readFile("json/structure_1.json");
    private static final String              CONSULTA2_UUID           = "2-2-2-2-2";
    private static final String              CONSULTA2_OPERATION_CODE = "E10352B";
    private static final String              CONSULTA3_UUID           = "3-3-3-3-3";
    private static final String              CONSULTA4_UUID           = "4-4-4-4-4";
    private static final String              CONSULTA4_JSON_STRUC     = readFile("json/structure_wrong_3.json");
    private static final String              CONSULTA4_OPERATION_CODE = "E10352D";
    private static final String              CONSULTA5_UUID           = "5-5-5-5-5";
    private static final String              CONSULTA5_OPERATION_CODE = "E10352E";

    private static final String              URL_JSON_STAT_1          = "https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/68904221-f893-456c-8046-261150ba9a2e/I208004_n100.json";
    private static final String              CONTENT_JSON_STAT_1      = readFile("json-stat/I208004_n100.json");
    private static final String              URL_JSON_STAT_2          = "https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/c2618af7-7b42-4510-b759-0f4ae59f41e6/E30138_16003.json";
    private static final String              CONTENT_JSON_STAT_2      = readFile("json-stat/E30138_16003.json");
    private static final String              URL_JSON_STAT_3          = "https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/c0058c2d-6a0e-4261-9629-db2595d32434/E30467_00018.json";
    private static final String              CONTENT_JSON_STAT_3      = readFile("json-stat/E30467_00018.json");
    private static final String              URL_JSON_STAT_4          = "https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/898714f0-bfa5-4dc8-b711-e6155b78bcee/I216019_0012.json";
    private static final String              CONTENT_JSON_STAT_4      = readFile("json-stat/I216019_0012.json");
    private static final String              URL_JSON_STAT_5          = "https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/898714f0-bfa5-4dc8-b711-e6155b78bcee/E30467_00012.json";
    private static final String              CONTENT_JSON_STAT_5      = readFile("json-stat/E30467_00012.json");
    private static final String              URL_JSON_STAT_6          = "https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/898714f0-bfa5-4dc8-b711-e6155b78bcee/I216019_0005.json";
    private static final String              CONTENT_JSON_STAT_6      = readFile("json-stat/I216019_0005.json");
    private static final String              URL_JSON_STAT_7          = "https://ibestat.caib.es/ibestat/service/ibestat/pxcontent/6/es/898714f0-bfa5-4dc8-b711-e6155b78bcee/E30162_02030.json";
    private static final String              CONTENT_JSON_STAT_7      = readFile("json-stat/E30162_02030.json");

    @Autowired
    protected IndicatorsDataService          indicatorsDataService;

    @Autowired
    private IndicatorsDataProviderService    indicatorsDataProviderService;

    @Autowired
    private DatasetRepositoriesServiceFacade datasetRepositoriesServiceFacade;

    @Autowired
    private IndicatorsService                indicatorsService;

    @Test
    public void testRetrieveDataDefinitionsOperationsCode() throws Exception {
        List<String> operationsCodes = indicatorsDataService.retrieveDataDefinitionsOperationsCodes(getServiceContextAdministrador());
        assertEquals(3, operationsCodes.size());
        assertEquals(operationsCodes.get(0), CONSULTA1_OPERATION_CODE);
        assertEquals(operationsCodes.get(1), CONSULTA4_OPERATION_CODE);
        assertEquals(operationsCodes.get(2), CONSULTA5_OPERATION_CODE);
    }

    @Test
    public void testFindDataDefinitionsByOperationCode() throws Exception {
        List<DataDefinition> dataDefs = indicatorsDataService.findDataDefinitionsByOperationCode(getServiceContextAdministrador(), CONSULTA1_OPERATION_CODE);
        assertEquals(1, dataDefs.size());
        assertEquals(CONSULTA1_UUID, dataDefs.get(0).getUuid());
    }

    @Test
    public void testFindDataDefinitionsByOperationCodeNotVisible() throws Exception {
        List<DataDefinition> dataDefs = indicatorsDataService.findDataDefinitionsByOperationCode(getServiceContextAdministrador(), CONSULTA2_OPERATION_CODE);
        assertEquals(0, dataDefs.size());
    }

    /*
     * Checking conversion from json to Data object
     * @see es.gobcan.istac.indicators.core.serviceapi.IndicatorsDataServiceTestBase#testRetrieveDataStructure()
     */
    @Test
    public void testRetrieveDataStructure() throws Exception {
        when(indicatorsDataProviderService.retrieveDataStructureJson(Matchers.any(ServiceContext.class), Matchers.eq(CONSULTA1_UUID))).thenReturn(CONSULTA1_JSON_STRUC);

        DataStructure dataStruc = indicatorsDataService.retrieveDataStructure(getServiceContextAdministrador(), CONSULTA1_UUID);
        assertEquals("Sociedades mercantiles que ampl??an capital Gran PX.", dataStruc.getTitle());
        assertEquals("urn:uuid:bf800d7a-53cd-49a9-a90e-da2f1be18f0e", dataStruc.getPxUri());
        assertEquals(CONSULTA1_UUID, dataStruc.getUuid());

        String[] stubs = new String[]{"Periodos", "Provincias por CC.AA."};
        checkElementsInCollection(stubs, dataStruc.getStub());

        String[] headings = new String[]{"Naturaleza jur??dica", "Indicadores"};
        checkElementsInCollection(headings, dataStruc.getHeading());

        String[] labels = new String[]{"2011 Enero (p)", "2010 TOTAL", "2010 Diciembre (p)", "2010 Noviembre (p)", "2010 Octubre (p)", "2010 Septiembre (p)", "ESPA??A", " ANDALUC??A", "  Almer??a",
                "  C??diz", "  C??rdoba", "  Granada", "  Huelva", "  Ja??n", "  M??laga", "  Sevilla", " ARAG??N", "  Huesca", "  Teruel", "  Zaragoza", " ASTURIAS (PRINCIPADO DE)", " BALEARS (ILLES)",
                " CANARIAS", "  Palmas (Las)", "  Santa Cruz de Tenerife", " CANTABRIA", " CASTILLA Y LE??N", "  ??vila", "  Burgos", "  Le??n", "  Palencia", "  Salamanca", "  Segovia", "  Soria",
                "  Valladolid", "  Zamora", " CASTILLA-LA MANCHA", "  Albacete", "  Ciudad Real", "  Cuenca", "  Guadalajara", "  Toledo", " CATALU??A", "  Barcelona", "  Girona", "  Lleida",
                "  Tarragona", " COMUNITAT VALENCIANA", "  Alicante/Alacant", "  Castell??n/Castell??", "  Valencia/Val??ncia", " EXTREMADURA", "  Badajoz", "  C??ceres", " GALICIA", "  Coru??a (A)",
                "  Lugo", "  Ourense", "  Pontevedra", " MADRID (COMUNIDAD DE)", " MURCIA (REGI??N DE)", " NAVARRA (COMUNIDAD FORAL DE)", " PA??S VASCO", "  ??lava", "  Guip??zcoa", "  Vizcaya",
                " RIOJA (LA)", " CEUTA", " MELILLA", "TOTAL DE SOCIEDADES", "N??mero de sociedades"};
        checkElementsInCollection(labels, dataStruc.getValueLabels().values());

        String[] codes = new String[]{"2011M01", "2010", "2010M12", "2010M11", "2010M10", "2010M09", "ES", "ES61", "ES611", "ES612", "ES613", "ES614", "ES615", "ES616", "ES617", "ES618", "ES24",
                "ES241", "ES242", "ES243", "ES12", "ES53", "ES70", "ES701", "ES702", "ES13", "ES41", "ES411", "ES412", "ES413", "ES414", "ES415", "ES416", "ES417", "ES418", "ES419", "ES42", "ES421",
                "ES422", "ES423", "ES424", "ES425", "ES51", "ES511", "ES512", "ES513", "ES514", "ES52", "ES521", "ES522", "ES523", "ES43", "ES431", "ES432", "ES11", "ES111", "ES112", "ES113", "ES114",
                "ES30", "ES62", "ES22", "ES21", "ES211", "ES212", "ES213", "ES23", "ES63", "ES64", "T", "NumSoc"};
        checkElementsInCollection(codes, dataStruc.getValueCodes().values());

        String temporalVar = "Periodos";
        assertEquals(temporalVar, dataStruc.getTemporalVariable());

        assertTrue(dataStruc.getSpatialVariables().isEmpty());

        String[] notes = new String[]{
                "(p) Dato provisional#(..) Dato no disponible#En Otras sociedades se incluyen:#Desde 2003 las Sociedades Comanditarias y Sociedades Colectivas.#Hasta 2002 las Sociedades de Responsabilidad Limitada y Sociedades Colectivas.#Hasta 1998 el capital suscrito se mide en millones de pesetas."};
        checkElementsInCollection(notes, dataStruc.getNotes());

        String source = "Instituto Canario de Estad??stica (ISTAC) a partir de datos del Instituto Nacional de Estad??stica (INE).";
        assertEquals(source, dataStruc.getSource());

        String surveyCode = "C00040A";
        assertEquals(surveyCode, dataStruc.getSurveyCode());

        String surveyTitle = "Estad??stica de Sociedades Mercantiles";
        assertEquals(surveyTitle, dataStruc.getSurveyTitle());

        String[] publishers = {"Instituto Canario de Estad??stica (ISTAC)"};
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
            assertEquals(ServiceExceptionParameters.UUID, e.getExceptionItems().get(0).getMessageParameters()[0]);
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

    @Test
    public void testRetrieveJsonStatData() throws Exception {
        {
            // I208004_n100.json"
            when(indicatorsDataProviderService.retrieveJsonStat(Matchers.any(ServiceContext.class), Matchers.eq(URL_JSON_STAT_1))).thenReturn(CONTENT_JSON_STAT_1);
            JsonStatData jsonStatData = indicatorsDataService.retrieveJsonStatData(getServiceContextAdministrador(), URL_JSON_STAT_1);
            assertEquals("Institut d'Estad??stica de les Illes Balears (IBESTAT) a partir de datos de EGATUR. Espa??a (CC BY 3.0)", jsonStatData.getSource());
            assertEquals("Gasto de los turistas extranjeros por periodo y Comunidad Aut??noma de destino principal.", jsonStatData.getLabel());
            assertEquals(4, jsonStatData.getDimension().size());

            assertEquals(6240, jsonStatData.getValue().size());
            assertTrue(jsonStatData.getValue().contains("."));
            assertTrue(jsonStatData.getStatus() != null && !jsonStatData.getStatus().isEmpty());
            assertEquals(48, jsonStatData.getStatus().size());

            assertTrue(jsonStatData.getDimension().containsKey("Periodo"));
            assertTrue(jsonStatData.getDimension().containsKey("Comunidad Aut??noma de destino"));
            assertTrue(jsonStatData.getDimension().containsKey("Datos"));
            assertTrue(jsonStatData.getDimension().containsKey("Gastos"));

            assertEquals("Periodo", jsonStatData.getDimension("Periodo").getLabel());
            assertEquals(Integer.valueOf(64), jsonStatData.getDimension("Periodo").getCategory().getIndex("2016M01"));
            assertEquals("2016M01", jsonStatData.getDimension("Periodo").getCategory().getLabel("2016M01"));
            assertNull(jsonStatData.getDimension("Periodo").getCategory().getUnit());

            assertEquals("Comunidad Aut??noma de destino", jsonStatData.getDimension("Comunidad Aut??noma de destino").getLabel());
            assertEquals(Integer.valueOf(7), jsonStatData.getDimension("Comunidad Aut??noma de destino").getCategory().getIndex("RR"));
            assertEquals("Resto CCAA", jsonStatData.getDimension("Comunidad Aut??noma de destino").getCategory().getLabel("RR"));
            assertNull(jsonStatData.getDimension("Comunidad Aut??noma de destino").getCategory().getUnit());

            assertEquals("Datos", jsonStatData.getDimension("Datos").getLabel());
            assertEquals(Integer.valueOf(3), jsonStatData.getDimension("Datos").getCategory().getIndex("Tvar_a"));
            assertEquals("Tasa de variaci??n del acumulado", jsonStatData.getDimension("Datos").getCategory().getLabel("Tvar_a"));
            assertNotNull(jsonStatData.getDimension("Datos").getCategory().getUnit());
            assertEquals(4, jsonStatData.getDimension("Datos").getCategory().getUnit().size());
            assertTrue(jsonStatData.getDimension("Datos").getCategory().getUnit().containsKey("abs"));
            assertTrue(jsonStatData.getDimension("Datos").getCategory().getUnit().containsKey("Tvar"));
            assertTrue(jsonStatData.getDimension("Datos").getCategory().getUnit().containsKey("Abs_a"));
            assertTrue(jsonStatData.getDimension("Datos").getCategory().getUnit().containsKey("Tvar_a"));

            assertEquals("Porcentaje", jsonStatData.getDimension("Datos").getCategory().getUnit("Tvar_a").getBase());
            assertEquals(Integer.valueOf(2), jsonStatData.getDimension("Datos").getCategory().getUnit("Tvar_a").getDecimals());

            assertEquals("I208004_n100", jsonStatData.getExtension().getDatasetId());
            assertEquals("es", jsonStatData.getExtension().getLang());
            assertEquals("Institut d'Estad??stica de les Illes Balears (IBESTAT)#Tel??fono: +34 971 784 575#Correo electr??nico: info@ibestat.caib.es#http://www.ibestat.cat",
                    jsonStatData.getExtension().getContact());
            assertEquals("Gasto de los turistas con destino principal las Illes Balears (EGATUR)", jsonStatData.getExtension().getSurvey());
            assertEquals("", jsonStatData.getExtension().getBasePeriod());
            assertEquals(2, jsonStatData.getExtension().getMetadata().size());
            assertEquals("Gasto de los turistas extranjeros por periodo y Comunidad Aut??noma de destino principal.", jsonStatData.getExtension().getMetadata().get(0).getTitle());
            assertEquals("http://www.ibestat.com/ibestat/service/ibestat/pxcontent/68904221-f893-456c-8046-261150ba9a2e/I208004_n100.px", jsonStatData.getExtension().getMetadata().get(0).getHref());
        }

        {
            // E30138_16003.json
            when(indicatorsDataProviderService.retrieveJsonStat(Matchers.any(ServiceContext.class), Matchers.eq(URL_JSON_STAT_2))).thenReturn(CONTENT_JSON_STAT_2);
            JsonStatData jsonStatData = indicatorsDataService.retrieveJsonStatData(getServiceContextAdministrador(), URL_JSON_STAT_2);
            assertEquals("Institut d'Estad??stica de les Illes Balears (IBESTAT) a partir de datos del Instituto Nacional de Estad??stica (INE). Espa??a (CC BY 3.0)", jsonStatData.getSource());
            assertEquals("??ndice de precios y tasas de variaci??n por periodo, Illes Balears/nacional y r??bricas.", jsonStatData.getLabel());
            assertEquals(4, jsonStatData.getDimension().size());

            assertEquals(106704, jsonStatData.getValue().size());
            assertTrue(jsonStatData.getStatus() != null && jsonStatData.getStatus().isEmpty());

            assertTrue(jsonStatData.getDimension().containsKey("Periodo"));
            assertTrue(jsonStatData.getDimension().containsKey("R??bricas"));
            assertTrue(jsonStatData.getDimension().containsKey("Datos"));
            assertTrue(jsonStatData.getDimension().containsKey("Illes Balears/nacional"));

            assertEquals("Periodo", jsonStatData.getDimension("Periodo").getLabel());
            assertEquals(Integer.valueOf(233), jsonStatData.getDimension("Periodo").getCategory().getIndex("2002M01"));
            assertEquals("2002M01", jsonStatData.getDimension("Periodo").getCategory().getLabel("2002M01"));
            assertNull(jsonStatData.getDimension("Periodo").getCategory().getUnit());

            assertEquals("R??bricas", jsonStatData.getDimension("R??bricas").getLabel());
            assertEquals(Integer.valueOf(56), jsonStatData.getDimension("R??bricas").getCategory().getIndex("57"));
            assertEquals("Otros bienes y servicios", jsonStatData.getDimension("R??bricas").getCategory().getLabel("57"));
            assertNull(jsonStatData.getDimension("R??bricas").getCategory().getUnit());

            assertEquals("Datos", jsonStatData.getDimension("Datos").getLabel());
            assertEquals(Integer.valueOf(1), jsonStatData.getDimension("Datos").getCategory().getIndex("varMens"));
            assertEquals("Variaci??n mensual", jsonStatData.getDimension("Datos").getCategory().getLabel("varMens"));
            assertNotNull(jsonStatData.getDimension("Datos").getCategory().getUnit());
            assertEquals(4, jsonStatData.getDimension("Datos").getCategory().getUnit().size());
            assertTrue(jsonStatData.getDimension("Datos").getCategory().getUnit().containsKey("indice"));
            assertTrue(jsonStatData.getDimension("Datos").getCategory().getUnit().containsKey("varMens"));
            assertTrue(jsonStatData.getDimension("Datos").getCategory().getUnit().containsKey("varAny"));
            assertTrue(jsonStatData.getDimension("Datos").getCategory().getUnit().containsKey("varActual"));

            assertEquals("Porcentaje", jsonStatData.getDimension("Datos").getCategory().getUnit("varActual").getBase());
            assertEquals(Integer.valueOf(1), jsonStatData.getDimension("Datos").getCategory().getUnit("varActual").getDecimals());

            assertEquals("E30138_16003", jsonStatData.getExtension().getDatasetId());
            assertEquals("es", jsonStatData.getExtension().getLang());
            assertEquals("Institut d'Estad??stica de les Illes Balears (IBESTAT)#Tel??fono: +34 971 784 575#Correo electr??nico: info@ibestat.caib.es#http://www.ibestat.cat",
                    jsonStatData.getExtension().getContact());
            assertEquals("??ndice de precios de consumo (IPC)", jsonStatData.getExtension().getSurvey());
            assertEquals("2016", jsonStatData.getExtension().getBasePeriod());
            assertEquals(2, jsonStatData.getExtension().getMetadata().size());
            assertEquals("??ndice de precios y tasas de variaci??n por periodo, Illes Balears/nacional y r??bricas.", jsonStatData.getExtension().getMetadata().get(0).getTitle());
            assertEquals("http://www.ibestat.com/ibestat/service/ibestat/pxcontent/c2618af7-7b42-4510-b759-0f4ae59f41e6/E30138_16003.px", jsonStatData.getExtension().getMetadata().get(0).getHref());
        }

        {
            // E30467_00018.json
            when(indicatorsDataProviderService.retrieveJsonStat(Matchers.any(ServiceContext.class), Matchers.eq(URL_JSON_STAT_3))).thenReturn(CONTENT_JSON_STAT_3);
            JsonStatData jsonStatData = indicatorsDataService.retrieveJsonStatData(getServiceContextAdministrador(), URL_JSON_STAT_3);
            assertEquals("Institut d'Estad??stica de les Illes Balears (IBESTAT) a partir de datos del Instituto Nacional de Estad??stica (INE). Espa??a (CC BY 3.0)", jsonStatData.getSource());
            assertEquals("Menores condenados por sexo, nacionalidad y a??o.", jsonStatData.getLabel());
            assertEquals(4, jsonStatData.getDimension().size());

            assertEquals(63, jsonStatData.getValue().size());
            assertTrue(jsonStatData.getStatus() != null && jsonStatData.getStatus().isEmpty());

            assertTrue(jsonStatData.getDimension().containsKey("Sexo"));
            assertTrue(jsonStatData.getDimension().containsKey("Nacionalidad"));
            assertTrue(jsonStatData.getDimension().containsKey("A??o"));
            assertTrue(jsonStatData.getDimension().containsKey("Datos"));

            assertEquals("Sexo", jsonStatData.getDimension("Sexo").getLabel());
            assertEquals(Integer.valueOf(0), jsonStatData.getDimension("Sexo").getCategory().getIndex("TT"));
            assertEquals("Ambos sexos", jsonStatData.getDimension("Sexo").getCategory().getLabel("TT"));
            assertNull(jsonStatData.getDimension("Sexo").getCategory().getUnit());

            assertEquals("Nacionalidad", jsonStatData.getDimension("Nacionalidad").getLabel());
            assertEquals(Integer.valueOf(2), jsonStatData.getDimension("Nacionalidad").getCategory().getIndex("2"));
            assertEquals("Extranjera", jsonStatData.getDimension("Nacionalidad").getCategory().getLabel("2"));
            assertNull(jsonStatData.getDimension("Nacionalidad").getCategory().getUnit());

            assertEquals("Datos", jsonStatData.getDimension("Datos").getLabel());
            assertEquals(Integer.valueOf(0), jsonStatData.getDimension("Datos").getCategory().getIndex("aux"));
            assertEquals("N??mero de personas", jsonStatData.getDimension("Datos").getCategory().getLabel("aux"));
            assertNotNull(jsonStatData.getDimension("Datos").getCategory().getUnit());
            assertEquals(1, jsonStatData.getDimension("Datos").getCategory().getUnit().size());
            assertTrue(jsonStatData.getDimension("Datos").getCategory().getUnit().containsKey("aux"));

            assertEquals("N??mero de personas", jsonStatData.getDimension("Datos").getCategory().getUnit("aux").getBase());
            assertEquals(Integer.valueOf(0), jsonStatData.getDimension("Datos").getCategory().getUnit("aux").getDecimals());

            assertEquals("E30467_00001", jsonStatData.getExtension().getDatasetId());
            assertEquals("es", jsonStatData.getExtension().getLang());
            assertEquals("Institut d'Estad??stica de les Illes Balears (IBESTAT)#Tel??fono: +34 971 784 575#Correo electr??nico: info@ibestat.caib.es#http://www.ibestat.cat",
                    jsonStatData.getExtension().getContact());
            assertEquals("Estad??stica de menores", jsonStatData.getExtension().getSurvey());
            assertEquals("", jsonStatData.getExtension().getBasePeriod());
            assertEquals(2, jsonStatData.getExtension().getMetadata().size());
            assertEquals("Menores condenados por sexo, nacionalidad y a??o.", jsonStatData.getExtension().getMetadata().get(0).getTitle());
            assertEquals("http://www.ibestat.com/ibestat/service/ibestat/pxcontent/c0058c2d-6a0e-4261-9629-db2595d32434/E30467_00001.px", jsonStatData.getExtension().getMetadata().get(0).getHref());
        }

        {
            // I216019_0012.json
            when(indicatorsDataProviderService.retrieveJsonStat(Matchers.any(ServiceContext.class), Matchers.eq(URL_JSON_STAT_4))).thenReturn(CONTENT_JSON_STAT_4);
            JsonStatData jsonStatData = indicatorsDataService.retrieveJsonStatData(getServiceContextAdministrador(), URL_JSON_STAT_4);
            assertEquals("Institut d'Estad??stica de les Illes Balears (IBESTAT) a partir de datos de la Seguridad Social. Espa??a (CC BY 3.0)", jsonStatData.getSource());
            assertEquals("Demograf??a de las empresas inscritas en la Seguridad Social por periodo e isla de localizaci??n de la empresa.", jsonStatData.getLabel());
            assertEquals(3, jsonStatData.getDimension().size());

            assertEquals(784, jsonStatData.getValue().size());
            assertTrue(jsonStatData.getStatus() != null && jsonStatData.getStatus().isEmpty());

            assertTrue(jsonStatData.getDimension().containsKey("Periodo"));
            assertTrue(jsonStatData.getDimension().containsKey("Isla de localizaci??n de la empresa"));
            assertTrue(jsonStatData.getDimension().containsKey("Datos"));

            assertEquals("Periodo", jsonStatData.getDimension("Periodo").getLabel());
            assertEquals(Integer.valueOf(48), jsonStatData.getDimension("Periodo").getCategory().getIndex("2009Q2"));
            assertEquals("2009T2", jsonStatData.getDimension("Periodo").getCategory().getLabel("2009Q2"));
            assertNull(jsonStatData.getDimension("Periodo").getCategory().getUnit());

            assertEquals("Isla de localizaci??n de la empresa", jsonStatData.getDimension("Isla de localizaci??n de la empresa").getLabel());
            assertEquals(Integer.valueOf(7), jsonStatData.getDimension("Isla de localizaci??n de la empresa").getCategory().getIndex("9"));
            assertEquals("OTRAS CC.AA.", jsonStatData.getDimension("Isla de localizaci??n de la empresa").getCategory().getLabel("9"));
            assertNull(jsonStatData.getDimension("Isla de localizaci??n de la empresa").getCategory().getUnit());

            assertEquals("Datos", jsonStatData.getDimension("Datos").getLabel());
            assertEquals(Integer.valueOf(0), jsonStatData.getDimension("Datos").getCategory().getIndex("nif_a"));
            assertEquals("Alta de empresas", jsonStatData.getDimension("Datos").getCategory().getLabel("nif_a"));
            assertNotNull(jsonStatData.getDimension("Datos").getCategory().getUnit());
            assertEquals(2, jsonStatData.getDimension("Datos").getCategory().getUnit().size());
            assertTrue(jsonStatData.getDimension("Datos").getCategory().getUnit().containsKey("nif_a"));
            assertTrue(jsonStatData.getDimension("Datos").getCategory().getUnit().containsKey("nif_b"));

            assertEquals("N??mero de empresas", jsonStatData.getDimension("Datos").getCategory().getUnit("nif_b").getBase());
            assertEquals(Integer.valueOf(0), jsonStatData.getDimension("Datos").getCategory().getUnit("nif_b").getDecimals());

            assertEquals("I216019_0012", jsonStatData.getExtension().getDatasetId());
            assertEquals("es", jsonStatData.getExtension().getLang());
            assertEquals("Institut d'Estad??stica de les Illes Balears (IBESTAT)#Tel??fono: +34 971 784 575#Correo electr??nico: info@ibestat.caib.es#http://www.ibestat.cat",
                    jsonStatData.getExtension().getContact());
            assertEquals("Demograf??a de las empresas inscritas en la Seguridad Social", jsonStatData.getExtension().getSurvey());
            assertEquals("", jsonStatData.getExtension().getBasePeriod());
            assertEquals(2, jsonStatData.getExtension().getMetadata().size());
            assertEquals("Demograf??a de las empresas inscritas en la Seguridad Social por periodo e isla de localizaci??n de la empresa.", jsonStatData.getExtension().getMetadata().get(0).getTitle());
            assertEquals("http://www.ibestat.com/ibestat/service/ibestat/pxcontent/898714f0-bfa5-4dc8-b711-e6155b78bcee/I216019_0012.px", jsonStatData.getExtension().getMetadata().get(0).getHref());
        }

    }

    @Test
    public void testJsonStatDataValuesToDataContent() throws Exception {
        {
            // E30467_00012.json
            when(indicatorsDataProviderService.retrieveJsonStat(Matchers.any(ServiceContext.class), Matchers.eq(URL_JSON_STAT_5))).thenReturn(CONTENT_JSON_STAT_5);
            JsonStatData jsonStatData = indicatorsDataService.retrieveJsonStatData(getServiceContextAdministrador(), URL_JSON_STAT_5);

            List<DataContent> dataContent = JsonStatUtils.jsonStatDataValuesToDataContent(jsonStatData);

            assertEquals(jsonStatData.getValue().size(), dataContent.size());

            {
                String expectedValue = "1021";

                List<DataContent> selectedDataContent = searchDataContentByValue(dataContent, expectedValue);

                assertEquals(1, selectedDataContent.size());
                assertEquals(expectedValue, selectedDataContent.get(0).getValue());
                assertEquals(jsonStatData.getId().size(), selectedDataContent.get(0).getDimCodes().size());
                assertTrue(selectedDataContent.get(0).getDimCodes().contains("TT")); // Ambos sexos
                assertTrue(selectedDataContent.get(0).getDimCodes().contains("2019")); // A??o
                assertTrue(selectedDataContent.get(0).getDimCodes().contains("TT")); // TOTAL
            }

            {
                String expectedValue = "40";

                List<DataContent> selectedDataContent = searchDataContentByValue(dataContent, expectedValue);

                assertEquals(1, selectedDataContent.size());
                assertEquals(expectedValue, selectedDataContent.get(0).getValue());
                assertEquals(jsonStatData.getId().size(), selectedDataContent.get(0).getDimCodes().size());
                assertTrue(selectedDataContent.get(0).getDimCodes().contains("6")); // Mujer
                assertTrue(selectedDataContent.get(0).getDimCodes().contains("2016")); // A??o
                assertTrue(selectedDataContent.get(0).getDimCodes().contains("13")); // Realizaci??n de tareas socio-educativas
            }

            {
                String expectedValue = "99";

                List<DataContent> selectedDataContent = searchDataContentByValue(dataContent, expectedValue);

                assertEquals(1, selectedDataContent.size());
                assertEquals(expectedValue, selectedDataContent.get(0).getValue());
                assertEquals(jsonStatData.getId().size(), selectedDataContent.get(0).getDimCodes().size());
                assertTrue(selectedDataContent.get(0).getDimCodes().contains("6")); // Mujer
                assertTrue(selectedDataContent.get(0).getDimCodes().contains("2017")); // A??o
                assertTrue(selectedDataContent.get(0).getDimCodes().contains("8")); // Libertad vigilada
            }

            {
                String expectedValue = "129";

                List<DataContent> selectedDataContent = searchDataContentByValue(dataContent, expectedValue);

                assertEquals(1, selectedDataContent.size());
                assertEquals(expectedValue, selectedDataContent.get(0).getValue());
                assertEquals(jsonStatData.getId().size(), selectedDataContent.get(0).getDimCodes().size());
                assertTrue(selectedDataContent.get(0).getDimCodes().contains("1")); // Hombre
                assertTrue(selectedDataContent.get(0).getDimCodes().contains("2018")); // A??o
                assertTrue(selectedDataContent.get(0).getDimCodes().contains("6")); // Internamiento semiabierto
            }

            {
                String expectedValue = "22";

                List<DataContent> selectedDataContent = searchDataContentByValue(dataContent, expectedValue);

                assertEquals(1, selectedDataContent.size());
                assertEquals(expectedValue, selectedDataContent.get(0).getValue());
                assertEquals(jsonStatData.getId().size(), selectedDataContent.get(0).getDimCodes().size());
                assertTrue(selectedDataContent.get(0).getDimCodes().contains("1")); // Hombre
                assertTrue(selectedDataContent.get(0).getDimCodes().contains("2018")); // A??o
                assertTrue(selectedDataContent.get(0).getDimCodes().contains("2")); // Amonestaci??n
            }
        }

        {
            // I216019_0005.json
            when(indicatorsDataProviderService.retrieveJsonStat(Matchers.any(ServiceContext.class), Matchers.eq(URL_JSON_STAT_6))).thenReturn(CONTENT_JSON_STAT_6);
            JsonStatData jsonStatData = indicatorsDataService.retrieveJsonStatData(getServiceContextAdministrador(), URL_JSON_STAT_6);

            List<DataContent> dataContent = JsonStatUtils.jsonStatDataValuesToDataContent(jsonStatData);

            assertEquals(jsonStatData.getValue().size(), dataContent.size());

            {
                String expectedValue = "20732";

                List<DataContent> selectedDataContent = searchDataContentByValue(dataContent, expectedValue);

                assertEquals(1, selectedDataContent.size());
                assertEquals(expectedValue, selectedDataContent.get(0).getValue());
                assertEquals(jsonStatData.getId().size(), selectedDataContent.get(0).getDimCodes().size());
                assertTrue(selectedDataContent.get(0).getDimCodes().contains("TT")); // TOTAL
                assertTrue(selectedDataContent.get(0).getDimCodes().contains("2021Q2")); // 2021T2
                assertTrue(selectedDataContent.get(0).getDimCodes().contains("trab_1")); // Mantienen el n??mero de trabajadores
            }

            {
                String expectedValue = "9328";

                List<DataContent> selectedDataContent = searchDataContentByValue(dataContent, expectedValue);

                assertEquals(1, selectedDataContent.size());
                assertEquals(expectedValue, selectedDataContent.get(0).getValue());
                assertEquals(jsonStatData.getId().size(), selectedDataContent.get(0).getDimCodes().size());
                assertTrue(selectedDataContent.get(0).getDimCodes().contains("TT")); // (A) AGRICULTURA, GANADER??A, SILVICULTURA Y PESCA
                assertTrue(selectedDataContent.get(0).getDimCodes().contains("2019Q4")); // 2019T4
                assertTrue(selectedDataContent.get(0).getDimCodes().contains("trab_2")); // Reducen trabajadores
            }

            {
                String expectedValue = "1202";

                List<DataContent> selectedDataContent = searchDataContentByValue(dataContent, expectedValue);

                assertEquals(1, selectedDataContent.size());
                assertEquals(expectedValue, selectedDataContent.get(0).getValue());
                assertEquals(jsonStatData.getId().size(), selectedDataContent.get(0).getDimCodes().size());
                assertTrue(selectedDataContent.get(0).getDimCodes().contains("G47")); // (47) Comercio al por menor, excepto de veh??culos de motor y motocicletas
                assertTrue(selectedDataContent.get(0).getDimCodes().contains("2009Q2")); // 2009T2
                assertTrue(selectedDataContent.get(0).getDimCodes().contains("trab_3")); // Incrementan trabajadores
            }

            {
                String expectedValue = "2014";

                List<DataContent> selectedDataContent = searchDataContentByValue(dataContent, expectedValue);

                assertEquals(1, selectedDataContent.size());
                assertEquals(expectedValue, selectedDataContent.get(0).getValue());
                assertEquals(jsonStatData.getId().size(), selectedDataContent.get(0).getDimCodes().size());
                assertTrue(selectedDataContent.get(0).getDimCodes().contains("G")); // G) COMERCIO AL POR MAYOR Y AL POR MENOR; REPARACI??N DE VEH??CULOS DE MOTOR Y MOTOCICLETAS
                assertTrue(selectedDataContent.get(0).getDimCodes().contains("2012Q2")); // 2012T2
                assertTrue(selectedDataContent.get(0).getDimCodes().contains("trab_3")); // Incrementan trabajadores
            }
        }

        {
            // E30162_02030.json
            when(indicatorsDataProviderService.retrieveJsonStat(Matchers.any(ServiceContext.class), Matchers.eq(URL_JSON_STAT_7))).thenReturn(CONTENT_JSON_STAT_7);
            JsonStatData jsonStatData = indicatorsDataService.retrieveJsonStatData(getServiceContextAdministrador(), URL_JSON_STAT_7);

            List<DataContent> dataContent = JsonStatUtils.jsonStatDataValuesToDataContent(jsonStatData);

            assertEquals(jsonStatData.getValue().size(), dataContent.size());

            {
                String expectedValue = "....";

                List<DataContent> selectedDataContent = searchDataContentByValue(dataContent, expectedValue);

                assertEquals(1001, selectedDataContent.size());
            }

            {
                String expectedValue = "10.9"; // Watch out! The value in the file is 10.90

                List<DataContent> selectedDataContent = searchDataContentByValue(dataContent, expectedValue);

                assertEquals(1, selectedDataContent.size());
                assertEquals(expectedValue, selectedDataContent.get(0).getValue());
                assertEquals(jsonStatData.getId().size(), selectedDataContent.get(0).getDimCodes().size());
                assertTrue(selectedDataContent.get(0).getDimCodes().contains("gopa")); // Grau d'ocupaci?? per parcel??les
                assertTrue(selectedDataContent.get(0).getDimCodes().contains("2021M05")); // 2021M05
            }

            {
                String expectedValue = "14865";

                List<DataContent> selectedDataContent = searchDataContentByValue(dataContent, expectedValue);

                assertEquals(1, selectedDataContent.size());
                assertEquals(expectedValue, selectedDataContent.get(0).getValue());
                assertEquals(jsonStatData.getId().size(), selectedDataContent.get(0).getDimCodes().size());
                assertTrue(selectedDataContent.get(0).getDimCodes().contains("paro")); // Parcel??les ocupades
                assertTrue(selectedDataContent.get(0).getDimCodes().contains("2017M09")); // 2017M09
            }

            {
                String expectedValue = "1095";

                List<DataContent> selectedDataContent = searchDataContentByValue(dataContent, expectedValue);

                assertEquals(1, selectedDataContent.size());
                assertEquals(expectedValue, selectedDataContent.get(0).getValue());
                assertEquals(jsonStatData.getId().size(), selectedDataContent.get(0).getDimCodes().size());
                assertTrue(selectedDataContent.get(0).getDimCodes().contains("npla")); // Nombre de places
                assertTrue(selectedDataContent.get(0).getDimCodes().contains("2001M05")); // 2001M05
            }

            {
                String expectedValue = "7.71";

                List<DataContent> selectedDataContent = searchDataContentByValue(dataContent, expectedValue);

                assertEquals(1, selectedDataContent.size());
                assertEquals(expectedValue, selectedDataContent.get(0).getValue());
                assertEquals(jsonStatData.getId().size(), selectedDataContent.get(0).getDimCodes().size());
                assertTrue(selectedDataContent.get(0).getDimCodes().contains("gopa")); // Grau d'ocupaci?? per parcel??les
                assertTrue(selectedDataContent.get(0).getDimCodes().contains("1999M06")); // 2001M05
            }

        }
    }

    private List<DataContent> searchDataContentByValue(List<DataContent> dataContent, String expectedValue) {
        return (List<DataContent>) CollectionUtils.select(dataContent, new Predicate() {

            @Override
            public boolean evaluate(Object object) {
                return expectedValue.equals(((DataContent) object).getValue());
            }
        });
    }

    private List<String> getDataDefinitionsUuids(Collection<DataDefinition> dataDefinitions) {
        List<String> uuids = new ArrayList<String>();
        for (DataDefinition dataDef : dataDefinitions) {
            uuids.add(dataDef.getUuid());
        }
        return uuids;
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/IndicatorsDataServiceTest_DataGpe.xml";
    }

    @Override
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
