package es.gobcan.istac.indicators.core.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Table values for geographic values
 */
@Entity
@Table(name = "LIS_GEOGR_VALUES", uniqueConstraints = {@UniqueConstraint(columnNames = {"CODE"})})
public class GeographicValue extends GeographicValueBase {
    private static final long serialVersionUID = 1L;

    protected GeographicValue() {
    }
}
