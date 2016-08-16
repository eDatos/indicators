package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.gobcan.istac.indicators.core.domain.IndicatorRepository;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsMocks;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-test.xml"})
@TransactionConfiguration(defaultRollback = true, transactionManager = "txManager")
@Transactional
public class IndicatorRepositoryTest {

    @Autowired
    IndicatorRepository indicatorRepository;

    @Test
    public void testFilterIndicatorsNotPublished() throws Exception {
        List<String> indicatorsUuid = IndicatorsMocks.mockUuidsList(4);
        indicatorRepository.filterIndicatorsNotPublished(indicatorsUuid);
    }

    @Test
    public void testFilterIndicatorsNotPublishedWith1000Elements() throws Exception {
        List<String> indicatorsUuid = IndicatorsMocks.mockUuidsList(1000);
        indicatorRepository.filterIndicatorsNotPublished(indicatorsUuid);
    }

    @Test
    public void testFilterIndicatorsNotPublishedWithMoreThan1000Elements() throws Exception {
        List<String> indicatorsUuid = IndicatorsMocks.mockUuidsList(1100);
        indicatorRepository.filterIndicatorsNotPublished(indicatorsUuid);
    }
}
