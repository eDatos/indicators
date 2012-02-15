package es.gobcan.istac.indicators.core.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Variable of data source
 */
@Entity
@Table(name = "TBL_DATA_SOURCE_VARIABLES")
public class DatasourceVariable extends DatasourceVariableBase {
    private static final long serialVersionUID = 1L;

    protected DatasourceVariable() {
    }

    public DatasourceVariable(String variable, String category) {
        super(variable, category);
    }
}
