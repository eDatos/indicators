package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Assert;
import org.siemac.metamac.common.test.dbunit.MetamacDBUnitBaseTests;
import org.siemac.metamac.sso.client.MetamacPrincipal;
import org.siemac.metamac.sso.client.MetamacPrincipalAccess;
import org.siemac.metamac.sso.client.SsoClientConstants;
import org.springframework.beans.factory.annotation.Value;

import es.gobcan.istac.indicators.core.constants.IndicatorsConstants;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.dto.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.enume.domain.RoleEnum;

public abstract class IndicatorsBaseTest extends MetamacDBUnitBaseTests {

    @Value("${indicators.db.provider}")
    private String                        databaseProvider;

    @Value("${datasource.default_schema}")
    private String                        defaultSchema;

    
    private HashMap<String, List<String>> tablePrimaryKeys                                 = null;

    // -------------------------------------------------------------------------------
    // MOCK CONSTANTS
    // -------------------------------------------------------------------------------

    protected static String               NOT_EXISTS                                       = "not-exists";

    // Subject codes
    protected static String               SUBJECT_CODE_1                                   = "1";
    protected static String               SUBJECT_CODE_2                                   = "2";
    protected static String               SUBJECT_CODE_3                                   = "3";
    protected static String               SUBJECT_CODE_4                                   = "4";
    protected static String               SUBJECT_CODE_NOT_EXIST                           = "not-exists";

    // Indicators systems
    protected static String               INDICATORS_SYSTEM_1                              = "IndSys-1";
    protected static String               INDICATORS_SYSTEM_1_CODE                         = "CODE-1";
    protected static String               INDICATORS_SYSTEM_1_V2                           = INDICATORS_SYSTEM_1 + "-v2";
    protected static String               INDICATORS_SYSTEM_2                              = "IndSys-2";
    protected static String               INDICATORS_SYSTEM_2_CODE                         = "CODE-2";
    protected static String               INDICATORS_SYSTEM_3                              = "IndSys-3";
    protected static String               INDICATORS_SYSTEM_3_CODE                         = "CODE-3";
    protected static String               INDICATORS_SYSTEM_3_VERSION                      = IndicatorsDataBaseTest.NOT_INITIAL_VERSION;
    protected static String               INDICATORS_SYSTEM_4                              = "IndSys-4";
    protected static String               INDICATORS_SYSTEM_4_CODE                         = "CODE-4";
    protected static String               INDICATORS_SYSTEM_5                              = "IndSys-5";
    protected static String               INDICATORS_SYSTEM_5_CODE                         = "CODE-5";
    protected static String               INDICATORS_SYSTEM_6                              = "IndSys-6";
    protected static String               INDICATORS_SYSTEM_6_CODE                         = "CODE-6";
    protected static String               INDICATORS_SYSTEM_7                              = "IndSys-7";
    protected static String               INDICATORS_SYSTEM_7_CODE                         = "CODE-7";
    protected static String               INDICATORS_SYSTEM_8                              = "IndSys-8";
    protected static String               INDICATORS_SYSTEM_8_CODE                         = "CODE-8";
    protected static String               INDICATORS_SYSTEM_9                              = "IndSys-9";
    protected static String               INDICATORS_SYSTEM_9_CODE                         = "CODE-9";
    protected static String               INDICATORS_SYSTEM_10                             = "IndSys-10";
    protected static String               INDICATORS_SYSTEM_11                             = "IndSys-11";
    protected static String               INDICATORS_SYSTEM_11_VERSION                     = IndicatorsDataBaseTest.INIT_VERSION_MAXIMUM_MINOR_VERSION;
    protected static String               INDICATORS_SYSTEM_12                             = "IndSys-12";
    protected static String               INDICATORS_SYSTEM_12_VERSION                     = IndicatorsDataBaseTest.MAXIMUM_LIMIT_VERSION;

