package es.gobcan.istac.indicators.core.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Indicator entity
 */
@Entity
@Table(name = "TB_INDICATORS", uniqueConstraints = {@UniqueConstraint(columnNames = {"CODE"}), @UniqueConstraint(columnNames = {"VIEW_CODE"})})
public class Indicator extends IndicatorBase {

    private static final long serialVersionUID = 1L;

    public Indicator() {
    }
    
    // IDEA: Would be faster as repo queries?
    public IndicatorVersion getProductionIndicatorVersion() {
        return getIndicatorVersion(getProductionVersionNumber());
    }
    
    public IndicatorVersion getDiffusionIndicatorVersion() {
        return getIndicatorVersion(getDiffusionVersionNumber());
    }
    
    private IndicatorVersion getIndicatorVersion(String indicatorVersionNumber) {
        if (indicatorVersionNumber != null) {
            for (IndicatorVersion indicatorVersion : getVersions()) {
                if (indicatorVersionNumber.equals(indicatorVersion.getVersionNumber())) {
                    return indicatorVersion;
                }
            }
        }
        return null;        
    }
}