package es.gobcan.istac.indicators.core.dto;

import es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum;
import es.gobcan.istac.indicators.core.util.shared.IndicatorUtils;

/**
 * Dto for quantity
 * Quantity: Quantity
 * Amount: Quantity
 * Magnitude: Quantity
 * Fraction: extends Magnitude
 * Ratio: extends Fraction
 * Rate: extends Ratio
 * Index: extends Rate
 * Change Rate: extends Rate
 */
public class QuantityDto extends QuantityDtoBase {

    private static final long serialVersionUID = 1L;

    public QuantityDto() {
        // default values
        setUnitMultiplier(Integer.valueOf(1));
    }

    public Boolean isQuantityOrExtension(QuantityTypeEnum type) {
        return IndicatorUtils.isQuantityOrExtension(getType());
    }

    public Boolean isAmountOrExtension(QuantityTypeEnum type) {
        return IndicatorUtils.isAmountOrExtension(getType());
    }
    
    public Boolean isMagnituteOrExtension() {
        return IndicatorUtils.isMagnitudeOrExtension(getType());
    }
    
    public Boolean isFractionOrExtension() {
        return IndicatorUtils.isFractionOrExtension(getType());
    }

    public Boolean isRatioOrExtension() {
        return IndicatorUtils.isRatioOrExtension(getType());
    }

    public Boolean isRateOrExtension() {
        return IndicatorUtils.isRateOrExtension(getType());
    }

    public Boolean isIndexOrExtension() {
        return IndicatorUtils.isIndexOrExtension(getType());
    }

    public Boolean isChangeRateOrExtension() {
        return IndicatorUtils.isChangeRateOrExtension(getType());
    }
}
