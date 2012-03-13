package es.gobcan.istac.indicators.core.criteria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Restriction of the type "AND"
 */
public class IndicatorsCriteriaConjunctionRestriction implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private List<IndicatorsCriteriaPropertyRestriction> restrictions = new ArrayList<IndicatorsCriteriaPropertyRestriction>();

    public List<IndicatorsCriteriaPropertyRestriction> getRestrictions() {
        return restrictions;
    }
}