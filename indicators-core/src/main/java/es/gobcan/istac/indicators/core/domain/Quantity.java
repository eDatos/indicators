package es.gobcan.istac.indicators.core.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Metric characteristics of the indicator
 */
@Entity
@Table(name = "TB_QUANTITIES")
public class Quantity extends QuantityBase {
    private static final long serialVersionUID = 1L;

    public Quantity() {
    }
}
