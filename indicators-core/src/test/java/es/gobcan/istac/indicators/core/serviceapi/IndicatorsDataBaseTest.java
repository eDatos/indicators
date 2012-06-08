package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

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

import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.MeasureValue;
import es.gobcan.istac.indicators.core.domain.TimeGranularity;
import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorDataDimensionTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.MeasureDimensionTypeEnum;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsAsserts;

public abstract class IndicatorsDataBaseTest extends IndicatorsBaseTest {

    protected abstract IndicatorsService getIndicatorsService();
    protected abstract DatasetRepositoriesServiceFacade getDatasetRepositoriesServiceFacade();

    protected static Date createDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        return cal.getTime();
    }

    protected void checkElementsInCollection(String[] expected, Collection<List<String>> collection) {
        List<String> values = new ArrayList<String>();
        for (List<String> vals : collection) {
            values.addAll(vals);
        }
        checkElementsInCollection(expected, values);
    }
    protected void checkElementsInCollection(String[] expected, List<String> collection) {
        for (String elem : expected) {
            assertTrue("Element "+elem+" not in collection",collection.contains(elem));
        }
        assertEquals(expected.length, collection.size());
    }
    
    protected void checkElementsOrder(String[] expected, List<String> collection) {
        assertEquals(expected.length, collection.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals("Element "+expected[i]+" not in collection",collection.get(i),expected[i]);
        }
    }

    protected void assertIndicatorEmptyData(String indicatorUuid, String indicatorVersionNumber) throws MetamacException {
        IndicatorVersion indicatorVersion = getIndicatorsService().retrieveIndicator(getServiceContextAdministrador(), indicatorUuid, indicatorVersionNumber);
        assertNotNull(indicatorVersion);
        assertNull(indicatorVersion.getDataRepositoryId());
    }

    protected void checkDataDimensions(Map<String, List<String>> dimCodes, String indicatorUuid, String indicatorVersion) throws Exception {
        IndicatorVersion indicator = getIndicatorsService().retrieveIndicator(getServiceContextAdministrador(), indicatorUuid, indicatorVersion);
        assertNotNull(indicator);
        assertNotNull(indicator.getDataRepositoryId());
        List<ConditionObservationDto> conditionObservationsList = getDatasetRepositoriesServiceFacade().findCodeDimensions(indicator.getDataRepositoryId());
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

    protected void checkDataObservations(Map<String, List<String>> dimCodes, String indicatorUuid, String indicatorVersion, List<String> plainData) throws Exception {
        IndicatorVersion indicator = getIndicatorsService().retrieveIndicator(getServiceContextAdministrador(), indicatorUuid, indicatorVersion);
        assertNotNull(indicator);
        assertNotNull(indicator.getDataRepositoryId());
        int index = 0;
        for (String measureCode : dimCodes.get(IndicatorDataDimensionTypeEnum.MEASURE.name())) {
            for (String timeCode : dimCodes.get(IndicatorDataDimensionTypeEnum.TIME.name())) {
                for (String geoCode : dimCodes.get(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name())) {
                    ConditionObservationDto condition = new ConditionObservationDto();
                    condition.addCodesDimension(new CodeDimensionDto(IndicatorDataDimensionTypeEnum.TIME.name(), timeCode));
                    condition.addCodesDimension(new CodeDimensionDto(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), geoCode));
                    condition.addCodesDimension(new CodeDimensionDto(IndicatorDataDimensionTypeEnum.MEASURE.name(), measureCode));

                    Map<String, ObservationDto> observations = getDatasetRepositoriesServiceFacade().findObservations(indicator.getDataRepositoryId(), Arrays.asList(condition));
                    assertNotNull(observations);
                    assertEquals(1, observations.keySet().size());
                    List<ObservationDto> observationsList = new ArrayList<ObservationDto>(observations.values());
                    assertEquals("Index: "+index,plainData.get(index), observationsList.get(0).getPrimaryMeasure());
                    index++;
                }
            }
        }
    }

    protected void checkDataAttributes(Map<String, List<String>> dimCodes, String indicatorUuid, String indicatorVersion, String attrId, Map<String, AttributeBasicDto> expectedAttributes)
            throws Exception {
        IndicatorVersion indicator = getIndicatorsService().retrieveIndicator(getServiceContextAdministrador(), indicatorUuid, indicatorVersion);
        assertNotNull(indicator);
        assertNotNull(indicator.getDataRepositoryId());

        ConditionDimensionDto geoCondition = createCondition(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), dimCodes.get(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name()));
        ConditionDimensionDto timeCondition = createCondition(IndicatorDataDimensionTypeEnum.TIME.name(), dimCodes.get(IndicatorDataDimensionTypeEnum.TIME.name()));
        ConditionDimensionDto measureCondition = createCondition(IndicatorDataDimensionTypeEnum.MEASURE.name(), dimCodes.get(IndicatorDataDimensionTypeEnum.MEASURE.name()));

        Map<String, ObservationExtendedDto> observations = getDatasetRepositoriesServiceFacade().findObservationsExtendedByDimensions(indicator.getDataRepositoryId(),
                Arrays.asList(geoCondition, timeCondition, measureCondition));

        for (Entry<String, ObservationExtendedDto> entry : observations.entrySet()) {
            String key = entry.getKey();
            ObservationExtendedDto obs = entry.getValue();

            AttributeBasicDto expectedAttr = expectedAttributes.get(key);
            AttributeBasicDto actualAttr = findAttribute(obs, attrId);

            IndicatorsAsserts.assertEqualsAttributeBasic(expectedAttr, actualAttr);
        }
    }

    protected AttributeBasicDto findAttribute(ObservationExtendedDto observation, String attrId) {
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
    protected String generateObservationUniqueKey(String geoValue, String timeValue, String measureValue) {
        CodeDimensionDto geoDimCode = new CodeDimensionDto(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), geoValue);
        CodeDimensionDto timeDimCode = new CodeDimensionDto(IndicatorDataDimensionTypeEnum.TIME.name(), timeValue);
        CodeDimensionDto measureDimCode = new CodeDimensionDto(IndicatorDataDimensionTypeEnum.MEASURE.name(), measureValue);
        return DtoUtils.generateUniqueKey(Arrays.asList(geoDimCode, timeDimCode, measureDimCode));
    }

    /*
     * Helper for attributes
     */
    protected AttributeBasicDto createAttribute(String id, String locale, String value) {
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
    protected ConditionDimensionDto createCondition(String dimensionId, List<String> codeDimensions) {
        ConditionDimensionDto condition = new ConditionDimensionDto();
        condition.setDimensionId(dimensionId);
        condition.setCodesDimension(codeDimensions);
        return condition;
    }
    
    protected List<String> getGeographicalGranularitiesCodes(List<GeographicalGranularity> granularities) {
        if (granularities == null) {
            return null;
        }
        List<String> codes = new ArrayList<String>();
        for (GeographicalGranularity granularity : granularities) {
            codes.add(granularity.getCode());
        }
        return codes;
    }
    
    protected List<String> getTimeGranularitiesNames(List<TimeGranularity> granularities) {
        if (granularities == null) {
            return null;
        }
        List<String> codes = new ArrayList<String>();
        for (TimeGranularity granularity : granularities) {
            codes.add(granularity.getGranularity().name());
        }
        return codes;
    }
    
    protected List<String> getTimeValuesCodes(List<TimeValue> timeValues) {
        if (timeValues == null) {
            return null;
        }
        List<String> codes = new ArrayList<String>();
        for (TimeValue timeValue : timeValues) {
            codes.add(timeValue.getTimeValue());
        }
        return codes;
    }
    
    protected List<String> getMeasureNames(List<MeasureValue> measures) {
        if (measures == null) {
            return null;
        }
        List<String> codes = new ArrayList<String>();
        for (MeasureValue measure : measures) {
            codes.add(measure.getMeasureValue().name());
        }
        return codes;
    }
    
    protected List<String> getGeographicalValuesCodes(List<GeographicalValue> geoValues) {
        if (geoValues == null) {
            return null;
        }
        List<String> codes = new ArrayList<String>();
        for (GeographicalValue geoValue: geoValues) {
            codes.add(geoValue.getCode());
        }
        return codes;
    }

    
    /*
     * DBUnit methods and helpers
     */
    
    protected abstract String getDataSetDSRepoFile();
    
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
        //NOTHING 
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
