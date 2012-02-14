package es.gobcan.istac.indicators.core.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Version of indicator entity
 */
@Entity
@Table(name = "TBL_INDICATOR_VERSIONS")
public class IndicatorVersion extends IndicatorVersionBase {
    private static final long serialVersionUID = 1L;

    public IndicatorVersion() {
    }
}
