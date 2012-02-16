package es.gobcan.istac.indicators.core.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Variable of data source
 */
@Entity
@Table(name = "TBL_DATA_SOURCE_VARIABLES")
public class DataSourceVariable extends DataSourceVariableBase {
    private static final long serialVersionUID = 1L;

    protected DataSourceVariable() {
    }

    public DataSourceVariable(String variable, String category) {
        super(variable, category);
    }
}
