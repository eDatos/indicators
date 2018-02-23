package es.gobcan.istac.indicators.rest.types;

import java.io.Serializable;

/**
 * Enum for IndicatorDataDimensionTypeEnum
 * 
 * @see es.gobcan.istac.indicators.core.enume.domain.IndicatorDataDimensionTypeEnum
 */
public enum IndicatorDataDimensionTypeEnum implements Serializable {
    GEOGRAPHICAL, TIME, MEASURE;

    private IndicatorDataDimensionTypeEnum() {
    }

    public String getName() {
        return name();
    }
}
