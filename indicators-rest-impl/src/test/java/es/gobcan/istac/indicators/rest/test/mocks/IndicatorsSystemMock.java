package es.gobcan.istac.indicators.rest.test.mocks;

import java.util.HashMap;
import java.util.Map;

import es.gobcan.istac.indicators.core.domain.IndicatorsSystem;

public class IndicatorsSystemMock {

    private static Map<String, IndicatorsSystem> INSTANCES = new HashMap<String, IndicatorsSystem>();

    public static IndicatorsSystem mockIndicatorsSystem1() {
        IndicatorsSystem indicatorsSystem = INSTANCES.get("IndicatorsSystem_1");
        if (indicatorsSystem != null) {
            return indicatorsSystem;
        }

        indicatorsSystem = new IndicatorsSystem();
        indicatorsSystem.setCode("CODIGO_0001");
        return indicatorsSystem;
    }

    public static IndicatorsSystem mockIndicatorsSystem2() {
        IndicatorsSystem indicatorsSystem = INSTANCES.get("IndicatorsSystem_2");
        if (indicatorsSystem != null) {
            return indicatorsSystem;
        }

        indicatorsSystem = new IndicatorsSystem();
        indicatorsSystem.setCode("COD_00002");
        return indicatorsSystem;
    }
}
