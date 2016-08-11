package es.gobcan.istac.indicators.core.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

/**
 * Instance of indicator
 */
@Entity
@Table(name = "TB_INDICATORS_INSTANCES")
public class IndicatorInstance extends IndicatorInstanceBase {
    private static final long serialVersionUID = 1L;

    public IndicatorInstance() {
    }
    
    public void setTimeValuesAsList(List<String> timeValuesList) {
        if (timeValuesList != null && timeValuesList.size() > 0) {
            setTimeValues(StringUtils.join(timeValuesList, "#"));
        } else {
            setTimeValues(null);
        }
    }
    
    public List<String> getTimeValuesAsList() {
        List<String> timeValuesList = new ArrayList<String>();
        if (!StringUtils.isEmpty(getTimeValues())) {
            for(String timeValueStr : getTimeValues().split("#")) {
                timeValuesList.add(timeValueStr);
            }
        }
        return timeValuesList;
    }

    public boolean isFilteredByGeographicalValues() {
        return getGeographicalValues() != null && getGeographicalValues().size() > 0;
    }

    public boolean isFilteredByGeographicalGranularity() {
        return getGeographicalGranularity() != null;
    }

    public boolean isFilteredByTimeValues() {
        return !StringUtils.isEmpty(getTimeValues());
    }

    public boolean isFilteredByTimeGranularity() {
        return getTimeGranularity() != null;
    }

}
