package es.gobcan.istac.indicators.core.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Table values for geographical values
 */
@Entity
@Table(name = "TB_LIS_GEOGR_VALUES", uniqueConstraints = {@UniqueConstraint(columnNames = {"CODE"})})
public class GeographicalValue extends GeographicalValueBase {
    private static final long serialVersionUID = 1L;

    protected GeographicalValue() {
    }
}
