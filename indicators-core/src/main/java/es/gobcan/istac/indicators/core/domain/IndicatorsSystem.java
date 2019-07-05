package es.gobcan.istac.indicators.core.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import es.gobcan.istac.indicators.core.util.IndicatorsVersionUtils;

/**
 * Indicators system entity
 */
@Entity
@Table(name = "TB_INDICATORS_SYSTEMS", uniqueConstraints = {@UniqueConstraint(columnNames = {"CODE"})})
public class IndicatorsSystem extends IndicatorsSystemBase {

    private static final long serialVersionUID = 1L;

    public IndicatorsSystem() {
    }

    public IndicatorsSystemVersion getProductionIndicatorsSystemVersion() {
        return getIndicatorsSystemVersion(getProductionVersion().getVersionNumber());
    }

    public IndicatorsSystemVersion getDiffusionIndicatorsSystemVersion() {
        return getIndicatorsSystemVersion(getDiffusionVersion().getVersionNumber());
    }

    private IndicatorsSystemVersion getIndicatorsSystemVersion(String indicatorSystemVersionNumber) {
        if (indicatorSystemVersionNumber != null) {
            for (IndicatorsSystemVersion indicatorsSystemVersion : getVersions()) {
                if (IndicatorsVersionUtils.equalsVersionNumber(indicatorSystemVersionNumber, indicatorsSystemVersion.getVersionNumber())) {
                    return indicatorsSystemVersion;
                }
            }
        }
        return null;
    }
}
