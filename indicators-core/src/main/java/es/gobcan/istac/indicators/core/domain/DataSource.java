package es.gobcan.istac.indicators.core.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

import es.gobcan.istac.indicators.core.dto.DataSourceDto;

/**
 * Data source of indicator
 */
@Entity
@Table(name = "TB_DATA_SOURCES")
public class DataSource extends DataSourceBase {
    private static final long serialVersionUID = 1L;
    
    public DataSource() {
    }
    
    public boolean isAbsoluteMethodObsValue() {
        return DataSourceDto.OBS_VALUE.equals(getAbsoluteMethod());
    }
    
    public void establishAbsoluteMethodAsObsValue() {
        setAbsoluteMethod(DataSourceDto.OBS_VALUE);
    }
}
