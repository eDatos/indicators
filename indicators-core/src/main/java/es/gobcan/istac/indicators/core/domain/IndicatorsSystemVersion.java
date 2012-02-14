package es.gobcan.istac.indicators.core.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Version of indicators system entity
 */
@Entity
@Table(name = "TBL_INDIC_SYSTEMS_VERSIONS")
public class IndicatorsSystemVersion extends IndicatorsSystemVersionBase {
    private static final long serialVersionUID = 1L;

    public IndicatorsSystemVersion() {
    }
}