    // Dimensions
    protected static String               DIMENSION_NOT_EXISTS                             = "Dim-not-exists";
    protected static String               DIMENSION_1_INDICATORS_SYSTEM_1_V1               = "IndSys-1-v1-Dimension-1";
    protected static String               DIMENSION_1_INDICATORS_SYSTEM_1_V2               = "IndSys-1-v2-Dimension-1";
    protected static String               DIMENSION_1A_INDICATORS_SYSTEM_1_V2              = "IndSys-1-v2-Dimension-1A";
    protected static String               DIMENSION_1B_INDICATORS_SYSTEM_1_V2              = "IndSys-1-v2-Dimension-1B";
    protected static String               DIMENSION_1BA_INDICATORS_SYSTEM_1_V2             = "IndSys-1-v2-Dimension-1BA";
    protected static String               DIMENSION_2_INDICATORS_SYSTEM_1_V2               = "IndSys-1-v2-Dimension-2";
    protected static String               DIMENSION_1_INDICATORS_SYSTEM_3                  = "IndSys-3-v1-Dimension-1";

    // Indicator instances
    protected static String               INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V1      = "IndSys-1-v1-IInstance-1";
    protected static String               INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2      = "IndSys-1-v2-IInstance-1";
    protected static String               INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_1_V2      = "IndSys-1-v2-IInstance-2";
    protected static String               INDICATOR_INSTANCE_3_INDICATORS_SYSTEM_1_V2      = "IndSys-1-v2-IInstance-3";
    protected static String               INDICATOR_INSTANCE_3_INDICATORS_SYSTEM_1_V2_CODE = "IndSys-1-v2-IInstance-3-code";
    protected static String               INDICATOR_INSTANCE_1A_INDICATORS_SYSTEM_3_V1     = "IndSys-3-v1-IInstance-1A";
    protected static String               INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_3_V1      = "IndSys-3-v1-IInstance-2";
    protected static String               INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_6_V1      = "IndSys-2-v1-IInstance-2";
    protected static String               INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_10_V1     = "IndSys-10-v1-IInstance-1";
    protected static String               INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_10_V1     = "IndSys-10-v1-IInstance-2";
    protected static String               INDICATOR_INSTANCE_3_INDICATORS_SYSTEM_10_V1     = "IndSys-10-v1-IInstance-3";

    protected static String               INDICATORS_SYSTEM_1_IINSTANCE_1                  = "IndSys-1-v1-IInstance-1";
    protected static String               INDICATORS_SYSTEM_1_V2_IINSTANCE_1               = "IndSys-1-v2-IInstance-1";
    protected static String               INDICATORS_SYSTEM_1_V2_IINSTANCE_2               = "IndSys-1-v2-IInstance-2";
    protected static String               INDICATORS_SYSTEM_1_V2_IINSTANCE_3               = "IndSys-1-v2-IInstance-3";
    protected static String               INDICATORS_SYSTEM_3_IINSTANCE_1A                 = "IndSys-3-v1-IInstance-1A";
    protected static String               INDICATORS_SYSTEM_3_IINSTANCE_2                  = "IndSys-3-v1-IInstance-2";
    protected static String               INDICATORS_SYSTEM_6_IINSTANCE_1                  = "IndSys-6-v1-IInstance-1";
    protected static String               INDICATORS_SYSTEM_6_IINSTANCE_2                  = "IndSys-6-v1-IInstance-2";
    protected static String               INDICATORS_SYSTEM_10_IINSTANCE_1                 = "IndSys-10-v1-IInstance-1";
    protected static String               INDICATORS_SYSTEM_10_V2_IINSTANCE_1              = "IndSys-10-v2-IInstance-1";
    protected static String               INDICATORS_SYSTEM_10_IINSTANCE_2                 = "IndSys-10-v1-IInstance-2";
    protected static String               INDICATORS_SYSTEM_10_IINSTANCE_3                 = "IndSys-10-v1-IInstance-3";

    protected static String               INDICATORS_SYSTEM_11_IINSTANCE_1A                = "IndSys-11-v1-IInstance-1A";
    protected static String               INDICATORS_SYSTEM_11_IINSTANCE_2                 = "IndSys-11-v1-IInstance-2";
    protected static String               INDICATORS_SYSTEM_12_IINSTANCE_1A                = "IndSys-12-v1-IInstance-1A";
    protected static String               INDICATORS_SYSTEM_12_IINSTANCE_2                 = "IndSys-12-v1-IInstance-2";

