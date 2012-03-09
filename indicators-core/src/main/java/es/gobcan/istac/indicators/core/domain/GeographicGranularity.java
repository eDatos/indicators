package es.gobcan.istac.indicators.core.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Table values for geographic granularities
 */
@Entity
@Table(name = "LIS_GEOGR_GRANULARITIES", uniqueConstraints = {@UniqueConstraint(columnNames = {"CODE"})})
public class GeographicGranularity extends GeographicGranularityBase {
    private static final long serialVersionUID = 1L;

    public GeographicGranularity() {
    }
}
