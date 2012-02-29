package es.gobcan.istac.indicators.core.dto.serviceapi;

import es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum;
import es.gobcan.istac.indicators.core.serviceimpl.util.IndicatorUtils;

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
        return QuantityTypeEnum.QUANTITY.equals(type);
    }

    public Boolean isAmountOrExtension(QuantityTypeEnum type) {
        return QuantityTypeEnum.AMOUNT.equals(type);
    }
    
    public Boolean isMagnituteOrExtension() {
        return IndicatorUtils.isMagnituteOrExtension(getType());
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
