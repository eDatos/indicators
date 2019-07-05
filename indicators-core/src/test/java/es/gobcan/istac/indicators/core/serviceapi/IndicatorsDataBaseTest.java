package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
import org.junit.Before;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceObservationDto;
import es.gobcan.istac.edatos.dataset.repository.dto.CodeDimensionDto;
import es.gobcan.istac.edatos.dataset.repository.dto.ConditionDimensionDto;
import es.gobcan.istac.edatos.dataset.repository.dto.ConditionObservationDto;
import es.gobcan.istac.edatos.dataset.repository.dto.InternationalStringDto;
import es.gobcan.istac.edatos.dataset.repository.dto.LocalisedStringDto;
import es.gobcan.istac.edatos.dataset.repository.dto.ObservationDto;
import es.gobcan.istac.edatos.dataset.repository.dto.ObservationExtendedDto;
import es.gobcan.istac.edatos.dataset.repository.service.DatasetRepositoriesServiceFacade;
import es.gobcan.istac.edatos.dataset.repository.util.DtoUtils;
import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.HasVersionNumber;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.MeasureValue;
import es.gobcan.istac.indicators.core.domain.TimeGranularity;
import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorDataDimensionTypeEnum;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsAsserts;
import es.gobcan.istac.indicators.core.vo.GeographicalValueVO;

public abstract class IndicatorsDataBaseTest extends IndicatorsBaseTest {

    public static final String INIT_VERSION                                             = "1.0";
    public static final String INIT_VERSION_MINOR_INCREMENT                             = "1.1";
    public static final String INIT_VERSION_SOME_MINOR_INCREMENTS                       = "1.5";
    public static final String INIT_VERSION_HUGE_INCREMENT                              = "1.300";
    public static final String INIT_VERSION_HUGE_INCREMENT_SOME_MINOR_INCREMENTS        = "1.322";
    public static final String INIT_VERSION_ANOTHER_HUGE_INCREMENT                      = "1.500";
    public static final String INIT_VERSION_ANOTHER_HUGE_INCREMENT_WITH_MINOR_INCREMENT = "1.501";
    public static final String INIT_VERSION_MAXIMUM_MINOR_VERSION                       = "1.99999";
    public static final String SECOND_VERSION                                           = "2.0";
    public static final String NOT_INITIAL_VERSION                                      = "11.33";
    public static final String NOT_INITIAL_VERSION_WITH_MINOR_INCREMENT                 = "11.34";
    public static final String ANOTHER_NOT_INITIAL_VERSION                              = "12.0";
    public static final String MAXIMUM_LIMIT_VERSION                                    = "99999.9";

    @Before
    public void onBefore() throws Exception {
        clearDatabase();
    }

    protected abstract IndicatorsService getIndicatorsService();

    protected abstract DatasetRepositoriesServiceFacade getDatasetRepositoriesServiceFacade();

    private class DatabaseObjectNameRowMapper implements RowMapper<String> {

        @Override
        public String mapRow(ResultSet rs, int line) throws SQLException {
            ResultSetExtractor<String> extractor = new ResultSetExtractor<String>() {

                @Override
                public String extractData(ResultSet rs) throws SQLException {
                    return rs.getString(1);
                }
            };
            return extractor.extractData(rs);
        }
    }

    protected void clearDatabase() {
        // Drop dynamic tables
        List<String> tableNames = getDatasourceDSRepository().query("SELECT TABLE_NAME FROM TB_DATASETS", new DatabaseObjectNameRowMapper());
        for (String tableName : tableNames) {
            try {
                getDatasourceDSRepository().update("DROP VIEW " + tableName);
            } catch (Exception e) {
                // ignore error
            }
        }

        // Drop views
        List<String> viewNames = getDatasourceDSRepository().query("SELECT VIEW_NAME FROM USER_VIEWS", new DatabaseObjectNameRowMapper());
        for (String objectName : viewNames) {
            try {
                getDatasourceDSRepository().update("DROP VIEW " + objectName);
            } catch (Exception e) {
                // ignore error
            }
        }

        // Truncate tables
        getDatasourceDSRepository().update("truncate table TB_LOCALISED_STRINGS");
        getDatasourceDSRepository().update("truncate table TB_DATASET_DIMENSIONS");
        getDatasourceDSRepository().update("truncate table TB_DATASET_ATTRIBUTES");
        getDatasourceDSRepository().update("truncate table TB_ATTRIBUTE_DIMENSIONS");
        getDatasourceDSRepository().update("delete from TB_ATTRIBUTES"); // delete instead of truncate due to constraint
        getDatasourceDSRepository().update("delete from TB_DATASETS");
        getDatasourceDSRepository().update("delete from TB_INTERNATIONAL_STRINGS");

    }

