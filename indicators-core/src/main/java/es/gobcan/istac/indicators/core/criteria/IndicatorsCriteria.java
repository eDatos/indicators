package es.gobcan.istac.indicators.core.criteria;

import java.io.Serializable;

public final class IndicatorsCriteria implements Serializable {

    private static final long             serialVersionUID = 1L;

    private IndicatorsCriteriaConjunctionRestriction conjunctionRestriction;
    
    public IndicatorsCriteriaConjunctionRestriction getConjunctionRestriction() {
        return conjunctionRestriction;
    }
    
    public void setConjunctionRestriction(IndicatorsCriteriaConjunctionRestriction conjunctionRestriction) {
        this.conjunctionRestriction = conjunctionRestriction;
    }
}