    // Geographical values
    protected static String               GEOGRAPHICAL_VALUE_1                             = "1";
    protected static String               GEOGRAPHICAL_VALUE_2                             = "2";
    protected static String               GEOGRAPHICAL_VALUE_3                             = "3";
    protected static String               GEOGRAPHICAL_VALUE_4                             = "4";
    protected static String               GEOGRAPHICAL_VALUE_5                             = "5";
    protected static String               GEOGRAPHICAL_VALUE_6                             = "6";
    protected static String               GEOGRAPHICAL_VALUE_7                             = "7";
    protected static String               GEOGRAPHICAL_VALUE_8                             = "8";
    protected static String               GEOGRAPHICAL_VALUE_9                             = "9";

    // Geographical granularities
    protected static String               GEOGRAPHICAL_GRANULARITY_1                       = "1";
    protected static String               GEOGRAPHICAL_GRANULARITY_2                       = "2";
    protected static String               GEOGRAPHICAL_GRANULARITY_3                       = "3";
    protected static String               GEOGRAPHICAL_GRANULARITY_4                       = "4";
    protected static String               GEOGRAPHICAL_GRANULARITY_5                       = "5";

    // Indicators
    protected static final String         INDICATOR1_UUID                                  = "Indicator-1";
    protected static final String         INDICATOR1_DS_GPE_UUID                           = "Indicator-1-v1-DataSource-1-GPE-TIME";
    protected static final String         INDICATOR1_GPE_JSON_DATA                         = readFile("json/data_temporals_batch_update.json");
    protected static final String         INDICATOR1_VERSION                               = IndicatorsDataBaseTest.INIT_VERSION_MINOR_INCREMENT;

    protected static String               INDICATOR_1                                      = "Indicator-1";
    protected static String               INDICATOR_1_V2                                   = IndicatorsDataBaseTest.SECOND_VERSION;
    protected static String               INDICATOR_1_DS_GPE_UUID                          = "Indicator-1-v1-DataSource-1-GPE-GEO-TIME";
    protected static String               INDICATOR_1_GPE_JSON_DATA                        = readFile("json/data_temporal_spatials.json");
    protected static String               INDICATOR_1_CODE                                 = "CODE-1";
    protected static String               INDICATOR_2                                      = "Indicator-2";
    protected static String               INDICATOR_3                                      = "Indicator-3";
    protected static String               INDICATOR_3_VERSION                              = IndicatorsDataBaseTest.NOT_INITIAL_VERSION;
    protected static String               INDICATOR_3_DS_GPE_UUID                          = "Indicator-3-v1-DataSource-1-GPE-GEO-TIME";
    protected static String               INDICATOR_3_GPE_JSON_DATA                        = readFile("json/data_temporal_spatials.json");
    protected static String               INDICATOR_4                                      = "Indicator-4";
    protected static String               INDICATOR_4_DS_GPE_UUID                          = "Indicator-4-v1-DataSource-1-GPE-GEO-TIME";
    protected static String               INDICATOR_4_GPE_JSON_DATA                        = readFile("json/data_temporal_spatials.json");
    protected static String               INDICATOR_5                                      = "Indicator-5";
    protected static String               INDICATOR_5_DS_GPE_UUID                          = "Indicator-5-v1-DataSource-1-GPE-GEO-TIME";
    protected static String               INDICATOR_5_GPE_JSON_DATA                        = readFile("json/data_temporal_spatials.json");
    protected static String               INDICATOR_6                                      = "Indicator-6";
    protected static String               INDICATOR_7                                      = "Indicator-7";
    protected static String               INDICATOR_8                                      = "Indicator-8";
    protected static String               INDICATOR_9                                      = "Indicator-9";
    protected static String               INDICATOR_10                                     = "Indicator-10";
    protected static String               INDICATOR_11                                     = "Indicator-11";
    protected static String               INDICATOR_12                                     = "Indicator-12";
    protected static String               INDICATOR_13                                     = "Indicator-13";
    protected static String               INDICATOR_14                                     = "Indicator-14";
    protected static String               INDICATOR_14_VERSION                             = IndicatorsDataBaseTest.INIT_VERSION_MAXIMUM_MINOR_VERSION;
    protected static String               INDICATOR_15                                     = "Indicator-15";
    protected static String               INDICATOR_15_VERSION                             = IndicatorsDataBaseTest.MAXIMUM_LIMIT_VERSION;

