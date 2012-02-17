package es.gobcan.istac.indicators.core.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Dimension of indicators system
 */
@Entity
@Table(name = "TBL_DIMENSIONS", uniqueConstraints = {@UniqueConstraint(columnNames = {"ORDER_IN_LEVEL", "INDICATORS_SYSTEM_VERSION_FK", "PARENT_FK"})})
public class Dimension extends DimensionBase {
    private static final long serialVersionUID = 1L;

    public Dimension() {
    }
}
