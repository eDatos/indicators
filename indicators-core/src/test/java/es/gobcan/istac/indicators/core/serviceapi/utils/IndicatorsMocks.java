package es.gobcan.istac.indicators.core.serviceapi.utils;

import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.core.common.dto.serviceapi.InternationalStringDto;

/**
 * Mocks
 */
public class IndicatorsMocks {

    /**
     * Mock a simple String
     */
    public static String mockString(int count) {
        return MetamacMocks.mockString(count);
    }
    
    /**
     * Mock a InternationalString with locales "en" and "es"
     */
    public static InternationalStringDto mockInternationalString() {
        return MetamacMocks.mockInternationalString();
    }
}
