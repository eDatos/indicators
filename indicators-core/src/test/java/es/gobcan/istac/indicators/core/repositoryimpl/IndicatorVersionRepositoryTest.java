package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.gobcan.istac.indicators.core.domain.IndicatorVersionRepository;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsMocks;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-test.xml"})
@TransactionConfiguration(defaultRollback = true, transactionManager = "txManager")
@Transactional
public class IndicatorVersionRepositoryTest {

    @Autowired
    IndicatorVersionRepository indicatorVersionRepository;

    @Test
    public void testFindIndicatorsVersionLinkedToAnyDataGpeUuids() throws Exception {
        List<String> dataGpeUuids = IndicatorsMocks.mockUuidsList(4);
        indicatorVersionRepository.findIndicatorsVersionLinkedToAnyDataGpeUuids(dataGpeUuids);
    }

    @Test
    public void testFindIndicatorsVersionLinkedToAnyDataGpeUuidsWith1000Elements() throws Exception {
        List<String> dataGpeUuids = IndicatorsMocks.mockUuidsList(1000);
        indicatorVersionRepository.findIndicatorsVersionLinkedToAnyDataGpeUuids(dataGpeUuids);
    }

    @Test
    public void testFindIndicatorsVersionLinkedToAnyDataGpeUuidsWithMoreThan1000Elements() throws Exception {
        List<String> dataGpeUuids = IndicatorsMocks.mockUuidsList(1001);
        indicatorVersionRepository.findIndicatorsVersionLinkedToAnyDataGpeUuids(dataGpeUuids);
    }
}
