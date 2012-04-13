package es.gobcan.istac.indicators.core.serviceapi;

import org.fornax.cartridges.sculptor.framework.test.AbstractDbUnitJpaTests;
import org.junit.Ignore;
import org.junit.Test;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Spring based transactional test with DbUnit support.
 * 
 * This class depends on a Jaxi instance
 */
public class IndicatorsDataProviderServiceTest extends AbstractDbUnitJpaTests implements IndicatorsDataProviderServiceTestBase {
    
    @Autowired
    protected IndicatorsDataProviderService indicatorsDataProviderService;

    @Test
    @Ignore
    public void testRetrieveDataStructureJson() throws Exception {
        //TODO: research about mocking HttpRequest -> Zeben has used a LocalHttpServer
        //in IbestatDroidEmbeddedTest in stat4-you crawler project
    }

    @Test
    @Ignore
    public void testRetrieveDataJson() throws Exception {
        //TODO: research about mocking HttpRequest
    }
}
