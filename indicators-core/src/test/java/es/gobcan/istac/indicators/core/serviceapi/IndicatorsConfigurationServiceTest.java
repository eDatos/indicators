package es.gobcan.istac.indicators.core.serviceapi;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Spring based transactional test with DbUnit support.
 */
public class IndicatorsConfigurationServiceTest implements IndicatorsConfigurationServiceTestBase {

    @Autowired
    protected IndicatorsConfigurationService indicatorsConfigurationService;

    @Override
    @Test
    public void testRetrieveLastSuccessfulGpeQueryDate() throws Exception {
    }

    @Override
    @Test
    public void testSetLastSuccessfulGpeQueryDate() throws Exception {
    }

}
