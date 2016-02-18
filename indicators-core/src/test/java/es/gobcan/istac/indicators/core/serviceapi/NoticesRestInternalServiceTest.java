package es.gobcan.istac.indicators.core.serviceapi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.rest.notices.v1_0.domain.Notice;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.gobcan.istac.indicators.core.domain.Indicator;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.notices.ServiceNoticeAction;
import es.gobcan.istac.indicators.core.notices.ServiceNoticeMessage;
import es.gobcan.istac.indicators.core.service.NoticesRestInternalService;
import es.gobcan.istac.indicators.core.service.NoticesRestInternalServiceImpl;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsMocks;

import static org.junit.Assert.assertNotNull;;

@RunWith(SpringJUnit4ClassRunner.class)
// "classpath:spring/include/indicators-service-mockito.xml"
@ContextConfiguration(locations = {"classpath:spring/applicationContext-test.xml"})
@TransactionConfiguration(defaultRollback = true, transactionManager = "txManager")
@Transactional
// public class IndicatorsServiceTest extends IndicatorsBaseTest {
public class NoticesRestInternalServiceTest {

    private static final String MY_DATA_REPOSITORY_TABLE_NAME = "MYDATAREPOSITORYTABLENAME";
    private static final String MY_CODE = "MYCODE";
    private static final String MY_VIEW_CODE = "MYVIEWCODE";
    private static final String MY_DATA_VIEWS_ROLE = "MYDATAVIEWSROLE";
    private static final String MY_OLD_DATASET_ID = "MYOLDDATASETID";

    @Test
    public void testCreateNotice() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Method createNotice = NoticesRestInternalServiceImpl.class.getDeclaredMethod("createNotice", String.class, String.class, List.class, Object[].class);
        createNotice.setAccessible(true);

        IndicatorVersion failedIndicator = getMockedIndicatorVersion();

        Notice noticeCreateReplaceDataset = (Notice) createNotice
                .invoke(getNoticesRestInternalService(), ServiceNoticeAction.INDICATOR_CREATE_REPLACE_DATASET_ERROR, ServiceNoticeMessage.INDICATOR_CREATE_REPLACE_DATASET_ERROR,
                        Arrays.asList(failedIndicator), new Object[]{ failedIndicator.getIndicator().getViewCode(), failedIndicator.getDataRepositoryTableName() });
        
        assertNotNull(noticeCreateReplaceDataset.getMessages().getMessages().get(0).getResources().getResources().get(0).getManagementAppLink());
        assertNotNull(noticeCreateReplaceDataset.getMessages().getMessages().get(0).getResources().getResources().get(0).getSelfLink());

        Notice noticeAssignRolePermissionsDataset = (Notice) createNotice.invoke(getNoticesRestInternalService(), ServiceNoticeAction.INDICATOR_ASSIGN_ROLE_PERMISSIONS_DATASET_ERROR,
                ServiceNoticeMessage.INDICATOR_ASSIGN_ROLE_PERMISSIONS_DATASET_ERROR, new ArrayList<IndicatorVersion>(), new Object[]{ MY_DATA_VIEWS_ROLE, MY_VIEW_CODE });
        
        assertNotNull(noticeAssignRolePermissionsDataset.getMessages().getMessages().get(0).getText());        

        Notice noticeUpdateIndicatorsData = (Notice) createNotice.invoke(getNoticesRestInternalService(), ServiceNoticeAction.INDICATOR_POPULATION_ERROR,
                ServiceNoticeMessage.INDICATOR_POPULATION_ERROR, Arrays.asList(failedIndicator), new Object[]{});
        
        assertNotNull(noticeUpdateIndicatorsData.getMessages().getMessages().get(0).getResources().getResources().get(0).getManagementAppLink());
        assertNotNull(noticeUpdateIndicatorsData.getMessages().getMessages().get(0).getResources().getResources().get(0).getSelfLink());

        Notice noticeDeleteDataset = (Notice) createNotice.invoke(getNoticesRestInternalService(), ServiceNoticeAction.INDICATOR_DELETE_DATASET_ERROR,
                ServiceNoticeMessage.INDICATOR_DELETE_DATASET_ERROR, Arrays.asList(failedIndicator), new Object[]{ MY_OLD_DATASET_ID });
        
        assertNotNull(noticeDeleteDataset.getMessages().getMessages().get(0).getResources().getResources().get(0).getManagementAppLink());
        assertNotNull(noticeDeleteDataset.getMessages().getMessages().get(0).getResources().getResources().get(0).getSelfLink());
        
    }
    
    // Commented out to avoid sending multiple mails each time is tested. Intended for manual debug
    // @Test
    public void testCreateNotification() {        
        getNoticesRestInternalService().createAssignRolePermissionsDatasetErrorBackgroundNotification(MY_DATA_VIEWS_ROLE, MY_VIEW_CODE);
        getNoticesRestInternalService().createCreateReplaceDatasetErrorBackgroundNotification(getMockedIndicatorVersion());        
        getNoticesRestInternalService().createDeleteDatasetErrorBackgroundNotification(getMockedIndicatorVersion(), MY_OLD_DATASET_ID);
        getNoticesRestInternalService().createUpdateIndicatorsDataErrorBackgroundNotification(Arrays.asList(getMockedIndicatorVersion()));
        
    }

    private NoticesRestInternalService getNoticesRestInternalService() {
        return (NoticesRestInternalService) ApplicationContextProvider.getApplicationContext().getBean(NoticesRestInternalService.BEAN_ID);
    }

    private IndicatorVersion getMockedIndicatorVersion() {
        IndicatorVersion indicatorVersion = new IndicatorVersion();
        indicatorVersion.setIndicator(new Indicator());
        indicatorVersion.getIndicator().setCode(MY_CODE);
        indicatorVersion.getIndicator().setViewCode(MY_VIEW_CODE);
        indicatorVersion.setDataRepositoryTableName(MY_DATA_REPOSITORY_TABLE_NAME);
        indicatorVersion.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorVersion.setSubjectCode(IndicatorsMocks.mockString(10));
        indicatorVersion.setSubjectTitle(IndicatorsMocks.mockInternationalString());
        return indicatorVersion;
    }
}
