package es.gobcan.istac.indicators.rest.test.mocks;

import java.util.HashMap;
import java.util.Map;

import es.gobcan.istac.indicators.core.domain.IndicatorInstance;

public class IndicatorInstanceMock {

    private static Map<String, IndicatorInstance> INSTANCES            = new HashMap<String, IndicatorInstance>();
    private static String                         INDICATOR_INSTANCE_1 = "IndicatorInstance1";

    public static IndicatorInstance mockIndicatorInstance1() {
        IndicatorInstance indicatorInstance  = INSTANCES.get(INDICATOR_INSTANCE_1);
        if (indicatorInstance != null) {
            return indicatorInstance;
        }
        indicatorInstance = new IndicatorInstance();
        INSTANCES.put(INDICATOR_INSTANCE_1, indicatorInstance);
        
        indicatorInstance.setTitle(MockUtil.createInternationalStringDomain());        
        return indicatorInstance;
    }
}
