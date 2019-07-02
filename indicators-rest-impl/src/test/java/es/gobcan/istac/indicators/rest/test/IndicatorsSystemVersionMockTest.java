package es.gobcan.istac.indicators.rest.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.siemac.metamac.core.common.util.shared.StringUtils;

import es.gobcan.istac.indicators.core.util.IndicatorsVersionUtils;
import es.gobcan.istac.indicators.rest.test.mocks.IndicatorsSystemVersionMock;

public class IndicatorsSystemVersionMockTest {

    /*
     * This test tries to ensure that the version constants used in tests have the same format than the version constant used in the application. If the format of this constant changes, the test fails
     * and it will be necessary to change the format of the test constants
     */
    @Test
    public void testInitialVersion() {
        assertTrue(StringUtils.equals(IndicatorsSystemVersionMock.INIT_VERSION, IndicatorsVersionUtils.INITIAL_VERSION));
    }

}
