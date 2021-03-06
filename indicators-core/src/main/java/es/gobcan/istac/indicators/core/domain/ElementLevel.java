package es.gobcan.istac.indicators.core.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Level at indicators system
 */
@Entity
@Table(name = "TB_ELEMENTS_LEVELS", uniqueConstraints = {@UniqueConstraint(columnNames = {"ORDER_IN_LEVEL", "IND_SYSTEM_VERSION_FIRST_FK", "PARENT_FK"})})
public class ElementLevel extends ElementLevelBase {

    private static final long serialVersionUID = 1L;

    public ElementLevel() {
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
     * If exists, retrieves uuid of parent dimension
     */
    public String getParentUuid() {
        return super.getParent() != null ? super.getParent().getDimension().getUuid() : null;
    }

    public String getElementUuid() {
        if (this.getDimension() != null) {
            return this.getDimension().getUuid();
        } else if (this.getIndicatorInstance() != null) {
            return this.getIndicatorInstance().getUuid();
        }
        return null;
    }
}