    protected static Date createDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        return cal.getTime();
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

        // Retrieve all dataset's code dimensions
        Map<String, List<String>> repoDimCodes = getDatasetRepositoriesServiceFacade().findCodeDimensions(indicator.getDataRepositoryId());

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
                    assertEquals("Index: " + index, plainData.get(index), observationsList.get(0).getPrimaryMeasure());
                    index++;
                }
            }
        }
    }

    protected void checkDataAttributes(Map<String, List<String>> dimCodes, String indicatorUuid, String indicatorVersion, String attrId,
            Map<String, AttributeInstanceObservationDto> expectedAttributes) throws Exception {
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

            AttributeInstanceObservationDto expectedAttr = expectedAttributes.get(key);
            AttributeInstanceObservationDto actualAttr = findAttribute(obs, attrId);

            IndicatorsAsserts.assertEqualsAttributeBasic(expectedAttr, actualAttr);
        }
    }

    protected AttributeInstanceObservationDto findAttribute(ObservationExtendedDto observation, String attrId) {
        for (AttributeInstanceObservationDto attr : observation.getAttributes()) {
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
    protected AttributeInstanceObservationDto createAttribute(String id, String locale, String value) {
        AttributeInstanceObservationDto attributeBasicDto = new AttributeInstanceObservationDto();
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
        for (GeographicalValue geoValue : geoValues) {
            codes.add(geoValue.getCode());
        }
        return codes;
    }

    protected List<String> getGeographicalValuesVOCodes(List<GeographicalValueVO> geoValues) {
        if (geoValues == null) {
            return null;
        }
        List<String> codes = new ArrayList<String>();
        for (GeographicalValueVO geoValue : geoValues) {
            codes.add(geoValue.getCode());
        }
        return codes;
    }

    protected List<String> getIndicatorsVersionsUUIDs(List<IndicatorVersion> indicatorsVersions) {
        if (indicatorsVersions == null) {
            return null;
        }
        List<String> uuids = new ArrayList<String>();
        for (IndicatorVersion indicatorVersion : indicatorsVersions) {
            uuids.add(indicatorVersion.getUuid());
        }
        return uuids;
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

    public JdbcTemplate getDatasourceDSRepository() {
        return this.jdbcTemplate;
    }

    @Override
    public void specificSetUpDatabaseTester() throws Exception {
        setUpDatabaseTester(getClass(), jdbcTemplate.getDataSource(), getDataSetDSRepoFile());
    }

    private void setUpDatabaseTester(Class<?> clazz, DataSource dataSource, String datasetFileName) throws Exception {
        // Setup database tester
        if (databaseTester == null) {
            Connection connection = dataSource.getConnection();
            try {
                databaseTester = new OracleDataSourceDatabaseTester(dataSource, connection.getMetaData().getUserName());
            } finally {
                connection.close();
            }
        }

        IDatabaseConnection dbUnitConnection = databaseTester.getConnection();
        try {
            // Create dataset
            ReplacementDataSet dataSetReplacement = new ReplacementDataSet((new FlatXmlDataSetBuilder()).build(clazz.getClassLoader().getResource(datasetFileName)));
            dataSetReplacement.addReplacementObject("[NULL]", null);
            dataSetReplacement.addReplacementObject("[null]", null);
            dataSetReplacement.addReplacementObject("[UNIQUE_SEQUENCE]", (new Date()).getTime());

            // DbUnit inserts and updates rows in the order they are found in your dataset. You must therefore order your tables and rows appropriately in your datasets to prevent foreign keys
            // constraint
            // violation.
            // Since version 2.0, the DatabaseSequenceFilter can now be used to automatically determine the tables order using foreign/exported keys information.
            /*
             * ITableFilter filter = new DatabaseSequenceFilter(dbUnitConnection);
             * IDataSet dataset = new (filter, dataSetReplacement);
             */

            // Delete all data (dbunit not delete TBL_LOCALISED_STRINGS...)
            initializeDatabase(dbUnitConnection);

            databaseTester.setSetUpOperation(DatabaseOperation.REFRESH);
            databaseTester.setTearDownOperation(DatabaseOperation.NONE);
            databaseTester.setDataSet(dataSetReplacement);
            databaseTester.onSetup();
        } finally {
            dbUnitConnection.close();
        }
    }

    private void initializeDatabase(IDatabaseConnection dbUnitConnection) throws Exception {
        // Remove tables content
        List<String> tableNamesToDelete = getDSRepoTablesToDelete(dbUnitConnection);
        for (String tableNameToDelete : tableNamesToDelete) {
            dbUnitConnection.getConnection().prepareStatement("DELETE FROM " + tableNameToDelete).execute();
        }

        List<String> sequencesNamesToDrop = getDSRepoSequencesToDrop(dbUnitConnection);
        for (String sequenceNameToDrop : sequencesNamesToDrop) {
            try {
                dbUnitConnection.getConnection().prepareStatement("DROP SEQUENCE " + sequenceNameToDrop).execute();
            } catch (SQLException e) {
                // Sequence already dropped
            }
        }

        List<String> tableNamesToDrop = getDSRepoTablesToDrop(dbUnitConnection);
        for (String tableNameToDrop : tableNamesToDrop) {
            try {
                dbUnitConnection.getConnection().prepareStatement("DROP TABLE " + tableNameToDrop).execute();
            } catch (SQLException e) {
                // table already dropped
            }
        }

        // Restart sequences
        List<String> sequences = getDSRepoSequencesToRestart();
        if (sequences != null) {
            for (String sequence : sequences) {
                restartSequence(dbUnitConnection, sequence);
            }
        }
    }

    private List<String> getDSRepoTablesToDrop(IDatabaseConnection dbUnitConnection) throws Exception {
        List<String> dynamicTables = new ArrayList<String>();
        for (String tableName : dbUnitConnection.createDataSet().getTableNames()) {
            if (tableName.startsWith("DATA_")) {
                dynamicTables.add(tableName);
            }
        }
        return dynamicTables;
    }

    private List<String> getDSRepoSequencesToDrop(IDatabaseConnection dbUnitConnection) throws Exception {
        List<String> dynamicTables = new ArrayList<String>();
        for (String tableName : dbUnitConnection.createDataSet().getTableNames()) {
            if (tableName.startsWith("DATA_")) {
                String seqName = tableName.replaceFirst("DATA", "SEQ");
                dynamicTables.add(seqName);
            }
        }
        return dynamicTables;
    }

    private List<String> getDSRepoTablesToDelete(IDatabaseConnection dbUnitConnection) throws Exception {
        List<String> dynamicTables = new ArrayList<String>();
        dynamicTables.add("TB_ATTRIBUTE_DIMENSIONS");
        dynamicTables.add("TB_DATASET_DIMENSIONS");
        dynamicTables.add("TB_ATTRIBUTES");
        dynamicTables.add("TB_DATASETS");
        dynamicTables.add("TB_LOCALISED_STRINGS");
        dynamicTables.add("TB_INTERNATIONAL_STRINGS");
        for (String tableName : dbUnitConnection.createDataSet().getTableNames()) {
            if (tableName.startsWith("TB_") && !dynamicTables.contains(tableName)) {
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

    protected HasVersionNumber getHasVersionNumberMock(String versionNumber) {
        return new HasVersionNumberMock(versionNumber);
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

class HasVersionNumberMock implements HasVersionNumber {

    private String code;
    private String versionNumber;

    public HasVersionNumberMock(String versionNumber) {
        super();
        this.versionNumber = versionNumber;
        this.code = "code" + (new Date()).getTime();
    }

    @Override
    public String getVersionNumber() {
        return versionNumber;
    }

    @Override
    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    @Override
    public String getCode() {
        return code;
    }

}
