package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.PersistenceException;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.commons.lang3.StringUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.hibernate.exception.ConstraintViolationException;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.srm.core.stream.message.DatetimeAvro;
import org.siemac.metamac.srm.core.stream.message.VariableElementAvro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.GeographicalValueRepository;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorInstanceProperties;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystem;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemHistory;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.error.ServiceExceptionParameters;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.service.SrmRestInternalService;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsAsserts;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsMocks;
import es.gobcan.istac.indicators.core.serviceapi.utils.SrmResourcesMocks;

/**
 * Test to IndicatorsSystemService. Testing: indicators systems, dimensions, indicators instances
 * Spring based transactional test with DbUnit support.
 * Only testing properties are not in Dto
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/include/indicators-notices-service-mockito.xml", "classpath:spring/include/indicators-srm-service-mockito.xml",
        "classpath:spring/include/indicators-data-service-mockito.xml", "classpath:spring/applicationContext-test.xml"})
@TransactionConfiguration(defaultRollback = true, transactionManager = "txManager")
@Transactional
public class IndicatorsSystemsServiceTest extends IndicatorsBaseTest {

    List<GeographicalValue>                                                       geographicalValues = new ArrayList<GeographicalValue>();

    @Autowired
    protected IndicatorsSystemsService                                            indicatorsSystemService;

    @Autowired
    private IndicatorsDataService                                                 indicatorsDataService;

    @Autowired
    private IndicatorsDataProviderService                                         indicatorsDataProviderService;

    @Autowired
    private SrmRestInternalService                                                srmRestInternalService;

    @Autowired
    private GeographicalValueRepository                                           geographicalValueRepository;

    @Autowired
    protected es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService configurationService;

    @Before
    public void setUp() throws MetamacException {
        geographicalValues = indicatorsSystemService.findAllGeographicalValues(getServiceContextAdministrador());
        Map<String, String> geographicalVariableElementsByCode = SrmResourcesMocks.buildVariableElementsIdByCodeOfCodelist(geographicalValues);
        when(srmRestInternalService.retrieveVariableElementsIdByCodesOfCodelists(Matchers.any(String.class))).thenReturn(geographicalVariableElementsByCode);

        when(srmRestInternalService.retrieveCodelistLastVersion(configurationService.retrieveDefaultGeographicalCodeListUrn()))
                .thenReturn(SrmResourcesMocks.buildCodelist("defaultJsonstat", "defaultCodelistJsonstat", "es", "urn:defaultCodelistJsonstat"));
    }

    @Test
    public void testCreateIndicatorsSystem() throws Exception {

        IndicatorsSystemVersion indicatorsSystemVersion = new IndicatorsSystemVersion();
        indicatorsSystemVersion.setIndicatorsSystem(new IndicatorsSystem());
        indicatorsSystemVersion.getIndicatorsSystem().setCode(IndicatorsMocks.mockString(10));
        indicatorsSystemVersion.getIndicatorsSystem().setIsOperational(true);
        // Create
        IndicatorsSystemVersion indicatorsSystemVersionCreated = indicatorsSystemService.createIndicatorsSystem(getServiceContextAdministrador(), indicatorsSystemVersion);
        assertEquals(getServiceContextAdministrador().getUserId(), indicatorsSystemVersionCreated.getCreatedBy());
        assertEquals(getServiceContextAdministrador().getUserId(), indicatorsSystemVersionCreated.getLastUpdatedBy());

        // Validate properties are not in Dto
        String uuid = indicatorsSystemVersionCreated.getIndicatorsSystem().getUuid();
        String version = indicatorsSystemVersionCreated.getVersionNumber();
        IndicatorsSystemVersion indicatorsSystemCreated = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, version);
        assertFalse(indicatorsSystemCreated.getIndicatorsSystem().getIsPublished());
        assertTrue(indicatorsSystemVersionCreated.getIsLastVersion());
        assertNull(indicatorsSystemCreated.getIndicatorsSystem().getDiffusionVersion());
    }

    @Test
    public void testCreateIndicatorsSystemNonOperational() throws Exception {

        IndicatorsSystemVersion indicatorsSystemVersion = new IndicatorsSystemVersion();
        indicatorsSystemVersion.setIndicatorsSystem(new IndicatorsSystem());
        indicatorsSystemVersion.getIndicatorsSystem().setCode(IndicatorsMocks.mockString(10));
        indicatorsSystemVersion.getIndicatorsSystem().setIsOperational(false);

        // non operational metadata
        indicatorsSystemVersion.setTitle(IndicatorsMocks.mockInternationalString("es", "title-1"));
        indicatorsSystemVersion.setAcronym(IndicatorsMocks.mockInternationalString("es", "acronym-1"));
        indicatorsSystemVersion.setDescription(IndicatorsMocks.mockInternationalString("es", "description-1"));
        indicatorsSystemVersion.setObjective(IndicatorsMocks.mockInternationalString("es", "objective-1"));

        // Create
        IndicatorsSystemVersion indicatorsSystemVersionCreated = indicatorsSystemService.createIndicatorsSystem(getServiceContextAdministrador(), indicatorsSystemVersion);
        assertEquals(getServiceContextAdministrador().getUserId(), indicatorsSystemVersionCreated.getCreatedBy());
        assertEquals(getServiceContextAdministrador().getUserId(), indicatorsSystemVersionCreated.getLastUpdatedBy());

        // Validate properties are not in Dto
        String uuid = indicatorsSystemVersionCreated.getIndicatorsSystem().getUuid();
        String version = indicatorsSystemVersionCreated.getVersionNumber();
        IndicatorsSystemVersion indicatorsSystemCreated = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, version);
        assertFalse(indicatorsSystemCreated.getIndicatorsSystem().getIsPublished());
        assertTrue(indicatorsSystemVersionCreated.getIsLastVersion());
        assertNull(indicatorsSystemCreated.getIndicatorsSystem().getDiffusionVersion());

        assertNotNull(indicatorsSystemCreated.getTitle());
        assertNotNull(indicatorsSystemCreated.getAcronym());
        assertNotNull(indicatorsSystemCreated.getDescription());
        assertNotNull(indicatorsSystemCreated.getObjective());
        assertEquals(Boolean.FALSE, indicatorsSystemCreated.getIndicatorsSystem().getIsOperational());
    }

    @Test
    public void testDeleteIndicatorWithPublishedAndDraft() throws Exception {

        String uuid = INDICATORS_SYSTEM_1;

        // Retrieve before delete
        {
            IndicatorsSystemVersion indicatorsSystemVersion1 = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, IndicatorsDataBaseTest.INIT_VERSION);
            assertFalse(indicatorsSystemVersion1.getIsLastVersion());
            IndicatorsSystemVersion indicatorsSystemVersion2 = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, IndicatorsDataBaseTest.SECOND_VERSION);
            assertTrue(indicatorsSystemVersion2.getIsLastVersion());
        }

        // Delete indicator
        indicatorsSystemService.deleteIndicatorsSystem(getServiceContextAdministrador(), uuid);

        // Validation
        // Retrieve after delete
        {
            IndicatorsSystemVersion indicatorsSystemVersion1 = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, IndicatorsDataBaseTest.INIT_VERSION);
            assertTrue(indicatorsSystemVersion1.getIsLastVersion());
            assertNull(indicatorsSystemVersion1.getIndicatorsSystem().getProductionVersion());
        }
    }

    @Test
    public void testVersioningIndicatorsSystem() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;

        // Retrieve before versioning
        {
            IndicatorsSystemVersion indicatorsSystemVersion1 = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, INDICATORS_SYSTEM_3_VERSION);
            assertTrue(indicatorsSystemVersion1.getIsLastVersion());
        }

        // Versioning
        IndicatorsSystemVersion newVersion = indicatorsSystemService.versioningIndicatorsSystem(getServiceContextAdministrador(), uuid, VersionTypeEnum.MAJOR);

        // Retrieve after delete
        {
            IndicatorsSystemVersion indicatorsSystemVersion1 = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, INDICATORS_SYSTEM_3_VERSION);
            assertFalse(indicatorsSystemVersion1.getIsLastVersion());
            IndicatorsSystemVersion indicatorsSystemVersion2 = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, newVersion.getVersionNumber());
            assertTrue(indicatorsSystemVersion2.getIsLastVersion());
        }
    }

    @Test
    public void testVersioningIndicatorsSystemMaximumMinorVersionReached() throws Exception {

        String uuid = INDICATORS_SYSTEM_11;

        // Retrieve before versioning
        {
            IndicatorsSystemVersion indicatorsSystemVersion1 = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, INDICATORS_SYSTEM_11_VERSION);
            assertEquals(IndicatorsDataBaseTest.INIT_VERSION_MAXIMUM_MINOR_VERSION, indicatorsSystemVersion1.getVersionNumber());
        }

        // Versioning
        indicatorsSystemService.versioningIndicatorsSystem(getServiceContextAdministrador(), uuid, VersionTypeEnum.MINOR);

        // Retrieve after delete
        {
            IndicatorsSystemVersion indicatorsSystemVersion1 = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, INDICATORS_SYSTEM_11_VERSION);
            assertEquals(IndicatorsDataBaseTest.INIT_VERSION_MAXIMUM_MINOR_VERSION, indicatorsSystemVersion1.getVersionNumber());
            IndicatorsSystemVersion indicatorsSystemVersion2 = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, IndicatorsDataBaseTest.SECOND_VERSION);
            assertEquals(IndicatorsDataBaseTest.SECOND_VERSION, indicatorsSystemVersion2.getVersionNumber());
        }
    }

    @Test
    public void testVersioningIndicatorsSystemMinorVersionLimitReachedError() throws Exception {

        String uuid = INDICATORS_SYSTEM_12;

        // Retrieve before versioning
        {
            IndicatorsSystemVersion indicatorsSystemVersion1 = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, INDICATORS_SYSTEM_12_VERSION);
            assertEquals(IndicatorsDataBaseTest.MAXIMUM_LIMIT_VERSION, indicatorsSystemVersion1.getVersionNumber());
        }

        // Minor versioning
        expectedMetamacException(MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.RESOURCE_MAXIMUM_VERSION_REACHED)
                .withMessageParameters(VersionTypeEnum.MINOR, INDICATORS_SYSTEM_12_VERSION).build());

        indicatorsSystemService.versioningIndicatorsSystem(getServiceContextAdministrador(), uuid, VersionTypeEnum.MINOR);
        fail("Should not allow minor versioning when the indicator system version number rearches 99999.9");
    }

    @Test
    public void testVersioningIndicatorsSystemMajorVersionLimitReachedError() throws Exception {

        String uuid = INDICATORS_SYSTEM_12;

        // Retrieve before versioning
        {
            IndicatorsSystemVersion indicatorsSystemVersion1 = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, INDICATORS_SYSTEM_12_VERSION);
            assertEquals(IndicatorsDataBaseTest.MAXIMUM_LIMIT_VERSION, indicatorsSystemVersion1.getVersionNumber());
        }

        // Major versioning
        expectedMetamacException(MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.RESOURCE_MAXIMUM_VERSION_REACHED)
                .withMessageParameters(VersionTypeEnum.MAJOR, INDICATORS_SYSTEM_12_VERSION).build());

        indicatorsSystemService.versioningIndicatorsSystem(getServiceContextAdministrador(), uuid, VersionTypeEnum.MAJOR);
        fail("Should not allow major versioning when the indicator system version number rearches 99999.9");
    }


    @Test
    public void testArchiveIndicatorsSystem() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;
        String versionNumber = INDICATORS_SYSTEM_3_VERSION;

        // Archive
        indicatorsSystemService.archiveIndicatorsSystem(getServiceContextAdministrador(), uuid);

        // Validate properties are not in Dto
        IndicatorsSystemVersion indicatorsSystemCreated = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, versionNumber);
        assertFalse(indicatorsSystemCreated.getIndicatorsSystem().getIsPublished());
    }

    @Test
    public void testFindIndicatorsInstancesInPublishedIndicatorsSystems() throws Exception {
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(IndicatorInstance.class).distinctRoot()
                    .withProperty(IndicatorInstanceProperties.lastValuesCache().geographicalCode()).eq(SrmResourcesMocks.GEOGRAPHICAL_CODE_VALUE_ES).orderBy(IndicatorInstanceProperties.uuid())
                    .ascending().build();

            PagingParameter paging = PagingParameter.pageAccess(10);

            PagedResult<IndicatorInstance> indicatorsInstances = indicatorsSystemService.findIndicatorsInstancesInPublishedIndicatorsSystems(getServiceContextAdministrador(), conditions, paging);

            assertEquals(6, indicatorsInstances.getValues().size());

            assertTrue(contains(indicatorsInstances.getValues(), INDICATORS_SYSTEM_1_IINSTANCE_1));
            assertTrue(contains(indicatorsInstances.getValues(), INDICATORS_SYSTEM_10_IINSTANCE_2));
            assertTrue(contains(indicatorsInstances.getValues(), INDICATORS_SYSTEM_10_IINSTANCE_3));
            assertTrue(contains(indicatorsInstances.getValues(), INDICATORS_SYSTEM_3_IINSTANCE_1A));
            assertTrue(contains(indicatorsInstances.getValues(), INDICATORS_SYSTEM_3_IINSTANCE_2));
            assertTrue(contains(indicatorsInstances.getValues(), INDICATORS_SYSTEM_6_IINSTANCE_2));
        }
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(IndicatorInstance.class).distinctRoot()
                    .withProperty(IndicatorInstanceProperties.lastValuesCache().geographicalCode()).eq(SrmResourcesMocks.GEOGRAPHICAL_CODE_VALUE_ES611).orderBy(IndicatorInstanceProperties.uuid())
                    .ascending().build();

            PagingParameter paging = PagingParameter.pageAccess(10);

            PagedResult<IndicatorInstance> indicatorsInstances = indicatorsSystemService.findIndicatorsInstancesInPublishedIndicatorsSystems(getServiceContextAdministrador(), conditions, paging);

            assertEquals(2, indicatorsInstances.getValues().size());

            assertTrue(contains(indicatorsInstances.getValues(), INDICATORS_SYSTEM_10_IINSTANCE_1));
            assertTrue(contains(indicatorsInstances.getValues(), INDICATORS_SYSTEM_6_IINSTANCE_1));
        }
    }

    @Test
    public void testFindIndicatorsInstancesInPublishedIndicatorsSystemsFilteredBySystem() throws Exception {
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(IndicatorInstance.class).distinctRoot()
                    .withProperty(IndicatorInstanceProperties.lastValuesCache().geographicalCode()).eq(SrmResourcesMocks.GEOGRAPHICAL_CODE_VALUE_ES)
                    .withProperty(IndicatorInstanceProperties.elementLevel().indicatorsSystemVersion().indicatorsSystem().uuid()).eq(INDICATORS_SYSTEM_1).orderBy(IndicatorInstanceProperties.uuid())
                    .ascending().build();

            PagingParameter paging = PagingParameter.pageAccess(10);

            PagedResult<IndicatorInstance> indicatorsInstances = indicatorsSystemService.findIndicatorsInstancesInPublishedIndicatorsSystems(getServiceContextAdministrador(), conditions, paging);

            assertEquals(1, indicatorsInstances.getValues().size());

            assertEquals(INDICATORS_SYSTEM_1_IINSTANCE_1, indicatorsInstances.getValues().get(0).getUuid());
        }
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(IndicatorInstance.class).distinctRoot()
                    .withProperty(IndicatorInstanceProperties.lastValuesCache().geographicalCode()).eq(SrmResourcesMocks.GEOGRAPHICAL_CODE_VALUE_ES)
                    .withProperty(IndicatorInstanceProperties.elementLevel().indicatorsSystemVersion().indicatorsSystem().uuid()).eq(INDICATORS_SYSTEM_3).orderBy(IndicatorInstanceProperties.uuid())
                    .ascending().build();

            PagingParameter paging = PagingParameter.pageAccess(10);

            PagedResult<IndicatorInstance> indicatorsInstances = indicatorsSystemService.findIndicatorsInstancesInPublishedIndicatorsSystems(getServiceContextAdministrador(), conditions, paging);

            assertEquals(2, indicatorsInstances.getValues().size());

            assertEquals(INDICATORS_SYSTEM_3_IINSTANCE_1A, indicatorsInstances.getValues().get(0).getUuid());
            assertEquals(INDICATORS_SYSTEM_3_IINSTANCE_2, indicatorsInstances.getValues().get(1).getUuid());
        }
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(IndicatorInstance.class).distinctRoot()
                    .withProperty(IndicatorInstanceProperties.lastValuesCache().geographicalCode()).eq(SrmResourcesMocks.GEOGRAPHICAL_CODE_VALUE_ES)
                    .withProperty(IndicatorInstanceProperties.elementLevel().indicatorsSystemVersion().indicatorsSystem().uuid()).eq(INDICATORS_SYSTEM_10).orderBy(IndicatorInstanceProperties.uuid())
                    .ascending().build();

            PagingParameter paging = PagingParameter.pageAccess(10);

            PagedResult<IndicatorInstance> indicatorsInstances = indicatorsSystemService.findIndicatorsInstancesInPublishedIndicatorsSystems(getServiceContextAdministrador(), conditions, paging);

            assertEquals(2, indicatorsInstances.getValues().size());

            assertEquals(INDICATORS_SYSTEM_10_IINSTANCE_2, indicatorsInstances.getValues().get(0).getUuid());
            assertEquals(INDICATORS_SYSTEM_10_IINSTANCE_3, indicatorsInstances.getValues().get(1).getUuid());
        }
    }

    @Test
    public void testFindIndicatorsInstancesInLastVersionIndicatorsSystemsFilteredBySystem() throws Exception {
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(IndicatorInstance.class).distinctRoot()
                    .withProperty(IndicatorInstanceProperties.elementLevel().indicatorsSystemVersion().indicatorsSystem().uuid()).eq(INDICATORS_SYSTEM_1).orderBy(IndicatorInstanceProperties.uuid())
                    .ascending().build();

            PagingParameter paging = PagingParameter.pageAccess(10);

            PagedResult<IndicatorInstance> indicatorsInstances = indicatorsSystemService.findIndicatorsInstancesInLastVersionIndicatorsSystems(getServiceContextAdministrador(), conditions, paging);

            assertEquals(3, indicatorsInstances.getValues().size());

            assertEquals(INDICATORS_SYSTEM_1_V2_IINSTANCE_1, indicatorsInstances.getValues().get(0).getUuid());
            assertEquals(INDICATORS_SYSTEM_1_V2_IINSTANCE_2, indicatorsInstances.getValues().get(1).getUuid());
            assertEquals(INDICATORS_SYSTEM_1_V2_IINSTANCE_3, indicatorsInstances.getValues().get(2).getUuid());
        }
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(IndicatorInstance.class).distinctRoot()
                    .withProperty(IndicatorInstanceProperties.elementLevel().indicatorsSystemVersion().indicatorsSystem().uuid()).eq(INDICATORS_SYSTEM_3).orderBy(IndicatorInstanceProperties.uuid())
                    .ascending().build();

            PagingParameter paging = PagingParameter.pageAccess(10);

            PagedResult<IndicatorInstance> indicatorsInstances = indicatorsSystemService.findIndicatorsInstancesInLastVersionIndicatorsSystems(getServiceContextAdministrador(), conditions, paging);

            assertEquals(2, indicatorsInstances.getValues().size());

            assertEquals(INDICATORS_SYSTEM_3_IINSTANCE_1A, indicatorsInstances.getValues().get(0).getUuid());
            assertEquals(INDICATORS_SYSTEM_3_IINSTANCE_2, indicatorsInstances.getValues().get(1).getUuid());
        }
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(IndicatorInstance.class).distinctRoot()
                    .withProperty(IndicatorInstanceProperties.elementLevel().indicatorsSystemVersion().indicatorsSystem().uuid()).eq(INDICATORS_SYSTEM_10).orderBy(IndicatorInstanceProperties.uuid())
                    .ascending().build();

            PagingParameter paging = PagingParameter.pageAccess(10);

            PagedResult<IndicatorInstance> indicatorsInstances = indicatorsSystemService.findIndicatorsInstancesInLastVersionIndicatorsSystems(getServiceContextAdministrador(), conditions, paging);

            assertEquals(1, indicatorsInstances.getValues().size());

            assertEquals(INDICATORS_SYSTEM_10_V2_IINSTANCE_1, indicatorsInstances.getValues().get(0).getUuid());
        }
    }

    @Test
    @Transactional
    public void testCreateGeographicalValue() throws Exception {
        String code = "CANARIAS";
        VariableElementAvro variableElementAvro = IndicatorsMocks.mockVariableElementAvro(code, "VARIABLE_TERRITORY", GEOGRAPHICAL_GRANULARITY_2);
        SpecificRecordBase message = variableElementAvro;

        // Create
        indicatorsSystemService.updateGeopgraphicalValuesFromSrmVariableElements(getServiceContextAdministrador(), message);
        GeographicalValue geographicalValueCreated = geographicalValueRepository.findGeographicalValueByCode(variableElementAvro.getCode());
        // Validate
        IndicatorsAsserts.assertEqualsGeographicalValue(variableElementAvro, geographicalValueCreated);

        // Audit validations
        assertNotNull(geographicalValueCreated.getCreatedBy());
        assertNotNull(geographicalValueCreated.getCreatedDate());
        assertNotNull(geographicalValueCreated.getLastUpdated());
        assertNotNull(geographicalValueCreated.getLastUpdatedBy());
        assertEquals(getServiceContextAdministrador().getUserId(), geographicalValueCreated.getCreatedBy());
        assertEquals(getServiceContextAdministrador().getUserId(), geographicalValueCreated.getLastUpdatedBy());
    }

    @Test
    @Transactional
    public void testCreateGeographicalValueErrorGeographicalValueRequired() throws Exception {
        try {
            VariableElementAvro variableElementAvro = null;
            SpecificRecordBase message = variableElementAvro;

            // Create
            indicatorsSystemService.updateGeopgraphicalValuesFromSrmVariableElements(getServiceContextAdministrador(), message);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.GEOGRAPHICAL_VALUE, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateGeographicalValueErrorGeographicalValueCodeRequired() throws Exception {
        try {
            String code = "CANARIAS";
            VariableElementAvro variableElementAvro = IndicatorsMocks.mockVariableElementAvro(code, VARIABLE_TERRITORY_CODE, GEOGRAPHICAL_GRANULARITY_2);
            variableElementAvro.setCode(null);
            SpecificRecordBase message = variableElementAvro;

            // Create
            indicatorsSystemService.updateGeopgraphicalValuesFromSrmVariableElements(getServiceContextAdministrador(), message);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.GEOGRAPHICAL_VALUE_CODE, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateGeographicalValueErrorGranularityRequired() throws Exception {
        try {
            String code = "CANARIAS";
            VariableElementAvro variableElementAvro = IndicatorsMocks.mockVariableElementAvro(code, VARIABLE_TERRITORY_CODE, GEOGRAPHICAL_GRANULARITY_2);
            variableElementAvro.setGeographicGranularities(null);
            SpecificRecordBase message = variableElementAvro;

            // Create
            indicatorsSystemService.updateGeopgraphicalValuesFromSrmVariableElements(getServiceContextAdministrador(), message);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.GEOGRAPHICAL_VALUE_GRANULARITY, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateGeographicalValueErrorGranularityRequiredEmpty() throws Exception {
        try {
            String code = "CANARIAS";
            VariableElementAvro variableElementAvro = IndicatorsMocks.mockVariableElementAvro(code, VARIABLE_TERRITORY_CODE, StringUtils.EMPTY);
            variableElementAvro.setGeographicGranularities(null);
            SpecificRecordBase message = variableElementAvro;

            // Create
            indicatorsSystemService.updateGeopgraphicalValuesFromSrmVariableElements(getServiceContextAdministrador(), message);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.GEOGRAPHICAL_VALUE_GRANULARITY, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testUpdateGeographicalValue() throws Exception {
        VariableElementAvro variableElementAvro = IndicatorsMocks.mockVariableElementAvro(GEOGRAPHICAL_VALUE_CODE_1, VARIABLE_TERRITORY_CODE, GEOGRAPHICAL_GRANULARITY_4);
        variableElementAvro.setLatitud(22.232511);
        variableElementAvro.setLongitud(41232.254112);

        SpecificRecordBase message = variableElementAvro;

        // update
        indicatorsSystemService.updateGeopgraphicalValuesFromSrmVariableElements(getServiceContextAdministrador(), message);

        GeographicalValue geographicalValueUpdated = geographicalValueRepository.findGeographicalValueByCode(variableElementAvro.getCode());

        // Validate
        IndicatorsAsserts.assertEqualsGeographicalValue(variableElementAvro, geographicalValueUpdated);
        assertTrue(geographicalValueUpdated.getLastUpdated().isAfter(geographicalValueUpdated.getCreatedDate()));
    }

    @Test
    @Transactional
    public void testUpdateGeographicalGranularityNotExists() throws Exception {
        VariableElementAvro variableElementAvro = IndicatorsMocks.mockVariableElementAvro(GEOGRAPHICAL_VALUE_CODE_1, VARIABLE_TERRITORY_CODE, GEOGRAPHICAL_VALUE_1);
        variableElementAvro.setGeographicGranularities(null);
        SpecificRecordBase message = variableElementAvro;

        try {
            // update
            indicatorsSystemService.updateGeopgraphicalValuesFromSrmVariableElements(getServiceContextAdministrador(), message);
            fail("geographical value not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.GEOGRAPHICAL_VALUE_GRANULARITY, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testDeleteGeographicalValue() throws Exception {
        VariableElementAvro variableElementAvro = IndicatorsMocks.mockVariableElementAvro(GEOGRAPHICAL_VALUE_CODE_10, VARIABLE_TERRITORY_CODE, GEOGRAPHICAL_GRANULARITY_4);
        variableElementAvro.setValidTo(DatetimeAvro.newBuilder().setInstant((new DateTime()).getMillis()).build());

        SpecificRecordBase message = variableElementAvro;

        // delete
        indicatorsSystemService.updateGeopgraphicalValuesFromSrmVariableElements(getServiceContextAdministrador(), message);

        GeographicalValue geoValue = geographicalValueRepository.findGeographicalValueByCode(variableElementAvro.getCode());
        assertNull(geoValue);

    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/IndicatorsServiceFacadeIndicatorsSystemsTest.xml";
    }

    private boolean contains(List<IndicatorInstance> instances, String uuid) {
        for (IndicatorInstance instance : instances) {
            if (uuid.equals(instance.getUuid())) {
                return true;
            }
        }
        return false;
    }
}