    // Data sources
    protected static String               DATA_SOURCE_1_INDICATOR_1_V1                     = "Indicator-1-v1-DataSource-1";
    protected static String               DATA_SOURCE_1_INDICATOR_1_V2                     = "Indicator-1-v2-DataSource-1";
    protected static String               DATA_SOURCE_2_INDICATOR_1_V2                     = "Indicator-1-v2-DataSource-2";
    protected static String               DATA_SOURCE_1_INDICATOR_3                        = "Indicator-3-v1-DataSource-1";
    protected static String               DATA_SOURCE_1_INDICATOR_11                       = "Indicator-11-v1-DataSource-1";

    // Quantity units
    protected static String               QUANTITY_UNIT_1                                  = "1";
    protected static String               QUANTITY_UNIT_2                                  = "2";
    protected static String               QUANTITY_UNIT_3                                  = "3";

    // Unit multiplier
    protected static String               UNIT_MULTIPLIER_1                                = "1";
    protected static String               UNIT_MULTIPLIER_2                                = "2";
    protected static String               UNIT_MULTIPLIER_3                                = "3";
    protected static String               UNIT_MULTIPLIER_4                                = "4";
    protected static String               UNIT_MULTIPLIER_5                                = "5";
    protected static String               UNIT_MULTIPLIER_6                                = "6";
    protected static String               UNIT_MULTIPLIER_7                                = "7";

    // Subjects
    protected static String               SUBJECT_1                                        = "1";
    protected static String               SUBJECT_2                                        = "2";
    protected static String               SUBJECT_3                                        = "3";
    protected static String               SUBJECT_4                                        = "4";
    protected static String               SUBJECT_5                                        = "5";

    // -------------------------------------------------------------------------------
    // SERVICE CONTEXT
    // -------------------------------------------------------------------------------

    protected ServiceContext getServiceContextWithoutPrincipal() {
        return mockServiceContextWithoutPrincipal();
    }

    protected ServiceContext getServiceContextAdministrador() {
        ServiceContext serviceContext = getServiceContextWithoutPrincipal();
        putMetamacPrincipalInServiceContext(serviceContext, RoleEnum.ADMINISTRADOR);
        return serviceContext;
    }

    protected ServiceContext getServiceContextAdministrador2() {
        ServiceContext serviceContext = new ServiceContext("junit2", "junit", "app");
        putMetamacPrincipalInServiceContext(serviceContext, RoleEnum.ADMINISTRADOR);
        return serviceContext;
    }

    protected ServiceContext getServiceContextTecnicoProduccion() {
        ServiceContext serviceContext = getServiceContextWithoutPrincipal();
        putMetamacPrincipalInServiceContext(serviceContext, RoleEnum.TECNICO_PRODUCCION);
        return serviceContext;
    }

    protected ServiceContext getServiceContextTecnicoApoyoProduccion() {
        ServiceContext serviceContext = getServiceContextWithoutPrincipal();
        putMetamacPrincipalInServiceContext(serviceContext, RoleEnum.TECNICO_APOYO_PRODUCCION);
        return serviceContext;
    }

    protected ServiceContext getServiceContextTecnicoDifusion() {
        ServiceContext serviceContext = getServiceContextWithoutPrincipal();
        putMetamacPrincipalInServiceContext(serviceContext, RoleEnum.TECNICO_DIFUSION);
        return serviceContext;
    }

    protected ServiceContext getServiceContextTecnicoApoyoDifusion() {
        ServiceContext serviceContext = getServiceContextWithoutPrincipal();
        putMetamacPrincipalInServiceContext(serviceContext, RoleEnum.TECNICO_APOYO_DIFUSION);
        return serviceContext;
    }

