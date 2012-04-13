package es.gobcan.istac.indicators.core.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

import es.gobcan.istac.indicators.core.dto.DataSourceDto;

/**
 * Metric characteristics of a data source of indicator
 */
@Entity
@Table(name = "TB_RATES_DERIVATIONS")
public class RateDerivation extends RateDerivationBase {
    private static final long serialVersionUID = 1L;

    public RateDerivation() {
    }
    
    public boolean isMethodObsValue() {
        return DataSourceDto.OBS_VALUE.equals(getMethod());
    }
    
    public void setMethodObsValue() {
        setMethod(DataSourceDto.OBS_VALUE);
    }
}
