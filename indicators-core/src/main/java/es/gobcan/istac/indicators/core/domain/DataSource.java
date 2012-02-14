package es.gobcan.istac.indicators.core.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Data source of indicator
 */
@Entity
@Table(name = "TBL_DATA_SOURCES")
public class DataSource extends DataSourceBase {

    private static final long serialVersionUID = 1L;

    public DataSource() {
    }

    public DataSource(String queryGpe, String px, String temporaryVariable, String geographicVariable) {
        super(queryGpe, px, temporaryVariable, geographicVariable);
    }
}
