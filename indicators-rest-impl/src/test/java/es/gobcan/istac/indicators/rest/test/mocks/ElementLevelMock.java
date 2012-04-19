package es.gobcan.istac.indicators.rest.test.mocks;

import java.util.HashMap;
import java.util.Map;

import es.gobcan.istac.indicators.core.domain.Dimension;
import es.gobcan.istac.indicators.core.domain.ElementLevel;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;

public class ElementLevelMock {

    private static Map<String, ElementLevel> INSTANCES                   = new HashMap<String, ElementLevel>();
    private static String                    INDICATORSYSTEM_1_LEVEL_1   = "indicatorSystem_1_level_1";
    private static String                    INDICATORSYSTEM_1_LEVEL_1_1 = "indicatorSystem_1_level_1_1";

    public static ElementLevel mockElementLevel_indicatorSystem_1_level_1() {
        ElementLevel elementLevel = INSTANCES.get(INDICATORSYSTEM_1_LEVEL_1);
        if (elementLevel != null) {
            return elementLevel;
        }

        elementLevel = new ElementLevel();
        INSTANCES.put(INDICATORSYSTEM_1_LEVEL_1, elementLevel);
        Dimension dimension = DimensionMock.mockDimension1();
        elementLevel.setDimension(dimension);
        elementLevel.setIndicatorsSystemVersion(IndicatorsSystemVersionMock.mockIndicatorsSystemVersion1());
        elementLevel.addChildren(ElementLevelMock.mockElementLevel_indicatorSystem_1_level_1_1());
        return elementLevel;
    }

    public static ElementLevel mockElementLevel_indicatorSystem_1_level_1_1() {
        ElementLevel elementLevel = INSTANCES.get(INDICATORSYSTEM_1_LEVEL_1_1);
        if (elementLevel != null) {
            return elementLevel;
        }

        elementLevel = new ElementLevel();
        INSTANCES.put(INDICATORSYSTEM_1_LEVEL_1_1, elementLevel);

        IndicatorInstance indicatorInstance = IndicatorInstanceMock.mockIndicatorInstance1();
        elementLevel.setIndicatorsSystemVersion(IndicatorsSystemVersionMock.mockIndicatorsSystemVersion1());
        elementLevel.setIndicatorInstance(indicatorInstance);
        return elementLevel;

    }

}
