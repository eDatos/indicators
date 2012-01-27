package es.gobcan.istac.indicators.core.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Indicator system entity
 */
@Entity
@Table(name = "TBL_INDICATORS_SYSTEMS", uniqueConstraints = {@UniqueConstraint(columnNames = {"CODE"})})
public class IndicatorSystem extends IndicatorSystemBase {
    private static final long serialVersionUID = 1L;

    public IndicatorSystem() {
    }

    public IndicatorSystem(String code) {
        super(code);
    }
}
