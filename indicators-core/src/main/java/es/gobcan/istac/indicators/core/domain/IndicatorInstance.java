package es.gobcan.istac.indicators.core.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Instance of indicator
 */
@Entity
@Table(name = "TBL_INDICATOR_INSTANCES")
public class IndicatorInstance extends IndicatorInstanceBase {
    private static final long serialVersionUID = 1L;

    public IndicatorInstance() {
    }
}
