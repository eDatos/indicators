package es.gobcan.istac.indicators.core.externalitemscache.serviceapi;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.dbunit.MetamacDBUnitBaseTests;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.sso.client.MetamacPrincipal;
import org.siemac.metamac.sso.client.MetamacPrincipalAccess;
import org.siemac.metamac.sso.client.SsoClientConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.gobcan.istac.indicators.core.constants.IndicatorsConstants;
import es.gobcan.istac.indicators.core.enume.domain.RoleEnum;
import es.gobcan.istac.indicators.core.externalitemscache.domain.CategoryCache;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-test.xml"})
@TransactionConfiguration(defaultRollback = true, transactionManager = "txManager")
@Transactional
public class CategoryCacheServiceTest extends MetamacDBUnitBaseTests implements CategoryCacheServiceTestBase {

    @Value("${datasource.default_schema}")
    private String                        defaultSchema;
    private HashMap<String, List<String>> tablePrimaryKeys           = null;

    protected static String               CATEGORY_CODE_ECONOMIA     = "category-ECONOMIA";
    protected static String               CATEGORY_TITLE_ECONOMIA_ES = "Economía";
    protected static String               CATEGORY_TITLE_ECONOMIA_EN = "Economy";
    protected static String               CATEGORY_ELEMENT_ECONOMIA  = "category-element-ECONOMIA";
    protected static String               CATEGORY_UUID_ECONOMIA     = "1";

    @Autowired
    protected CategoryCacheService        categoryCacheService;

    @Test
    public void testRetrieveCategoryCacheByCategoryCode() throws Exception {

        CategoryCache categoryCache = categoryCacheService.retrieveCategoryCacheByCategoryCode(getServiceContextAdministrador(), CATEGORY_CODE_ECONOMIA);

        assertEqualsCategoryCache(categoryCache);
    }

    @Test
    public void testRetrieveCategoryCacheByCategoryElementCode() throws Exception {
        CategoryCache categoryCache = categoryCacheService.retrieveCategoryCacheByCategoryElementCode(getServiceContextAdministrador(), CATEGORY_ELEMENT_ECONOMIA);

        assertEqualsCategoryCache(categoryCache);
    }

    @Test
    public void testRetrieveAllCategories() throws Exception {
        // NO TESTS.
    }

    @Test
    public void testUpdateCategoryCacheAll() throws Exception {
        // NO TEST
    }

    @Test
    public void testCreateCategoryCacheByCategoryElement() throws Exception {
        // NO TEST
    }

    private void assertEqualsCategoryCache(CategoryCache categoryCache) {
        assertEquals(categoryCache.getCategoryCode(), CATEGORY_CODE_ECONOMIA);
        assertEqualsInternationalString(categoryCache.getTitle(), "es", CATEGORY_TITLE_ECONOMIA_ES, "en", CATEGORY_TITLE_ECONOMIA_EN);
        assertEquals(categoryCache.getCategoryElementCode(), CATEGORY_ELEMENT_ECONOMIA);
        assertEquals(categoryCache.getUuid(), CATEGORY_UUID_ECONOMIA);
    }

    public static void assertEqualsInternationalString(InternationalString internationalString, String locale1, String label1, String locale2, String label2) {
        int count = 0;
        if (locale1 != null) {
            assertEquals(label1, internationalString.getLocalisedLabel(locale1));
            count++;
        }
        if (locale2 != null) {
            assertEquals(label2, internationalString.getLocalisedLabel(locale2));
            count++;
        }
        assertEquals(count, internationalString.getTexts().size());
    }

    @Override
    protected List<String> getTableNamesOrderedByFKDependency() {
        List<String> tables = new ArrayList<String>();
        tables.add("TB_CONFIGURATION");
        tables.add("TB_INTERNATIONAL_STRINGS");
        tables.add("TB_CATEGORY_CACHE");
        tables.add("TB_EXTERNAL_ITEMS");
        tables.add("TB_LIS_QUANTITIES_UNITS");
        tables.add("TB_LIS_UNITS_MULTIPLIERS");
        tables.add("TB_LIS_GEOGR_GRANULARITIES");
        tables.add("TB_LIS_GEOGR_VALUES");
        tables.add("TB_INDICATORS");
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
        tables.add("TB_LOCALISED_STRINGS");
        tables.add("TB_INDIC_VERSION_LAST_VALUE");
        tables.add("TB_INDIC_INST_LAST_VALUE");
        tables.add("TB_INDIC_INST_GEO_VALUES");
        tables.add("TB_INDICATORS_SYSTEMS_HIST");
        tables.add("TB_TRANSLATIONS");
        tables.add("TV_CONSULTA");
        return tables;
    }

    @Override
    protected List<String> getSequencesToRestart() {
        List<String> sequences = new ArrayList<String>();
        sequences.add("SEQ_I18NSTRS");
        sequences.add("SEQ_L10NSTRS");
        sequences.add("SEQ_EXTERNAL_ITEMS");
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
        sequences.add("SEQ_CATEGORY_CACHE");

        return sequences;
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/CategoryCacheServiceTest.xml";
    }

    @Override
    protected Map<String, List<String>> getTablePrimaryKeys() {
        if (tablePrimaryKeys == null) {
            tablePrimaryKeys = new HashMap<String, List<String>>();

            // NO PRIMARY KEYS TO ADD
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

    @Override
    protected String getDefaultSchema() {
        return defaultSchema;
    }

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

    private void putMetamacPrincipalInServiceContext(ServiceContext serviceContext, RoleEnum role) {
        MetamacPrincipal metamacPrincipal = new MetamacPrincipal();
        metamacPrincipal.setUserId(serviceContext.getUserId());
        metamacPrincipal.getAccesses().add(new MetamacPrincipalAccess(role.getName(), IndicatorsConstants.SECURITY_APPLICATION_ID, null));
        serviceContext.setProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE, metamacPrincipal);
    }

}
