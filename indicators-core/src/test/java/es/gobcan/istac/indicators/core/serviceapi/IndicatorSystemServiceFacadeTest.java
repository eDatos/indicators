package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsAsserts;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsMocks;

/**
 * Spring based transactional test with DbUnit support.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext-test.xml" })
public class IndicatorSystemServiceFacadeTest extends IndicatorsBaseTests /*implements IndicatorSystemServiceFacadeTestBase */{ // TODO descomentar implements

    @Autowired
    protected IndicatorSystemServiceFacade indicatorSystemServiceFacade;
    
    @Test
    public void testCreateIndicatorSystem() throws Exception {
        IndicatorSystemDto indicatorSystemDto = new IndicatorSystemDto();
        indicatorSystemDto.setCode(IndicatorsMocks.mockString(10));
        indicatorSystemDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorSystemDto.setAcronym(IndicatorsMocks.mockInternationalString());
        indicatorSystemDto.setUri(IndicatorsMocks.mockString(100));
        indicatorSystemDto.setObjetive(IndicatorsMocks.mockInternationalString());
        indicatorSystemDto.setDescription(IndicatorsMocks.mockInternationalString());
        
        // Create
        IndicatorSystemDto indicatorSystemDtoCreated = indicatorSystemServiceFacade.createIndicatorSystem(getServiceContext(), indicatorSystemDto);
        
        // Validate
        assertNotNull(indicatorSystemDtoCreated);
        assertNotNull(indicatorSystemDtoCreated.getUuid());
        assertNotNull(indicatorSystemDtoCreated.getVersionNumber());
        IndicatorSystemDto indicatorSystemDtoRetrieved = indicatorSystemServiceFacade.retrieveIndicatorSystem(getServiceContext(), indicatorSystemDtoCreated.getUuid(), indicatorSystemDtoCreated.getVersionNumber());
        IndicatorsAsserts.assertEqualsIndicatorSystem(indicatorSystemDto, indicatorSystemDtoRetrieved);
        
        // Audit
        assertEquals(getServiceContext().getUserId(), indicatorSystemDtoCreated.getCreatedBy());
        assertTrue(DateUtils.isSameDay(new Date(), indicatorSystemDtoCreated.getCreatedDate()));
        assertTrue(DateUtils.isSameDay(new Date(), indicatorSystemDtoCreated.getLastUpdated()));
        assertEquals(getServiceContext().getUserId(), indicatorSystemDtoCreated.getLastUpdatedBy());
    }
    
    @Test
    public void testCreateIndicatorSystemCodeRequired() throws Exception {
        
        IndicatorSystemDto indicatorSystemDto = new IndicatorSystemDto();
        indicatorSystemDto.setCode(null);
        indicatorSystemDto.setTitle(IndicatorsMocks.mockInternationalString());
        
        try {
            indicatorSystemServiceFacade.createIndicatorSystem(getServiceContext(), indicatorSystemDto);
            fail("code required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_VALIDATION_METADATA_REQUIRED.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("CODE", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }
    
    // TODO REVISAR LOS metadatos no repetibles y obligatorios
    @Test
    public void testCreateIndicatorSystemCodeDuplicated() throws Exception {
        
        IndicatorSystemDto indicatorSystemDto = new IndicatorSystemDto();
        indicatorSystemDto.setCode("CODE-1");
        indicatorSystemDto.setTitle(IndicatorsMocks.mockInternationalString());
        
        try {
            indicatorSystemServiceFacade.createIndicatorSystem(getServiceContext(), indicatorSystemDto);
            fail("code duplicated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORY_SYSTEM_ALREADY_EXIST_CODE_DUPLICATED.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorSystemDto.getCode(), e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }
    

//    @Test
//    public void testMakeDraftIndicatorSystem() throws Exception {
//        // TODO Auto-generated method stub
////        fail("testMakeDraftIndicatorSystem not implemented");
//    }
//
//    @Test
//    public void testUpdateIndicatorSystem() throws Exception {
//        // TODO Auto-generated method stub
////        fail("testUpdateIndicatorSystem not implemented");
//    }
//
//    @Test
//    public void testPublishIndicatorSystem() throws Exception {
//        // TODO Auto-generated method stub
////        fail("testPublishIndicatorSystem not implemented");
//    }
//
//    @Test
//    public void testRetrieveIndicatorSystem() throws Exception {
//        // TODO Auto-generated method stub
////        fail("testRetrieveIndicatorSystem not implemented");
//    }
//
//    @Test
//    public void testDeleteIndicatorSystem() throws Exception {
//        // TODO Auto-generated method stub
////        fail("testDeleteIndicatorSystem not implemented");
//    }    
    
    @Override
    protected String getDataSetFile() {
        return "dbunit/IndicatorSystemServiceFacadeTest.xml";
    }

    @Override
    protected List<String> getTablesToRemoveContent() {
        List<String> tables = new ArrayList<String>();
        tables.add("TBL_LOCALISED_STRINGS");
        tables.add("TBL_INDICATOR_SYSTEM_VERS");
        tables.add("TBL_INDICATORS_SYSTEMS");
        tables.add("TBL_INTERNATIONAL_STRINGS");
        return tables;
    }
}
