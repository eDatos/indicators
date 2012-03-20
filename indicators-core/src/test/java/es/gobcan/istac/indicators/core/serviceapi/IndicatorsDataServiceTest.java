package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;

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

import es.gobcan.istac.indicators.core.domain.DataBasic;
import es.gobcan.istac.indicators.core.domain.DataStructure;
/**
 * Spring based transactional test with DbUnit support.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/include/indicators-data-service-mockito.xml","classpath:spring/applicationContext-test.xml"})
public class IndicatorsDataServiceTest extends IndicatorsBaseTest implements IndicatorsDataServiceTestBase {

    private static final String UUID_CONSULTA1 = "2d4887dc-52f0-4c17-abbb-ef1fdc62e868";
    private static final String JSON_CONSULTA1 = "{\"table\":\"Sociedades mercantiles que amplían capital Gran PX.\",\"uriPx\":\"urn:uuid:bf800d7a-53cd-49a9-a90e-da2f1be18f0e\",\"stub\":[\"Periodos\",\"Provincias por CC.AA.\"],\"heading\":[\"Naturaleza jurídica\",\"Indicadores\"],\"categories\":[{\"variable\":\"Periodos\",\"labels\":[\"2011 Enero (p)\",\"2010 TOTAL\",\"2010 Diciembre (p)\",\"2010 Noviembre (p)\",\"2010 Octubre (p)\",\"2010 Septiembre (p)\"],\"codes\":[\"2011M01\",\"2010\",\"2010M12\",\"2010M11\",\"2010M10\",\"2010M09\"]},{\"variable\":\"Provincias por CC.AA.\",\"labels\":[\"ESPAÑA\",\" ANDALUCÍA\",\"  Almería\",\"  Cádiz\",\"  Córdoba\",\"  Granada\",\"  Huelva\",\"  Jaén\",\"  Málaga\",\"  Sevilla\",\" ARAGÓN\",\"  Huesca\",\"  Teruel\",\"  Zaragoza\",\" ASTURIAS (PRINCIPADO DE)\",\" BALEARS (ILLES)\",\" CANARIAS\",\"  Palmas (Las)\",\"  Santa Cruz de Tenerife\",\" CANTABRIA\",\" CASTILLA Y LEÓN\",\"  Ávila\",\"  Burgos\",\"  León\",\"  Palencia\",\"  Salamanca\",\"  Segovia\",\"  Soria\",\"  Valladolid\",\"  Zamora\",\" CASTILLA-LA MANCHA\",\"  Albacete\",\"  Ciudad Real\",\"  Cuenca\",\"  Guadalajara\",\"  Toledo\",\" CATALUÑA\",\"  Barcelona\",\"  Girona\",\"  Lleida\",\"  Tarragona\",\" COMUNITAT VALENCIANA\",\"  Alicante/Alacant\",\"  Castellón/Castelló\",\"  Valencia/València\",\" EXTREMADURA\",\"  Badajoz\",\"  Cáceres\",\" GALICIA\",\"  Coruña (A)\",\"  Lugo\",\"  Ourense\",\"  Pontevedra\",\" MADRID (COMUNIDAD DE)\",\" MURCIA (REGIÓN DE)\",\" NAVARRA (COMUNIDAD FORAL DE)\",\" PAÍS VASCO\",\"  Álava\",\"  Guipúzcoa\",\"  Vizcaya\",\" RIOJA (LA)\",\" CEUTA\",\" MELILLA\"],\"codes\":[\"ES\",\"ES61\",\"ES611\",\"ES612\",\"ES613\",\"ES614\",\"ES615\",\"ES616\",\"ES617\",\"ES618\",\"ES24\",\"ES241\",\"ES242\",\"ES243\",\"ES12\",\"ES53\",\"ES70\",\"ES701\",\"ES702\",\"ES13\",\"ES41\",\"ES411\",\"ES412\",\"ES413\",\"ES414\",\"ES415\",\"ES416\",\"ES417\",\"ES418\",\"ES419\",\"ES42\",\"ES421\",\"ES422\",\"ES423\",\"ES424\",\"ES425\",\"ES51\",\"ES511\",\"ES512\",\"ES513\",\"ES514\",\"ES52\",\"ES521\",\"ES522\",\"ES523\",\"ES43\",\"ES431\",\"ES432\",\"ES11\",\"ES111\",\"ES112\",\"ES113\",\"ES114\",\"ES30\",\"ES62\",\"ES22\",\"ES21\",\"ES211\",\"ES212\",\"ES213\",\"ES23\",\"ES63\",\"ES64\"]},{\"variable\":\"Naturaleza jurídica\",\"labels\":[\"TOTAL DE SOCIEDADES\"],\"codes\":[\"T\"]},{\"variable\":\"Indicadores\",\"labels\":[\"Número de sociedades\"],\"codes\":[\"NumSoc\"]}],\"temporals\":[\"Periodos\"],\"spatials\":[],\"notes\":[\"(p) Dato provisional#(..) Dato no disponible#En Otras sociedades se incluyen:#Desde 2003 las Sociedades Comanditarias y Sociedades Colectivas.#Hasta 2002 las Sociedades de Responsabilidad Limitada y Sociedades Colectivas.#Hasta 1998 el capital suscrito se mide en millones de pesetas.\"],\"source\":\"Instituto Canario de Estadística (ISTAC) a partir de datos del Instituto Nacional de Estadística (INE).\",\"data\":[{\"Valor\":\"3.585\"},{\"Valor\":\"497\"},{\"Valor\":\"56\"},{\"Valor\":\"60\"},{\"Valor\":\"49\"},{\"Valor\":\"37\"},{\"Valor\":\"21\"},{\"Valor\":\"40\"},{\"Valor\":\"112\"},{\"Valor\":\"122\"},{\"Valor\":\"124\"},{\"Valor\":\"22\"},{\"Valor\":\"13\"},{\"Valor\":\"89\"},{\"Valor\":\"68\"},{\"Valor\":\"122\"},{\"Valor\":\"134\"},{\"Valor\":\"79\"},{\"Valor\":\"55\"},{\"Valor\":\"6\"},{\"Valor\":\"116\"},{\"Valor\":\"7\"},{\"Valor\":\"13\"},{\"Valor\":\"22\"},{\"Valor\":\"7\"},{\"Valor\":\"18\"},{\"Valor\":\"6\"},{\"Valor\":\"3\"},{\"Valor\":\"31\"},{\"Valor\":\"9\"},{\"Valor\":\"93\"},{\"Valor\":\"28\"},{\"Valor\":\"19\"},{\"Valor\":\"12\"},{\"Valor\":\"4\"},{\"Valor\":\"30\"},{\"Valor\":\"634\"},{\"Valor\":\"457\"},{\"Valor\":\"68\"},{\"Valor\":\"60\"},{\"Valor\":\"49\"},{\"Valor\":\"357\"},{\"Valor\":\"106\"},{\"Valor\":\"63\"},{\"Valor\":\"188\"},{\"Valor\":\"59\"},{\"Valor\":\"40\"},{\"Valor\":\"19\"},{\"Valor\":\"198\"},{\"Valor\":\"99\"},{\"Valor\":\"12\"},{\"Valor\":\"17\"},{\"Valor\":\"70\"},{\"Valor\":\"850\"},{\"Valor\":\"96\"},{\"Valor\":\"30\"},{\"Valor\":\"175\"},{\"Valor\":\"22\"},{\"Valor\":\"49\"},{\"Valor\":\"104\"},{\"Valor\":\"26\"},{\"Valor\":\"0\"},{\"Valor\":\"0\"},{\"Valor\":\"34.413\"},{\"Valor\":\"4.546\"},{\"Valor\":\"422\"},{\"Valor\":\"487\"},{\"Valor\":\"410\"},{\"Valor\":\"468\"},{\"Valor\":\"240\"},{\"Valor\":\"229\"},{\"Valor\":\"1.075\"},{\"Valor\":\"1.215\"},{\"Valor\":\"1.063\"},{\"Valor\":\"213\"},{\"Valor\":\"70\"},{\"Valor\":\"780\"},{\"Valor\":\"550\"},{\"Valor\":\"1.141\"},{\"Valor\":\"1.246\"},{\"Valor\":\"694\"},{\"Valor\":\"552\"},{\"Valor\":\"233\"},{\"Valor\":\"1.448\"},{\"Valor\":\"69\"},{\"Valor\":\"236\"},{\"Valor\":\"292\"},{\"Valor\":\"72\"},{\"Valor\":\"189\"},{\"Valor\":\"91\"},{\"Valor\":\"61\"},{\"Valor\":\"359\"},{\"Valor\":\"79\"},{\"Valor\":\"1.182\"},{\"Valor\":\"306\"},{\"Valor\":\"276\"},{\"Valor\":\"129\"},{\"Valor\":\"108\"},{\"Valor\":\"363\"},{\"Valor\":\"7.425\"},{\"Valor\":\"5.752\"},{\"Valor\":\"659\"},{\"Valor\":\"445\"},{\"Valor\":\"569\"},{\"Valor\":\"3.248\"},{\"Valor\":\"1.063\"},{\"Valor\":\"437\"},{\"Valor\":\"1.748\"},{\"Valor\":\"607\"},{\"Valor\":\"412\"},{\"Valor\":\"195\"},{\"Valor\":\"1.735\"},{\"Valor\":\"673\"},{\"Valor\":\"191\"},{\"Valor\":\"192\"},{\"Valor\":\"679\"},{\"Valor\":\"6.814\"},{\"Valor\":\"1.010\"},{\"Valor\":\"429\"},{\"Valor\":\"1.510\"},{\"Valor\":\"241\"},{\"Valor\":\"449\"},{\"Valor\":\"820\"},{\"Valor\":\"205\"},{\"Valor\":\"20\"},{\"Valor\":\"12\"},{\"Valor\":\"2.471\"},{\"Valor\":\"329\"},{\"Valor\":\"36\"},{\"Valor\":\"25\"},{\"Valor\":\"38\"},{\"Valor\":\"34\"},{\"Valor\":\"16\"},{\"Valor\":\"11\"},{\"Valor\":\"78\"},{\"Valor\":\"91\"},{\"Valor\":\"74\"},{\"Valor\":\"7\"},{\"Valor\":\"6\"},{\"Valor\":\"61\"},{\"Valor\":\"41\"},{\"Valor\":\"80\"},{\"Valor\":\"118\"},{\"Valor\":\"69\"},{\"Valor\":\"49\"},{\"Valor\":\"24\"},{\"Valor\":\"119\"},{\"Valor\":\"1\"},{\"Valor\":\"22\"},{\"Valor\":\"30\"},{\"Valor\":\"7\"},{\"Valor\":\"22\"},{\"Valor\":\"7\"},{\"Valor\":\"3\"},{\"Valor\":\"24\"},{\"Valor\":\"3\"},{\"Valor\":\"84\"},{\"Valor\":\"21\"},{\"Valor\":\"15\"},{\"Valor\":\"19\"},{\"Valor\":\"4\"},{\"Valor\":\"25\"},{\"Valor\":\"503\"},{\"Valor\":\"397\"},{\"Valor\":\"37\"},{\"Valor\":\"31\"},{\"Valor\":\"38\"},{\"Valor\":\"236\"},{\"Valor\":\"80\"},{\"Valor\":\"31\"},{\"Valor\":\"125\"},{\"Valor\":\"47\"},{\"Valor\":\"24\"},{\"Valor\":\"23\"},{\"Valor\":\"125\"},{\"Valor\":\"54\"},{\"Valor\":\"13\"},{\"Valor\":\"11\"},{\"Valor\":\"47\"},{\"Valor\":\"478\"},{\"Valor\":\"67\"},{\"Valor\":\"21\"},{\"Valor\":\"118\"},{\"Valor\":\"21\"},{\"Valor\":\"33\"},{\"Valor\":\"64\"},{\"Valor\":\"6\"},{\"Valor\":\"0\"},{\"Valor\":\"1\"},{\"Valor\":\"2.507\"},{\"Valor\":\"347\"},{\"Valor\":\"31\"},{\"Valor\":\"44\"},{\"Valor\":\"27\"},{\"Valor\":\"30\"},{\"Valor\":\"19\"},{\"Valor\":\"13\"},{\"Valor\":\"73\"},{\"Valor\":\"110\"},{\"Valor\":\"78\"},{\"Valor\":\"19\"},{\"Valor\":\"7\"},{\"Valor\":\"52\"},{\"Valor\":\"38\"},{\"Valor\":\"80\"},{\"Valor\":\"81\"},{\"Valor\":\"48\"},{\"Valor\":\"33\"},{\"Valor\":\"24\"},{\"Valor\":\"132\"},{\"Valor\":\"6\"},{\"Valor\":\"20\"},{\"Valor\":\"25\"},{\"Valor\":\"8\"},{\"Valor\":\"22\"},{\"Valor\":\"5\"},{\"Valor\":\"5\"},{\"Valor\":\"36\"},{\"Valor\":\"5\"},{\"Valor\":\"93\"},{\"Valor\":\"22\"},{\"Valor\":\"16\"},{\"Valor\":\"11\"},{\"Valor\":\"7\"},{\"Valor\":\"37\"},{\"Valor\":\"460\"},{\"Valor\":\"352\"},{\"Valor\":\"48\"},{\"Valor\":\"22\"},{\"Valor\":\"38\"},{\"Valor\":\"205\"},{\"Valor\":\"70\"},{\"Valor\":\"22\"},{\"Valor\":\"113\"},{\"Valor\":\"45\"},{\"Valor\":\"39\"},{\"Valor\":\"6\"},{\"Valor\":\"116\"},{\"Valor\":\"46\"},{\"Valor\":\"15\"},{\"Valor\":\"9\"},{\"Valor\":\"46\"},{\"Valor\":\"556\"},{\"Valor\":\"86\"},{\"Valor\":\"36\"},{\"Valor\":\"110\"},{\"Valor\":\"22\"},{\"Valor\":\"32\"},{\"Valor\":\"56\"},{\"Valor\":\"19\"},{\"Valor\":\"0\"},{\"Valor\":\"1\"},{\"Valor\":\"2.036\"},{\"Valor\":\"297\"},{\"Valor\":\"20\"},{\"Valor\":\"46\"},{\"Valor\":\"26\"},{\"Valor\":\"26\"},{\"Valor\":\"8\"},{\"Valor\":\"14\"},{\"Valor\":\"69\"},{\"Valor\":\"88\"},{\"Valor\":\"71\"},{\"Valor\":\"15\"},{\"Valor\":\"5\"},{\"Valor\":\"51\"},{\"Valor\":\"38\"},{\"Valor\":\"64\"},{\"Valor\":\"63\"},{\"Valor\":\"26\"},{\"Valor\":\"37\"},{\"Valor\":\"14\"},{\"Valor\":\"66\"},{\"Valor\":\"3\"},{\"Valor\":\"11\"},{\"Valor\":\"9\"},{\"Valor\":\"3\"},{\"Valor\":\"10\"},{\"Valor\":\"9\"},{\"Valor\":\"2\"},{\"Valor\":\"14\"},{\"Valor\":\"5\"},{\"Valor\":\"64\"},{\"Valor\":\"14\"},{\"Valor\":\"15\"},{\"Valor\":\"5\"},{\"Valor\":\"7\"},{\"Valor\":\"23\"},{\"Valor\":\"440\"},{\"Valor\":\"362\"},{\"Valor\":\"35\"},{\"Valor\":\"18\"},{\"Valor\":\"25\"},{\"Valor\":\"183\"},{\"Valor\":\"49\"},{\"Valor\":\"23\"},{\"Valor\":\"111\"},{\"Valor\":\"30\"},{\"Valor\":\"19\"},{\"Valor\":\"11\"},{\"Valor\":\"107\"},{\"Valor\":\"40\"},{\"Valor\":\"9\"},{\"Valor\":\"13\"},{\"Valor\":\"45\"},{\"Valor\":\"400\"},{\"Valor\":\"54\"},{\"Valor\":\"30\"},{\"Valor\":\"99\"},{\"Valor\":\"23\"},{\"Valor\":\"25\"},{\"Valor\":\"51\"},{\"Valor\":\"14\"},{\"Valor\":\"2\"},{\"Valor\":\"0\"},{\"Valor\":\"2.156\"},{\"Valor\":\"321\"},{\"Valor\":\"41\"},{\"Valor\":\"29\"},{\"Valor\":\"19\"},{\"Valor\":\"14\"},{\"Valor\":\"19\"},{\"Valor\":\"16\"},{\"Valor\":\"79\"},{\"Valor\":\"104\"},{\"Valor\":\"59\"},{\"Valor\":\"19\"},{\"Valor\":\"6\"},{\"Valor\":\"34\"},{\"Valor\":\"19\"},{\"Valor\":\"68\"},{\"Valor\":\"64\"},{\"Valor\":\"41\"},{\"Valor\":\"23\"},{\"Valor\":\"16\"},{\"Valor\":\"103\"},{\"Valor\":\"4\"},{\"Valor\":\"18\"},{\"Valor\":\"19\"},{\"Valor\":\"4\"},{\"Valor\":\"7\"},{\"Valor\":\"7\"},{\"Valor\":\"1\"},{\"Valor\":\"34\"},{\"Valor\":\"9\"},{\"Valor\":\"75\"},{\"Valor\":\"23\"},{\"Valor\":\"21\"},{\"Valor\":\"8\"},{\"Valor\":\"6\"},{\"Valor\":\"17\"},{\"Valor\":\"405\"},{\"Valor\":\"296\"},{\"Valor\":\"41\"},{\"Valor\":\"33\"},{\"Valor\":\"35\"},{\"Valor\":\"221\"},{\"Valor\":\"66\"},{\"Valor\":\"43\"},{\"Valor\":\"112\"},{\"Valor\":\"34\"},{\"Valor\":\"24\"},{\"Valor\":\"10\"},{\"Valor\":\"124\"},{\"Valor\":\"47\"},{\"Valor\":\"10\"},{\"Valor\":\"16\"},{\"Valor\":\"51\"},{\"Valor\":\"420\"},{\"Valor\":\"72\"},{\"Valor\":\"28\"},{\"Valor\":\"112\"},{\"Valor\":\"22\"},{\"Valor\":\"31\"},{\"Valor\":\"59\"},{\"Valor\":\"13\"},{\"Valor\":\"2\"},{\"Valor\":\"0\"}]}";
    
    @Autowired
    protected IndicatorsDataService indicatorsDataService;
    
    @Autowired
    private IndicatorsDataProviderService indicatorsDataProviderService;

    @Before
    public void initMock() throws MetamacException {
        doReturn(JSON_CONSULTA1).when(indicatorsDataProviderService).retrieveDataStructureJson(Matchers.any(ServiceContext.class), Matchers.same(UUID_CONSULTA1));
    }
    
    @Test
    @Override
    public void testFindDataDefinitions() throws Exception {
        List<DataBasic> dataDefs = indicatorsDataService.findDataDefinitions(getServiceContext());
        assertEquals(1, dataDefs.size());
        assertTrue(1 == dataDefs.get(0).getId());
    }

    @Test
    @Override
    public void testRetrieveDataStructure() throws Exception {
        DataStructure dataStruc = indicatorsDataService.retrieveDataStructure(getServiceContext(), UUID_CONSULTA1);
        assertEquals("Sociedades mercantiles que amplían capital Gran PX.",dataStruc.getTitle());
        assertEquals("urn:uuid:bf800d7a-53cd-49a9-a90e-da2f1be18f0e",dataStruc.getUriPx());
        
        String[] stubs = new String[] {"Periodos","Provincias por CC.AA."};
        compareCollection(stubs, dataStruc.getStub());
        
        String[] headings = new String[] {"Naturaleza jurídica","Indicadores"};
        compareCollection(headings, dataStruc.getHeading());

        String[] labels = new String[] {"2011 Enero (p)","2010 TOTAL","2010 Diciembre (p)","2010 Noviembre (p)","2010 Octubre (p)","2010 Septiembre (p)","ESPAÑA"," ANDALUCÍA","  Almería","  Cádiz","  Córdoba","  Granada","  Huelva","  Jaén","  Málaga","  Sevilla"," ARAGÓN","  Huesca","  Teruel","  Zaragoza"," ASTURIAS (PRINCIPADO DE)"," BALEARS (ILLES)"," CANARIAS","  Palmas (Las)","  Santa Cruz de Tenerife"," CANTABRIA"," CASTILLA Y LEÓN","  Ávila","  Burgos","  León","  Palencia","  Salamanca","  Segovia","  Soria","  Valladolid","  Zamora"," CASTILLA-LA MANCHA","  Albacete","  Ciudad Real","  Cuenca","  Guadalajara","  Toledo"," CATALUÑA","  Barcelona","  Girona","  Lleida","  Tarragona"," COMUNITAT VALENCIANA","  Alicante/Alacant","  Castellón/Castelló","  Valencia/València"," EXTREMADURA","  Badajoz","  Cáceres"," GALICIA","  Coruña (A)","  Lugo","  Ourense","  Pontevedra"," MADRID (COMUNIDAD DE)"," MURCIA (REGIÓN DE)"," NAVARRA (COMUNIDAD FORAL DE)"," PAÍS VASCO","  Álava","  Guipúzcoa","  Vizcaya"," RIOJA (LA)"," CEUTA"," MELILLA","TOTAL DE SOCIEDADES", "Número de sociedades"};
        compareCollection(labels, dataStruc.getCategoryLabels().values());
   		
        String[] codes = new String[] {"2011M01","2010","2010M12","2010M11","2010M10","2010M09","ES","ES61","ES611","ES612","ES613","ES614","ES615","ES616","ES617","ES618","ES24","ES241","ES242","ES243","ES12","ES53","ES70","ES701","ES702","ES13","ES41","ES411","ES412","ES413","ES414","ES415","ES416","ES417","ES418","ES419","ES42","ES421","ES422","ES423","ES424","ES425","ES51","ES511","ES512","ES513","ES514","ES52","ES521","ES522","ES523","ES43","ES431","ES432","ES11","ES111","ES112","ES113","ES114","ES30","ES62","ES22","ES21","ES211","ES212","ES213","ES23","ES63","ES64","T","NumSoc"};
        compareCollection(codes, dataStruc.getCategoryCodes().values());
        
        String[] temporals = new String[] {"Periodos"};
        compareCollection(temporals, dataStruc.getTemporals());
        
        String[] spatials = new String[] {};
        compareCollection(spatials, dataStruc.getSpatials());
        
        String[] notes = new String[] {"(p) Dato provisional#(..) Dato no disponible#En Otras sociedades se incluyen:#Desde 2003 las Sociedades Comanditarias y Sociedades Colectivas.#Hasta 2002 las Sociedades de Responsabilidad Limitada y Sociedades Colectivas.#Hasta 1998 el capital suscrito se mide en millones de pesetas."};
        compareCollection(notes, dataStruc.getNotes());
        
        assertEquals("Instituto Canario de Estadística (ISTAC) a partir de datos del Instituto Nacional de Estadística (INE).",dataStruc.getSource());
    }

    @Test
    @Override
    public void testRetrieveData() throws Exception {
        fail("not implemented");
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
