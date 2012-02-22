package es.gobcan.istac.indicators.core.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Dimension of indicators system
 */
@Entity
@Table(name = "TBL_DIMENSIONS")
public class Dimension extends DimensionBase {
    private static final long serialVersionUID = 1L;

    public Dimension() {
    }
}
