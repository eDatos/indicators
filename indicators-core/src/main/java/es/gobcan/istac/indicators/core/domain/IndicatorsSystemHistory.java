package es.gobcan.istac.indicators.core.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.joda.time.DateTime;

/**
 * Indicators system entity
 */
@Entity
@Table(name = "TB_INDICATORS_SYSTEMS_HIST", uniqueConstraints = @UniqueConstraint(columnNames =  {
    "VERSION_NUMBER", "INDICATORS_SYSTEM_FK"}
)
)
public class IndicatorsSystemHistory extends IndicatorsSystemHistoryBase {
    private static final long serialVersionUID = 1L;

    protected IndicatorsSystemHistory() {
    }

    public IndicatorsSystemHistory(String versionNumber, IndicatorsSystem indicatorsSystem, DateTime publicationDate) {
        super(versionNumber, indicatorsSystem);
        setPublicationDate(publicationDate);
    }
    
    
}
