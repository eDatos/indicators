package es.gobcan.istac.indicators.core.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Version of indicator system entity
 */
@Entity
@Table(name = "TBL_INDICATOR_SYSTEM_VERS")
public class IndicatorSystemVersion extends IndicatorSystemVersionBase {
    private static final long serialVersionUID = 1L;

    public IndicatorSystemVersion() {
    }
}
