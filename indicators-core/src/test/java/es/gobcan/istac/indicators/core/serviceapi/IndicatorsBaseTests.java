package es.gobcan.istac.indicators.core.serviceapi;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.filter.ITableFilter;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.oracle.OracleDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.fornax.cartridges.sculptor.framework.util.db.OrderedDeleteAllOperation;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class IndicatorsBaseTests {

    private final Logger log = LoggerFactory.getLogger(IndicatorsBaseTests.class);
    
	private ApplicationContext applicationContext;
    private JdbcTemplate jdbcTemplate;
    
    private final ServiceContext serviceContext = new ServiceContext("junit", "junit", "app");
    private final ServiceContext serviceContext2 = new ServiceContext("junit2", "junit2", "app");

    private DataSourceDatabaseTester databaseTester = null;

    protected ServiceContext getServiceContext() {
        return serviceContext;
    }    
    protected ServiceContext getServiceContext2() {
        return serviceContext2;
    }
    
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
//  @Autowired
	protected ApplicationContext getApplicationContext() {
	    return applicationContext;
	}
  
	/**
	 *  Return the JdbcTemplate that this base class manages.
	 */
	public final JdbcTemplate getJdbcTemplate() {
	    return this.jdbcTemplate;
	}
    
    /**
     * Creates the dataset executes the dbunit setup operation
     */
    @Before
    public void setUpDatabaseTester() throws Exception {
        setUpDatabaseTester(getClass(), getJdbcTemplate().getDataSource(), getDataSetFile());
    }
    
    private void setUpDatabaseTester(Class<?> clazz, DataSource dataSource, String datasetFileName) throws Exception {

        // Setup database tester
        if (databaseTester == null) {
            databaseTester = new OracleDataSourceDatabaseTester(dataSource);
            databaseTester.setSchema(dataSource.getConnection().getMetaData().getUserName()); // TODO poner explícitamente el nombre del esquema en fichero de propiedades
        }
        
        // Create dataset
        ReplacementDataSet dataSetReplacement = new ReplacementDataSet((new FlatXmlDataSetBuilder()).build(clazz.getClassLoader().getResource(datasetFileName)));
        dataSetReplacement.addReplacementObject("[NULL]", null);
        dataSetReplacement.addReplacementObject("[null]", null);
        dataSetReplacement.addReplacementObject("[UNIQUE_SEQUENCE]", (new Date()).getTime());
        
        // DbUnit inserts and updates rows in the order they are found in your dataset. You must therefore order your tables and rows appropriately in your datasets to prevent foreign keys constraint violation.
        // Since version 2.0, the DatabaseSequenceFilter can now be used to automatically determine the tables order using foreign/exported keys information.
        ITableFilter filter = new DatabaseSequenceFilter(databaseTester.getConnection());
        IDataSet dataset = new FilteredDataSet(filter, dataSetReplacement);

        // Delete all data (dbunit not delete TBL_LOCALISED_STRINGS...)
        removeDatabaseContent(databaseTester.getConnection().getConnection());

        databaseTester.setSetUpOperation(DatabaseOperation.REFRESH);
        databaseTester.setTearDownOperation(new OrderedDeleteAllOperation());
        databaseTester.setDataSet(dataset);
        databaseTester.onSetup();
    }

    /**
     * Executes the dbunit teardown operation
     */
    @After
    public void tearDownDatabaseTester() throws Exception {
        if (databaseTester != null) {
            removeDatabaseContent(databaseTester.getConnection().getConnection());
            databaseTester.onTearDown();
        }
    }
    
    protected IDatabaseConnection getConnection() throws Exception {
        IDatabaseConnection connection = new DatabaseConnection(getJdbcTemplate().getDataSource().getConnection());
        DatabaseConfig config = connection.getConfig();
        config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new OracleDataTypeFactory());

        return connection;
    }

    protected void logDb() {
        IDatabaseConnection connection = null;
        try {
            connection = getConnection();
            
            ITableFilter filter = new DatabaseSequenceFilter(connection);
            IDataSet dataset = new FilteredDataSet(filter, connection.createDataSet());

            StringWriter out = new StringWriter();

            FlatXmlDataSet.write(dataset, out);
            log.info(out.getBuffer().toString());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ignore) {
                }
            }
        }
    }

    private void removeDatabaseContent(Connection connection) throws Exception {
        // Remove tables content
        List<String> tableNamesToRemove = getTablesToRemoveContent();
        if (tableNamesToRemove != null) {
            for (String tableNameToRemove : tableNamesToRemove) {
                connection.prepareStatement("DELETE FROM " + tableNameToRemove).execute();        
            }
        }
    }
    
    /**
     * Start the id sequence from a high value to avoid conflicts with test
     * data. You can define the sequence name with {@link #getSequenceName}.
     */
    public static void restartSequence(IDatabaseConnection dbConnection, String sequenceName) {
        if (sequenceName == null) {
            return;
        }
        Connection connection = null;
        Statement stmt = null;
        try {
            connection = dbConnection.getConnection();
            stmt = connection.createStatement();
            stmt.execute("ALTER SEQUENCE " + sequenceName + " RESTART WITH 10000");
        } catch (Exception e) {
            try {
                stmt.close();
            } catch (SQLException ignore) {
            }
            try {
                stmt = connection.createStatement();
                stmt.execute("UPDATE SEQUENCE SET SEQ_COUNT = 10000 WHERE SEQ_NAME = '" + sequenceName + "'");
            } catch (Exception e2) {
                throw new RuntimeException("Couldn't restart sequence: " + sequenceName + " : " + e.getMessage(), e);
            }
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ignore) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ignore) {
                }
            }
        }
    }
    
    /**
     * DatasourceTester with support for Oracle data types.
     */
    private class OracleDataSourceDatabaseTester extends DataSourceDatabaseTester {
        public OracleDataSourceDatabaseTester(DataSource dataSource) {
            super(dataSource);
        }

        @Override
        public IDatabaseConnection getConnection() throws Exception {
            IDatabaseConnection connection = super.getConnection();

            connection.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new OracleDataTypeFactory());
            return connection;
        }
    }
    
    /**
     * Override this method to specify the XML file with DBUnit test data
     */
    protected abstract String getDataSetFile();
    
    /**
     * Get table names to remove content when tear down database
     */
    protected abstract List<String> getTablesToRemoveContent();
}
