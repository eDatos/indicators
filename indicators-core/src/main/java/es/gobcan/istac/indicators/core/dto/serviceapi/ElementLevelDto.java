package es.gobcan.istac.indicators.core.dto.serviceapi;

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
    public Boolean isDimension() {
        return getDimension() != null;
    }

    /**
     * @return True when element is an indicator instance
     */
    public Boolean isIndicatorInstance() {
        return getIndicatorInstance() != null;
    }

    /**
     * Order in level of element
     */
    public Long getOrderInLevel() {
        if (isDimension()) {
            return getDimension().getOrderInLevel();
        } else if (isIndicatorInstance()) {
            return getIndicatorInstance().getOrderInLevel();
        }
        return null;
    }

    /**
     * Parent dimension of element
     */
    public String getParentUuid() {
        if (isDimension()) {
            return getDimension().getParentUuid();
        } else if (isIndicatorInstance()) {
            return getIndicatorInstance().getParentUuid();
        }
        return null;
    }
}
