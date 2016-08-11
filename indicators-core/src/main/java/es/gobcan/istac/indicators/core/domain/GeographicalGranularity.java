package es.gobcan.istac.indicators.core.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Table values for geographical granularities
 */
@Entity
@Table(name = "TB_LIS_GEOGR_GRANULARITIES", uniqueConstraints = {@UniqueConstraint(columnNames = {"CODE"})})
public class GeographicalGranularity extends GeographicalGranularityBase {
    private static final long serialVersionUID = 1L;

    public GeographicalGranularity() {
    }
}
