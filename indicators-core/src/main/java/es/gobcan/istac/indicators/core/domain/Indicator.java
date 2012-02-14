package es.gobcan.istac.indicators.core.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Indicator entity
 * TODO  uniqueConstraints = {@UniqueConstraint(columnNames = {"CODE"})})
 */
@Entity
@Table(name = "TBL_INDICATORS")
public class Indicator extends IndicatorBase {
    private static final long serialVersionUID = 1L;

    public Indicator() {
    }
}
