package es.gobcan.istac.indicators.core.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Version of indicator entity
 */
@Entity
@Table(name = "TB_INDICATORS_VERSIONS", uniqueConstraints = {@UniqueConstraint(columnNames = {"DATA_REPOSITORY_ID"}), @UniqueConstraint(columnNames = {"DATA_REPOSITORY_TABLE_NAME"})})
public class IndicatorVersion extends IndicatorVersionBase {

    private static final long serialVersionUID = 1L;

    public IndicatorVersion() {
    }

}
