package es.gobcan.istac.indicators.core.util;

import es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum;

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
public class IndicatorUtils {

    public static Boolean isQuantityOrExtension(QuantityTypeEnum type) {
        return QuantityTypeEnum.QUANTITY.equals(type);
    }

    public static Boolean isAmountOrExtension(QuantityTypeEnum type) {
        return QuantityTypeEnum.AMOUNT.equals(type);
    }

    public static Boolean isMagnitudeOrExtension(QuantityTypeEnum type) {
        return QuantityTypeEnum.MAGNITUDE.equals(type) || isFractionOrExtension(type);
    }

    public static Boolean isFractionOrExtension(QuantityTypeEnum type) {
        return QuantityTypeEnum.FRACTION.equals(type) || isRatioOrExtension(type);
    }

    public static Boolean isRatioOrExtension(QuantityTypeEnum type) {
        return QuantityTypeEnum.RATIO.equals(type) || isRateOrExtension(type);
    }

    public static Boolean isRateOrExtension(QuantityTypeEnum type) {
        return QuantityTypeEnum.RATE.equals(type) || isChangeRateOrExtension(type) || isIndexOrExtension(type);
    }

    public static Boolean isIndexOrExtension(QuantityTypeEnum type) {
        return QuantityTypeEnum.INDEX.equals(type);
    }

    public static Boolean isChangeRateOrExtension(QuantityTypeEnum type) {
        return QuantityTypeEnum.CHANGE_RATE.equals(type);
    }

    public static Boolean isIndicatorNeedsBePopulated(String dataRepositoryId, Boolean needsUpdate, Boolean inconsistentData) {
        boolean recentlyCreated = dataRepositoryId == null;
        boolean updateHasFailed = needsUpdate;
        boolean modifiedAfterPopulate = inconsistentData;
        return recentlyCreated || updateHasFailed || modifiedAfterPopulate;
    }
}