    protected ServiceContext getServiceContextTecnicoSistemaIndicadores() {
        ServiceContext serviceContext = getServiceContextWithoutPrincipal();
        putMetamacPrincipalInServiceContext(serviceContext, RoleEnum.TECNICO_SISTEMA_INDICADORES);
        return serviceContext;
    }

    protected ServiceContext getServiceContextTecnicoSistemaIndicadoresOnlyAccessToIndicatorsSystem1() {
        ServiceContext serviceContext = getServiceContextWithoutPrincipal();
        MetamacPrincipal metamacPrincipal = new MetamacPrincipal();
        metamacPrincipal.setUserId(serviceContext.getUserId());
        metamacPrincipal.getAccesses().add(new MetamacPrincipalAccess(RoleEnum.TECNICO_SISTEMA_INDICADORES.getName(), IndicatorsConstants.SECURITY_APPLICATION_ID, "CODE-1"));
        serviceContext.setProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE, metamacPrincipal);
        return serviceContext;
    }

    protected ServiceContext getServiceContextTecnicoSistemaIndicadoresOnlyAccessToIndicatorsSystem2() {
        ServiceContext serviceContext = getServiceContextWithoutPrincipal();
        MetamacPrincipal metamacPrincipal = new MetamacPrincipal();
        metamacPrincipal.setUserId(serviceContext.getUserId());
        metamacPrincipal.getAccesses().add(new MetamacPrincipalAccess(RoleEnum.TECNICO_SISTEMA_INDICADORES.getName(), IndicatorsConstants.SECURITY_APPLICATION_ID, "CODE-2"));
        serviceContext.setProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE, metamacPrincipal);
        return serviceContext;
    }

    @Override
    public void removeCyclicDependencies() throws Exception {
        super.removeCyclicDependencies();
        Connection connection = getConnection().getConnection();
        try {
            connection.prepareStatement("UPDATE TB_QUANTITIES SET NUMERATOR_FK = null, DENOMINATOR_FK = null, BASE_QUANTITY_FK = null").execute();
        } finally {
            connection.close();
        }
    }

    @Override
    protected List<String> getTableNamesOrderedByFKDependency() {
        List<String> tables = new ArrayList<String>();
        tables.add("TB_CONFIGURATION");
        tables.add("TB_INTERNATIONAL_STRINGS");
        tables.add("TB_LIS_QUANTITIES_UNITS");
        tables.add("TB_LIS_GEOGR_GRANULARITIES");
        tables.add("TB_LIS_GEOGR_VALUES");
        tables.add("TB_INDICATORS");
        tables.add("TB_LIS_UNITS_MULTIPLIERS");
        tables.add("TB_QUANTITIES");
        tables.add("TB_INDICATORS_VERSIONS");
        tables.add("TB_RATES_DERIVATIONS");
        tables.add("TB_DATA_SOURCES");
        tables.add("TB_DATA_SOURCES_VARIABLES");
        tables.add("TB_DIMENSIONS");
        tables.add("TB_INDICATORS_INSTANCES");
        tables.add("TB_INDICATORS_SYSTEMS");
        tables.add("TB_INDIC_SYSTEMS_VERSIONS");
        tables.add("TB_ELEMENTS_LEVELS");
        tables.add("TB_EXTERNAL_ITEMS");
        tables.add("TB_LOCALISED_STRINGS");
        tables.add("TB_INDIC_VERSION_LAST_VALUE");
        tables.add("TB_INDIC_INST_LAST_VALUE");
        tables.add("TB_INDIC_INST_GEO_VALUES");
        tables.add("TB_INDICATORS_SYSTEMS_HIST");
        tables.add("TB_TRANSLATIONS");
        tables.add("TV_AREAS_TEMATICAS");
        tables.add("TV_CONSULTA");
        return tables;
    }

    @Override
    protected List<String> getSequencesToRestart() {
        List<String> sequences = new ArrayList<String>();
        sequences.add("SEQ_I18NSTRS");
        sequences.add("SEQ_L10NSTRS");
        sequences.add("SEQ_INDIC_VERSION_LAST_VALUE");
        sequences.add("SEQ_INDIC_INST_LAST_VALUE");
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
        sequences.add("SEQ_INDICATORS_SYSTEMS_HIST");
        sequences.add("SEQ_TRANSLATIONS");
        sequences.add("SEQ_UNITS_MULTIPLIERS");

        return sequences;
    }

    public static <T> List<T> getList(T... values) {
        return Arrays.asList(values);
    }

    public static String readFile(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(IndicatorsBaseTest.class.getClassLoader().getResourceAsStream(filename), "UTF-8"));
            StringBuffer strbuf = new StringBuffer();
            String line = reader.readLine();
            while (line != null) {
                strbuf.append(line);
                line = reader.readLine();
            }
            return strbuf.toString();
        } catch (Exception e) {
            Assert.fail("Error Reading file " + filename);
            return null;
        }
    }

    private void putMetamacPrincipalInServiceContext(ServiceContext serviceContext, RoleEnum role) {
        MetamacPrincipal metamacPrincipal = new MetamacPrincipal();
        metamacPrincipal.setUserId(serviceContext.getUserId());
        metamacPrincipal.getAccesses().add(new MetamacPrincipalAccess(role.getName(), IndicatorsConstants.SECURITY_APPLICATION_ID, null));
        serviceContext.setProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE, metamacPrincipal);
    }

    @Override
    protected Map<String, List<String>> getTablePrimaryKeys() {
        if (tablePrimaryKeys == null) {
            tablePrimaryKeys = new HashMap<String, List<String>>();
            tablePrimaryKeys.put("TV_AREAS_TEMATICAS", Arrays.asList("ID_AREA_TEMATICA"));
            tablePrimaryKeys.put("TV_CONSULTA", Arrays.asList("ID_CONSULTA"));
            tablePrimaryKeys.put("TB_INDIC_INST_GEO_VALUES", Arrays.asList("GEOGRAPHICAL_VALUE_FK", "INDICATOR_INSTANCE_FK"));
        }
        // It is necessary to specify the table name and the column name in upper and lower case for compatibility with the different database providers.
        Map<String, List<String>> primaryKeysInLowerCase = new HashMap<>();
        for (Entry<String, List<String>> primaryKey : tablePrimaryKeys.entrySet()) {
            List<String> values = new ArrayList<>(primaryKey.getValue());
            toLowerCase(values);
            primaryKeysInLowerCase.put(primaryKey.getKey().toLowerCase(), values);
        }
        tablePrimaryKeys.putAll(primaryKeysInLowerCase);
        return tablePrimaryKeys;
    }

    private void toLowerCase(List<String> strings) {
        ListIterator<String> iterator = strings.listIterator();
        while (iterator.hasNext()) {
            iterator.set(iterator.next().toLowerCase());
        }
    }
    
    protected List<String> getIndicatorsInstancesUUIDs(List<IndicatorInstance> indicatorsInstances) {
        if (indicatorsInstances == null) {
            return null;
        }
        List<String> uuids = new ArrayList<String>();
        for (IndicatorInstance indicatorInstance : indicatorsInstances) {
            uuids.add(indicatorInstance.getUuid());
        }
        return uuids;
    }

    protected List<String> getIndicatorsInstancesDtosUUIDs(List<IndicatorInstanceDto> indicatorsInstances) {
        if (indicatorsInstances == null) {
            return null;
        }
        List<String> uuids = new ArrayList<String>();
        for (IndicatorInstanceDto indicatorInstance : indicatorsInstances) {
            uuids.add(indicatorInstance.getUuid());
        }
        return uuids;
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
            assertTrue("Element " + elem + " not in collection", collection.contains(elem));
        }
        assertEquals("Size does not match", expected.length, collection.size());
    }

    protected void checkElementsOrder(String[] expected, List<String> collection) {
        assertEquals(expected.length, collection.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals("Element " + expected[i] + " not in collection", collection.get(i), expected[i]);
        }
    }

    @Override
    protected DataBaseProvider getDatabaseProvider() {
        return DataBaseProvider.valueOf(databaseProvider);
    }

    @Override
    protected String getDefaultSchema() {
        return defaultSchema;
    }
}
