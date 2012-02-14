package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsMocks;

/**
 * Spring based transactional test with DbUnit support.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-test.xml"})
public class IndicatorsServiceFacadeTest extends IndicatorsBaseTest implements IndicatorsServiceFacadeTestBase {

    @Autowired
    protected IndicatorsServiceFacade indicatorsServiceFacade;

    private static String             NOT_EXISTS = "not-exists";

    @Test
    public void testCreateIndicator() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode(IndicatorsMocks.mockString(10));
        indicatorDto.setName(IndicatorsMocks.mockInternationalString());
        indicatorDto.setAcronym(IndicatorsMocks.mockInternationalString());
        indicatorDto.setSubjectCode(IndicatorsMocks.mockString(10));
        indicatorDto.setCommentary(IndicatorsMocks.mockInternationalString());
        indicatorDto.setNotes(IndicatorsMocks.mockInternationalString());
        indicatorDto.setNoteUrl(IndicatorsMocks.mockString(100));
        
        // Create
        IndicatorDto indicatorDtoCreated = indicatorsServiceFacade.createIndicator(getServiceContext(), indicatorDto);

        // Validate
        assertNotNull(indicatorDtoCreated);
        assertNotNull(indicatorDtoCreated.getUuid());
        assertNotNull(indicatorDtoCreated.getVersionNumber());
        
        // TODO OBTENER INDICADOR
//        IndicatorDto indicatorDtoRetrieved = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), indicatorDtoCreated.getUuid(),
//                indicatorDtoCreated.getVersionNumber());
//        assertEquals(IndicatorStateEnum.DRAFT, indicatorDtoCreated.getState());
//        assertEquals(IndicatorStateEnum.DRAFT, indicatorDtoRetrieved.getState());
//        assertEquals("1.000", indicatorDtoRetrieved.getProductionVersion());
//        assertNull(indicatorDtoRetrieved.getDiffusionVersion());
//        assertNull(indicatorDtoRetrieved.getProductionValidationDate());
//        assertNull(indicatorDtoRetrieved.getProductionValidationUser());
//        assertNull(indicatorDtoRetrieved.getDiffusionValidationDate());
//        assertNull(indicatorDtoRetrieved.getDiffusionValidationUser());
//        assertNull(indicatorDtoRetrieved.getPublicationDate());
//        assertNull(indicatorDtoRetrieved.getPublicationUser());
//        assertNull(indicatorDtoRetrieved.getArchiveDate());
//        assertNull(indicatorDtoRetrieved.getArchiveUser());
//
//        IndicatorsAsserts.assertEqualsIndicator(indicatorDto, indicatorDtoRetrieved);
//
        // Validate audit
        assertEquals(getServiceContext().getUserId(), indicatorDtoCreated.getCreatedBy());
        assertTrue(DateUtils.isSameDay(new Date(), indicatorDtoCreated.getCreatedDate()));
        assertTrue(DateUtils.isSameDay(new Date(), indicatorDtoCreated.getLastUpdated()));
        assertEquals(getServiceContext().getUserId(), indicatorDtoCreated.getLastUpdatedBy());
    }

    @Test
    public void testCreateIndicatorCodeRequired() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode(null);
        indicatorDto.setName(IndicatorsMocks.mockInternationalString());

        try {
            indicatorsServiceFacade.createIndicator(getServiceContext(), indicatorDto);
            fail("code required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_VALIDATION_METADATA_REQUIRED.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("INDICATOR.CODE", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorNameRequired() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode(IndicatorsMocks.mockString(10));
        indicatorDto.setName(null);

        try {
            indicatorsServiceFacade.createIndicator(getServiceContext(), indicatorDto);
            fail("name required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_VALIDATION_METADATA_REQUIRED.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("INDICATOR.NAME", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

//    // TODO code unique
//    @Test
//    public void testCreateIndicatorCodeDuplicated() throws Exception {
//
//        IndicatorDto indicatorDto = new IndicatorDto();
//        indicatorDto.setCode("CoDe-1");
//        indicatorDto.setName(IndicatorsMocks.mockInternationalString());
//
//        try {
//            indicatorsServiceFacade.createIndicator(getServiceContext(), indicatorDto);
//            fail("code duplicated");
//        } catch (MetamacException e) {
//            assertEquals(1, e.getExceptionItems().size());
//            assertEquals(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_ALREADY_EXIST_CODE_DUPLICATED.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
//            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
//            assertEquals(indicatorDto.getCode(), e.getExceptionItems().get(0).getMessageParameters()[0]);
//        }
//    }
//
// // TODO code unique
//    @Test
//    public void testCreateIndicatorCodeDuplicatedInsensitive() throws Exception {
//
//        IndicatorDto indicatorDto = new IndicatorDto();
//        indicatorDto.setCode("CoDe-1");
//        indicatorDto.setName(IndicatorsMocks.mockInternationalString());
//
//        try {
//            indicatorsServiceFacade.createIndicator(getServiceContext(), indicatorDto);
//            fail("code duplicated");
//        } catch (MetamacException e) {
//            assertEquals(1, e.getExceptionItems().size());
//            assertEquals(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_ALREADY_EXIST_CODE_DUPLICATED.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
//            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
//            assertEquals(indicatorDto.getCode(), e.getExceptionItems().get(0).getMessageParameters()[0]);
//        }
//    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/IndicatorsServiceFacadeTest.xml";
    }
}
