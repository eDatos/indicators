package es.gobcan.istac.indicators.core.serviceapi;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Spring based transactional test with DbUnit support.
 * This class depends on a Jaxi instance
 */
public class IndicatorsDataProviderServiceTest implements IndicatorsDataProviderServiceTestBase {

    @Autowired
    protected IndicatorsDataProviderService indicatorsDataProviderService;

    @Override
    @Test
    public void testRetrieveDataStructureJson() throws Exception {
        // IDEA: research about mocking HttpRequest -> Zeben has used a LocalHttpServer
        // in IbestatDroidEmbeddedTest in stat4-you crawler project
    }

    @Override
    @Test
    public void testRetrieveDataJson() throws Exception {
        // IDEA: research about mocking HttpRequest
    }

    @Override
    public void testRetrieveDataStructureJsonStat() throws Exception {
        // TODO Auto-generated method stub

    }

}
