package es.gobcan.istac.indicators.core.dto;

/**
 * Dto for element of level. Can be a dimension or an indicator instance
 */
public class ElementLevelDto extends ElementLevelDtoBase {

    private static final long serialVersionUID = 1L;

    public ElementLevelDto() {
    }

    /**
     * @return True when element is a dimension
     */
    public Boolean isElementTypeDimension() {
        return getDimension() != null;
    }

    /**
     * @return True when element is an indicator instance
     */
    public Boolean isElementTypeIndicatorInstance() {
        return getIndicatorInstance() != null;
    }

    /**
     * Order in level of element
     */
    public Long getOrderInLevel() {
        if (isElementTypeDimension()) {
            return getDimension().getOrderInLevel();
        } else if (isElementTypeIndicatorInstance()) {
            return getIndicatorInstance().getOrderInLevel();
        }
        return null;
    }

    /**
     * Parent dimension of element
     */
    public String getParentUuid() {
        if (isElementTypeDimension()) {
            return getDimension().getParentUuid();
        } else if (isElementTypeIndicatorInstance()) {
            return getIndicatorInstance().getParentUuid();
        }
        return null;
    }
}
