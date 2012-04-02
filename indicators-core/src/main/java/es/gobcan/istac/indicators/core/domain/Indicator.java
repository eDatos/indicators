package es.gobcan.istac.indicators.core.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Indicator entity
 */
@Entity
@Table(name = "TB_INDICATORS", uniqueConstraints = {@UniqueConstraint(columnNames = {"CODE"})})
public class Indicator extends IndicatorBase {
    private static final long serialVersionUID = 1L;

    public Indicator() {
    }
}
