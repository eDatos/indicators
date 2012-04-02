package es.gobcan.istac.indicators.core.serviceapi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.siemac.metamac.common.test.MetamacBaseTests;

public abstract class IndicatorsBaseTest extends MetamacBaseTests {

    @Override
    public void setUpDatabaseTester() throws Exception {
        removeCyclicDependences();
        super.setUpDatabaseTester();
    }
    
    @Override
    public void tearDownDatabaseTester() throws Exception {
        removeCyclicDependences();
        super.tearDownDatabaseTester();
    }
    
    @Override
    protected List<String> getTablesToRemoveContent() {
        List<String> tables = new ArrayList<String>();
        tables.add("TB_LOCALISED_STRINGS");
        tables.add("TB_ELEMENTS_LEVELS");
        tables.add("TB_INDICATORS_INSTANCES");
        tables.add("TB_DIMENSIONS");
        tables.add("TB_INDIC_SYSTEMS_VERSIONS");
        tables.add("TB_INDICATORS_SYSTEMS");
        tables.add("TB_DATA_SOURCES_VARIABLES");
        tables.add("TB_DATA_SOURCES");
        tables.add("TB_INDICATORS_VERSIONS");
        tables.add("TB_INDICATORS");
        tables.add("TB_RATES_DERIVATIONS");
        tables.add("TB_QUANTITIES");
        tables.add("TB_LIS_QUANTITIES_UNITS");
        tables.add("TB_LIS_GEOGR_VALUES");
        tables.add("TB_LIS_GEOGR_GRANULARITIES");
        tables.add("TB_INTERNATIONAL_STRINGS");
        tables.add("TV_AREAS_TEMATICAS");
        return tables;
    }

    @Override
    protected List<String> getSequencesToRestart() {
        List<String> sequences = new ArrayList<String>();
        sequences.add("SEQ_I18NSTRS");
        sequences.add("SEQ_L10NSTRS");
        sequences.add("SEQ_INDIC_SYSTEMS_VERSIONS");
        sequences.add("SEQ_INDICATORS_SYSTEMS");
        sequences.add("SEQ_DIMENSIONS");
        sequences.add("SEQ_DATA_SOURCES");
        sequences.add("SEQ_DATA_SOURCES_VARIABLES");
        sequences.add("SEQ_INDICATORS_VERSIONS");
        sequences.add("SEQ_INDICATORS");
        sequences.add("SEQ_INDICATORS_INSTANCES");
        sequences.add("SEQ_ELEMENTS_LEVELS");
        sequences.add("SEQ_RATES_DERIVATIONS");
        sequences.add("SEQ_QUANTITIES");
        sequences.add("SEQ_QUANTITIES_UNITS");
        sequences.add("SEQ_GEOGR_VALUES");
        sequences.add("SEQ_GEOGR_GRANULARITIES");

        return sequences;
    }
    
    private void removeCyclicDependences() throws Exception {
        Connection connection = getConnection().getConnection();
        connection.prepareStatement("UPDATE TB_QUANTITIES SET NUMERATOR_FK = null, DENOMINATOR_FK = null, BASE_QUANTITY_FK = null").execute();
    }
    
    public static <T> List<T> getList(T... values) {
        return Arrays.asList(values);
    }
    
    public static String readFile(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(IndicatorsBaseTest.class.getClassLoader().getResourceAsStream(filename)));
            StringBuffer strbuf = new StringBuffer();
            String line = reader.readLine();
            while (line != null) {
                strbuf.append(line);
                line = reader.readLine();
            }
            return strbuf.toString();
        } catch (Exception e) {
            Assert.fail("Error Reading file "+filename);
            return null;
        }
    }
}
