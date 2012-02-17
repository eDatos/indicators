package es.gobcan.istac.indicators.core.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Instance of indicator
 */
@Entity
@Table(name = "TBL_INDICATOR_INSTANCES", uniqueConstraints = {@UniqueConstraint(columnNames = {"INDICATOR_UUID", "INDICATORS_SYSTEM_VERSION_FK", "DIMENSION_FK"})})
public class IndicatorInstance extends IndicatorInstanceBase {
    private static final long serialVersionUID = 1L;

    public IndicatorInstance() {
    }

    public IndicatorInstance(IndicatorsSystemVersion indicatorsSystemVersion) {
        super(indicatorsSystemVersion);
    }
}
