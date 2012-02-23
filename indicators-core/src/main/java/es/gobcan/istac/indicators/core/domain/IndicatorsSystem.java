package es.gobcan.istac.indicators.core.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Indicators system entity
 */
@Entity
@Table(name = "TBL_INDICATORS_SYSTEMS", uniqueConstraints = {@UniqueConstraint(columnNames = {"CODE"})})
public class IndicatorsSystem extends IndicatorsSystemBase {
    private static final long serialVersionUID = 1L;

    public IndicatorsSystem() {
    }
}
